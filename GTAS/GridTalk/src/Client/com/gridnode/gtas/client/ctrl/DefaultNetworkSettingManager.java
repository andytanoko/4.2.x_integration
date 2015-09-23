/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultNetworkSettingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.connection.GetNetworkSettingEvent;
import com.gridnode.gtas.events.connection.SaveNetworkSettingEvent;
import com.gridnode.gtas.model.connection.INetworkSetting;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultNetworkSettingManager extends DefaultAbstractManager
  implements IGTNetworkSettingManager
{

  DefaultNetworkSettingManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_NETWORK_SETTING, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTNetworkSettingEntity nws = (IGTNetworkSettingEntity)entity;
      Map nwsMap = new HashMap();

      Short connectionLevel = (Short)nws.getFieldValue(IGTNetworkSettingEntity.CONNECTION_LEVEL);
      String localJmsRouter = nws.getFieldString(IGTNetworkSettingEntity.LOCAL_JMS_ROUTER);
      String httpProxyServer = nws.getFieldString(IGTNetworkSettingEntity.HTTP_PROXY_SERVER);
      Integer httpProxyPort = (Integer)nws.getFieldValue(IGTNetworkSettingEntity.HTTP_PROXY_PORT);
      String proxyAuthUser = nws.getFieldString(IGTNetworkSettingEntity.PROXY_AUTH_USER);
      String proxyAuthPassword = nws.getFieldString(IGTNetworkSettingEntity.PROXY_AUTH_PASSWORD);
      Integer keepAliveInterval = (Integer)nws.getFieldValue(IGTNetworkSettingEntity.KEEP_ALIVE_INTERVAL);
      Integer responseTimeout = (Integer)nws.getFieldValue(IGTNetworkSettingEntity.RESPONSE_TIMEOUT);

      nwsMap.put(INetworkSetting.CONNECTION_LEVEL, connectionLevel);
      nwsMap.put(INetworkSetting.LOCAL_JMS_ROUTER, localJmsRouter);
      nwsMap.put(INetworkSetting.HTTP_PROXY_SERVER, httpProxyServer);
      nwsMap.put(INetworkSetting.HTTP_PROXY_PORT, httpProxyPort);
      nwsMap.put(INetworkSetting.PROXY_AUTH_USER, proxyAuthUser);
      nwsMap.put(INetworkSetting.PROXY_AUTH_PASSWORD, proxyAuthPassword);
      nwsMap.put(INetworkSetting.KEEP_ALIVE_INTERVAL, keepAliveInterval);
      nwsMap.put(INetworkSetting.RESPONSE_TIMEOUT, responseTimeout);

//System.out.println("[DEBUG] DefaultNetworkSettingManager.doUpdate(): INetworkSetting.KEEP_ALIVE_INTERVAL=" + INetworkSetting.KEEP_ALIVE_INTERVAL);
//System.out.println("[DEBUG] DefaultNetworkSettingManager.doUpdate(): keepAliveInterval=" + keepAliveInterval);
//System.out.println("[DEBUG] DefaultNetworkSettingManager.doUpdate(): keepAliveInterval.getClass().getName()=" + keepAliveInterval.getClass().getName());

      SaveNetworkSettingEvent event = new SaveNetworkSettingEvent(nwsMap);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating networkSetting",t);
    }
  }


  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new NotApplicableException("Create is not applicable to NetworkSetting");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_NETWORK_SETTING;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_NETWORK_SETTING;
  }

  public IGTNetworkSettingEntity getNetworkSetting() throws GTClientException
  {
    try
    {
      return (IGTNetworkSettingEntity)handleGetEvent(getGetEvent(null));
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving networkSetting entity",t);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(uid != null)
    {
      throw new java.lang.IllegalArgumentException("NetworkSetting may not be referenced by uid");
    }
    return new GetNetworkSettingEvent();
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new NotApplicableException("NetworkSetting does not have associated list events");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("NetworkSetting entity may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_NETWORK_SETTING.equals(entityType))
    {
      return new DefaultNetworkSettingEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }
}