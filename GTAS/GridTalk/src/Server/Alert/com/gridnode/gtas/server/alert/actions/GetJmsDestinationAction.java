/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetJmsDestinationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.events.alert.GetJmsDestinationEvent;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

public class GetJmsDestinationAction
  extends    AbstractGetEntityAction
{ 
	public static final String ACTION_NAME = "GetJmsDestinationAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
  	return ActionHelper.convertJmsDestinationToMap((JmsDestination) entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
  	GetJmsDestinationEvent getEvent = (GetJmsDestinationEvent) event;
  	return ServiceLookupHelper.getAlertManager().getJmsDestinationByUID(getEvent.getUid());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
  	GetJmsDestinationEvent getEvent = (GetJmsDestinationEvent) event;
  	return new Object[]
  	                  {
  	                    String.valueOf(getEvent.getUid())
  	                  };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetJmsDestinationEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}