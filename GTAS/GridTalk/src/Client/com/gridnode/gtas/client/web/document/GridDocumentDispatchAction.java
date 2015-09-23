/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-26     Andrew Hill         Created
 * 2002-12-05     Andrew Hill         Attachments
 * 2002-12-31     Daniel D'Cotta      Commented out attachments
 * 2003-08-21     Andrew Hill         Support for processInstanceId and userTrackingIdentifier fields
 * 2003-08-25     Andrew Hill         Support for processInsanceUid
 * 2003-10-15     Daniel D'Cotta      Added manual export
 * 2009-03-18     Ong Eu Soon         Add Audit File Name, Receipt Audit File Name & Doc Transaction Status
 */
package com.gridnode.gtas.client.web.document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class GridDocumentDispatchAction extends EntityDispatchAction2
{
  protected static final Number[] _fields = new Number[]{
    IGTGridDocumentEntity.G_DOC_ID,
    IGTGridDocumentEntity.G_DOC_FILENAME,
    IGTGridDocumentEntity.FOLDER,
    IGTGridDocumentEntity.U_DOC_NUM,
    IGTGridDocumentEntity.U_DOC_FILENAME,
    IGTGridDocumentEntity.U_DOC_VERSION,
    IGTGridDocumentEntity.U_DOC_DOC_TYPE,
    IGTGridDocumentEntity.U_DOC_FILE_TYPE,
    IGTGridDocumentEntity.U_DOC_FILESIZE,
    IGTGridDocumentEntity.S_G_DOC_ID,
    IGTGridDocumentEntity.R_G_DOC_ID,
    IGTGridDocumentEntity.REF_G_DOC_ID,
    IGTGridDocumentEntity.REF_U_DOC_NUM,
    IGTGridDocumentEntity.REF_U_DOC_FILENAME,
    IGTGridDocumentEntity.SOURCE_FOLDER,
    IGTGridDocumentEntity.PARTNER_FUNCTION,
    IGTGridDocumentEntity.S_NODE_ID,
    IGTGridDocumentEntity.S_BIZ_ENTITY_ID,
    IGTGridDocumentEntity.S_PARTNER_ID,
    IGTGridDocumentEntity.S_PARTNER_NAME,
    IGTGridDocumentEntity.S_PARTNER_TYPE,
    IGTGridDocumentEntity.S_PARTNER_GROUP,
    IGTGridDocumentEntity.S_USER_ID,
    IGTGridDocumentEntity.S_USER_NAME,
    IGTGridDocumentEntity.R_NODE_ID,
    IGTGridDocumentEntity.R_BIZ_ENTITY_ID,
    IGTGridDocumentEntity.R_PARTNER_ID,
    IGTGridDocumentEntity.R_PARTNER_NAME,
    IGTGridDocumentEntity.R_PARTNER_TYPE,
    IGTGridDocumentEntity.R_PARTNER_GROUP,
    IGTGridDocumentEntity.DT_IMPORT,
    IGTGridDocumentEntity.DT_SEND_END,
    IGTGridDocumentEntity.DT_TRANSACTION_COMPLETE,
    IGTGridDocumentEntity.DT_RECEIVE_END,
    IGTGridDocumentEntity.DT_EXPORT,
    IGTGridDocumentEntity.DT_VIEW,
    IGTGridDocumentEntity.R_CHANNEL_NAME,
    IGTGridDocumentEntity.ENCRYPTION_LEVEL,
    IGTGridDocumentEntity.S_ROUTE,
    IGTGridDocumentEntity.PORT_NAME,
    IGTGridDocumentEntity.HAS_ATTACHMENT,
    IGTGridDocumentEntity.PROCESS_INSTANCE_ID, //20030821AH
    IGTGridDocumentEntity.USER_TRACKING_IDENTIFIER, //20030821AH
    IGTGridDocumentEntity.PROCESS_INSTANCE_UID, //20030825AH
    IGTGridDocumentEntity.AUDIT_FILE_NAME,
    IGTGridDocumentEntity.DOC_TRANS_STATUS,
    IGTGridDocumentEntity.RECEIPT_AUDIT_FILE_NAME,
  };

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_GRID_DOCUMENT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new GridDocumentRenderer(rContext,edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    if(edit)
    {
      //20050322AH - assert that the user is an admin for edit mode      
      IGTSession gtasSession = getGridTalkSession(actionContext);
      if(!gtasSession.isAdmin())
      {
        throw new IllegalStateException("not an admin user");
      }
      //.....
      
      
      return IDocumentKeys.GRID_DOCUMENT_UPDATE;
    }
    else
    {
      return IDocumentKeys.GRID_DOCUMENT_VIEW;
    }
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    GridDocumentAForm form = (GridDocumentAForm)actionContext.getActionForm();
    IGTGridDocumentEntity doc = (IGTGridDocumentEntity)entity;
      
    Object piUid = doc.getFieldValue(IGTGridDocumentEntity.PROCESS_INSTANCE_UID);
    
    
    initFormFields(actionContext, _fields);

    form.setAttachmentFilenames(doc.getFieldStringArray(doc.ATTACHMENT_FILENAMES));

  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new GridDocumentAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_GRID_DOCUMENT;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //20030123AH
    basicValidateFiles(errors,IGTGridDocumentEntity.U_DOC_FILENAME,form,entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    GridDocumentAForm form = (GridDocumentAForm)actionContext.getActionForm();
    IGTGridDocumentEntity gridDocument = (IGTGridDocumentEntity)entity;

    transferFieldFiles(actionContext, gridDocument, gridDocument.U_DOC_FILENAME, true);
  }

  public ActionForward send(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws Exception
  {
    try
    {
      ActionForward forward = mapping.findForward("listview");
      String url = StaticWebUtils.addParameterToURL(forward.getPath(),"folder","import");
      forward = new ActionForward(url, forward.getRedirect());
      String[] uids = null;

      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        OperationContext previousOC = opCon.popOperationContext();
        if(previousOC != null)
        {
          OperationContext.saveOperationContext(previousOC, request);
          forward = processSOCForward( previousOC.getResumeForward(), previousOC );
          uids = request.getParameterValues("fuid");
        }
      }
      else
      {
        uids = request.getParameterValues("uid");
      }
      //..........

      if( (uids == null) || (uids.length == 0) )
      {
        return forward;
      }
      ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTGridDocumentManager manager = (IGTGridDocumentManager)gtasSession.getManager(IGTManager.MANAGER_GRID_DOCUMENT);
      long[] gridDocs = StaticUtils.primitiveLongArrayValue(uids);
      manager.send(gridDocs);
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error sending documents",t);
    }
  }

  public ActionForward export(ActionMapping mapping, ActionForm actionForm,
                              HttpServletRequest request, HttpServletResponse response)
                              throws Exception
  {
    try
    {
      ActionForward forward = mapping.findForward("listview");
      String url = StaticWebUtils.addParameterToURL(forward.getPath(), "folder", "export"); // redirect it to export folder
      forward = new ActionForward(url, forward.getRedirect());
      String[] uids = null;

      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        OperationContext previousOC = opCon.popOperationContext();
        if(previousOC != null)
        {
          OperationContext.saveOperationContext(previousOC, request);
          forward = processSOCForward( previousOC.getResumeForward(), previousOC );
          uids = request.getParameterValues("fuid");
        }
      }
      else
      {
        uids = request.getParameterValues("uid");
      }
      
      if( (uids == null) || (uids.length == 0) )
      {
        return forward;
      }
      ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTGridDocumentManager manager = (IGTGridDocumentManager)gtasSession.getManager(IGTManager.MANAGER_GRID_DOCUMENT);
      long[] gridDocs = StaticUtils.primitiveLongArrayValue(uids);
      manager.export(gridDocs);
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting documents", t);
    }
  }

  protected ActionForward getDivertForward(
    ActionContext actionContext,
    OperationContext opCon,
    ActionMapping mapping,
    String divertMapping)
    throws GTClientException
  { //20030822AH
    if("divertViewProcessInstance".equals(divertMapping))
    {
      //Once the bfpr can auto-render view diversions this code will be obsolete
      try
      {
        GridDocumentAForm form = (GridDocumentAForm)actionContext.getActionForm();
        String processInstanceUid = form.getProcessInstanceUid(); //20030825AH - Use uid instead of id
        if(StaticUtils.stringEmpty(processInstanceUid))
        {
          throw new IllegalStateException("No processInstanceUid defined in action form");
        }

        IGTSession gtasSession = getGridTalkSession(actionContext);
        IGTGridDocumentManager mgr = (IGTGridDocumentManager)gtasSession.getManager(IGTManager.MANAGER_GRID_DOCUMENT);
        IGTFieldMetaInfo fmi = mgr.getSharedFieldMetaInfo(IGTEntity.ENTITY_GRID_DOCUMENT, IGTGridDocumentEntity.PROCESS_INSTANCE_UID);
        IForeignEntityConstraint constraint = (IForeignEntityConstraint)fmi.getConstraint();

        ActionForward forward = mapping.findForward(divertMapping);
        String path = forward.getPath();
        path = StaticWebUtils.addParameterToURL(path, "keyField", constraint.getKeyFieldName());
        path = StaticWebUtils.addParameterToURL(path, constraint.getKeyFieldName(), processInstanceUid );
        forward = new ActionForward(path, forward.getRedirect());

        return forward;
      }
      catch(Throwable t)
      {
        throw new GTClientException("Error preparing forward path for processInstance diversion",t);
      }
    }
    else
    {
      return super.getDivertForward(actionContext, opCon, mapping, divertMapping);
    }
  }
}