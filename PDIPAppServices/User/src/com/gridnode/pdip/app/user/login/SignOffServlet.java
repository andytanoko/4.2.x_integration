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
 * May 14 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Remove subject from session data.
 * Jul 28 2003    Neo Sok Lay         Synchronize trackLogoutTime().
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
 * This is a servlet providing a SignOff service.
 * This servlet only process the Http POST method.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1
 * @since 2.0
 */
public class SignOffServlet
  extends ServletHelper
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5046878842209419342L;

	/**
   * Process the signoff http request.
   *
   * The request must contain the following parameters:<P>
   * <LI><CODE>SIGNON_SESSION</CODE> - The session to signoff.</LI>
   *
   * <P>After the processing, returns in the response object:<P>
   * <LI>an (int) Error code in the header field</LI>
   * <LI>an exception stack trace (if any) in the content body</LI>
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Logger.debug("[SignOffServlet.doPost] Enter");

    String session  = request.getParameter(SIGNON_SESSION);

    Logger.log("[SignOffServlet.doPost] [session]=["+session+"]");

    try
    {
      SignOnResource resource = SignOnResourceManager.getInstance().getSignOnResource(
                                  session);

      if (resource == null)
      {
        //error
        writeResponse(response, ERR_AUTH_FAIL,
          new SignOnException("SignOn session record does not exists!"));
      }
      else
      {
        if (resource.isValidSignOn())
        {
          trackLogoutTime(resource.getSignOnUser());
          resource.logout();
        }
        removeSessionData(resource);
        SignOnResourceManager.getInstance().removeSignOnResource(session);

        writeResponse(response, ERR_NO_ERROR, "");
      }
    }
    catch (LoginException ex)
    {
      Logger.warn("[SignOffServlet.doPost] Logout Validation Error ", ex);
      writeResponse(response, ERR_AUTH_FAIL, new SignOnException(ex.getMessage()));
    }
    catch (Exception ex)
    {
      Logger.warn("[SignOffServlet.doPost] Logout Error ", ex);
      writeResponse(response, ERR_SERVICE_FAIL, new SignOnException(ex));
    }

    Logger.debug("[SignOffServlet.doPost] Exit");
  }

  /**
   * Track the logout time of the user.
   *
   * @param userID UserID of the user who signs off.
   */
  private synchronized void trackLogoutTime(String userID)
    throws ServletException
  {
    Logger.log("[SignOffServlet.trackLogoutTime] Enter, UserID: "+userID);

    try
    {
      IUserManagerObj mgr = getUserMgr();

      UserAccount acct = mgr.findUserAccount(userID);

      if (acct == null)
        throw new ServletException("User Account not found: "+userID);

      UserAccountState accountState = acct.getAccountState();
      accountState.setLastLogoutTime(new java.sql.Timestamp(System.currentTimeMillis()));
      mgr.updateUserAccount(acct);
    }
    catch (ServletException ex)
    {
      Logger.warn("[SignOffServlet.trackLogoutTime] Servlet Error: "+ex.getMessage());
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SignOffServlet.trackLogoutTime] Error ", ex);
      throw new ServletException(ex);
    }
  }

  /**
   * Remove the authenticated subject from session data
   *
   * @param signOnResource The SignOnResource to remove from
   */
  private void removeSessionData(SignOnResource signOnResource)
    throws ServletException
  {
    Logger.log("[SignOffServlet.removeSessionData] Enter");

    try
    {
      ISessionManagerObj mgr = getSessionMgr();

      //remove the subject
      mgr.removeSessionData(signOnResource.getSessionID(), AuthSubject.KEY);

      //update state???? no method in session mgr
    }
    catch (ServletException ex)
    {
      Logger.warn("[SignOffServlet.removeSessionData] Servlet Error: "+ex.getMessage());
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[SignOffServlet.removeSessionData] Error ", ex);
      throw new ServletException(ex);
    }

  }
}