/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FilterConnector.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Feb 05 2003    Neo Sok Lay         Equal comparison on Name only, otherwise
 *                                    will have problem finding the connector
 *                                    if connector and filter factory are not
 *                                    created from the same JVM.
 */
package com.gridnode.pdip.framework.db.filter;

import java.io.Serializable;

/**
 * This class defines a filter component for connecting 2 data filters.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 1.0a build 0.9.9.6
 */
public class FilterConnector
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2101588418886701571L;
	private String _name;

  /**
   * Constructs a connector for connecting one filter to the next.
   *
   * @param name Name of the connector
   *
   * @since 1.0a build 0.9.9.6
   */
  public FilterConnector(String name)
  {
    _name = name;
  }

  /**
   * Get the name of this connector.
   *
   * @return The name of the connector
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Applys the syntax of this connector on the filters.
   *
   * @param filterStr The string expression of first filter
   * @param nextFilterStr The string expression of second filter
   *
   * @since 1.0a build 0.9.9.6
   */
  public String applySyntax(String filterStr, String nextFilterStr)
  {
    return getName() + "(" + filterStr + ", " + nextFilterStr + ")";
  }

  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof FilterConnector))
      return false;

    FilterConnector otherCon = (FilterConnector)o;
    return getName().equals(otherCon.getName());
  }

}