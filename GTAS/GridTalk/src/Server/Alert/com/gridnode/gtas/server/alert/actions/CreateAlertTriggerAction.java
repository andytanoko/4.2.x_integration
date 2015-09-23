/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateAlertTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;


import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.events.alert.CreateAlertTriggerEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class CreateAlertTriggerAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6385351674816490284L;
	public static final String ACTION_NAME = "CreateAlertTriggerAction";


  protected Class getExpectedEventClass()
  {
    return CreateAlertTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getGridTalkAlertMgr().findAlertTrigger(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateAlertTriggerEvent createEvent = (CreateAlertTriggerEvent)event;

    AlertTrigger newTrigger = new AlertTrigger();

    newTrigger.setLevel(createEvent.getLevel().intValue());
    newTrigger.setAlertType(createEvent.getAlertType());
    newTrigger.setDocumentType(createEvent.getDocType());
    newTrigger.setPartnerType(createEvent.getPartnerType());
    newTrigger.setPartnerGroup(createEvent.getPartnerGroup());
    newTrigger.setPartnerId(createEvent.getPartnerID());
    newTrigger.setAlertUID(createEvent.getAlertUID());

    Boolean isEnabled = createEvent.getIsEnabled();
    if (isEnabled != null)
      newTrigger.setEnabled(isEnabled.booleanValue());

    Boolean isAttachDoc = createEvent.getIsAttachDoc();
    if (isAttachDoc != null)
      newTrigger.setAttachDoc(isAttachDoc.booleanValue());

    Collection recipients = createEvent.getRecipients();
    if (recipients != null)
      newTrigger.getRecipients().addAll(recipients);

    return newTrigger;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateAlertTriggerEvent createEvent = (CreateAlertTriggerEvent)event;
    return new Object[]
           {
             AlertTrigger.ENTITY_NAME,
             createEvent.getLevel(),
             createEvent.getAlertType(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ServiceLookupHelper.getGridTalkAlertMgr().createAlertTrigger(
             (AlertTrigger)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertTriggerToMap((AlertTrigger)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    CreateAlertTriggerEvent createEvent = (CreateAlertTriggerEvent)event;

    Long alertUID = createEvent.getAlertUID();
    String alertType = createEvent.getAlertType();
    ActionHelper.checkAlertExists(alertUID);
    ActionHelper.checkAlertTypeExists(alertType);

  }

}