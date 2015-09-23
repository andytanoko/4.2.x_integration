/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReturnBackendServlet.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 24, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback;

import com.gridnode.gridtalk.tester.loopback.command.impl.ReturnBackendCommand;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReturnBackendServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5515665211130138145L;

	/**
	 * 
	 */
	public ReturnBackendServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String mn = "doGet";
		String pipCode = request.getParameter("pip");
		String docId = request.getParameter("docid");
		
		ReturnBackendCommand cmd = new ReturnBackendCommand(docId);
		try
		{
			cmd.execute(pipCode);
		}
		catch (Throwable t)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, "Error", t);
		}
	}
}
