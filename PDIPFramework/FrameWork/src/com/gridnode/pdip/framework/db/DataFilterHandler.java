/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Sep 04 2001    Liu Dan             (De)Serialize data filter from/to String.
 *                                    Convert JDO data filter to IDataFilter.
 * Jun 14 2002    Neo Sok Lay         Change implementation for (de)serialize
 *                                    (from)to String. Check for null or empty
 *                                    string.
 * Aug 22 2002    Neo Sok Lay         Use the new Object-xml (de)serialization.
 * Oct 16 2003    Neo Sok Lay         Include conversion to/from SortFilter.
 * Feb 07 2007		Alain Ah Ming				Use new log error codes
 */
package com.gridnode.pdip.framework.db;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;

//import org.exolab.castor.mapping.Mapping;

import java.io.File;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.Vector;

/**
 * Handles serialization and de-serialization of data filters to and from
 * xml file.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 1.0a build 0.9.9.6
 */
public class DataFilterHandler implements IDataHandler
{
  private static final ObjectXmlSerializer _ser = new ObjectXmlSerializer();
  private static final XmlObjectDeserializer _deser = new XmlObjectDeserializer();

  /**
   * Convert a data filter to its JDO representation.
   *
   * @param filter The data filter to convert
   * @return Converted JDO data filter, or <B>null</B> if <I>filter</I> is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static DataFilter toJDO(IDataFilter filter)
  {
    if (filter == null)
      return null;

    DataFilter convFilter = new DataFilter();

    convFilter.setNegate(filter.hasNegation());
    convFilter.setValueFilter(toJDO(filter.getValueFilter(), filter));
    convFilter.setLeftFilter(toJDO(filter.getLeftFilter()));
    convFilter.setRightFilter(toJDO(filter.getRightFilter()));
    convFilter.setConnector(toName(filter.getConnector(), ((DataFilterImpl)filter).getFilterFactory()));
    convFilter.setNextFilter(toJDO(filter.getNextFilter()));
    convFilter.setNextConnector(toName(filter.getNextConnector(), ((DataFilterImpl)filter).getFilterFactory()));
    convFilter.setSortFilters(toJDO(filter.getOrderFields(), filter.getSortOrders()));

    return convFilter;
  }

  /**
   * Converts the specified order fields and sort orders to SortFilter objects
   * for serialization.
   * 
   * @param orderFields Array of fields to sort by.
   * @param sortOrders Array of sort order for each corresponding order field.
   * @return Vector for SortFilter objects converted. <b>null</b> if
   * orderFields has a size of 0.
   */
  public static Vector toJDO(Object[] orderFields, boolean[] sortOrders)
  {
    if (orderFields.length==0)
      return null;
    
    Vector v = new Vector(orderFields.length);
      
    for (int i=0; i<orderFields.length; i++)
    {
      SortFilter filter = new SortFilter();
      filter.setSortField(orderFields[i]);
      filter.setSortOrder(sortOrders[i]);
      v.add(filter);
    }
    
    return v;
  }
  
  public static IDataFilter fromJDO(DataTypeFilter typeF)
  {
    // System.out.println("fromJOD(typefilter): filter is "+typeF);

    if (typeF == null)
      return null;

    return fromJDO(typeF.getFilter(), typeF.getType());
  }

  public static IDataFilter fromJDO(DataFilter filter, String type)
  {
    if (filter == null)
      return null;

    //Convert into a OQL data filter
    DataFilterImpl convFilter = new DataFilterImpl(type);
    DataFilterFactory factory = convFilter.getFilterFactory();

    boolean negate = filter.hasNegation();

    //filter tree components
    IDataFilter leftFilter = fromJDO(filter.getLeftFilter(), type);
    IDataFilter rightFilter = fromJDO(filter.getRightFilter(), type);
    FilterConnector conn = (filter.getConnector()==null? null :
      (FilterConnector)factory.getComponent(filter.getConnector()));

    //a filter tree or normal criteria filter
    if (leftFilter != null && rightFilter != null && conn != null)
      convFilter = new DataFilterImpl(type, leftFilter, conn, rightFilter);
    else
    {
      IValueFilter valFilter =
        fromJDO(filter.getValueFilter(), factory);
      if (valFilter != null)
        convFilter = new DataFilterImpl(type, valFilter, negate);
    }

    //next filter in the chain
    IDataFilter nextFilter = fromJDO(filter.getNextFilter(), type);
    FilterConnector nextConn = (filter.getNextConnector()==null? null :
      (FilterConnector)factory.getComponent(filter.getNextConnector()));

    if (nextFilter != null && nextConn != null)
    {
      convFilter.addFilter(nextConn, nextFilter);
    }
    
    // convert the sort filters
    Vector sortFilterV = filter.getSortFilters();
    if (sortFilterV != null)
    {
      SortFilter[] sortFilters = (SortFilter[])sortFilterV.toArray(new SortFilter[sortFilterV.size()]);
      for (int i=0; i<sortFilters.length; i++)
      {
        convFilter.addOrderField(sortFilters[i].getSortField(), sortFilters[i].getSortOrder());
      }
    }
    //finish conversion
    return convFilter;
  }

