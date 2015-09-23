/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Schedule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00118              Created
 * Mar 08, 2007   i00118			  Changed File _reportContent
 * 									  to Byte[] _reportContent
 */

package com.gridnode.gridtalk.genreport.model;

import java.util.Date;
import java.io.Serializable;

import com.gridnode.util.db.AbstractPersistable;

/**
 * @author i00118
 * @hibernate.class table = "`rpt_schedule`" optimistic-lock = "version"
 * @hibernate.mapping schema = "GTVAN"
 * @hibernate.query name = "modifySchedule"
 *   query = "update Schedule set reportFormat=:format, customerList=:cust, emailList=:email, username=:name, groupName=:groupName, modifiedDateTime=:date where scheduleId=:id"  
 * @hibernate.query name = "updateSchedule"
 *   query = "update Schedule set nextRunDateTime=:next_run_date_time, nextStartDateTime=:next_start_date_time, nextEndDateTime=:next_end_date_time where UID=:uid"
 * @hibernate.query name = "getSchedule"
 *   query = "from Schedule where scheduleId=:id"
 * @hibernate.query name = "getScheduleReport"
 *   query ="from Schedule where nextRunDateTime>=:startRangeCall and nextRunDateTime<=:endRangeCall"
 */
public class Schedule extends AbstractPersistable implements Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 8989982648130601220L;
  private int _scheduleId;
  private Date _effectiveStartDate;
  private String _emailList;
  private String _customerList;
  private String _reportLocation;
  private String _groupName;
  private String _frequency;
  private String _runTime;
  private Date _nextRunDateTime;
  private byte[] _reportContent;
  private Date _nextStartDateTime;
  private Date _nextEndDateTime;
  private String _reportFormat;
  private String _reportType;
  private String _username;
  private Date _createdDateTime;
  private Date _modifiedDateTime;
  
  public Schedule()
  {
    
  }
	
  public Schedule(String reportType, String frequency, Date effectiveStartDate, String runTime, 
                  String reportFormat, String groupName, String emailList, String customerList, String username, Date createdDateTime)
  {
	_reportType = reportType;
    _frequency = frequency;
    _effectiveStartDate = effectiveStartDate;
    _runTime = runTime;
    _reportFormat = reportFormat;
    _groupName = groupName;
    _emailList = emailList;
    _customerList = customerList;
    _username = username;
    _createdDateTime = createdDateTime;
  }

  /**
   * @hibernate.property  column = "`customer_list`" 
   */
  public String getCustomerList() 
  {
    return _customerList;
  }
  
  public void setCustomerList(String list) 
  {
	_customerList = list;
  }
  
  /**
   * @hibernate.property column="`effective_start_date`" not-null="true"
   * @return
   */
  public Date getEffectiveStartDate() 
  {
	return _effectiveStartDate;
  }
  
  public void setEffectiveStartDate(Date startDate)
  {
	_effectiveStartDate = startDate;
  }
  
  /**
   * @hibernate.property column="`email_list`" 
   * @return
   */
  public String getEmailList() 
  {
	return _emailList;
  }
  
  public void setEmailList(String list) 
  {
	_emailList = list;
  }
  
  /**
   * @hibernate.property column="`frequency`" not-null="true"
   * @return
   */
  public String getFrequency() 
  {
	return _frequency;
  }
  
  public void setFrequency(String _frequency) 
  {
	this._frequency = _frequency;
  }
  
  /**
   * @hibernate.property column="`group_name`" not-null="true"
   * @return
   */
  public String getGroupName() 
  {
	return _groupName;
  }
  
  public void setGroupName(String _group) 
  {
	this._groupName = _group;
  }
  
  /**
   * @hibernate.property column="`next_run_date_time`"
   * @return
   */
  public Date getNextRunDateTime() 
  {
	return _nextRunDateTime;
  }
  
  public void setNextRunDateTime(Date runDateTime) 
  {
	_nextRunDateTime = runDateTime;
  }
  
  /**
   * @hibernate.property column="`report_location`"
   * @return
   */
  public String getReportLocation() 
  {
	return _reportLocation;
  }
  
  public void setReportLocation(String location) 
  {
	_reportLocation = location;
  }
  
  /**
   * @hibernate.property column="`run_time`" not-null="true"
   * @return
   */
  public String getRunTime() 
  {
	return _runTime;
  }
  
  public void setRunTime(String time) 
  {
	_runTime = time;
  }
  
  /**
   * @hibernate.property column="`schedule_id`" 
   * @hibernate.generator-param value = "schedule_schedule_id_seq" name ="sequence" 
   * @return
   */
  public int getScheduleId() 
  {
	return _scheduleId;
  }
  
  public void setScheduleId(int id) 
  {
	_scheduleId = id;
  }

  /**
   * @hibernate.property column="`report_content`" type = "binary"
   * @return
   */
  public byte[] getReportContent()
  {
    return _reportContent;
  }

  public void setReportContent(byte[] content)
  {
    _reportContent = content;
  }

  /**
   * @hibernate.property column="`next_end_date_time`"
   * @return
   */
  public Date getNextEndDateTime()
  {
    return _nextEndDateTime;
  }

  public void setNextEndDateTime(Date endDateTime)
  {
    _nextEndDateTime = endDateTime;
  }

  /**
   * @hibernate.property column="`next_start_date_time`"
   * @return
   */
  public Date getNextStartDateTime()
  {
    return _nextStartDateTime;
  }

  public void setNextStartDateTime(Date startDateTime)
  {
    _nextStartDateTime = startDateTime;
  }

  /**
   * @hibernate.property column="`report_format`" not-null="true"
   * @return
   */
  public String getReportFormat()
  {
    return _reportFormat;
  }

  public void setReportFormat(String format)
  {
    _reportFormat = format;
  }

  /**
   * @hibernate.property column="`report_type`" not-null="true"
   * @return
   */
  public String getReportType()
  {
    return _reportType;
  }

  public void setReportType(String type)
  {
    _reportType = type;
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

  /**
   * @hibernate.property column="`modified_date_time`"
   * @return
   */
  public Date getModifiedDateTime()
  {
    return _modifiedDateTime;
  }

  public void setModifiedDateTime(Date dateTime)
  {
    _modifiedDateTime = dateTime;
  }
}
