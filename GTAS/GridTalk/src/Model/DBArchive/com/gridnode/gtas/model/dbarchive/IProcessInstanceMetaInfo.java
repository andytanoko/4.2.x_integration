/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcessInstanceMetaInfo.java
 *
 ****************************************************************************
 * Date           		 Author              			Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       			Created
 * Oct 6,  2005		     Sumedh Chalermkanjana		Adopt com.gridnode.gtas.server.estore.model.IProcessInstanceMetaInfo from Wei Xiang for use in UI
 * Dec 4,  2006        Regina Zeng              Added valid and invalid for remark field
 * Dec 18, 2006        Tam Wei Xiang            Added failedReason, detailReason
 */
package com.gridnode.gtas.model.dbarchive;

public interface IProcessInstanceMetaInfo
{
  /**
   * Name for DocummentMEtaInfo entity.
   */
  public static final String ENTITY_NAME = "ProcessInstanceMetaInfo";
  
  public static final String REMARK_VALID = "Valid";
  
  public static final String REMARK_INVALID = "Invalid";
  
  /**
   * The following is the fieldID for the respective field(eg Process_State, process_start_date). 
   * Their type is stated after the declaration of the Number object.
   */  
  
  public static final Number UID = new Integer(0);  //Long
    
  /**
   * FieldId for Whether-the-Activity-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  public static final Number VERSION       = new Integer(2); //Double
  
  public static final Number Process_Instance_ID = new Integer(3); //String
  
  public static final Number Process_State = new Integer(4); //String
  
  public static final Number Process_Start_Date = new Integer(5); //Date
  
  public static final Number Process_End_Date = new Integer(6); //Date
  
  public static final Number Partner_Duns = new Integer(7); //String
  
  public static final Number Process_Def = new Integer(8); //String
  
  public static final Number Role_Type = new Integer(9); //String
  
  public static final Number Partner_ID = new Integer(10); //String
  
  public static final Number Partner_Name = new Integer(11); //String
  
  public static final Number Doc_Number = new Integer(12); //String
  
  public static final Number Doc_Date_Generated = new Integer(13); //Long
  
  public static final Number ASSOC_DOCS = new Integer(15); //Collection

  public static final Number USER_TRACKING_ID = new Integer(16); //Long
  
  public static final Number REMARK = new Integer(17); //Collection
  
  public static final Number FAILED_REASON = new Integer(18); //Integer
  
  public static final Number DETAIL_REASON = new Integer(19); //String
  
  public static final Number RETRY_NUMBER = new Integer(20); //Integer
}
