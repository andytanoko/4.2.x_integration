/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServletHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Add getSessionMgr() method.
 * Oct 17 2005    Neo Sok Lay         Change JNDI lookup names
 */
package com.gridnode.pdip.app.user.login;

import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.exceptions.SignOnException;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;

/**
 * This provides helper methods for writing to http response, getting
 * UserManagerBean, etc. This should not be used as the actual servlet.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0
 */
public class ServletHelper
  extends    HttpServlet
  implements ISignOnKeys
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2296433272244753075L;

	public ServletHelper()
  {
  }

  /**
   * Write to the http response
   *
   * @param response The http response object
   * @param errCode The error code to be written to the <CODE>ERR_CODE</CODE>
   * header field.
   * @param content The Exception object providing the exception trace for the
   * content body.
   */
  protected void writeResponse(
    HttpServletResponse response, int errCode, SignOnException content)
    throws IOException
  {
    writeResponse(response, errCode, content.toString());
  }

  /**
   * Write to the http response
   *
   * @param response The http response object
   * @param errCode The error code to be written to the <CODE>ERR_CODE</CODE>
   * header field.
   * @param content The content body.
   */
  protected void writeResponse(
    HttpServletResponse response, int errCode, String content)
    throws IOException
  {
    response.addIntHeader(ERR_CODE, errCode);
    response.setContentType("text/plain");
    response.setContentLength(content.length());
    response.getOutputStream().print(content);
    response.getOutputStream().close();
    response.flushBuffer();
  }

  /**
   * Lookup and obtain a proxy object for the UserManagerBean.
   */
  protected IUserManagerObj getUserMgr() throws ServletException
  {
   IUserManagerObj mgr = null;
   try
   {
     mgr = (IUserManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
             //USER_MGR_JNDI,
             IUserManagerHome.class.getName(),
             IUserManagerHome.class,
             new Object[0]);
   }
   catch (ServiceLookupException ex)
   {
     throw new ServletException("Failed to Create UserManager EJB: caught " + ex);
   }
   return mgr;
  }

  /**
   * Lookup and obtain a proxy object for the SessionManagerBean.
   */
  protected ISessionManagerObj getSessionMgr() throws ServletException
  {
   ISessionManagerObj mgr = null;
   try
   {
     mgr = (ISessionManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
             //SESSION_MGR_JNDI,
             ISessionManagerHome.class.getName(),
             ISessionManagerHome.class,
             new Object[0]);
   }
   catch (ServiceLookupException ex)
   {
     throw new ServletException("Failed to Create SessionManager EJB: caught " + ex);
   }
   return mgr;
  }


}