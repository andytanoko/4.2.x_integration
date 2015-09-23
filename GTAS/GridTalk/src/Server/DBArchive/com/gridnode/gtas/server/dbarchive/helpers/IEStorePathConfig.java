/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEStorePathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 8, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public interface IEStorePathConfig extends IPathConfig
{
	//EStore path
	public static final String ESTORE_DATA_DIRECTORY = "dbarchive.path.estore";
	
	public static final String ESTORE_TEMP = "dbarchive.path.estore.temp";

}
