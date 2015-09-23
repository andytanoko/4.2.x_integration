/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateTemplateAction.java
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
import com.gridnode.gtas.events.gridform.CreateTemplateEvent;
import com.gridnode.gtas.server.gridform.helpers.ActionHelper;
import com.gridnode.gtas.server.gridform.helpers.Logger;

import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * Action that handles the creation of a template entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class CreateTemplateAction
  extends    AbstractCreateEntityAction
{
  public static final String ACTION_NAME = "CreateTemplateAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateTemplateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getGFManager().findGFTemplate(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateTemplateEvent addEvent = (CreateTemplateEvent)event;

    GFTemplate newTemplate = new GFTemplate();
    newTemplate.setTemplateName (addEvent.getName());
    newTemplate.setFilename     (addEvent.getFilename());

    return newTemplate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateTemplateEvent addEvent = (CreateTemplateEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             GFTemplate.ENTITY_NAME,
             addEvent.getName(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    GFTemplate template = (GFTemplate)entity;
    ActionHelper.transferTemplateFromTemp(template);
    return ActionHelper.getGFManager().createGFTemplate(template);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGFTemplateToMap((GFTemplate)entity);
  }

  // ****************** Own methods **********************************
}