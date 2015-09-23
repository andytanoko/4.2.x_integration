/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSchemaZipFileEntryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * An event for getting schema zip file entries
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class GetSchemaZipFileEntryEvent extends EventSupport
{
	public static final String ZIP_FILE_NAME = "ZipFilename";
	
	public GetSchemaZipFileEntryEvent(String zipFilename)
		throws EventException
	{
		checkSetString(ZIP_FILE_NAME, zipFilename);
	}
	
	public String getZipFilename()
	{
		return (String)getEventData(ZIP_FILE_NAME);
	}
	
	public String getEventName()
	{
		return "java:comp/env/param/event/GetSchemaZipFileEntryEvent";
	}
}
