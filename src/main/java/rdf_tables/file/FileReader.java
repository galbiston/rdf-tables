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
package rdf_tables.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdf_tables.cli.FormatParameter;
import rdf_tables.datatypes.PrefixController;

/**
 *
 *
 */
public class FileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, File excludedFile, File outputFile, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(excludedFile), outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVDirectory(File inputDirectory, File outputFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        return convertCSVDirectory(inputDirectory, Arrays.asList(), outputFile, null, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVFile(File inputFile, char delimiter) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, new ArrayList<>(), null, null, RDFFormat.TTL, delimiter, false);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, null, rdfFormat, delimiter, isNamedIndividual);
    }

    public static Model convertCSVFile(File inputFile, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        List<File> inputFiles = Arrays.asList(inputFile);
        return convertCSVFiles(inputFiles, excludedFiles, outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static HashMap<String, Model> convertCSVDirectories(File inputDirectory, List<File> excludedFiles, File outputDirectory, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        HashMap<String, Model> models = new HashMap<>();
        List<File> inputFiles = Arrays.asList(inputDirectory.listFiles());
        for (File inputFile : inputFiles) {
            File outputFile = new File(outputDirectory.getAbsoluteFile(), FormatParameter.buildFilename(inputFile, rdfFormat));
            Model model = convertCSVFiles(Arrays.asList(inputFile), excludedFiles, outputFile, null, rdfFormat, delimiter, isNamedIndividual);
            models.put(inputFile.getName(), model);
        }
        return models;
    }

    public static Model convertCSVFiles(List<File> inputFiles, List<File> excludedFiles, File outputFile, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {

        if (prefixesFile != null) {
            HashMap<String, String> prefixMap = PrefixReader.read(prefixesFile);
            PrefixController.addPrefixes(prefixMap);
        }

        Model model = ModelFactory.createDefaultModel();
        for (File inputFile : inputFiles) {
            if (excludedFiles != null && !excludedFiles.contains(inputFile)) {
                FileConverter.writeToModel(inputFile, model, delimiter, isNamedIndividual);
            }
        }

        if (outputFile != null) {
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                RDFDataMgr.write(out, model, rdfFormat);
            } catch (IOException ex) {
                LOGGER.error("IOException: {}, File: {}", ex.getMessage(), outputFile);
            }
        }
        return model;
    }

}
