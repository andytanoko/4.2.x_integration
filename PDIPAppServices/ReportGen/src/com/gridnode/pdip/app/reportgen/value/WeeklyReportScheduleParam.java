/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WeeklyReportScheduleParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002    H.Sushil              Created
 */

package com.gridnode.pdip.app.reportgen.value;

import java.util.Date;
import java.io.Serializable;


/**
 * Class which serves as datastructure for Weekly Report Generation.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */

public class WeeklyReportScheduleParam extends ReportParam implements Serializable
{
  private Integer dayOfWeek;
  private Integer weekInterval;

  public void setDayOfWeek(Integer dayOfWeek)
  {
    this.dayOfWeek  =  dayOfWeek;
  }

  public Integer getDayOfWeek()
  {
    return dayOfWeek;
  }

  public void setWeekInterval(Integer weekInterval)
  {
    this.weekInterval  =  weekInterval;
  }

  public Integer getWeekInterval()
  {
    return weekInterval;
  }

}