/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdftables.cli;

import com.beust.jcommander.IStringConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 *
 */
public class PropsConverter implements IStringConverter<Properties> {

    @Override
    public Properties convert(String propsFilePath) {

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propsFilePath));
        } catch (IOException ex) {
            throw new AssertionError(ex.getLocalizedMessage());
        }

        return props;
    }

}
