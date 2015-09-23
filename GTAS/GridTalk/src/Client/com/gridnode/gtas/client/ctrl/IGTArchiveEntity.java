/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTArchiveEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-12-17     Ong Eu Soon         Created
 * 2009-03-30     Tam Wei Xiang       #122 - Added ARchive_older_than, is_archive_frequency_once,
 *                                           client_tz
 * 2009-04-11     Tam Wei Xiang       #122 - Remove ARCHIVE_NAME
 */

package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.dbarchive.IArchiveMetaInfo;

public interface IGTArchiveEntity extends IGTEntity
{
  public static final Number UID                = IArchiveMetaInfo.UID;
  public static final Number ARCHIVE_ID         = IArchiveMetaInfo.ARCHIVE_ID;
  public static final Number ARCHIVE_DESCRIPTION        = IArchiveMetaInfo.ARCHIVE_DESCRIPTION;
  public static final Number FROM_START_DATE          = IArchiveMetaInfo.FROM_START_DATE;
  public static final Number FROM_START_TIME          = IArchiveMetaInfo.FROM_START_TIME;
  public static final Number TO_START_DATE            = IArchiveMetaInfo.TO_START_DATE;
  public static final Number TO_START_TIME            =IArchiveMetaInfo.TO_START_TIME;
  public static final Number ARCHIVE_TYPE       = IArchiveMetaInfo.ARCHIVE_TYPE;

  public static final Number PROCESS_DEF_NAME_LIST        = IArchiveMetaInfo.PROCESS_DEF_NAME_LIST;
  public static final Number INCLUDE_INCOMPLETE_PROCESSES = IArchiveMetaInfo.INCLUDE_INCOMPLETE_PROCESSES;
  public static final Number PARTNER_ID_FOR_ARCHIVE            = IArchiveMetaInfo.PARTNER_ID_FOR_ARCHIVE;
  
  public static final Number FOLDER_LIST             = IArchiveMetaInfo.FOLDER_LIST;
  public static final Number DOCUMENT_TYPE_LIST           = IArchiveMetaInfo.DOCUMENT_TYPE_LIST;
  
  public static final Number ENABLE_SEARCH_ARCHIVED = IArchiveMetaInfo.ENABLE_SEARCH_ARCHIVED;
  public static final Number ENABLE_RESTORE_ARCHIVED = IArchiveMetaInfo.ENABLE_RESTORE_ARCHIVED;
  
  public static final Number ARCHIVE_OLDER_THAN = IArchiveMetaInfo.ARCHIVE_OLDER_THAN;
  public static final Number IS_ARCHIVE_FREQUENCY_ONCE = IArchiveMetaInfo.IS_ARCHIVE_FREQUENCY_ONCE;
  public static final Number CLIENT_TZ = IArchiveMetaInfo.CLIENT_TZ;
  
  
  // Constants for ARCHIVE_TYPE
  public static final String ARCHIVE_TYPE_PROCESS_INSTANCE  = "ProcessInstance";
  public static final String ARCHIVE_TYPE_DOCUMENT          = "Document";
  
  public static final String ARCHIVE_FREQUENCE_ONCE = "true";
}