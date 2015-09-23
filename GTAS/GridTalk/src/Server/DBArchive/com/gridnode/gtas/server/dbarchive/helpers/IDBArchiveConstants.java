/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDBArchiveConstants.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Sep 15, 2004 			Mahesh             	Created
 * Mar 31, 2006       Tam Wei Xiang       New constant for archive startup and 
 *                                        complete alert and archive failed
 * Aug 31, 2006       Tam Wei Xiang       Merge from ESTORE stream.
 * Feb 26, 2007       Tam Wei Xiang       Add common constant for TM n GT archive  
 * Jun 06, 2007       Tam Wei Xiang       Add common constant archive job id and orphanRecord                                 
 */
package com.gridnode.gtas.server.dbarchive.helpers;

public interface IDBArchiveConstants
{
  //public static final String ACTION_NAME="ACTION_NAME";
  
  //public static final String ARCHIVE_ACTION="ARCHIVE_ACTION";
  //public static final String RESTORE_ACTION="RESTORE_ACTION";
  
  //public static final String ARCHIVE_ALERT_NAME = "ARCHIVE_ALERT";
  public static final String RESTORE_ALERT_NAME = "RESTORE_ALERT";
  public static final String ARCHIVE_START_ALERT = "ARCHIVE_START_ALERT";
  public static final String ARCHIVE_COMPLETE_ALERT = "ARCHIVE_COMPLETE_ALERT";
  public static final String ARCHIVE_FAILED_ALERT = "ARCHIVE_FAIL_ALERT";
  
  public static final String ARCHIVE_PROCESSINSTANCE = "ProcessInstance";
  public static final String ARCHIVE_GRIDDOCUMENT = "Document";
  
  //public static final String ARCHIVE_NAME = "ARCHIVE_NAME";
  //public static final String ARCHIVE_DESC = "ARCHIVE_DESC"; //Description
  
  //public static final String PATH_KEY = "PathKey";
  
  //Constant share by both GT n TM plug-in
  public static final String ARCHIVE_OPERATION = "archiveOp";
  public static final String ARCHIVE_TYPE = "archiveType";
  public static final String ARCHIVE_ID = "archiveID";
  public static final String ARCHIVE_OP_ARCHIVE = "archive";
  public static final String ARCHIVE_OP_RESTORE = "restore";
  public static final String ARCHIVE_STATUS = "archiveStatus";
  public static final String ARCHIVE_SUMMARY_FILE = "archiveSummary";
  public static final String ARCHIVE_JOBS_ID = "jobsID";
  public static final String ARCHIVE_OPRHAN_RECORD = "archiveOrphanRecord";
  public static final String ARCHIVE_FROM_START_DATE = "archiveFromStartDate";
  public static final String ARCHIVE_TO_START_DATE = "archiveToStartDate";
}
