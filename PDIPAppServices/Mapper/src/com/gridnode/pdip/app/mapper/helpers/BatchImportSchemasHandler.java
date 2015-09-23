/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BatchImportSchemasHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 6, 2006    i00107              Created
 */

package com.gridnode.pdip.app.mapper.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.gridnode.pdip.app.mapper.exception.BatchImportSchemasException;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.SchemaMapping;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * @author i00107
 * @since GT 4.0
 * Handles the actual work of importing schemas.
 */
public class BatchImportSchemasHandler
{
	private String _zipFile;
	private String _baseDir;
	private List<SchemaMapping> _schemaMappings;
	
	private String _schemaPathKey;
	private String _schemaBaseSubPath;
	
	/**
	 * Construct a BatchImportSchemasHandler
	 * 
	 * @param zipFile The fullpath of the zip file containing the schema files
	 * @param baseDir Base directory to import the schema files under the root schema directory
	 * @param schemaMappings List of SchemaMapping(s) to create as schema type MappingFile(s)
	 */
	public BatchImportSchemasHandler(String zipFile, String baseDir, List<SchemaMapping> schemaMappings)
	{
		_zipFile = zipFile;
		_baseDir = baseDir;
		_schemaMappings = schemaMappings;
		
		_schemaPathKey = IMapperPathConfig.PATH_SCHEMA;
		_schemaBaseSubPath = getSchemaBaseDir();
	}

	/**
	 * During import schemas, it does two things:
	 * <ol>
	 * <li>For schema files specified to create as schema type Mapping File (i.e. in the <code>schemaMappings</code> list),
	 * the schema files will be created with unique filename at the resolved destination schema directory, and the corresponding
	 * MappingFile record will be created in the database. If, however, the record creation failed, the created schema file will also be
	 * removed from the destination schema directory.</li>
	 * <li>For other schema files which are not specified to create as schema type Mapping File, they will be created at the resolved
	 * destination schema directory, overwriting any existing file with same filename.</li>
	 * </ol>
	 *
	 */
	public void importSchemas() throws BatchImportSchemasException
	{
		ZipFile zipFileObj = null;
		try
		{
			//get ZipFile object
			zipFileObj = new ZipFile(_zipFile);
			
			//foreach schemaMapping, find the zip entry and create mapping file, move the schema file to correct location, cache the imported entry
			List<String> processedEntries = processSchemaMappings(zipFileObj);
			
			//foreach zip entry, if entry not processed above, just move the schema file to correct location
			processOtherSchemaFiles(zipFileObj, processedEntries);
		}
		catch (Exception ex)
		{
			Logger.warn("[BatchImportSchemasHandler.importSchemas] Unable to import schemas", ex);
			throw new BatchImportSchemasException("Unable to import schemas", ex);
		}
		finally
		{
			closeZipFile(zipFileObj);
		}
	}
	
	private void processOtherSchemaFiles(ZipFile zipFileObj, List<String> processedEntries)
		throws Exception
	{
		String destPathKey = _schemaPathKey;
		String destSubPath = _schemaBaseSubPath;
		Enumeration<? extends ZipEntry> entries = zipFileObj.entries();
		//foreach zip entry, if entry not processed above, just move the schema file to correct location
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if (!entry.isDirectory() && !processedEntries.contains(entry.getName()))
			{
				// but here we want to overwrite existing files
				InputStream srcIS = zipFileObj.getInputStream(entry);
				String destRelPath = destSubPath + entry.getName();
				FileUtil.replace(destPathKey, destRelPath, srcIS);
			}
		}
	}
	
	private String getSchemaBaseDir()
	{
		if (_baseDir == null || _baseDir.length() == 0)
		{
			return "";
		}
		else
		{
			return _baseDir + "/";
		}
	}
	
	private List<String> processSchemaMappings(ZipFile zipFileObj)
		throws Exception
	{
		if (_schemaMappings == null || _schemaMappings.isEmpty())
		{
			return new ArrayList<String>();
		}
		
		List<String> processedEntries = new ArrayList<String>();
		
		String destPathKey = _schemaPathKey;
		String destSubPath = _schemaBaseSubPath;
		
		//foreach schemaMapping, find the zip entry and create mapping file, move the schema file to correct location, cache the imported entry
		for (SchemaMapping mapping : _schemaMappings)
		{
			ZipEntry entry = zipFileObj.getEntry(mapping.getZipEntryName());
			if (entry.isDirectory())
			{
				Logger.warn("[BatchImportSchemasHandler.processSchemaMappings] Specified Schema Mapping is a directory entry in zip file: "+entry.getName()+", skipping it.", null);
				continue;
			}

			InputStream srcIS = zipFileObj.getInputStream(entry);
			String destRelPath = destSubPath + entry.getName();
			String destFilename = FileUtil.create(destPathKey, destRelPath, srcIS); //this may change the filename of the schema file
			
			String subPath = FileUtil.extractPath(destRelPath);
			try
			{
				createMappingFile(mapping, subPath, destFilename);
			}
			catch (Exception ex)
			{
				//since fail to create the mapping file record, will remove the created file
				FileUtil.delete(destPathKey, subPath+destFilename);
				throw ex;
			}
			processedEntries.add(entry.getName());
		}
		return processedEntries;
	}
	
	private void createMappingFile(SchemaMapping mapping, String subPath, String filename)
		throws ApplicationException, SystemException
	{
		MappingFile mappingFile = new MappingFile();
		mappingFile.setDescription(mapping.getDescription());
		mappingFile.setFilename(filename);
		mappingFile.setPath(_schemaPathKey);
		mappingFile.setName(mapping.getMappingName());
		mappingFile.setSubPath(subPath);
		mappingFile.setType(MappingFile.SCHEMA);
		
		try
		{
			mappingFile = (MappingFile)MappingFileEntityHandler.getInstance().createEntity(mappingFile);
			Logger.debug("[BatchImportSchemasHandler.createMappingFile] Created Mapping File: "+mappingFile.getEntityDescr());
		}
		catch (ApplicationException ex)
		{
			throw ex;
		}
		catch (SystemException ex)
		{
			throw ex;
		}
		catch (Throwable ex)
		{
			throw new SystemException("Unexpected error during MappingFile creation", ex);
		}
	}
	
	private void closeZipFile(ZipFile zipFileObj)
	{
		try
		{
			if (zipFileObj != null)
			{
				zipFileObj.close();
			}
		}
		catch (IOException ex)
		{
			
		}
	}
}
