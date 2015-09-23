/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Also retrieve the UIDs of the Actions
 *                                    bound to the Alert.
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.alert.GetAlertEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;

import com.gridnode.pdip.app.alert.model.Alert;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Alert.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAlertAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7276724889282046635L;
	public static final String ACTION_NAME = "GetAlertAction";

  protected Class getExpectedEventClass()
  {
    return GetAlertEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAlertEvent getEvent = (GetAlertEvent)event;
    return ActionHelper.getAlertByUid(getEvent.getUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertToMap((Alert)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAlertEvent getEvent = (GetAlertEvent)event;
    return new Object[]
           {
             Alert.ENTITY_NAME,
             String.valueOf(getEvent.getUid())
           };
  }
}