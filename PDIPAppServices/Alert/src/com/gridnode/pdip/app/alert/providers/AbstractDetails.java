/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractDetails.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 10 Oct 2001    Srinath             Creation
 * 27 Feb 2003    Neo Sok Lay         Implement Serializable.
 * 16 May 2003    Neo Sok Lay         Add method to get field formatted to a pattern.
 * Mar 03 2006    Neo Sok Lay         Use generics
 */

package com.gridnode.pdip.app.alert.providers;

import java.text.DecimalFormat;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * This helpers class will be extended by the other modules/application when the
 * alert needs to be triggered.
 * <BR>
 * This class is only used when the user provides the data as <%ObjectName.key%>.
 * This class is also used to retrieves the value of this.
 *
 * @author Srinath
 */

public abstract class AbstractDetails
  implements    IDataProvider,
                Serializable
{
  public static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy 'at' hh:mm:ss a";

  protected HashMap<String,Object> content = new HashMap<String,Object>();

  public AbstractDetails()
  {
  }

  /**
   * Get the value of a field, formatted to a specified pattern.
   *
   * @param fieldName The name of the field.
   * @param pattern The pattern to format to. <b>null</b> if pattern is not
   * required, in which case, default pattern may be applied to certain value
   * types like Date, Double, or Float.
   * @return The field value, formatted to the specified pattern.
   */
  public String get(String fieldName, String pattern)
  {
    Object value = getFieldValue(fieldName);
    if (value == null)
      return null;

    String val = null;

    if (value instanceof Date)
    {
      val = formatDate(value, pattern);
    }
    else if(value instanceof Number)  // Value obtained is of type Number
    {
      val = formatNumber(value, pattern);
    }

    return (val == null ? value.toString() : val);
  }

  /**
   * Get the value of a field, with no specific formatting pattern. The field
   * value will be formatted using default pattern.
   *
   * @param fieldName The name of the field.
   * @return The value of the field, formatted using default pattern.
   */
  public String get(String fieldName)
  {
    return get(fieldName, null);
    /*030516NSL call get(fieldName, pattern) with no pattern
    Object value = getFieldValue(fieldName);
    if (value == null)
      return null;

    if(value instanceof Date)
    {
      String pattern = null;
      SimpleDateFormat formatter;
      try
      {
        pattern = (String)content.get(TYPE_PATTERN);
      }
      catch(Exception e)
      {
      }
      if(pattern == null)
      {
        formatter = new SimpleDateFormat("dd.MM.yyyy 'at' hh:mm:ss a");
      }
      else
      {
        formatter = new SimpleDateFormat(pattern);
      }
      return formatter.format((Date)value);
    }
    else if(value instanceof Double || value instanceof Float)
    {
      return Float.toString(((Number)value).floatValue());
    }
    else
      return value.toString();
    */
  }

  public String getType()
  {
    return TYPE_NORMAL;
  }

  protected Object getFieldValue(String fieldName)
  {
    return content.get(fieldName);
  }

  protected void set(String fieldName, Object val)
  {
    content.put(fieldName, val);
  }

  protected static boolean isEmptyStr(String val)
  {
    return val == null || val.trim().length() == 0;
  }

  /**
   * Format a date value to a specified pattern.
   *
   * @param date The date object.
   * @param pattern The pattern to format to. <b>null</b> to use default pattern.
   * @return The formatted date value string. <b>null</b> if problems encountered during
   * formatting.
   */
  protected static String formatDate(Object date, String pattern)
  {
    String val = null;

    if (isEmptyStr(pattern))
      pattern = DEFAULT_DATE_PATTERN;

    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat(pattern);
      val = formatter.format(date);
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
    }
    return val;
  }

  /**
   * Format a numeric value to a specified pattern.
   *
   * @param number The number object.
   * @param pattern The pattern to format to. <b>null</b> to use default pattern.
   * @return The formatted numeric value string. <b>null</b> if problems
   * encountered during formatting.
   */
  protected static String formatNumber(Object number, String pattern)
  {
    String val = null;

    if (isEmptyStr(pattern))
    {
      if(number instanceof Double || number instanceof Float)
        val = Float.toString(((Number)number).floatValue());
    }
    else
    {
      try
      {
        DecimalFormat df = new DecimalFormat(pattern);
        val = df.format(number);
      }
      catch(Exception e)
      {
        e.printStackTrace(System.err);
      }
    }

    return val;
  }
}