/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISchemaMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 6, 2006    i00107              Created
 */

package com.gridnode.pdip.app.mapper.model;

/**
 * @author i00107
 * @since GT4.0
 * This interface defines the properties and fieldIDs to access the
 * entity SchemaMapping. The SchemaMapping entity is non-persistent in database.
 */
public interface ISchemaMapping
{
	public static final String ENTITY_NAME = "SchemaMapping";
	
	/**
	 * Field ID for the entry name of the schema file in the zip file. A String. 
	 */
	public static final Number ZIP_ENTRY_NAME = new Integer(1); //String(255)
	
	/**
	 * Field ID for the directory path of the schema file in the zip file. A String.
	 */
	public static final Number PATH = new Integer(2); //String(80), depends on SUB_PATH in MappingFile entity.
	
	/**
	 * Field ID for the file name of the schema file in the zip file. A String.
	 */
	public static final Number FILENAME = new Integer(3); //String(80)
	
	/**
	 * Field ID for the Name of the MappingFile to create for the schema file. A String.
	 */
	public static final Number MAPPING_NAME = new Integer(4); //String(30)
	
	/**
	 * Field ID for the Description of the MappingFile to create for the schema file. A String.
	 */
	public static final Number DESCRIPTION = new Integer(5); //String(80)
	
}
