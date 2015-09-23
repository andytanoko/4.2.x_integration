/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTypeAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-04     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.alert.GetAlertTypeEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.alert.model.AlertType;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a AlertType.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAlertTypeAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3756895031418391147L;
	public static final String ACTION_NAME = "GetAlertTypeAction";

  protected Class getExpectedEventClass()
  {
    return GetAlertTypeEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAlertTypeEvent getEvent = (GetAlertTypeEvent)event;
    return ServiceLookupHelper.getAlertManager().getAlertTypeByUId(getEvent.getUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertTypeToMap((AlertType)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAlertTypeEvent getEvent = (GetAlertTypeEvent)event;
    return new Object[]
           {
             AlertType.ENTITY_NAME,
             String.valueOf(getEvent.getUid())
           };
  }
}