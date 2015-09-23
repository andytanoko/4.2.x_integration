/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportParam.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002    H.Sushil              Created
 */


package com.gridnode.pdip.app.reportgen.value;

import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;


/**
 * Class which serves as generic datastructure for Report Generation.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */

public class ReportParam implements Serializable
{
  private String reportName;
  private String reportTargetPath;

  private String reportTemplate;
  private String reportDataSource;

  private String reportFormat;

  private Date startDate;
  private Date endDate;

  private Integer totalOccurence;

  public ReportParam()
  {

  }

  public void setReportName(String reportName)
  {
    this.reportName = reportName;
  }

  public String getReportName()
  {
    return reportName;
  }

  public void setReportTargetPath(String reportTargetPath)
  {
    this.reportTargetPath  = reportTargetPath;
  }

  public String getReportTargetPath()
  {
    return reportTargetPath;
  }

  public void setReportTemplate(String reportTemplate)
  {
    this.reportTemplate = reportTemplate;
  }

  public String getReportTemplate()
  {
    return reportTemplate;
  }

  public void setReportDataSource(String reportDataSource)
  {
    this.reportDataSource = reportDataSource;
  }

  public String getReportDataSource()
  {
    return reportDataSource;
  }

  public void setReportFormat(String reportFormat)
  {
    this.reportFormat = reportFormat;
  }

  public String getReportFormat()
  {
    return reportFormat;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate  =  startDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDateTime(Integer hours,Integer minutes)
  {
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      cal.set(Calendar.HOUR_OF_DAY,hours.intValue());
      cal.set(Calendar.MINUTE,minutes.intValue());
      cal.set(Calendar.SECOND,0);
      startDate = cal.getTime();
  }

  public void setEndDate(Date endDate)
  {
    this.endDate  =  endDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDateTime(Integer hours,Integer minutes)
  {
      Calendar cal = Calendar.getInstance();
      cal.setTime(endDate);
      cal.set(Calendar.HOUR_OF_DAY,hours.intValue());
      cal.set(Calendar.MINUTE,minutes.intValue());
      endDate = cal.getTime();
  }


  public void setTotalOccurence(Integer totalOccurence)
  {
    this.totalOccurence  =  totalOccurence;
  }

  public Integer getTotalOccurence()
  {
    return totalOccurence;
  }
}