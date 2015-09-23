/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 18 2002    Neo Sok Lay         Extend from AbstractGetEntityAction.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.gtas.events.user.GetUserAccountEvent;

import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a User account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -304990013274785616L;
	public static final String ACTION_NAME = "GetUserAccountAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertUserAccountToMap((UserAccount)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetUserAccountEvent getEvent = (GetUserAccountEvent)event;

    if (getEvent.getAccountUID() == null)
      return ActionHelper.getUserManager().findUserAccount(getEvent.getUserID());
    else
      return ActionHelper.getUserManager().findUserAccount(getEvent.getAccountUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetUserAccountEvent getEvent = (GetUserAccountEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getAccountUID()),
             String.valueOf(getEvent.getUserID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetUserAccountEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}