/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SortFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.filter;

/**
 * This class defines a simple sorting filter for JDO marshalling/unmarshalling.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2 I3
 */
public class SortFilter
{
  private Object _sortField;
  private boolean _sortOrder;
  
  /**
   * Constructs an empty SortFilter 
   */
  public SortFilter()
  {
  }

  /**
   * Gets the SortField value.
   * @return SortField value.
   */
  public Object getSortField()
  {
    return _sortField;
  }

  /**
   * Gets the SortOrder value.
   * 
   * @return SortOrder value. <b>true</b> means ascending, <b>false</b>
   * means descending.
   */
  public boolean getSortOrder()
  {
    return _sortOrder;
  }

  /**
   * Sets the SortField value.
   * 
   * @param sortField The SortField value to set.
   */
  public void setSortField(Object sortField)
  {
    _sortField = sortField;
  }

  /**
   * Sets the SortOrder value.
   * 
   * @param sortOrder The SortOrder value to set.
   */
  public void setSortOrder(boolean sortOrder)
  {
    _sortOrder = sortOrder;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    buff.append(getSortField()).append(
      getSortOrder()?" ASC":" DESC");
    
    return buff.toString();
  }

}
