/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateFileTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 26 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;

import com.gridnode.gtas.events.document.UpdateFileTypeEvent;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the update of a FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateFileTypeAction
  extends    AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1630812796843362666L;

	private FileType _fileType;

  public static final String ACTION_NAME = "UpdateFileTypeAction";

  protected Class getExpectedEventClass()
  {
    return UpdateFileTypeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertFileTypeToMap((FileType)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateFileTypeEvent updEvent = (UpdateFileTypeEvent)event;
    _fileType = ActionHelper.getManager().findFileType(updEvent.getFileTypeUID());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateFileTypeEvent updEvent = (UpdateFileTypeEvent)event;

    _fileType.setDescription(updEvent.getFileTypeDesc());
    _fileType.setParameters(updEvent.getParameters());
    _fileType.setProgramName(updEvent.getProgramName());
    _fileType.setProgramPath(updEvent.getProgramPath());
    _fileType.setWorkingDirectory(updEvent.getWorkingDir());

    return _fileType;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateFileType((FileType)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findFileType(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateFileTypeEvent updEvent = (UpdateFileTypeEvent)event;
    return new Object[]
           {
             FileType.ENTITY_NAME,
             updEvent.getFileTypeUID()
           };
  }

}