/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateAlertAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Create Alert will also create the
 *                                    AlertAction bindings.
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.CreateAlertEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new Alert.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateAlertAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7005109437032190114L;
	public static final String ACTION_NAME = "CreateAlertAction";

  protected Class getExpectedEventClass()
  {
    return CreateAlertEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getAlertByAlertUId(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateAlertEvent createEvent = (CreateAlertEvent)event;

    Alert newAlert = new Alert();
    newAlert.setName(createEvent.getName());
    newAlert.setDescr(createEvent.getDescription());
    newAlert.setAlertType(createEvent.getType());
    newAlert.setBindActions(createEvent.getActions());
    return newAlert;
  }


  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateAlertEvent createEvent = (CreateAlertEvent)event;
    return new Object[]
           {
             Alert.ENTITY_NAME,
             createEvent.getName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.createAlert((Alert)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertToMap((Alert)entity);
  }

}