/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: AuditFileEntityFieldID.java
 * 
 * ***************************************************************************
 * Date 			  	Author 		   Changes
 * ***************************************************************************
 * 16 Sep 2005 	  Sumedh      	Created
 */
package com.gridnode.gtas.model.dbarchive.docforpi;

import java.util.Hashtable;

public class AuditFileEntityFieldID
{
	private static Hashtable table;

	static
	{
		table = new Hashtable();
		table.put(IAuditFileMetaInfo.ENTITY_NAME, new Number[] {
				IAuditFileMetaInfo.FILENAME, 
        IAuditFileMetaInfo.DOC_NO,
				IAuditFileMetaInfo.DOC_TYPE, 
        IAuditFileMetaInfo.PARTNER_ID,
				IAuditFileMetaInfo.PARTNER_DUNS, 
        IAuditFileMetaInfo.PARTNER_NAME,
				IAuditFileMetaInfo.DATE_CREATED, 
        IAuditFileMetaInfo.PREAMBLE,
				IAuditFileMetaInfo.DELIVERY_HEADER, 
        IAuditFileMetaInfo.SERVICE_HEADER,
				IAuditFileMetaInfo.SERVICE_CONTENT,
				IAuditFileMetaInfo.DOC_META_INFO_UID
		});
	}

	public static Hashtable getEntityFieldID()
	{
		return table;
	}
}
