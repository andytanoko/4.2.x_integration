/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: receiver.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 * Jan 12 2007    Neo Sok Lay             Remove Gntool capability
 */

package com.gridnode.pdip.base.transport.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.base.transport.helpers.HttpMessageContext;

public class receiver extends HttpServlet
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2468711331465362887L;
	//private static final String CONTENT_TYPE = "text/html";
  public void init() throws ServletException
  {
    System.out.println("Starting receiver");
    HttpMessageContext context = HttpMessageContext.getInstance();
    context.initConfig(getServletConfig());
    context.Info("GTAS HTTP Channel started");
    context.Info(context.toString());
  }

  /*NSL20070112
  public void tool1returnInfoResponse(HttpServletRequest request, HttpServletResponse response, String cmdline)
  {
      try
      {
        HttpMessageContext context = HttpMessageContext.getInstance();
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>gntool1 for GTAS</title></head>");
        out.println("<body>");
        out.println("<form method=\"GET\" action=\"" + request.getRequestURI() + "\">");
        out.println("<p><input type=\"text\" name=\"cmdline\" value =\"" +  cmdline +"\" size=\"100\"><input type=\"submit\" value=\"Submit\" name=\"B1\"></p>");
        out.println("</form>");
        out.println("<pre>" +  "System Setting:\r\n" + context.toString()  + "\r\nSystem Log:\r\n" + context.getLoggerBufferMessage() + "</pre>");
        out.println("</body></html>");
      }
      catch (Exception ex)
      {

      }
  }

  public void toolreturnLoginPage(HttpServletRequest request, HttpServletResponse response)
  {
      try
      {
        //HttpMessageContext context = HttpMessageContext.getInstance();
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Pleae Login GTAS</title></head>");
        out.println("<body>");
        out.println("<form method=\"GET\" action=\"" + request.getRequestURI() + "\">");
        out.println("<p><input type=\"text\" name=\"username\" size=\"20\"><input type=\"password\" name=\"passwd\" size=\"20\"><input type=\"submit\" value=\"Submit\" name=\"B1\"></p>");
        out.println("</form>");
        out.println("</body></html>");
      }
      catch (Exception ex)
      {
      }
  }

  public boolean doTool(HttpServletRequest request, HttpServletResponse response)
  {
      String uri = request.getRequestURI();
      HttpMessageContext context = HttpMessageContext.getInstance();
      if(uri.indexOf("gntool1") >= 0)
      {
        HttpSession session =  request.getSession();
        Object cookie = session.getAttribute("login");
        if(cookie == null)
         {
           String username = request.getParameter("username");
           String passwd = request.getParameter("passwd");
           if(username != null && passwd != null && username.equals(context.getLogin_username()) && passwd.equals(context.getLogin_passwd()))
           {
            session.setAttribute("login", new Object());
            tool1returnInfoResponse(request, response, "-h");
            return true;
           }
           else
           {
            toolreturnLoginPage(request, response);
            return true;
            }
         }
        String cmdline = request.getParameter("cmdline");
        String[] args = HttpMessageContext.stringVector2Array(HttpMessageContext.string2Vector(cmdline, ' '));
        if(args != null)
        {
          RNIFTestEngine testengine = new RNIFTestEngine(true);
          testengine.parseParameter(new GNTransportHeader(args));
          if(!testengine.isDisableStringLogCommand())
            context.setEnableLoggerBuffer(true);
          testengine.run();
        }
        tool1returnInfoResponse(request, response, cmdline);
        return true;
      }
      else
        return false;
  }
  */

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    /*NSL20070112
    String uri = request.getRequestURI();
    if(uri == null || uri.length() == 0)
      HTTPServletTransportHandler.returnNotSupprotResponse(response, "receiver");
    else
    {
      if(!doTool(request, response))
        HTTPServletTransportHandler.returnNotSupprotResponse(response, "receiver");
    }
    */
    HTTPServletTransportHandler.returnNotSupprotResponse(response, "receiver");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    HTTPServletTransportHandler.receive(request, response);
  }

  public void destroy()
  {
  }
}