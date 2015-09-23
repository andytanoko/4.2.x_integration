/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectConverter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Apr 12 2002    Neo Sok Lay         Change convertToSQLFilter() to handle
 *                                    filter tree.
 * Dec 11 2002    Neo Sok Lay         Use MetaInfoFactory instead of EntityMetaInfoLoader.
 * Oct 15 2003    Neo Sok Lay         Support Sort Order conversion.
 * Oct 20 2003    Neo Sok Lay         To be able to support sorting even without any
 *                                    Value filtering.
 * Oct 21 2005    Neo Sok Lay         Support selection of fields without any Value filtering.                                   
 */
package com.gridnode.pdip.framework.db;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.DomainValueFilter;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.FilterOperator;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.IValueFilter;
import com.gridnode.pdip.framework.db.filter.RangeValueFilter;
import com.gridnode.pdip.framework.db.filter.SingleValueFilter;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * This class handles conversion of data filters produced by differing
 * factories. Conversion is required due to JDO-Entity implementations. No
 * conversion is required for pure JDO implementations.<P>
 *
 * Currently, it is capable of converting default->sql | oql,
 * sql -> default | oql, and oql -> default | sql data filters.<P>
 * Note that the conversion is very dependent on meta info of the entities.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class ObjectConverter
{
  /**
   * Convert a data filter to "sql" type.
   *
   * @param entity Class name of the entity involved in the filter
   * @param filter The data filter to convert
   * @return Converted "sql" data filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public static IDataFilter convertToSQLFilter(
    String entity, IDataFilter filter)
  {
    DataFilterImpl filterImpl = (DataFilterImpl)filter;
    String filterName =filterImpl.getFilterName();

    //don't convert if already a sql data filter
    if (filterName.equalsIgnoreCase("sql"))
      return filter;

    DataFilterImpl sqlFilter = new DataFilterImpl("sql");
    EntityMetaInfo metaInfo = MetaInfoFactory.getInstance().getMetaInfoFor(entity);

    DataFilterImpl prevFilter = null;
    DataFilterImpl currFilter = filterImpl;

    /**
     * @todo can use recursion to do the conversion
     */
    //go through the filter chain to convert each data filter
    do
    {
      IDataFilter leftFilter = currFilter.getLeftFilter();
      IDataFilter rightFilter = currFilter.getRightFilter();
      FilterConnector conn = currFilter.getConnector();

      if (leftFilter != null && rightFilter != null && conn != null)
      {
        //a filter tree
        leftFilter = convertToSQLFilter(entity, leftFilter);
        rightFilter = convertToSQLFilter(entity, rightFilter);
        convertAndAdd(sqlFilter, (DataFilterImpl)leftFilter,
          (DataFilterImpl)rightFilter, currFilter, prevFilter);
      }
      else if (currFilter.getValueFilter() != null)
      {
        //a normal filter
        IValueFilter valFilter = currFilter.getValueFilter();
        FieldMetaInfo fieldMeta =
          metaInfo.findFieldMetaInfo((Integer)valFilter.getFilterField());
        Object fieldName = fieldMeta.getSqlName();

        if (valFilter instanceof SingleValueFilter)
        {
          convertAndAdd(sqlFilter, fieldName, currFilter, prevFilter,
            (SingleValueFilter)valFilter);
        }
        else if (valFilter instanceof RangeValueFilter)
        {
          convertAndAdd(sqlFilter, fieldName, currFilter, prevFilter,
            (RangeValueFilter)valFilter);
        }
        else if (valFilter instanceof DomainValueFilter)
        {
          convertAndAdd(sqlFilter, fieldName, currFilter, prevFilter,
            (DomainValueFilter)valFilter);
        }
      }
      prevFilter = currFilter;
      currFilter = (DataFilterImpl)currFilter.getNextFilter();
    }
    while (currFilter != null);

    //convert the ordering criteria
    Object[] orderFields = filterImpl.getOrderFields();
    boolean[] sortOrders = filterImpl.getSortOrders();
    if (orderFields != null && orderFields.length > 0)
    {
      //Vector actualFields = new Vector();
      for (int i=0; i<orderFields.length; i++)
      {
        try
        {
          //change order field to its sql name
          FieldMetaInfo fieldMeta =
            metaInfo.findFieldMetaInfo((Number)orderFields[i]);
          Object fieldName = fieldMeta.getSqlName();
          if (!fieldName.equals(""))
          {
            //actualFields.add(fieldName);
            
            sqlFilter.addOrderField(fieldName, sortOrders[i]);
          }
        }
        catch (Exception ex)
        {
        }
      }
      //sqlFilter.setOrderFields(actualFields.toArray());
    }
    
    //NSL20051021 convert the select fields
    Object[] selectFields = filterImpl.getSelectFields();
    if (selectFields != null && selectFields.length > 0)
    {
      List list = new ArrayList(); 	
      for (int i=0; i<selectFields.length; i++)
      {
    	//change select field to its sql name
    	FieldMetaInfo fieldMeta = metaInfo.findFieldMetaInfo((Number)selectFields[i]);
    	Object fieldName = fieldMeta.getSqlName();
    	if (!fieldName.equals(""))
    	{
    	  list.add(fieldName);
    	}
      }
      sqlFilter.setSelectFields(list.toArray(new String[list.size()]), filterImpl.getDistinct());
    }
    
    return sqlFilter;
  }

  /**
   * Convert a data filter to "oql" type.
   *
   * @param entity Class name of the entity involved in the filter
   * @param filter The data filter to convert
   * @return Converted OQL filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public static IDataFilter convertToOQLFilter(
    String entity, IDataFilter filter)
  {
    DataFilterImpl filterImpl = (DataFilterImpl)filter;
    String filterName =filterImpl.getFilterName();

    //don't convert if already oql type
    if (filterName.equalsIgnoreCase("oql"))
      return filter;

    DataFilterImpl oqlFilter = new DataFilterImpl("oql");
    EntityMetaInfo metaInfo = MetaInfoFactory.getInstance().getMetaInfoFor(entity);


    DataFilterImpl prevFilter = null;
    DataFilterImpl currFilter = filterImpl;

    /**
     * @todo can use recursion to do conversion
     */
    //go through the filter chain to convert each data filter
    do
    {
      //convert the criteria filter

      IDataFilter leftFilter = currFilter.getLeftFilter();
      IDataFilter rightFilter = currFilter.getRightFilter();
      FilterConnector conn = currFilter.getConnector();

      if (leftFilter != null && rightFilter != null && conn != null)
      {
        //a filter tree
        leftFilter = convertToOQLFilter(entity, leftFilter);
        rightFilter = convertToOQLFilter(entity, rightFilter);
        convertAndAdd(oqlFilter, (DataFilterImpl)leftFilter,
          (DataFilterImpl)rightFilter, currFilter, prevFilter);
      }
      else if (currFilter.getValueFilter() != null)
      {
        //a normal filter
        IValueFilter valFilter = currFilter.getValueFilter();
        FieldMetaInfo fieldMeta =
          metaInfo.findFieldMetaInfo((Number)valFilter.getFilterField());
        Object fieldName = fieldMeta.getOqlName();

        if (valFilter instanceof SingleValueFilter)
        {
          convertAndAdd(oqlFilter, fieldName, currFilter, prevFilter,
            (SingleValueFilter)valFilter);
        }
        else if (valFilter instanceof RangeValueFilter)
        {
          convertAndAdd(oqlFilter, fieldName, currFilter, prevFilter,
            (RangeValueFilter)valFilter);
        }
        else if (valFilter instanceof DomainValueFilter)
        {
          convertAndAdd(oqlFilter, fieldName, currFilter, prevFilter,
            (DomainValueFilter)valFilter);
        }
      }
      prevFilter = currFilter;
      currFilter = (DataFilterImpl)currFilter.getNextFilter();
    }
    while (currFilter != null);

    //convert the order criteria
    Object[] orderFields = filterImpl.getOrderFields();
    boolean[] sortOrders = filterImpl.getSortOrders();
    if (orderFields != null && orderFields.length > 0)
    {
      //Vector actualFields = new Vector();
      for (int i=0; i<orderFields.length; i++)
      {
        try
        {
          FieldMetaInfo fieldMeta =
            metaInfo.findFieldMetaInfo((Number)orderFields[i]);
          Object fieldName = fieldMeta.getOqlName();
          if (!fieldName.equals(""))
          {
            //actualFields.add(fieldName);
            oqlFilter.addOrderField(fieldName, sortOrders[i]);
          }
        }
        catch (Exception ex)
        {
        }
      }
      //oqlFilter.setOrderFields(actualFields.toArray());
    }
    
    //NSL20051021 convert the select fields
    Object[] selectFields = filterImpl.getSelectFields();
    if (selectFields != null && selectFields.length > 0)
    {
      List list = new ArrayList(); 	
      for (int i=0; i<selectFields.length; i++)
      {
    	//change select field to its sql name
    	FieldMetaInfo fieldMeta = metaInfo.findFieldMetaInfo((Number)selectFields[i]);
    	Object fieldName = fieldMeta.getOqlName();
    	if (!fieldName.equals(""))
    	{
    	  list.add(fieldName);
    	}
      }
      oqlFilter.setSelectFields(list.toArray(new String[list.size()]), filterImpl.getDistinct());
    }

    return oqlFilter;
  }

  /**
   * Convert a data filter to a filter tree and add it to a filter chain.
   *
   * @param newFilter The data filter to be converted
   * @param leftFilter The left data filter of the filter tree
   * @param rightFilter The right data filter of the filter tree
   * @param currFilter The current data filter in process of conversion
   * (equivalent to <I>newFilter</I>)
   * @param prevFilter The previous data filter in process of conversion
   *
   * @since 1.0a build 0.9.9.6
   */
  private static void convertAndAdd(
    DataFilterImpl newFilter, DataFilterImpl leftFilter,
    DataFilterImpl rightFilter, DataFilterImpl currFilter,
    DataFilterImpl prevFilter)
  {
    FilterConnector conn =
      (FilterConnector)newFilter.getFilterFactory().getComponent(
      currFilter.getFilterFactory().findComponentKey(
      currFilter.getConnector() ) );

    DataFilterImpl filter =
      new DataFilterImpl(
          newFilter.getFilterName(), leftFilter, conn, rightFilter);

    if (prevFilter == null)
    {
      newFilter.addFilter(conn, filter);
    }
    else
    {
      prevFilter.addFilter(conn, filter);
    }
  }

  /**
   * Convert a "single" type criteria filter and add it to a data filter.
   *
   * @param newFilter The data filter to add the converted criteria filter
   * @param fieldName Name of the field involved in the criteria filter
   * @param currFilter The current data filter in process of conversion
   * (equivalent to <I>newFilter</I>)
   * @param prevFilter The previous data filter in conversion process
   * @param sFilter The "single" type criteria filter to be converted.
   *
   * @since 1.0a build 0.9.9.6
   */
  private static void convertAndAdd(
    DataFilterImpl newFilter, Object fieldName, DataFilterImpl currFilter,
    DataFilterImpl prevFilter, SingleValueFilter sFilter)
  {
    FilterOperator op =
      (FilterOperator)newFilter.getFilterFactory().getComponent(
       currFilter.getFilterFactory().findComponentKey(sFilter.getOperator()));

    if (prevFilter == null)
    {
      newFilter.addSingleFilter(
        null, fieldName, op, convertNested(sFilter.getSingleValue()),
        currFilter.hasNegation());
    }
    else
    {
      FilterConnector conn =
        (FilterConnector)newFilter.getFilterFactory().getComponent(
        currFilter.getFilterFactory().findComponentKey(
        prevFilter.getNextConnector() ) );

      newFilter.addSingleFilter(
        conn, fieldName, op, convertNested(sFilter.getSingleValue()),
        currFilter.hasNegation());
    }
  }

  /**
   * Convert a "range" type criteria filter and add it to a data filter.
   *
   * @param newFilter The data filter to add the converted criteria filter
   * @param fieldName Name of the field involved in the criteria filter
   * @param currFilter The current data filter in process of conversion
   * (equivalent to <I>newFilter</I>)
   * @param prevFilter The previous data filter in conversion process
   * @param rFilter The "range" type criteria filter to be converted.
   *
   * @since 1.0a build 0.9.9.6
   */
  private static void convertAndAdd(
    DataFilterImpl newFilter, Object fieldName, DataFilterImpl currFilter,
    DataFilterImpl prevFilter, RangeValueFilter rFilter)
  {
    if (prevFilter == null)
    {
      newFilter.addRangeFilter(
        null, fieldName, convertNested(rFilter.getLowValue()),
        convertNested(rFilter.getHighValue()),
        currFilter.hasNegation());
    }
    else
    {
      FilterConnector conn =
        (FilterConnector)newFilter.getFilterFactory().getComponent(
        currFilter.getFilterFactory().findComponentKey(
        prevFilter.getNextConnector()));

      newFilter.addRangeFilter(
        conn, fieldName, convertNested(rFilter.getLowValue()),
        convertNested(rFilter.getHighValue()),
        currFilter.hasNegation());
    }
  }

  /**
   * Convert a "domain" type criteria filter and add it to a data filter.
   *
   * @param newFilter The data filter to add the converted criteria filter
   * @param fieldName Name of the field involved in the criteria filter
   * @param currFilter The current data filter in process of conversion
   * (equivalent to <I>newFilter</I>)
   * @param prevFilter The previous data filter in conversion process
   * @param dFilter The "domain" type criteria filter to be converted.
   *
   * @since 1.0a build 0.9.9.6
   */
  private static void convertAndAdd(
    DataFilterImpl newFilter, Object fieldName, DataFilterImpl currFilter,
    DataFilterImpl prevFilter, DomainValueFilter dFilter)
  {
    if (prevFilter == null)
    {
      newFilter.addDomainFilter(
        null, fieldName, convertNested(dFilter.getDomainValues()),
        currFilter.hasNegation());
    }
    else
    {
      FilterConnector conn =
        (FilterConnector)newFilter.getFilterFactory().getComponent(
        currFilter.getFilterFactory().findComponentKey(
        prevFilter.getNextConnector()));

      newFilter.addDomainFilter(
        conn, fieldName, convertNested(dFilter.getDomainValues()),
        currFilter.hasNegation());
    }
  }

  /**
   * Convert a collection of values to be used in data filter.
   *
   * @param domainVal The collection of values to convert
   * @return The converted collection
   *
   * @since 1.0a build 0.9.9.6
   */
  private static Collection convertNested(Collection domainVal)
  {
    Vector conV = new Vector();
    Object[] vals = domainVal.toArray();

    for (int i=0; i<vals.length; i++)
    {
      conV.add(convertNested(vals[i]));
    }
    return conV;
  }

  /**
   * Convert an object value to be used in data filter.
   *
   * @param val The object value to convert
   * @return The converted object
   *
   * @since 1.0a build 0.9.9.6
   */
  private static Object convertNested(Object val)
  {
    if (val == null)
      return val;

    //use the key in place of entity object
    if (val instanceof IEntity)
    {
      val = ((IEntity)val).getKey();
    }
    return val;
  }
}