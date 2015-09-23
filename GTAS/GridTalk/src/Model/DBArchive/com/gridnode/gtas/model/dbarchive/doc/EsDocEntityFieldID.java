/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsDocEntityFieldID.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 6 Oct 2005      	Sumedh Chalermkanjana   Created
 * 2 Dec 2005       Tam Wei Xiang           added in PATH_KEY and SUB_PATH_KEY
 *                                          into Number[]
 * 27 Mar 2006      Tam Wei Xiang           added ProcessInstanceID in the Number[] 
 * 17 Oct 2006      Regina Zeng             Remove column UdocFilename and AuditFilename                                        
 */
package com.gridnode.gtas.model.dbarchive.doc;

import java.util.Hashtable;

import com.gridnode.gtas.model.dbarchive.docforpi.IDocumentMetaInfo;

public class EsDocEntityFieldID
{
  private static Hashtable table;
  
  static
  {
    table = new Hashtable();
    table.put(IDocumentMetaInfo.ENTITY_NAME,
        new Number[] 
        {
          IDocumentMetaInfo.Filename,
          IDocumentMetaInfo.UID,
          IDocumentMetaInfo.Doc_Type,
          IDocumentMetaInfo.Doc_Number,
          IDocumentMetaInfo.Partner_ID,
          IDocumentMetaInfo.Date_Time_Create,
          IDocumentMetaInfo.Folder,
          IDocumentMetaInfo.SENDER_CERT,
          IDocumentMetaInfo.RECEIVER_CERT,
          IDocumentMetaInfo.Partner_Name,
          IDocumentMetaInfo.Date_Time_Send_Start,
          IDocumentMetaInfo.Date_Time_Send_End,
          IDocumentMetaInfo.PATH_KEY,
          IDocumentMetaInfo.SUB_PATH_KEY,
          IDocumentMetaInfo.Process_Instance_ID,
          IDocumentMetaInfo.Doc_Date_Generated,
          IDocumentMetaInfo.GDOC_UID,
          IDocumentMetaInfo.USER_TRACKING_ID,
          IDocumentMetaInfo.REMARK,
          IDocumentMetaInfo.ATTACHMENT_FILENAMES,
          IDocumentMetaInfo.IS_CONTAIN_ATTACHMENT,
          IDocumentMetaInfo.RECEIPT_AUDIT_FILENAME,
          IDocumentMetaInfo.DOC_TRANS_STATUS
        });
  }
  
  public static Hashtable getEntityFieldID()
  {
    return table;
  }
}
