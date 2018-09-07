/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.riot.RDFFormat;

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
    @Parameter(names = {"--format", "-f"}, description = "The file serialistion used for the RDF output: json-ld, nt, nq, json, xml, ttl. Default: ttl", validateWith = FormatValidator.class, converter = FormatConverter.class)
    private RDFFormat outputFormat = RDFFormat.TTL;

    //4) Separator value - COMMA, TAB, SPACE
    @Parameter(names = {"--sep", "-s"}, description = "Column separator in the input file. Any character except ':', '^' and '|'. Keywords TAB, SPACE and COMMA are also supported. Default: ',' ", validateWith = SeparatorValidator.class)
    private String inputSeparator = "COMMA";

    //5) Prefixes file
    @Parameter(names = {"--prefixes", "-p"}, description = "Prefix definition file of key=value pairs with no header (Java Properties format).", converter = PropsConverter.class)
    private HashMap<String, String> prefixProps;

    //6) Datatypes file
    @Parameter(names = {"--datatypes", "-d"}, description = "Datatype definition file of key=value pairs with no header (Java Properties format).", converter = PropsConverter.class)
    private HashMap<String, String> datatypeProps;

    //7) Owl NamedIndividual
    @Parameter(names = {"-named", "-n"}, description = "Boolean value for creating OWL NamedIndividuals in the data. Default: true", arity = 1)
    private boolean owlNamedIndividual = true;

    //8) Excluded Files
    @Parameter(names = {"-excluded", "-x"}, description = "Excluded files not to be used as input from a folder.")
    private List<File> excludedFiles = new ArrayList<>();

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public RDFFormat getOutputFormat() {
        return outputFormat;
    }

    public String getInputSeparator() {
        return inputSeparator;
    }

    public HashMap<String, String> getPrefixProps() {
        return prefixProps;
    }

    public HashMap<String, String> getDatatypeProps() {
        return datatypeProps;
    }

    public boolean isOwlNamedIndividual() {
        return owlNamedIndividual;
    }

    public List<File> getExcludedFiles() {
        return excludedFiles;
    }

    @Override
    public String toString() {
        return "ArgsConfig{" + "inputFile=" + inputFile + ", outputFile=" + outputFile + ", outputFormat=" + outputFormat + ", inputSeparator=" + inputSeparator + ", prefixProps=" + prefixProps + ", datatypeProps=" + datatypeProps + ", owlNamedIndividual=" + owlNamedIndividual + ", excludedFiles=" + excludedFiles + '}';
    }

}
