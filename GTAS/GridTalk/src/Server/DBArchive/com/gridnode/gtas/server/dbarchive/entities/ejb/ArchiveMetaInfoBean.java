/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveMetaInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18, 2008   Ong Eu Soon         Created
 */


package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

public class ArchiveMetaInfoBean extends AbstractEntityBean
{
	public String getEntityName()
	{
		return ArchiveMetaInfo.ENTITY_NAME;
	}
}
