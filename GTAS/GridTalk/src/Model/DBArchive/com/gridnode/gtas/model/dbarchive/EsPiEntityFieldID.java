/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsPiEntityFieldID.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 16 Sep 2005      Sumedh Chalermkanjana   Created
 * 19 Oct 2006      Regina Zeng             Add user tracking ID and remark
 */
package com.gridnode.gtas.model.dbarchive;

import java.util.Hashtable;

public class EsPiEntityFieldID
{
  private static Hashtable table;
  
  static
  {
    table = new Hashtable();
    table.put(IProcessInstanceMetaInfo.ENTITY_NAME,
        new Number[] 
        {
    			IProcessInstanceMetaInfo.Process_Instance_ID,
    			IProcessInstanceMetaInfo.Doc_Number,
    			IProcessInstanceMetaInfo.Partner_ID,
    			IProcessInstanceMetaInfo.Process_State,
    			IProcessInstanceMetaInfo.Process_Start_Date,
    			IProcessInstanceMetaInfo.Process_End_Date,
    			IProcessInstanceMetaInfo.ASSOC_DOCS,
          IProcessInstanceMetaInfo.Doc_Date_Generated,
          IProcessInstanceMetaInfo.Process_Def,
          IProcessInstanceMetaInfo.Partner_Name,
          IProcessInstanceMetaInfo.UID,
          IProcessInstanceMetaInfo.USER_TRACKING_ID,
          IProcessInstanceMetaInfo.REMARK,
          IProcessInstanceMetaInfo.FAILED_REASON,
          IProcessInstanceMetaInfo.DETAIL_REASON,
          IProcessInstanceMetaInfo.RETRY_NUMBER
        });
  }
  
  public static Hashtable getEntityFieldID()
  {
    return table;
  }
}
