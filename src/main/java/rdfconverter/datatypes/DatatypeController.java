/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.datatypes;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDBaseNumericType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gerg
 */
public class DatatypeController {

    //TODO load from file
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final HashMap<String, String> DATATYPE_PREFIXES = new HashMap<>();
    private static final HashMap<String, BaseDatatype> DATATYPES = new HashMap<>();

    public static final String HEADER_ITEM_SEPARATOR = " ";
    public static final String HTTP_PREFIX = "http://";
    public static final String BOOLEAN_URI = XSDBaseNumericType.XSDboolean.getURI();

    public static final HashMap<String, String> getDatatypePrefixes() {

        if (DATATYPE_PREFIXES.isEmpty()) {
            loadXSDDatatypePrefixes();
            loadXSDDatatypes();
        }
        return DATATYPE_PREFIXES;
    }

    public static final HashMap<String, BaseDatatype> getDatatypes() {

        if (DATATYPES.isEmpty()) {
            loadXSDDatatypePrefixes();
            loadXSDDatatypes();
        }
        return DATATYPES;
    }

    private static void loadXSDDatatypePrefixes() {

        DATATYPE_PREFIXES.put("string", XSDBaseNumericType.XSDstring.getURI());
        DATATYPE_PREFIXES.put("int", XSDBaseNumericType.XSDint.getURI());
        DATATYPE_PREFIXES.put("integer", XSDBaseNumericType.XSDinteger.getURI());
        DATATYPE_PREFIXES.put("positiveinteger", XSDBaseNumericType.XSDpositiveInteger.getURI());
        DATATYPE_PREFIXES.put("nonnegativeinteger", XSDBaseNumericType.XSDnonNegativeInteger.getURI());
        DATATYPE_PREFIXES.put("nonpositiveinteger", XSDBaseNumericType.XSDnonPositiveInteger.getURI());
        DATATYPE_PREFIXES.put("double", XSDBaseNumericType.XSDdouble.getURI());
        DATATYPE_PREFIXES.put("decimal", XSDBaseNumericType.XSDdecimal.getURI());
        DATATYPE_PREFIXES.put("boolean", XSDBaseNumericType.XSDboolean.getURI());
        DATATYPE_PREFIXES.put("date", XSDBaseNumericType.XSDdate.getURI());
        DATATYPE_PREFIXES.put("datetime", XSDBaseNumericType.XSDdateTime.getURI());
        DATATYPE_PREFIXES.put("time", XSDBaseNumericType.XSDtime.getURI());
        DATATYPE_PREFIXES.put("duration", XSDBaseNumericType.XSDduration.getURI());
    }

    private static void loadXSDDatatypes() {
        DATATYPES.put(XSDBaseNumericType.XSDstring.getURI(), XSDBaseNumericType.XSDstring);
        DATATYPES.put(XSDBaseNumericType.XSDint.getURI(), XSDBaseNumericType.XSDint);
        DATATYPES.put(XSDBaseNumericType.XSDinteger.getURI(), XSDBaseNumericType.XSDinteger);
        DATATYPES.put(XSDBaseNumericType.XSDpositiveInteger.getURI(), XSDBaseNumericType.XSDpositiveInteger);
        DATATYPES.put(XSDBaseNumericType.XSDnonNegativeInteger.getURI(), XSDBaseNumericType.XSDnonNegativeInteger);
        DATATYPES.put(XSDBaseNumericType.XSDnonPositiveInteger.getURI(), XSDBaseNumericType.XSDnonPositiveInteger);
        DATATYPES.put(XSDBaseNumericType.XSDdouble.getURI(), XSDBaseNumericType.XSDdouble);
        DATATYPES.put(XSDBaseNumericType.XSDdecimal.getURI(), XSDBaseNumericType.XSDdecimal);
        DATATYPES.put(XSDBaseNumericType.XSDboolean.getURI(), XSDBaseNumericType.XSDboolean);
        DATATYPES.put(XSDBaseNumericType.XSDdate.getURI(), XSDBaseNumericType.XSDdate);
        DATATYPES.put(XSDBaseNumericType.XSDdateTime.getURI(), XSDBaseNumericType.XSDdateTime);
        DATATYPES.put(XSDBaseNumericType.XSDtime.getURI(), XSDBaseNumericType.XSDtime);
        DATATYPES.put(XSDBaseNumericType.XSDduration.getURI(), XSDBaseNumericType.XSDduration);
    }

    public static final void addPrefixDatatype(String prefix, BaseDatatype datatype) {
        getDatatypes();
        DATATYPE_PREFIXES.put(prefix, datatype.getURI());
        DATATYPES.put(datatype.getURI(), datatype);
    }

    public static final String lookupDatatypeURI(String datatypeLabel, String baseURI) {
        getDatatypes();
        //Check if datatypeLabel is a URI and if so return.
        String tidyLabel = datatypeLabel.toLowerCase();
        if (DATATYPE_PREFIXES.containsKey(tidyLabel)) {
            return DATATYPE_PREFIXES.get(tidyLabel);
        } else {
            return PrefixController.lookupURI(datatypeLabel, baseURI);
        }
    }

    public static final Literal extractLiteral(String data, String datatypeURI) {
        getDatatypes();
        if (DATATYPES.containsKey(datatypeURI)) {
            BaseDatatype datatype = DATATYPES.get(datatypeURI);
            String tidyData = tidyDatatype(data, datatypeURI);
            return ResourceFactory.createTypedLiteral(tidyData, datatype);
        } else {
            LOGGER.error("Datatype URI {} not recognised for data: {}", datatypeURI, data);
            return null;
        }

    }

    /**
     * Format according to xsd restrictions.
     *
     * @param untidyData
     * @param dataTypeURI
     * @return
     */
    private static String tidyDatatype(String untidyData, String dataTypeURI) {
        String tidyData = untidyData.trim();

        if (dataTypeURI.equals(DatatypeController.BOOLEAN_URI)) {
            tidyData = tidyData.toLowerCase();
        }

        return tidyData;
    }

}
