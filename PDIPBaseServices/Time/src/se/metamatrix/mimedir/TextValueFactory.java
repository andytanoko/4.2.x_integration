/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

import java.text.ParseException;

public class TextValueFactory extends ValueFactory
{
    public String getValueTypeName()
    {
	return "text";
    }

    public Object parseMimedirString(String in)
	throws ParseException
    {
	char source[] = new char[in.length()];
	StringBuffer target = new StringBuffer(in.length());
	source = in.toCharArray();
	boolean nextEscaped = false;
	for(int i = 0; i < source.length; i++) {
	    if(nextEscaped) {
		nextEscaped = false;
		if(source[i] == 'n' || source[i] == 'N') {
		    target.append('\n');
		} else if(source[i] == '\\') {
		    target.append('\\');
		} else if(source[i] == ',') {
		    target.append(',');
		} else {
		    throw new ParseException("Unknown escape code", i);
		}
	    } else {
		if(source[i] == '\\') {
		    nextEscaped = true;
		} else if(source[i] == ',') {
		    throw new ParseException("Unescaped comma is invalid", i);
		} else {
		    target.append(source[i]);
		}
	    }
	}
	return target.toString();
    }

    public String mimedirRepresentation(Object obj)
    {
	throw new Error("not implemented yet");
    }

    public boolean canHaveMultipleValues()
    {
	return true;
    }
}
