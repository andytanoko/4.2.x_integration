/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFileTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 26 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.document.GetFileTypeEvent;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.gtas.server.document.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetFileTypeAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2531775148419012519L;
	public static final String ACTION_NAME = "GetFileTypeAction";

  protected Class getExpectedEventClass()
  {
    return GetFileTypeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetFileTypeEvent getEvent = (GetFileTypeEvent)event;
    return ActionHelper.getManager().findFileType(getEvent.getFileTypeUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertFileTypeToMap((FileType)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetFileTypeEvent updEvent = (GetFileTypeEvent)event;
    return new Object[]
           {
             FileType.ENTITY_NAME,
             updEvent.getFileTypeUID()
           };
  }
}