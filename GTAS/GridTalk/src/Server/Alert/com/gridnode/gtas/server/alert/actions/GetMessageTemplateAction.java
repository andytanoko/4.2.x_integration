/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMessageTemplateAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.alert.GetMessageTemplateEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.alert.model.MessageTemplate;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a MessageTemplate.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetMessageTemplateAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7640672083651673098L;
	public static final String ACTION_NAME = "GetMessageTemplateAction";

  protected Class getExpectedEventClass()
  {
    return GetMessageTemplateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetMessageTemplateEvent getEvent = (GetMessageTemplateEvent)event;
    return ServiceLookupHelper.getAlertManager().getMessageTemplateByUId(getEvent.getUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertMessageTemplateToMap((MessageTemplate)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetMessageTemplateEvent getEvent = (GetMessageTemplateEvent)event;
    return new Object[]
           {
             MessageTemplate.ENTITY_NAME,
             String.valueOf(getEvent.getUid())
           };
  }
}