/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMappingFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction.
 */
package com.gridnode.gtas.server.mapper.actions;

import java.util.Map;

import com.gridnode.gtas.events.mapper.GetMappingFileEvent;
import com.gridnode.gtas.model.mapper.MappingFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class GetMappingFileAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3488531929081765095L;
	private static final String ACTION_NAME = "GetMappingFileAction";
	
	protected Map convertToMap(AbstractEntity entity)
	{
		return MappingFile.convertToMap(entity, MappingFileEntityFieldID.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
	  GetMappingFileEvent getEvent = (GetMappingFileEvent)event;
		return getManager().findMappingFile(getEvent.getMappingFileUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
	  GetMappingFileEvent getEvent = (GetMappingFileEvent)event;
		return new Object[] {MappingFile.ENTITY_NAME, getEvent.getMappingFileUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetMappingFileEvent.class;
	}

  private IMappingManagerObj getManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }
}