/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.file;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdftables.cli.FormatConverter;

/**
 *
 *
 */
public class FileSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final File checkOutputFile(File inputFile, File outputFile, RDFFormat rdfFormat) {

        if (outputFile == null) {
            if (inputFile.isDirectory()) {
                outputFile = inputFile;
            } else {
                String outputFilename = FormatConverter.buildFilename(inputFile, rdfFormat);
                outputFile = new File(inputFile.getParentFile(), outputFilename);
            }
        }
        return outputFile;
    }

    private static final String CSV_EXTENSION = ".csv";

    public static File insertFileCount(File sourceFile, int fileCount) {
        return insertFileCount(sourceFile, fileCount, CSV_EXTENSION);
    }

    public static File insertFileCount(File sourceFile, int fileCount, String fileExtension) {
        String sourceFilename = sourceFile.getAbsolutePath();
        int endIndex = sourceFilename.indexOf(fileExtension);
        //No CSV file extension so set to the end of the file.
        if (endIndex == -1) {
            endIndex = sourceFilename.length();
        }
        String targetFilename = sourceFilename.substring(0, endIndex) + fileCount + fileExtension;
        return new File(targetFilename);
    }

}
