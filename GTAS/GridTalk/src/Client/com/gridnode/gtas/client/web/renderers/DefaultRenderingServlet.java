/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultRenderingServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill     Created
 * 2002-10-24     Andrew Hill     Removed init() code
 * 2002-10-29     Andrew Hill     Made OBSOLETE by RenderingAction
 */
package com.gridnode.gtas.client.web.renderers;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gtas.client.web.StaticWebUtils;

/**
 * 20021029AH
 * This servlet has been replaced by the RenderingAction so that rendering can take advantage of
 * struts ExceptionHandler hooks.
 */
public class DefaultRenderingServlet extends HttpServlet
{
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }

  public void destroy()
  {
    super.destroy();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      RenderingUtils.renderPipeline(response.getWriter(), StaticWebUtils.getRenderingPipeline(request));
    }
    catch(Throwable t)
    {
      throw new ServletException("Error in DefaultRenderingServlet",t);
    }
  }


}