/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateResponseTrackRecordAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 28 2003    Neo Sok Lay         Created
 * Apr 30 2003    Neo Sok Lay         Add IsAttachResponseDoc field.
 */
package com.gridnode.gtas.server.docalert.actions;

import java.util.Map;

import com.gridnode.gtas.events.docalert.CreateResponseTrackRecordEvent;
import com.gridnode.gtas.server.docalert.helpers.ActionHelper;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the creation of a new ResponseTrackRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I7
 */
public class CreateResponseTrackRecordAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5635745856478996156L;
	public static final String ACTION_NAME = "CreateResponseTrackRecordAction";


  protected Class getExpectedEventClass()
  {
    return CreateResponseTrackRecordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.findResponseTrackRecord(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateResponseTrackRecordEvent createEvent = (CreateResponseTrackRecordEvent)event;

    ResponseTrackRecord newRecord = new ResponseTrackRecord();

    newRecord.setAlertRecipientXpath(createEvent.getAlertRecipientXpath());
    newRecord.setReceiveResponseAlert(createEvent.getReceiveResponseAlert());
    newRecord.setReminderAlerts(createEvent.getReminderAlerts());
    newRecord.setResponseDocIdXpath(createEvent.getResponseDocIdXpath());
    newRecord.setResponseDocType(createEvent.getResponseDocType());
    newRecord.setSentDocIdXpath(createEvent.getSentDocIdXpath());
    newRecord.setSentDocType(createEvent.getSentDocType());
    newRecord.setStartTrackDateXpath(createEvent.getStartTrackDateXpath());
    newRecord.setAttachResponseDoc(createEvent.isAttachResponseDoc());

    return newRecord;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateResponseTrackRecordEvent createEvent = (CreateResponseTrackRecordEvent)event;
    return new Object[]
           {
             ResponseTrackRecord.ENTITY_NAME,
             createEvent.getSentDocType(),
             createEvent.getResponseDocType(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.createResponseTrackRecord((ResponseTrackRecord)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertResponseTrackRecordToMap((ResponseTrackRecord)entity);
  }
}