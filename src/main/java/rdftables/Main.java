package rdftables;

import com.beust.jcommander.JCommander;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rdftables.cli.ArgsConfig;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArgsConfig argsConfig = new ArgsConfig();
        JCommander.newBuilder()
                .addObject(argsConfig)
                .build()
                .parse(args);

        //File inputFile = new File("");
        //Model model = ModelFactory.createDefaultModel();
        //FileConverter.writeToModel(inputFile, model);
    }
}
