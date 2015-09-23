/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-15     Andrew Hill         Created
 * 2002-10-03     Daniel              Modify upload method
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class MappingFileDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";

  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_MAPPING_FILE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new MappingFileRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.MAPPING_FILE_UPDATE : IDocumentKeys.MAPPING_FILE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    //@todo: use type value passed from listview
    IGTMappingFileEntity mappingFile = (IGTMappingFileEntity)entity;
    MappingFileAForm form = (MappingFileAForm)actionContext.getActionForm();
    form.setName(mappingFile.getFieldString(mappingFile.NAME));
    form.setDescription(mappingFile.getFieldString(mappingFile.DESCRIPTION));
    form.setFilename(mappingFile.getFieldString(mappingFile.FILENAME));
    form.setType(mappingFile.getFieldString(mappingFile.TYPE));
    form.setSubPath(mappingFile.getFieldString(mappingFile.SUB_PATH));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new MappingFileAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_MAPPING_FILE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTSession gtasSession = getGridTalkSession(actionContext);
    MappingFileAForm mappingFileAForm = (MappingFileAForm)form;
    IGTMappingFileEntity mappingFile = (IGTMappingFileEntity)entity;

    basicValidateString(errors, IGTMappingFileEntity.NAME, mappingFileAForm, mappingFile);
    basicValidateString(errors, IGTMappingFileEntity.DESCRIPTION, mappingFileAForm, mappingFile);
    basicValidateString(errors, IGTMappingFileEntity.TYPE, mappingFileAForm, mappingFile);
    basicValidateString(errors, IGTMappingFileEntity.SUB_PATH, mappingFileAForm, mappingFile);

    // Sanity check to see that type can convert to a short.
    try
    {
      new Short(mappingFileAForm.getType());
    }
    catch(Exception e)
    {
      errors.add("type",new ActionError("mappingFile.error.type.invalid"));
    }
    basicValidateFiles(errors, IGTMappingFileEntity.FILENAME, mappingFileAForm, mappingFile);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    MappingFileAForm form = (MappingFileAForm)actionContext.getActionForm();
    IGTMappingFileEntity mappingFile = (IGTMappingFileEntity)entity;

    mappingFile.setFieldValue(IGTMappingFileEntity.NAME, form.getName());
    mappingFile.setFieldValue(IGTMappingFileEntity.DESCRIPTION, form.getDescription());
    mappingFile.setFieldValue(IGTMappingFileEntity.TYPE, new Short(form.getType()));
    mappingFile.setFieldValue(IGTMappingFileEntity.SUB_PATH, form.getSubPath());
    
    // 03102002 DDJ: Changed to use the EntityDispatchAction.transferFieldFiles()
    transferFieldFiles(actionContext, mappingFile, IGTMappingFileEntity.FILENAME, true);
  }

  protected ActionForward performSaveForwardProcessing(ActionContext actionContext,
                                                        OperationContext completedOpCon,
                                                       ActionForward forward)
    throws GTClientException
  {
    if(completedOpCon.getPreviousContext() == null)
    { //20021126AH - Only append type if we didnt divert here
      return appendType(actionContext, completedOpCon, forward);
    }
    else
    {
      return forward;
    }
  }

  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  {
    ActionForward forward = super.getCancelForward(actionContext);
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    return appendType(actionContext, opCon, forward);
  }

  private ActionForward appendType(ActionContext actionContext,
                                                   OperationContext opCon,
                                                   ActionForward forward)
    throws GTClientException
  {
    MappingFileAForm form = (MappingFileAForm)opCon.getActionForm();
    String path = forward.getPath();
    path = StaticWebUtils.addParameterToURL(path,"type",form.getType());
    forward = new ActionForward(path, true);
    return forward;
  }

/* 20031210 DDJ: Commented out, probablely used for testing
  public ActionForward bob(ActionMapping mapping,
                                ActionForm actionForm,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);

    Log log = LogFactory.getLog(this.getClass());
    log.info("In bob");

    MappingFileAForm form = (MappingFileAForm)actionForm;
    if(form == null) throw new NullPointerException("Null form");

    IGTSession gtasSession = getGridTalkSession(request);
    if(gtasSession == null) throw new NullPointerException("Null gtasSession");
    IGTMappingFileManager mfMgr = (IGTMappingFileManager)gtasSession.getManager(IGTManager.MANAGER_MAPPING_FILE);
    IGTMappingFileEntity mf = (IGTMappingFileEntity)StaticUtils.getFirst( mfMgr.getByKey("bob",IGTMappingFileEntity.NAME) );
    if(mf == null) throw new NullPointerException("mf is null");
    transferFieldFiles(actionContext, mf, mf.FILENAME, true);
    mfMgr.update(mf);
    return null;
  }
*/
}