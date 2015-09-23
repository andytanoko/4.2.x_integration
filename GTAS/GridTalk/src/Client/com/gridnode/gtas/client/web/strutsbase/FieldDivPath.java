/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldDivPath.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-08-25     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.Serializable;

import com.gridnode.gtas.client.utils.StaticUtils;

/**
 * A DivPath allows you to work with the field based diversion path format for mapping names.
 * This is a string of the form "field:name:mode:value" where name is the name of the field,
 * mode is one of "create", "update", or "view" and value is the string representation of the value
 * in a format suitable for use in keyField based url. The named field must be a FER field as the metainfo
 * is used for looking up the name of the keyField and target entity type. For single value fields it
 * is not necessary to specify a value as this can be retrieved from the actionForm, and the mode is also
 * optional and defaults to the current mode, or create if there is no value in the actionForm.
 * To perform a field based diversion use the javascript divertTo method as normal but instead of the name
 * of an ActionForward use the DivPath format. (ie: "field:processInstanceUid::"). FBDs only work when using
 * an EntityDispatchAction subclass (unless you write custom code to interpret it yourself). 
 */
public class FieldDivPath implements Serializable
{
  public static final String SEPERATOR = ":";
  public static final String DIV_PATH_PREFIX = "field" + SEPERATOR;
  
  public static final String MODE_CREATE = "create";
  public static final String MODE_UPDATE = "update";
  public static final String MODE_VIEW = "view";
  public static final String MODE_DEFAULT = "";
  public static final String VALUE_DEFAULT = "";
  
  private static final String[] _allowedModes = new String[]
  {
    MODE_DEFAULT, MODE_CREATE, MODE_UPDATE, MODE_VIEW,
  };
  
  private String _fieldName;
  private String _mode;
  private String _value;
  
  /**
   * No-args constructor
   */
  public FieldDivPath()
  {
    
  }
  
  /**
   * Constructor that takes a string in the divPath format
   * @param divPath
   */
  public FieldDivPath(String divPath)
  {
    setDivPath(divPath);
  }
  
  /**
   * Returns a String of the form "FieldDivPath[x]" where x is a divPath in the usual format, or the
   * word "null" if the fieldName has yet to be initialised.
   * @return string
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("FieldDivPath[");
    buffer.append(getDivPath());
    buffer.append("]");
    return buffer.toString();
  }
  
  /**
   * Clear the fieldName, mode, and value information in this DivPath object.
   */
  public void reset()
  {
    _fieldName = null;
    _mode = null;
    _value = null;
  }
  
  /**
   * Set the fieldName, mode, and value information based on the supplied divPath string.
   * If you pass a null or empty string then the current information in the object is cleared.
   * @param divPath
   * @throws IllegalArgumentException if the string is not in a valid format
   */
  public void setDivPath(String divPath)
  {
    reset();
    if( StaticUtils.stringEmpty(divPath) )
    {
      return;
    }
    else
    {
      if(!divPath.startsWith(DIV_PATH_PREFIX))
      {
        throw new IllegalArgumentException("DivPath does not start with:"
                                            + DIV_PATH_PREFIX
                                            + " \""
                                            + divPath 
                                            + "\"");
      }
      String path = divPath.substring( DIV_PATH_PREFIX.length() );
      //We dont want to use a tokenizer as the value may itself contain SEPERATORs
      //however we want to treat everything after the valueBreak as the value
      int modeBreak = path.indexOf(SEPERATOR);
      int valueBreak = path.indexOf(SEPERATOR, modeBreak + 1 );
      if( (modeBreak == -1) || (valueBreak == -1) )
      {
        throw new IllegalArgumentException("Malformed divPath \""
                                            + divPath
                                            + "\"");
      }
      String fieldName = path.substring(0, modeBreak);
      String mode = path.substring(modeBreak + 1, valueBreak);
      String value = path.substring(valueBreak + 1);
      
      setFieldName(fieldName);
      setMode(mode);
      setValue(value);
    }
  }
  
  /**
   * Return the contents of this DivPath in the divPath format.
   * @return divPath
   */
  public String getDivPath()
  {
    if(_fieldName == null)
    {
      return null;
    }
    else
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append( DIV_PATH_PREFIX );
      buffer.append( getFieldName() );
      buffer.append( SEPERATOR );
      buffer.append( getMode() );
      buffer.append( SEPERATOR );
      buffer.append( getValue() );
      return buffer.toString();
    }
  }
  
  /**
   * Return the fieldName element of this divPath or null if it is not set.
   * @return fieldName
   */
  public String getFieldName()
  {
    return _fieldName;
  }

  /**
   * Return the mode of this divPath or empty string "" if it is not set
   * @return mode
   */
  public String getMode()
  {
    return _mode == null ? "" : _mode;
  }

  /**
   * Return the value part of this divPath or empty string "" if it is not set
   * @return value
   */
  public String getValue()
  {
    return _value == null ? "" : _value;
  }

  /**
   * Set the fieldName for this divPath
   * @param fieldName
   */
  public void setFieldName(String string)
  {
    _fieldName = string;
  }

  /**
   * Set the mode for this divPath
   * @param mode
   */
  public void setMode(String string)
  {
    if(!( (string == null) || StaticUtils.arrayContains(_allowedModes, string) ))
    {
      throw new IllegalArgumentException("Invalid mode \"" + string + "\"");
    }
    if("".equals(string)) string = null;
      _mode = string;
  }

  /**
   * Set the value for this divPath
   * @param value
   */
  public void setValue(String string)
  {
    if("".equals(string)) string = null;
    _value = string;
  }

  /**
   * Returns true if the mappingName is not null and begins with the "field:" prefix.
   * Note that the remainder of the string is not checked to see that it is correctly formated.
   * @param mappingName
   * @return isDivPath true if mappingName starts with "field:"
   */
  public static final boolean isDivPath(String mappingName)
  {
    return ((mappingName != null) && mappingName.startsWith(DIV_PATH_PREFIX));
  }

  /**
   * Temporary main method for testing and debugging purposes.
   * This really should be implemented as a JUnit test but Im being
   * lazy...
   * @param args Ignored
   */
  public static void main(String[] args)
  {
    FieldDivPath dp = new FieldDivPath();
    dp.setDivPath("field:bob:update:42");
    dp.setDivPath("field:x:y:z");
    dp.setDivPath("field:aaaa:bbbb:bbbb");
    dp.setDivPath("field:test::");
    dp.setDivPath("field:slartibartfast::");
    
    
    System.out.println("dp=" + dp);
    
  }

}
