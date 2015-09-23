/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DomainValueFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * Oct 16 2003    Neo Sok Lay         Modify toString() implementation.
 */
package com.gridnode.pdip.framework.db.filter;

import java.text.MessageFormat;
import java.util.Collection;
import java.io.Serializable;

/**
 * This filter holds a "Domain" comparison criteria.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 1.0a build 0.9.9.6
 */
public class DomainValueFilter implements IValueFilter, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4705754124783547820L;

	private static final MessageFormat PATTERN = new MessageFormat("Domain({0},{1})");
   
  private Collection _domainValues;
  private Object     _filterField;

  /**
   * Constructs a "domain" type criteria.
   *
   * @param field The field involved in the criteria
   * @param values The values involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public DomainValueFilter(Object field, Collection values)
  {
    _filterField   = field;
    _domainValues = values;
  }

  /**
   * Get the list of values in the comparison.
   *
   * @return The list of values involved in the criteria
   *
   * @since 1.0a build 0.9.9.6
   */
  public Collection   getDomainValues()
  {
    return _domainValues;
  }

  // **************** Methods from IValueFilter ******************

  public Object       getFilterField()
  {
    return _filterField;
  }

  public String toString()
  {
    Object[] params = new Object[]{
      getFilterField(),
      getDomainValues(),
    };
    
    return PATTERN.format(params);
    /*031016NSL
    return "Domain(" + getFilterField() + ", " + getDomainValues() + ")";
    */
  }
}