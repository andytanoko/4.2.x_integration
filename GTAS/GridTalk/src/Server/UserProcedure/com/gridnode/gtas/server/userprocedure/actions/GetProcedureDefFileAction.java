/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcedureDefFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */

package com.gridnode.gtas.server.userprocedure.actions;

import java.util.Map;

import com.gridnode.gtas.events.userprocedure.GetProcedureDefFileEvent;
import com.gridnode.gtas.model.userprocedure.ProcedureDefFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class GetProcedureDefFileAction extends AbstractGetEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6408095222909409082L;
	private static final String ACTION_NAME = "GetProcedureDefFileAction";


  protected Map convertToMap(AbstractEntity entity)
	{
  	return ProcedureDefFile.convertToMap(entity,
  	                                     ProcedureDefFileEntityFieldID.getEntityFieldID(),
  	                                     null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetProcedureDefFileEvent getProcedureDefFileEvent =(GetProcedureDefFileEvent) event;
		return getManager().getProcedureDefinitionFile(getProcedureDefFileEvent.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetProcedureDefFileEvent getProcedureDefFileEvent =(GetProcedureDefFileEvent) event;
		return new Object[] {ProcedureDefFile.ENTITY_NAME, getProcedureDefFileEvent.getUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetProcedureDefFileEvent.class;
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



