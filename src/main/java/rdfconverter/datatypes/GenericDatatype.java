/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdfconverter.datatypes;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.graph.impl.LiteralLabel;

/**
 *
 * @author Greg Albiston
 */
public class GenericDatatype extends BaseDatatype {

    public GenericDatatype(String typeURI) {
        super(typeURI);
    }

    /**
     * Convert a value of this datatype out to lexical form.
     *
     * @param value
     * @return
     */
    @Override
    public String unparse(Object value) {
        return (String) value;
    }

    /**
     * Parse a lexical form of this datatype to a value
     *
     * @param lexicalForm
     * @return
     * @throws DatatypeFormatException if the lexical form is not legal
     */
    @Override
    public Object parse(String lexicalForm) throws DatatypeFormatException {
        return lexicalForm;
    }

    /**
     * Compares two instances of values of the given datatype. .
     *
     * @param value1
     * @param value2
     * @return
     */
    @Override
    public boolean isEqual(LiteralLabel value1, LiteralLabel value2) {
        return value1.getDatatype() == value2.getDatatype()
                && value1.getValue().equals(value2.getValue());
    }

}
