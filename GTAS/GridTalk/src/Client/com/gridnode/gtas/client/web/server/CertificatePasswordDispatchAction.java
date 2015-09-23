/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificatePasswordDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTCertificateManager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.registration.RegistrationInfoDispatchAction;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.TaskDispatchAction;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.exceptions.IErrorCode;

public class CertificatePasswordDispatchAction extends TaskDispatchAction
{
  protected IDocumentRenderer getFormRenderer(ActionContext actionContext, RenderingContext rContext, boolean edit) throws com.gridnode.gtas.client.GTClientException
  {
    return new CertificatePasswordRenderer(rContext);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new CertificatePasswordAForm();
  }

  protected boolean forceUseDvTemplate(ActionContext actionContext) throws GTClientException
  {
    return true;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return IDocumentKeys.CERTIFICATE_PASSWORD;
  }

  protected boolean validate(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    CertificatePasswordAForm form = (CertificatePasswordAForm)actionContext.getActionForm();

    String oldPassword = form.getOldPassword();
    String securityPassword = form.getSecurityPassword();
    String confirmPassword = form.getConfirmPassword();

    if(StaticUtils.stringEmpty(oldPassword))
    {
      errors.add("oldPassword",new ActionError("certificatePassword.error.oldPassword.required"));
    }
    if(StaticUtils.stringEmpty(securityPassword))
    {
      errors.add("securityPassword",new ActionError("certificatePassword.error.securityPassword.required"));
    }
    if(StaticUtils.stringEmpty(confirmPassword))
    {
      errors.add("confirmPassword",new ActionError("certificatePassword.error.confirmPassword.required"));
    }
    if(!StaticUtils.objectsEqual(securityPassword,confirmPassword))
    {
      errors.add("confirmPassword",new ActionError("certificatePassword.error.confirmPassword.mismatch"));
    }
    if(StaticUtils.objectsEqual(securityPassword,oldPassword))
    {
      errors.add("securityPassword",new ActionError("certificatePassword.error.securityPassword.equal"));
    }
    if(     (securityPassword.length() < RegistrationInfoDispatchAction.MIN_PASS_LEN)
        ||  (securityPassword.length() > RegistrationInfoDispatchAction.MAX_PASS_LEN) )
    {
      errors.add("securityPassword",new ActionError("registrationInfo.error.securityPassword.invalid"));
    }

    return (errors.empty());
  }

  public ActionForward save(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws IOException, ServletException, GTClientException
  {
    //(Im too lazy to rewrite the standard save button...)
    return complete(mapping,actionForm,request,response);
  }

  protected boolean doComplete(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    if(!validate(actionContext, errors)) return true;
    try
    {
      CertificatePasswordAForm form = (CertificatePasswordAForm)actionContext.getActionForm();
      IGTSession gtasSession = getGridTalkSession(actionContext);
      IGTCertificateManager certMgr = (IGTCertificateManager)gtasSession.getManager(IGTManager.MANAGER_CERTIFICATE);
      certMgr.changePrivateCertPassword( form.getOldPassword(), form.getSecurityPassword() );
      return false;
    }
    catch(Throwable t)
    {
      Throwable rootEx = StaticWebUtils.getRootException(t);
      if(rootEx instanceof ResponseException)
      {
        ActionError error = getActionError((ResponseException)rootEx);
        String field = getErrorField(rootEx);
        errors.add(field, error);
        return true;
      }
      else
      {
        throw new GTClientException("Error processing private certificate password change",t);
      }
    }
  }

  protected void initialiseActionForm(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  protected String getErrorField(Throwable t)
  {
    if( (t != null) && (t instanceof ResponseException) )
    {
      if( ((ResponseException)t).getErrorCode() == IErrorCode.INCORRECT_PRIVATE_CERT_PASSWORD )
      {
        return "oldPassword";
      }
    }
    return super.getErrorField(t);
  }

  protected ActionError getActionError(ResponseException e)
  {
    if( (e != null) && (e.getErrorCode() == IErrorCode.INCORRECT_PRIVATE_CERT_PASSWORD) )
    {
      return new ActionError("certificatePassword.error.oldPassword.invalid");
    }
    return super.getActionError(e);
  }
}