/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Send.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 20 2002    Qingsong                 Created
 */

package com.gridnode.pdip.base.transport.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class sender extends HttpServlet
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2726728687386929260L;
	//private static final String CONTENT_TYPE = "text/html";
  public void init() throws ServletException
  {
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    HTTPServletTransportHandler.returnNotSupprotResponse(response, "sender");
  }
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    HTTPServletTransportHandler.send(request, response);
  }
  //Clean up resources
  public void destroy()
  {
  }
}