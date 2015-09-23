/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateTemplateAction.java
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
import com.gridnode.gtas.events.gridform.UpdateTemplateEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;

import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * Action that handles the update of a template entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class UpdateTemplateAction
  extends    AbstractUpdateEntityAction
{
  public static final String ACTION_NAME = "UpdateTemplateAction";

  private GFTemplate _templateToUpdate;
  private boolean _isUploading;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    GFTemplate template = (GFTemplate)entity;
    if(_isUploading)
    {
      ActionHelper.transferTemplateFromTemp(template);
    }
    ActionHelper.getGFManager().updateGFTemplate(template);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateTemplateEvent updEvent = (UpdateTemplateEvent)event;

    _templateToUpdate.setTemplateName (updEvent.getName());

    _isUploading = updEvent.getIsUploading().booleanValue();
    if(_isUploading)
    {
      _templateToUpdate.setFilename     (updEvent.getFilename());
    }

    return _templateToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateTemplateEvent updEvent = (UpdateTemplateEvent)event;
    /**@todo TBD */
    return new Object[]
           {
             GFTemplate.ENTITY_NAME,
             updEvent.getName(),
           };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateTemplateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    _templateToUpdate = verifyValidTemplate((UpdateTemplateEvent)event);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getGFManager().findGFTemplate(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGFTemplateToMap((GFTemplate)entity);
  }

  // **************************** Own Methods *****************************

  private GFTemplate verifyValidTemplate(UpdateTemplateEvent event)
    throws Exception
  {
    GFTemplate template = ActionHelper.verifyGFTemplate(event.getUID());
    return template;
  }
}