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
package rdf_tables.cli;

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
    @Parameter(names = {"--format", "-f"}, description = "The file serialistion used for the RDF output: json-ld, json-rdf, nt, nq, thrift, trig, trix, ttl, ttl-pretty, xml, xml-plain, xml-pretty. Default: ttl", validateWith = FormatParameter.class, converter = FormatParameter.class)
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
