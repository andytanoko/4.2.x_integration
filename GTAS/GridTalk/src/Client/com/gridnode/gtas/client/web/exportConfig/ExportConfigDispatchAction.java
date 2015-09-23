/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTExportConfig;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTMheReference;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.MheReferenceRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ExportConfigDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_EXPORT_CONFIG;
  }
  
  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_EXPORT_CONFIG;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ExportConfigRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(!edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
    else
    {
      return IDocumentKeys.EXPORT_CONFIG_UPDATE;
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTExportConfig exportConfig = (IGTExportConfig)entity;
    ExportConfigAForm form = (ExportConfigAForm)actionContext.getActionForm();
    if(exportConfig.isNewEntity())
    {
      IGTMheReference exportableEntities = (IGTMheReference)exportConfig.getFieldValue(exportConfig.EXPORTABLE_ENTITIES);
      if (exportableEntities == null)
        throw new NullPointerException("exportableEntities is null");
      form.setExportableEntities(exportableEntities);
    }    
    else
    {
      String exportFile = exportConfig.getFieldString(exportConfig.EXPORT_FILE);
      form.setExportFile( exportFile );
    }
  }


  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ExportConfigAForm();
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    try
    {
      ExportConfigAForm form = (ExportConfigAForm)actionForm;
      IGTExportConfig exportConfig = (IGTExportConfig)entity;
      
      if( StaticUtils.stringArrayEmpty(form.getSelectedEntities(), true) )
      {
        EntityFieldValidator.addFieldError( errors,
                                            "selectedEntities",
                                            "exportConfig",
                                            EntityFieldValidator.REQUIRED,
                                            null);
      }                                
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating exportConfig",t);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTSession gtasSession = getGridTalkSession(actionContext);
    ExportConfigAForm form = (ExportConfigAForm)actionContext.getActionForm();
    IGTExportConfig exportConfig = (IGTExportConfig)entity;
    
    if(exportConfig.isNewEntity())
    {
      String[] selectedEntities = form.getSelectedEntities();
      if(selectedEntities == null) selectedEntities = StaticUtils.EMPTY_STRING_ARRAY;
      try
      {
        IGTMheReference mheRef = MheReferenceRenderer.getMheReference(selectedEntities, gtasSession);
        exportConfig.setFieldValue(exportConfig.SELECTED_ENTITIES, mheRef);
      }
      catch(Throwable t)
      {
        throw new GTClientException("An unexpected error occured while translating request parameters to MheReference",t);
      }
    }
  }
  
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  { //nb: we return to the view after saving so user can download the file
    super.saveWithManager(actionContext, manager, entity);
    initialiseActionForm(actionContext, entity);
    setReturnToView(actionContext, true);
  }

}