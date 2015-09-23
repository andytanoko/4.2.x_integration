/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEsDocEntity.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 6 Oct 2005       Sumedh Chalermkanjana   Created
 * 2 Dec 2005       Tam Wei Xiang           Added in attr SUB_PATH_KEY and PATH_KEY
 * 28 Mar 2006      Tam Wei Xiang           Added new column process instance ID to UI.
 * 17 Oct 2006      Regina Zeng             Remove column UdocFilename and AuditFilename
 *                                          Add column gdoc ID, remark, doc date, user tracking ID
 * 13 Dec 2006      Tam Wei Xiang           Commented ownCert, tpCert                                         
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.dbarchive.docforpi.IDocumentMetaInfo;

/**
 * This interface is model interface for client side for UI framework.
 */
public interface IGTEsDocEntity extends IGTEntity
{
	 public static final Number UID = IDocumentMetaInfo.UID;
	
	 public static final Number DOCUMENT_TYPE = IDocumentMetaInfo.Doc_Type;
	 public static final Number DOCUMENT_NUMBER = IDocumentMetaInfo.Doc_Number;
	 public static final Number PARTNER_ID = IDocumentMetaInfo.Partner_ID;
	 public static final Number DOC_DATE_CREATED = IDocumentMetaInfo.Date_Time_Create;
	 public static final Number DOC_DATE_SENT = IDocumentMetaInfo.Date_Time_Send_Start;
	 public static final Number DOC_DATE_RECEIVED = IDocumentMetaInfo.Date_Time_Send_End;
	 public static final Number FOLDER = IDocumentMetaInfo.Folder;
	 
	 /* This field I consulted wx. */
	 //public static final Number UDOC_FILENAME = IDocumentMetaInfo.UI_FILENAME;
	 //public static final Number FILENAME = IDocumentMetaInfo.Filename;
	 
   //public static final Number OWN_CERTIFICATE = IDocumentMetaInfo.OWN_CERT;
	 //public static final Number TP_CERTIFICATE = IDocumentMetaInfo.TP_CERT;
	 public static final Number PARTNER_NAME = IDocumentMetaInfo.Partner_Name;
	 	 	 
	 public static final Number PATH_KEY = IDocumentMetaInfo.PATH_KEY;
	 public static final Number SUB_PATH_KEY = IDocumentMetaInfo.SUB_PATH_KEY;
	 
	 public static final Number PROCESS_INSTANCE_ID = IDocumentMetaInfo.Process_Instance_ID;
   
   public static final Number GDOC_ID = IDocumentMetaInfo.GDOC_UID;
   public static final Number DOC_DATE = IDocumentMetaInfo.Doc_Date_Generated;
   public static final Number REMARK = IDocumentMetaInfo.REMARK;
   public static final Number USER_TRACKING_ID = IDocumentMetaInfo.USER_TRACKING_ID;   
}
