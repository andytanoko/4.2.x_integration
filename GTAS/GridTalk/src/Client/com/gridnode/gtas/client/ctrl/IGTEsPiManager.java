/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEsPiManager.java
 *
 ****************************************************************************
 * Date                Author                   Changes
 ****************************************************************************
 *  6 Oct 2005	      Sumedh Chalermkanjana     Created
 * 28 Nov 2006        Regina Zeng               Added PI_ENTITY to assist in EsDocDetailRenderer
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.dbarchive.*;
import java.util.TimeZone;

public interface IGTEsPiManager extends IGTManager
{
	//10 Nov 2005 TWX: added userSelectTZ
	public IGTListPager getEsPiListPager(EsPiSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending, 
			TimeZone userSelectTZ) 
		throws GTClientException;
}
