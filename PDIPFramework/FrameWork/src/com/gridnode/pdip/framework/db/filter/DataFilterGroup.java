/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterGroup.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 02 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.filter;

import com.gridnode.pdip.framework.db.DataObject;

import java.util.Vector;

/**
 * This class defines a group of 'typed' data filters for serialization.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public class DataFilterGroup extends DataObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7904976138482033902L;
	private Vector _filters = new Vector();

  public DataFilterGroup()
  {
  }

  /**
   * Get the data filters in this group.<p>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The data filters (DataTypeFilter) in the group
   *
   * @since 1.0a build 0.9.9.6
   */
  public Vector getFilters()
  {
    return _filters;
  }

  /**
   * Set the data filters in the group.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param filters Vector of {@link DataTypeFilter}
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setFilters(Vector filters)
  {
    _filters = filters;
  }

  /**
   * Add a data filter into this group.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param filter The data filter to add
   *
   * @since 1.0a build 0.9.9.6
   */
  public void addFilter(DataTypeFilter filter)
  {
    if (!_filters.contains(filter))
      _filters.add(filter);
  }

  /**
   * Gives a short description of this filter group.
   *
   * @return A string representation of this group of filters
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    return getFilters()+"";
  }
}