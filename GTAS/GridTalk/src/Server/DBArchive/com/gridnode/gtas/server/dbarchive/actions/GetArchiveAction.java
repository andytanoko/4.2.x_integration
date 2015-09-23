/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetArchiveAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 17 2009    Ong Eu Soon           Created
 */


package com.gridnode.gtas.server.dbarchive.actions;

import java.util.Map;

import com.gridnode.gtas.events.dbarchive.GetArchiveEvent;
import com.gridnode.gtas.model.dbarchive.ArchiveEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerObj;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerHome;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetArchiveAction extends AbstractGetEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4824126724700061233L;
	private static final String ACTION_NAME = "GetArchiveAction";

  protected Map convertToMap(AbstractEntity entity)
	{
		return ArchiveMetaInfo.convertToMap(entity,
		                                  ArchiveEntityFieldID.getEntityFieldID(),
		                                  null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetArchiveEvent getArchiveEvent = (GetArchiveEvent)event;
		 
		return getManager().getArchive(getArchiveEvent.getUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetArchiveEvent getArchiveEvent = (GetArchiveEvent)event;
		return new Object[] {ArchiveMetaInfo.ENTITY_NAME, getArchiveEvent.getUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetArchiveEvent.class;
	}

  private IArchiveManagerObj getManager()
    throws ServiceLookupException
  {
    return (IArchiveManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IArchiveManagerHome.class.getName(),
               IArchiveManagerHome.class,
               new Object[0]);
  }
}