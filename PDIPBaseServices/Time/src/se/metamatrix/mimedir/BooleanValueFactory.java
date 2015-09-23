/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */

package se.metamatrix.mimedir;

/**
 *
 * @author Daniel Resare <noa@metamatrix.se>
 */

import java.text.ParseException;

public class BooleanValueFactory extends ValueFactory
{
    /**
     * Returns the exact name of this ValueType as defined in RFC2425
     * 5.8.4. if available.
     */
    public String getValueTypeName()
    {
	return "boolean";
    }

    /**
     * Returns a String representation of the data in o suitable for
     * inclusion into a MIME directory datastream.
     */
    public String mimedirRepresentation(Object o)
    {
	throw new Error("not implemented yet");
    }

    /**
     * Parses a string in mimedir format and returns an object of its
     * own type that represent the same information. For example there 
     * could be a MimedirDate class that is a subclass of
     * java.util.Date that implements the ValueType interface. To
     * instantiate a new MimedirDate that represent a date in the
     * string 20000307T0927Z you use this method.
     */
    public Object parseMimedirString(String in)
	throws ParseException
    {
	if(in.compareToIgnoreCase("true") == 0 
	   || in.compareToIgnoreCase("false") == 0) {
	    return new Boolean(in);
	} else {
	    throw new ParseException("'"+in+"' is not 'TRUE' or 'FALSE'",0);
	}
    }


    /**
     * Indicates to the Parser weather contentlines of this type can
     * contain multiple values. Multple values is then assumed to be
     * separated by comma in the value part of the contentline and
     * will generate multiple calls to ContentHandler.contentLine
     */
    public boolean canHaveMultipleValues()
    {
	return false;
    }
}