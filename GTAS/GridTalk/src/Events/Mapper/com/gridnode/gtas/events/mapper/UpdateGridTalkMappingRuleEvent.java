/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateGridTalkMappingRuleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 02 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for the creation of new GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateGridTalkMappingRuleEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8530824158161286858L;
	public static final String MAPPING_RULE_UID       = "MappingRule UID";
  public static final String MAPPING_RULE_DESC      = "MappingRule Desc";
  public static final String SOURCE_DOC_TYPE        = "Source Doc Type";
  public static final String TARGET_DOC_TYPE        = "Target Doc Type";
  public static final String SOURCE_FILE_TYPE       = "Source File Type";
  public static final String TARGET_FILE_TYPE       = "Target File Type";
  public static final String HEADER_TRANSFORMATION  = "Header Transformation";
  public static final String TRANSFORM_WITH_HEADER  = "Transform With Header";
  public static final String TRANSFORM_WITH_SOURCE  = "Transform With Source";
  public static final String MAPPING_RULE_TYPE      = "MappingRule Type";
  public static final String MAPPING_FILE_UID       = "MappingFile UID";
  public static final String TRANSFORM_REF_DOC      = "Transform Ref Doc";
  public static final String REFERENCE_DOC_UID      = "Reference Doc UID";
  public static final String XPATH                  = "XPath";
  public static final String PARAM_NAME             = "Param Name";
  public static final String KEEP_ORIGINAL          = "Keep Original";
  public static final String MAPPING_CLASS          = "Mapping Class";

  public UpdateGridTalkMappingRuleEvent(
    Long mappingRuleUID,
    String mappingRuleDesc,
    String sourceDocType,
    String targetDocType,
    String sourceFileType,
    String targetFileType,
    Boolean headerTransformation,
    Boolean transformWithHeader,
    Boolean transformWithSource,
    Short mappingRuleType,
    Long mappingFileUID,
    Boolean transformRefDoc,
    Long referenceDocUID,
    String xPath,
    String paramName,
    Boolean keepOriginal,
    String mappingClass)
  {
    setEventData(MAPPING_RULE_UID, mappingRuleUID);
    setEventData(MAPPING_RULE_DESC, mappingRuleDesc);
    setEventData(SOURCE_DOC_TYPE, sourceDocType);
    setEventData(TARGET_DOC_TYPE, targetDocType);
    setEventData(SOURCE_FILE_TYPE, sourceFileType);
    setEventData(TARGET_FILE_TYPE, targetFileType);
    setEventData(HEADER_TRANSFORMATION, headerTransformation);
    setEventData(TRANSFORM_WITH_HEADER, transformWithHeader);
    setEventData(TRANSFORM_WITH_SOURCE, transformWithSource);
    setEventData(MAPPING_RULE_TYPE, mappingRuleType);
    setEventData(MAPPING_FILE_UID, mappingFileUID);
    setEventData(TRANSFORM_REF_DOC, transformRefDoc);
    setEventData(REFERENCE_DOC_UID, referenceDocUID);
    setEventData(XPATH, xPath);
    setEventData(PARAM_NAME, paramName);
    setEventData(KEEP_ORIGINAL, keepOriginal);
    setEventData(MAPPING_CLASS, mappingClass);
  }

  public Long getGridTalkMappingRuleUID()
  {
    return (Long)getEventData(MAPPING_RULE_UID);
  }

  public String getGridTalkMappingRuleDesc()
  {
    return (String)getEventData(MAPPING_RULE_DESC);
  }

  public String getSourceDocType()
  {
    return (String)getEventData(SOURCE_DOC_TYPE);
  }

  public String getTargetDocType()
  {
    return (String)getEventData(TARGET_DOC_TYPE);
  }

  public String getSourceFileType()
  {
    return (String)getEventData(SOURCE_FILE_TYPE);
  }

  public String getTargetFileType()
  {
    return (String)getEventData(TARGET_FILE_TYPE);
  }

  public Boolean isHeaderTransformation()
  {
    return (Boolean)getEventData(HEADER_TRANSFORMATION);
  }

  public Boolean isTransformWithHeader()
  {
    return (Boolean)getEventData(TRANSFORM_WITH_HEADER);
  }

  public Boolean isTransformWithSource()
  {
    return (Boolean)getEventData(TRANSFORM_WITH_SOURCE);
  }

  public Short getMappingRuleType()
  {
    return (Short)getEventData(MAPPING_RULE_TYPE);
  }

  public Long getMappingFileUID()
  {
    return (Long)getEventData(MAPPING_FILE_UID);
  }

  public Boolean isTransformRefDoc()
  {
    return (Boolean)getEventData(TRANSFORM_REF_DOC);
  }

  public Long getReferenceDocUID()
  {
    return (Long)getEventData(REFERENCE_DOC_UID);
  }

  public String getXPath()
  {
    return (String)getEventData(XPATH);
  }

  public String getParamName()
  {
    return (String)getEventData(PARAM_NAME);
  }

  public Boolean isKeepOriginal()
  {
    return (Boolean)getEventData(KEEP_ORIGINAL);
  }
  
  public String getMappingClass()
  {
    return (String)getEventData(MAPPING_CLASS);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateGridTalkMappingRuleEvent";
  }

}