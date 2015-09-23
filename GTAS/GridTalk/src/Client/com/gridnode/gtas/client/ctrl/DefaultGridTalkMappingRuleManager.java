/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGridTalkMappingRuleManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.mapper.CreateGridTalkMappingRuleEvent;
import com.gridnode.gtas.events.mapper.DeleteGridTalkMappingRuleEvent;
import com.gridnode.gtas.events.mapper.GetGridTalkMappingRuleEvent;
import com.gridnode.gtas.events.mapper.GetGridTalkMappingRuleListEvent;
import com.gridnode.gtas.events.mapper.UpdateGridTalkMappingRuleEvent;
import com.gridnode.gtas.model.mapper.IGridTalkMappingRule;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultGridTalkMappingRuleManager extends DefaultAbstractManager
  implements IGTGridTalkMappingRuleManager
{
  DefaultGridTalkMappingRuleManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_GRIDTALK_MAPPING_RULE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTGridTalkMappingRuleEntity gtmr = (IGTGridTalkMappingRuleEntity)entity;
    IGTMappingRuleEntity mr = (IGTMappingRuleEntity)entity.getFieldValue(IGTGridTalkMappingRuleEntity.MAPPING_RULE);
    try
    {
      Long uid = (Long)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.UID);
      String description = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.DESCRIPTION);
      String sourceDocType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.SOURCE_DOC_TYPE);
      String targetDocType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.TARGET_DOC_TYPE);
      String sourceDocFileType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.SOURCE_DOC_FILE_TYPE);
      String targetDocFileType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.TARGET_DOC_FILE_TYPE);
      Boolean headerTransformation = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_HEADER_TRANSFORMATION);
      Boolean transformWithHeader = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_HEADER);
      Boolean transformWithSource = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_SOURCE);
      Short type = (Short)mr.getFieldValue(IGTMappingRuleEntity.TYPE);
      Long mappingFileUid = (Long)mr.getFieldValue(IGTMappingRuleEntity.MAPPING_FILE);
      Boolean transformRefDoc = (Boolean)mr.getFieldValue(IGTMappingRuleEntity.IS_TRANSFORM_REF_DOC);
      Long refDocUid = (Long)mr.getFieldValue(IGTMappingRuleEntity.REF_DOC_UID);
      String xpath = mr.getFieldString(IGTMappingRuleEntity.XPATH);
      String paramName = mr.getFieldString(IGTMappingRuleEntity.PARAM_NAME);
      Boolean keepOriginal = (Boolean)mr.getFieldValue(IGTMappingRuleEntity.IS_KEEP_ORIGINAL);
      String mappingClass = (String)mr.getFieldValue(IGTMappingRuleEntity.MAPPING_CLASS);
      UpdateGridTalkMappingRuleEvent event = new UpdateGridTalkMappingRuleEvent(
                                uid,
                                description,
                                sourceDocType,
                                targetDocType,
                                sourceDocFileType,
                                targetDocFileType,
                                headerTransformation,
                                transformWithHeader,
                                transformWithSource,
                                type,
                                mappingFileUid,
                                transformRefDoc,
                                refDocUid,
                                xpath,
                                paramName,
                                keepOriginal,
                                mappingClass);
       handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTGridTalkMappingRuleEntity gtmr = (IGTGridTalkMappingRuleEntity)entity;
    IGTMappingRuleEntity mr = (IGTMappingRuleEntity)entity.getFieldValue(IGTGridTalkMappingRuleEntity.MAPPING_RULE);
    try
    {
      String name = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.NAME);
      String description = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.DESCRIPTION);
      String sourceDocType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.SOURCE_DOC_TYPE);
      String targetDocType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.TARGET_DOC_TYPE);
      String sourceDocFileType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.SOURCE_DOC_FILE_TYPE);
      String targetDocFileType = gtmr.getFieldString(IGTGridTalkMappingRuleEntity.TARGET_DOC_FILE_TYPE);
      Boolean headerTransformation = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_HEADER_TRANSFORMATION);
      Boolean transformWithHeader = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_HEADER);
      Boolean transformWithSource = (Boolean)gtmr.getFieldValue(IGTGridTalkMappingRuleEntity.IS_TRANSFORM_WITH_SOURCE);
      Short type = (Short)mr.getFieldValue(IGTMappingRuleEntity.TYPE);
      Long mappingFileUid = (Long)mr.getFieldValue(IGTMappingRuleEntity.MAPPING_FILE);
      Boolean transformRefDoc = (Boolean)mr.getFieldValue(IGTMappingRuleEntity.IS_TRANSFORM_REF_DOC);
      Long refDocUid = (Long)mr.getFieldValue(IGTMappingRuleEntity.REF_DOC_UID);
      String xpath = mr.getFieldString(IGTMappingRuleEntity.XPATH);
      String paramName = mr.getFieldString(IGTMappingRuleEntity.PARAM_NAME);
      Boolean keepOriginal = (Boolean)mr.getFieldValue(IGTMappingRuleEntity.IS_KEEP_ORIGINAL);
      String mappingClass = (String)mr.getFieldValue(IGTMappingRuleEntity.MAPPING_CLASS);
      CreateGridTalkMappingRuleEvent event = new CreateGridTalkMappingRuleEvent(
                                name,
                                description,
                                sourceDocType,
                                targetDocType,
                                sourceDocFileType,
                                targetDocFileType,
                                headerTransformation,
                                transformWithHeader,
                                transformWithSource,
                                type,
                                mappingFileUid,
                                transformRefDoc,
                                refDocUid,
                                xpath,
                                paramName,
                                keepOriginal,
                                mappingClass);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  public Collection getAllOfHeaderTransformation(Boolean headerTransformation)
    throws GTClientException
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,IGridTalkMappingRule.HEADER_TRANSFORMATION,
                           filter.getEqualOperator(),headerTransformation,false);

    GetGridTalkMappingRuleListEvent event = new GetGridTalkMappingRuleListEvent(filter);
    return handleGetListEvent(event);
  }

  public IGTGridTalkMappingRuleEntity getGridTalkMappingRuleByUID(long uid)
    throws GTClientException
  {
    return (IGTGridTalkMappingRuleEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_GRIDTALK_MAPPING_RULE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_GRIDTALK_MAPPING_RULE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridTalkMappingRuleEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridTalkMappingRuleListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteGridTalkMappingRuleEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_MAPPING_RULE.equals(entityType))
    {
      return new DefaultMappingRuleEntity();
    }
    return new DefaultGridTalkMappingRuleEntity();
  }

}