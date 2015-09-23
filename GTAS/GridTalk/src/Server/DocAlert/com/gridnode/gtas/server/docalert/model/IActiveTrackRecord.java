/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActiveTrackRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ActiveTrackRecord entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IActiveTrackRecord
{
  /**
   * Name for ActiveTrackRecord entity.
   */
  public static final String ENTITY_NAME = "ActiveTrackRecord";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID          = new Integer(0);  //Integer

  /**
   * FieldId for TrackRecordUID. A Long.
   */
  public static final Number TRACK_RECORD_UID    = new Integer(1);

  /**
   * FieldId for DaysToReminder. An Integer.
   */
  public static final Number DAYS_TO_REMINDER  = new Integer(2);

  /**
   * FieldId for AlarmUID. A Long.
   */
  public static final Number ALARM_UID = new Integer(3);

  /**
   * FieldId for SentGridDocUID. A Long.
   */
  public static final Number SENT_GRIDDOC_UID = new Integer(4);

}