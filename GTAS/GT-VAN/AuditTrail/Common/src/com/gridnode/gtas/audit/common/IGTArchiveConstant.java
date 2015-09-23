/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTArchiveConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 23, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.common;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IGTArchiveConstant
{
  public static final String ARCHIVE_NAME = "archiveName";
  public static final String ARCHIVE_DESCRIPTION = "archiveDesc";
  
  public static final String IS_ENABLED_ARCHIVED_SEARCHED = "enableArchivedSearch";
  public static final String IS_ENABLED_RESTORE = "enableRestore";

  public static final String ARCHIVE_ID = "archiveID";
  public static final String ARCHIVE_TYPE = "archiveType";
  public static final String ARCHIVE_TYPE_PI = "ProcessInstance";
  public static final String ARCHIVE_TYPE_DOCUMENT = "Document";
  
  public static final String ARCHIVE_OPERATION = "archiveOp";
  public static final String ARCHIVE_OP_ARCHIVE = "archive";
  public static final String ARCHIVE_OP_RESTORE = "restore";
  public static final String ARCHIVE_STATUS = "archiveStatus";
  public static final String ARCHIVE_SUMMARY_FILE = "archiveSummary";
  
  public static final String CUSTOMER_ID = "customerID";
  public static final String ARCHIVE_ORPHAN_RECORD = "archiveOrphanRecord";
  public static final String ARCHIVE_JOBS_ID = "archiveJobID";
  
  public static final String FROM_START_DATE_TIME = "fromStartDate";
  public static final String TO_START_DATE_TIME = "toStartDate";
}
