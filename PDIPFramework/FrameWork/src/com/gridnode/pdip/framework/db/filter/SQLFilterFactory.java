/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SQLFilterFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * May 16 2002    Neo Sok Lay         Format Boolean value to 1 and 0 for
 *                                    true and false respectively.
 * Oct 15 2003    Neo Sok Lay         Enable ORDER Syntax to support specification
 *                                    on ascending or descending orders.
 * Oct 17 2005    Neo Sok Lay         Change methods from private to protected to improve
 *                                    performance: quote(String), format(Object), 
 *                                    formatString(String)                                   
 * Oct 21 2005    Neo Sok Lay         Support SELECT syntax to support specification
 *                                    on distinct fields.  
 * Jan 25 2007    Neo Sok Lay         Handle Oracle EngineType to customize formatting of
 *                                    Date and Timestamp datatypes.     
 * Jun 09 2007    Tam Wei Xiang       Support Select MIN, MAX  
 */
package com.gridnode.pdip.framework.db.filter;

import java.awt.Color;
import java.awt.Font;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This factory produces filter components for SQL.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class SQLFilterFactory extends DataFilterFactory
{ 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 209317652914495991L;

  /**
   * Constructs a data filter factory for SQL.
   *
   * @since 1.0a build 0.9.9.6
   */
  public SQLFilterFactory()
  {
    super();
    _factoryName = "sql";
  }

  /**
   * Creates a "And" connector for SQL.
   *
   * @return "And" connector created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createAndConnector()
  {
    return createConnector("AND");
  }
  /**
   * Creates a "Or" connector for SQL.
   *
   * @return "Or" connector created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createOrConnector()
  {
    return createConnector("OR");
  }

  /**
   * Creates a "Equals" operator for SQL.
   *
   * @return "Equal" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createEqualOperator()
  {
    return new FilterOperator("=")
    {
      public String applySyntax(IValueFilter filter)
      {
        SingleValueFilter sFilter = (SingleValueFilter) filter;
        if (sFilter.getSingleValue() == null)
          return sFilter.getFilterField() + " IS NULL";

        return sFilter.getFilterField()
          + getName()
          + format(sFilter.getSingleValue());
      }
    };
  }

  /**
   * Creates a "Not Equal" operator for SQL.
   *
   * @return "Not Equal" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotEqualOperator()
  {
    return new FilterOperator("<>")
    {
      public String applySyntax(IValueFilter filter)
      {
        SingleValueFilter sFilter = (SingleValueFilter) filter;
        if (sFilter.getSingleValue() == null)
          return sFilter.getFilterField() + " IS NOT NULL";

        return sFilter.getFilterField()
          + getName()
          + format(sFilter.getSingleValue());
      }
    };
  }

  /**
   * Creates a "Greater than" operator.
   *
   * @return "Greater than" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createGreaterOperator()
  {
    return createInfixOperator(">");
  }

  /**
   * Create a "Greater or Equal" operator.
   *
   * @return "Greater or Equal" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createGreaterOrEqualOperator()
  {
    return createInfixOperator(">=");
  }

  /**
   * Create a "Less than" operator.
   *
   * @return "Less than" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLessOperator()
  {
    return createInfixOperator("<");
  }

  /**
   * Create a "Less than or Equal" operator.
   *
   * @return "Less than or Equal" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLessOrEqualOperator()
  {
    return createInfixOperator("<=");
  }

  /**
   * Create a "Not" operator.
   *
   * @return "Not" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotOperator()
  {
    return new FilterOperator("NOT")
    {
      public String applySyntax(String filterStr)
      {
        return getName() + "(" + filterStr + ")";
      }
    };
  }

  /**
   * Create a "Like" operator.
   *
   * @return "Like" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLikeOperator()
  {
    return new FilterOperator("LIKE")
    {
      public String applySyntax(IValueFilter filter)
      {
        SingleValueFilter sFilter = (SingleValueFilter) filter;
        Object value = sFilter.getSingleValue();
        return sFilter.getFilterField()
          + " "
          + getName()
          + " "
          + quote((value == null ? "" : formatString(value.toString())) + "%");
      }
    };
  }

  /**
   * Create a "In" operator.
   *
   * @return "In" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createInOperator()
  {
    return new FilterOperator("IN")
    {
      public String applySyntax(IValueFilter filter)
      {
        DomainValueFilter dFilter = (DomainValueFilter) filter;
        Object[] values = dFilter.getDomainValues().toArray();
        String filterStr = new String();
        if (values.length > 0)
        {
          filterStr = dFilter.getFilterField() + " " + getName() + "(";
          filterStr += format(values[0]);
          for (int i = 1; i < values.length; i++)
          {
            filterStr += "," + format(values[i]);
          }
          filterStr += ")";
        }
        return filterStr;
      }
    };
  }

  /**
   * Create a "Between" operator.
   *
   * @return "Between" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createBetweenOperator()
  {
    return new FilterOperator("BETWEEN")
    {
      public String applySyntax(IValueFilter filter)
      {
        RangeValueFilter rFilter = (RangeValueFilter) filter;
        return rFilter.getFilterField()
          + " "
          + getName()
          + " "
          + format(rFilter.getLowValue())
          + " AND "
          + format(rFilter.getHighValue());
      }
    };
  }

  /**
   * Create a "Locate" operator.
   *
   * @return "Locate" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLocateOperator()
  {
    return new FilterOperator("LIKE")
    {
      public String applySyntax(IValueFilter filter)
      {
        SingleValueFilter sFilter = (SingleValueFilter) filter;
        return sFilter.getFilterField()
          + " "
          + getName()
          + " "
          + quote("%" + formatString(sFilter.getSingleValue().toString()) + "%");
      }
    };
  }

  /**
   * Apply quotes to the string value.
   *
   * @param value The value to apply quotes
   * @return Quoted value.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String quote(String value)
  {
    return "'" + value + "'";
  }

  /**
   * Formats an object to its string representation in db.
   *
   * @param value The object to format
   * @return The formatted value in string
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String format(Object value)
  {
    if (value == null)
      return "NULL";

    /** @todo support for arithmetic values */
    //    if (value instanceof DBArithmeticValue)
    //      return value.toString();
    //    else
    if (value instanceof String)
      return quote(formatString((String) value));
    else if (value instanceof Boolean)
      return quote(formatBoolean((Boolean) value));
    else if (value instanceof Timestamp)
    {
      String ts = quote(formatTime((Timestamp)value));
      if (isOracle())
      {
        return "TO_TIMESTAMP("+ts+",'YYYY-MM-DD HH24:MI:SS.FF3')";
      }
      else
      {
        return ts; 
      }
    }
    else if (value instanceof Date)
    {
      String dt = quote(formatTime((Date) value));
      if (isOracle())
      {
        return "TO_DATE("+dt+",'YYYY-MM-DD HH24:MI:SS')";
      }
      else
      {
        return dt;
      }
    }
    else if (value instanceof Number)
      return value.toString();
    else if (value instanceof Color)
      return quote(formatColor((Color) value));
    else if (value instanceof Font)
      return quote(formatFont((Font) value));

    return "NULL";
  }

  private boolean isOracle()
  {
    return (getEngineType()!=null && getEngineType().toLowerCase().indexOf("oracle")>=0);
  }
  
  /**
   * Creates a connector.
   *
   * @param name The name of the connector to create
   * @return The created connector
   *
   * @since 1.0a build 0.9.9.6
   */
  private FilterConnector createConnector(String name)
  {
    return new FilterConnector(name)
    {
      public String applySyntax(String fStr, String nFilterStr)
      {
        return "(" + fStr + " " + getName() + " " + nFilterStr + ")";
      }
    };
  }

  /**
   * Creates an operator of infix order.
   *
   * @param name The name of the connector to create
   * @return The created operator
   *
   * @since 1.0a build 0.9.9.6
   */
  private FilterOperator createInfixOperator(String name)
  {
    return new FilterOperator(name)
    {
      public String applySyntax(IValueFilter filter)
      {
        SingleValueFilter sFilter = (SingleValueFilter) filter;
        return sFilter.getFilterField()
          + getName()
          + format(sFilter.getSingleValue());
      }
    };
  }

  /**
   * Format a date value for use in SQL statement.
   *
   * @param date The date value to format
   * @return The formatted date value string (JDBC format - YYYY-MM-DD HH:MM:SS)
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String formatTime(Date date)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    //format : YYYY-MM-DD HH:MM:SS
    return (
      cal.get(Calendar.YEAR)
        + "-"
        + (cal.get(Calendar.MONTH) + 1)
        + '-'
        + cal.get(Calendar.DAY_OF_MONTH)
        + " "
        + cal.get(Calendar.HOUR_OF_DAY)
        + ':'
        + cal.get(Calendar.MINUTE)
        + ":"
        + cal.get(Calendar.SECOND));
  }

  /**
   * Format a Timestamp value for use in SQL statement.
   *
   * @param ts The timestamp value to format
   * @return The formatted timestamp value string (JDBC format - YYYY-MM-DD HH:MM:SS.FFF)
   */
  public static String formatTime(Timestamp ts)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTime(ts);
    //format : YYYY-MM-DD HH:MM:SS.FFF
    return (
      cal.get(Calendar.YEAR)
        + "-"
        + (cal.get(Calendar.MONTH) + 1)
        + '-'
        + cal.get(Calendar.DAY_OF_MONTH)
        + " "
        + cal.get(Calendar.HOUR_OF_DAY)
        + ':'
        + cal.get(Calendar.MINUTE)
        + ":"
        + cal.get(Calendar.SECOND)
        + "."
        + cal.get(Calendar.MILLISECOND)
        );
  }

  /**
   * Format a date in milliseconds into a value string for use in SQL statement.
   *
   * @param t Milliseconds since January 1, 1970, 00:00:00 GMT.
   *          A negative number is the number of milliseconds before
   *          January 1, 1970, 00:00:00 GMT.
   * @return The formatted value string
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String formatTime(long t)
  {
    return formatTime(new Date(t));
  }

  /**
   * Format a Color object into a string of the RGB components separated
   * by commas.
   *
   * @param color The Color object to format
   * @return The formatted value string
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String formatColor(Color color)
  {
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }

  /**
   * Format a Font object into a string of the font components (font name,
   * font style, font size) separated by commas.
   *
   * @param font The Font object to format
   * @return The formatted value string
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String formatFont(Font font)
  {
    return font.getFontName() + "," + font.getStyle() + "," + font.getSize();
  }

  /**
   * Format a string to handle reserved chars in SQL statement.
   *
   * @param str The string to format
   * @return The formatted string value
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static String formatString(String str)
  {
    String newStr = new String();
    int start = 0;
    int index = str.indexOf("'");
    while (index != -1)
    {
      newStr += str.substring(start, ++index) + "'";
      start = index;
      index = str.indexOf("'", start);
    }
    newStr += str.substring(start);
    return newStr;
  }

  /**
   * Convert the Boolean value to 1 for true, and to 0 for false.
   *
   * @param val The boolean value.
   * @return "1" if val.booleanValue() is true, otherwise "0".
   */
  private static String formatBoolean(Boolean val)
  {
    return val.booleanValue() ? "1" : "0";
  }

  /**
   * Applys selection syntax for SQL.
   *
   * @param tableName The object to select on
   * @param fieldNames Names of the fields to select
   * @return The selection statement
   *
   * @since 1.0a build 0.9.9.6
   */
  /*031015NSL Use method from parent factory
  public String getSelectSyntax(String tableName, String[] fieldNames)
  {
    if (fieldNames == null || fieldNames.length == 0)
      return "SELECT * FROM "+tableName;
  
    String select = "SELECT "+fieldNames[0];
    for (int i=1; i<fieldNames.length; i++)
     select += (","+fieldNames[i]);
  
    select += (" FROM "+tableName);
    return select;
  }
  */

  /**
   * Applys Deletion syntax.
   *
   * @param objName The object to delete
   * @return The deletion statement
   *
   * @since 2.0
   */
  /*031015NSL Use method from parent factory
  public String getDeleteSyntax(String objName)
  {
    String delete = "DELETE FROM "+objName;
    return delete;
  }
  */

  /**
   * Applys Insert syntax.
   *
   * @param objName The object to insert into
   * @param objFieldNames Names of the fields in the insert statement
   * @return The Insert statement
   *
   * @since 2.0
   */
  public String getInsertSyntax(String objName, String[] objFieldNames)
  {
    /*031015NSL
    StringBuffer insert = new StringBuffer("INSERT INTO ");
    StringBuffer value = new StringBuffer(" VALUES(?");
    
    insert.append(objName).append("(").append(objFieldNames[0]);
    for (int i=1; i<objFieldNames.length; i++)
    {
      insert.append(",").append(objFieldNames[i]);
      value.append(",?");
    }
    insert.append(")").append(value.toString()).append(")");
    return insert.toString();
    */

    // cannot use parent method because there is extra parameter for the Values part of the syntax
    StringBuffer buff = new StringBuffer(objFieldNames[0]);
    StringBuffer valBuff = new StringBuffer("?");
    for (int i = 1; i < objFieldNames.length; i++)
    {
      buff.append(',').append(objFieldNames[i]);
      valBuff.append(",?");
    }
    String[] params =
      new String[] { objName, buff.toString(), valBuff.toString()};

    return formatSyntax(getInsertSyntax(), params);

  }

  /**
   * Applys Update syntax.
   *
   * @param objName The object to update into
   * @param objFieldNames Names of the fields in the update statement
   * @return The Update statement
   *
   * @since 2.0
   */
  public String getUpdateSyntax(String objName, String[] objFieldNames)
  {
    /*031015NSL 
    StringBuffer update = new StringBuffer("UPDATE ");
    update.append(objName).append(" SET ").append(objFieldNames[0]).append("=?");
    for (int i=1; i<objFieldNames.length; i++)
    {
      update.append(",").append(objFieldNames[i]).append("=?");
    }
    return update.toString();
    */

    // override parent method because the fields need a '=?'
    StringBuffer buff = new StringBuffer(objFieldNames[0]).append("=?");
    for (int i = 1; i < objFieldNames.length; i++)
    {
      buff.append(',').append(objFieldNames[i]).append("=?");
    }
    String[] params = new String[] { objName, buff.toString()};

    return formatSyntax(getUpdateSyntax(), params);
  }

  /*
   * Applys condition syntax for SQL.
   *
   * @param select The selection portion
   * @param condition The condition portion
   * @return The string with condition syntax applied on <I>select</I>
   * and <I>condition</I>.
   *
   * @since 1.0a build 0.9.9.6
   */
  /*031015BNSL Use parent factory.
  public String applyConditionSyntax(String select, String condition)
  {
    return select + " WHERE "+condition;
  }
  */

  /*
   * Applys order syntax on a SQL query.
   *
   * @param query The query
   * @param orderFields The fields to order by
   * @return The resulting query with ordering
   *
   * @since 1.0a build 0.9.9.6
   */
  /*031015NSL Use parent factory method
  public String applyOrderSyntax(String query, Object[] orderFields)
  {
    if (orderFields == null || orderFields.length == 0)
      return query;
  
    String str = query + " ORDER BY " + orderFields[0];
    for (int i=1; i<orderFields.length; i++)
      str += ( "," + orderFields[i] );
    return str;
  }
  */

  protected MessageFormat createSelectSyntax()
  {
    return new MessageFormat("SELECT {1} FROM {0}");
  }

  protected MessageFormat createSelectDistinctSyntax()
  {
    return new MessageFormat("SELECT DISTINCT {1} FROM {0}");
  }
  
  protected MessageFormat createSelectMaxSyntax()
  {
    return new MessageFormat("SELECT MAX({1}) FROM {0}");
  }
  
  protected MessageFormat createSelectMinSyntax()
  {
    return new MessageFormat("SELECT MIN({1}) FROM {0}");
  }
  
  protected MessageFormat createDeleteSyntax()
  {
    return new MessageFormat("DELETE FROM {0}");
  }

  protected MessageFormat createUpdateSyntax()
  {
    return new MessageFormat("UPDATE {0} SET {1}");
  }

  protected MessageFormat createInsertSyntax()
  {
    return new MessageFormat("INSERT INTO {0}({1}) VALUES({2})");
  }

  protected MessageFormat createConditionSyntax()
  {
    return new MessageFormat("{0} WHERE {1}");
  }

  protected MessageFormat createOrderSyntax()
  {
    return new MessageFormat("{0} ORDER BY {1}");
  }

  protected MessageFormat createAscendOrderSyntax()
  {
    return new MessageFormat("{0} ASC");
  }

  protected MessageFormat createDescendOrderSyntax()
  {
    return new MessageFormat("{0} DESC");
  }
}