/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertPwAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-23     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.login;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.GTSessionFactory;
import com.gridnode.gtas.client.ctrl.GlobalContext;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.web.xml.IDocumentManager;
import com.gridnode.gtas.exceptions.IErrorCode;

public class GetCertPwAction extends GTActionBase
{
  public static final String FWD_MAPPING = "getCertPassword";
  
  public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    ActionContext actionContext = new ActionContext(mapping,form,request,response);
    ActionErrors actionErrors = getActionErrors(request);
    if(actionErrors == null) actionErrors = new ActionErrors();
    String submittingFlag = request.getParameter("submitting");
    if("true".equals(submittingFlag))
    {
      String securityPassword = ((GetCertPwAForm)form).getSecurityPassword();
      if(StaticUtils.stringEmpty(securityPassword))
      {
        ActionError securityPasswordAErr = new ActionError("getCertPw.error.securityPassword.required");
        actionErrors.add("securityPassword",securityPasswordAErr);
      }
      else
      {
        try
        {
          GlobalContext globalContext = GlobalContext.getInstance();
          IGTSession gtasSession = GTSessionFactory.getSession(IGTSession.SESSION_DEFAULT, globalContext);
          gtasSession.setPrivateCertificatePassword(securityPassword);
System.out.println("setPrivateCertificatePassword returned without exception");
          return mapping.findForward("success");
        }
        catch(Throwable t)
        {
          ActionError error = null;
          Throwable rootEx = StaticWebUtils.getRootException(t);
          if(rootEx instanceof ResponseException)
          {
            if( ((ResponseException)rootEx).getErrorCode() == IErrorCode.INCORRECT_PRIVATE_CERT_PASSWORD )
            {
              error = new ActionError("getCertPw.error.securityPassword.invalid");
              actionErrors.add( "securityPassword", error );
            }  
          }
          if(error == null)
          {
            saveExceptions(actionContext, t);
            error = getActionErrorForThrowable(t);
            actionErrors.add( IGlobals.EXCEPTION_ERROR_PROPERTY, error );
          }
        }
      }
    }  
    saveErrors(request, actionErrors);  
    prepRenderers(actionContext);
    return mapping.findForward("view");
  }
  
  protected void prepRenderers(ActionContext actionContext) throws GTClientException
  {
    HttpServletRequest request = actionContext.getRequest();
    Locale locale = getLocale(request);
    IDocumentManager docMgr = getDocumentManager();
    if(docMgr == null) throw new java.lang.NullPointerException("No document manager found");
    MessageResources messageResources = getResources(request);
    ActionErrors actionErrors = getActionErrors(request);

    String submitUrl = getActionForwardPath(actionContext, "submit");
    if(submitUrl == null)
			throw new NullPointerException("submitUrl is null");
    String target = IDocumentKeys.GET_CERT_PW;

    ISimpleResourceLookup rLookup = new SimpleResourceLookup(locale, messageResources);

    IURLRewriter urlRewriter = new URLRewriterImpl( actionContext.getResponse(),
                                                    request.getContextPath());
    RenderingContext rContext = new RenderingContext( null,
                                                      docMgr,
                                                      rLookup,
                                                      urlRewriter,
                                                      actionErrors,
                                                      request,
                                                      getServlet().getServletContext() );
    BaseTagRenderer baseTagRenderer = new BaseTagRenderer(request, "base_tag");

    RenderingPipelineImpl pipeline = new RenderingPipelineImpl(target, docMgr);
    pipeline.addRenderer(baseTagRenderer);

    IDocumentRenderer pageRenderer = new GetCertPwRenderer(rContext,
                                                          (GetCertPwAForm)actionContext.getActionForm(),
                                                          submitUrl);
    pipeline.addRenderer(pageRenderer);

    addCommonContentRenderer(actionContext, rContext, pipeline);
    setRenderingPipeline(request, pipeline);
  }
}
