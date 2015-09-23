/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateActionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.UpdateActionEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a Action.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateActionAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7801819761078621254L;

	private Action _action;

  public static final String ACTION_NAME = "UpdateActionAction";

  protected Class getExpectedEventClass()
  {
    return UpdateActionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertActionToMap((Action)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateActionEvent updEvent = (UpdateActionEvent)event;
    _action = ServiceLookupHelper.getAlertManager().getActionByActionUId(updEvent.getUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateActionEvent updEvent = (UpdateActionEvent)event;

    _action.setName(updEvent.getName());
    _action.setDescr(updEvent.getDescription());
    _action.setMsgUid(updEvent.getMessageTemplate());

    return _action;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ServiceLookupHelper.getAlertManager().updateAction((Action)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getActionByActionUId(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateActionEvent updEvent = (UpdateActionEvent)event;
    return new Object[]
           {
             Action.ENTITY_NAME,
             String.valueOf(updEvent.getUid())
           };
  }
}