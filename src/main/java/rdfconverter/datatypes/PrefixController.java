/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.datatypes;

import java.util.HashMap;
import static rdfconverter.datatypes.DatatypeController.HTTP_PREFIX;

/**
 *
 * @author Gerg
 */
public class PrefixController {

    private static final HashMap<String, String> PREFIXES = new HashMap<>();
    public static final String PREFIX_SEPARATOR = ":";

    public static final String lookupURI(String classString, String baseURI) {
        //Check property URI for HTTP prefix.
        if (classString.startsWith(HTTP_PREFIX)) {
            return classString;
        } else if (PREFIXES.containsKey(classString)) {
            return PREFIXES.get(classString);
        } else {

            String[] parts = classString.split(PREFIX_SEPARATOR);

            if (parts.length > 1) {
                if (PREFIXES.containsKey(parts[0])) {
                    return PREFIXES.get(parts[0]) + parts[1];
                }
            }
            return baseURI + classString;
        }
    }

}
