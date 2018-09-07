/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.file;

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
import rdftables.cli.FormatConverter;
import rdftables.datatypes.PrefixController;

/**
 *
 *
 */
public class FileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, prefixesFile, rdfFormat, separator);
    }

    public static Model convertCSVDirectory(File inputDirectory, File excludedFile, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(excludedFile), outputFile, prefixesFile, rdfFormat, separator);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, separator);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char separator) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, separator);
    }

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, RDFFormat rdfFormat, char separator) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, null, rdfFormat, separator);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char separator) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, separator);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, separator);
    }

    public static HashMap<String, Model> convertCSVDirectories(File inputDirectory, List<File> excludedFiles, File outputDirectory, RDFFormat rdfFormat, char separator) {
        HashMap<String, Model> models = new HashMap<>();
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        for (File inputFile : inputFiles) {
            File outputFile = new File(outputDirectory.getAbsoluteFile(), FormatConverter.buildFilename(inputFile, rdfFormat));
            Model model = convertCSVFiles(Arrays.asList(inputFile), excludedFiles, outputFile, null, rdfFormat, separator);
            models.put(inputFile.getName(), model);
        }
        return models;
    }

    public static Model convertCSVFiles(List<File> inputFiles, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator) {

        if (prefixesFile != null) {
            HashMap<String, String> prefixMap = PrefixReader.read(prefixesFile);
            PrefixController.addPrefixes(prefixMap);
        }

        Model model = ModelFactory.createDefaultModel();
        for (File inputFile : inputFiles) {
            if (!excludedFiles.contains(inputFile)) {
                FileConverter.writeToModel(inputFile, model, separator);
            }
        }
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, model, rdfFormat);
        } catch (IOException ex) {
            LOGGER.error("IOException: {}, File: {}", ex.getMessage(), outputFile);
        }
        return model;
    }

    public static void compileCSVFolder(File tdbStorageFolder, File sourceCSVFolder, File outputRDFFile, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char separator) {

        LOGGER.info("Reading CSV Folder Started: {}", sourceCSVFolder);
        if (!sourceCSVFolder.exists()) {
            LOGGER.info("CSV Folder Not Found - Skipping: {}", sourceCSVFolder);
            return;
        }

        Model model = convertCSVDirectory(sourceCSVFolder, outputRDFFile, prefixesFile, rdfFormat, separator);

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
