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
 * 28 Nov 2005				 Sumedh								Adopt this com.gridnode.gtas.server.estore.model.IDocumentMetaInfo into this package.
 * Dec 02, 2005        Tam Wei Xiang       Added in SUB_PATH_KEY and PATH_KEY	
 * 17 Oct 2006         Regina Zeng         Remove field UdocFilename and AuditFilename
 *                                         Add column remark, user tracking ID                                       
 */
package com.gridnode.gtas.model.dbarchive.docforpi;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in DocumentMetaInfo entity.
 *
 * Tam Wei Xiang
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
  
  public static final Number Doc_Date_Generated = new Integer(8); //Long
  
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
  
  //public static final Number Is_Request = new Integer(20); //Boolean
  
  public static final Number File_Path = new Integer(20); //String
  
  public static final Number Zip_File_Name = new Integer(21); //String
  
  public static final Number Originator_ID = new Integer(22); //String
  
  public static final Number GDOC_UID = new Integer(24); //Long
  
  public static final Number Archive_Type = new Integer(39); //String
  
  public static final Number SENDER_CERT = new Integer(26); //String
  
  public static final Number RECEIVER_CERT = new Integer(27); //String
  
  //public static final Number UI_FILENAME = new Integer(27); //String
  
  public static final Number UDOC_FILENAME = new Integer(28); //String
  
  public static final Number RNIF_VERSION = new Integer(29); //String 
  
  //TWX
  public static final Number PATH_KEY = new Integer(30); //String
  
  public static final Number SUB_PATH_KEY = new Integer(31); //String
  
  public static final Number IS_CONTAIN_ATTACHMENT = new Integer(36); //Boolean
  
  public static final Number ATTACHMENT_FILENAMES = new Integer(37); //String
  
  public static final Number REMARK = new Integer(38); //String
  
  public static final Number RECEIPT_AUDIT_FILENAME = new Integer(39); //String
  
  public static final Number DOC_TRANS_STATUS = new Integer(40); //String
  
  public static final Number USER_TRACKING_ID = new Integer(41); //String
}
