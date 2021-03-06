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
package io.github.galbiston.rdf_tables;

import com.beust.jcommander.JCommander;
import io.github.galbiston.rdf_tables.cli.ArgsConfig;
import io.github.galbiston.rdf_tables.cli.DelimiterValidator;
import io.github.galbiston.rdf_tables.datatypes.DatatypeController;
import io.github.galbiston.rdf_tables.datatypes.PrefixController;
import io.github.galbiston.rdf_tables.file.FileReader;
import io.github.galbiston.rdf_tables.file.FileSupport;
import java.io.File;
import java.util.List;
import org.apache.jena.riot.RDFFormat;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArgsConfig argsConfig = new ArgsConfig();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(argsConfig)
                .build();

        jCommander.setProgramName("RDFTables");
        jCommander.parse(args);
        if (argsConfig.isHelp()) {
            jCommander.usage();
            return;
        }
        //Load prefixes.
        PrefixController.addPrefixes(argsConfig.getPrefixProps());

        //Load datatypes.
        DatatypeController.addPrefixDatatypeURIs(argsConfig.getDatatypeProps());

        //Organise input and output files.
        File inputFile = argsConfig.getInputFile();
        RDFFormat rdfFormat = argsConfig.getOutputFormat();
        File outputFile = FileSupport.checkOutputFile(inputFile, argsConfig.getOutputFile(), rdfFormat);
        List<File> excludedFiles = argsConfig.getExcludedFiles();
        char delimiter = DelimiterValidator.getDelimiterCharacter(argsConfig.getInputDelimiter());
        Boolean isNamedIndividual = argsConfig.isOwlNamedIndividual();

        //Convert files.
        Boolean isDirectoryInput = inputFile.isDirectory();
        Boolean isDirectoryOutput = outputFile.isDirectory();
        if (isDirectoryInput && !isDirectoryOutput) {
            //Directory to File.
            FileReader.convertDirectory(inputFile, excludedFiles, outputFile, rdfFormat, delimiter, isNamedIndividual);
        } else if (!isDirectoryInput && !isDirectoryOutput) {
            //File to File.
            FileReader.convertFile(inputFile, excludedFiles, outputFile, rdfFormat, delimiter, isNamedIndividual);
        } else if (!isDirectoryInput && isDirectoryOutput) {
            //File to Directory.
            //Use input filename and output directory path to create output file.
            File outputFile2 = new File(outputFile.getAbsoluteFile(), inputFile.getName());
            FileReader.convertFile(inputFile, excludedFiles, outputFile2, rdfFormat, delimiter, isNamedIndividual);
        } else {
            //Directory to Directory.
            FileReader.convertDirectories(inputFile, excludedFiles, outputFile, rdfFormat, delimiter, isNamedIndividual);
        }

    }

}
