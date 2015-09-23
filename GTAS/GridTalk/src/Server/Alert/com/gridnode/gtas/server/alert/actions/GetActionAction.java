/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetActionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.alert.GetActionEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.alert.model.Action;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Action.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetActionAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3849108915704936439L;
	public static final String ACTION_NAME = "GetActionAction";

  protected Class getExpectedEventClass()
  {
    return GetActionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetActionEvent getEvent = (GetActionEvent)event;
    return ServiceLookupHelper.getAlertManager().getActionByActionUId(getEvent.getUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertActionToMap((Action)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetActionEvent getEvent = (GetActionEvent)event;
    return new Object[]
           {
             Action.ENTITY_NAME,
             String.valueOf(getEvent.getUid())
           };
  }
}