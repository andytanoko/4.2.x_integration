/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IISATProperty.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 6, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.helpers;

/**
 * Contain all the ISAT property category, key
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IISATProperty
{
  //Configuration Property
  public static final String CATEGORY = "ISAT";
  public static final String RESPONSE_URL = "reprocess.response.url";
  public static final String ARCHIVE_STATUS_RESPONSE_URL = "archive.status.forward.url";
  
  //TEMP SOLution only
  public static final String SUMMARY_FILE_URL = "archive.show.summary.url";
  
  public static final String ARCHIVE_FOLDER = "audit.archive.folder";
  public static final String TOTAL_PROCESS_TRANS_IN_ARCHIVE_FILE = "audit.archive.total.process.in.zip";
  public static final String TOTAL_ORPHAN_TRACE_EVENT_INFO_IN_ZIPFILE = "audit.archive.total.traceinfo.in.zip";
  public static final String EMAIL_ALERT_DATE_PATTERN = "audit.email.alert.date.pattern";
  public static final String ARCHIVE_SCHEDULER_DATE_PATTERN = "audit.scheduler.date.pattern";
  public static final String ARCHIVE_SCHEDULER_TIME_INTERVAL = "audit.scheduler.time.interval"; //pls keep this value same as the active time interval
                                                                                                //of the AuditSchedulerServiceMbean.
  public static final String ARCHIVE_SCHEDULER_DESCRIPTION = "archive.scheduler.description"; 
  public static final String ARCHIVE_SCHEDULER_NAME_PREFIX = "archive.scheduler.name.prefix";
  
  //Archive JMS Category name
  public static final String ARCHIVE_JMS_CATEGORY = "isat.jms.archive";
  
  public static final String REPROCESS_JMS_CATEGORY = "isat.jms.reprocess";
  
  public static final String GT_ARCHIVE_JMS_CATEGORY = "isat.jms.gt.archive";
  
  public static final String LOCAL_EVENT_Q_JMS_CATEGORY = "isat.jms.local.event";
}
