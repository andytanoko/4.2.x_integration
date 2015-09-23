/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractServlet.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 17, 2007        Regina Zeng          Created
 * Mar 05, 2007			Alain Ah Ming				Added error code to error logs
 */

package com.gridnode.gridtalk.genreport.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Regina Zeng
 * Abstract implementation of a servlet.
 */
public abstract class AbstractServlet extends HttpServlet
{
  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_UTIL, "AbstractServlet");  
  
  /**
   * This method is declared final so subclasses cannot override it.
   */
  public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      System.out.println("Got a GET");
      _logger.debugMessage("doGet", null, "Received a GET");
      String id = "GenReportServlet";
      ServletOutputStream os = response.getOutputStream();
      response.setContentType("text/html");
      os.println("<html>");
      os.println("<head><title>" + id + "</title></head>");
      os.println("<body>");
      os.println("<p>The servlet has received a GET. Please use Post.</p>");
      os.println("</body></html>");
      os.flush();
    }
    catch (IOException ex)
    {
      _logger.logWarn("doGet", null, "IO Error. Unable to handle request", ex);
    }
  }
}
