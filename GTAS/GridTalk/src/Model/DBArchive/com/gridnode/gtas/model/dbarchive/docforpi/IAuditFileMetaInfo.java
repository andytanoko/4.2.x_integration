/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditFileMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 12, 2005        Tam Wei Xiang       Created
 * 30 Nov 2005				 Sumedh							 Adopt this file from com.gridnode.gtas.server.estore.model.IAuditFileMetaInfo
 */
package com.gridnode.gtas.model.dbarchive.docforpi;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in AuditFileInfo entity.
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since GT 2.4.7
 */
public interface IAuditFileMetaInfo
{
	/**
   * Name for AuditFileInfo entity.
   */
  public static final String ENTITY_NAME = "AuditFileInfo";

  public static final Number UID = new Integer(0);  //Long
  
  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  public static final Number VERSION       = new Integer(2); //Double
  
  /**
   * The following is the fieldID for the respective field(eg docType, docNo). 
   * Their type is stated after the declaration of the Number object.
   */
  public static final Number FILENAME = new Integer(3); //String
  
  public static final Number DOC_NO = new Integer(4); //String
  
  public static final Number DOC_TYPE = new Integer(5); //String
  
  public static final Number PARTNER_ID = new Integer(6); //String
  
  public static final Number PARTNER_DUNS = new Integer(7); //String
  
  public static final Number PARTNER_NAME = new Integer(8); //String
  
  public static final Number DATE_CREATED = new Integer(9); //Date
  
  public static final Number PREAMBLE = new Integer(10); //String
  
  public static final Number DELIVERY_HEADER = new Integer(11); //String
  
  public static final Number SERVICE_HEADER = new Integer(12); //String
  
  public static final Number SERVICE_CONTENT = new Integer(13); //String
  
  //public static final Number CERTIFICATE = new Integer(14); //String
  
  public static final Number DOC_META_INFO_UID = new Integer(15); //Long
}