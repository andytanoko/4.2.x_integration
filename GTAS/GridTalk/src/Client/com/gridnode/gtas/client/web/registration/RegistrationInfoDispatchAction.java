/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationInfoDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-27     Andrew Hill         Created
 * 2002-10-28     Andrew Hill         getField for invalid rego error
 * 2003-03-26     Andrew Hill         Only flag rmoc confirm if not done already
 * 2003-04-14     Andrew Hill         Use constants for password length checking
 * 2003-04-16     Andrew Hill         NodeLock changes
 * 2003-04-22     Andrew Hill         Call update from view if unregistered
 * 2003-04-24     Andrew Hill         Choose field for displaying 7003 error
 * 2003-05-02     Andrew Hill         Fix bugs with expired registrations
 * 2003-11-05     Andrew Hill         noSecurity hack for GNDB00016109
 * 2006-06-22     Tam Wei Xiang       GNDB00026893 : modified getActionError(.)
 *                                    and getErrorField(.) to handle the error
 *                                    IErrorCode.LICENSE_EXPIRED_ERROR
 */

package com.gridnode.gtas.client.web.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTCompanyProfileEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRegistrationInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTRegistrationInfoManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.gridnode.CompanyProfileDispatchAction;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.exceptions.IErrorCode;

public class RegistrationInfoDispatchAction extends EntityDispatchAction2
{
  public static final String[] LICENSE_FILE_EXTENSIONS = null; //20030416AH

  public static final int MIN_PASS_LEN = 6; //20030414AH
  public static final int MAX_PASS_LEN = 12; //20030414AH


  protected void flagRmocConfirmation(ActionContext actionContext,
                                      OperationContext opCon,
                                      boolean isUpdateMode)
    throws GTClientException
  { //20030326AH - Only flag rmoc if not done yet
    if(isUpdateMode)
    {
      RegistrationInfoAForm rForm = (RegistrationInfoAForm)opCon.getActionForm();
      Short state = StaticUtils.shortValue( rForm.getRegistrationState() );
      if(IGTRegistrationInfoEntity.REG_STATE_REG.equals(state)) return;
    }
    super.flagRmocConfirmation(actionContext, opCon, isUpdateMode);

  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_REGISTRATION_INFO;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new RegistrationInfoRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return edit ? IDocumentKeys.REGISTRATION_INFO_UPDATE : IDocumentKeys.REGISTRATION_INFO_VIEW; //20030417AH
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    RegistrationInfoAForm form = (RegistrationInfoAForm)actionContext.getActionForm();
    IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)entity;

    form.setGridnodeId( rInfo.getFieldString(rInfo.GRIDNODE_ID) );
    form.setGridnodeName( rInfo.getFieldString(rInfo.GRIDNODE_NAME) );
    form.setCategory( rInfo.getFieldString(rInfo.CATEGORY) );
    form.setBizConnections( rInfo.getFieldString(rInfo.BIZ_CONNECTIONS) );
    form.setProdKeyF1( rInfo.getFieldString(rInfo.PRODUCT_KEY_F1) );
    form.setProdKeyF2( rInfo.getFieldString(rInfo.PRODUCT_KEY_F2) );
    form.setProdKeyF3( rInfo.getFieldString(rInfo.PRODUCT_KEY_F3) );
    form.setProdKeyF4( rInfo.getFieldString(rInfo.PRODUCT_KEY_F4) );
    form.setLicStartDate( rInfo.getFieldString(rInfo.LIC_START_DATE) );
    form.setLicEndDate( rInfo.getFieldString(rInfo.LIC_END_DATE) );
    form.setRegistrationState( rInfo.getFieldString(rInfo.REG_STATE) );

    IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)rInfo.getFieldValue(rInfo.COMPANY_PROFILE);
    CompanyProfileDispatchAction.initialiseActionForm(form,profile);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new RegistrationInfoAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_REGISTRATION_INFO;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    RegistrationInfoAForm rForm = (RegistrationInfoAForm)form;
    IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)entity;
    IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)rInfo.getFieldValue(rInfo.COMPANY_PROFILE);

    Short state = StaticUtils.shortValue( rForm.getRegistrationState() );
    if(IGTRegistrationInfoEntity.REG_STATE_NOT_REG.equals(state))
    {
      basicValidateString(actionErrors, rInfo.GRIDNODE_ID, rForm, rInfo);
      basicValidateString(actionErrors, rInfo.GRIDNODE_NAME, rForm, rInfo);
      CompanyProfileDispatchAction.validateActionForm(profile,rForm,actionErrors);

      basicValidateFiles(actionErrors,rInfo.LICENSE_FILE,rForm,rInfo,LICENSE_FILE_EXTENSIONS); //20030416AH
    }
    else if(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS.equals(state))
    {
      if(rInfo.getSession().isNoSecurity())
      { //20031105AH - HACK for GNDB00016109 (garrrrh horrible)
        rForm.setSecurityPassword(IGTSession.PCP_DEFAULT);
        rForm.setConfirmPassword(IGTSession.PCP_DEFAULT);
      }
      
      
      basicValidateString(actionErrors, rInfo.SECURITY_PASSWORD, rForm, rInfo);
      basicValidateString(actionErrors, rInfo.CONFIRM_PASSWORD, rForm, rInfo);
      //"" + will waste a string concat to save me having to type a null check as well. naughty. ;-)
      String securityPass = "" + rForm.getSecurityPassword();
      String confirmPass = "" + rForm.getConfirmPassword();
      if(!securityPass.equals(rForm.getConfirmPassword()))
      {
        actionErrors.add("confirmPassword",new ActionError("registrationInfo.error.confirmPassword.mismatch"));
      }
      //following should be handled by validator & metaInfo. todo!
      if( (securityPass.length() < MIN_PASS_LEN)
        || (securityPass.length() > MAX_PASS_LEN) )
      {
        actionErrors.add("securityPassword",new ActionError("registrationInfo.error.securityPassword.invalid"));
      }
    }
    else if(IGTRegistrationInfoEntity.REG_STATE_REG.equals(state))
    {
      basicValidateFiles(actionErrors,rInfo.LICENSE_FILE,rForm,rInfo,LICENSE_FILE_EXTENSIONS); //20030416AH
    }
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)entity;
    IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)rInfo.getFieldValue(rInfo.COMPANY_PROFILE);
    RegistrationInfoAForm form = (RegistrationInfoAForm)actionContext.getActionForm();

    Short state = StaticUtils.shortValue( form.getRegistrationState() );
    if(IGTRegistrationInfoEntity.REG_STATE_NOT_REG.equals(state))
    {
      rInfo.setFieldValue( rInfo.GRIDNODE_ID, StaticUtils.integerValue(form.getGridnodeId()) );
      rInfo.setFieldValue( rInfo.GRIDNODE_NAME,   form.getGridnodeName() );
      rInfo.setFieldValue( rInfo.PRODUCT_KEY_F1,  form.getProdKeyF1() );
      rInfo.setFieldValue( rInfo.PRODUCT_KEY_F2,  form.getProdKeyF2() );
      rInfo.setFieldValue( rInfo.PRODUCT_KEY_F3,  form.getProdKeyF3() );
      rInfo.setFieldValue( rInfo.PRODUCT_KEY_F4,  form.getProdKeyF4() );
      CompanyProfileDispatchAction.updateEntityFields(form, profile);
      transferFieldFiles(actionContext,rInfo,rInfo.LICENSE_FILE,false); //20030416AH
    }
    else if(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS.equals(state))
    {
      rInfo.setFieldValue( rInfo.SECURITY_PASSWORD, form.getSecurityPassword() );
      rInfo.setFieldValue( rInfo.CONFIRM_PASSWORD, form.getConfirmPassword() );
    }
    else
    {
      transferFieldFiles(actionContext,rInfo,rInfo.LICENSE_FILE,false); //20030416AH
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    IGTRegistrationInfoEntity entity = null;
    IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTRegistrationInfoManager manager = (IGTRegistrationInfoManager)gtasSession.getManager(IGTManager.MANAGER_REGISTRATION_INFO);

    entity = manager.getRegistrationInfo();
    if(entity == null)
    {
      throw new GTClientException("No registrationInfo available to view/update");
    }

    opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

  protected void performCancelProcessing(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)getEntity(actionContext);
      Short state = (Short)rInfo.getFieldValue(rInfo.REG_STATE);
      if(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS.equals(state))
      {
        IGTSession gtasSession = getGridTalkSession(actionContext);
        IGTRegistrationInfoManager manager = (IGTRegistrationInfoManager)gtasSession.getManager(rInfo.getType());
        manager.cancelRegistration(rInfo);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error canceling registration",t);
    }
  }

  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)getEntity(actionContext);
      Short state = (Short)rInfo.getFieldValue(rInfo.REG_STATE);
      if(IGTRegistrationInfoEntity.REG_STATE_REG.equals(state))
      {
        return actionContext.getMapping().findForward("resumeView"); //20030417AH
      }
      else
      {
        return getResumeForward(actionContext,actionContext.getActionForm());
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting cancel ActionForward mapping",t);
    }
  }

  protected ActionForward getCompleteForward(ActionContext actionContext)
    throws GTClientException
  { //20030417AH
    IGTRegistrationInfoEntity rInfo = (IGTRegistrationInfoEntity)getEntity(actionContext);
    Short state = (Short)rInfo.getFieldValue(rInfo.REG_STATE);
    ActionForward forward = null;
    if(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS.equals(state))
    {
      forward = actionContext.getMapping().findForward("resumeView");
    }
    else if(IGTRegistrationInfoEntity.REG_STATE_EXPIRED.equals(state))
    { //20030502AH
      forward = actionContext.getMapping().findForward("resumeView");
    }
    else if(IGTRegistrationInfoEntity.REG_STATE_REG.equals(state))
    {
      forward = actionContext.getMapping().findForward("resumeView");
    }
    else
    {
      forward = getResumeForward(actionContext,actionContext.getActionForm());
    }
    return forward;
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
      manager.create(entity);
    }
    else
    {
      manager.update(entity);
    }
    initialiseActionForm(actionContext,entity);
  }

  protected String getErrorField(Throwable t)
  {
    if( (t != null) && (t instanceof ResponseException) )
    {
      if( ((ResponseException)t).getErrorCode() == IErrorCode.INVALID_REGISTRATION_ERROR )
      {
        return "licFile";
      }
      if( ((ResponseException)t).getErrorCode() == IErrorCode.INVALID_LICENSE_FILE_ERROR )
      { //20030424AH
        return "licFile";
      }
      if( ((ResponseException)t).getErrorCode() == IErrorCode.NODELOCK_ERROR )
      { //20030417AH
        return "licFile";
      }
      if( ((ResponseException)t).getErrorCode() == IErrorCode.LICENSE_EXPIRED_ERROR )
      { //TWX 22062006
        return "licFile";
      }
    }
    return super.getErrorField(t);
  }

  protected ActionError getActionError(ResponseException e)
  {
    if( (e != null) && (e.getErrorCode() == IErrorCode.INVALID_REGISTRATION_ERROR) )
    {
      return new ActionError("registrationInfo.error.registration.invalid"); //20030424AH
    }
    if( (e != null) && (e.getErrorCode() == IErrorCode.INVALID_LICENSE_FILE_ERROR) )
    { //20030424AH
      return new ActionError("registrationInfo.error.licFile.invalid");
    }
    if( (e != null) && (e.getErrorCode() == IErrorCode.NODELOCK_ERROR) )
    { //20030417AH
      return new ActionError("registrationInfo.error.licFile.nodelockError");
    }
    if( (e != null) && (e.getErrorCode() == IErrorCode.LICENSE_EXPIRED_ERROR))
    {
    	//TWX 22062006
    	return new ActionError("registrationInfo.error.licFile.licenseExpired");
    }
    return super.getActionError(e);
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  { //20030417AH
    //If there were any problems with the license file, trash it and make em upload again
    ActionErrors actionErrors = getActionErrors(actionContext.getRequest());
    ActionError licFileErr = MessageUtils.getFirstError(actionErrors,"licFile");
    if(licFileErr != null)
    {
      RegistrationInfoAForm form = (RegistrationInfoAForm)actionContext.getActionForm();
      form.clearLicFile();
    }
  }

  /**
   * The view method has been overridden to check if the product is registered and if not forward
   * call the update method to put the form into edit mode.
   * If registered, will call the superclass implementation to proceed as normal.
   */
  public ActionForward view(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  { //20030422AH
    try
    {
      IGTSession gtasSession = getGridTalkSession(request);
      if( gtasSession.isRegistered() )
      {
        return super.view( mapping, actionForm, request, response );
      }
      else
      {
        return update(mapping, actionForm, request, response);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in view method",t);
    }
  }

}