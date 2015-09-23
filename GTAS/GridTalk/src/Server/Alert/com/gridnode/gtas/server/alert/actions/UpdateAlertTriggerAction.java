/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateAlertTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.events.alert.UpdateAlertTriggerEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UpdateAlertTriggerAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3061057889295726292L;

	private AlertTrigger _trigger;

  public static final String ACTION_NAME = "UpdateAlertTriggerAction";

  protected Class getExpectedEventClass()
  {
    return UpdateAlertTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertTriggerToMap((AlertTrigger)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateAlertTriggerEvent updEvent = (UpdateAlertTriggerEvent)event;
    _trigger = ServiceLookupHelper.getGridTalkAlertMgr().findAlertTrigger(
                 updEvent.getUID());

    Long alertUID = updEvent.getAlertUID();
    ActionHelper.checkAlertExists(alertUID);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateAlertTriggerEvent updEvent = (UpdateAlertTriggerEvent)event;

    _trigger.setDocumentType(updEvent.getDocType());
    _trigger.setPartnerType(updEvent.getPartnerType());
    _trigger.setPartnerGroup(updEvent.getPartnerGroup());
    _trigger.setPartnerId(updEvent.getPartnerID());
    _trigger.setAlertUID(updEvent.getAlertUID());

    Boolean isEnabled = updEvent.getIsEnabled();
    if (isEnabled != null)
      _trigger.setEnabled(isEnabled.booleanValue());

    Boolean isAttachDoc = updEvent.getIsAttachDoc();
    if (isAttachDoc != null)
      _trigger.setAttachDoc(isAttachDoc.booleanValue());

    _trigger.getRecipients().clear();
    Collection recipients = updEvent.getRecipients();
    if (recipients != null)
      _trigger.getRecipients().addAll(recipients);

    return _trigger;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ServiceLookupHelper.getGridTalkAlertMgr().updateAlertTrigger(
      (AlertTrigger)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ServiceLookupHelper.getGridTalkAlertMgr().findAlertTrigger(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateAlertTriggerEvent updEvent = (UpdateAlertTriggerEvent)event;
    return new Object[]
           {
             AlertTrigger.ENTITY_NAME,
             String.valueOf(updEvent.getUID()),
           };
  }
}