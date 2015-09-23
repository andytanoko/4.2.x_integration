/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateActionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.CreateActionEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new Action.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateActionAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2761571828692331282L;
	public static final String ACTION_NAME = "CreateActionAction";

  protected Class getExpectedEventClass()
  {
    return CreateActionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getActionByActionUId(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateActionEvent createEvent = (CreateActionEvent)event;

    Action newAction = new Action();
    newAction.setName(createEvent.getName());
    newAction.setDescr(createEvent.getDescription());
    newAction.setMsgUid(createEvent.getMessageTemplate());

    return newAction;
  }


  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateActionEvent createEvent = (CreateActionEvent)event;
    return new Object[]
           {
             Action.ENTITY_NAME,
             createEvent.getName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().createAction((Action)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertActionToMap((Action)entity);
  }

}