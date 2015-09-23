/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HelpLinkRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-08-03     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.utils.StaticUtils;

public class HelpLinkRenderer extends AbstractRenderer
{
  private String _pageKey;

  public HelpLinkRenderer(RenderingContext rContext, String pageKey)
  {
    super(rContext);
    if(pageKey == null) throw new java.lang.NullPointerException("No pageKey specified");
    _pageKey = pageKey;
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    try
    {
      String helpKey = rContext.getDocumentManager().getDocumentHelpKey(_pageKey);
      if(StaticUtils.stringNotEmpty(helpKey))
      {
        Element helpElement = getElementById("help", false);
        if(helpElement != null)
        {
          // change ...help.jsp... to ...help.jsp?id=gtas_ag.0...
          String onclick = helpElement.getAttribute("onclick");
          onclick = StringUtils.replace(onclick, "jsp", "jsp?id=" + helpKey);
          helpElement.removeAttribute("onclick");
          helpElement.setAttribute("onclick", onclick);   
        }
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error redering HelpLink", e);
    }
  }


}