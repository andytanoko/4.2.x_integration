package com.gridnode.pdip.app.servicemgmt.router;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.app.servicemgmt.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerHome;
import com.gridnode.pdip.app.servicemgmt.facade.ejb.IServiceMgmtManagerObj;
import com.gridnode.pdip.app.servicemgmt.model.IServiceAssignment;
import com.gridnode.pdip.app.servicemgmt.model.WebService;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
//import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
//import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTSoapRouterServlet.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 10, 2004   	Qingsong             	Created
 * Oct 07 2005    Neo Sok Lay         Change method signatures
 */

public class GTSoapRouterServlet extends HttpServlet
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4275455203417894201L;
	protected static final String CONTENT_TYPE = "text/html";
  protected static final String Key_ServiceName = "service";
  public void init() throws ServletException
  {
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>GTSoapRouterServlet</title></head>");
    out.println("<body>");
    out.println("<p>The servlet has received a GET. Please use post.</p>");
    out.println("</body></html>");
  }

  static protected byte[] getRequestContent(HttpServletRequest request) throws IOException
  {
    return ILogIn.getMessage(request.getInputStream());
  }

  static protected Hashtable getRequestHeader(HttpServletRequest request)
  {
      ILogIn context = ILogIn.getInstance();
      Enumeration headerNames = request.getHeaderNames();
      Hashtable   headers = new Hashtable();
      while(headerNames.hasMoreElements())
      {
        String headerName = (String)headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        if(context.getLogheader())
          context.Debug("getRequestHeader: header[" + headerName +"] [" + headerValue +"]");
        headers.put(headerName, headerValue);
      }
      return headers;
  }


  protected IServiceMgmtManagerObj getServerManager() throws ServiceLookupException
  {
      return (IServiceMgmtManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IServiceMgmtManagerHome.class.getName(),
      IServiceMgmtManagerHome.class,
      new Object[0]);
  }

  protected WebService getAWebService(String userName, String serviceName) throws Exception //ServiceLookupException, FindEntityException, SystemException
  {
    return getServerManager().findAssignedWebService(userName, IServiceAssignment.PARTNER_TYPE, serviceName);
  }

  protected WebService getAWebService(String serviceName) throws Exception //ServiceLookupException, FindEntityException, SystemException
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, WebService.SERVICE_NAME, filter.getEqualOperator(),serviceName, false);
    filter.addSingleFilter(filter.getAndConnector(), WebService.SERVICE_GROUP, filter.getEqualOperator(),WebService.INTERNAL_GROUP, false);
    Collection result= getServerManager().findWebServices(filter);
    if(result == null || result.isEmpty())
      return null;
    return (WebService)result.iterator().next();
  }


  protected void addResponseHeaders(HttpServletResponse response, Hashtable headers)
  {
    if(response == null || headers == null)
    return;
    ILogIn context = ILogIn.getInstance();
    Enumeration keys = headers.keys();
    while( keys.hasMoreElements() )
    {
      String keyValue = (String)keys.nextElement();
      String headerValue = (String)headers.get(keyValue);
      if(context.getLogheader())
        context.Debug("addResponseHeaders: header [" + keyValue +"] [" + headerValue +"]");
      try
      {
        response.addHeader(keyValue, headerValue);
      }
      catch (Exception ex)
      {
        context.Error(ILogErrorCodes.SERVICEMGMT_MESSAGE_HEADER,
                      "[GTSoapRouterServlet.addResponseHeaders]Error adding response header [" + keyValue +"] [" + headerValue +"]", ex);
      }
    }
  }

 protected static String getSearchPara(HttpServletRequest request, String paraName)
  {
    return getSearchPara(request.getQueryString(), paraName);
  }

 protected static String getSearchPara(String searchString, String paraName)
  {
   if(searchString == null || searchString.trim().length() <= 0)
       return "";
   if(searchString.charAt(0) != '?')
    searchString = '?' + searchString;
   System.out.println("searchString:" + searchString);
   int index = searchString.indexOf("?" + paraName + "=");
   if(index < 0)
       {
          index = searchString.indexOf("&" + paraName + "=");
       }
   if(index < 0 )
      return "";
   String val = searchString.substring(index);
   val = val.substring(val.indexOf("=") + 1);
   int end = val.indexOf("&");
   if(end >= 0)
        val = val.substring(0, end);
   return val;
  }

  protected String getServiceName(HttpServletRequest request)
  {
    return getSearchPara(request, Key_ServiceName);
  }

  protected String getUserName(HttpServletRequest request)
  {
    try
    {
      return request.getUserPrincipal().getName();
    }
    catch (Exception ex)
    {
      return "";
    }
  }

  protected GNHttpConnection routerRequest(String url, Hashtable header, byte[] contend, boolean isPost) throws IOException
  {
    ILogIn context = ILogIn.getInstance();
    GNHttpConnection httpconnection = new GNHttpConnection(url,true,false,false,"","","","",50000);
    if(context.getLogheader())
        {
          context.Debug("sendRN:" + httpconnection.logMessageSetting());
        }
    if(isPost)
      httpconnection.doPost(header, contend);
    else
      httpconnection.doGet(header);
    return httpconnection;
  }

  protected GNHttpConnection routerRequest(String url, HttpServletRequest request, HttpServletResponse response, boolean isPost) throws IOException
  {
      //ILogIn context = ILogIn.getInstance();

      GTConfigFile  header = new GTConfigFile(getRequestHeader(request));
      header.removeProperty("host");
      //header.removeProperty("authorization");

      byte[] contend   = isPost? getRequestContent(request) : null;

      GNHttpConnection httpconnection = routerRequest(url, header.getProperties(), contend, isPost);

      int response_Code = httpconnection.getResponseCode();
      byte[] response_contend = httpconnection.getResponseMessage();
      GTConfigFile response_header = new GTConfigFile(httpconnection.getResponseHeaders());
      response_header.removeProperty("host");
      response_header.removeProperty("Transfer-Encoding");

      response.setContentType(response_header.getStringProperty("Content-Type"));
      response.setStatus(response_Code);
      addResponseHeaders(response,response_header.getProperties());

      ServletOutputStream out =  response.getOutputStream();
      out.write(response_contend);
      out.close();
      return httpconnection;
  }

  protected String getServiceURL(HttpServletRequest request) throws Exception //ServiceLookupException, FindEntityException, SystemException
  {
    String serviceName = getServiceName(request);
    String userName = getUserName(request);
    WebService aService = getAWebService(userName, serviceName);
    ILogIn.getInstance().Debug("requesting service url for user["+ userName + "] service [" + serviceName + "]");
    if(aService == null)
      {
        ILogIn.getInstance().Debug("cannot get service url for user["+ userName + "] service [" + serviceName + "]");
        return "";
      }
    ILogIn.getInstance().Debug("got service url[" + aService.getEndPoint() + "] for user["+ userName + "] service [" + serviceName + "]");
    return aService.getEndPoint();
  }

  protected boolean canAccess(HttpServletRequest request) throws Exception //ServiceLookupException, FindEntityException, SystemException
  {
    String serviceName = getServiceName(request);
    String userName = getUserName(request);
    ILogIn.getInstance().Debug("Testing user["+ userName + "] service [" + serviceName + "]");
    if(userName != null && userName.trim().length() > 0 && getAWebService(userName, serviceName) != null)
      {
        ILogIn.getInstance().Debug("user["+ userName + "] is authorized to access service [" + serviceName + "]");
        return true;
      }
    else
      {
        ILogIn.getInstance().Debug("user["+ userName + "] is not authorized to access service [" + serviceName + "]");
        return false;
      }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {

    try
    {
      if(canAccess(request))
      {
        routerRequest(getServiceURL(request), request, response, true);
      }
      else
      {
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream out =  response.getOutputStream();
        out.write(("You cannot access this service").getBytes());
        out.close();
      }
    }
    catch (Exception ex)
    {
      response.setContentType(CONTENT_TYPE);
      ServletOutputStream out =  response.getOutputStream();
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      out.write(("Internal Server Error").getBytes());
      out.close();
    }
  }

  public void destroy()
  {
  }

  public static void main(String args[])
      throws Exception
  {
    GTSoapRouterServlet r = new GTSoapRouterServlet();
//    System.out.println(r.getSearchPara("?service=http://www.ntu.edu.sg/", "service"));
    r.routerRequest("http://www.ntu.edu.sg/", (Hashtable)null, null, false);
  }

}