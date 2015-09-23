/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.connection;

import com.gridnode.gtas.client.ctrl.IGTNetworkSettingEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class NetworkSettingRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number[] _fields = {
    IGTNetworkSettingEntity.CONNECTION_LEVEL,
    IGTNetworkSettingEntity.KEEP_ALIVE_INTERVAL,
    IGTNetworkSettingEntity.RESPONSE_TIMEOUT,
  };

  public NetworkSettingRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      if(_edit) //@todo: remove once gtas is ready for the other options
      { //Temp hack as these features arent ready in gtas yet
        //only allow firewall sans proxy...
        replaceNodeWithNewElement("span","connectionLevel_value",false,false);
      }

      //RenderingContext rContext = getRenderingContext();
      NetworkSettingAForm form = (NetworkSettingAForm)getActionForm();
      IGTNetworkSettingEntity nws = (IGTNetworkSettingEntity)getEntity();
      renderCommonFormElements(nws.getType(),_edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null, nws, _fields);
      renderLabel("minutes_1","generic.minutes",false);
      renderLabel("minutes_2","generic.minutes",false);
      Short connectionLevel = form.getConnectionLevelShort();
      if(IGTNetworkSettingEntity.CONNECTION_LEVEL_NO_FIREWALL.equals(connectionLevel))
      {
        renderField(bfpr, nws, IGTNetworkSettingEntity.LOCAL_JMS_ROUTER);
        removeNode("proxy_section",false);
        renderLabel("router_heading","networkSetting.router.heading",false);
      }
      else if(IGTNetworkSettingEntity.CONNECTION_LEVEL_FIREWALL_NO_PROXY.equals(connectionLevel))
      {
        removeNode("router_section",false);
        removeNode("proxy_section",false);
      }
      else if(IGTNetworkSettingEntity.CONNECTION_LEVEL_PROXY_NO_AUTH.equals(connectionLevel))
      {
        renderField(bfpr, nws, IGTNetworkSettingEntity.HTTP_PROXY_SERVER);
        renderField(bfpr, nws, IGTNetworkSettingEntity.HTTP_PROXY_PORT);
        removeNode("proxyAuth_section",false);
        removeNode("router_section",false);
        renderLabel("proxy_heading","networkSetting.proxy.heading",false);
      }
      else if(IGTNetworkSettingEntity.CONNECTION_LEVEL_PROXY_WITH_AUTH.equals(connectionLevel))
      {
        renderField(bfpr, nws, IGTNetworkSettingEntity.HTTP_PROXY_SERVER);
        renderField(bfpr, nws, IGTNetworkSettingEntity.HTTP_PROXY_PORT);
        renderField(bfpr, nws, IGTNetworkSettingEntity.PROXY_AUTH_USER);
        renderField(bfpr, nws, IGTNetworkSettingEntity.PROXY_AUTH_PASSWORD);
        removeNode("router_section",false);
        renderLabel("proxy_heading","networkSetting.proxyAuth.heading",false);
      }
      else
      {
        // No connectionLevel selected yet.
        removeNode("router_section",false);
        removeNode("proxy_section",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering networkSetting screen",t);
    }
  }
}

