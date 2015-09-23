/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay        Created
 * Apr 30 2003    Neo Sok Lay         Add AttachResponseDoc field.
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 */
package com.gridnode.gtas.server.docalert.model;

import java.util.ArrayList;
import java.util.Collection;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;


/**
 * This is an object model for the ResponseTrackRecord entity. A
 * ResponseTrackRecord describes a configuration for tracking for response
 * document for a sent business document, e.g. PO confirmation for a sent PO Request.
 *
 * The Model:<BR><PRE>
 *   UId                 - UID for a ResponseTrackRecord entity instance.
 *   Version             - Version of this entity instance.
 *   SentDocType         - Document type of the sent document to be tracked.
 *   SentDocIdXpath      - Xpath for extracting document identifier from sent document.
 *                         If not present, UDocNum from the document will be used
 *                         as the document identifier.
 *   StartTrackDateXpath - Xpath for extracting the date to start tracking for
 *                         response document. If not present, DateTimeSendStart
 *                         from the sent document will be used as the tracking date.
 *   ResponseDocType     - Document type of the response document to track for.
 *   ResponseDocIdXpath  - Xpath for extracting document identifier from response document.
 *                         If not present, UDocNum from the document will be used
 *                         as the document identifier.
 *   ReceiveResponseAlert- Name of the Alert to raise when the response document
 *                         is received.
 *   AlertRecipientXpath - Xpath to extract the email code for the recipient
 *                         of the ReceiveResponseAlert.
 *   ReminderAlerts      - The ReminderAlert configurations for this track record.
 *   AttachResponseDoc   - Whether to attach response document when raising
 *                         receive response alert.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class ResponseTrackRecord
  extends    AbstractEntity
  implements IResponseTrackRecord
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8457447275758127000L;
	protected String _sentDocType;
  protected String _sentDocIdXpath;
  protected String _startTrackDateXpath;
  protected String _responseDocType;
  protected String _responseDocIdXpath;
  protected String _receiveResponseAlert;
  protected String _alertRecipientXpath;
  protected Collection _reminderAlerts = new ArrayList();
  protected boolean _attachRespDoc = true;

  public ResponseTrackRecord()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return new StringBuffer("Track Request doc: ").append(getSentDocType()).append(
                " for Response doc: ").append(getResponseDocType()).toString();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getSentDocType()
  {
    return _sentDocType;
  }

  public String getSentDocIdXpath()
  {
    return _sentDocIdXpath;
  }

  public String getStartTrackDateXpath()
  {
    return _startTrackDateXpath;
  }

  public String getResponseDocType()
  {
    return _responseDocType;
  }

  public String getResponseDocIdXpath()
  {
    return _responseDocIdXpath;
  }

  public String getReceiveResponseAlert()
  {
    return _receiveResponseAlert;
  }

  public String getAlertRecipientXpath()
  {
    return _alertRecipientXpath;
  }

  public Collection getReminderAlerts()
  {
    return _reminderAlerts;
  }

  public boolean isAttachResponseDoc()
  {
    return _attachRespDoc;
  }

  // *************** Setters for attributes *************************

  public void setSentDocType(String sentDocType)
  {
    _sentDocType = sentDocType;
  }

  public void setSentDocIdXpath(String docIdXpath)
  {
    _sentDocIdXpath = docIdXpath;
  }

  public void setStartTrackDateXpath(String startTrackDateXpath)
  {
    _startTrackDateXpath = startTrackDateXpath;
  }

  public void setResponseDocType(String responseDocType)
  {
    _responseDocType = responseDocType;
  }

  public void setResponseDocIdXpath(String docIdXpath)
  {
    _responseDocIdXpath = docIdXpath;
  }

  public void setReceiveResponseAlert(String alertName)
  {
    _receiveResponseAlert = alertName;
  }

  public void setAlertRecipientXpath(String alertRecipientXpath)
  {
    _alertRecipientXpath = alertRecipientXpath;
  }

  public void setReminderAlerts(Collection reminderAlerts)
  {
    _reminderAlerts = reminderAlerts;
  }

//  public void addReminderAlert(ReminderAlert reminderAlert)
//  {
//    _reminderAlerts.add(reminderAlert);
//  }

  public void setAttachResponseDoc(boolean attachRespDoc)
  {
    _attachRespDoc = attachRespDoc;
  }
}