/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Mar 06 2006    Neo Sok Lay         Use generics
 *                                    Add SchemaMapping
 * Mar 20 2006    Tam Wei Xiang       To place the schemaMapping field's ID 
 *                                    into a new hashtable          
 * Feb 06 2007    Chong SoonFui       Commented IMappingFile.CAN_DELETE                         
 */
package com.gridnode.gtas.model.mapper;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Mapper module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public class MappingFileEntityFieldID
{
  private Hashtable<String,Number[]> _table;
  private Hashtable<String,Number[]> _schemaMappingTable;
  private static MappingFileEntityFieldID _self = null;

  private MappingFileEntityFieldID()
  {
    _table = new Hashtable<String,Number[]>();

    _table.put(IMappingFile.ENTITY_NAME,
      new Number[]
      {
//        IMappingFile.CAN_DELETE,
        IMappingFile.DESCRIPTION,
        IMappingFile.FILENAME,
        IMappingFile.NAME,
        IMappingFile.PATH,
        IMappingFile.SUB_PATH,
        IMappingFile.TYPE,
        IMappingFile.UID
      });
    
    _schemaMappingTable = new Hashtable<String, Number[]>();
    _schemaMappingTable.put(ISchemaMapping.ENTITY_NAME,
        new Number[]
        {
    			ISchemaMapping.DESCRIPTION,
    			ISchemaMapping.FILENAME,
    			ISchemaMapping.MAPPING_NAME,
    			ISchemaMapping.PATH,
    			ISchemaMapping.ZIP_ENTRY_NAME,
    });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new MappingFileEntityFieldID();
    }
    return _self._table;
  }
  
  public static Hashtable getEntityFieldIDForSchemaMapping()
  {
  	if (_self == null)
    {
      _self = new MappingFileEntityFieldID();
    }
    return _self._schemaMappingTable;
  }
}