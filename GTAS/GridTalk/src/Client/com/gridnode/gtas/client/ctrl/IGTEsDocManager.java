/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEsDocManager.java
 *
 ****************************************************************************
 * Date             Author                    Changes
 ****************************************************************************
 *  6 Oct 2005	    Sumedh Chalermkanjana     Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.dbarchive.EsDocSearchQuery;

import java.util.TimeZone;

public interface IGTEsDocManager extends IGTManager
{
	public IGTListPager getEsDocListPager(EsDocSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending,TimeZone userSelectTZ) 
		throws GTClientException;
	
	public IGTListPager getAssocEsDocListPager(Long uid, Number[] sortField, boolean[] sortAscending) 
	throws GTClientException;
}
