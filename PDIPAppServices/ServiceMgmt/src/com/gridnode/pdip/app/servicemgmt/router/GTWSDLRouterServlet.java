package com.gridnode.pdip.app.servicemgmt.router;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.app.servicemgmt.model.WebService;
//import com.gridnode.pdip.framework.exceptions.FindEntityException;
//import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
//import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTWSDLRouterServlet.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 10, 2004   	Qingsong             	Created
 * Oct 07 2005    Neo Sok Lay         Change method signatures
 */

public class GTWSDLRouterServlet extends GTSoapRouterServlet
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7169049241220245176L;

	public GTWSDLRouterServlet()
  {
  }

  protected String getWSDLURL(HttpServletRequest request) throws Exception //ServiceLookupException, FindEntityException, SystemException
  {
    String serviceName = getServiceName(request);
    ILogIn.getInstance().Debug("requesting WSDL for service [" + serviceName + "]");
    WebService aService = getAWebService(serviceName);
    if(aService == null)
      {
        ILogIn.getInstance().Debug("cannot get WSDL for service [" + serviceName + "]");
        return "";
      }
    ILogIn.getInstance().Debug("got WSDL[" + aService.getWsdlUrl() + "] for service [" + serviceName + "]");
    return aService.getWsdlUrl();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doGet(request, response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      routerRequest(getWSDLURL(request), request, response, false);
    }
    catch (Exception ex)
    {
      ServletOutputStream out =  response.getOutputStream();
      response.setContentType(CONTENT_TYPE);
      response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
      out.write(("Internal Server Error").getBytes());
      out.close();
    }
  }
  /*
  public static void main(String[] args)
  {
    GTWSDLRouterServlet GTWSDLRouterServlet1 = new GTWSDLRouterServlet();
  }*/
}