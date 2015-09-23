/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateUserAccountAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 13 2002    Neo Sok Lay         Associate user of a default User role.
 * Jun 16 2002    Neo Sok Lay         Set the createBy user.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractCreateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Neo Sok Lay         Return created entity to client.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.gtas.server.user.helpers.Logger;
import com.gridnode.gtas.events.user.CreateUserAccountEvent;

import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the creation of a new User account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateUserAccountAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6407141172360130808L;
	public static final String ACTION_NAME = "CreateUserAccountAction";
  public static final String DEFAULT_USER_ROLE = "User";

  private static Long _defUserRoleUID;

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateUserAccountEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getUserManager().findUserAccount(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateUserAccountEvent addEvent = (CreateUserAccountEvent)event;

    UserAccount newAcct = new UserAccount();
    newAcct.setId(addEvent.getUserID());
    newAcct.setEmail(addEvent.getEmail());
    newAcct.setPassword(addEvent.getInitialPassword());
    newAcct.setPhone(addEvent.getPhone());
    newAcct.setProperty(addEvent.getProperty());
    newAcct.setUserName(addEvent.getUserName());

    UserAccountState acctState = new UserAccountState();
    acctState.setUserID(addEvent.getUserID());
    acctState.setState(addEvent.isEnabled() ?
      UserAccountState.STATE_ENABLED : UserAccountState.STATE_DISABLED);
    acctState.setCreateBy(_userID);

    newAcct.setAccountState(acctState);

    return newAcct;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateUserAccountEvent addEvent = (CreateUserAccountEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             UserAccount.ENTITY_NAME,
             addEvent.getUserID(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    Long created = ActionHelper.getUserManager().createUserAccount(
                            (UserAccount)entity);

    //assign default role to new user
    try
    {
      if (_defUserRoleUID == null)
      {
        Role role = ActionHelper.verifyRole(DEFAULT_USER_ROLE);
        _defUserRoleUID = new Long(role.getUId());
      }

      ActionHelper.getACLManager().assignRoleToSubject(
          _defUserRoleUID,
          created,
          UserAccount.ENTITY_NAME);
    }
    catch (Exception ex)
    {
      Logger.warn("Unable to retrieve default user role!", ex);
    }

    return created;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertUserAccountToMap((UserAccount)entity);
  }

  // ****************** Own methods **********************************
}