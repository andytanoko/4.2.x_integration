/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SignOffServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Store authenticated subject onto its
 *                                    session data.
 * Jul 28 2003    Neo Sok Lay         Synchronize trackLoginTime() to workaround
 *                                    the concurrent entity modification error.
 */
package com.gridnode.pdip.app.user.login;

import java.io.IOException;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.app.user.exceptions.SignOnException;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.helpers.Logger;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

/**
 * This is a servlet providing a SignOn service.
 * This servlet only process the Http POST method.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1
 * @since 2.0
 */
public class SignOnServlet
  extends    ServletHelper
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7300112703176305239L;

	/**
   * Process the signon http request.
   *
   * The request must contain the following parameters:<P>
   * <LI><CODE>SIGNON_USER_ID</CODE> - The userID of user to signon.</LI>
   * <LI><CODE>SIGNON_PASSWORD</CODE> - The password provided for authentication.</LI>
   * <LI><CODE>SIGNON_APPLICATION</CODE> - The application to signon to.</LI>
   * <LI><CODE>SIGNON_SESSION</CODE> - The session to signoff.</LI>
   *
   * <P>After the processing, returns in the response object:<P>
   * <LI>an (int) Error code in the header field</LI>
   * <LI>an exception stack trace (if any) in the content body</LI>
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Logger.debug("[SignOnServlet.doPost] Enter");

    String userID   = request.getParameter(SIGNON_USER_ID);
    String password = request.getParameter(SIGNON_PASSWORD);
    String app      = request.getParameter(SIGNON_APPLICATION);
    String session  = request.getParameter(SIGNON_SESSION);

    Logger.log("[SignOnServlet.doPost] [UserID,app,session]=["+userID+","+
      app+","+session+"]");

    try
    {
      SignOnResource resource = SignOnResourceManager.getInstance().getSignOnResource(
                                  session);

      if (resource == null)
      {
        resource = new SignOnResource(session, app);
        SignOnResourceManager.getInstance().addSignOnResource(resource);
      }

      resource.login(userID, password);
      trackLoginTime(userID);
      authenticateSession(resource);

      writeResponse(response, ERR_NO_ERROR, "");
    }
    catch (LoginException ex)
    {
      Logger.warn("[SignOnServlet.doPost] Login Validation Error ", ex);
      writeResponse(response, ERR_AUTH_FAIL, new SignOnException(ex.getMessage()));
    }
    catch (Exception ex)
    {
      Logger.warn("[SignOnServlet.doPost] Login Error ", ex);
      writeResponse(response, ERR_SERVICE_FAIL, new SignOnException(ex));
    }

    Logger.debug("[SignOnServlet.doPost] Exit");
  }

  /**
   * Track the login time of the user.
   *
   * @param userID UserID of the user who signs on.
   */
  private synchronized void trackLoginTime(String userID)
    throws ServletException
  {
    Logger.log("[SignOnServlet.trackLoginTime] Enter, UserID: "+userID);

    try
    {
      IUserManagerObj mgr = getUserMgr();

      UserAccount acct = mgr.findUserAccount(userID);

      if (acct == null)
        throw new ServletException("User Account not found: "+userID);

      UserAccountState accountState = acct.getAccountState();
      accountState.setLastLoginTime(new java.sql.Timestamp(System.currentTimeMillis()));
      accountState.setNumLoginTries((short)0);
      accountState.setIsFreeze(false);
      mgr.updateUserAccount(acct);
    }
    catch (ServletException ex)
    {
      Logger.warn("[SignOnServlet.trackLoginTime] Servlet Error: "+ex.getMessage());
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SignOnServlet.trackLoginTime] Error ", ex);
      throw new ServletException(ex);
    }
  }

  /**
   * Save the authenticated subject and authenticate the session.
   *
   * @param signOnResource The SignOnResource to authenticate.
   */
  private void authenticateSession(SignOnResource signOnResource)
    throws ServletException
  {
    Logger.log("[SignOnServlet.authenticateSession] Enter");

    try
    {
      ISessionManagerObj mgr = getSessionMgr();

      //save the subject
      mgr.setSessionData(signOnResource.getSessionID(), new AuthSubject(signOnResource.getSubject()));
      //authenticate the session
      boolean authenticated = mgr.authSession(signOnResource.getSessionID(), signOnResource.getSignOnUser());
      if (!authenticated)
        throw new LoginException("Another user has already been authenticated on this Session!");  
    }
    catch (ServletException ex)
    {
      Logger.warn("[SignOnServlet.authenticateSession] Servlet Error: "+ex.getMessage());
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SignOnServlet.authenticateSession] Error ", ex);
      throw new ServletException(ex);
    }

  }
}