/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupResultDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-02     Andrew Hill         Created
 * 2002-11-14     Andrew Hill         Security Password Support
 * 2002-11-20     Andrew Hill         Recognise error 1507 (INVALID_SEC_PWD_ERROR)
 * 2002-12-02     Andrew Hill         Check IGTSession.isRegistered() first
 * 2003-11-05     Andrew Hill         noSecurity support for GNDB00016109
 */

package com.gridnode.gtas.client.web.connection;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTConnectionSetupParamEntity;
import com.gridnode.gtas.client.ctrl.IGTConnectionSetupResultEntity;
import com.gridnode.gtas.client.ctrl.IGTConnectionSetupResultManager;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.ctrl.StaticCtrlUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.exceptions.IErrorCode;

public class ConnectionSetupResultDispatchAction extends EntityDispatchAction2
{
  static final String GRIDMASTER_LIST       = "GridMasterList";
  static final String ROUTER_LIST           = "RouterList";
  static final String RETURN_TO_UPDATE_KEY  = "ReturnToUpdate";
  static final String INVALID_PW_MSG        = "Invalid Security password";

  public ActionForward update(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  { //20021202AH
    IGTSession gtasSession = getGridTalkSession(request);
    if(gtasSession.isRegistered())
    { // If we have registered gtas then proceed as normal
      return super.update(mapping,actionForm,request,response);
    }
    else
    { // Otherwise redirect to the rego screen!
      return mapping.findForward("register");
    }
  }


  protected String getEntityName()
  {
    return IGTEntity.ENTITY_CONNECTION_SETUP_RESULT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ConnectionSetupResultRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return edit ? IDocumentKeys.CONNECTION_SETUP_RESULT_UPDATE : IDocumentKeys.CONNECTION_SETUP_RESULT_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)actionContext.getActionForm();
    IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)entity;
    IGTConnectionSetupParamEntity param = (IGTConnectionSetupParamEntity)
                                          result.getFieldValue(IGTConnectionSetupResultEntity.SETUP_PARAMETERS);


    form.setCurrentLocation( param.getFieldString(IGTConnectionSetupParamEntity.CURRENT_LOCATION) );
    form.setServicingRouter( param.getFieldString(IGTConnectionSetupParamEntity.SERVICING_ROUTER) );
    form.setOriginalLocation( param.getFieldString(IGTConnectionSetupParamEntity.ORIGINAL_LOCATION) );
    form.setOriginalServicingRouter( param.getFieldString(IGTConnectionSetupParamEntity.ORIGINAL_SERVICING_ROUTER) );

    form.setStatus( result.getFieldString(IGTConnectionSetupResultEntity.STATUS));
    form.setFailureReason( result.getFieldString(IGTConnectionSetupResultEntity.FAILURE_REASON) );

