/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserLoginAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 05 2002    Neo Sok Lay         Retrieve authenticated subject from
 *                                    session manager and set into statemachine.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGridTalkAction instead
 *                                    of GridTalkEJBAction (to be phased out).
 * Aug 19 2002    Koh Han Sing        Set username after login.
 * Oct 23 2002    Ang Meng Hua        Set Enterprise ID in StateMachine
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.gtas.server.user.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.InvalidStateException;

import com.gridnode.gtas.server.gridnode.model.GridNode;

import com.gridnode.pdip.app.user.login.AuthSubject;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the Login of a user.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLoginAction
  extends    AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3063654197057820159L;
	public static final String ACTION_NAME = "UserLoginAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return UserLoginEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    UserLoginEvent loginEvent = (UserLoginEvent)event;

    Object[] params = new Object[]
                      {
                        loginEvent.getUserID(),
                        loginEvent.getPassword(),
                      };

    return constructEventResponse(
             IErrorCode.LOGIN_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    login((UserLoginEvent)event);
    return constructEventResponse();
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * Validates for SessionID and Application.
   * This action is for login, so no UserID validation.
   */
  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(false);
    checkApplication();
  }

  // **************************** Own Methods ****************************

  /**
   * Perform the user login.
   *
   * @param loginEvent The event for login.
   * @exception Exception Error during login.
   * @since 2.0
   */
  private void login(UserLoginEvent loginEvent) throws Exception
  {
    ActionHelper.getUserManager().login(
      _application,
      _sessionID,
      loginEvent.getUserID(),
      loginEvent.getPassword().toString());

    //after successful login, get the principals
    //from the stored session data and cache in statemachine.
    //also set the UserID in statemachine.
    obtainPrincipals();
    setUserID(loginEvent.getUserID());

    // Koh Han Sing 20020819
    UserAccount acct = ActionHelper.getUserManager().findUserAccount(_userID);
    setUserName(acct.getUserName());

    // amh 20021023
    try
    {
      GridNode myGridNode = ActionHelper.getGridNodeManager().findMyGridNode();
      setEnterpriseID(myGridNode.getID());
    }
    catch (Exception ex)
    {
      Logger.warn("My GridNode not setup yet!", ex);
    }
  }

  private void obtainPrincipals() throws Exception
  {
    AuthSubject subject = new AuthSubject(getSessionManager().getSessionData(
                              _sessionID, AuthSubject.KEY));

    if (subject.getDataContents() != null)
    {
      setPrincipals(subject.getSubject().getPrincipals());
    }
  }
}