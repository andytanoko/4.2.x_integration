/*
 Copyright © 2000, 2001 Metamatrix Development & Consulting AB.

 This file is subject to the terms and conditions of the GNU General Public
 License.  See the file COPYING in the main directory of this archive
 for more details.

 */
package se.metamatrix.mimedir;

public class Value
{
    String name;
    String valueTypeName;

    /**
     * Creates a new Value object with the specified name and
     * valueTypeName
     */
    public Value(String name, String valueTypeName)
    {
        this.name = name;
        this.valueTypeName = valueTypeName;
    }

   /**
    * Returns the name of this value
    */
    public String getName(){
        return name;
    }

   /**
    * Returns the name of the valuetype of this value
    */
    public String getValueTypeName(){
        return valueTypeName;
    }

}