    prepLists(actionContext,false);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ConnectionSetupResultAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_CONNECTION_SETUP_RESULT;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    ConnectionSetupResultAForm rForm = (ConnectionSetupResultAForm)form;
    IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)entity;
    IGTConnectionSetupParamEntity param = (IGTConnectionSetupParamEntity)
                                          result.getFieldValue(IGTConnectionSetupResultEntity.SETUP_PARAMETERS);
    Short status = rForm.getStatusShort();
    if( IGTConnectionSetupResultEntity.STATUS_FAILURE.equals(status) || IGTConnectionSetupResultEntity.STATUS_NOT_DONE.equals(status) )
    {
      if(result.getSession().isNoSecurity() )
      { //20031105AH
        rForm.setSecurityPassword(IGTSession.PCP_DEFAULT);
      }
      
      
      basicValidateString(actionErrors, IGTConnectionSetupParamEntity.CURRENT_LOCATION, rForm, param);
      basicValidateString(actionErrors, IGTConnectionSetupParamEntity.SERVICING_ROUTER, rForm, param);
      basicValidateString(actionErrors, IGTConnectionSetupParamEntity.SECURITY_PASSWORD, rForm, param);
    }
    else if(IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status) )
    {
      // Only the order can be changed.
      // We leave uid validation to b-tier
    }
    else
    {
      actionErrors.add("status",new ActionError("connectionSetupResult.error.status.invalid"));
    }
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)actionContext.getActionForm();
    IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)entity;
    IGTConnectionSetupParamEntity param = (IGTConnectionSetupParamEntity)
                                          result.getFieldValue(IGTConnectionSetupResultEntity.SETUP_PARAMETERS);

    Short status = form.getStatusShort();
    if( IGTConnectionSetupResultEntity.STATUS_FAILURE.equals(status) || IGTConnectionSetupResultEntity.STATUS_NOT_DONE.equals(status) )
    {
      param.setFieldValue(IGTConnectionSetupParamEntity.ORIGINAL_LOCATION, form.getOriginalLocation());
      param.setFieldValue(IGTConnectionSetupParamEntity.SERVICING_ROUTER, form.getServicingRouter());
      param.setFieldValue(IGTConnectionSetupParamEntity.SECURITY_PASSWORD, form.getSecurityPassword());
    }
    else if(IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status) )
    {
      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      List gridMasterList = (List)opCon.getAttribute(GRIDMASTER_LIST);
      List routerList = (List)opCon.getAttribute(ROUTER_LIST);
      Collection gridMasterUids = StaticCtrlUtils.getUids(gridMasterList);
      Collection routerUids = StaticCtrlUtils.getUids(routerList);
      result.setFieldValue(IGTConnectionSetupResultEntity.AVAILABLE_GRIDMASTERS, gridMasterUids);
      result.setFieldValue(IGTConnectionSetupResultEntity.AVAILABLE_ROUTERS, routerUids);
    }
    else
    {
      throw new java.lang.IllegalStateException("Illegal STATUS for connectionSetupResult:" + status);
    }
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    IGTConnectionSetupResultEntity entity = null;
    IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTConnectionSetupResultManager manager = (IGTConnectionSetupResultManager)
                                        gtasSession.getManager(IGTManager.MANAGER_CONNECTION_SETUP_RESULT);
    entity = manager.getConnectionSetupResult();
    if(entity == null)
    {
      throw new GTClientException("No connectionSetupResult entity to update");
    }

    opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

  protected void arrangeLists(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)actionContext.getActionForm();
      //IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)getEntity(actionContext);
      Short status = form.getStatusShort();
      if(IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status))
      {
        OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
        List gridMasterList = (List)opCon.getAttribute(GRIDMASTER_LIST);
        if(gridMasterList == null) throw new NullPointerException("gridMasterList (inOpCon"); //20030422AH
        List routerList = (List)opCon.getAttribute(ROUTER_LIST);
        if(routerList == null) throw new NullPointerException("routerList (in opCon)"); //20030422AH


        StaticUtils.arrangeList(gridMasterList,form.getAvailableGridMastersOrder() );
        StaticUtils.arrangeList(routerList,form.getAvailableRoutersOrder() );

        form.setAvailableGridMastersOrder( StaticUtils.initialOrderString(gridMasterList.size()) );
        form.setAvailableRoutersOrder( StaticUtils.initialOrderString(routerList.size()) );
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing order of availableRouters and availableGridMasters",t);
    }
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeLists(actionContext);
  }

  protected void performPreSaveProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeLists(actionContext);
  }

  protected String performPreDivertProcessing(ActionContext actionContext, String divertMappingName)
    throws GTClientException
  {
    arrangeLists(actionContext);
    return divertMappingName;
  }

  protected void prepLists(ActionContext actionContext, boolean reloaded) throws GTClientException
  {
    try
    {
      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      List gridMasterList = (List)opCon.getAttribute(GRIDMASTER_LIST);
      if(reloaded || (gridMasterList == null))
      {
        IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)getEntity(actionContext);
        gridMasterList =  (List)result.getFieldEntities(IGTConnectionSetupResultEntity.AVAILABLE_GRIDMASTERS);
        List routerList = (List)result.getFieldEntities(IGTConnectionSetupResultEntity.AVAILABLE_ROUTERS);
        if (gridMasterList == null)
					throw new NullPointerException("gridMasterList is null"); //20030422AH
        if (routerList == null)
					throw new NullPointerException("routerList is null"); //20030422AH
        opCon.setAttribute(GRIDMASTER_LIST,gridMasterList);
        opCon.setAttribute(ROUTER_LIST,routerList);

        ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)actionContext.getActionForm();
        String initRoutersOrder = StaticUtils.initialOrderString( routerList.size() );
        String initMastersOrder = StaticUtils.initialOrderString( gridMasterList.size() );
        form.setAvailableGridMastersOrder(initMastersOrder);
        form.setAvailableRoutersOrder(initRoutersOrder);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error prepping GridMaster and Router lists in OperationContext",t);
    }
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    Short status = (Short)entity.getFieldValue(IGTConnectionSetupResultEntity.STATUS);
    boolean returnToUpdate = !(IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status));
    actionContext.setAttribute(RETURN_TO_UPDATE_KEY,new Boolean(returnToUpdate));
    manager.update(entity);
    if(returnToUpdate)
    {
      initialiseActionForm(actionContext,entity);
      prepLists(actionContext,true); //yuck (we are calling it twice this way - but it works)
    }
  }

  protected boolean isReturnToUpdate(ActionContext actionContext)
    throws GTClientException
  {
    Boolean returnToUpdate = (Boolean)actionContext.getAttribute(RETURN_TO_UPDATE_KEY);
    return returnToUpdate.booleanValue();
  }

  public ActionForward restoreSettings(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws GTClientException
  {
    //@todo: use javaScript to do this (havent time to work out the code for setting the select
    //field now)
    try
    {
      ConnectionSetupResultAForm form = (ConnectionSetupResultAForm)actionForm;
      form.setCurrentLocation( form.getOriginalLocation() );
      form.setServicingRouter( form.getOriginalServicingRouter() );
      return update(mapping,actionForm,request,response);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error restoring original connectionSetup settings",t);
    }
  }

  protected String getErrorField(Throwable t)
  {
    if( (t != null) && (t instanceof ResponseException) )
    {
      if( ((ResponseException)t).getErrorCode() == IErrorCode.INVALID_SEC_PWD_ERROR )
      {
        return "securityPassword";
      }
    }

    /*if( (t != null) && (t instanceof ResponseException) )
    {

      if( (((ResponseException)t).getErrorCode() == IErrorCode.CONNECTION_SETUP_ERROR)
          &&  (((ResponseException)t).getMessage().indexOf(INVALID_PW_MSG) != -1) )
      {
        return "securityPassword";
      }
    }*/
    return super.getErrorField(t);
  }

  protected ActionError getActionError(ResponseException t)
  {
    if( (t != null) && (t instanceof ResponseException) )
    {
      if( ((ResponseException)t).getErrorCode() == IErrorCode.INVALID_SEC_PWD_ERROR )
      {
        return new ActionError("registrationInfo.error.securityPassword.invalid");
      }
    }

    /*if( (e != null))
    {
      if( (e.getErrorCode() == IErrorCode.CONNECTION_SETUP_ERROR)
          && (e.getMessage().indexOf(INVALID_PW_MSG) != -1) )
      {
        return new ActionError("registrationInfo.error.securityPassword.invalid");
      }
    }*/
    return super.getActionError(t);
  }
}