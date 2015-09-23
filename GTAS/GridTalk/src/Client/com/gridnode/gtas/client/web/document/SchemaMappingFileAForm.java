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
 * Mar 14, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class SchemaMappingFileAForm extends GTActionFormBase
{
	private String _path;
	private String _fileName;
	private String _mappingName;
	private String _description;
	
	public String getDescription()
	{
		return _description;
	}
	public void setDescription(String _description)
	{
		this._description = _description;
	}
	public String getFileName()
	{
		return _fileName;
	}
	public void setFileName(String _filename)
	{
		this._fileName = _filename;
	}
	public String getMappingName()
	{
		return _mappingName;
	}
	public void setMappingName(String filename)
	{
		_mappingName = filename;
	}
	public String getPath()
	{
		return _path;
	}
	public void setPath(String _path)
	{
		this._path = _path;
	}
	
	
}
