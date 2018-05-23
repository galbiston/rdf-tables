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

    private static final char COLUMN_SEPARATOR = ',';
    private static final String PROPERTY_SEPARATOR = " ";
    private static final String HTTP_PREFIX = "http://";
    private static final Resource NAMED_INDIVIDUAL = ResourceFactory.createResource(OWL.NS + "NamedIndividual");

    public static final void writeToModel(File inputFile, Model model, HashMap<String, String> prefixMap) {

        LOGGER.info("File Conversion Started: {}", inputFile.getPath());

        List<String> datatypeURIs = new ArrayList<>();
        List<Property> propertyURIs = new ArrayList<>();
        int lineNumber = 1;

        try (CSVReader reader = new CSVReader(new FileReader(inputFile), COLUMN_SEPARATOR)) {

            String baseURI = readHeader(reader.readNext(), datatypeURIs, propertyURIs);

            String[] line;
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                readData(line, baseURI, datatypeURIs, propertyURIs, model);
            }
            model.setNsPrefixes(prefixMap);

        } catch (IOException | RuntimeException ex) {

            LOGGER.error("FileConverter: Line - {}, File - {}, Exception - {}", lineNumber, inputFile.getAbsolutePath(), ex.getMessage());
        }

        LOGGER.info("File Conversion Completed: {}", inputFile.getPath());
    }

    private static String readHeader(String[] headerLine, List<String> datatypeURIs, List<Property> propertyURIs) {

        String baseURI = "";

        for (int i = 0; i < headerLine.length; i++) {
            String header = headerLine[i];
            String[] parts = header.split(PROPERTY_SEPARATOR);

            //Extract datatype and propertyURI from header field.
            String dataType;
            String propertyURI;
            if (parts.length > 1) {
                dataType = parts[0];
                propertyURI = parts[1].trim();
            } else {       //Default to INDIVIDUAL if not present.
                dataType = "INDIVIDUAL";
                propertyURI = parts[0].trim();
            }

            //First column is always a resource so datatype specifies the BASE URI.
            if (i > 0) {
                datatypeURIs.add(DatatypeController.lookupDatatypeURI(dataType.trim()));
            } else {
                datatypeURIs.add(DatatypeController.INDIVIDUAL);       //Added to keep alignment of the types with the columns in the data.
                baseURI = dataType.trim();
            }

            //Check property URI for HTTP prefix.
            if (!propertyURI.startsWith(HTTP_PREFIX)) {
                propertyURI = baseURI + propertyURI;
            }
            Property property = ResourceFactory.createProperty(propertyURI);
            propertyURIs.add(property);

        }
        return baseURI;
    }

    private static void readData(String[] dataLine, String baseURI, List<String> datatypeURIs, List<Property> propertyURIs, Model model) {

        //Create subject as individual with specified class (held in zero field of propertyURIs).
        String tidyData = dataLine[0].trim();

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
        Resource subject = model.createResource(uri, propertyURIs.get(0));
        subject.addProperty(RDF.type, NAMED_INDIVIDUAL);

        //Apply a label to the subject.
        Literal labelLiteral = model.createLiteral(label);
        subject.addLiteral(RDFS.label, labelLiteral);

        //Extract the objects.
        for (int i = 1; i < dataLine.length; i++) {

            String dataTypeURI = datatypeURIs.get(i);
            tidyData = tidyString(dataLine[i], dataTypeURI);

            //Exit early on empty or whitespace string.
            if (tidyData.equals("")) {
                continue;
            }

            Property property = propertyURIs.get(i);
            RDFNode object;

            if (dataTypeURI.equals(DatatypeController.INDIVIDUAL)) {

                //Add base URI if HTTP prefix missing.
                if (!tidyData.startsWith(HTTP_PREFIX)) {
                    tidyData = baseURI + tidyData;
                }
                object = model.createResource(tidyData);
            } else {
                object = DatatypeController.extractLiteral(tidyData, dataTypeURI);
            }

            subject.addProperty(property, object);
        }

    }

    /**
     * Format according to xsd restrictions.
     *
     * @param untidyData
     * @param dataTypeURI
     * @return
     */
    private static String tidyString(String untidyData, String dataTypeURI) {
        String tidyData = untidyData.trim();

        if (dataTypeURI.equals(DatatypeController.BOOLEAN_URI)) {
            tidyData = tidyData.toLowerCase();
        }

        return tidyData;
    }
}
