/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceMetaInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
 */
public class ProcessInstanceMetaInfoBean extends AbstractEntityBean
{
	public String getEntityName()
	{
		return ProcessInstanceMetaInfo.ENTITY_NAME;
	}
}
