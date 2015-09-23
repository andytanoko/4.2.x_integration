/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGridTalkMappingFileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.mapper.IGridTalkMappingRule;

public interface IGTGridTalkMappingRuleEntity extends IGTEntity
{
  public static final Number UID = IGridTalkMappingRule.UID;
  public static final Number CAN_DELETE = IGridTalkMappingRule.CAN_DELETE;
  public static final Number NAME = IGridTalkMappingRule.NAME;
  public static final Number DESCRIPTION = IGridTalkMappingRule.DESCRIPTION;
  public static final Number SOURCE_DOC_TYPE = IGridTalkMappingRule.SOURCE_DOC_TYPE;
  public static final Number TARGET_DOC_TYPE = IGridTalkMappingRule.TARGET_DOC_TYPE;
  public static final Number SOURCE_DOC_FILE_TYPE = IGridTalkMappingRule.SOURCE_DOC_FILE_TYPE;
  public static final Number TARGET_DOC_FILE_TYPE = IGridTalkMappingRule.TARGET_DOC_FILE_TYPE;
  public static final Number IS_HEADER_TRANSFORMATION = IGridTalkMappingRule.HEADER_TRANSFORMATION;
  public static final Number IS_TRANSFORM_WITH_HEADER = IGridTalkMappingRule.TRANSFORM_WITH_HEADER;
  public static final Number IS_TRANSFORM_WITH_SOURCE = IGridTalkMappingRule.TRANSFORM_WITH_SOURCE;
  public static final Number MAPPING_RULE = IGridTalkMappingRule.MAPPING_RULE;
}