/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveSchedulerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler.facade.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.gridnode.gtas.audit.archive.scheduler.exception.ArchiveScheduleException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IArchiveSchedulerObj extends EJBObject
{
  
/**
   * Add the Archive Scheduler. The next runtime, archive start/end date range will be determined also.
   * @param frequency
   * @param effectiveStartDateTime
   * @param archiveRecordOlderThan
   * @param archiveEveryNth
   * @param customer
   * @param isEnabled
   * @throws ArchiveScheduleException
   */
  public void addArchiveSchedule(Hashtable archiveScheduleData) throws ArchiveScheduleException, RemoteException;
  
  /**
   * Load all the outstanding archive schedule task given the specified range.
   * @param range
   */
  public void loadOutstandingTask(Date startDateRange, Date endDateRange) throws RemoteException;
  
  /**
   * Update the archive Invoke status for the ArchiveScheduler identified by the archiveSchedulerUID
   * @param archiveInvokeSuccess the status for invoking the archival task  
   * @param archiveSchedulerUID the UID of the archive scheduler
   */
  public void updateArchiveSchedulerStatus(boolean archiveInvokeSuccess, String archiveSchedulerUID) throws ArchiveScheduleException, RemoteException;
  
  /**
   * Invoke the archive scheduler directly without waiting to its next runtime. The archive date range 
   * criteria will be based on the date value of archiveStartDate and archiveEndDate as determine by the
   * nextRuntime. 
   * This allow the user to force the archive start immediately.
   * 
   * This will be useful if the scheduler fail to invoke the archive task (fail to deliver the request to JMS)
   * @param archiveUID
   * @throws ArchiveScheduleException
   */
  public void runArchiveSchedulerNow(String archiveUID) throws ArchiveScheduleException, RemoteException;
  
  /**
   * Update the ArchiveScheduler record as identified by the schedulerUID
   * @param schedulerUID the PK of the ArchiveScheduler
   * @param archiveScheduleData the data need to be updated to ArchiveScheduler
   * @throws ArchiveScheduleException if we have problem updating the ArchiveScheduler record
   */
  public void updateArchiveScheduler(String schedulerUID, Hashtable archiveScheduleData) throws ArchiveScheduleException, RemoteException; 
  
  /**
   * Delete the ArchiveScheduler record given the archiveSchedulerUID
   * @param archiveSchedulerUID the PK of the ArchiveScheduler record
   * @throws ArchiveScheduleException throw if such a record is not exist in DB
   */
  public void deleteArchiveSchedulerRecord(String archiveSchedulerUID) throws ArchiveScheduleException, RemoteException;
  
  /**
   * Get the response url
   * @return
   * @throws RemoteException
   */
  public String getArchiveSchedulerResponseURL() throws RemoteException;
  
  /**
   * Get the Archive Scheduler date pattern. It define the date pattern passed from UI.
   * @return
   * @throws RemoteException
   */
  public String getArchiveSchedulerDatePattern() throws RemoteException;
}
