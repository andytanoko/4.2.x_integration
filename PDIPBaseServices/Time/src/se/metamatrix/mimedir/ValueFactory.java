/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

/**
 * ValueFactory is an abstract class whose subclasses implements
 * factories that from the contents of a mime directory value.
 *
 * @author Daniel Resare <noa@metamatrix.se>
 */

import java.text.ParseException;

public abstract class ValueFactory
{
    /**
     * Returns the exact name of this ValueType as defined in RFC2425
     * 5.8.4. if available.
     */
   public abstract String getValueTypeName();

    /**
     * Returns a String representation of the data in o suitable for
     * inclusion into a MIME directory datastream.
     */
   public abstract String mimedirRepresentation(Object o);

    /**
     * Parses a string in mimedir format and returns an object of its
     * own type that represent the same information. For example there 
     * could be a MimedirDate class that is a subclass of
     * java.util.Date that implements the ValueType interface. To
     * instantiate a new MimedirDate that represent a date in the
     * string 20000307T0927Z you use this method.
     */
   public abstract Object parseMimedirString(String in) throws ParseException;

    /**
     * Indicates to the Parser weather contentlines of this type can
     * contain multiple values. Multple values is then assumed to be
     * separated by comma in the value part of the contentline and
     * will generate multiple calls to ContentHandler.contentLine
     */
  public  abstract boolean canHaveMultipleValues();
}
