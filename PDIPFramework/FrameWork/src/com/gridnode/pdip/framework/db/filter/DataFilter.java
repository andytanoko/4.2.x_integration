/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Oct 16 2003    Neo Sok Lay         Add SortFilter.
 */
package com.gridnode.pdip.framework.db.filter;

import java.text.MessageFormat;
import java.util.Vector;

/**
 * This class defines a simple data filter for JDO marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class DataFilter
{
  private static final MessageFormat PATTERN =
    new MessageFormat("negate?{0}@{1} {2} {3}/{4} {5} {6}/O:{7}");

  private boolean _negate;
  private ValueFilter _valueFilter;
  private DataFilter _leftFilter;
  private DataFilter _rightFilter;
  private String _connector;
  private DataFilter _nextFilter;
  private String _nextConnector;
  private Vector _sortFilters = new Vector();

  public DataFilter()
  {
  }

  /**
   * Check whether the entire criteria will be negated.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return <B>true</B> if the criteria will be negated, <B>false</B>
   * otherwise.
   *
   * @since 1.0a build 0.9.9.6
   */
  public boolean hasNegation()
  {
    return _negate;
  }

  /**
   * Get the criteria filter of this data filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The relevant criteria filter.
   *
   * @since 1.0a build 0.9.9.6
   */
  public ValueFilter getValueFilter()
  {
    return _valueFilter;
  }

  /**
   * Get the data filter on the left of this root filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The left filter of the filter tree with this filter as the root.
   *
   * @since 1.0a build 0.9.9.6
   */
  public DataFilter getLeftFilter()
  {
    return _leftFilter;
  }

  /**
   * Get the data filter on the right of this root filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The right filter of the filter tree with this filter as the root.
   *
   * @since 1.0a build 0.9.9.6
   */
  public DataFilter getRightFilter()
  {
    return _rightFilter;
  }

  /**
   * Get the connector from left filter to right filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The connector connecting left filter and right filter, if any.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getConnector()
  {
    return _connector;
  }

  /**
   * Get the data filter next to this in the filter chain.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The next data filter in the filter chain, if any.
   *
   * @since 1.0a build 0.9.9.6
   */
  public DataFilter getNextFilter()
  {
    return _nextFilter;
  }

  /**
   * Get the connector from this filter to next filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return Name of the connector from this filter to next filter, if any.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getNextConnector()
  {
    return _nextConnector;
  }

  /**
   * Sets whether to negate the criteria of this filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param negate <B>true</B> to negate, <B>false</B> otherwise
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setNegate(boolean negate)
  {
    _negate = negate;
  }

  /**
   * Sets the criteria for this filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param valFilter The criteria to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setValueFilter(ValueFilter valFilter)
  {
    _valueFilter = valFilter;
  }

  /**
   * Sets the filter on the left of this filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param leftFilter The filter to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setLeftFilter(DataFilter leftFilter)
  {
    _leftFilter = leftFilter;
  }

  /**
   * Sets the filter on the right of this filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param rightFilter The filter to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setRightFilter(DataFilter rightFilter)
  {
    _rightFilter = rightFilter;
  }

  /**
   * Sets the connector from left filter to right filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param conn The connector to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setConnector(String conn)
  {
    _connector = conn;
  }

  /**
   * Sets the filter next to this filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param nextFilter The filter to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setNextFilter(DataFilter nextFilter)
  {
    _nextFilter = nextFilter;
  }

  /**
   * Sets the connector from this filter to next filter.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param nextConn The connector to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setNextConnector(String nextConn)
  {
    _nextConnector = nextConn;
  }

  /**
   * Gives a short description of this data filter.
   *
   * @return Short string representation of this filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    Object[] params =
      new Object[] {
        Boolean.valueOf(hasNegation()),
        getLeftFilter(),
        getConnector(),
        getRightFilter(),
        getValueFilter(),
        getNextConnector(),
        getNextFilter(),
        getSortFilters(),
        };

    return PATTERN.format(params);

    /*031016NSL
    return "negate?"+hasNegation() + "@" + getLeftFilter() + " " + getConnector() + " " +
           getRightFilter() + "/" + getValueFilter() + " " + getNextConnector() +
           " " + getNextFilter();
    */
  }

  /**
   * Gets the SortFilter(s), if any, for this DataFilter.
   * 
   * @return The SortFilter(s) for this DataFilter.
   */
  public Vector getSortFilters()
  {
    return _sortFilters;
  }

  /**
   * Sets the SortFilter(s) for this DataFilter.
   * 
   * @param filters The SortFilter(s) for this DataFilter.
   */
  public void setSortFilters(Vector filters)
  {
    _sortFilters = filters;
  }

}