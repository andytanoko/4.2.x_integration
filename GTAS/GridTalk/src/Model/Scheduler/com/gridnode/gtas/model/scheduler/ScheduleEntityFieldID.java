/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduleEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 11 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.scheduler;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Scheduler module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class ScheduleEntityFieldID
{
  private Hashtable _table;
  private static ScheduleEntityFieldID _self = null;

  private ScheduleEntityFieldID()
  {
    _table = new Hashtable();

    //Schedule
    _table.put(ISchedule.ENTITY_NAME,
      new Number[]
      {
        ISchedule.DAYS_OF_WEEK,
        ISchedule.DELAY_PERIOD,
        ISchedule.DISABLED,
        ISchedule.FREQUENCY,
        ISchedule.START_DATE,
        ISchedule.START_TIME,
        ISchedule.TASK_ID,
        ISchedule.TASK_TYPE,
        ISchedule.TIMES_TO_RUN,
        ISchedule.UID,
        ISchedule.WEEK_OF_MONTH
      });

  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ScheduleEntityFieldID();
    }
    return _self._table;
  }
}
