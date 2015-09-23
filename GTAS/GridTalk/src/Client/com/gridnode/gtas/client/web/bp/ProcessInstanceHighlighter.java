/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceHighlighter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-31     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.navigation.Highlighter;
import com.gridnode.gtas.client.web.navigation.Navgroup;
import com.gridnode.gtas.client.web.navigation.NavigationConfig;

public class ProcessInstanceHighlighter extends Highlighter
{
  protected String _param = null;

  public void setParam(String param)
  {
    assertNotFrozen();
    _param = param;
  }

  public String getParam()
  {
    return _param;
  }

  public String toString()
  {
    return "ProcessInfoHighlighter[" + getId() + "]";
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
  public String getHighlight( NavigationConfig navConfig,
                              Navgroup navgroup,
                              String mappingPath,
                              HttpServletRequest request)
    throws GTClientException
  {
    try
    {
      if(navgroup == null) return null;
      if(mappingPath == null) throw new NullPointerException("mappingPath is null");
      String id = super.getHighlight(navConfig,navgroup,mappingPath,request);
      String param = getParam();
      if( StaticUtils.stringNotEmpty(param) )
      {
        String processDefName = request.getParameter( param );
        if( StaticUtils.stringNotEmpty(processDefName))
        {
          id = id + "_" + processDefName;
        }
      }
      return id;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting highlight in "
                                  + navgroup
                                  + " for mappingPath \""
                                  + mappingPath + "\" in ProcessInstanceHighlighter",t);
    }
  }
}

