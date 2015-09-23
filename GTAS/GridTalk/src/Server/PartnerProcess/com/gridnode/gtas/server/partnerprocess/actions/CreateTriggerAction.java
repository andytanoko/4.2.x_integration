/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 * Dec 13 2002    Koh Han Sing        Added triggerType and isRequest
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Oct 20 2003    Guo Jianyu          Add NumOfRetries, RetryInterval and ChannelUID
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.Map;

import com.gridnode.gtas.events.partnerprocess.CreateTriggerEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public class CreateTriggerAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5661352113680391256L;
	public static final String ACTION_NAME = "CreateTriggerAction";

  protected Class getExpectedEventClass()
  {
    return CreateTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findTrigger(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateTriggerEvent createEvent = (CreateTriggerEvent)event;

    Trigger newTrigger = new Trigger();
    newTrigger.setLevel(createEvent.getTriggerLevel());
    newTrigger.setPartnerFunctionId(createEvent.getPartnerFunctionId());
    newTrigger.setProcessId(createEvent.getProcessId());
    newTrigger.setDocumentType(createEvent.getDocType());
    newTrigger.setPartnerType(createEvent.getPartnerType());
    newTrigger.setPartnerGroup(createEvent.getPartnerGroup());
    newTrigger.setPartnerId(createEvent.getPartnerId());
    newTrigger.setTriggerType(createEvent.getTriggerType());
    newTrigger.setIsRequest(createEvent.getIsRequest());
    newTrigger.setIsLocalPending(createEvent.getIsLocalPending());
    newTrigger.setNumOfRetries(createEvent.getNumOfRetries());
    newTrigger.setRetryInterval(createEvent.getRetryInterval());
    newTrigger.setChannelUID(createEvent.getChannelUID());

    return newTrigger;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateTriggerEvent createEvent = (CreateTriggerEvent)event;
    return new Object[]
           {
             Trigger.ENTITY_NAME,
             String.valueOf(createEvent.getTriggerLevel()),
             createEvent.getPartnerFunctionId(),
             createEvent.getProcessId(),
             createEvent.getDocType(),
             createEvent.getPartnerType(),
             createEvent.getPartnerGroup(),
             createEvent.getPartnerId(),
             createEvent.getTriggerType(),
             createEvent.getIsRequest(),
             createEvent.getIsLocalPending(),
             createEvent.getNumOfRetries(),
             createEvent.getRetryInterval(),
             createEvent.getChannelUID()
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createTrigger((Trigger)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertTriggerToMap((Trigger)entity);
  }

}