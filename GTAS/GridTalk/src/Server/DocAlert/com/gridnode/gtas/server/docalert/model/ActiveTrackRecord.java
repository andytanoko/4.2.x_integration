/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActiveTrackRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for ActiveTrackRecord entity. An ActiveTrackRecord
 * is a record of a document that is currently being tracked for response
 * document which will result in a reminder alert in few days time if no
 * response document is received.
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a ActiveTrackRecord entity instance.
 *   TrackRecordUID   - UID of the ResponseTrackRecord this record originates from.
 *   DaysToReminder   - The number of days after tracking date before the
 *                      reminder alert will be raised.
 *   AlarmUID         - UID of the alarm that will sound off when it is time
 *                      for raising the reminder alert.
 *   SentGridDocUID   - UID of the sent document (GridDocument).
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ActiveTrackRecord
  extends    AbstractEntity
  implements IActiveTrackRecord
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -913668754474696426L;
	protected Long  _trackRecordUID;
  protected int   _daysToReminder;
  protected Long  _alarmUID;
  protected Long  _sentGridDocUID;

  public ActiveTrackRecord()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return "NextReminder for document "+ getSentGridDocUID() + " is " + getDaysToReminder()
           + " days after tracking date";
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public Long getTrackRecordUID()
  {
    return _trackRecordUID;
  }

  public int getDaysToReminder()
  {
    return _daysToReminder;
  }

  public Long getAlarmUID()
  {
    return _alarmUID;
  }

  public Long getSentGridDocUID()
  {
    return _sentGridDocUID;
  }

  // *************** Setters for attributes *************************

  public void setTrackRecordUID(Long trackRecordUID)
  {
    _trackRecordUID = trackRecordUID;
  }

  public void setDaysToReminder(int daysToReminder)
  {
    _daysToReminder = daysToReminder;
  }

  public void setAlarmUID(Long alarmUID)
  {
    _alarmUID = alarmUID;
  }

  public void setSentGridDocUID(Long sentGridDocUID)
  {
    _sentGridDocUID = sentGridDocUID;
  }

}