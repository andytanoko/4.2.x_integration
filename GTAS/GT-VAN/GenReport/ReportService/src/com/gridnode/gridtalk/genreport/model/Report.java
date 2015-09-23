/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Report.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00118              Created
 * Mar 08, 2007   i00118			  Changed File _reportContent
 * 									  to Byte[] _reportContent
 * Mar 12, 2007   i00118			  Add query "getReport"
 * Mar 22, 2007   i00118			  Created a new constructor without emailList 
 * 									  as parameter and changed emailList as not-null false
 */

package com.gridnode.gridtalk.genreport.model;


import java.io.Serializable;
import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * @author i00118
 * @hibernate.class table = "`rpt_report`" optimistic-lock = "version"
 * @hibernate.mapping schema = "GTVAN"
 * @hibernate.query name = "updateReport"
 *   query = "update Report set status=:status,reportContent=:report_content,reportLocation=:report_location where UID = :uid"
 * @hibernate.query name = "deleteReport"
 *   query = "from Report where (createdDateTime + archiveDuration) - SYSDATE <0"
 * @hibernate.query name = "getReport"
 *   query = "from Report where UID=:uid"
 * @hibernate.query name = "updateArchiveDuration"
 *   query = "update Report set archiveDuration=:archive_duration"
 */
public class Report extends AbstractPersistable implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 2054975893518231724L;
  private String _reportType;
  private String _frequency;
  private String _status;
  private String _customerList;
  private Date _startDateTime;
  private Date _endDateTime;
  private String _reportFormat;
  private byte[] _reportContent;
  private int _archiveDuration;
  private String _emailList;
  private String _scheduleDocId;
  private String _reportLocation;
  private String _group;
  private Date _lastRunDateTime;
  private String _username;  
  private Date _createdDateTime;
  
  /**
   * This constructor is default for hibernate.   
   */
  public Report()
  {
    
  }
  
  /**
   * This constructor is for generating report now - View Report Online only. 
   * @param reportType
   * @param frequency
   * @param startDateTime
   * @param endDateTime
   * @param group
   * @param reportFormat
   * @param status
   * @param customerList
   * @param username
   * @param createdDateTime
   */
  public Report(String reportType, String frequency, Date startDateTime, 
          Date endDateTime, String group, String reportFormat, 
          String status, String customerList, String username, Date createdDateTime) 
  {
	_reportType = reportType;
	_frequency = frequency;
	_startDateTime = startDateTime;
	_endDateTime = endDateTime;
	_group = group;
	_reportFormat = reportFormat;
	_status = status;
	_customerList = customerList;
	_username = username;
	_createdDateTime = createdDateTime;
  }
  
  /**
   * This constructor is for generating report now.
   * @param reportType
   * @param frequency
   * @param startDateTime
   * @param endDateTime
   * @param group
   * @param reportFormat
   * @param emailList
   * @param status
   * @param customerList
   * @param username
   * @param createdDateTime
   */
  public Report(String reportType, String frequency, Date startDateTime, 
                Date endDateTime, String group, String reportFormat, 
                String emailList, String status, String customerList, String username, Date createdDateTime) 
  {
    _reportType = reportType;
    _frequency = frequency;
    _startDateTime = startDateTime;
    _endDateTime = endDateTime;
    _group = group;
    _reportFormat = reportFormat;
    _emailList = emailList;
    _status = status;
    _customerList = customerList;
    _username = username;
    _createdDateTime = createdDateTime;
  }
  
  /**
   * This constructor is for generating schedule report.
   * @param reportType
   * @param frequency
   * @param status
   * @param customerList
   * @param startDateTime
   * @param endDateTime
   * @param reportFormat
   * @param reportContent
   * @param emailList
   * @param scheduleDocId
   * @param reportLocation
   * @param group
   * @param lastRunDateTime
   * @param username
   * @param createdDateTime
   */
  public Report(String reportType, String frequency, String status, String customerList,
                Date startDateTime, Date endDateTime, String reportFormat, byte[] reportContent,
                String emailList, String scheduleDocId, String reportLocation, String group,
                Date lastRunDateTime, String username, Date createdDateTime)
  {
    _reportType = reportType;
    _frequency = frequency;
    _status = status;
    _customerList = customerList;
    _startDateTime = startDateTime;
    _endDateTime = endDateTime;
    _reportFormat = reportFormat;
    _reportContent = reportContent;
    _emailList = emailList;
    _scheduleDocId = scheduleDocId;
    _reportLocation = reportLocation ;
    _group = group;
    _lastRunDateTime = lastRunDateTime;
    _username = username;
    _createdDateTime = createdDateTime;
  }

  /**
   * The default value for archive duration is 30 days.
   * @return the value of archive duration
   * @hibernate.property column="`archive_duration`" not-null="true" insert="false"
   */
  public int getArchiveDuration() 
  {
    return _archiveDuration;
  }

  public void setArchiveDuration(int archiveDuration) 
  {
	this._archiveDuration = archiveDuration;
  }

  /**
   * @hibernate.property column="`customer_list`" 
   * @return
   */
  public String getCustomerList() 
  {
	return _customerList;
  }

  public void setCustomerList(String customerList) 
  {
	this._customerList = customerList;
  }

  /**
   * @hibernate.property column="`email_list`"
   * @return
   */
  public String getEmailList() 
  {
	return _emailList;
  }

  public void setEmailList(String emailList) 
  {
	this._emailList = emailList;
  }

  /**
   * @hibernate.property column="`end_date_time`" not-null="true"
   * @return
   */
  public Date getEndDateTime() 
  {
	return _endDateTime;
  }
  
  public void setEndDateTime(Date endDateTime) 
  {
	this._endDateTime = endDateTime;
  }
  
  /**
   * @hibernate.property column="`frequency`" not-null="true"
   * @return
   */
  public String getFrequency() 
  {
	return _frequency;
  }
  
  public void setFrequency(String frequency) 
  {
	this._frequency = frequency;
  }
  
  /**
   * @hibernate.property column="`group_name`" not-null="true"
   * @return
   */
  public String getGroup() 
  {
	return _group;
  }
  
  public void setGroup(String group) 
  {
	this._group = group;
  }
  
  /**
   * @hibernate.property column="`last_run_date_time`" 
   * @return
   */
  public Date getLastRunDateTime() 
  {
	return _lastRunDateTime;
  }
  
  public void setLastRunDateTime(Date lastRunDateTime) 
  {
	this._lastRunDateTime = lastRunDateTime;
  }
  
  /**
   * @hibernate.property column="`report_content`" type = "binary"
   * @return
   */
  public byte[] getReportContent() 
  {
	return _reportContent;
  }
  
  public void setReportContent(byte[] reportContent) 
  {
	this._reportContent = reportContent;
  }
  
  /**
   * @hibernate.property column="`report_format`" not-null="true"
   * @return
   */
  public String getReportFormat() 
  {
	return _reportFormat;
  }
  
  public void setReportFormat(String reportFormat) 
  {
	this._reportFormat = reportFormat;
  }
  
  /**
   * @hibernate.property column="`report_location`" 
   * @return
   */
  public String getReportLocation() 
  {
	return _reportLocation;
  }
  
  public void setReportLocation(String reportLocation) 
  {
	this._reportLocation = reportLocation;
  }
  
  /**
   * @hibernate.property column="`report_type`" not-null="true"
   * @return
   */
  public String getReportType() 
  {
	return _reportType;
  }
  
  public void setReportType(String reportType) 
  {
	this._reportType = reportType;
  }
  
  /**
   * @hibernate.property column="`schedule_doc_id`"
   * @return
   */
  public String getScheduleDocId() 
  {
	return _scheduleDocId;
  }
  
  public void setScheduleDocId(String scheduleDocId) 
  {
	this._scheduleDocId = scheduleDocId;
  }
  
  /**
   * @hibernate.property column="`start_date_time`" not-null="true"
   * @return
   */
  public Date getStartDateTime() 
  {
	return _startDateTime;
  }
  
  public void setStartDateTime(Date startDateTime) 
  {
	this._startDateTime = startDateTime;
  }
  
  /**
   * @hibernate.property column="`status`" not-null="true"
   * @return
   */
  public String getStatus() 
  {
	return _status;
  }
  
  public void setStatus(String status) 
  {
	this._status = status;
  }

  /**
   * @hibernate.property column="`username`" not-null="true"
   * @return
   */
  public String getUsername()
  {
    return _username;
  }

  public void setUsername(String _username)
  {
    this._username = _username;
  }
  
  /*
   * @hibernate.id generator-class = "assigned" column = "uid" name="uid"
   * @hibernate.version column = "version" unsaved-value = "null"
   */
  public String getReportNo()
  {
    return getUID();
  }

  /**
   * @hibernate.property column="`created_date_time`"
   * @return
   */
  public Date getCreatedDateTime()
  {
    return _createdDateTime;
  }

  public void setCreatedDateTime(Date dateTime)
  {
    _createdDateTime = dateTime;
  }
}