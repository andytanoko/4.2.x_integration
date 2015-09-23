/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OneOffReportScheduleParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002    H.Sushil              Created
 */

package com.gridnode.pdip.app.reportgen.value;

import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;
/**
 * Class which serves as datastructure for One time Report Generation.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */


public class OneOffReportScheduleParam extends ReportParam implements Serializable
{
  private Date oneOffDate;

  public void setOneOffDate(Date oneOffDate)
  {
    this.oneOffDate = oneOffDate;
  }

  public Date getOneOffDate()
  {
    return oneOffDate;
  }

  public void setOneOffDateTime(Integer hours,Integer minutes)
  {
      Calendar cal = Calendar.getInstance();
      cal.setTime(oneOffDate);
      cal.set(Calendar.HOUR_OF_DAY,hours.intValue());
      cal.set(Calendar.MINUTE,minutes.intValue());
      oneOffDate = cal.getTime();
  }
}