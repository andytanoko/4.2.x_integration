/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateFileTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 25 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;

import com.gridnode.gtas.events.document.CreateFileTypeEvent;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the creation of a new FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateFileTypeAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 115299544415137878L;
	public static final String ACTION_NAME = "CreateFileTypeAction";

  protected Class getExpectedEventClass()
  {
    return CreateFileTypeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findFileType(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateFileTypeEvent createEvent = (CreateFileTypeEvent)event;

    FileType newFileType = new FileType();
    newFileType.setName(createEvent.getFileTypeName());
    newFileType.setDescription(createEvent.getFileTypeDesc());
    newFileType.setProgramName(createEvent.getProgramName());
    newFileType.setProgramPath(createEvent.getProgramPath());
    newFileType.setParameters(createEvent.getParameters());
    newFileType.setWorkingDirectory(createEvent.getWorkingDir());

    return newFileType;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateFileTypeEvent createEvent = (CreateFileTypeEvent)event;
    return new Object[]
           {
             FileType.ENTITY_NAME,
             createEvent.getFileTypeName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createFileType((FileType)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertFileTypeToMap((FileType)entity);
  }

}