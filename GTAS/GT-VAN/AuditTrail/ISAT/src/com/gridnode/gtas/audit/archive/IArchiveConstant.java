/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IArchiveConstant
{
  public static final String CRITERIA_FROM_START_DATE_TIME = "fromStartDateTime";
  public static final String CRITERIA_TO_START_DATE_TIME = "toStartDateTime";
  public static final String ARCHIVE_ORPHAN_RECORD = "archiveOrphanRecord";
  
  //category in zip file
  public static final String CATEGORY_PROCESS_TRANS = "processTrans";
  public static final String CATEGORY_DOC_TRANS = "docTrans";
  public static final String CATEGORY_TRACE_EVENT_INFO = "eventInfo";
  public static final String CATEGORY_TRACE_EVENT_HEADER = "eventInfoHeader";
  public static final String CATEGORY_BIZ_DOCUMENT = "businessDoc";
  public static final String CATEGORY_ORPHAN_RECORD = "orphanRecord";
  
  //archive folder
  public static final String ARCHIVE_FILENAME_PREFIX = "TMArchive";
  
  //for TM ARCHIVE HTTP GET
  public static final String ARCHIVE_NAME = "archiveName";
  public static final String ARCHIVE_DESCRIPTION = "description";
  public static final String FROM_START_DATE = "fromStartDate"; //format yyyy-MM-dd
  public static final String TO_START_DATE = "toStartDate";
  public static final String FROM_START_TIME = "fromStartTime"; //format hh:mm
  public static final String TO_START_TIME = "toStartTime";
  public static final String USER_TIMEZONE = "timezone";
  public static final String GROUP_INFO = "customer";
  
  
  //for TM ARCHIVE redirect
  public static final String ARCHIVE_ACT_STATUS = "status";
  public static final String ARCHIVE_ACT = "archive_act";
  
  //for TM download summary file
  public static final String SUMMARY_FILENAME = "summaryFilename";
  
  //for listing TM summary file
  public static final String TOTAL_FILE = "totalFile";
  
  public static final String ARCHIVE_ACTIVITY = "archiveAct";
  public static final String ARCHIVE_ACT_ARCHIVE = "archive";
  public static final String ARCHIVE_ACT_RESTORE = "restore";
  public static final String ARCHIVE_ACT_LIST_SUMMARY = "listSummaryFile";
  public static final String ARCHIVE_ACT_DOWNLOAD_SUMMARY = "downloadSummaryFile";
  public static final String ARCHIVE_ACT_ORPHAN = "orphan";
  
  public static final String RESTORE_SUMMARY_FILE = "resSummaryFile";
  
  //Archive Email Alert
  public static final String ISAT_ARCHIVE_STARTED = "isat.archive.started";
  public static final String ISAT_ARCHIVE_COMPLETED = "isat.archive.completed";
  public static final String ISAT_ARCHIVE_FAILED = "isat.archive.failed";
  public static final String ISAT_RESTORE_COMPLETED = "isat.restore.completed";
  public static final String ISAT_RESTORE_FAILED = "isat.restore.failed";
  public static final String ISAT_GT_TM_ARCHIVE_STARTED = "isat.gttm.archive.started";
  public static final String ISAT_GT_TM_ARCHIVE_COMPLETED = "isat.gttm.archive.completed";
  public static final String ISAT_GT_TM_ARCHIVE_FAILED = "isat.gttm.archive.failed";
  
  //Archive service (distribute task to multiple node)
  public static final String ARCHIVE_ID = "archiveID";
  public static final String ARCHIVE_JOBS_ID = "jobsID";
  public static final String ARCHIVE_STATUS_FAILED = "failed";
  public static final String ARCHIVE_SUMMARY_LOCK = "archiveSummLock";
  public static final String ARCHIVE_BY_DOCUMENT = "archiveByDoc";
  public static final String ARCHIVE_BY_PI = "archiveByPI";
  public static final String ARCHIVE_BY_TM = "archiveTMStatus";
  public static final String ARCHIVE_TYPE_TM = "TM";
  
  //JNDI Lookup
  public static final String JNDI_LOOKUP = "isat.jndi.lookup"; 
  public static final String CONTEXT_FACTORY = "ContextFactory";
  public static final String PROVIDER_URL = "ProviderUrl";
  public static final String NAMING = "Naming";
  
  //Singleton Archive
  public static final String SINGLETON_ARCHIVE = "isat.archive.singleton";
  public static final String SINGLETON_ARCHIVE_MBEAN = "mbean.name";
  public static final String RMI_ADAPTOR = "rmi.adaptor";
  
}
