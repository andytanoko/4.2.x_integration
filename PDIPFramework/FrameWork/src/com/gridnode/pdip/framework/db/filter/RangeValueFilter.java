/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RangeValueFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * Oct 16 2003    Neo Sok Lay         Modify toString() implementation.
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * This filter holds the "Range" comparison criteria.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class RangeValueFilter
  implements IValueFilter, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8279936195187084885L;

	private static final MessageFormat PATTERN = new MessageFormat("Range({0},{1},{2})");

  private Object     _highValue;
  private Object     _lowValue;
  private Object     _filterField;

  /**
   * Constructs a "range" type criteria.
   *
   * @param field The field involved in the criteria
   * @param lowValue The low value involved in the criteria
   * @param highValue The high value involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public RangeValueFilter(Object field, Object lowValue, Object highValue)
  {
     _filterField = field;
     _lowValue    = lowValue;
     _highValue   = highValue;
  }

  /**
   * Get the low value in the comparison.
   *
   * @return The low value involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object       getLowValue()
  {
    return _lowValue;
  }

  /**
   * Get the high value in the comparsion.
   *
   * @return The high value involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object       getHighValue()
  {
    return _highValue;
  }

  // *************** Methods from IValueFilter *******************

  public Object       getFilterField()
  {
    return _filterField;
  }
  
  public String toString()
  {
    Object[] params = new Object[]{
      getFilterField(),
      getLowValue(),
      getHighValue(),
    };
    
    return PATTERN.format(params);
    /*031016NSL
    return "Range(" + getFilterField() + "," +
           getLowValue() + ", " + getHighValue() + ")";
    */
  }
}