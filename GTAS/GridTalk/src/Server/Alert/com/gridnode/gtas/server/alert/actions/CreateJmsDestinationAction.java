/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateJmsDestinationAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.events.alert.CreateJmsDestinationEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.Logger;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class CreateJmsDestinationAction
  extends    AbstractCreateEntityAction
{ 
	public static final String ACTION_NAME = "CreateJmsDestinationAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateJmsDestinationEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().getJmsDestinationByUID(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
  	CreateJmsDestinationEvent addEvent = (CreateJmsDestinationEvent) event;
  	JmsDestination jd = new JmsDestination();
  	jd.setName(addEvent.getName());
  	jd.setType(addEvent.getType());
  	jd.setJndiName(addEvent.getJndiName());
  	jd.setDeliveryMode(addEvent.getDeliveryMode());
  	jd.setPriority(addEvent.getPriority());
  	jd.setConnectionFactoryJndi(addEvent.getConnectionFactoryJndi());
  	jd.setConnectionUser(addEvent.getConnectionUser());
  	jd.setConnectionPassword(addEvent.getConnectionPassword());
  	jd.setLookupProperties(addEvent.getLookupProperties());
  	jd.setRetryInterval(addEvent.getRetryInterval());
  	jd.setMaximumRetries(addEvent.getMaximumRetries());
  	return jd;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
  	CreateJmsDestinationEvent addEvent = (CreateJmsDestinationEvent) event;
  	return new Object[]
  	                  {
  	                    JmsDestination.ENTITY_NAME,
  	                    addEvent.getName()
  	                  };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().createJmsDestination((JmsDestination) entity); 
  }

  protected Map convertToMap(AbstractEntity entity)
  {
  	return ActionHelper.convertJmsDestinationToMap((JmsDestination) entity);
  }

//  /**
//   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doSemanticValidation(com.gridnode.pdip.framework.rpf.event.IEvent)
//   */
//  protected void doSemanticValidation(IEvent event) throws Exception
//  {
//    CreateBusinessEntityEvent addEvent = (CreateBusinessEntityEvent)event;
//    Collection domainIdentifiers = addEvent.getDomainIdentifiers();
//    if (domainIdentifiers != null)
//    {
//      ActionHelper.checkDomainIdentifiers(domainIdentifiers, addEvent);
//    }
//  }

  // ****************** Own methods **********************************
  
  private void log(String message)
  {
  	Logger.log("[CreateJmsDestinationAction]" + message);
  }
  
}