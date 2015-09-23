/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFileTypeEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class FileTypeDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_FILE_TYPE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new FileTypeRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.FILE_TYPE_UPDATE : IDocumentKeys.FILE_TYPE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTFileTypeEntity fileType = (IGTFileTypeEntity)entity;
    FileTypeAForm form = (FileTypeAForm)actionContext.getActionForm();
    form.setDescription( fileType.getFieldString(IGTFileTypeEntity.DESCRIPTION) );
    form.setFileType( fileType.getFieldString(IGTFileTypeEntity.FILE_TYPE) );
    form.setParameters( fileType.getFieldString(IGTFileTypeEntity.PARAMETERS) );
    form.setProgramName( fileType.getFieldString(IGTFileTypeEntity.PROGRAM_NAME) );
    form.setProgramPath( fileType.getFieldString(IGTFileTypeEntity.PROGRAM_PATH) );
    form.setWorkingDirectory( fileType.getFieldString(IGTFileTypeEntity.WORKING_DIR) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new FileTypeAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_FILE_TYPE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTFileTypeEntity fileType = (IGTFileTypeEntity)entity;
    basicValidateString(errors, IGTFileTypeEntity.FILE_TYPE, form, entity);
    basicValidateString(errors, IGTFileTypeEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTFileTypeEntity.PROGRAM_NAME, form, entity);
    basicValidateString(errors, IGTFileTypeEntity.PROGRAM_PATH, form, entity);
    basicValidateString(errors, IGTFileTypeEntity.PARAMETERS, form, entity);
    basicValidateString(errors, IGTFileTypeEntity.WORKING_DIR, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    FileTypeAForm form = (FileTypeAForm)actionContext.getActionForm();
    IGTFileTypeEntity fileType = (IGTFileTypeEntity)entity;

    fileType.setFieldValue(IGTFileTypeEntity.FILE_TYPE, form.getFileType() );
    fileType.setFieldValue(IGTFileTypeEntity.DESCRIPTION, form.getDescription() );
    fileType.setFieldValue(IGTFileTypeEntity.PROGRAM_NAME, form.getProgramName() );
    fileType.setFieldValue(IGTFileTypeEntity.PROGRAM_PATH, form.getProgramPath() );
    fileType.setFieldValue(IGTFileTypeEntity.PARAMETERS, form.getParameters() );
    fileType.setFieldValue(IGTFileTypeEntity.WORKING_DIR, form.getWorkingDirectory() );
  }
}