/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSchemaZipFileEntryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.mapper.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.gridnode.gtas.events.mapper.GetSchemaZipFileEntryEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.helpers.SchemaZipFileEntryHandler;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * An action to handle the extraction of the zip entry from the schemaZipFile that user
 * uploaded into GT.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class GetSchemaZipFileEntryAction extends AbstractGridTalkAction
{
	
	private static final String ACTION_NAME = "GetSchemaZipFileEntryAction";

	@Override
	protected IEventResponse constructErrorResponse(IEvent event,
																									TypedException ex)
	{
		GetSchemaZipFileEntryEvent zipEntryEvent = (GetSchemaZipFileEntryEvent)event;
		Object[] params = {zipEntryEvent.getZipFilename()};
		return constructEventResponse(IErrorCode.GET_SCHEMA_ZIP_FILE_ENTRY_ERROR, params, ex);
	}

	@Override
	protected IEventResponse doProcess(IEvent event) throws Throwable
	{
		GetSchemaZipFileEntryEvent zipEntryEvent = (GetSchemaZipFileEntryEvent)event;
		String zipFilename = zipEntryEvent.getZipFilename();
		
		File zipFile = getSchemaZipFile(zipFilename);
		Logger.log("[GetSchemaZipFileEntryAction] zip file is "+zipFile.getAbsolutePath());
		
		HashMap<String, ArrayList<String> > zipEntryMap = SchemaZipFileEntryHandler.getInstance().getZipFileEntry(zipFile);
		
		return constructEventResponse(zipEntryMap);
	}
	
	private File getSchemaZipFile(String zipFilename)
		throws ApplicationException
	{
		File zipFile = null;
		try
		{
			zipFile = FileUtil.getFile(IPathConfig.PATH_TEMP, getUserID()+"/in/", zipFilename);
		}
		catch(FileAccessException ex)
		{
			Logger.warn("[GetSchemaZipFileEntryAction.getSchemaZipFile] Error occured while accessing the file "+zipFilename);
			throw new ApplicationException("File access to "+zipFilename+" has failed", ex);
		}
		
		if(zipFile == null)
		{
			Logger.warn("[GetSchemaZipFileEntryAction.getSchemaZipFile] Zip file not found ! "+zipFilename);
			throw new ApplicationException("Zip file "+zipFilename+" not found !");
		}
		
		return zipFile;
	}

	@Override
	protected String getActionName()
	{
		return ACTION_NAME;
	}

	@Override
	protected Class getExpectedEventClass()
	{
		return GetSchemaZipFileEntryEvent.class;
	}

}
