/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-29     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;
 
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTActionEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ActionDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ACTION;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ActionRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ACTION_UPDATE : IDocumentKeys.ACTION_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTActionEntity action = (IGTActionEntity)entity;

    ActionAForm form = (ActionAForm)actionContext.getActionForm();
    form.setName(action.getFieldString(IGTActionEntity.NAME));
    form.setDescription(action.getFieldString(IGTActionEntity.DESCRIPTION));
    form.setMsgId(action.getFieldString(IGTActionEntity.MSG_ID));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ActionAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ACTION;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTActionEntity action = (IGTActionEntity)entity;
    //ActionAForm tform = (ActionAForm)form;

    basicValidateString(errors, IGTActionEntity.NAME, form, entity);
    basicValidateString(errors, IGTActionEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTActionEntity.MSG_ID, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTActionEntity action = (IGTActionEntity)entity;
    ActionAForm form = (ActionAForm)actionContext.getActionForm();

    action.setFieldValue(IGTActionEntity.NAME, form.getName());
    action.setFieldValue(IGTActionEntity.DESCRIPTION, form.getDescription());
    action.setFieldValue(IGTActionEntity.MSG_ID, form.getMsgIdLong());
  }
}