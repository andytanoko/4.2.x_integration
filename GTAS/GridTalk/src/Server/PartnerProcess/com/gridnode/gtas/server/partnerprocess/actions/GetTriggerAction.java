/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.partnerprocess.GetTriggerEvent;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetTriggerAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7180255748155755845L;
	public static final String ACTION_NAME = "GetTriggerAction";

  protected Class getExpectedEventClass()
  {
    return GetTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetTriggerEvent getEvent = (GetTriggerEvent)event;
    return ActionHelper.getManager().findTrigger(getEvent.getTriggerUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertTriggerToMap((Trigger)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetTriggerEvent getEvent = (GetTriggerEvent)event;
    return new Object[]
           {
             Trigger.ENTITY_NAME,
             String.valueOf(getEvent.getTriggerUid())
           };
  }
}