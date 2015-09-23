/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Oct 15 2003    Neo Sok Lay         Allow setting order fields specifying
 *                                    the ascending or descending.
 * Oct 21 2005    Neo Sok Lay         Allow setting of fields to return, and
 *                                    whether to return distinct result set.                                   
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.*;
import java.util.*;

/**
 * An abstract implementation of a data filter. The components for
 * the concrete data filter are provided by the DataFilterFactory.
 *
 * @author Neo Sok Lay
 * @version GT 2.2 I3
 */
public class DataFilterImpl implements IDataFilter, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3117080494744941664L;
	protected IDataFilter     _nextFilter;
  protected FilterConnector _nextConn;
  private boolean           _hasNegation;
  private IValueFilter      _valueFilter;
  private String            _filterName = "default";
  private DataFilterFactory _filterFactory;
  //private Object[]          _orderFields;
  private List              _orderFields = Collections.synchronizedList(new ArrayList(0));
  private IDataFilter       _leftFilter;
  private IDataFilter       _rightFilter;
  private FilterConnector   _conn;
  private boolean 			_distinct = false;
  private Object[]			_selectFields;

  public DataFilterImpl()
  {
    _filterFactory = FilterFactoryRegistry.getFilterFactory(_filterName);
  }

  /**
   * Constructs a filter of the specified type
   * @param filterName Type of the filter to construct.
   * Valid types are: "default", "oql", "sql"
   */
  public DataFilterImpl(String filterName)
  {
    _filterName = filterName;
    _filterFactory = FilterFactoryRegistry.getFilterFactory(_filterName);
  }

  /**
   * Constructs a filter using the criteria filter
   * @param filterName Type of the filter to construct
   * @param valueFilter The criteria of the filter
   * @param negate Whether to negate the criteria
   */
  public DataFilterImpl(
         String filterName,
         IValueFilter valueFilter,
         boolean negate)
  {
    this(filterName);
    _valueFilter = valueFilter;
    _hasNegation = negate;
  }

  /**
   * Constructs a filter tree (of default type) with left and right filters
   * @param filter1 The left filter
   * @param connector The connector from <I>filter1</I> to <I>filter2</I>
   * @param filter2 The right filter
   */
  public DataFilterImpl(
         IDataFilter filter1,
         FilterConnector connector,
         IDataFilter filter2)
  {
    this();
    _leftFilter = filter1;
    _rightFilter = filter2;
    _conn = connector;
  }

  /**
   * Constructs a filter tree of specific type with left and right filters
   * @param filterName Type of the filter to construct
   * @param filter1 The left filter
   * @param connector The connector from <I>filter1</I> to <I>filter2</I>
   * @param filter2 The right filter
   */
  public DataFilterImpl(
         String filterName,
         IDataFilter filter1,
         FilterConnector connector,
         IDataFilter filter2)
  {
    this(filterName);
    _leftFilter = filter1;
    _rightFilter = filter2;
    _conn = connector;
  }

  /**
   *
   */
  public void addRangeFilter(
       FilterConnector connector,
       Object field,
       Object lowValue,
       Object highValue,
       boolean negate)
  {
    IValueFilter vFilter = new RangeValueFilter(field, lowValue, highValue);
    addLastFilter(vFilter, negate, connector);
  }

  /**
   *
   */
  public void addSingleFilter(
       FilterConnector connector,
       Object field,
       FilterOperator op,
       Object value,
       boolean negate)
  {
    IValueFilter vFilter = new SingleValueFilter(field, op, value);
    addLastFilter(vFilter, negate, connector);
  }

  /**
   *
   */
  public void addDomainFilter(
       FilterConnector connector,
       Object field,
       Collection values,
       boolean negate)
  {
    IValueFilter vFilter = new DomainValueFilter(field, values);
    addLastFilter(vFilter, negate, connector);
  }

  /**
   *
   */
  public void addFilter(
         FilterConnector connector,
         IDataFilter filter)
  {
    getLastFilter().setNextFilter(filter, connector);
  }

  /**
   *
   */
  public boolean      hasNegation()
  {
    return _hasNegation;
  }

  /**
   *
   */
  public String       getFilterExpr()
  {
    String filterStr = "";

    if (_leftFilter != null && _rightFilter != null && _conn != null)
    {
      //filter tree: _leftFilter _conn _rightFilter [_nextConn _nextFilter]
      filterStr = _conn.applySyntax(
        _leftFilter.getFilterExpr(), _rightFilter.getFilterExpr());

      return ( _nextFilter == null ?
               filterStr :
               getNextConnector().applySyntax(
                filterStr, getNextFilter().getFilterExpr())
             );
    }

    //last filter
    if (_nextFilter == null)
      return applySyntax();

    //empty filter, chain starts from next filter
    if (_valueFilter == null)
      return getNextFilter().getFilterExpr();

    //has next filter
    return getNextConnector().applySyntax(
           applySyntax(), getNextFilter().getFilterExpr());
  }

  /**
   *
   */
  public IDataFilter  getNextFilter()
  {
   return _nextFilter;
  }

  /**
   *
   */
  public IValueFilter getValueFilter()
  {
    return _valueFilter;
  }

  /**
   *
   */
  public FilterConnector   getNextConnector()
  {
    return _nextConn;
  }

  /**
   *
   */
  public FilterConnector   getAndConnector()
  {
    return _filterFactory.getAndConnector();
  }

  /**
   *
   */
  public FilterConnector   getOrConnector()
  {
    return _filterFactory.getOrConnector();
  }

  /**
   *
   */
  public FilterOperator    getEqualOperator()
  {
    return _filterFactory.getEqualOperator();
  }

  /**
   *
   */
  public FilterOperator    getNotEqualOperator()
  {
    return _filterFactory.getNotEqualOperator();
  }

  /**
   *
   */
  public FilterOperator    getGreaterOperator()
  {
    return _filterFactory.getGreaterOperator();
  }

  /**
   *
   */
  public FilterOperator    getGreaterOrEqualOperator()
  {
    return _filterFactory.getGreaterOrEqualOperator();
  }

  /**
   *
   */
  public FilterOperator    getLessOperator()
  {
    return _filterFactory.getLessOperator();
  }

  /**
   *
   */
  public FilterOperator    getLessOrEqualOperator()
  {
    return _filterFactory.getLessOrEqualOperator();
  }

  /**
   *
   */
  public FilterOperator    getLikeOperator()
  {
    return _filterFactory.getLikeOperator();
  }

  /**
   *
   */
  public FilterOperator    getLocateOperator()
  {
    return _filterFactory.getLocateOperator();
  }

  /**
   *
   */
  public FilterOperator    getNotOperator()
  {
    return _filterFactory.getNotOperator();
  }

  /**
   *
   */
  public DataFilterFactory getFilterFactory()
  {
    return _filterFactory;
  }

  /**
   *
   */
  public String            getFilterName()
  {
    return _filterName;
  }

  /**
   *
   */
  public void setNextFilter(IDataFilter filter, FilterConnector conn)
  {
    _nextFilter = filter;
    _nextConn   = conn;
  }

  /**
   *
   */
  public void setOrderFields(Object[] orderFields)
  {
    //031015NSL
    //_orderFields = orderFields;
    _orderFields.clear();
    if (orderFields != null)
    {
      for (int i=0; i<orderFields.length; i++)
      {
        addOrderField(orderFields[i], true);
      }
    }
  }

  /**
   *
   */
  public Object[] getOrderFields()
  {
    //031015NSL
    //return _orderFields;
    OrderField[] orderFieldObjs = getOrderFieldObjs();
    Object[] orderFields = new Object[orderFieldObjs.length];
    for (int i=0; i<orderFields.length; i++)
    {
      orderFields[i] = orderFieldObjs[i].getField();
    }
    return orderFields;    
  }

  /**
   *
   */
  public IDataFilter getLeftFilter()
  {
    return _leftFilter;
  }

  /**
   *
   */
  public IDataFilter getRightFilter()
  {
    return _rightFilter;
  }

  /**
   *
   */
  public FilterConnector getConnector()
  {
    return _conn;
  }

  /**
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#getDistinct()
   */
  public boolean getDistinct() {
	return _distinct;
  }

  /**
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#getSelectFields()
   */
  public Object[] getSelectFields() {
	return _selectFields;
  }

  /**
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#setSelectFields(Object[],boolean)
   */
  public void setSelectFields(Object[] selectFields, boolean distinct) {
    _selectFields = selectFields;
    _distinct = distinct;
  }


