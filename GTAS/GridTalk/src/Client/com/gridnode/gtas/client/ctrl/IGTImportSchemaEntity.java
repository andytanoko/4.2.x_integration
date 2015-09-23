/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTImportSchema.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTImportSchemaEntity extends IGTEntity
{
	public static Number ZIP_FILE = new Integer(-10);
	public static Number BASE_DIR = new Integer(-20);
	public static Number SCHEMA_MAPPING = new Integer(-30); //a list of schema mapping file
	public static Number IS_IMPORT_SCHEMA_MAPPING = new Integer(-40); //use to diffentiate the
	                                                               //batch import schema mapping screen
	                                                               //and the uploadZip file screen
	//public static Number MARK_AS_DELETE = new Integer(-50);
}
