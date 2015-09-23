/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultResponseTrackRecordManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 * 2003-05-21     Andrew Hill         Support for the isAttachResponseDoc field
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.docalert.CreateResponseTrackRecordEvent;
import com.gridnode.gtas.events.docalert.DeleteResponseTrackRecordEvent;
import com.gridnode.gtas.events.docalert.GetResponseTrackRecordEvent;
import com.gridnode.gtas.events.docalert.GetResponseTrackRecordListEvent;
import com.gridnode.gtas.events.docalert.UpdateResponseTrackRecordEvent;
import com.gridnode.gtas.model.docalert.IReminderAlert;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultResponseTrackRecordManager extends DefaultAbstractManager
  implements IGTResponseTrackRecordManager
{
  DefaultResponseTrackRecordManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_RESPONSE_TRACK_RECORD, session);
  }

  private List extractReminderAlertList(IGTResponseTrackRecordEntity responseTrackRecord)
    throws GTClientException
  {
    try
    {
      List reminderAlertList = (List)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.REMINDER_ALERTS);
      if(reminderAlertList != null)
      {
        List alertList = new Vector(reminderAlertList.size());
        Iterator i = reminderAlertList.iterator();
        while(i.hasNext())
        {
          IGTReminderAlertEntity reminderAlert = (IGTReminderAlertEntity)i.next();
          if(reminderAlert == null) throw new java.lang.NullPointerException("Null ReminderAlert entity found in reminderAlerts of " + responseTrackRecord);

          HashMap reminderAlerts = new HashMap();
          reminderAlerts.put(IReminderAlert.DAYS_TO_REMINDER,  (Integer)reminderAlert.getFieldValue(IGTReminderAlertEntity.DAYS_TO_REMINDER));
          reminderAlerts.put(IReminderAlert.ALERT_TO_RAISE,    reminderAlert.getFieldString(IGTReminderAlertEntity.ALERT_TO_RAISE));
          reminderAlerts.put(IReminderAlert.DOC_RECPT_XPATH,   reminderAlert.getFieldString(IGTReminderAlertEntity.DOC_RECIPIENT_XPATH));
          reminderAlerts.put(IReminderAlert.DOC_SENDER_XPATH,  reminderAlert.getFieldString(IGTReminderAlertEntity.DOC_SENDER_XPATH));
          reminderAlerts.put(IReminderAlert.UID,  (Long)reminderAlert.getFieldValue(IGTReminderAlertEntity.UID));
          alertList.add(reminderAlerts);
        }
        return alertList;
      }
      else
      {
        return new Vector(0);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting reminderAlerts from " + responseTrackRecord, t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)entity;

      Long uid = (Long)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.UID);
      String sentDocType = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_TYPE);
      String sentDocIdXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH);
      String startTrackDateXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH);
      String responseDocType = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE);
      String responseDocIdXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH);
      String receiveResponseAlert = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT);
      String alertRecipientXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH);
      Collection reminderAlerts = (Collection)extractReminderAlertList(responseTrackRecord);
      Boolean isAttachResponseDoc = (Boolean)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC); //20030521AH

      UpdateResponseTrackRecordEvent event = new UpdateResponseTrackRecordEvent(uid,
                                                                                sentDocType,
                                                                                sentDocIdXpath,
                                                                                startTrackDateXpath,
                                                                                responseDocType,
                                                                                responseDocIdXpath,
                                                                                receiveResponseAlert,
                                                                                alertRecipientXpath,
                                                                                reminderAlerts,
                                                                                isAttachResponseDoc); //20030521AH

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTResponseTrackRecordEntity responseTrackRecord = (IGTResponseTrackRecordEntity)entity;

      String sentDocType = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_TYPE);
      String sentDocIdXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.SENT_DOC_ID_XPATH);
      String startTrackDateXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH);
      String responseDocType = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE);
      String responseDocIdXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RESPONSE_DOC_ID_XPATH);
      String receiveResponseAlert = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.RECEIVE_RESPONSE_ALERT);
      String alertRecipientXpath = responseTrackRecord.getFieldString(IGTResponseTrackRecordEntity.ALERT_RECIPIENT_XPATH);
      Collection reminderAlerts = (Collection)extractReminderAlertList(responseTrackRecord);
      Boolean isAttachResponseDoc = (Boolean)responseTrackRecord.getFieldValue(IGTResponseTrackRecordEntity.IS_ATTACH_RESPONSE_DOC); //20030521AH

      CreateResponseTrackRecordEvent event = new CreateResponseTrackRecordEvent(sentDocType,
                                                                                sentDocIdXpath,
                                                                                startTrackDateXpath,
                                                                                responseDocType,
                                                                                responseDocIdXpath,
                                                                                receiveResponseAlert,
                                                                                alertRecipientXpath,
                                                                                reminderAlerts,
                                                                                isAttachResponseDoc); //20030521AH

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_RESPONSE_TRACK_RECORD;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_RESPONSE_TRACK_RECORD;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetResponseTrackRecordEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetResponseTrackRecordListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteResponseTrackRecordEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_RESPONSE_TRACK_RECORD.equals(entityType))
    {
      return new DefaultResponseTrackRecordEntity();
    }
    else if(IGTEntity.ENTITY_REMINDER_ALERT.equals(entityType))
    {
      return new DefaultReminderAlertEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public IGTReminderAlertEntity newReminderAlert() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_REMINDER_ALERT);
    entity.setNewEntity(true);
    return (IGTReminderAlertEntity)entity;
  }
}