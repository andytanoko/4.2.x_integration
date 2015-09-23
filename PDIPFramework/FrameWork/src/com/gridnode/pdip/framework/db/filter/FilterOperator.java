/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FilterOperator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * Feb 05 2003    Neo Sok Lay         Equal comparison on Name only, otherwise
 *                                    will have problem finding the operator
 *                                    if operator and filter factory are not
 *                                    created from the same JVM.
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.Serializable;

/**
 * This class defines a filter component for operations in a criteria.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 1.0a build 0.9.9.6
 */
public class FilterOperator
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4657210011063309530L;
	private String _name;

  /**
   * Constructs a operator for use in criteria.
   *
   * @param name The name of the operator to construct
   *
   * @since 1.0a build 0.9.9.6
   */
  public FilterOperator(String name)
  {
    _name = name;
  }

  /**
   * Get the name of this operator.
   *
   * @return The name of this operator
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Applys the syntax of this operator on the criteria.
   *
   * @param filter The criteria to apply syntax on
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applySyntax(IValueFilter filter)
  {
    return filter.toString();
  }

  /**
   * Applys the syntax of this operator on the filter expression.
   *
   * @param filterStr The string expression of the filter to apply syntax.
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applySyntax(String filterStr)
  {
    return _name + "(" + filterStr + ")";
  }

  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof FilterOperator))
      return false;

    FilterOperator otherOp = (FilterOperator)o;
    return getName().equals(otherOp.getName());
  }
}