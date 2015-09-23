/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

/**
 * A class that encapsulates a parameter pair of key and value. Please 
 * note that this is an immutable object that can only be changed at
 * creation time.
 */

public class Parameter
{
    String name;
    String value;
    
    public Parameter(String name, String value)
    {
	this.name = name;
	this.value = value;
    }

    public String getName()
    {
	return name;
    }

    public String getValue()
    {
	return value;
    }

}
