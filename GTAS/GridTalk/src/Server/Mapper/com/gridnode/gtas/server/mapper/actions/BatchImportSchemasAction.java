/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BatchImportSchemasAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 6, 2006    i00107             Created
 * Mar 20, 2006   Tam Wei Xiang      Modified method doSemanticValidation
 */

package com.gridnode.gtas.server.mapper.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.gridnode.gtas.events.mapper.BatchImportSchemasEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.mapper.MappingFileEntityFieldID;
import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.mapper.exception.BatchImportSchemasException;
import com.gridnode.pdip.app.mapper.model.SchemaMapping;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * @author i00107
 * @sicne GT 4.0
 * This action handles import of schema set into GridTalk.
 */
public class BatchImportSchemasAction extends AbstractGridTalkAction
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3883115909597993820L;

	private static final String ACTION_NAME = "BatchImportSchemasAction";
	
	private List<SchemaMapping> _schemaMappings;
	
	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.exceptions.TypedException)
	 */
	@Override
	protected IEventResponse constructErrorResponse(IEvent event,
																									TypedException ex)
	{
		BatchImportSchemasEvent bisEvent = (BatchImportSchemasEvent)event;
		Object[] params = {
				bisEvent.getZipFile(),
		};
		return constructEventResponse(IErrorCode.BATCH_IMPORT_SCHEMA_ERROR, params, ex);
	}

	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(com.gridnode.pdip.framework.rpf.event.IEvent)
	 */
	@Override
	protected IEventResponse doProcess(IEvent event) throws Throwable
	{
		BatchImportSchemasEvent bisEvent = (BatchImportSchemasEvent)event;
		String zipFilename = bisEvent.getZipFile();
		File zipFile = getZipFile(zipFilename);
		try
		{
			// do import
			ServiceLookupHelper.getManager().importSchemas(zipFile.getCanonicalPath(), bisEvent.getBaseDir(), _schemaMappings);
		}
		finally
		{
			//cleanup temp files
			cleanupTempFiles(zipFilename);
		}
		
		return constructEventResponse();
	}

	private File getZipFile(String zipFilename) throws BatchImportSchemasException
	{
		File zipFile = null;
		try
		{
	    zipFile = FileUtil.getFile(IPathConfig.PATH_TEMP, getUserID()+"/in/",
	                               zipFilename);
		}
		catch (FileAccessException ex)
		{
			Logger.warn("[BatchImportSchemasAction.getZipFile] Unable to access the zip file", ex);
			throw new BatchImportSchemasException("Unable to access the zip file: "+zipFilename, ex);
		}
    if ((zipFile == null) || (!zipFile.exists()))
    {
    	throw new BatchImportSchemasException("Unable to find the zip file: "+zipFilename);
    }
    return zipFile;
	}
	
	private void cleanupTempFiles(String zipFilename)
	{
    try
    {
      FileUtil.delete(IPathConfig.PATH_TEMP,  _userID+"/in/", zipFilename);
    }
    catch (Exception ex)
    {
      Logger.debug("[BatchImportSchemasAction.cleanupTempFiles] Unable to delete zip file from temp folder"+
                   zipFilename);
    }
	}
	
	@Override
	protected void doSemanticValidation(IEvent event) throws Exception
	{
		BatchImportSchemasEvent bisEvent = (BatchImportSchemasEvent)event;
		
		//validate the entries in SchemaMapping
		Collection<HashMap> schemaMappings = bisEvent.getSchemaMappings();
		//String baseDir = bisEvent.getBaseDir();
		_schemaMappings = new ArrayList<SchemaMapping>();
		SchemaMapping schemaMapping;
		for (HashMap mapping : schemaMappings)
		{
			//TWX 20 Mar 2006 : get the fieldID through method getEntityFieldIDForSchemaMapping
			schemaMapping = (SchemaMapping)SchemaMapping.convertMapToEntity(MappingFileEntityFieldID.getEntityFieldIDForSchemaMapping(), mapping);
			bisEvent.checkString(BatchImportSchemasEvent.ZIP_ENTRY_NAME, schemaMapping.getZipEntryName());
			bisEvent.checkString(BatchImportSchemasEvent.MAPPING_NAME, schemaMapping.getMappingName());
			bisEvent.checkString(BatchImportSchemasEvent.MAPPING_DESCR, schemaMapping.getDescription());
			schemaMapping.setPath(extractPath(schemaMapping.getZipEntryName()));
			schemaMapping.setFileName(extractFilename(schemaMapping.getZipEntryName()));
			
			_schemaMappings.add(schemaMapping);
		}
	}

	private String extractPath(String fullPath)
	{
		int lastIndex = fullPath.lastIndexOf(File.pathSeparatorChar);
		if (lastIndex > -1)
		{
			return fullPath.substring(0, lastIndex);
		}
		else
		{
			return "";
		}
	}
	
	private String extractFilename(String fullPath)
	{
		int lastIndex = fullPath.lastIndexOf(File.pathSeparatorChar);
		if (lastIndex > -1)
		{
			return fullPath.substring(lastIndex + 1);
		}
		else
		{
			return fullPath;
		}
	}
	
	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
	 */
	@Override
	protected String getActionName()
	{
		return ACTION_NAME;
	}

	/**
	 * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
	 */
	@Override
	protected Class getExpectedEventClass()
	{
		return BatchImportSchemasEvent.class;
	}

}
