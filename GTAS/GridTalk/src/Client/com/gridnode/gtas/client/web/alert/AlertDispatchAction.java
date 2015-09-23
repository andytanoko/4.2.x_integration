/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.alert;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAlertEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class AlertDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_ALERT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new AlertRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.ALERT_UPDATE : IDocumentKeys.ALERT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTAlertEntity alert = (IGTAlertEntity)entity;

    AlertAForm form = (AlertAForm)actionContext.getActionForm();
    form.setName(alert.getFieldString(IGTAlertEntity.NAME));
    form.setDescription(alert.getFieldString(IGTAlertEntity.DESCRIPTION));
    form.setType(alert.getFieldString(IGTAlertEntity.TYPE));
    form.setActions(alert.getFieldStringArray(IGTAlertEntity.ACTIONS));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new AlertAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ALERT;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTAlertEntity alert = (IGTAlertEntity)entity;
    //AlertAForm tform = (AlertAForm)form;

    basicValidateString(errors, IGTAlertEntity.NAME, form, entity);
    basicValidateString(errors, IGTAlertEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTAlertEntity.TYPE, form, entity);

    // No validation needed for actions
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTAlertEntity alert = (IGTAlertEntity)entity;
    AlertAForm form = (AlertAForm)actionContext.getActionForm();

    alert.setFieldValue(IGTAlertEntity.NAME, form.getName());
    alert.setFieldValue(IGTAlertEntity.DESCRIPTION, form.getDescription());
    alert.setFieldValue(IGTAlertEntity.TYPE, form.getTypeLong());
    alert.setFieldValue(IGTAlertEntity.ACTIONS, form.getActionsLongCollection());
  }
}