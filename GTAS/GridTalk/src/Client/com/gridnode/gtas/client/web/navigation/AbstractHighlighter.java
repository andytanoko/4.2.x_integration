/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractHighlighter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IdentifiedBean;

public abstract class AbstractHighlighter extends IdentifiedBean
{
  private String _type;

  public void setType(String type)
  {
    assertNotFrozen();
    _type = type;
  }

  public String getType()
  {
    return _type;
  }

  public String toString()
  {
    return this.getClass().getName() + "[" + getId() + "])";
  }

  public void freeze()
  {
    super.freeze();
  }

  /**
   * Return the id of the element in the navigation page that is to be highlighted.
   * @param navConfig
   * @param navgroup
   * @param mappingPath
   * @param request
   * @return highlightNodeId
   * @throws GTClientException
   */
  public abstract String getHighlight(NavigationConfig navConfig,
                                      Navgroup navgroup,
                                      String mappingPath,
                                      HttpServletRequest request)
    throws GTClientException;
}