/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduledTaskAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-02-11     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.web.scheduler;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ScheduledTaskAForm extends GTActionFormBase
{
  private String _monthOption;
   
  // ScheduledTask entity fields
  private String _taskType;
  private String _taskId;
  private String _frequency;
  private String _count;
  private String _interval;
  private String _weekOfMonth;
  private String[] _daysOfWeek;
  private String _startDate;
  private String _runTime;
  private String _isDisabled;
  private String _dayOfWeek; //single selection for Monthly
  private String _reverseInterval;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _isDisabled = "false";
    _daysOfWeek = new String[]{};
  }

  public void doNetscape6Reset(ActionMapping mapping, HttpServletRequest request)
  { 
  }

  public void setMonthOption(String value)
  {
    _monthOption = value;
  }
  
  public void setTaskType(String value)
  {
    _taskType = value;
  }

  public void setTaskId(String value)
  {
    _taskId = value;
  }

  public void setFrequency(String value)
  {
    _frequency = value;
  }

  public void setCount(String value)
  {
    _count = value;
  }

  public void setStartDate(String value)
  {
    _startDate = value;
  }

  public void setRunTime(String value)
  {
    _runTime = value;
  }

  public void setDaysOfWeek(String[] values)
  {
    _daysOfWeek = values;
  }

  public void setInterval(String value)
  {
    _interval = value;
  }

  public void setWeekOfMonth(String value)
  {
    _weekOfMonth = value;
  }

  public void setIsDisabled(String value)
  {
    _isDisabled = value;
  }

  public void setDayOfWeek(String value)
  {
    _dayOfWeek = value;
  }
  
  public void setReverseInterval(String value)
  {
    _reverseInterval = value;
  }
  
  public String getMonthOption()
  {
    return _monthOption;
  }
  public String getTaskType()
  {
    return _taskType;
  }

  public String getTaskId()
  {
    return _taskId;
  }

  public String getFrequency()
  {
    return _frequency;
  }

  public String getCount()
  {
    return _count;
  }

  public String getInterval()
  {
    return _interval;
  }

  public String getWeekOfMonth()
  {
    return _weekOfMonth;
  }

  public String[] getDaysOfWeek()
  {
    return _daysOfWeek;
  }

  public String getStartDate()
  {
    return _startDate;
  }

  public String getRunTime()
  {
    return _runTime;
  }

  public String getIsDisabled()
  {
    return _isDisabled;
  }

  public String getDayOfWeek()
  {
    return _dayOfWeek;
  }
  
  public String getReverseInterval()
  {
    return _reverseInterval;
  }
}