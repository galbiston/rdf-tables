/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class FileSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Model convertCSVDirectory(File dataDirectory, File targetFile, File prefixesFile) {
        return FileSupport.convertCSVDirectory(dataDirectory, Arrays.asList(), targetFile, prefixesFile);
    }

    public static Model convertCSVDirectory(File dataDirectory, File excludedFile, File targetFile, Resource targetGraph, File prefixesFile) {
        return FileSupport.convertCSVDirectory(dataDirectory, Arrays.asList(excludedFile), targetFile, prefixesFile);
    }

    public static Model convertCSVDirectory(File dataDirectory, List<File> excludedFiles, File targetFile, File prefixesFile) {
        List<File> dataFiles = Arrays.asList(dataDirectory.listFiles());
        return convertCSVFiles(dataFiles, excludedFiles, targetFile, prefixesFile);
    }

    public static Model convertCSVFiles(List<File> dataFiles, List<File> excludedFiles, File targetFile, File prefixesFile) {

        HashMap<String, String> prefixMap = PrefixReader.read(prefixesFile);

        Model model = ModelFactory.createDefaultModel();
        for (File inputFile : dataFiles) {
            if (!excludedFiles.contains(inputFile)) {
                FileConverter.writeToModel(inputFile, model, prefixMap);
            }
        }
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            RDFDataMgr.write(out, model, RDFFormat.TTL);
        } catch (IOException ex) {
            LOGGER.error("IOException: {}, File: {}", ex.getMessage(), targetFile);
        }
        return model;
    }

    private static final String CSV_EXTENSION = ".csv";

    public static File insertFileCount(File sourceFile, int fileCount) {
        String sourceFilename = sourceFile.getAbsolutePath();
        int endIndex = sourceFilename.indexOf(CSV_EXTENSION);
        //No CSV file extension so set to the end of the file.
        if (endIndex == -1) {
            endIndex = sourceFilename.length();
        }
        String targetFilename = sourceFilename.substring(0, endIndex) + fileCount + CSV_EXTENSION;
        return new File(targetFilename);
    }

    public static void compileCSVFolder(File tdbStorageFolder, File sourceCSVFolder, File targetRDFFile, Resource targetGraph, File prefixesFile) {

        LOGGER.info("Reading CSV Folder Started: {}", sourceCSVFolder);
        if (!sourceCSVFolder.exists()) {
            LOGGER.info("CSV Folder Not Found - Skipping: {}", sourceCSVFolder);
            return;
        }

        Model model = FileSupport.convertCSVDirectory(sourceCSVFolder, targetRDFFile, prefixesFile);

        Dataset dataset = TDBFactory.createDataset(tdbStorageFolder.getAbsolutePath());
        dataset.begin(ReadWrite.WRITE);
        if (dataset.containsNamedModel(targetGraph.getURI())) {
            Model existingModel = dataset.getNamedModel(targetGraph.getURI());
            existingModel.add(model);
        } else {
            dataset.addNamedModel(targetGraph.getURI(), model);
        }

        dataset.commit();
        dataset.end();
        dataset.close();
        TDBFactory.release(dataset);
        LOGGER.info("Reading RDF Completed: {}", sourceCSVFolder);
    }

}
