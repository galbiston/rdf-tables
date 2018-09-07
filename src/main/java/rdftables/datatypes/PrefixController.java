/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.datatypes;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static rdftables.datatypes.DatatypeController.HTTP_PREFIX;

/**
 *
 * @author Gerg
 */
public class PrefixController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final HashMap<String, String> PREFIXES = new HashMap<>();
    public static final String PREFIX_SEPARATOR = ":";

    public static HashMap<String, String> getPrefixes() {
        if (PREFIXES.isEmpty()) {
            loadPrefixes();
        }
        return PREFIXES;
    }

    private static void loadPrefixes() {
        PREFIXES.put("olo", "http://purl.org/ontology/olo/core#");
        PREFIXES.put("owl", "http://www.w3.org/2002/07/owl#");
        PREFIXES.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        PREFIXES.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        PREFIXES.put("time", "http://www.w3.org/2006/time#");
        PREFIXES.put("xsd", "http://www.w3.org/2001/XMLSchema#");
    }

    public static final void addPrefixes(Map<String, String> prefixes) {
        PREFIXES.putAll(prefixes);
    }

    public static final void addPrefix(String prefix, String uri) {
        PREFIXES.put(prefix, uri);
    }

    public static final String lookupURI(String classLabel, String baseURI) {
        getPrefixes();
        //Check property URI for HTTP prefix.
        if (checkURI(classLabel)) {
            return classLabel;
        } else if (PREFIXES.containsKey(classLabel)) {
            return PREFIXES.get(classLabel);
        } else {

            String[] parts = classLabel.split(PREFIX_SEPARATOR);

            if (parts.length > 1) {
                if (PREFIXES.containsKey(parts[0])) {
                    return PREFIXES.get(parts[0]) + parts[1];
                } else {
                    LOGGER.error("Prefix unknown for {}", classLabel);
                    throw new AssertionError();
                }
            }
            return baseURI + classLabel;
        }
    }

    public static final boolean checkURI(String candidateURI) {
        return candidateURI.startsWith(HTTP_PREFIX);
    }

}
