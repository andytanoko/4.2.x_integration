/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResponseTrackRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Apr 30 2003    Neo Sok Lay         Add IS_ATTACH_RESPONSE_DOC field.
 */
package com.gridnode.gtas.model.docalert;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ResponseTrackRecord entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I7
 */
public interface IResponseTrackRecord
{
  /**
   * Name for ResponseTrackRecord entity.
   */
  public static final String ENTITY_NAME = "ResponseTrackRecord";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID         = new Integer(0);  //Integer

  /**
   * FieldId for SentDocType. A String.
   */
  public static final Number SENT_DOC_TYPE    = new Integer(2);  //String(12)

  /**
   * FieldId for SentDocIdXpath. A String.
   */
  public static final Number SENT_DOCID_XPATH  = new Integer(3);  //String(255)

  /**
   * FieldId for StartTrackDateXpath. A String.
   */
  public static final Number START_TRACK_DATE_XPATH = new Integer(4);  //String(255)

  /**
   * FieldId for ResponseDocType. A String.
   */
  public static final Number RESPONSE_DOC_TYPE    = new Integer(5);  //String(12)

  /**
   * FieldId for ResponseDocIdXpath. A String.
   */
  public static final Number RESPONSE_DOCID_XPATH  = new Integer(6);  //String(255)

  /**
   * FieldId for ReceiveResponseAlert. A String.
   */
  public static final Number RECEIVE_RESPONSE_ALERT  = new Integer(7);  //String(80)

  /**
   * FieldId for AlertRecipientXpath. A String.
   */
  public static final Number ALERT_RECPT_XPATH  = new Integer(8);  //String(255)

  /**
   * FieldId for ReminderAlerts. A Collection of ReminderAlert.
   */
  public static final Number REMINDER_ALERTS    = new Integer(9);

  /**
   * FieldId for IsAttachRespDoc. A Boolean.
   */
  public static final Number IS_ATTACH_RESPONSE_DOC = new Integer(10);

}