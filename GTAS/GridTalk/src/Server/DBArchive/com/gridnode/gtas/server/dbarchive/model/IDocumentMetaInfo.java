/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 * Jan 19, 2006        Tam Wei Xiang       Change the variable GDOC_UID to GDOC_ID
 * Apr 26, 2006        Tam Wei Xiang       Added new field 'PROCESS_INSTANCE_UID'
 *                                         (= estore process instance meta info uid).
 * Oct 17, 2006        Regina Zeng         Remove field UdocFilename and AuditFilename                                         
 */
package com.gridnode.gtas.server.dbarchive.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in DocumentMetaInfo entity.
 *
 * @author Tam Wei Xiang
 * @author Regina Zeng
 * 
 * @version
 * @since
 */
public interface IDocumentMetaInfo
{
  /**
   * Name for DocummentMEtaInfo entity.
   */
  public static final String ENTITY_NAME = "DocumentMetaInfo";
  
  /**
   * The following is the fieldID for the respective field(eg docType, docNumber). 
   * Their type is stated after the declaration of the Number object.
   */
  public static final Number UID = new Integer(0);  //Long
  
  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  public static final Number VERSION       = new Integer(2); //Double
  
  public static final Number Doc_Type = new Integer(3);  //String
  
  public static final Number Doc_Number = new Integer(4); //String
  
  public static final Number Partner_Duns = new Integer(5); //String
  
  public static final Number Partner_ID = new Integer(6); //String
  
  public static final Number Partner_Name = new Integer(7); //String
  
  public static final Number Doc_Date_Generated = new Integer(8); //String
  
  public static final Number Date_Time_Send_Start = new Integer(9); //Date
  
  public static final Number Date_Time_Send_End = new Integer(10); //Date
  
  public static final Number Date_Time_Receive_Start = new Integer(11); //Date
  
  public static final Number Date_Time_Receive_End = new Integer(12); //Date
  
  public static final Number Date_Time_Create = new Integer(13); //Date
  
  public static final Number Date_Time_Import = new Integer(14); //Date
  
  public static final Number Date_Time_Export = new Integer(15); //Date
  
  public static final Number Process_Def = new Integer(16); //String
  
  public static final Number Process_Instance_ID = new Integer(17); //String
  
  public static final Number Filename = new Integer(18); //String
  
  public static final Number Folder = new Integer(19); //String
  
  public static final Number Is_Request = new Integer(20); //Boolean
  
  public static final Number File_Path = new Integer(21); //String
  
  public static final Number Zip_File_Name = new Integer(22); //String
  
  public static final Number Originator_ID = new Integer(23); //String
  
  public static final Number GDOC_ID = new Integer(24); //Long
  
  public static final Number IS_ARCHIVED_BY_PI = new Integer(25); //String
  
  public static final Number SENDER_CERT = new Integer(26); //String
  
  public static final Number RECEIVER_CERT = new Integer(27); //String
  
  public static final Number UI_FILENAME = new Integer(28); //String
  
  public static final Number UDOC_FILENAME = new Integer(29); //String
  
  public static final Number RNIF_VERSION = new Integer(30); //String 
  
  public static final Number PATH_KEY = new Integer(31); //String
  
  public static final Number SUB_PATH_KEY = new Integer(32); //String
  
  public static final Number PROCESS_INSTANCE_INFO_UID = new Integer(33); //Long
  
  public static final Number GDOC_FILENAME = new Integer(34); //String
  
  public static final Number IS_ORIGINAL_DOC = new Integer(35); //Boolean
  
  public static final Number IS_CONTAIN_ATTACHMENT = new Integer(36); //Boolean
  
  public static final Number ATTACHMENT_FILENAMES = new Integer(37); //String
  
  public static final Number REMARK = new Integer(38); //String
  
  public static final Number RECEIPT_AUDIT_FILENAME = new Integer(39); //String
  
  public static final Number DOC_TRANS_STATUS = new Integer(40); //String
  
  public static final Number USER_TRACKING_ID = new Integer(41); //String
}
