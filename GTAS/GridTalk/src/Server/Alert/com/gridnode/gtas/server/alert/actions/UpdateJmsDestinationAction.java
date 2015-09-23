/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJmsDestinationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.events.alert.UpdateJmsDestinationEvent;
import com.gridnode.gtas.events.bizreg.UpdateBusinessEntityEvent;
import com.gridnode.gtas.events.partner.UpdatePartnerEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;

import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class UpdateJmsDestinationAction
  extends    AbstractUpdateEntityAction
{ 
	public static final String ACTION_NAME = "UpdateJmsDestinationAction";

	private JmsDestination jdToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
  	ServiceLookupHelper.getAlertManager().updateJmsDestination((JmsDestination) entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
	{
  	UpdateJmsDestinationEvent ue = (UpdateJmsDestinationEvent) event;
  	jdToUpdate = ServiceLookupHelper.getAlertManager().getJmsDestinationByUID(ue.getUid());
	}
  
  protected AbstractEntity prepareUpdateData(IEvent event)
  {
  	UpdateJmsDestinationEvent ue = (UpdateJmsDestinationEvent) event;
  	jdToUpdate.setName(ue.getName());
  	jdToUpdate.setType(ue.getType());
  	jdToUpdate.setJndiName(ue.getJndiName());
  	jdToUpdate.setDeliveryMode(ue.getDeliveryMode());
  	jdToUpdate.setPriority(ue.getPriority());
  	jdToUpdate.setConnectionFactoryJndi(ue.getConnectionFactoryJndi());
  	jdToUpdate.setConnectionUser(ue.getConnectionUser());
  	jdToUpdate.setConnectionPassword(ue.getConnectionPassword());
  	jdToUpdate.setLookupProperties(ue.getLookupProperties());
  	jdToUpdate.setRetryInterval(ue.getRetryInterval());
  	jdToUpdate.setMaximumRetries(ue.getMaximumRetries());
  	return jdToUpdate; 
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
  	UpdateJmsDestinationEvent updateEvent = (UpdateJmsDestinationEvent) event;
  	return new Object[]
  	                  {
  	                    JmsDestination.ENTITY_NAME,
  	                    updateEvent.getUid()
  	                  };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateJmsDestinationEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().getJmsDestinationByUID(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
  	return ActionHelper.convertJmsDestinationToMap((JmsDestination) entity);
  }
}