/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpBackendReceiveServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.util.IAlertKeys;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This servlet is use to receive incoming Http connections from the backend system.
 */
public class HttpBackendReceiveServlet extends HttpServlet
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 6848470698821536064L;

  private Logger _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHC, "HttpBackendReceiveServlet");
  
  /**
   * Just return a get-post page
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      System.out.println("Got a GET");
      _logger.debugMessage("doGet", null, "Received a GET");
      String id = "HttpBackendReceiveServlet";
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

  /**
   * Handle the HTTP request.
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      System.out.println("Got a POST");
      _logger.debugMessage("doPost", null, "Received a POST");
      Hashtable<String, String> headers = getRequestHeaders(request);
      byte[] content = getRequestContent(request);
      
      String valMsg = ReceiveDelegate.getInstance().validateHeaders(headers);
      String respMsg;
      int respCode;
      if (valMsg != null)
      {
        respMsg = valMsg;
        respCode = HttpServletResponse.SC_BAD_REQUEST;
      }
      else
      {
        respMsg = ReceiveDelegate.getInstance().receive(headers, content);
        if (respMsg == null)
        {
          respMsg = "Transaction Accepted";
          respCode = HttpServletResponse.SC_ACCEPTED;
        }
        else
        {
          respCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
      }
      response.setContentType("text/html");
      response.setStatus(respCode);
      ServletOutputStream os = response.getOutputStream();
      os.println(respMsg);
      os.flush();
    }
    catch (IOException ex)
    {
      _logger.logError(ILogErrorCodes.BACKEND_SERVLET_POST, "doPost", null, "IO Error. Unable to handle request", ex);
      AlertUtil.getInstance().sendAlert(IAlertKeys.UNEXPECTED_SYSTEM_ERROR,
                                        ILogTypes.TYPE_HTTPBC_ISHC, 
                                        "HttpBackendReceiveServlet",
                                        "doPost",
                                        "Error while handling request", 
                                        ex);
    }
  }

  
  private Hashtable<String, String> getRequestHeaders(HttpServletRequest request)
  {
    Hashtable<String, String> reqHeaders = new Hashtable<String, String>();
    Enumeration headers = request.getHeaderNames();
    if (headers != null)
    {
      while (headers.hasMoreElements())
      {
        String name = (String)headers.nextElement();
        String value = request.getHeader(name);
        reqHeaders.put(name, value);
      }
    }
    return reqHeaders;
  }
  
  private byte[] getRequestContent(HttpServletRequest request) throws IOException
  {
    return IOUtil.read(request.getInputStream());
  }
}
