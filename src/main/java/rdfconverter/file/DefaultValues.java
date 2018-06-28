/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.file;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.OWL;

/**
 *
 *
 */
public interface DefaultValues {

    public static final char COLUMN_SEPARATOR = ',';
    public static final Resource NO_CLASS_ANON = ResourceFactory.createResource("http://example.org#NoClass");
    public static final String HEADER_ITEM_SEPARATOR = "\\|";       //Have to escape the pipe when reading.
    public static final String HEADER_ITEM_SEPARATOR_CHARACTER = "|";
    public static final Boolean IS_OWL_INDIVIDUAL = Boolean.FALSE;
    public static final String CLASS_CHARACTER = ":";
    public static final Boolean IS_RDFS_LABEL = Boolean.TRUE;
    public static final Resource NAMED_INDIVIDUAL = ResourceFactory.createResource(OWL.NS + "NamedIndividual");
    public static final char INVERT_CHARACTER = '^';

}
