/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportConfigDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-06-02     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTExportConfigManager;
import com.gridnode.gtas.client.ctrl.IGTImportConfig;
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

public class ImportConfigDispatchAction extends EntityDispatchAction2
{
  private static final String _fileExt[] = 
  { //Valid file extensions for importFile
    "zip",
  };
  
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_IMPORT_CONFIG;
  }
  
  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  { //Import and Export config share the same manager in order to ah... umm
    //'reduce code duplication'. Yeh thats it. 'reduce code duplication'.
    return IGTManager.MANAGER_EXPORT_CONFIG;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ImportConfigRenderer(rContext, edit);
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
      return IDocumentKeys.IMPORT_CONFIG_UPDATE;
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTImportConfig importConfig = (IGTImportConfig)entity;
    ImportConfigAForm form = (ImportConfigAForm)actionContext.getActionForm();
    
    IGTMheReference conflictingEntities = getConflictingEntities(importConfig);
    form.setConflictingEntities(conflictingEntities);
    form.setIsOverwrite( importConfig.getFieldString(importConfig.IS_OVERWRITE) );
  }


  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ImportConfigAForm();
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    try
    {
      ImportConfigAForm form = (ImportConfigAForm)actionForm;
      IGTImportConfig importConfig = (IGTImportConfig)entity;
      
      IGTMheReference conflictingEntities = getConflictingEntities(importConfig);
      if(conflictingEntities == null)
      {
        EntityFieldValidator.basicValidateFiles(errors, importConfig.IMPORT_FILE, form, importConfig, _fileExt);
        basicValidateString(errors, importConfig.IS_OVERWRITE, form, importConfig);
      }
      else
      {
        //Its not necessary to specify any overwrite records - the user may wish to leave all
        //those existing ones intact.
        
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating importConfig",t);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ImportConfigAForm form = (ImportConfigAForm)actionContext.getActionForm();
    IGTImportConfig importConfig = (IGTImportConfig)entity;
    
    IGTMheReference conflictingEntities = getConflictingEntities(importConfig);                    
    if(conflictingEntities == null)
    { 
      //We havent tried to import yet
      importConfig.setFieldValue(importConfig.IS_OVERWRITE, StaticUtils.booleanValue(form.getIsOverwrite())) ;
      transferFieldFiles(actionContext, importConfig, importConfig.IMPORT_FILE, false);
    }
    else
    {
      //We had conflicts - so we showed the manual overwrite thinghy - now we need to set in
      //the list of stuff to overwrite
      IGTSession gtasSession = getGridTalkSession(actionContext);
      IGTMheReference overwriteEntities = MheReferenceRenderer.getMheReference(form.getOverwriteEntities(), gtasSession);
      if (overwriteEntities == null)
        throw new NullPointerException("overWriteEntities is null");
      importConfig.setFieldValue(importConfig.OVERWRITE_ENTITIES, overwriteEntities);
    }
  }
  
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    IGTImportConfig importConfig = (IGTImportConfig)entity;
    super.saveWithManager(actionContext, manager, importConfig);
    IGTMheReference conflictingEntities = getConflictingEntities(importConfig);
    if(conflictingEntities != null)
    {
      initialiseActionForm(actionContext, entity);
      setReturnToView(actionContext, true);
    }
  }

  private IGTMheReference getConflictingEntities(IGTImportConfig importConfig)
    throws GTClientException
  {
    IGTMheReference conflicts = (IGTMheReference)importConfig.getFieldValue(importConfig.CONFLICTING_ENTITIES);
    return conflicts;
  }

  protected IGTEntity getNewEntityInstance( ActionContext actionContext,
                                            IGTManager manager)
    throws GTClientException
  { //nb: ImportConfig isnt the (nominal!) primary entity for the exportConfig manager so we
    //dont use the default behaviour of calling newEntity() but rather call the getImportConfig() method
    IGTExportConfigManager exportConfigMgr = (IGTExportConfigManager)manager;
    return exportConfigMgr.getImportConfig();
  }

}