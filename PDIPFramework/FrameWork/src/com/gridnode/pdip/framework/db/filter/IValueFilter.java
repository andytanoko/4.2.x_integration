/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IValueFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.filter;

/**
 * This interface defines the basic functionality of a criteria filter.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public interface IValueFilter
{
  /**
   * Gives the criteria representation by this filter.
   *
   * @return A string representation of the filter
   *
   * @since 1.0a build 0.9.9.6
   */
  String toString();

  /**
   * Gives the field that is involved in the criteria.
   *
   * @return The field involved in this filter
   *
   * @since 1.0a build 0.9.9.6
   */
  Object getFilterField();
}