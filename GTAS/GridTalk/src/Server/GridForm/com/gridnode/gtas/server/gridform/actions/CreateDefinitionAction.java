/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateDefinitionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 * Jun 25 2002    Daniel D'Cotta      Extend from AbstractCreateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Daniel D'Cotta      Return created entity to client.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.events.gridform.CreateDefinitionEvent;
import com.gridnode.gtas.server.gridform.helpers.ActionHelper;
import com.gridnode.gtas.server.gridform.helpers.Logger;

import com.gridnode.pdip.app.gridform.model.GFDefinition;
import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * Action that handles the creation of a definition entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class CreateDefinitionAction
  extends    AbstractCreateEntityAction
{
  public static final String ACTION_NAME = "CreateDefinitionAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateDefinitionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getGFManager().findGFDefinition(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {

    CreateDefinitionEvent addEvent = (CreateDefinitionEvent)event;

    GFTemplate template = new GFTemplate();
    template.setUId(addEvent.getTemplate().longValue());

    GFDefinition newDefinition = new GFDefinition();
    newDefinition.setDefinitionName (addEvent.getName());
    newDefinition.setFilename       (addEvent.getFilename());
    newDefinition.setTemplate       (template);

    return newDefinition;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateDefinitionEvent addEvent = (CreateDefinitionEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             GFDefinition.ENTITY_NAME,
             addEvent.getName(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    GFDefinition definition = (GFDefinition)entity;
    ActionHelper.transferDefinitionFromTemp(definition);
    return ActionHelper.getGFManager().createGFDefinition(definition);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGFDefinitionToMap((GFDefinition)entity);
  }

  // ****************** Own methods **********************************
}