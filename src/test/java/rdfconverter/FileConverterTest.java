/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter;

import java.io.File;
import java.util.HashMap;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rdfconverter.file.FileConverter;

/**
 *
 * @author Greg Albiston
 */
public class FileConverterTest {

    private static Model modelTest1;

    public FileConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        File inputFile = new File(FileConverterTest.class.getClassLoader().getResource("Test1.csv").getFile());
        modelTest1 = ModelFactory.createDefaultModel();
        FileConverter.writeToModel(inputFile, modelTest1, new HashMap<>());
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
     * Test of Age method, of class FileConverter. Check int datatype.
     */
    @Test
    public void testAge() {
        System.out.println("Age");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#age");
        Literal object = ResourceFactory.createTypedLiteral(21);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of ShoeSize method, of class FileConverter. Check integer datatype
     * and local property URI.
     */
    @Test
    public void testShoeSize() {
        System.out.println("ShoeSize");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example2.org/ont#shoeSize");
        Literal object = ResourceFactory.createTypedLiteral(8);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Income method, of class FileConverter. Check double datatype.
     */
    @Test
    public void testIncome() {
        System.out.println("Income");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#income");
        Literal object = ResourceFactory.createTypedLiteral(12000.01);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Time method, of class FileConverter. Check time datatype.
     */
    @Test
    public void testTime() {
        System.out.println("Time");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#start");
        Literal object = ResourceFactory.createTypedLiteral("11:00:12", XSDBaseNumericType.XSDtime);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Period method, of class FileConverter. Check duration datatype.
     */
    @Test
    public void testPeriod() {
        System.out.println("Period");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#period");
        Literal object = ResourceFactory.createTypedLiteral("PT130S", XSDBaseNumericType.XSDduration);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Appointment method, of class FileConverter. Check dateTime
     * datatype.
     */
    @Test
    public void testAppointment() {
        System.out.println("Appointment");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#appointment");
        Literal object = ResourceFactory.createTypedLiteral("2001-10-26T21:32:52+02:00", XSDBaseNumericType.XSDdateTime);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Birthday method, of class FileConverter. Check date datatype.
     */
    @Test
    public void testBirthday() {
        System.out.println("Birthday");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonA");
        Property predicate = ResourceFactory.createProperty("http://example.org#birthday");
        Literal object = ResourceFactory.createTypedLiteral("2001-10-26", XSDBaseNumericType.XSDdate);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Driving Licence method, of class FileConverter. Check boolean
     * datatype.
     */
    @Test
    public void testDrivingLicence() {
        System.out.println("DrivingLicence");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonB");
        Property predicate = ResourceFactory.createProperty("http://example.org#drivingLicence");
        Literal object = ResourceFactory.createTypedLiteral(true);
        Statement s = ResourceFactory.createStatement(subject, predicate, object);

        Literal r = modelTest1.getProperty(subject, predicate).getLiteral();
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Person method, of class FileConverter. Check rdf:type creation.
     */
    @Test
    public void testPerson() {
        System.out.println("Person");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonB");
        Property predicate = RDF.type;
        Resource object = ResourceFactory.createResource("http://example.org#Person");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Name method, of class FileConverter. Check string datatype.
     */
    @Test
    public void testName() {
        System.out.println("Name");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonC");
        Property predicate = ResourceFactory.createProperty("http://example.org#name");
        Literal object = ResourceFactory.createTypedLiteral("Peter");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Label method, of class FileConverter. Check rdfs:label creation.
     */
    @Test
    public void testLabel() {
        System.out.println("Label");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonD");
        Property predicate = RDFS.label;
        Literal object = ResourceFactory.createTypedLiteral("PersonD");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Vehicle method, of class FileConverter. Check resource datatype.
     */
    @Test
    public void testVehicle() {
        System.out.println("Vehicle");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonE");
        Property predicate = ResourceFactory.createProperty("http://example.org#vehicle");
        Resource object = ResourceFactory.createResource("http://example.org#VehicleE");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Vehicle2 method, of class FileConverter. Check local URI.
     */
    @Test
    public void testVehicle2() {
        System.out.println("Vehicle2");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonD");
        Property predicate = ResourceFactory.createProperty("http://example.org#vehicle");
        Resource object = ResourceFactory.createResource("http://example2.org/ont#VehicleD");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Vehicle3 method, of class FileConverter. Check repeated property
     * URI.
     */
    @Test
    public void testVehicle3() {
        System.out.println("Vehicle3");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonE");
        Property predicate = ResourceFactory.createProperty("http://example.org#vehicle");
        Resource object = ResourceFactory.createResource("http://example.org#VehicleE");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);

        Resource object2 = ResourceFactory.createResource("http://example.org#VehicleF");
        Statement s2 = ResourceFactory.createStatement(subject, predicate, object2);

        boolean result = modelTest1.contains(s) && modelTest1.contains(s2);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of Missing Filed method, of class FileConverter. Check missing
     * field.
     */
    @Test
    public void testMissingField() {
        System.out.println("MissingField");

        Resource subject = ResourceFactory.createResource("http://example.org#PersonE");
        Property predicate = ResourceFactory.createProperty("http://example.org#name");
        Literal object = ResourceFactory.createTypedLiteral("");
        Statement s = ResourceFactory.createStatement(subject, predicate, object);
        boolean result = modelTest1.contains(s);
        boolean expResult = false;
        assertEquals(expResult, result);
    }

}
