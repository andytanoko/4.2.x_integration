/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */

package se.metamatrix.mimedir;

/**
 * This is a quite peculiar class, that is used to parse a string that
 * contain either a date value or a datetime value (such as the
 * dtstart in RFC2445). It returns an se.metamatrix.mimedir.ResDate
 * object. Please observe that the parsing of values that uses the
 * default timezone this class will depend on the timezone settings of
 * the computer it runs on.
 *
 * @author Daniel Resare <noa@metamatrix.se>
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;

public class DateTimeValueFactory extends ValueFactory
{
    static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat dateTimeFormat = new
		SimpleDateFormat("yyyyMMdd'T'HHmmss");

    public DateTimeValueFactory()
    {
		timeFormat.setLenient(false);
		dateTimeFormat.setLenient(false);
    }

    /**
     * Returns the exact name of this ValueType as defined in RFC2425
     * 5.8.4. if available.
     */
    public String getValueTypeName()
    {
		return "datetime";
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
		Date when = null;
		if(in.indexOf("T") == -1) {
			when = timeFormat.parse(in);
			return new ResDate(when, false);
		} else {
			if(in.indexOf("Z") != -1) {
				dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				in = in.substring(0,15);
			}
			when = dateTimeFormat.parse(in);
			return new ResDate(when, true);
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
		return true;
    }
}