/**
   * @return The last filter in the filter chain
   */
  private DataFilterImpl getLastFilter()
  {
    if (_valueFilter == null)
      return this;

    DataFilterImpl curr = this;
    while (curr.getNextFilter() != null)
    {
      curr = (DataFilterImpl)curr.getNextFilter();
    }
    return curr;
  }

  /**
   * Adds a filter at the end of the filter chain
   * @param vFilter The criteria of the new filter
   * @param negate Whether to negate the criteria
   * @param conn The connector from current last filter to this new filter
   */
  private void addLastFilter(
          IValueFilter vFilter,
          boolean negate,
          FilterConnector conn)
  {
    DataFilterImpl lastFilter = getLastFilter();
    if (lastFilter.getValueFilter() != null ||
        (_leftFilter != null && _rightFilter != null && _conn != null))
    {
      DataFilterImpl filter = new DataFilterImpl(_filterName, vFilter, negate);
      lastFilter.setNextFilter(filter, conn);
    }
    else
    {
      _valueFilter = vFilter;
      _hasNegation = negate;
    }
  }

  /**
   * Apply the syntax of the filter factory on this filter. The filter factory
   * is determined by the filter name (type).
   */
  public String applySyntax()
  {
    if (getValueFilter() == null)
      return "";

    String syntaxStr = getFilterFactory().applySyntax(getValueFilter());

    if (hasNegation())
      return getNotOperator().applySyntax(syntaxStr);
    return syntaxStr;
  }

  public boolean equals(Object other)
  {
    if (other == null || !(other instanceof DataFilterImpl))
      return false;

    DataFilterImpl otherFilter = (DataFilterImpl)other;
    return getFilterExpr().equals(otherFilter.getFilterExpr());
  }
  
  /** 
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#addOrderField(java.lang.Object, boolean)
   */
  public void addOrderField(Object orderField, boolean sortAscending)
  {
    _orderFields.add(new OrderField(orderField, sortAscending));  
  }

  /** 
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#getSortOrders()
   */
  public boolean[] getSortOrders()
  {
    OrderField[] orderFieldObjs = getOrderFieldObjs();
    boolean[] sortOrders = new boolean[orderFieldObjs.length];
    for (int i=0; i<sortOrders.length; i++)
    {
      sortOrders[i] = orderFieldObjs[i].getSortOrder();
    }
    
    return sortOrders;    
  }

  /** 
   * @see com.gridnode.pdip.framework.db.filter.IDataFilter#setOrderFields(java.lang.Object[], boolean[])
   */
  public void setOrderFields(Object[] orderFields, boolean[] sortAscending)
  {
    // any one of them is null, just setOrderFields(orderFields)
    if (orderFields == null || sortAscending == null)
      setOrderFields(orderFields);
    else //both not null
    {
      // check array lengths
      if (orderFields.length != sortAscending.length)
        throw new IllegalArgumentException("Parameter mismatch: orderFields and sortAscending array size does not match.");
    
      // no problem, add the order fields.
      _orderFields.clear();
      if (orderFields != null)
      {
        for (int i=0; i<orderFields.length; i++)
        {
          addOrderField(orderFields[i], sortAscending[i]);
        }
      }
    }
  }
  
  /**
   * Get array of the OrderField objects in this filter
   * 
   * @return Array of OrderField objects, which may be empty if no
   * order fields have been specified so far.
   */
  private OrderField[] getOrderFieldObjs()
  {
    return (OrderField[])_orderFields.toArray(new OrderField[_orderFields.size()]);
  }
  
  /**
   * A simple Order field definition with the Field to order by
   * and the sorting order.
   * 
   * @author Neo Sok Lay
   * @since GT 2.2 I3
   */
  private class OrderField implements Serializable
  {
    /**
		 * Serial Version UID
		 */
		private static final long serialVersionUID = -2844951338544357407L;
		private Object _field;
    private boolean _sortOrder;

    /**
     * Constructs an OrderField.
     * 
     * @param field The field to sort by.
     * @param sortOrder The sorting order. <b>true</b> for
     * ascending order, <b>false</b> for descending order.
     */    
    OrderField(Object field, boolean sortOrder)
    {
      _field = field;
      _sortOrder = sortOrder;
    }
    
    /**
     * Get the field to sort by.
     * 
     * @return The field to sort by.
     */
    Object getField()
    {
      return _field;
    }
    
    /**
     * Get the sorting order.
     * 
     * @return <b>true</b> for ascending order, <b>false</b>
     * for descending order.
     */
    boolean getSortOrder()
    {
      return _sortOrder;
    }
  }
}