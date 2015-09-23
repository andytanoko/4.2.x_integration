/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateDefinitionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 * Jul 08 2002    Daniel D'Cotta      Add file handling.
 * Jun 25 2002    Daniel D'Cotta      Extend from AbstractUpdateEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 25 2002    Daniel D'Cotta      Return created entity to client.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.server.gridform.helpers.ActionHelper;
import com.gridnode.gtas.server.gridform.helpers.Logger;
import com.gridnode.gtas.events.gridform.UpdateDefinitionEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;

import com.gridnode.pdip.app.gridform.model.GFDefinition;
import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * Action that handles the update of a definition entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class UpdateDefinitionAction
  extends    AbstractUpdateEntityAction
{
  public static final String ACTION_NAME = "UpdateDefinitionAction";

  private GFDefinition _definitionToUpdate;
  private boolean _isUploading;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    GFDefinition definition = (GFDefinition)entity;
    if(_isUploading)
    {
      ActionHelper.transferDefinitionFromTemp(definition);
    }
    ActionHelper.getGFManager().updateGFDefinition(definition);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateDefinitionEvent updEvent = (UpdateDefinitionEvent)event;

    _definitionToUpdate.setDefinitionName(updEvent.getName());

    _isUploading = updEvent.getIsUploading().booleanValue();
    if(_isUploading)
    {
      _definitionToUpdate.setFilename(updEvent.getFilename());
    }

    GFTemplate template = new GFTemplate();
    template.setUId(updEvent.getTemplate().longValue());
    _definitionToUpdate.setTemplate(template);

    return _definitionToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateDefinitionEvent updEvent = (UpdateDefinitionEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             GFDefinition.ENTITY_NAME,
             updEvent.getName(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateDefinitionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    _definitionToUpdate = verifyValidDefinition((UpdateDefinitionEvent)event);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getGFManager().findGFDefinition(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGFDefinitionToMap((GFDefinition)entity);
  }

  // **************************** Own Methods *****************************

  private GFDefinition verifyValidDefinition(UpdateDefinitionEvent event)
    throws Exception
  {
    GFDefinition definition = ActionHelper.verifyGFDefinition(event.getUID());
    return definition;
  }
}