/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.IStringConverter;
import java.io.File;
import org.apache.jena.riot.RDFFormat;

/**
 *
 *
 */
public class FormatConverter implements IStringConverter<RDFFormat> {

    @Override
    public RDFFormat convert(String rdfFormat) {

        switch (rdfFormat.toLowerCase()) {
            case "ttl":
                return RDFFormat.TTL;
            case "ttl-pretty":
                return RDFFormat.TURTLE_PRETTY;
            case "json-ld":
                return RDFFormat.JSONLD;
            case "nt":
                return RDFFormat.NT;
            case "nq":
                return RDFFormat.NQ;
            case "json-rdf":
                return RDFFormat.RDFJSON;
            case "xml-plain":
                return RDFFormat.RDFXML_PLAIN;
            case "xml-pretty":
                return RDFFormat.RDFXML_PRETTY;
            case "xml":
                return RDFFormat.RDFXML;
            case "thrift":
                return RDFFormat.RDF_THRIFT;
            case "trig":
                return RDFFormat.TRIG;
            case "trix":
                return RDFFormat.TRIX;
            default:
                return RDFFormat.TTL;
        }
    }

    public static final String fileExtension(RDFFormat rdfFormat) {

        switch (rdfFormat.toString()) {
            case "TTL":
                return ".ttl";
            case "ttl-pretty":
                return ".ttl";
            case "json-ld":
                return ".json";
            case "nt":
                return ".nt";
            case "nq":
                return ".nq";
            case "json-rdf":
                return ".jsonld";
            case "xml-plain":
                return ".rdf";
            case "xml-pretty":
                return ".rdf";
            case "xml":
                return ".rdf";
            case "thrift":
                return ".trdf";
            case "trig":
                return ".trig";
            case "trix":
                return ".trix";
            default:
                return ".ttl";
        }

    }

    public static final String buildFilename(File inputFile, RDFFormat rdfFormat) {

        String inputFilename = inputFile.getName();

        int endIndex = inputFilename.indexOf(".");
        //No CSV file extension so set to the end of the file.
        if (endIndex == -1) {
            endIndex = inputFilename.length();
        }
        return inputFilename.substring(0, endIndex) + fileExtension(rdfFormat);
    }

}
