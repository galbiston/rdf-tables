/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.JCommander;
import org.apache.jena.riot.RDFFormat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gerg
 */
public class ArgsConfigTest {

    public ArgsConfigTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOutputFormat method, of class ArgsConfig.
     */
    @Test
    public void testGetOutputFormat_XML() {
        System.out.println("getOutputFormat_XML");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"--format", "xml", "-i", "test.rdf"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        RDFFormat expResult = RDFFormat.RDFXML;
        RDFFormat result = args.getOutputFormat();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getOutputFormat method, of class ArgsConfig.
     */
    @Test
    public void testGetOutputFormat() {
        System.out.println("getOutputFormat_TTL");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"--format", "ttl", "-i", "test.rdf"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        RDFFormat expResult = RDFFormat.TTL;
        RDFFormat result = args.getOutputFormat();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
