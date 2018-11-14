/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galbiston.rdf_tables.file;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb2.TDB2Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class TDB2Builder {

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

        Dataset dataset = TDB2Factory.connectDataset(tdbStorageFolder.getAbsolutePath());
        TDBBuilder.compile(sourceFolder, dataset, outputFile, targetGraph, prefixesFile, rdfFormat, delimiter, isNamedIndividual);
        dataset.close();
    }

}
