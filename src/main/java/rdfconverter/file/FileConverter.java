/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.file;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdfconverter.datatypes.DatatypeController;
import static rdfconverter.datatypes.DatatypeController.HEADER_ITEM_SEPARATOR;
import static rdfconverter.datatypes.DatatypeController.HTTP_PREFIX;
import rdfconverter.datatypes.PrefixController;

/**
 * Read a CSV file of triples.
 *
 * First column is subject and subsequent columns are objects.
 *
 * Header row provides 1) datatype and 2) property in each column (space
 * separated by default). e.g. "INTEGER http://example.org/tom/schema#age"
 *
 * Datatype can be upper or lower case and optional (assumed to be a Resource if
 * datatype is absent).
 *
 * Header row first column specifies the BASE URI of the document and the type
 * of the subject. e.g. "http://example.org/tom/my-data#
 * http://example.org/tom/schema#Person"
 *
 * BASE URI is prefixed for any property or resource which does not have
 * "http://" at start.
 *
 * Empty fields will be ignored so that irregular rows can be used.
 *
 * @author Greg Albiston
 */
public class FileConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Resource NAMED_INDIVIDUAL = ResourceFactory.createResource(OWL.NS + "NamedIndividual");

    private static final String CLASS_CHARACTER = ":";

    public static final void writeToModel(File inputFile, Model model, HashMap<String, String> prefixMap) {
        writeToModel(inputFile, model, prefixMap, ',');
    }

    public static final void writeToModel(File inputFile, Model model, HashMap<String, String> prefixMap, char separator) {

        LOGGER.info("File Conversion Started: {}", inputFile.getPath());

        HashMap<Integer, String> datatypeURIs = new HashMap<>();
        HashMap<Integer, Property> propertyURIs = new HashMap<>();
        HashMap<Integer, Resource> classURIs = new HashMap<>();
        List<Integer> targetColumns = new ArrayList<>();
        int lineNumber = 1;

        try (CSVReader reader = new CSVReader(new FileReader(inputFile), separator)) {

            String baseURI = readHeader(reader.readNext(), datatypeURIs, propertyURIs, classURIs, targetColumns);

            String[] line;
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                readData(line, baseURI, datatypeURIs, propertyURIs, classURIs, targetColumns, model);
            }
            model.setNsPrefixes(prefixMap);

        } catch (IOException | RuntimeException ex) {

            LOGGER.error("FileConverter: Line - {}, File - {}, Exception - {}", lineNumber, inputFile.getAbsolutePath(), ex.getMessage());
        }

        LOGGER.info("File Conversion Completed: {}", inputFile.getPath());
    }

    private static String readHeader(String[] headerLine, HashMap<Integer, String> datatypeURIs, HashMap<Integer, Property> propertyURIs, HashMap<Integer, Resource> classURIs, List<Integer> targetColumns) {

        String baseURI = "";

        for (int i = 0; i < headerLine.length; i++) {
            String header = headerLine[i];
            String[] parts = header.split(HEADER_ITEM_SEPARATOR);

            //Extract datatype and propertyURI from header field.
            String datatypeLabel = null;
            String propertyLabel = parts[0];
            String classLabel = null;
            Integer targetColumn = 0;

            switch (parts.length) {
                case 1:
                    datatypeLabel = "string";
                    break;
                case 2:
                    //First column is always a resource so datatype specifies the BASE URI.
                    if (i == 0) {
                        baseURI = parts[0];
                    }
                    if (integerCheck(parts[1])) {
                        targetColumn = Integer.parseInt(parts[1]);
                    } else {

                        if (parts[1].startsWith(CLASS_CHARACTER)) {
                            classLabel = parts[1].substring(1);
                        } else {
                            datatypeLabel = parts[1];
                        }
                    }
                    break;
                case 3:
                    if (parts[1].startsWith(CLASS_CHARACTER)) {
                        classLabel = parts[1].substring(1);
                    } else {
                        datatypeLabel = parts[1];
                    }

                    if (parts[2].startsWith(CLASS_CHARACTER)) {
                        classLabel = parts[2].substring(1);
                    } else {
                        targetColumn = Integer.parseInt(parts[2]);
                    }
                    break;
                default:
                    datatypeLabel = parts[1];
                    targetColumn = Integer.parseInt(parts[2]);
                    classLabel = parts[3];
            }

            //Record the target column
            targetColumns.add(targetColumn);

            if (i > 0) {
                createDatatype(i, datatypeLabel, baseURI, datatypeURIs);
                createProperty(i, propertyLabel, baseURI, propertyURIs);
            }

            createClass(i, classLabel, baseURI, classURIs);
        }
        return baseURI;
    }

    private static void createProperty(Integer index, String propertyLabel, String baseURI, HashMap<Integer, Property> propertyURIs) {
        String uri = PrefixController.lookupURI(propertyLabel, baseURI);
        Property property = ResourceFactory.createProperty(uri);
        propertyURIs.put(index, property);
    }

    private static void createClass(Integer index, String classLabel, String baseURI, HashMap<Integer, Resource> classURIs) {

        if (classLabel != null) {
            String uri = PrefixController.lookupURI(classLabel, baseURI);
            Resource resource = ResourceFactory.createResource(uri);
            classURIs.put(index, resource);
        }
    }

    private static void createDatatype(int index, String datatypeLabel, String baseURI, HashMap<Integer, String> datatypeURIs) {
        if (datatypeLabel != null) {
            datatypeURIs.put(index, DatatypeController.lookupDatatypeURI(datatypeLabel, baseURI));
        }
    }

    private static boolean integerCheck(String checkString) {
        try {
            Integer.parseInt(checkString);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private static void readData(String[] dataLine, String baseURI, HashMap<Integer, String> datatypeURIs, HashMap<Integer, Property> propertyURIs, HashMap<Integer, Resource> classURIs, List<Integer> targetColumns, Model model) {

        //Map of subject encountered in each row.
        HashMap<Integer, Resource> indviduals = new HashMap<>();

        //Find all individuals
        for (Integer index : classURIs.keySet()) {
            //Create subject as individual with specified class.
            String tidyData = dataLine[index].trim();
            Resource subject = createIndividual(tidyData, model, baseURI, classURIs.get(index));
            indviduals.put(index, subject);
        }

        //Extract the objects.
        for (int i = 1; i < dataLine.length; i++) {

            String data = dataLine[i];

            //Skip early on empty or whitespace string.
            if (data.equals("")) {
                continue;
            }

            Property property = propertyURIs.get(i);
            RDFNode object;
            if (datatypeURIs.containsKey(i)) {
                String datatypeURI = datatypeURIs.get(i);
                object = DatatypeController.extractLiteral(data, datatypeURI);
            } else if (indviduals.containsKey(i)) {
                //No datatype so must be an individual.
                object = indviduals.get(i);
            } else {
                LOGGER.error("Cannot find: {} in index: {}. Class URI may be missing from column header.", data, i);
                throw new AssertionError();
            }

            Resource targetSubject = indviduals.get(targetColumns.get(i));
            targetSubject.addProperty(property, object);
        }
    }

    private static Resource createIndividual(String tidyData, Model model, String baseURI, Resource classURI) {
        //Check whether the subject contains an explicit URI already.
        String uri;
        String label;
        if (tidyData.startsWith(HTTP_PREFIX)) {
            uri = tidyData;
            label = tidyData.substring(tidyData.indexOf("#") + 1);
        } else {
            uri = baseURI + tidyData;
            label = tidyData;
        }

        //Create the individual with specified class and owl:namedIndividual class
        Resource subject = model.createResource(uri);
        if (classURI != null) {
            subject.addProperty(RDF.type, classURI);
        }
        subject.addProperty(RDF.type, NAMED_INDIVIDUAL);

        //Apply a label to the subject.
        Literal labelLiteral = model.createLiteral(label);
        subject.addLiteral(RDFS.label, labelLiteral);
        return subject;
    }

}
