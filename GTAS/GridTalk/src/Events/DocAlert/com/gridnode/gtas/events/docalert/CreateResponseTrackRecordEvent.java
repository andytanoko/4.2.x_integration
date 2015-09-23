/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateResponseTrackRecordEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Apr 30 2003    Neo Sok Lay         Add IS_ATTACH_RESPONSE_DOC field.
 */
package com.gridnode.gtas.events.docalert;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;
import java.util.Map;

/**
 * This Event class contains the data for the creation of new ResponseTrackRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I7
 */
public class CreateResponseTrackRecordEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5557388454857027264L;
	public static final String SENT_DOC_TYPE = "Sent DocType";
  public static final String SENT_DOCID_XPATH = "Sent DocId Xpath";
  public static final String START_TRACK_DATE_XPATH = "Start TrackDate Xpath";
  public static final String RESP_DOC_TYPE = "Response DocType";
  public static final String RESP_DOCID_XPATH = "Response DocId Xpath";
  public static final String RECV_RESP_ALERT = "Receive Response Alert";
  public static final String ALERT_RECPT_XPATH = "Alert Recipient Xpath";
  public static final String REMINDER_ALERTS = "ReminderAlerts";
  public static final String IS_ATTACH_RESP_DOC = "Is Attach Response Doc";

  public CreateResponseTrackRecordEvent(
    String sentDocType,
    String sentDocIdXpath,
    String startTrackDateXpath,
    String responseDocType,
    String responseDocIdXpath,
    String receiveRespAlert,
    String alertRecipientXpath,
    Collection reminderAlerts,
    Boolean isAttachRespDoc) throws EventException
  {
    checkSetString(SENT_DOC_TYPE, sentDocType);
    checkSetString(RESP_DOC_TYPE, responseDocType);
    setEventData(SENT_DOCID_XPATH, sentDocIdXpath);
    setEventData(RESP_DOCID_XPATH, responseDocIdXpath);
    setEventData(START_TRACK_DATE_XPATH, startTrackDateXpath);
    setEventData(RECV_RESP_ALERT, receiveRespAlert);
    setEventData(ALERT_RECPT_XPATH, alertRecipientXpath);
    setEventData(IS_ATTACH_RESP_DOC, isAttachRespDoc);

    if (reminderAlerts != null)
      checkSetCollection(REMINDER_ALERTS, reminderAlerts, Map.class);
  }

  public String getSentDocType()
  {
    return (String)getEventData(SENT_DOC_TYPE);
  }

  public String getSentDocIdXpath()
  {
    return (String)getEventData(SENT_DOCID_XPATH);
  }

  public String getResponseDocType()
  {
    return (String)getEventData(RESP_DOC_TYPE);
  }

  public String getResponseDocIdXpath()
  {
    return (String)getEventData(RESP_DOCID_XPATH);
  }

  public String getStartTrackDateXpath()
  {
    return (String)getEventData(START_TRACK_DATE_XPATH);
  }

  public String getReceiveResponseAlert()
  {
    return (String)getEventData(RECV_RESP_ALERT);
  }

  public String getAlertRecipientXpath()
  {
    return (String)getEventData(ALERT_RECPT_XPATH);
  }

  public Collection getReminderAlerts()
  {
    return (Collection)getEventData(REMINDER_ALERTS);
  }

  public boolean isAttachResponseDoc()
  {
    Boolean attach = (Boolean)getEventData(IS_ATTACH_RESP_DOC);
    return (attach == null) ? false : attach.booleanValue();
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateResponseTrackRecordEvent";
  }

}