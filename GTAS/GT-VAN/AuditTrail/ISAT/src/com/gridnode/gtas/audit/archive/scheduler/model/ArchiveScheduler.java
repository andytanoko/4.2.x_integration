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
 * Mar 12, 2007   Tam Wei Xiang       Created
 * Jun 01, 2007   Tam Wei Xiang       Support archive by customer
 */

package com.gridnode.gtas.audit.archive.scheduler.model;

import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * @hibernate.class table = "`isat_archive_scheduler`"
 * @hibernate.query
 *   query = "FROM ArchiveScheduler a WHERE a.nextRunTime > :startDate AND a.nextRunTime <= :endDate AND a.active = :isActive"
 *   name = "getArchiveScheduler"
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveScheduler extends AbstractPersistable
{
	private Date _effectiveStartDate; // once == effective start date time	
	private Date _lastRunTime;
	private Date _nextRunTime;
	private String _frequency;
	private boolean _isActive = true;
	private int _archiveRecordOlderThanNth;
  private String _customerList;
  private int _archiveEveryNth;
  
  private boolean _isSuccessInvoke = true; //indicate the status of invocation of the archive schedule. 
  private Date _archiveStartDateRange;
  private Date _archiveEndDateRange;
  private String _timezoneID; //We will convert the archive start date, end date to the given timezone
  private boolean _isArchiveOrphanRecord = false;
  
	public ArchiveScheduler() {}
	
	public ArchiveScheduler(Date effectiveStartDate, Date lastRunTime, Date nextRunTime, 
			             String frequency, boolean isActive, int archiveOnNthFrequencyBefore, 
                   String customerList, int archiveEveryNth, boolean isSuccessInvoke, Date archiveStartDateRange,
                   Date archiveEndDateRange, String timezoneCode, boolean isArchiveOrphanRecord)
	{
		setEffectiveStartDate(effectiveStartDate);
		setLastRunTime(lastRunTime);
		setNextRunTime(nextRunTime);
		setFrequency(frequency);
		setActive(isActive);
    setArchiveRecordOlderThanNth(archiveOnNthFrequencyBefore);
    setCustomerList(customerList);
    setArchiveEveryNth(archiveEveryNth);
    setSuccessInvoke(isSuccessInvoke);
    setArchiveStartDateRange(archiveStartDateRange);
    setArchiveEndDateRange(archiveEndDateRange);
    setTimezoneID(timezoneCode);
    setArchiveOrphanRecord(isArchiveOrphanRecord);
	}
	
	/**
   * @hibernate.property name = "effectiveStartDate" column = "`effective_start_time`"
	 */
	public Date getEffectiveStartDate() {
		return _effectiveStartDate;
	}
  
	public void setEffectiveStartDate(Date dateTime) {
		_effectiveStartDate = dateTime;
	}

  /**
   * @hibernate.property name = "frequency" column = "`frequency`"
   * @return
   */
	public String getFrequency() {
		return _frequency;
	}
	public void setFrequency(String _frequency) {
		this._frequency = _frequency;
	}
  
  /**
   * @hibernate.property name = "active" column = "`is_active`"
   * @return
   */
	public Boolean isActive() {
		return _isActive;
	}
	public void setActive(Boolean active) {
		_isActive = active;
	}
  
  /**
   * @hibernate.property name = "lastRunTime" column = "`last_run_time`"
   * @return
   */
	public Date getLastRunTime() {
		return _lastRunTime;
	}
	public void setLastRunTime(Date runTime) {
		_lastRunTime = runTime;
	}
  
  /**
   * @hibernate.property name = "nextRunTime" column = "`next_run_time`" type = "timestamp"
   * @return
   */
	public Date getNextRunTime() {
		return _nextRunTime;
	}
	public void setNextRunTime(Date runTime) {
		_nextRunTime = runTime;
	}
	
  /**
   * @hibernate.property name = "archiveRecordOlderThanNth" column = "`archive_record_older`"
   */
  public int getArchiveRecordOlderThanNth()
  {
    return _archiveRecordOlderThanNth;
  }

  public void setArchiveRecordOlderThanNth(int archiveRecordOlderThanNth)
  {
    _archiveRecordOlderThanNth = archiveRecordOlderThanNth;
  }
  
  /**
   * @hibernate.property name = "customerList" column = "`customer_list`"
   * @return
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
   * @hibernate.property name = "archiveEveryNth" column = "`archive_every`"
   * @return
   */
  public int getArchiveEveryNth()
  {
    return _archiveEveryNth;
  }

  public void setArchiveEveryNth(int everyNth)
  {
    _archiveEveryNth = everyNth;
  }
  
  /**
   * @hibernate.property column = "`archive_end_date`"
   * @return
   */
  public Date getArchiveEndDateRange()
  {
    return _archiveEndDateRange;
  }

  public void setArchiveEndDateRange(Date endDateRange)
  {
    _archiveEndDateRange = endDateRange;
  }

  /**
   * @hibernate.property column = "`archive_start_date`"
   * @return
   */
  public Date getArchiveStartDateRange()
  {
    return _archiveStartDateRange;
  }

  public void setArchiveStartDateRange(Date startDateRange)
  {
    _archiveStartDateRange = startDateRange;
  }
  
  /**
   * @hibernate.property column = "`is_success_invoke`"
   * @return
   */
  public boolean isSuccessInvoke()
  {
    return _isSuccessInvoke;
  }

  public void setSuccessInvoke(boolean successInvoke)
  {
    _isSuccessInvoke = successInvoke;
  }
  
  /**
   * @hibernate.property column = "`timezone_id`"
   * @return
   */
  public String getTimezoneID()
  {
    return _timezoneID;
  }

  public void setTimezoneID(String code)
  {
    _timezoneID = code;
  }
  
  /**
   * @hibernate.property column= "`is_archive_orphan_record`"
   * @return
   */
  public boolean isArchiveOrphanRecord()
  {
    return _isArchiveOrphanRecord;
  }

  public void setArchiveOrphanRecord(boolean archiveOrphanRecord)
  {
    _isArchiveOrphanRecord = archiveOrphanRecord;
  }

  public String toString()
  {
    return "ArchiveScheduler [startDateTime: "+getEffectiveStartDate()+" lastRunTime "+getLastRunTime()+" nextRunTime: "+getNextRunTime()+
           " frequency: "+getFrequency()+" isActive "+isActive()+
           " archiveOnNthFrequencyBefore "+getArchiveRecordOlderThanNth()+" customerList: "+getCustomerList()+
           " archiveEveryNth "+getArchiveEveryNth()+" archiveStartDateRange "+getArchiveStartDateRange()+
           " archiveEndDateRange "+getArchiveEndDateRange()+" isSuccessInvoke "+isSuccessInvoke()+
           " timezoneID "+getTimezoneID()+" isArchiveOrphanRecord "+isArchiveOrphanRecord()+"]";
  }
	
}
