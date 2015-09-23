/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateAlertAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Update Alert will also update the
 *                                    AlertAction bindings (add/remove).
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.UpdateAlertEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a Alert.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateAlertAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1289717400661850213L;

	private Alert _alert;

  public static final String ACTION_NAME = "UpdateAlertAction";

  protected Class getExpectedEventClass()
  {
    return UpdateAlertEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertToMap((Alert)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateAlertEvent updEvent = (UpdateAlertEvent)event;
    _alert = ServiceLookupHelper.getAlertManager().getAlertByAlertUId(updEvent.getUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateAlertEvent updEvent = (UpdateAlertEvent)event;

    _alert.setName(updEvent.getName());
    _alert.setDescr(updEvent.getDescription());
    _alert.setAlertType(updEvent.getType());
    _alert.setBindActions(updEvent.getActions());

    return _alert;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.updateAlert((Alert)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getAlertByAlertUId(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateAlertEvent updEvent = (UpdateAlertEvent)event;
    return new Object[]
           {
             Alert.ENTITY_NAME,
             String.valueOf(updEvent.getUid())
           };
  }
}