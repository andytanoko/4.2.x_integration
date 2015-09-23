/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 */

package com.gridnode.gtas.client.web.connection;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class NetworkSettingDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_NETWORK_SETTING;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new NetworkSettingRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return edit ? IDocumentKeys.NETWORK_SETTING_UPDATE : IDocumentKeys.NETWORK_SETTING_VIEW;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    NetworkSettingAForm form = (NetworkSettingAForm)actionContext.getActionForm();
    IGTNetworkSettingEntity nws = (IGTNetworkSettingEntity)entity;

    form.setConnectionLevel( nws.getFieldString(IGTNetworkSettingEntity.CONNECTION_LEVEL));
    form.setLocalJmsRouter( nws.getFieldString(IGTNetworkSettingEntity.LOCAL_JMS_ROUTER));
    form.setHttpProxyServer( nws.getFieldString(IGTNetworkSettingEntity.HTTP_PROXY_SERVER));
    form.setHttpProxyPort( nws.getFieldString(IGTNetworkSettingEntity.HTTP_PROXY_PORT));
    form.setProxyAuthUser( nws.getFieldString(IGTNetworkSettingEntity.PROXY_AUTH_USER));
    form.setProxyAuthPassword(nws.getFieldString(IGTNetworkSettingEntity.PROXY_AUTH_PASSWORD));
    form.setKeepAliveInterval(nws.getFieldString(IGTNetworkSettingEntity.KEEP_ALIVE_INTERVAL));
    form.setResponseTimeout(nws.getFieldString(IGTNetworkSettingEntity.RESPONSE_TIMEOUT));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new NetworkSettingAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_NETWORK_SETTING;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    IGTNetworkSettingEntity nws = (IGTNetworkSettingEntity)entity;
    NetworkSettingAForm nwsForm = (NetworkSettingAForm)form;
    Short connectionLevel = nwsForm.getConnectionLevelShort();

    basicValidateString(actionErrors, IGTNetworkSettingEntity.CONNECTION_LEVEL, nwsForm, nws);
    basicValidateString(actionErrors, IGTNetworkSettingEntity.KEEP_ALIVE_INTERVAL, nwsForm, nws);
    basicValidateString(actionErrors, IGTNetworkSettingEntity.RESPONSE_TIMEOUT, nwsForm, nws);
    if(IGTNetworkSettingEntity.CONNECTION_LEVEL_NO_FIREWALL.equals(connectionLevel))
    {
      basicValidateString(actionErrors, IGTNetworkSettingEntity.LOCAL_JMS_ROUTER, nwsForm, nws);
    }
    else if(IGTNetworkSettingEntity.CONNECTION_LEVEL_PROXY_NO_AUTH.equals(connectionLevel))
    {
      basicValidateString(actionErrors, IGTNetworkSettingEntity.HTTP_PROXY_SERVER, nwsForm, nws);
      basicValidateString(actionErrors, IGTNetworkSettingEntity.HTTP_PROXY_PORT, nwsForm, nws);
    }
    else if(IGTNetworkSettingEntity.CONNECTION_LEVEL_PROXY_WITH_AUTH.equals(connectionLevel))
    {
      basicValidateString(actionErrors, IGTNetworkSettingEntity.HTTP_PROXY_SERVER, nwsForm, nws);
      basicValidateString(actionErrors, IGTNetworkSettingEntity.HTTP_PROXY_PORT, nwsForm, nws);
      basicValidateString(actionErrors, IGTNetworkSettingEntity.PROXY_AUTH_USER, nwsForm, nws);
      basicValidateString(actionErrors, IGTNetworkSettingEntity.PROXY_AUTH_PASSWORD, nwsForm, nws);
    }
    // (No extra stuff to validate for FIREWALL_NO_PROXY)
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    NetworkSettingAForm form = (NetworkSettingAForm)actionContext.getActionForm();
    IGTNetworkSettingEntity nws = (IGTNetworkSettingEntity)entity;

    nws.setFieldValue(IGTNetworkSettingEntity.CONNECTION_LEVEL, StaticUtils.shortValue(form.getConnectionLevel()) );
    nws.setFieldValue(IGTNetworkSettingEntity.LOCAL_JMS_ROUTER, form.getLocalJmsRouter() );
    nws.setFieldValue(IGTNetworkSettingEntity.HTTP_PROXY_SERVER, form.getHttpProxyServer() );
    nws.setFieldValue(IGTNetworkSettingEntity.HTTP_PROXY_PORT, StaticUtils.integerValue(form.getHttpProxyPort()) );
    nws.setFieldValue(IGTNetworkSettingEntity.PROXY_AUTH_USER, form.getProxyAuthUser() );
    nws.setFieldValue(IGTNetworkSettingEntity.PROXY_AUTH_PASSWORD, form.getProxyAuthPassword() );
    nws.setFieldValue(IGTNetworkSettingEntity.KEEP_ALIVE_INTERVAL, StaticUtils.integerValue(form.getKeepAliveInterval()) );
    nws.setFieldValue(IGTNetworkSettingEntity.RESPONSE_TIMEOUT, StaticUtils.integerValue(form.getResponseTimeout()) );
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    IGTNetworkSettingEntity entity = null;
    IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTNetworkSettingManager manager = (IGTNetworkSettingManager)gtasSession.getManager(IGTManager.MANAGER_NETWORK_SETTING);

    entity = manager.getNetworkSetting();
    if(entity == null)
    {
      throw new GTClientException("No networkSetting entity to update");
    }

    opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

}