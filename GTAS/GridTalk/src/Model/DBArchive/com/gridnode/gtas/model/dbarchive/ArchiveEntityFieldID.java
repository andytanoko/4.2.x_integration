/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveEntityFieldID.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * Feb 10 2009      Ong Eu Soon             Created
 * Mar 30 2009     Tam Wei Xiang       #122 - Added ARchive_older_than, is_archive_frequency_once,
 *                                           client_tz
 * Apr 11 2009     Tam Wei Xiang       #122 - Remove ARCHIVE_NAME
 */
package com.gridnode.gtas.model.dbarchive;

import java.util.Hashtable;


public class ArchiveEntityFieldID
{
  private static Hashtable table;
  
  static
  {
    table = new Hashtable();
    table.put(IArchiveMetaInfo.ENTITY_NAME,
        new Number[] 
        {
          IArchiveMetaInfo.UID,
          IArchiveMetaInfo.ARCHIVE_ID,
          IArchiveMetaInfo.ARCHIVE_TYPE,
          IArchiveMetaInfo.ARCHIVE_DESCRIPTION,
          IArchiveMetaInfo.DOCUMENT_TYPE_LIST,
          IArchiveMetaInfo.ENABLE_RESTORE_ARCHIVED,
          IArchiveMetaInfo.ENABLE_SEARCH_ARCHIVED,
          IArchiveMetaInfo.FOLDER_LIST,
          IArchiveMetaInfo.FROM_START_DATE,
          IArchiveMetaInfo.FROM_START_TIME,
          IArchiveMetaInfo.INCLUDE_INCOMPLETE_PROCESSES,
          IArchiveMetaInfo.PARTNER_ID_FOR_ARCHIVE,
          IArchiveMetaInfo.TO_START_DATE,
          IArchiveMetaInfo.TO_START_TIME,
          IArchiveMetaInfo.IS_ARCHIVE_FREQUENCY_ONCE,
          IArchiveMetaInfo.ARCHIVE_OLDER_THAN,
          IArchiveMetaInfo.PROCESS_DEF_NAME_LIST
        });
  }
  
  public static Hashtable getEntityFieldID()
  {
    return table;
  }
}
