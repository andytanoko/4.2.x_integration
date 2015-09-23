/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HouseKeepingDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 * 2006-06-26     Tam Wei Xiang       Modified method initialiseActionForm(...),
 *                                    updateEntityFields(...), validateActionForm(...)
 *                                    to add new field 'WF_RECORDS_DAYS_TO_KEEP'.
 */
package com.gridnode.gtas.client.web.archive;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTHouseKeepingEntity;
import com.gridnode.gtas.client.ctrl.IGTHouseKeepingManager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.netweaver.helper.Logger;

public class HouseKeepingDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_HOUSE_KEEPING;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new HouseKeepingRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.HOUSE_KEEPING_UPDATE : IDocumentKeys.HOUSE_KEEPING_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    HouseKeepingAForm form = (HouseKeepingAForm)actionContext.getActionForm();
    form.setTempFilesDaysToKeep(entity.getFieldString(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP));
    form.setLogFilesDaysToKeep( entity.getFieldString(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP));
    form.setWfRecordsDaysToKeep(entity.getFieldString(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new HouseKeepingAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_HOUSE_KEEPING;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    basicValidateString(errors, IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP, form, entity);
    basicValidateString(errors, IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP, form, entity);
    basicValidateString(errors, IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    HouseKeepingAForm houseKeepingAForm = (HouseKeepingAForm)actionContext.getActionForm();
    entity.setFieldValue(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP,       houseKeepingAForm.getTempFilesDaysToKeepAsInteger());
    entity.setFieldValue(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP,        houseKeepingAForm.getLogFilesDaysToKeepAsInteger());
    entity.setFieldValue(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP,houseKeepingAForm.getWfRecordsDaysToKeepAsInteger());
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    ((IGTHouseKeepingManager)manager).saveHouseKeeping((IGTHouseKeepingEntity)entity);
    
    setReturnToView(actionContext, true);
  }
}