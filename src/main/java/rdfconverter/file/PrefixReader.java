/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.file;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg Albiston
 */
public class PrefixReader {

    //TODO - load default prefixes and from file.
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final char COLUMN_SEPARATOR = ',';

    public static final HashMap<String, String> read(File inputFile) {

        LOGGER.info("Prefix Parsing Started: {}", inputFile.getPath());
        HashMap<String, String> map = new HashMap<>();
        int lineNumber = 1;
        try (CSVReader reader = new CSVReader(new FileReader(inputFile), COLUMN_SEPARATOR)) {

            if (reader.readNext().length != 2) {
                LOGGER.error("Should only be two columns");
            } else {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    lineNumber++;
                    String prefix = line[0].trim();
                    String uri = line[1].trim();

                    map.put(prefix, uri);
                }
            }

        } catch (IOException | RuntimeException ex) {
            LOGGER.error("PrefixReader: Line - {}, File - {}, Exception - {}", lineNumber, inputFile.getAbsolutePath(), ex.getMessage());
        }

        return map;
    }

}
