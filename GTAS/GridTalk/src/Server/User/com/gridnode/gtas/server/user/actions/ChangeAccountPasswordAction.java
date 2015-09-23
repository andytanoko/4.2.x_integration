/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChangeAccountPasswordAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 14 2002    Neo Sok Lay         Checking for changing only own account.
 *                                    Checking for correct current password.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractUpdateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Neo Sok Lay         Return updated entity to client.
 */
package com.gridnode.gtas.server.user.actions;

import java.util.Map;

import com.gridnode.gtas.events.user.ChangeAccountPasswordEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the update of a User account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ChangeAccountPasswordAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4955223043649783262L;

	public static final String ACTION_NAME = "ChangeAccountPasswordAction";

  private UserAccount _acctToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getUserManager().updateUserAccount(
      (UserAccount)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    ChangeAccountPasswordEvent updEvent = (ChangeAccountPasswordEvent)event;

    _acctToUpdate.setPassword(updEvent.getNewPassword());

    return _acctToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ChangeAccountPasswordEvent updEvent = (ChangeAccountPasswordEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             UserAccount.ENTITY_NAME,
             updEvent.getAccountUID(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return ChangeAccountPasswordEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    ChangeAccountPasswordEvent chgEvent = (ChangeAccountPasswordEvent)event;

    return constructEventResponse(
             IErrorCode.CHANGE_PASSWORD_ERROR,
             getErrorMessageParams(event),
             ex);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    _acctToUpdate = verifyValidAccount((ChangeAccountPasswordEvent)event);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getUserManager().findUserAccount(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertUserAccountToMap((UserAccount)entity);
  }

  // **************************** Own Methods *****************************

  /**
   * Verify the existence of the user specified in the event. The user
   * must also be the same as the user logon to the current session.
   *
   * @param event The change password event.
   * @return The retrieved UserAccount is the specified account exists in the
   * database.
   * @exception Exception Bad User UID. The account does not exist in the
   * database, or the account does not belong to the user logon to the current
   * session.
   *
   * @since 2.0
   */
  private UserAccount verifyValidAccount(ChangeAccountPasswordEvent event)
    throws Exception
  {
    UserAccount acct = ActionHelper.verifyUserAccount(event.getAccountUID());

    //check that can only change own account password
    if (!_userID.equals(acct.getId()))
      throw new Exception("Not allowed to change other user's password!");

    //check that the password is correct
    if (!acct.isPasswordMatch(event.getCurrentPassword()))
      throw new Exception("Incorrect current password. Change not allowed.");

    return acct;
  }
}