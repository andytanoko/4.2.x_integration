/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RestoreDocumentDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-22     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.archive;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class RestoreDocumentDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_RESTORE_DOCUMENT;
  }
  
  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_ARCHIVE_DOCUMENT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new RestoreDocumentRenderer(rContext, edit);
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
      return IDocumentKeys.RESTORE_DOCUMENT_UPDATE;
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    //RestoreDocumentAForm form = (RestoreDocumentAForm)actionContext.getActionForm();
    //IGTRestoreDocument restoreDocument = (IGTRestoreDocument)entity;
    //Nothing to do
  }


  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new RestoreDocumentAForm();
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    try
    {
      //IGTSession gtasSession = 
      getGridTalkSession(actionContext);
      IGTRestoreDocument restoreDocument = (IGTRestoreDocument)entity;
      RestoreDocumentAForm form = (RestoreDocumentAForm)actionForm;
      
      basicValidateFiles(errors, IGTRestoreDocument.ARCHIVE_FILE, form, restoreDocument);                                   
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating restoreDocument",t);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    //IGTSession gtasSession = 
	getGridTalkSession(actionContext);
    IGTRestoreDocument restoreDocument = (IGTRestoreDocument)entity;
    //RestoreDocumentAForm form = (RestoreDocumentAForm)actionContext.getActionForm();
   
    transferFieldFiles(actionContext, restoreDocument, IGTRestoreDocument.ARCHIVE_FILE, false );
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
  }
  
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    IGTArchiveDocumentManager archiveDocumentMgr = (IGTArchiveDocumentManager)manager;
    archiveDocumentMgr.restoreDocuments((IGTRestoreDocument)entity);
  }
  
  protected IGTEntity getNewEntityInstance( ActionContext actionContext,
                                            IGTManager manager)
    throws GTClientException
  { //20030522AH
    IGTArchiveDocumentManager archiveDocumentMgr = (IGTArchiveDocumentManager)manager;
    return archiveDocumentMgr.getRestoreDocument();
  }

}