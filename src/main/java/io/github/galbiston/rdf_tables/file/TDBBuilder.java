/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.rdf_tables.file;

import static io.github.galbiston.rdf_tables.file.FileReader.convertDirectory;
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
     * @param sourceFolder
     * @param tdbStorageFolder
     * @param targetGraph
     * @param prefixesFile
     */
    public static void compileFolder(File sourceFolder, File tdbStorageFolder, Resource targetGraph, File prefixesFile) {
        compileFolder(sourceFolder, tdbStorageFolder, null, targetGraph, prefixesFile, RDFFormat.TTL, ',', true);
    }

    /**
     * Default input format TTL, comma separated with creation of OWL Named
     * Individuals.
     *
     * @param sourceFolder
     * @param tdbStorageFolder
     * @param outputFile
     * @param targetGraph
     * @param prefixesFile
     */
    public static void compileFolder(File sourceFolder, File tdbStorageFolder, File outputFile, Resource targetGraph, File prefixesFile) {
        compileFolder(sourceFolder, tdbStorageFolder, outputFile, targetGraph, prefixesFile, RDFFormat.TTL, ',', true);
    }

    public static void compileFolder(File sourceFolder, File tdbStorageFolder, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        compileFolder(sourceFolder, tdbStorageFolder, null, targetGraph, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
    }

    public static void compileFolder(File sourceFolder, File tdbStorageFolder, File outputFile, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {

        Dataset dataset = TDBFactory.createDataset(tdbStorageFolder.getAbsolutePath());
        compile(sourceFolder, dataset, outputFile, targetGraph, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
        dataset.close();
        TDBFactory.release(dataset);
    }

    protected static void compile(File sourceFolder, Dataset dataset, File outputFile, Resource targetGraph, File prefixesFile, RDFFormat rdfFormat, char delimiter, Boolean isNamedIndividual) {
        LOGGER.info("Reading Folder Started: {}", sourceFolder);
        if (!sourceFolder.exists()) {
            LOGGER.info("Folder Not Found - Skipping: {}", sourceFolder);
            return;
        }

        Model model = convertDirectory(sourceFolder, outputFile, prefixesFile, rdfFormat, delimiter, isNamedIndividual);

        dataset.begin(ReadWrite.WRITE);
        if (dataset.containsNamedModel(targetGraph.getURI())) {
            Model existingModel = dataset.getNamedModel(targetGraph.getURI());
            existingModel.add(model);
        } else {
            dataset.addNamedModel(targetGraph.getURI(), model);
        }

        dataset.commit();
        dataset.end();

        LOGGER.info("Reading RDF Completed: {}", sourceFolder);
    }
}
