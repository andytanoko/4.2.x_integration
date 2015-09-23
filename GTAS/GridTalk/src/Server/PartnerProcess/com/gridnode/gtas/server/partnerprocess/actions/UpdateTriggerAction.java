/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 * Dec 13 2002    Koh Han Sing        Added triggerType and isRequest
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Dec 04 2003    Guo Jianyu          Added NumOfRetries, ChannelUid and RetryInterval
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import java.util.Map;

import com.gridnode.gtas.events.partnerprocess.UpdateTriggerEvent;
import com.gridnode.gtas.server.partnerprocess.helpers.ActionHelper;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.0
 */
public class UpdateTriggerAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8538454508428674049L;

	private Trigger _trigger;

  public static final String ACTION_NAME = "UpdateTriggerAction";

  protected Class getExpectedEventClass()
  {
    return UpdateTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertTriggerToMap((Trigger)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateTriggerEvent updEvent = (UpdateTriggerEvent)event;
    _trigger = ActionHelper.getManager().findTrigger(updEvent.getTriggerUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateTriggerEvent updEvent = (UpdateTriggerEvent)event;

    _trigger.setLevel(updEvent.getTriggerLevel());
    _trigger.setPartnerFunctionId(updEvent.getPartnerFunctionId());
    _trigger.setProcessId(updEvent.getProcessId());
    _trigger.setDocumentType(updEvent.getDocType());
    _trigger.setPartnerType(updEvent.getPartnerType());
    _trigger.setPartnerGroup(updEvent.getPartnerGroup());
    _trigger.setPartnerId(updEvent.getPartnerId());
    _trigger.setTriggerType(updEvent.getTriggerType());
    _trigger.setIsRequest(updEvent.getIsRequest());
    _trigger.setIsLocalPending(updEvent.getIsLocalPending());
    _trigger.setChannelUID(updEvent.getChannelUID());
    _trigger.setNumOfRetries(updEvent.getNumOfRetries());
    _trigger.setRetryInterval(updEvent.getRetryInterval());

    return _trigger;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateTrigger((Trigger)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findTrigger(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateTriggerEvent updEvent = (UpdateTriggerEvent)event;
    return new Object[]
           {
             Trigger.ENTITY_NAME,
             String.valueOf(updEvent.getTriggerUid())
           };
  }

}