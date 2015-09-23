/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SingleValueFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Oct 16 2003    Neo Sok Lay         Modify toString() implementation.
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * This filter holds a "Single" comparison criteria.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class SingleValueFilter
  implements IValueFilter, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1071941148002595214L;

	private static final MessageFormat PATTERN = new MessageFormat("{0}({1},{2})");

  private FilterOperator  _operator;
  private Object          _singleValue;
  private Object          _filterField;

  /**
   * Constructs a "single" type criteria.
   *
   * @param field The field involved in the criteria
   * @param op The operator involved in the criteria
   * @param value The value involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public SingleValueFilter(Object field, FilterOperator op, Object value)
  {
    _filterField = field;
    _singleValue = value;
    _operator    = op;
  }

  /**
   * Get the comparison value in the criteria.
   *
   * @return The value involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object getSingleValue()
  {
    return _singleValue;
  }

  /**
   * Get the comparison operator in the criteria.
   *
   * @return The operator involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public FilterOperator    getOperator()
  {
    return _operator;
  }

  // ****************** Methods from IValueFilter *****************

  public Object       getFilterField()
  {
    return _filterField;
  }
  
  public String toString()
  {
    Object[] params = new Object[]{
      getOperator().getName(),
      getFilterField(),
      getSingleValue(),
    };
    return PATTERN.format(params);
    /*031016NSL
    return getOperator().getName() +
           "(" + getFilterField() + ", " +
           getSingleValue() + ")";
    */       
  }
}