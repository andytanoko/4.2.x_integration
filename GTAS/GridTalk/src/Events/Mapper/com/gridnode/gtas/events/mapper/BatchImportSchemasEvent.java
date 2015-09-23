/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BatchImportSchemasEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 6, 2006    i00107             Created
 */
package com.gridnode.gtas.events.mapper;

import java.util.Collection;
import java.util.HashMap;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * @author i00107
 * @since GT4.0
 * Event for performing batch import of schema set into GridTalk system.
 */
public class BatchImportSchemasEvent extends EventSupport
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7970148443542763303L;
	public static final String ZIP_FILE = "ZipFile";
	public static final String SCHEMA_MAPPINGS = "SchemaMappings";
	public static final String BASE_DIR = "BaseDir";
	
	public static final String ZIP_ENTRY_NAME = "ZipEntryName";
	public static final String MAPPING_NAME = "MappingName";
	public static final String MAPPING_DESCR = "Description";
	
	/**
	 * Construct a BatchImportSchemasEvent
	 * 
	 * @param zipFile Name of the zip file containing the schemas to import.
	 * @param baseDir The base directory to create for the imported schemas
	 * @param schemaMappings Collection of Maps representing the SchemaMapping(s) selected
	 * to be created as schema type MappingFile(s).
	 */
	public BatchImportSchemasEvent(String zipFile, String baseDir, Collection<HashMap> schemaMappings)
		throws EventException
	{
		checkSetString(ZIP_FILE, zipFile);
		setEventData(BASE_DIR, baseDir);
		setEventData(SCHEMA_MAPPINGS, schemaMappings);
	}
	
	public String getZipFile()
	{
		return (String)getEventData(ZIP_FILE);
	}
	
	public Collection<HashMap> getSchemaMappings()
	{
		return (Collection<HashMap>)getEventData(SCHEMA_MAPPINGS);
	}
	
	public String getBaseDir()
	{
		return (String)getEventData(BASE_DIR);
	}
	
	@Override
	public String getEventName()
	{
		return "java:comp/env/param/event/BatchImportSchemasEvent";
	}

	
}
