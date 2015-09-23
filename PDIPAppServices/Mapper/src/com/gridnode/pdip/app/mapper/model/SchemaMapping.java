/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SchemaMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 6, 2006    i00107             Created
 */

package com.gridnode.pdip.app.mapper.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * @author i00107
 * @sicne GT 4.0
 * This entity represents the temporary data model for a schema file in a zip file
 * that is to be created as schema type MappingFile.
 */
public class SchemaMapping extends AbstractEntity implements ISchemaMapping
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8694804502270410869L;
	private String _zipEntryName;
	private String _path;
	private String _fileName;
	private String _mappingName;
	private String _description;
	
	/**
	 * 
	 */
	public SchemaMapping()
	{
	}

	/**
	 * @see com.gridnode.pdip.framework.db.entity.AbstractEntity#getEntityName()
	 */
	@Override
	public String getEntityName()
	{
		return ENTITY_NAME;
	}

	/**
	 * @see com.gridnode.pdip.framework.db.entity.IEntity#getEntityDescr()
	 */
	public String getEntityDescr()
	{
		return getZipEntryName() + "/" + getMappingName();
	}

	/**
	 * @see com.gridnode.pdip.framework.db.entity.IEntity#getKeyId()
	 */
	public Number getKeyId()
	{
		return ZIP_ENTRY_NAME;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return _description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		_description = description;
	}

	/**
	 * @return Returns the fileName.
	 */
	public String getFileName()
	{
		return _fileName;
	}

	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName)
	{
		_fileName = fileName;
	}

	/**
	 * @return Returns the mappingName.
	 */
	public String getMappingName()
	{
		return _mappingName;
	}

	/**
	 * @param mappingName The mappingName to set.
	 */
	public void setMappingName(String mappingName)
	{
		_mappingName = mappingName;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath()
	{
		return _path;
	}

	/**
	 * @param path The path to set.
	 */
	public void setPath(String path)
	{
		_path = path;
	}

	/**
	 * @return Returns the zipEntryName.
	 */
	public String getZipEntryName()
	{
		return _zipEntryName;
	}

	/**
	 * @param zipEntryName The zipEntryName to set.
	 */
	public void setZipEntryName(String zipEntryName)
	{
		_zipEntryName = zipEntryName;
	}

}
