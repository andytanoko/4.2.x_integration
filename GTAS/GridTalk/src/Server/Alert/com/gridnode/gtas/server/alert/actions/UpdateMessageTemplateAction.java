/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateMessageTemplateAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 02 May 2003    Neo Sok Lay         Add update for Log message template.
 * 11 Jan 2006		SC									Add update for Jms message template.
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;
import java.util.Vector;

import com.gridnode.gtas.events.alert.UpdateMessageTemplateEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a MessageTemplate.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.1
 * @since 2.0
 */
public class UpdateMessageTemplateAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3417199656951014811L;

	private MessageTemplate _messageTemplate;

  public static final String ACTION_NAME = "UpdateMessageTemplateAction";

  protected Class getExpectedEventClass()
  {
    return UpdateMessageTemplateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertMessageTemplateToMap((MessageTemplate)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateMessageTemplateEvent updEvent = (UpdateMessageTemplateEvent)event;
    _messageTemplate = ServiceLookupHelper.getAlertManager().getMessageTemplateByUId(updEvent.getUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateMessageTemplateEvent updEvent = (UpdateMessageTemplateEvent)event;

    /* This line was commented by someone else. I think it is because name cannot be updated.*/
    //_messageTemplate.setName(updEvent.getName());
    clearMostFields(_messageTemplate);
    _messageTemplate.setMessageType(updEvent.getMessageType());
    _messageTemplate.setMessage(updEvent.getMessage());

    if (MessageTemplate.MSG_TYPE_EMAIL.equals(updEvent.getMessageType()))
    {
      _messageTemplate.setFromAddr(updEvent.getFromAddr());
      _messageTemplate.setToAddr(updEvent.getToAddr());
      _messageTemplate.setCcAddr(updEvent.getCcAddr());
      _messageTemplate.setSubject(updEvent.getSubject());
      _messageTemplate.setContentType(updEvent.getContentType());
      _messageTemplate.setLocation(null);
      _messageTemplate.setAppend(null);
    }
    else if (MessageTemplate.MSG_TYPE_LOG.equals(updEvent.getMessageType()))
    {
      _messageTemplate.setLocation(updEvent.getLocation());
      _messageTemplate.setAppend(updEvent.getIsAppend());
      _messageTemplate.setFromAddr(null);
      _messageTemplate.setToAddr(null);
      _messageTemplate.setCcAddr(null);
      _messageTemplate.setSubject(null);
      _messageTemplate.setContentType(null);
    } 
    else if (MessageTemplate.MSG_TYPE_JMS.equals(updEvent.getMessageType()))
    {
    	Long jmsDestinationUid = updEvent.getJmsDestinationUid();
    	ActionHelper.setJmsDestination(jmsDestinationUid, _messageTemplate);
    	Vector vector = (Vector) updEvent.getMessageProperties();
    	Vector mpVector = ActionHelper.processMessageProperties(vector);
    	_messageTemplate.setMessageProperty(mpVector);
    }

    return _messageTemplate;
  }
  
  /* don't clear name beacuse name field cannot be updated */
  public void clearMostFields(MessageTemplate mt)
  {
  	mt.setContentType(null);
  	mt.setMessageType(null);
  	mt.setFromAddr(null);
  	mt.setToAddr(null);
  	mt.setCcAddr(null);
  	mt.setSubject(null);
  	mt.setMessage(null);
  	mt.setLocation(null);
  	mt.setAppend(null);
  	mt.setJmsDestination(null);
  	mt.setMessageProperty(null);
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ServiceLookupHelper.getAlertManager().updateMessageTemplate((MessageTemplate)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getAlertManager().getMessageTemplateByUId(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateMessageTemplateEvent updEvent = (UpdateMessageTemplateEvent)event;
    return new Object[]
           {
             MessageTemplate.ENTITY_NAME,
             String.valueOf(updEvent.getUid())
           };
  }
}