/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTResponseTrackRecordEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 * 2003-05-21     Andrew Hill         Added isAttachResponseDoc field
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.docalert.IResponseTrackRecord;
 
public interface IGTResponseTrackRecordEntity extends IGTEntity
{
  //Fields
  public static final Number UID                    = IResponseTrackRecord.UID;
  public static final Number SENT_DOC_TYPE          = IResponseTrackRecord.SENT_DOC_TYPE;
  public static final Number SENT_DOC_ID_XPATH      = IResponseTrackRecord.SENT_DOCID_XPATH;
  public static final Number START_TRACK_DATE_XPATH = IResponseTrackRecord.START_TRACK_DATE_XPATH;
  public static final Number RESPONSE_DOC_TYPE      = IResponseTrackRecord.RESPONSE_DOC_TYPE;
  public static final Number RESPONSE_DOC_ID_XPATH  = IResponseTrackRecord.RESPONSE_DOCID_XPATH;
  public static final Number RECEIVE_RESPONSE_ALERT = IResponseTrackRecord.RECEIVE_RESPONSE_ALERT;
  public static final Number ALERT_RECIPIENT_XPATH  = IResponseTrackRecord.ALERT_RECPT_XPATH;
  public static final Number REMINDER_ALERTS        = IResponseTrackRecord.REMINDER_ALERTS;
  public static final Number IS_ATTACH_RESPONSE_DOC = IResponseTrackRecord.IS_ATTACH_RESPONSE_DOC; //20030521AH
}
