/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateUserAccountAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 25 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 14 2002    Neo Sok Lay         Checking for update only own account (
 *                                    if only profile update).
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractUpdateEntityAction
 *                                    instead of GridTalkEJBAction (to be phased
 *                                    out).
 * Jul 25 2002    Neo Sok Lay         Return updated entity to client.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.gtas.events.user.UpdateUserAccountEvent;

import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the update of a User account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateUserAccountAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6969016811518660469L;

	public static final String ACTION_NAME = "UpdateUserAccountAction";

  private UserAccount _acctToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.getUserManager().updateUserAccount(
      (UserAccount)entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateUserAccountEvent updEvent = (UpdateUserAccountEvent)event;

    _acctToUpdate.setEmail(updEvent.getUpdEmail());
    _acctToUpdate.setPhone(updEvent.getUpdPhone());
    _acctToUpdate.setProperty(updEvent.getUpdProperty());
    _acctToUpdate.setUserName(updEvent.getUpdUserName());

    //account state update required
    if (updEvent.getUpdState() != null)
    {
      UserAccountState acctState = _acctToUpdate.getAccountState();
      acctState.setState(updEvent.getUpdState().shortValue());

      if (updEvent.isResetLoginTries())
        acctState.setNumLoginTries((short)0);

      if (updEvent.isUnfreezeAccount())
      {
        acctState.setFreezeTime(null);
        acctState.setIsFreeze(false);
      }

      if (updEvent.getResetPassword() != null)
        _acctToUpdate.setPassword(updEvent.getResetPassword());
    }

    return _acctToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateUserAccountEvent updEvent = (UpdateUserAccountEvent)event;
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
    return UpdateUserAccountEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    _acctToUpdate = verifyValidAccount((UpdateUserAccountEvent)event);
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

  private UserAccount verifyValidAccount(UpdateUserAccountEvent event)
    throws Exception
  {
    UserAccount acct = ActionHelper.verifyUserAccount(event.getAccountUID());
    if (event.getUpdState() == null) //profile update only
    {
      if (!acct.getId().equals(_userID)) //must be own account
        throw new Exception("Not allowed to update other user's account profile!");
    }

    return acct;
  }
}