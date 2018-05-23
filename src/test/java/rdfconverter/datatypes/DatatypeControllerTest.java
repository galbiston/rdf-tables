/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.datatypes;

import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Greg Albiston
 */
public class DatatypeControllerTest {

    public DatatypeControllerTest() {
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
     * Test of extractLiteral DateTime method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralDate() {
        System.out.println("extractLiteralDate");
        String dateTime = "2001-10-26";
        String dataTypeURI = XSDBaseNumericType.XSDdate.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(dateTime, XSDBaseNumericType.XSDdate);

        Literal result = DatatypeController.extractLiteral(dateTime, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral DateTime method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralDateTime() {
        System.out.println("extractLiteralDateTime");
        String dateTime = "2001-10-26T21:32:52";
        String dataTypeURI = XSDBaseNumericType.XSDdateTime.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(dateTime, XSDBaseNumericType.XSDdateTime);

        Literal result = DatatypeController.extractLiteral(dateTime, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Time method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralTime() {
        System.out.println("extractLiteralTime");
        String time = "21:32:52";
        String dataTypeURI = XSDBaseNumericType.XSDtime.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(time, XSDBaseNumericType.XSDtime);

        Literal result = DatatypeController.extractLiteral(time, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Duration method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralDuration() {
        System.out.println("extractLiteralDuration");
        String duration = "PT1004199059S";
        String dataTypeURI = XSDBaseNumericType.XSDduration.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(duration, XSDBaseNumericType.XSDduration);

        Literal result = DatatypeController.extractLiteral(duration, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Double method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralDouble() {
        System.out.println("extractLiteralDouble");
        String data = "4.2";
        String dataTypeURI = XSDBaseNumericType.XSDdouble.getURI();

        Double doubleValue = Double.parseDouble(data);
        Literal expResult = ResourceFactory.createTypedLiteral(doubleValue);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Decimal method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralDecimal() {
        System.out.println("extractLiteralDecimal");
        String data = "4.2";
        String dataTypeURI = XSDBaseNumericType.XSDdecimal.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(data, XSDBaseNumericType.XSDdecimal);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Integer method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralInteger() {
        System.out.println("extractLiteralInteger");
        String data = "3";
        String dataTypeURI = XSDBaseNumericType.XSDinteger.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(data, XSDBaseNumericType.XSDinteger);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Int method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralInt() {
        System.out.println("extractLiteralInt");
        String data = "3";
        String dataTypeURI = XSDBaseNumericType.XSDint.getURI();

        int integerValue = Integer.parseInt(data);
        Literal expResult = ResourceFactory.createTypedLiteral(integerValue);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Positive Int method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralPositiveInt() {
        System.out.println("extractLiteralPositiveInt");
        String data = "3";
        String dataTypeURI = XSDBaseNumericType.XSDpositiveInteger.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(data, XSDBaseNumericType.XSDpositiveInteger);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral Boolean method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralBoolean() {
        System.out.println("extractLiteralBoolean");
        String data = "true";
        String dataTypeURI = XSDBaseNumericType.XSDboolean.getURI();

        boolean booleanValue = Boolean.valueOf(data);
        Literal expResult = ResourceFactory.createTypedLiteral(booleanValue);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of extractLiteral String method, of class DataTypeExtract.
     */
    @Test
    public void testExtractLiteralString() {
        System.out.println("extractLiteralString");
        String data = "Blah Blah";
        String dataTypeURI = XSDBaseNumericType.XSDstring.getURI();

        Literal expResult = ResourceFactory.createTypedLiteral(data);

        Literal result = DatatypeController.extractLiteral(data, dataTypeURI);
        //System.out.println("Exp: " + expResult + " Res: " + result);
        assertEquals(expResult, result);
    }
}
