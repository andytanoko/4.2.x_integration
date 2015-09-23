/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Schedule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 11 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.scheduler.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is an object model for Schedule entity.<P>
 *
 * The Model:<BR><PRE>
 *   TaskType     - The type of schedule (UserProcedure, CheckLicense, etc)
 *   TaskId       - Unique Id of the entity related to the schedule.
 *   Disable      - Whether the schedule has been disabled.
 *   StartDate    - Effective date of the schedule
 *   StartTime    - The run time of the schedule
 *   TimesToRun   - Restricted number of times to run the schedule
 *   DelayPeriod  - Interval between the runnings of the schedule (based on frequency)
 *   Frequency    - Frequency type for running of the schedule
 *   DaysOfWeek   - The Days in a Week to run the schedule
 *   WeekOfMonth  - The Nth week in the Month to run the schedule.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class Schedule extends AbstractEntity implements ISchedule
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5626432427519210650L;
	protected String _taskType;
  protected String _taskId;
  protected Boolean _disabled;
  protected Date _startDate;
  protected String _startTime;
  protected Integer _timesToRun;
  protected Integer _delayPeriod;
  protected Integer _frequency;
  protected List _daysOfWeek;
  protected Integer _weekOfMonth;

  public Schedule()
  {
    _daysOfWeek = new ArrayList();
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    String dateStr =
      (getStartDate() == null)
        ? null
        : new SimpleDateFormat("yyyy-MM-dd").format(getStartDate());
    return new StringBuffer()
      .append(getTaskType())
      .append('/')
      .append(getTaskId())
      .append('/')
      .append(dateStr)
      .append('/')
      .append(getStartTime())
      .toString();
  }

  public Number getKeyId()
  {
    return UID;
  }

  public List getDaysOfWeek()
  {
    return _daysOfWeek;
  }

  public Integer getDelayPeriod()
  {
    return _delayPeriod;
  }

  public Boolean getDisabled()
  {
    return _disabled;
  }

  public Integer getFrequency()
  {
    return _frequency;
  }

  public Date getStartDate()
  {
    return _startDate;
  }

  public String getStartTime()
  {
    return _startTime;
  }

  public String getTaskId()
  {
    return _taskId;
  }

  public String getTaskType()
  {
    return _taskType;
  }

  public Integer getTimesToRun()
  {
    return _timesToRun;
  }

  public Integer getWeekOfMonth()
  {
    return _weekOfMonth;
  }

  public void setDaysOfWeek(List daysOfWeek)
  {
    _daysOfWeek = daysOfWeek;
  }

  public void setDelayPeriod(Integer delayPeriod)
  {
    _delayPeriod = delayPeriod;
  }

  public void setDisabled(Boolean disabled)
  {
    _disabled = disabled;
  }

  public void setFrequency(Integer frequency)
  {
    _frequency = frequency;
  }

  public void setStartDate(Date startDate)
  {
    _startDate = startDate;
  }

  public void setStartTime(String startTime)
  {
    _startTime = startTime;
  }

  public void setTaskId(String taskId)
  {
    _taskId = taskId;
  }

  public void setTaskType(String taskType)
  {
    _taskType = taskType;
  }

  public void setTimesToRun(Integer timesToRun)
  {
    _timesToRun = timesToRun;
  }

  public void setWeekOfMonth(Integer weekOfMonth)
  {
    _weekOfMonth = weekOfMonth;
  }

}
