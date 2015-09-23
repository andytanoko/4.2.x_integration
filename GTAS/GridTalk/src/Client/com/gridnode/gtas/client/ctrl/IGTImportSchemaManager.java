/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTImportSchemaManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.HashMap;

import com.gridnode.gtas.client.GTClientException;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTImportSchemaManager extends IGTManager
{
	/**
	 * Get the pre-processed zip entry from backend.
	 * @param importSchema
	 * @return
	 * @throws GTClientException
	 */
	public HashMap<String, ArrayList<String> > getSchemaZipEntry(IGTImportSchemaEntity importSchema)
           throws GTClientException; 
}
