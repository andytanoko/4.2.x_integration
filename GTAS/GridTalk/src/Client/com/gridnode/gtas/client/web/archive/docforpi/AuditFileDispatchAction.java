package com.gridnode.gtas.client.web.archive.docforpi;

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: AuditFileDispatchAction.java
 * 
 * ***************************************************************************
 * * 	Date 					Author 									Changes
 * *************************************************************************** 
 *  12 Oct 2005 		Sumedh Chalermkanjana 	Created
 */

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.document.GridDocumentAForm;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.ctrl.*;

public class AuditFileDispatchAction extends EntityDispatchAction2
{
	public static final String EDIT_NOT_SUPPORT_ERROR_MESSAGE = "AuditFileDispatchAction: editing is NOT supported.";
	
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_AUDIT_FILE;
	}

	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new AuditFileRenderer(rContext, edit);
	}

	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		if (edit) 
		{ 
			throw new UnsupportedOperationException(EDIT_NOT_SUPPORT_ERROR_MESSAGE); 
		}
		return IDocumentKeys.AUDIT_FILE_VIEW;
	}

	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		IGTAuditFileEntity instance = (IGTAuditFileEntity) entity;

		AuditFileAForm form = (AuditFileAForm) actionContext.getActionForm();
		
		form.setFilename(instance.getFieldString(IGTAuditFileEntity.FILENAME));
		form.setDocNumber(instance.getFieldString(IGTAuditFileEntity.DOC_NO));
		form.setDocType(instance.getFieldString(IGTAuditFileEntity.DOC_TYPE));
		form.setPartnerID(instance.getFieldString(IGTAuditFileEntity.PARTNER_ID));
		form.setPartnerDuns(instance.getFieldString(IGTAuditFileEntity.PARTNER_DUNS));
		form.setPartnerName(instance.getFieldString(IGTAuditFileEntity.PARTNER_NAME));
		form.setDateTimeCreate(instance.getFieldString(IGTAuditFileEntity.DATE_CREATED));
		form.setPreamble(instance.getFieldString(IGTAuditFileEntity.PREAMBLE));
		form.setDeliveryHeader(instance.getFieldString(IGTAuditFileEntity.DELIVERY_HEADER));
		form.setServiceHeader(instance.getFieldString(IGTAuditFileEntity.SERVICE_HEADER));
		form.setServiceContent(instance.getFieldString(IGTAuditFileEntity.SERVICE_CONTENT));
	}

	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new AuditFileAForm();
	}

	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_AUDIT_FILE;
	}

	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm actionForm,
																		ActionErrors errors) throws GTClientException
	{
		throw new UnsupportedOperationException(EDIT_NOT_SUPPORT_ERROR_MESSAGE);
	}

	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		throw new UnsupportedOperationException(EDIT_NOT_SUPPORT_ERROR_MESSAGE);
	}
  
  protected ActionForward getDivertForward( ActionContext actionContext, OperationContext opCon,
                                           ActionMapping mapping, String divertMapping)                                           throws GTClientException
  { 
    if ("divertCancel".equals(divertMapping))
    {
      try
      {
        //AuditFileAForm form = (AuditFileAForm) actionContext.getActionForm();
        //String processInstanceUid = form.get;
        //if (StaticUtils.stringEmpty(processInstanceUid)) { throw new IllegalStateException("No processInstanceUid defined in action form"); }

        //IGTSession gtasSession = getGridTalkSession(actionContext);
        //IGTGridDocumentManager mgr = (IGTGridDocumentManager) gtasSession.getManager(IGTManager.MANAGER_GRID_DOCUMENT);
        //IGTFieldMetaInfo fmi = mgr.getSharedFieldMetaInfo(IGTEntity.ENTITY_AUDIT_FILE,IGTGridDocumentEntity.PROCESS_INSTANCE_UID);
        //IForeignEntityConstraint constraint = (IForeignEntityConstraint) fmi.getConstraint();

        ActionForward forward = mapping.findForward(divertMapping);
        String path = forward.getPath();
        path = StaticWebUtils.addParameterToURL(path, "keyField", "uid");
        path = StaticWebUtils.addParameterToURL(path, "uid", "15487");
        forward = new ActionForward(path, forward.getRedirect());

        return forward;
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error preparing forward path for cancel diversion",t);
      }
    }
    else
    {
      return super.getDivertForward(actionContext, opCon, mapping, divertMapping);
    }
  }
  
  /*
   * public ActionForward abort(ActionMapping mapping, ActionForm actionForm,
   * HttpServletRequest request, HttpServletResponse response) throws
   * IOException, ServletException, GTClientException { try { ActionForward
   * forward = mapping.findForward("resumeView"); ActionContext actionContext =
   * new ActionContext(mapping,actionForm,request,response); String uid =
   * actionContext.getRequest().getParameter("uid"); String url =
   * StaticWebUtils.addParameterToURL(forward.getPath(), "uid", uid); forward =
   * new ActionForward(url, forward.getRedirect()); String[] uids = null;
   * 
   * OperationContext opCon = OperationContext.getOperationContext(request);
   * if(opCon != null) { OperationContext previousOC =
   * opCon.popOperationContext(); if(previousOC != null) {
   * OperationContext.saveOperationContext(previousOC, request); forward =
   * processSOCForward( previousOC.getResumeForward(), previousOC ); uids =
   * request.getParameterValues("fuid"); } } else { uids =
   * request.getParameterValues("uid"); } if( (uids == null) || (uids.length ==
   * 0) ) { return forward; } IGTSession gtasSession =
   * getGridTalkSession(actionContext); IGTAuditFileManager manager =
   * (IGTAuditFileManager)gtasSession.getManager(IGTManager.MANAGER_AUDIT_FILE);
   * Collection audit = StaticUtils.getLongCollection(uids);
   * manager.abort(audit, "User Cancelled"); return forward; } catch(Throwable
   * t) { throw new GTClientException("Error aborting ProcessInstances
   * entitities",t); } }
   * 
   * public void doDelete(ActionContext actionContext) throws GTClientException {
   * //20030709AH - Modified to be doDelete() method //20030724AH - Corrected
   * method signature to override correct method! (oops), remove try/catch
   * String[] uids = getDeleteIds(actionContext);
   * 
   * if( (uids == null) || (uids.length == 0) ) { return; } IGTSession
   * gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
   * IGTAuditFileManager manager =
   * (IGTAuditFileManager)gtasSession.getManager(IGTManager.MANAGER_AUDIT_FILE);
   * Collection audit = StaticUtils.getLongCollection(uids);
   * manager.delete(audit, Boolean.FALSE); }
   */
}
// public void doDelete(ActionContext actionContext) throws GTClientException
//	{ // 20030709AH - Modified to be doDelete() method
//		// 20030724AH - Corrected method signature to override correct method!
//		// (oops), remove try/catch
//		String[] uids = getDeleteIds(actionContext); // 20030310AH
//
//		if ((uids == null) || (uids.length == 0)) { return; }
//		IGTSession gtasSession = getGridTalkSession(actionContext.getRequest()
//				.getSession());
//		IGTProcessInstanceManager manager = (IGTProcessInstanceManager) gtasSession
//				.getManager(IGTManager.MANAGER_PROCESS_INSTANCE);
//		Collection processes = StaticUtils.getLongCollection(uids);
//		manager.delete(processes, Boolean.FALSE);
//	}

//	protected ActionForward getDivertForward(	ActionContext actionContext,
//																						OperationContext opCon,
//																						ActionMapping mapping,
//																						String divertTo) throws GTClientException
//	{
//		String fuid = actionContext.getRequest().getParameter("singleFuid");
//		ActionForward divertForward = mapping.findForward(divertTo);
//		if (divertForward == null) { throw new GTClientException(
//																															"No mapping found for "
//																																	+ divertTo); }
//		if ("divertViewGridDocument".equals(divertTo))
//		{
//
//			divertForward = new ActionForward(StaticWebUtils
//					.addParameterToURL(divertForward.getPath(), "uid", fuid),
//																				divertForward.getRedirect());
//
//		}
//		return processSOCForward(divertForward, opCon);
//	}