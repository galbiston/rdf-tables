/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.jena.riot.RDFFormat;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

    /**
     * Test of isNamedIndividual method, of class ArgsConfig.
     */
    @Test
    public void testIsNamedIndividual() {
        System.out.println("getIsNamedIndividual");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        Boolean expResult = true;
        Boolean result = args.isOwlNamedIndividual();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of isNamedIndividual method, of class ArgsConfig.
     */
    @Test
    public void testIsNamedIndividual_false() {
        System.out.println("getIsNamedIndividual_false");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf", "-n", "false"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        Boolean expResult = false;
        Boolean result = args.isOwlNamedIndividual();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInputSeparator method, of class ArgsConfig.
     */
    @Test
    @Ignore
    public void testGetInputSeparator_tab() {
        System.out.println("getInputSeparator_tab");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf", "-s", "\t"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        String expResult = "\t";
        String result = args.getInputSeparator();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInputSeparator method, of class ArgsConfig.
     */
    @Test
    @Ignore
    public void testGetInputSeparator_space() {
        System.out.println("getInputSeparator_space");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf", "-s", " "};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        String expResult = " ";
        String result = args.getInputSeparator();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInputSeparator method, of class ArgsConfig.
     */
    @Test
    public void testGetInputSeparator_comma() {
        System.out.println("getInputSeparator_comma");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        String expResult = ",";
        String result = args.getInputSeparator();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInputSeparator method, of class ArgsConfig.
     */
    @Test(expected = ParameterException.class)
    public void testGetInputSeparator_reserved() {
        System.out.println("getInputSeparator_reserved");
        ArgsConfig args = new ArgsConfig();

        String[] argv = {"-i", "test.rdf", "-s", "|"};
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        String expResult = ",";
        String result = args.getInputSeparator();

        //System.out.println("Exp: " + expResult);
        //System.out.println("Res: " + result);
        assertEquals(expResult, result);
    }

}
