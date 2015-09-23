/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMappingRuleEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.mapper.IMappingRule;
/**
 * The IGTMappingRuleEntity is an embedded entity in the MAPPING_RULE field of
 * IGTGridTalkMappingRuleEntity.
 */
public interface IGTMappingRuleEntity extends IGTEntity
{
  // Type fields types
  public static final Short TYPE_MAPPING_CONVERT = IMappingRule.MAPPING_CONVERT;
  public static final Short TYPE_MAPPING_TRANSFORM = IMappingRule.MAPPING_TRANSFORM;
  public static final Short TYPE_MAPPING_SPLIT = IMappingRule.MAPPING_SPLIT;

  // Entity fields
  public static final Number UID = IMappingRule.UID;
  public static final Number NAME = IMappingRule.NAME;
  public static final Number DESCRIPTION = IMappingRule.DESCRIPTION;
  public static final Number TYPE = IMappingRule.TYPE;
  public static final Number MAPPING_FILE = IMappingRule.MAPPING_FILE;
  public static final Number IS_TRANSFORM_REF_DOC = IMappingRule.TRANSFORM_REF_DOC;
  public static final Number REF_DOC_UID = IMappingRule.REFERENCE_DOC_UID;
  public static final Number XPATH = IMappingRule.XPATH;
  public static final Number PARAM_NAME = IMappingRule.PARAM_NAME;
  public static final Number IS_KEEP_ORIGINAL = IMappingRule.KEEP_ORIGINAL;
  public static final Number MAPPING_CLASS = IMappingRule.MAPPING_CLASS;

  // The following can be factored out when metainfo is refactored in I4 :-)
  /*public String getTypeLabelKey() throws GTClientException;
  public String getTypeLabelKey(Short type) throws GTClientException;
  public Collection getAllowedTypes() throws GTClientException;*/
}