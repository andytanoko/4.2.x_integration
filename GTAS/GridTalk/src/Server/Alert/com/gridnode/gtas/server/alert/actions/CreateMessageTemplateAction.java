/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateMessageTemplateAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 02 May 2003    Neo Sok Lay         Handle creation of Log message template.
 * 12 Jan 2005		SC									Handle creation of Jms message template.
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.gridnode.gtas.events.alert.CreateMessageTemplateEvent;
import com.gridnode.gtas.model.alert.IMessageProperty;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.Logger;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.alert.model.MessageProperty;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.AssertUtil;

/**
 * This Action class handles the creation of a new MessageTemplate.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateMessageTemplateAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8269186812538421958L;
	public static final String ACTION_NAME = "CreateMessageTemplateAction";

  protected Class getExpectedEventClass()
  {
    return CreateMessageTemplateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getMessageTemplateByUId(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateMessageTemplateEvent createEvent = (CreateMessageTemplateEvent)event;
    
    MessageTemplate newMessageTemplate = new MessageTemplate();
    newMessageTemplate.setName(createEvent.getName());
    newMessageTemplate.setMessage(createEvent.getMessage());
    String messageType = createEvent.getMessageType();
    newMessageTemplate.setMessageType(messageType);

    if (MessageTemplate.MSG_TYPE_EMAIL.equals(messageType))
    {
      newMessageTemplate.setFromAddr(createEvent.getFromAddr());
      newMessageTemplate.setToAddr(createEvent.getToAddr());
      newMessageTemplate.setCcAddr(createEvent.getCcAddr());
      newMessageTemplate.setSubject(createEvent.getSubject());
      newMessageTemplate.setContentType(createEvent.getContentType());
    }
    else if (MessageTemplate.MSG_TYPE_LOG.equals(messageType))
    {
      newMessageTemplate.setLocation(createEvent.getLocation());
      newMessageTemplate.setAppend(createEvent.getIsAppend());
    }
    else if (MessageTemplate.MSG_TYPE_JMS.equals(messageType))
    {
    	Long jmsDestinationUid = createEvent.getJmsDestinationUid();
    	ActionHelper.setJmsDestination(jmsDestinationUid, newMessageTemplate);
    	Vector mpVector = ActionHelper.processMessageProperties((Vector) createEvent.getMessageProperties());
    	newMessageTemplate.setMessageProperty(mpVector);
    }
    else
    {
    	AssertUtil.fail("cannot reach here");
    }

    return newMessageTemplate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateMessageTemplateEvent createEvent = (CreateMessageTemplateEvent)event;
    return new Object[]
           {
             MessageTemplate.ENTITY_NAME,
             createEvent.getName()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().createMessageTemplate((MessageTemplate)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertMessageTemplateToMap((MessageTemplate)entity);
  }

  private void log(String message)
  {
  	Logger.log("[CreateMessageTemplateAction] " + message);
  }
}