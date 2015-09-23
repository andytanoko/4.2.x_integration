/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAttachmentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.document.GetAttachmentEvent;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Attachment.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAttachmentAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2377081116886643906L;
	public static final String ACTION_NAME = "GetAttachmentAction";

  protected Class getExpectedEventClass()
  {
    return GetAttachmentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAttachmentEvent getEvent = (GetAttachmentEvent)event;
    return ActionHelper.getManager().findAttachment(getEvent.getAttachmentUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAttachmentToMap((Attachment)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAttachmentEvent getEvent = (GetAttachmentEvent)event;
    return new Object[]
           {
             Attachment.ENTITY_NAME,
             getEvent.getAttachmentUID()
           };
  }
}