/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserProcedureAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction.
 */


package com.gridnode.gtas.server.userprocedure.actions;

import java.util.Map;

import com.gridnode.gtas.events.userprocedure.GetUserProcedureEvent;
import com.gridnode.gtas.model.userprocedure.UserProcedureEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetUserProcedureAction extends AbstractGetEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4824126724700061233L;
	private static final String ACTION_NAME = "GetUserProcedureAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return UserProcedure.convertToMap(entity,
		                                  UserProcedureEntityFieldID.getEntityFieldID(),
		                                  null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetUserProcedureEvent getUserProcedureEvent = (GetUserProcedureEvent)event;
		 
		return getManager().getUserProcedure(getUserProcedureEvent.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetUserProcedureEvent getUserProcedureEvent = (GetUserProcedureEvent)event;
		return new Object[] {UserProcedure.ENTITY_NAME, getUserProcedureEvent.getUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetUserProcedureEvent.class;
	}

  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }
}