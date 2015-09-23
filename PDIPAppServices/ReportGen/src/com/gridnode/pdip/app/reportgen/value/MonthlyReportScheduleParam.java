/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MonthlyReportScheduleParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002    H.Sushil              Created
 */

package com.gridnode.pdip.app.reportgen.value;


import java.util.Date;
import java.io.Serializable;


/**
 * Class which serves as datastructure for Monthly Report Generation.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */


public class MonthlyReportScheduleParam extends ReportParam implements Serializable
{
  private Integer dayOfMonth;
  private Integer monthInterval;

  public void setDayOfMonth(Integer dayOfMonth)
  {
    this.dayOfMonth  =  dayOfMonth;
  }

  public Integer getDayOfMonth()
  {
    return dayOfMonth;
  }

  public void setMonthInterval(Integer monthInterval)
  {
    this.monthInterval  =  monthInterval;
  }

  public Integer getMonthInterval()
  {
    return monthInterval;
  }

}