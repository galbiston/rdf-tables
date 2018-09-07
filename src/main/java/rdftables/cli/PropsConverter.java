/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.IStringConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 *
 */
public class PropsConverter implements IStringConverter<HashMap<String, String>> {

    @Override
    public HashMap<String, String> convert(String propsFilePath) {

        HashMap<String, String> map = new HashMap<>();

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(propsFilePath));
            props.forEach((k, v) -> map.put((String) k, (String) v));
        } catch (IOException ex) {
            throw new AssertionError(ex.getLocalizedMessage());
        }

        return map;
    }

}
