/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServerDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-03     Andrew Hill         Created
 * 2002-12-20     Andrew Hill         startBackendListener()
 * 2003-06-02     Andrew Hill         Remove deprecated getNavGroup() method
 * 2003-07-04     Andrew Hill         Validate existence of password. (Also mod serverUtils.js for key handling)
 * 2003-11-05     Andrew Hill         noSecurity support for GNDB00016109
 */
package com.gridnode.gtas.client.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.strutsbase.TaskDispatchAction;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ServerDispatchAction extends TaskDispatchAction
{

  public ActionForward connect(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
    try
    {
      ServerAForm form = (ServerAForm)actionForm;
      
      IGTSession gtasSession = getGridTalkSession(request); //20031105AH
      if( gtasSession.isNoSecurity() )
      { //20031105AH - noSecurity evil hack for diabolical defect GNDB00016109
        form.setSecurityPassword(IGTSession.PCP_DEFAULT);
      }
      
      String password = form.getSecurityPassword();
      if(StaticUtils.stringEmpty(password))
      { //20030704AH - Basic validation
        ActionErrors actionErrors = new ActionErrors();
        EntityFieldValidator.addFieldError(actionErrors,
                                          "securityPassword",
                                          "server",
                                          EntityFieldValidator.REQUIRED,
                                          null);
        saveErrors(actionContext.getRequest(), actionErrors);
      }
      else
      {
        //20031105AH - co: IGTSession gtasSession = getGridTalkSession(request);
        gtasSession.connectToGridMaster(password);
      }
    }
    catch(Throwable t)
    {
      ActionErrors actionErrors = new ActionErrors();
      storeThrowableDetails(actionContext,actionErrors,t);
      saveErrors(actionContext.getRequest(),actionErrors);
    }
    return update(mapping,actionForm,request,response);
  }

  public ActionForward disconnect(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
    try
    {
      IGTSession gtasSession = getGridTalkSession(request);
      gtasSession.disconnectFromGridMaster();
    }
    catch(Throwable t)
    {
      ActionErrors actionErrors = new ActionErrors();
      storeThrowableDetails(actionContext,actionErrors,t);
      saveErrors(actionContext.getRequest(),actionErrors);
    }
    return update(mapping,actionForm,request,response);
  }

  public ActionForward startBackendListener(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws Exception
  { //20021220AH
    ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
    try
    {
      //ServerAForm form = (ServerAForm)actionForm;
      IGTSession gtasSession = getGridTalkSession(request);
      gtasSession.startBackendListener();
    }
    catch(Throwable t)
    {
      ActionErrors actionErrors = new ActionErrors();
      storeThrowableDetails(actionContext,actionErrors,t);
      saveErrors(actionContext.getRequest(),actionErrors);
    }
    return update(mapping,actionForm,request,response);
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext, RenderingContext rContext, boolean edit) throws com.gridnode.gtas.client.GTClientException
  {
    return new ServerRenderer(rContext);
  }

  protected ActionForm createActionForm(ActionContext actionContext) throws com.gridnode.gtas.client.GTClientException
  {
    return new ServerAForm();
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws com.gridnode.gtas.client.GTClientException
  {
    return IDocumentKeys.SERVER;
  }

  protected boolean validate(ActionContext actionContext, ActionErrors errors) throws com.gridnode.gtas.client.GTClientException
  {
    return false;
  }

  protected boolean doComplete(ActionContext actionContext, ActionErrors errors) throws com.gridnode.gtas.client.GTClientException
  {
    return true; //Always return to the screen!
  }

  protected void initialiseActionForm(ActionContext actionContext) throws com.gridnode.gtas.client.GTClientException
  {
    
  }

  protected void flagRmocConfirmation(ActionContext actionContext,
                                      OperationContext opCon,
                                      boolean isUpdateMode)
    throws GTClientException
  { //20030221AH - Dont bother with rmoc confirmation messages for the server screen (yet)
    //no-op
  }
}