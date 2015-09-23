/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMappingFileAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 10, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ImportSchemaAForm extends GTActionFormBase
{
	private String _baseDir;
	private String[] _schemaMapping;
	
	private static final String ZIP_FILE = "zipFile";
	private FormFileElement[] _zipFile = null;
	
	private String _markAsDelete;
	
	//setter and getter to support the zipFile field
	public void setZipFileDelete(String[] deletions)
	{
		for(String file : deletions)
		{
			removeFileFromField(ZIP_FILE, file);
		}
	}
	
	public void setZipFileUpload(FormFile file)
	{
		Logger.log("ImportSchemaAForm zip file "+ file.getFileName()+" uploaded.");
		addFileToField(ZIP_FILE, file);
	}
	
	//must tally with the fieldName
	public FormFileElement[] getZipFile()
	{
		return _zipFile;
	}
	
	public void setZipFile(FormFileElement[] formFileElements)
	{
		_zipFile = formFileElements;
	}
	
	public String getBaseDir()
	{
		return _baseDir;
	}
	
	public void setBaseDir(String baseDir)
	{
		_baseDir = baseDir;
	}
	
	public String[] getSchemaMapping()
	{
		return _schemaMapping;
	}
	
	public void setSchemaMapping(String[] schemaMapping)
	{
		_schemaMapping = schemaMapping;
	}
	
	public String getMarkAsDelete()
	{
		return _markAsDelete;
	}
	
	public void setMarkAsDelete(String markAsDelete)
	{
		_markAsDelete = markAsDelete;
	}
}
