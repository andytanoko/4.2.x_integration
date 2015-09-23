/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateResponseTrackRecordAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 28 2003    Neo Sok Lay         Created
 * Apr 30 2003    Neo Sok Lay         Add IsAttachResponseDoc field.
 */
package com.gridnode.gtas.server.docalert.actions;

import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.events.docalert.UpdateResponseTrackRecordEvent;
import com.gridnode.gtas.server.docalert.helpers.ActionHelper;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a ResponseTrackRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class UpdateResponseTrackRecordAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7697866241349957727L;
	private ResponseTrackRecord _record;
  private Collection _currAlerts;

  public static final String ACTION_NAME = "UpdateResponseTrackRecordAction";

  protected Class getExpectedEventClass()
  {
    return UpdateResponseTrackRecordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertResponseTrackRecordToMap((ResponseTrackRecord)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateResponseTrackRecordEvent updEvent = (UpdateResponseTrackRecordEvent)event;
    _record = ActionHelper.findResponseTrackRecord(updEvent.getUID());
    _currAlerts = _record.getReminderAlerts();
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateResponseTrackRecordEvent updEvent = (UpdateResponseTrackRecordEvent)event;

    _record.setAlertRecipientXpath(updEvent.getAlertRecipientXpath());
    _record.setReceiveResponseAlert(updEvent.getReceiveResponseAlert());
    _record.setReminderAlerts(updEvent.getReminderAlerts());
    _record.setResponseDocIdXpath(updEvent.getResponseDocIdXpath());
    _record.setResponseDocType(updEvent.getResponseDocType());
    _record.setSentDocIdXpath(updEvent.getSentDocIdXpath());
    _record.setSentDocType(updEvent.getSentDocType());
    _record.setStartTrackDateXpath(updEvent.getStartTrackDateXpath());
    _record.setAttachResponseDoc(updEvent.isAttachResponseDoc());

    return _record;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.updateResponseTrackRecord(
      (ResponseTrackRecord)entity,
      _currAlerts);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.findResponseTrackRecord(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateResponseTrackRecordEvent updEvent = (UpdateResponseTrackRecordEvent)event;
    return new Object[]
           {
             ResponseTrackRecord.ENTITY_NAME,
             String.valueOf(updEvent.getUID()),
           };
  }

}