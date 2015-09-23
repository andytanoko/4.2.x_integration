/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserLogoutAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 05 2002    Neo Sok Lay         Remove principals from statemachine.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGridTalkAction instead
 *                                    of GridTalkEJBAction (to be phased out).
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the Logout of a user.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLogoutAction
  extends    AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6547318038761150475L;
	public static final String ACTION_NAME = "UserLogoutAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return UserLogoutEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                        _userID,
                        _application,
                      };

    return constructEventResponse(
             IErrorCode.LOGOUT_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    logout((UserLogoutEvent)event);
    return constructEventResponse();
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // **************************** Own Methods ****************************

  /**
   * Perform the user logout.
   *
   * @param loginEvent The event for logout.
   * @exception Exception Error during logout.
   * @since 2.0
   */
  private void logout(UserLogoutEvent logoutEvent) throws Exception
  {
    ActionHelper.getUserManager().logout(_sessionID);

    //after successful logout, clear the principals and UserID
    //from the statemachine.
    clearPrincipals();
    clearUserID();
  }
}