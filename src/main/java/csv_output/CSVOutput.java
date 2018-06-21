/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv_output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.ext.com.google.common.collect.ArrayListMultimap;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import rdfconverter.datatypes.Datatypes;
import rdfconverter.file.DefaultValues;
import static rdfconverter.file.DefaultValues.HEADER_ITEM_SEPARATOR;

/**
 *
 *
 */
public abstract class CSVOutput {

    protected static String BASE_URI = "";
    protected static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public final static void setBaseURI(String baseURI) {
        BASE_URI = baseURI;
    }

    public final static String getBaseURI() {
        return BASE_URI;
    }

    protected static void countProperties(ArrayListMultimap<Property, ? extends RDFNode> propertyListMap, HashMap<Property, Integer> countMap) {
        for (Property property : propertyListMap.keySet()) {
            Integer value = propertyListMap.get(property).size();
            if (countMap.containsKey(property)) {
                Integer maxValue = countMap.get(property);
                if (value > maxValue) {
                    countMap.put(property, value);
                }
            } else {
                countMap.put(property, value);
            }
        }
    }

    protected static void writeBaseHeader(List<String> header, Resource classResource) {
        header.add(getBaseHeader(BASE_URI, classResource));
    }

    public static String getBaseHeader(String baseURI, Resource classResource) {
        return getBaseHeader(baseURI, classResource.getURI());
    }

    public static String getBaseHeader(String baseURI, String classURI) {
        return baseURI + HEADER_ITEM_SEPARATOR + classURI;
    }

    protected static void writeHeader(List<String> header, Property property, Datatypes propertyDatatype) {
        HashMap<Property, Datatypes> propertyDatatypes = new HashMap<>();
        propertyDatatypes.put(property, propertyDatatype);
        writeHeader(header, property, 1, propertyDatatypes);
    }

    protected static void writeHeader(List<String> header, Property property) {
        writeHeader(header, property, 1, new HashMap<>());
    }

    protected static void writeHeader(List<String> header, Property property, Integer maxCount) {
        writeHeader(header, property, maxCount, new HashMap<>());
    }

    protected static void writeHeader(List<String> header, Property property, Integer maxCount, HashMap<Property, Datatypes> propertyDatatypes) {
        String headerLabel;
        if (propertyDatatypes.containsKey(property)) {
            Datatypes datatype = propertyDatatypes.get(property);
            headerLabel = property.getURI() + DefaultValues.HEADER_ITEM_SEPARATOR + datatype;
        } else {
            headerLabel = property.getURI();
        }

        for (int i = 0; i < maxCount; i++) {
            header.add(headerLabel);
        }

    }

    protected static void writeHeader(List<String> header, HashMap<Property, Integer> countMap) {
        writeHeader(header, countMap, new HashMap<>());
    }

    protected static void writeHeader(List<String> header, HashMap<Property, Integer> countMap, HashMap<Property, Datatypes> propertyDatatypes) {
        for (Map.Entry<Property, Integer> entry : countMap.entrySet()) {
            Property property = entry.getKey();

            //Build the header label from either the datatype and property or just the property.
            writeHeader(header, property, entry.getValue(), propertyDatatypes);
        }
    }

    protected void write(List<String> line, RDFNode rdfNode) {
        String label;
        if (rdfNode.isLiteral()) {
            Literal literal = rdfNode.asLiteral();
            label = literal.getLexicalForm();
        } else {
            Resource resource = rdfNode.asResource();
            String namespace = resource.getNameSpace();
            if (namespace.equals(BASE_URI)) {
                label = resource.getLocalName();
            } else {
                label = resource.getURI();
            }
        }
        line.add(label);
    }

    protected void write(List<String> line, List<? extends RDFNode> values, Integer maxCount) {
        for (int i = 0; i < maxCount; i++) {
            if (i < values.size()) {
                RDFNode rdfNode = values.get(i);
                write(line, rdfNode);
            } else {
                line.add("");
            }
        }
    }

    protected void write(List<String> line, ArrayListMultimap<Property, ? extends RDFNode> propertyListMap, HashMap<Property, Integer> countMap) {
        for (Map.Entry<Property, Integer> entry : countMap.entrySet()) {
            Property property = entry.getKey();
            Integer maxCount = entry.getValue();
            List<? extends RDFNode> rdfNodes = propertyListMap.get(property);
            for (int i = 0; i < maxCount; i++) {
                if (i < rdfNodes.size()) {
                    RDFNode rdfNode = rdfNodes.get(i);
                    write(line, rdfNode);
                } else {
                    line.add("");
                }
            }
        }
    }

}
