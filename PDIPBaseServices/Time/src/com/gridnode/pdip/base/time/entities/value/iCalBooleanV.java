/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */

package com.gridnode.pdip.base.time.entities.value;

 
public class iCalBooleanV extends iCalValueV
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7222969798038881966L;
	protected  boolean _boolValue = false;

  public iCalBooleanV(int kind)
  {
    if(kind != iCalValueKind.ICAL_BOOLEAN_VALUE)
      throw new IllegalArgumentException("Wrong Kind="+kind);
    _kind = (short)kind;
  }
  
  public iCalBooleanV(boolean value)
  {
    _kind = iCalValueKind.ICAL_BOOLEAN_VALUE;
    _boolValue = value;
  }
	
	public boolean getBoolValue()
	{
	  return _boolValue;
	}
	
	static final int ICAL_BOOL_VALUE = 1;
	static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[]
	{
	  new ValueFieldInfo("_boolValue", "boolean", ICAL_BOOL_VALUE)
	};
	
  public ValueFieldInfo[] getFieldInfos()
 {
   return FieldInfo;
 }
 
  public iCalValueV parseStr(String in)
  {
      if (in.equalsIgnoreCase("true"))
      return new iCalBooleanV(true);
    if (in.equalsIgnoreCase("false"))
      return new iCalBooleanV(false);
    throw new IllegalArgumentException("'" + in + "' is not 'TRUE' or 'FALSE'");
  }
  
  public String toString()
  {
  	return ""+_boolValue;
  }
	
}
