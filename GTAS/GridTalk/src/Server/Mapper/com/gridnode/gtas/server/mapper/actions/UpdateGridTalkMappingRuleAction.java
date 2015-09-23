/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateGridTalkMappingRuleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction.
 */
package com.gridnode.gtas.server.mapper.actions;

import java.util.Map;

import com.gridnode.gtas.events.mapper.UpdateGridTalkMappingRuleEvent;
import com.gridnode.gtas.model.mapper.GridTalkMappingRuleEntityFieldID;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the update of a GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class UpdateGridTalkMappingRuleAction
  extends    AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8427394710666200618L;
	private static final String ACTION_NAME = "UpdateGridTalkMappingRuleAction";

	private GridTalkMappingRule _gtMappingRuleToUpdate;
	
	protected Map convertToMap(AbstractEntity entity)
	{
		return GridTalkMappingRule.convertToMap(entity, GridTalkMappingRuleEntityFieldID.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateGridTalkMappingRuleEvent updEvent = (UpdateGridTalkMappingRuleEvent)event;

		return new Object[] {GridTalkMappingRule.ENTITY_NAME, updEvent.getGridTalkMappingRuleUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateGridTalkMappingRuleEvent updEvent = (UpdateGridTalkMappingRuleEvent)event;
		MappingRule mappingRule = _gtMappingRuleToUpdate.getMappingRule();
    mappingRule = updateMappingRule(mappingRule, updEvent);
    _gtMappingRuleToUpdate = updateGridTalkMappingRule(_gtMappingRuleToUpdate, updEvent, mappingRule);

		return _gtMappingRuleToUpdate;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getGTMappingManager().findGridTalkMappingRule(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getMappingManager().updateMappingRule(_gtMappingRuleToUpdate.getMappingRule());
		getGTMappingManager().updateGridTalkMappingRule(_gtMappingRuleToUpdate);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateGridTalkMappingRuleEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdateGridTalkMappingRuleEvent updEvent = (UpdateGridTalkMappingRuleEvent)event;
		_gtMappingRuleToUpdate = getGTMappingManager().findGridTalkMappingRule(updEvent.getGridTalkMappingRuleUID());
	}

  private MappingRule updateMappingRule(
    MappingRule updateMappingRule,
    UpdateGridTalkMappingRuleEvent event)
  {
    updateMappingRule.setDescription(event.getGridTalkMappingRuleDesc());
    updateMappingRule.setType(event.getMappingRuleType());

    MappingFile mappingFile = new MappingFile();
    mappingFile.setUId(event.getMappingFileUID().longValue());
    updateMappingRule.setMappingFile(mappingFile);

    updateMappingRule.setTransformRefDoc(event.isTransformRefDoc());
    updateMappingRule.setRefDocUID(event.getReferenceDocUID());
    updateMappingRule.setXPath(event.getXPath());
    updateMappingRule.setParamName(event.getParamName());
    updateMappingRule.setKeepOriginal(event.isKeepOriginal());
    updateMappingRule.setMappingClass(event.getMappingClass());
    
    return updateMappingRule;
  }

  private GridTalkMappingRule updateGridTalkMappingRule(
    GridTalkMappingRule updateGTMappingRule,
    UpdateGridTalkMappingRuleEvent event,
    MappingRule updateMappingRule)
  {
    updateGTMappingRule.setDescription(event.getGridTalkMappingRuleDesc());
    updateGTMappingRule.setSourceDocType(event.getSourceDocType());
    updateGTMappingRule.setTargetDocType(event.getTargetDocType());
    updateGTMappingRule.setSourceDocFileType(event.getSourceFileType());
    updateGTMappingRule.setTargetDocFileType(event.getTargetFileType());
    updateGTMappingRule.setHeaderTransformation(event.isHeaderTransformation());
    updateGTMappingRule.setTransformWithHeader(event.isTransformWithHeader());
    updateGTMappingRule.setTransformWithSource(event.isTransformWithSource());
    updateGTMappingRule.setMappingRule(updateMappingRule);

    return updateGTMappingRule;
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