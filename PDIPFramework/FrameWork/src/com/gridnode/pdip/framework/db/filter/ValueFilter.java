/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValueFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Oct 16 2003    Neo Sok Lay         Modify toString() implementation.
 */
package com.gridnode.pdip.framework.db.filter;

import java.text.MessageFormat;
import java.util.Vector;

/**
 * This class defines a simple criteria filter for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class ValueFilter
{
  private static final MessageFormat PATTERN = new MessageFormat("{0}@{1} {2}/{3}..{4}/{5}");
  
  private Object _filterField;
  private String _operator;
  private Object _singleValue;
  private Object _lowValue;
  private Object _highValue;
  private Vector _domainValues = new Vector();

  public ValueFilter()
  {
  }

  /**
   * Get the field in the criteria.
   *
   * @return The field involved in the criteria.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getFilterField()
  {
    return _filterField;
  }

  /**
   * Get the name of the operator in the criteria.
   *
   * @return The operator involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getOperator()
  {
    return _operator;
  }

  /**
   * Get the value of the "single" comparison criteria.
   *
   * @return The value involved in the criteria. Only valid if the criteria is
   * "single" type.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getSingleValue()
  {
    return _singleValue;
  }

  /**
   * Get the low value of the "range" comparison criteria.
   *
   * @return The low value involved in the criteria. Only valid if the criteria
   * is "range" type.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getLowValue()
  {
    return _lowValue;
  }

  /**
   * Get the high value of the "range" comparison criteria.
   *
   * @return The high value involved in the criteria. Only valid if the criteria
   * is "range" type.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getHighValue()
  {
    return _highValue;
  }

  /**
   * Get the values of the "domain" comparison criteria.
   *
   * @return The values involved in the criteria. Only valid if the criteria
   * is "domain" type.
   *
   * @since 1.0a build 0.9.9.6
   */
  public Vector getDomainValues()
  {
    return _domainValues;
  }

  /**
   * Sets the field involved in the criteria.
   *
   * @param filterField The field to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setFilterField(Object filterField)
  {
    _filterField = filterField;
  }

  /**
   * Sets the operator involved in the criteria.
   *
   * @param op The name of the operator
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setOperator(String op)
  {
    _operator = op;
  }

  /**
   * Sets the value involved in the "single" type criteria.
   *
   * @param singeVal The value to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setSingleValue(Object singleVal)
  {
    _singleValue = singleVal;
  }

  /**
   * Sets the low value involved in the "range" type criteria.
   *
   * @param lowValue The value to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setLowValue(Object lowValue)
  {
    _lowValue = lowValue;
  }

  /**
   * Sets the high value involved in the "range" type criteria.
   *
   * @param highValue The value to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setHighValue(Object highVal)
  {
    _highValue = highVal;
  }

  /**
   * Sets the values involved in the "domain" type criteria.
   *
   * @param domainValues The values to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setDomainValues(Vector domainValues)
  {
    _domainValues = domainValues;
  }

  /**
   * Gives a short description of this criteria filter.
   *
   * @return A description of this criteria filter.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    Object[] params = new Object[]{
      getFilterField(),
      getOperator(),
      getSingleValue(),
      getLowValue(),
      getHighValue(),
      getDomainValues(),
    };
    
    return PATTERN.format(params);
    /*031016NSL
    return getFilterField() + "@" + getOperator() + " " + getSingleValue() + "/"
           + getLowValue() + ".." + getHighValue() + "/" + getDomainValues();
    */       
  }
}