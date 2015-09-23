/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReminderAlert.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.docalert.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ReminderAlert entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IReminderAlert
{
  /**
   * Name of ReminderAlert entity.
   */
  public static final String  ENTITY_NAME = "ReminderAlert";

  /**
   * FieldId for the UID for a ReminderAlert entity instance. A Number.
   */
  public static final Number UID         = new Integer(0);

  /**
   * FieldId for the TrackRecordUID of the ReminderAlert. A Long.
   */
  public static final Number TRACK_RECORD_UID    = new Integer(1);

  /**
   * FieldId for the DaysToReminder of the ReminderAlert. An Integer.
   */
  public static final Number DAYS_TO_REMINDER = new Integer(2);

  /**
   * FieldId for AlertToRaise of the ReminderAlert. A String.
   */
  public static final Number ALERT_TO_RAISE  = new Integer(3); //String(80)

  /**
   * FieldId for the DocRecipientXpath of the ReminderAlert. An String.
   */
  public static final Number DOC_RECPT_XPATH = new Integer(4); //String(255)

  /**
   * FieldId for DocSenderXpath of the ReminderAlert. A String.
   */
  public static final Number DOC_SENDER_XPATH  = new Integer(5); //String(255)

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(6); //Double
}