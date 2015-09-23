/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSchemaMappingEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 9, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.mapper.ISchemaMapping;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTSchemaMappingFileEntity extends IGTEntity
{	
	public static Number PATH = ISchemaMapping.PATH;
	public static Number FILENAME = ISchemaMapping.FILENAME;
	public static Number MAPPING_NAME = ISchemaMapping.MAPPING_NAME;
	public static Number DESCRIPTION = ISchemaMapping.DESCRIPTION;
	public static Number ZIP_ENTRY_NAME = ISchemaMapping.ZIP_ENTRY_NAME;
}
