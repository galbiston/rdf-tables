/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class FormatValidator implements IParameterValidator {

    private static final List<String> PERMITTED_FORMATS = Arrays.asList("json-ld", "nt", "nq", "json-rdf", "xml-plain", "xml-plain", "xml", "thrift", "trig", "trix", "ttl", "ttl-pretty");

    @Override
    public void validate(String name, String value) throws ParameterException {
        String val = value.toLowerCase();
        if (!PERMITTED_FORMATS.contains(val)) {
            throw new ParameterException("Parameter " + name + " and value " + value + " should be one of " + String.join(", ", PERMITTED_FORMATS) + ".");
        }

    }

}
