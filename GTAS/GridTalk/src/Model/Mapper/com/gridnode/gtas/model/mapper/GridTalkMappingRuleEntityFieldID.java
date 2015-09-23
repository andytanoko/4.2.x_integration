/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented IGridTalkMappingRule.CAN_DELETE
 */
package com.gridnode.gtas.model.mapper;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Mapper module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GridTalkMappingRuleEntityFieldID
{
  private Hashtable _table;
  private static GridTalkMappingRuleEntityFieldID _self = null;

  private GridTalkMappingRuleEntityFieldID()
  {
    _table = new Hashtable();

    // GridTalkMappingRule
    _table.put(IGridTalkMappingRule.ENTITY_NAME,
      new Number[]
      {
//        IGridTalkMappingRule.CAN_DELETE,
        IGridTalkMappingRule.DESCRIPTION,
        IGridTalkMappingRule.HEADER_TRANSFORMATION,
        IGridTalkMappingRule.MAPPING_RULE,
        IGridTalkMappingRule.NAME,
        IGridTalkMappingRule.SOURCE_DOC_FILE_TYPE,
        IGridTalkMappingRule.SOURCE_DOC_TYPE,
        IGridTalkMappingRule.TARGET_DOC_FILE_TYPE,
        IGridTalkMappingRule.TARGET_DOC_TYPE,
        IGridTalkMappingRule.TRANSFORM_WITH_HEADER,
        IGridTalkMappingRule.TRANSFORM_WITH_SOURCE,
        IGridTalkMappingRule.UID,
      });

    // MappingRule
    _table.put(IMappingRule.ENTITY_NAME,
      new Number[]
      {
        IMappingRule.DESCRIPTION,
        IMappingRule.KEEP_ORIGINAL,
        IMappingRule.MAPPING_FILE,
        IMappingRule.NAME,
        IMappingRule.PARAM_NAME,
        IMappingRule.REFERENCE_DOC_UID,
        IMappingRule.TRANSFORM_REF_DOC,
        IMappingRule.TYPE,
        IMappingRule.UID,
        IMappingRule.XPATH,
        IMappingRule.MAPPING_CLASS
      });

    // MappingFile
    _table.put(IMappingFile.ENTITY_NAME,
      new Number[]
      {
        IMappingFile.DESCRIPTION,
        IMappingFile.NAME,
        IMappingFile.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new GridTalkMappingRuleEntityFieldID();
    }
    return _self._table;
  }
}