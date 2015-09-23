/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OQLFilterFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * Oct 15 2003    Neo Sok Lay         Modify to fit into parent factory changes.
 *                                    This class is not used at the moment
 *                                    since Castor-JDO is not in used.
 * Oct 17 2005    Neo Sok Lay         Change methods from private to protected to improve
 *                                    performance: quote(String), format(Object), 
 *                                    formatString(String)                                   
 * Oct 21 2005    Neo Sok Lay         Support SELECT syntax to support specification
 *                                    on distinct fields.                                   
 */
package com.gridnode.pdip.framework.db.filter;

import java.awt.Color;
import java.awt.Font;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This factory produces filter components for OQL.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public class OQLFilterFactory
  extends    DataFilterFactory
{ 
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 4374313849053124373L;

  /**
   * Constructs a data filter factory for OQL.
   *
   * @since 1.0a build 0.9.9.6
   */
  public OQLFilterFactory()
  {
    super();
    _factoryName = "oql";
  }

  /**
   * Creates a "And" connector for OQL.
   *
   * @return "And" connector created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createAndConnector()
  {
    return createConnector("and");
  }

  /**
   * Creates a "Or" connector for OQL.
   *
   * @return "Or" connector created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createOrConnector()
  {
    return createConnector("or");
  }

  /**
   * Creates a "Equal" operator for OQL.
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
               SingleValueFilter sFilter = (SingleValueFilter)filter;
               if (sFilter.getSingleValue() == null)
                 return "is_undefined(" + sFilter.getFilterField() + ")";

               return sFilter.getFilterField() +
                      getName() +
                      format(sFilter.getSingleValue());
             }
           };
  }

  /**
   * Creates a "Not Equal" operator for OQL.
   *
   * @return "Not Equal" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotEqualOperator()
  {
    return new FilterOperator("!=")
           {
             public String applySyntax(IValueFilter filter)
             {
               SingleValueFilter sFilter = (SingleValueFilter)filter;
               if (sFilter.getSingleValue() == null)
                 return "is_defined(" + sFilter.getFilterField() + ")";

               return sFilter.getFilterField() +
                      getName() +
                      format(sFilter.getSingleValue());
             }
           };
  }

  /**
   * Creates a "Greater" operator for OQL.
   *
   * @return "Greater" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createGreaterOperator()
  {
    return createInfixOperator(">");
  }

  /**
   * Creates a "Greater or Equal" operator for OQL.
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
   * Creates a "Less than" operator for OQL.
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
   * Creates a "Less than or Equal" operator for OQL.
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
   * Creates a "Not" operator for OQL.
   *
   * @return "Not" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createNotOperator()
  {
    return new FilterOperator("not")
           {
             public String applySyntax(String filterStr)
             {
               return getName() + "(" + filterStr + ")";
             }
           };
  }

  /**
   * Creates a "Like" operator for OQL.
   *
   * @return "Like" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLikeOperator()
  {
    return new FilterOperator("like")
           {
             public String applySyntax(IValueFilter filter)
             {
               SingleValueFilter sFilter = (SingleValueFilter)filter;
               Object value = sFilter.getSingleValue();
               return sFilter.getFilterField() + " " +
                      getName() + " " +
                      quote(
                      (value==null?"":formatString(value.toString())) + "%" );
             }
           };
  }

  /**
   * Creates a "In" operator for OQL.
   *
   * @return "In" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createInOperator()
  {
    return new FilterOperator("in list")
           {
             public String applySyntax(IValueFilter filter)
             {
               DomainValueFilter dFilter = (DomainValueFilter)filter;
               Object[] values = dFilter.getDomainValues().toArray();
               String filterStr = new String();
               if (values.length > 0)
               {
                 filterStr = dFilter.getFilterField() + " " +
                             getName() + "(";
                 filterStr += format(values[0]);
                 for (int i=1; i<values.length; i++)
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
   * Creates a "Between" operator for OQL.
   *
   * @return "Between" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createBetweenOperator()
  {
    return new FilterOperator("between")
           {
             public String applySyntax(IValueFilter filter)
             {
               RangeValueFilter rFilter = (RangeValueFilter)filter;
               return rFilter.getFilterField() + " " +
                      getName() + " " +
                      format(rFilter.getLowValue()) +
                      " and " +
                      format(rFilter.getHighValue());
             }
           };
  }

  /**
   * Creates a "Locate" operator for OQL.
   *
   * @return "Locate" operator created.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected Object createLocateOperator()
  {
    return new FilterOperator("like")
           {
             public String applySyntax(IValueFilter filter)
             {
               SingleValueFilter sFilter = (SingleValueFilter)filter;
               return sFilter.getFilterField() + " " +
                      getName() + " " +
                      quote( "%" +
                      formatString(sFilter.getSingleValue().toString()) + "%" );
             }
           };
  }

  /**
   * Apply double quotes to a string value.
   *
   * @param value The value to apply quotes
   * @return Quoted value.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String quote(String value)
  {
    return "\"" + value + "\"";
  }

  /**
   * Apply single quotes to a string value.
   *
   * @param value The value to apply quotes
   * @return Quoted value.
   *
   * @since 1.0a build 0.9.9.6
   */
  private String singleQuote(String value)
  {
    return "'" + value + "'";
  }

  /**
   * Formats a value into a string.
   *
   * @param value The value to format
   * @return The formatted value in string
   *
   * @since 1.0a build 0.9.9.6
   */
  protected String format(Object value)
  {
    if (value == null) return "nil";

    //    if (value instanceof DBArithmeticValue)
    //      return value.toString();
    //    else
    if (value instanceof String)
      return quote(formatString((String)value));
    else if (value instanceof Boolean)
      return quote(value.toString());
    else if (value instanceof Date)
      return "timestamp " + singleQuote(formatTime((Date)value));
    else if (value instanceof Number)
      return value.toString();
    else if (value instanceof Color)
      return quote(formatColor((Color)value));
    else if (value instanceof Font)
      return quote(formatFont((Font)value));

    return "nil";
  }

  /**
   * Creates a connector.
   *
   * @param name Name of the connector to create
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
   * Create an operator of infix order: field op value.
   *
   * @param name Name of the operator to create
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
               SingleValueFilter sFilter = (SingleValueFilter)filter;
               return sFilter.getFilterField() +
                      getName() +
                      format(sFilter.getSingleValue());
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
    return (cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + '-' +
          cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY)
          + ':'+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
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
   * @return The formatted string value, in UTF-8 encoding
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

    try
    {
      return new String(newStr.getBytes("UTF-8"), "UTF-8");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return newStr;
    }
  }

  /**
   * Applys selection syntax for OQL.
   *
   * @param objName The object to select on
   * @param objFieldNames Names of the fields to select
   * @return The selection statement
   *
   * @since 1.0a build 0.9.9.6
   */
  /*031015NSL Use parent factory method... this only works for later versions of Castor.
    Old versions' syntax does not support multiple column projections
  public String getSelectSyntax(String objName, String[] objFieldNames)
  {
    if (objFieldNames == null || objFieldNames.length == 0)
      return "select obj from "+objName+ " obj";

    //only objFieldNames[0] can be retrieved
    return "select obj."+objFieldNames[0]+" from "+objName+ " obj";
  }
  */
  
  /*
   * Applys condition syntax for OQL.
   *
   * @param select The selection portion
   * @param condition The condition portion
   * @return The string with condition syntax applied on <I>select</I>
   * and <I>condition</I>.
   *
   * @since 1.0a build 0.9.9.6
   */
  /*031015NSL Use parent factory method
  public String applyConditionSyntax(String select, String condition)
  {
    return ((condition==null || condition.length()==0)?
            select :  (select + " where "+condition));
  }
  */
  
  /*
   * Applys order syntax on a OQL query.
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

    String str = query + " order by " + orderFields[0];
    for (int i=1; i<orderFields.length; i++)
      str += ( "," + orderFields[i] );
    return str;
  }
  */
  
  protected MessageFormat createSelectSyntax()
  {
    return new MessageFormat("select {1} from {0}");
  }

  protected MessageFormat createSelectDistinctSyntax()
  {
    return new MessageFormat("select distinct {1} from {0}");
  }
  
  protected MessageFormat createConditionSyntax()
  {
    return new MessageFormat("{0} where {1}");
  }

  protected MessageFormat createOrderSyntax()
  {
    return new MessageFormat("{0} order by {1}");
  }

  protected MessageFormat createAscendOrderSyntax()
  {
    return new MessageFormat("{0} asc");
  }

  protected MessageFormat createDescendOrderSyntax()
  {
    return new MessageFormat("{0} desc");
  }  
}