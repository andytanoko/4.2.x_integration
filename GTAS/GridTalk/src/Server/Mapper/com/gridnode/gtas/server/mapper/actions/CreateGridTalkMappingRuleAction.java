/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateGridTalkMappingRuleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractCreateEntityAction
 */
package com.gridnode.gtas.server.mapper.actions;

import java.util.Map;

import com.gridnode.gtas.events.mapper.CreateGridTalkMappingRuleEvent;
import com.gridnode.gtas.model.mapper.GridTalkMappingRuleEntityFieldID;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class CreateGridTalkMappingRuleAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -730291891474534969L;
	private static final String ACTION_NAME = "CreateGridTalkMappingRuleAction";

	protected Map convertToMap(AbstractEntity entity)
	{
		return GridTalkMappingRule.convertToMap(entity, GridTalkMappingRuleEntityFieldID.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		GridTalkMappingRule gtMappingRule = (GridTalkMappingRule)entity;
		MappingRule mappingRule = (MappingRule)getMappingManager().createMappingRule(gtMappingRule.getMappingRule());
		gtMappingRule.setMappingRule(mappingRule);
		return getGTMappingManager().createGridTalkMappingRule(gtMappingRule);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {GridTalkMappingRule.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateGridTalkMappingRuleEvent createEvent = (CreateGridTalkMappingRuleEvent)event;
    MappingRule mappingRule = getMappingRule(createEvent);
    GridTalkMappingRule gtMappingRule = getGTMappingRule(createEvent, mappingRule);
    return gtMappingRule;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getGTMappingManager().findGridTalkMappingRule(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateGridTalkMappingRuleEvent.class;
	}

  private MappingRule getMappingRule(CreateGridTalkMappingRuleEvent event)
  {
    MappingRule newMappingRule = new MappingRule();
    newMappingRule.setName(event.getGridTalkMappingRuleName());
    newMappingRule.setDescription(event.getGridTalkMappingRuleDesc());
    newMappingRule.setType(event.getMappingRuleType());

    MappingFile mappingFile = new MappingFile();
    mappingFile.setUId(event.getMappingFileUID().longValue());
    newMappingRule.setMappingFile(mappingFile);

    newMappingRule.setTransformRefDoc(event.isTransformRefDoc());
    newMappingRule.setRefDocUID(event.getReferenceDocUID());
    newMappingRule.setXPath(event.getXPath());
    newMappingRule.setParamName(event.getParamName());
    newMappingRule.setKeepOriginal(event.isKeepOriginal());
    newMappingRule.setMappingClass(event.getMappingClass());
    System.out.println("CreateGridTalkMappingRuleAction.getMappingRule():"+event.getMappingClass());


    return newMappingRule;
  }

  private GridTalkMappingRule getGTMappingRule(
    CreateGridTalkMappingRuleEvent event,
    MappingRule mappingRule)
  {
    GridTalkMappingRule newGTMappingRule = new GridTalkMappingRule();
    newGTMappingRule.setName(event.getGridTalkMappingRuleName());
    newGTMappingRule.setDescription(event.getGridTalkMappingRuleDesc());
    newGTMappingRule.setSourceDocType(event.getSourceDocType());
    newGTMappingRule.setTargetDocType(event.getTargetDocType());
    newGTMappingRule.setSourceDocFileType(event.getSourceFileType());
    newGTMappingRule.setTargetDocFileType(event.getTargetFileType());
    newGTMappingRule.setHeaderTransformation(event.isHeaderTransformation());
    newGTMappingRule.setTransformWithHeader(event.isTransformWithHeader());
    newGTMappingRule.setTransformWithSource(event.isTransformWithSource());
    newGTMappingRule.setMappingRule(mappingRule);

    return newGTMappingRule;
  }

  private IGridTalkMappingManagerObj getGTMappingManager()
    throws ServiceLookupException
  {
    return (IGridTalkMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridTalkMappingManagerHome.class.getName(),
      IGridTalkMappingManagerHome.class,
      new Object[0]);
  }

  private IMappingManagerObj getMappingManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }
}