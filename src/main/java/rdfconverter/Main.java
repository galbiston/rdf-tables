package rdfconverter;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //1) Input folder/file to convert
        //- folder then use input filename to build. Get extension from serialisation.
        //2) Output folder/file to write to [optional], see above, default to same folder
        //- file then consildate into single file
        //3) Output serialisation - ttl, nt, nq, json-ld, json, xml
        //4) Separator value - COMMA, TAB, SPACE
        //5) Prefixes file
        //6) Datatypes file
        //7) Owl NamedIndividual
        //--help switch
        //--props file configuration
        //File inputFile = new File("");
        //Model model = ModelFactory.createDefaultModel();
        //FileConverter.writeToModel(inputFile, model);
    }
}
