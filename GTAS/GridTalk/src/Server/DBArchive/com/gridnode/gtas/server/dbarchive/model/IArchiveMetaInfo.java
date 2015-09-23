/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 16, 2006   Ong Eu Soon         Created    
 * Mar 30 2009     Tam Wei Xiang       #122 - Added ARchive_older_than, is_archive_frequency_once,
 *                                           client_tz 
 * Apr 16 2009     Tam Wei Xiang       #122 - Remove ARCHIVE_NAME                                         
 */
package com.gridnode.gtas.server.dbarchive.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in ArchiveMetaInfo entity.
 *
 * @author Ong Eu Soon
 * 
 * @version
 * @since
 */
public interface IArchiveMetaInfo
{
  /**
   * Name for IArchiveMetaInfo entity.
   */
  public static final String ENTITY_NAME = "ArchiveMetaInfo";
  
  /**
   * The following is the fieldID for the respective field(eg archiveID, archiveType). 
   * Their type is stated after the declaration of the Number object.
   */
  public static final Number UID = new Integer(0);  //Long
  
  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number ARCHIVE_ID  = new Integer(1); //String

//  public static final Number ARCHIVE_JOB_ID       = new Integer(2); //String
  
  public static final Number ARCHIVE_TYPE = new Integer(3);  //String
  
  public static final Number ARCHIVE_DESCRIPTION = new Integer(5); //String
  
  public static final Number FROM_START_DATE = new Integer(6); //String
  
  public static final Number TO_START_DATE = new Integer(7); //String
  
  public static final Number FROM_START_TIME = new Integer(8); //String
  
  public static final Number TO_START_TIME = new Integer(9); //String
  
  public static final Number PROCESS_DEF_NAME_LIST = new Integer(10); //String
  
  public static final Number INCLUDE_INCOMPLETE_PROCESSES = new Integer(11); //Boolean
  
  public static final Number FOLDER_LIST = new Integer(12); //String
  
  public static final Number DOCUMENT_TYPE_LIST = new Integer(13); //String
  
  public static final Number ENABLE_SEARCH_ARCHIVED = new Integer(14); //String
  
  public static final Number ENABLE_RESTORE_ARCHIVED = new Integer(15); //String
  
  public static final Number PARTNER_ID_FOR_ARCHIVE = new Integer(16); //String
  
  public static final Number CLIENT_TZ = new Integer(17); //String
  
  public static final Number ARCHIVE_OLDER_THAN = new Integer(18); //Integer
  
  public static final Number IS_ARCHIVE_FREQUENCY_ONCE = new Integer(19); //Boolean

  public static final Number VERSION = new Integer(20); //double
  
  public static final Number CAN_DELETE = new Integer(21); //boolean

}
