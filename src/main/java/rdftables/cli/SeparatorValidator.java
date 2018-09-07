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
public class SeparatorValidator implements IParameterValidator {

    private static final List<String> RESERVED_CHARACTERS = Arrays.asList(":", "^", "|");

    @Override
    public void validate(String name, String value) throws ParameterException {
        String val = value.toLowerCase();

        for (String reserved : RESERVED_CHARACTERS) {
            if (val.contains(reserved)) {
                throw new ParameterException("Parameter " + name + " and value " + value + " contains reserved character from " + String.join(", ", RESERVED_CHARACTERS) + ".");
            }
        }

    }

}
