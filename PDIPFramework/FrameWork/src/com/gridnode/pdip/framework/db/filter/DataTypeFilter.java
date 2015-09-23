/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataTypeFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Sep 05 2001    NSL/Liu Dan         Extends Data Object for serializable.
 * Oct 16 2003    Neo Sok Lay         Modify toString() implementation.
 */
package com.gridnode.pdip.framework.db.filter;

import com.gridnode.pdip.framework.db.DataObject;

import java.text.MessageFormat;

/**
 * This class defines a typed data filter for serialization. It simply wraps
 * a serializable data filter.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class DataTypeFilter extends DataObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1563775856753051436L;

	private static final MessageFormat PATTERN = new MessageFormat("{0}/{1}@{2}");
  
  private String _type;
  private DataFilter _filter;
  private String _andOr;

  public DataTypeFilter()
  {
  }

  /**
   * Get the type of the data filter.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The type of the data filter: "default", "oql", or "sql"
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getType()
  {
    return _type;
  }

  /**
   * Get the connector of the data filter to its previous filter.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The connector of the data filter to its previous filter, or
   * empty string if no previous filter.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getAndOr()
  {
    return _andOr;
  }

  /**
   * Get the data filter encapsulated.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The data filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public DataFilter getFilter()
  {
    return _filter;
  }
  /**
   * Set the type of the data filter.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param type The type. Valid types are "default", "sql", "oql"
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setType(String type)
  {
    _type = type;
  }

  /**
   * Sets the data filter.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param filter The data filter to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setFilter(DataFilter filter)
  {
    _filter = filter;
  }

  /**
   * Sets the connector of the data filter to its previous filter.<P>
   * NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param andOr The connector to set. Valid values are: "and", "or", or "" if
   * no previous filter.
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setAndOr(String andOr)
  {
    _andOr = andOr;
  }

  /**
   * Gives a short description of this typed data filter.
   *
   * @return A string representation of this object
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    Object[] params = new Object[]{
      getAndOr(),
      getType(),
      getFilter(),
    };
    
    return PATTERN.format(params);
    /*031016NSL
    return getAndOr() + "/" + getType() + "@" + getFilter();
    */
  }
}