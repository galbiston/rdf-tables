package rdftables;

import com.beust.jcommander.JCommander;
import java.io.File;
import java.util.List;
import org.apache.jena.riot.RDFFormat;
import rdftables.cli.ArgsConfig;
import rdftables.cli.SeparatorValidator;
import rdftables.datatypes.DatatypeController;
import rdftables.datatypes.PrefixController;
import rdftables.file.FileReader;
import rdftables.file.FileSupport;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        //Load prefixes.
        PrefixController.addPrefixes(argsConfig.getPrefixProps());

        //Load datatypes.
        DatatypeController.addPrefixDatatypes(argsConfig.getDatatypeProps());

        //Organise input and output files.
        File inputFile = argsConfig.getInputFile();
        RDFFormat rdfFormat = argsConfig.getOutputFormat();
        File outputFile = FileSupport.checkOutputFile(inputFile, argsConfig.getOutputFile(), rdfFormat);
        List<File> excludedFiles = argsConfig.getExcludedFiles();
        char separator = SeparatorValidator.getSeparatorCharacter(argsConfig.getInputSeparator());
        Boolean isNamedIndividual = argsConfig.isOwlNamedIndividual();

        //Convert files.
        Boolean isDirectoryInput = inputFile.isDirectory();
        Boolean isDirectoryOutput = outputFile.isDirectory();
        if (isDirectoryInput && !isDirectoryOutput) {
            //Directory to File.
            FileReader.convertCSVDirectory(inputFile, excludedFiles, outputFile, rdfFormat, separator, isNamedIndividual);
        } else if (!isDirectoryInput && !isDirectoryOutput) {
            //File to File.
            FileReader.convertCSVFile(inputFile, excludedFiles, outputFile, rdfFormat, separator, isNamedIndividual);
        } else if (!isDirectoryInput && isDirectoryOutput) {
            //File to Directory.
            //Use input filename and output directory path to create output file.
            File outputFile2 = new File(outputFile.getAbsoluteFile(), inputFile.getName());
            FileReader.convertCSVFile(inputFile, excludedFiles, outputFile2, rdfFormat, separator, isNamedIndividual);
        } else {
            //Directory to Directory.
            FileReader.convertCSVDirectories(inputFile, excludedFiles, outputFile, rdfFormat, separator, isNamedIndividual);
        }

    }

}