  private static IValueFilter fromJDO(
    ValueFilter valFilter, DataFilterFactory factory)
  {
    IValueFilter convFilter = null;

    //Find the filter operator
    Object field = valFilter.getFilterField();
//    System.out.println("Filter field class:"+field.getClass().getName());
//    System.out.println("Filter Field:"+field);
    FilterOperator op = (valFilter.getOperator() == null? null :
      (FilterOperator)factory.getComponent(valFilter.getOperator()));

    if (op != null)
    {
//      System.out.println("Single value:"+valFilter.getSingleValue());
      //Single value filter
      convFilter = new SingleValueFilter(field, op, valFilter.getSingleValue());
    }
    else if (!valFilter.getDomainValues().isEmpty())
    {
//      System.out.println("Domain value:"+valFilter.getDomainValues());
      //Domain value filter
      convFilter = new DomainValueFilter(field, valFilter.getDomainValues());
    }
    else
    {
//      System.out.println("Low value:"+valFilter.getLowValue());
//      System.out.println("High value:"+valFilter.getHighValue());
      //Range value filter
      convFilter = new RangeValueFilter(field, valFilter.getLowValue(),
        valFilter.getHighValue());
    }
    return convFilter;
  }

  /**
   * Find the key of a connector in its filter factory.
   *
   * @param connector The connector
   * @param factor Filter factory of the connector
   * @return The key of <I>connector</I> in <I>factory</I>, or <B>null</B> if
   * <I>connector</I> is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String toName(
    FilterConnector connector, DataFilterFactory factory)
  {
    if (connector == null)
      return null;
    return (String)factory.findComponentKey(connector);
  }

  /**
   * Find the key of an operator in its filter factory.
   *
   * @param op The operator
   * @param factor Filter factory of the operator
   * @return The key of <I>op</I> in <I>factory</I>, or <B>null</B> if <I>op</I>
   * is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String toName(FilterOperator op, DataFilterFactory factory)
  {
    if (op == null)
      return null;
    return (String)factory.findComponentKey(op);
  }

  /**
   * Converts a criteria filter to its JDO representation.
   *
   * @param valFilter The criteria filter to convert
   * @param filter The data filter containing the criteria filter
   * @return Converted JDO criteria filter, or <B>null</B> if <I>valFilter</I>
   * is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static ValueFilter toJDO(IValueFilter valFilter, IDataFilter filter)
  {
    if (valFilter == null)
      return null;

    if (valFilter instanceof SingleValueFilter)
      return toJDO((SingleValueFilter)valFilter, filter);
    else if (valFilter instanceof RangeValueFilter)
      return toJDO((RangeValueFilter)valFilter);
    else if (valFilter instanceof DomainValueFilter)
      return toJDO((DomainValueFilter)valFilter);

    return null;
  }

  /**
   * Converts a "single" type criteria filter to its JDO representation.
   *
   * @param valFilter The "single" type criteria filter
   * @param filter The data filter containing the criteria filter
   * @return Converted JDO criteria filter, or <B>null</B> if <I>valFilter</I>
   * is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static ValueFilter toJDO(SingleValueFilter valFilter, IDataFilter filter)
  {
    if (valFilter == null)
      return null;

    ValueFilter convFilter = new ValueFilter();
    convFilter.setFilterField(valFilter.getFilterField());
    convFilter.setOperator(toName(valFilter.getOperator(),
      ((DataFilterImpl)filter).getFilterFactory()));
    convFilter.setSingleValue(valFilter.getSingleValue());
    return convFilter;
  }

  /**
   * Converts a "range" type criteria filter to its JDO representation.
   *
   * @param valFilter The "range" type criteria filter
   * @param filter The data filter containing the criteria filter
   * @return Converted JDO criteria filter, or <B>null</B> if <I>valFilter</I>
   * is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static ValueFilter toJDO(RangeValueFilter valFilter)
  {
    if (valFilter == null)
      return null;

    ValueFilter convFilter = new ValueFilter();
    convFilter.setFilterField(valFilter.getFilterField());
    convFilter.setHighValue(valFilter.getHighValue());
    convFilter.setLowValue(valFilter.getLowValue());
    return convFilter;
  }

  /**
   * Converts a "domain" type criteria filter to its JDO representation.
   *
   * @param valFilter The "single" type criteria filter
   * @param filter The data filter containing the criteria filter
   * @return Converted JDO criteria filter, or <B>null</B> if <I>valFilter</I>
   * is null.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static ValueFilter toJDO(DomainValueFilter valFilter)
  {
    if (valFilter == null)
      return null;

    ValueFilter convFilter = new ValueFilter();
    convFilter.setFilterField(valFilter.getFilterField());
    convFilter.setDomainValues(new Vector(valFilter.getDomainValues()));
    return convFilter;
  }

  /**
   * Serialize a data filter to a file.
   *
   * @param filter The data filter to serialize.
   * @param type Type of the data filter.
   * @param outputFile Name of Output file.
   * @return Serialized data filter file, or <B>null</B> if error in
   * serializing the data filter.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static File serializeToFile(
    IDataFilter filter, String type, String outputFile)
  {
  	String mn = "serializeToFile";
    //convert the data filter to JDO representation
    DataFilter dFilter = toJDO(filter);

    //wrap the data filter in a group
    DataFilterGroup group = new DataFilterGroup();

    if (dFilter != null)
    {
      DataTypeFilter typeFilter = new DataTypeFilter();
      typeFilter.setFilter(dFilter);
      typeFilter.setType(type);
      typeFilter.setAndOr("");
      group.addFilter(typeFilter);
    }
    try
    {
      _ser.serialize(group, outputFile);

      return new File(outputFile);
    }
    catch (Exception ex)
    {
      logError(ILogErrorCodes.OBJECT_SERIALIZE_TO_XML_FILE, mn, "Failed to serialize filter group. Error: "+ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * Serialize a data filter to a string.
   *
   * @param filter The data filter to serialize.
   * @param type Type of the data filter.
   * @param writer The writer to use for output
   *
   * @since 1.0a build 0.9.9.6
   */
  public static String serializeToString(
    IDataFilter filter, String type)
  {
  	String mn = "serializeToString";
    if (filter == null)
      return null;

    StringWriter writer = new StringWriter();

    //convert the data filter to JDO representation
    DataFilter dFilter = toJDO(filter);
    DataTypeFilter typeFilter = new DataTypeFilter();
    if (dFilter != null)
    {
      typeFilter.setFilter(dFilter);
      typeFilter.setType(type);
      typeFilter.setAndOr("");
    }
    try
    {
      _ser.serialize(typeFilter, writer);
      return writer.toString();
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.OBJECT_SERIALIZE_TO_XML_STRING, mn, "Failed to serialize type filter. Error: "+ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * De-serialize a data filter xml file.
   *
   * @param inFile Input data filter file
   * @return De-serialized data filter group, or <B>null</B> if error
   * de-serializing data filter from file.
   *
   * @since 1.0a build 0.9.9.6
   */
  public static DataFilterGroup deserializeFromFile(String inFile)
  {
  	String mn = "deserializeFromFile";
    try
    {
      return (DataFilterGroup)_deser.deserialize(DataFilterGroup.class, inFile);
    }
    catch (Exception ex)
    {
      logError(ILogErrorCodes.OBJECT_DESERIALIZE_FROM_XML_FILE, mn, "Failed to deserialize filter group from file: ["+inFile+"]. Error: "+ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * De-serialize a data filter xml string.
   *
   * @param filterStr Input data filter string
   * @return De-serialized IDataFilter, or <B>null</B> if error
   * de-serializing data filter from string.
   *
   * @since 1.1
   */
  public static IDataFilter deserializeFromString(String filterStr)
  {
    if (filterStr == null || filterStr.trim().length() == 0)
      return null;

    StringReader reader = new StringReader(filterStr);
    try
    {
      DataTypeFilter typeFilter = (DataTypeFilter)_deser.deserialize(
                                    DataTypeFilter.class, reader);
      return fromJDO(typeFilter);
    }
    catch (Exception ex)
    {
      Log.error(ILogErrorCodes.OBJECT_DESERIALIZE_FROM_XML_STRING, Log.DB, "[DataFilterHandler.deserializeFromString] Error: "+ex.getMessage(), ex);
      return null;
    }
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(DataFilterHandler.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }
}

