/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GmtOffsetRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Andrew Hill         Created
 * 2003-02-19     Andrew Hill         Preserve isTimeout flag
 * 2003-06-26     Andrew Hill         Extend AbstractLoginRenderer to share some common behaviour
 */
package com.gridnode.gtas.client.web.login;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
 
public class GmtOffsetRenderer extends AbstractLoginRenderer implements IDocumentRenderer
{
  public GmtOffsetRenderer( RenderingContext rContext,
                            LoginAForm form,
                            String submitUrl)
  { //20030626AH - Refactor to use superclass
    super( rContext, form, submitUrl ); 
  }

  protected void render() throws RenderingException
  {
    try
    {
      super.render(); //20030626AH - Invoke common rendering for login renderers
      RenderingContext rContext = getRenderingContext();
      IURLRewriter urlRewriter = rContext.getUrlRewriter();
      renderLabel("title","gridtalk.title",false);
      // Render the submit URL using the url rewriter to ensure context path and session encoding
      renderElementAttribute("gmt_form","action",urlRewriter.rewriteURL(_submitUrl));
      if(isTimeout(rContext.getRequest()))
      { //20030219AH
        renderElementAttribute("isTimeout_value","value","true");
      }
    }
    catch(Exception e)
    {
      throw new RenderingException("Error rendering login page",e);
    }
  }

  protected boolean isTimeout(HttpServletRequest request)
  { //20030219AH
    return StaticUtils.primitiveBooleanValue(request.getParameter("isTimeout"));
  }
}