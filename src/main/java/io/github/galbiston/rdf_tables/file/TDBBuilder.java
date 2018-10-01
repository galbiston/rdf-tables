/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.rdf_tables.file;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static io.github.galbiston.rdf_tables.file.FileReader.convertCSVDirectory;

/**
 *
 *
 */
public class TDBBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Default input format TTL, comma separated with creation of OWL Named
     * Individuals.
     *
     * @param sourceCSVFolder
     * @param tdbStorageFolder
     * @param targetGraph
     * @param prefixesFile
     */
    public static void compileCSVFolder(File sourceCSVFolder, File tdbStorageFolder, Resource targetGraph, File prefixesFile) {
        compileCSVFolder(sourceCSVFolder, tdbStorageFolder, null, targetGraph, prefixesFile, RDFFormat.TTL, ',', true);
    }

    /**
     * Default input format TTL, comma separated with creation of OWL Named
     * Individuals.
     *
     * @param sourceCSVFolder
     * @param tdbStorageFolder
     * @param outputFile
     * @param targetGraph
     * @param prefixesFile
     */
    public static void compileCSVFolder(File sourceCSVFolder, File tdbStorageFolder, File outputFile, Resource targetGraph, File prefixesFile) {
        compileCSVFolder(sourceCSVFolder, tdbStorageFolder, outputFile, targetGraph, prefixesFile, RDFFormat.TTL, ',', true);
    }

    public static void compileCSVFolder(File sourceCSVFolder, File tdbStorageFolder, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        compileCSVFolder(sourceCSVFolder, tdbStorageFolder, null, targetGraph, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static void compileCSVFolder(File sourceCSVFolder, File tdbStorageFolder, File outputFile, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {

        LOGGER.info("Reading CSV Folder Started: {}", sourceCSVFolder);
        if (!sourceCSVFolder.exists()) {
            LOGGER.info("CSV Folder Not Found - Skipping: {}", sourceCSVFolder);
            return;
        }

        Model model = convertCSVDirectory(sourceCSVFolder, outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);

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
