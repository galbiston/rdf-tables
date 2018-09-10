/**
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, prefixesFile, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, File excludedFile, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(excludedFile), outputFile, prefixesFile, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, null, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, separator, isNamedIndividual);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, separator, isNamedIndividual);
    }

    public static HashMap<String, Model> convertCSVDirectories(File inputDirectory, List<File> excludedFiles, File outputDirectory, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {
        HashMap<String, Model> models = new HashMap<>();
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        for (File inputFile : inputFiles) {
            File outputFile = new File(outputDirectory.getAbsoluteFile(), FormatConverter.buildFilename(inputFile, rdfFormat));
            Model model = convertCSVFiles(Arrays.asList(inputFile), excludedFiles, outputFile, null, rdfFormat, separator, isNamedIndividual);
            models.put(inputFile.getName(), model);
        }
        return models;
    }

    public static Model convertCSVFiles(List<File> inputFiles, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {

        if (prefixesFile != null) {
            HashMap<String, String> prefixMap = PrefixReader.read(prefixesFile);
            PrefixController.addPrefixes(prefixMap);
        }

        Model model = ModelFactory.createDefaultModel();
        for (File inputFile : inputFiles) {
            if (!excludedFiles.contains(inputFile)) {
                FileConverter.writeToModel(inputFile, model, separator, isNamedIndividual);
            }
        }
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            RDFDataMgr.write(out, model, rdfFormat);
        } catch (IOException ex) {
            LOGGER.error("IOException: {}, File: {}", ex.getMessage(), outputFile);
        }
        return model;
    }

    /**
     * Default input format TTL, comma separated with creation of OWL Named
     * Individuals.
     *
     * @param tdbStorageFolder
     * @param sourceCSVFolder
     * @param outputRDFFile
     * @param targetGraph
     * @param prefixesFile
     */
    public static void compileCSVFolder(File tdbStorageFolder, File sourceCSVFolder, File outputRDFFile, Resource targetGraph, File prefixesFile) {
        compileCSVFolder(tdbStorageFolder, sourceCSVFolder, outputRDFFile, targetGraph, prefixesFile, RDFFormat.TTL, ',', true);
    }

    public static void compileCSVFolder(File tdbStorageFolder, File sourceCSVFolder, File outputRDFFile, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char separator, Boolean isNamedIndividual) {

        LOGGER.info("Reading CSV Folder Started: {}", sourceCSVFolder);
        if (!sourceCSVFolder.exists()) {
            LOGGER.info("CSV Folder Not Found - Skipping: {}", sourceCSVFolder);
            return;
        }

        Model model = convertCSVDirectory(sourceCSVFolder, outputRDFFile, prefixesFile, rdfFormat, separator, isNamedIndividual);

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
