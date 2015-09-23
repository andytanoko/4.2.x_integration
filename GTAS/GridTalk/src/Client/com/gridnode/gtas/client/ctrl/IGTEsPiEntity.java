/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEStoreSearchEntity.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 16 Sep 2005      Sumedh Chalermkanjana   Created
 * 16 Oct 2006      Regina Zeng             Add user tracking ID and remark
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.dbarchive.*;

/**
 * This interface is model interface for client side for UI framework.
 */
public interface IGTEsPiEntity extends IGTEntity 
{
  public static final Number UID = IProcessInstanceMetaInfo.UID;
  public static final Number PROCESS_DEF = IProcessInstanceMetaInfo.Process_Def;
  public static final Number DOCUMENT_NUMBER = IProcessInstanceMetaInfo.Doc_Number;
  public static final Number PARTNER_ID = IProcessInstanceMetaInfo.Partner_ID;
  public static final Number DOCUMENT_DATE = IProcessInstanceMetaInfo.Doc_Date_Generated;
  public static final Number PROCESS_STATE = IProcessInstanceMetaInfo.Process_State;
  public static final Number PROCESS_INSTANCE_ID = IProcessInstanceMetaInfo.Process_Instance_ID;
  public static final Number PROCESS_START_DATE = IProcessInstanceMetaInfo.Process_Start_Date;
  public static final Number PROCESS_END_DATE = IProcessInstanceMetaInfo.Process_End_Date;
  
  public static final Number ASSOC_DOCS = IProcessInstanceMetaInfo.ASSOC_DOCS;
  public static final Number PARTNER_NAME = IProcessInstanceMetaInfo.Partner_Name;
  
  public static final Number USER_TRACKING_ID = IProcessInstanceMetaInfo.USER_TRACKING_ID;
  public static final Number REMARK = IProcessInstanceMetaInfo.REMARK;
  public static final Number FAILED_REASON = IProcessInstanceMetaInfo.FAILED_REASON;
  public static final Number DETAIL_REASON = IProcessInstanceMetaInfo.DETAIL_REASON;
  public static final Number RETRY_NUMBER = IProcessInstanceMetaInfo.RETRY_NUMBER;
}
