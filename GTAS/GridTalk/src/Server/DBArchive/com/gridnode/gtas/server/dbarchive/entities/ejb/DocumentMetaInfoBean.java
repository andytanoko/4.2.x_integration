/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */


package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
/**
 * A DocumentInfoBean provide persistency service for DocumentInfo
 *
 * Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
*/
public class DocumentMetaInfoBean extends AbstractEntityBean
{
	public String getEntityName()
	{
		return DocumentMetaInfo.ENTITY_NAME;
	}
}
