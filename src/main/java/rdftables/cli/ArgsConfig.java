/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;
import java.util.Properties;

/**
 *
 *
 */
public class ArgsConfig {

    //1) Input folder/file to convert
    //- folder then use input filename to build. Get extension from serialisation.
    @Parameter(names = {"--input", "-i"}, description = "Input file or folder path for conversion process.", converter = FileConverter.class, required = true)
    private File inputFile;

    //2) Output folder/file to write to [optional], see above, default to same folder
    //- file then consildate into single file
    @Parameter(names = {"--output", "-o"}, description = "Output file or folder path for conversion process. Folder option will replicate input filenames. Single output file will consolidate an input folder.", converter = FileConverter.class)
    private File outputFile = null;

    //3) Output format/serialisation - ttl, nt, nq, json-ld, json, xml
    @Parameter(names = {"--format", "-f"}, description = "The file serialistion used for the RDF output: json-ld, nt, nq, json, xml, ttl. Default: ttl", validateWith = FormatValidator.class)
    private String outputFormat = "ttl";

    //4) Separator value - COMMA, TAB, SPACE
    @Parameter(names = {"--sep", "-s"}, description = "Column separator in the input file. Any character except ':', '^' and '|'. Default: ',' ", validateWith = SeparatorValidator.class)
    private String inputSeparator = ",";

    //5) Prefixes file
    @Parameter(names = {"--prefixes", "-p"}, description = "Prefix definition file of key=value pairs with no header (Java Properties format).", converter = PropsConverter.class)
    private Properties prefixProps;

    //6) Datatypes file
    @Parameter(names = {"--datatypes", "-d"}, description = "Datatype definition file of key=value pairs with no header (Java Properties format).", converter = PropsConverter.class)
    private Properties datatypeProps;

    //7) Owl NamedIndividual
    @Parameter(names = {"-named", "-n"}, description = "Boolean value for creating OWL NamedIndividuals in the data. Default: true", arity = 1)
    private boolean owlNamedIndividual = true;

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getInputSeparator() {
        return inputSeparator;
    }

    public Properties getPrefixProps() {
        return prefixProps;
    }

    public Properties getDatatypeProps() {
        return datatypeProps;
    }

    public boolean isOwlNamedIndividual() {
        return owlNamedIndividual;
    }

}
