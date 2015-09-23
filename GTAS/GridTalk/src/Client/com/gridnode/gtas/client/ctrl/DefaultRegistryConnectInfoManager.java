/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultRegistryConnectInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.bizreg.CreateRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.DeleteRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoListEvent;
import com.gridnode.gtas.events.bizreg.UpdateRegistryConnectInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultRegistryConnectInfoManager extends DefaultAbstractManager
  implements IGTRegistryConnectInfoManager
{
  DefaultRegistryConnectInfoManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_REGISTRY_CONNECT_INFO, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTRegistryConnectInfoEntity registryConnectInfo = (IGTRegistryConnectInfoEntity)entity;

      Long   uid        = (Long)registryConnectInfo.getFieldValue (IGTRegistryConnectInfoEntity.UID);
      String queryUrl   =       registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.QUERY_URL);
      String publishUrl =       registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_URL);
      String username   =       registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_USER);
      String password   =       registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD);
      
      UpdateRegistryConnectInfoEvent event = new UpdateRegistryConnectInfoEvent(uid, queryUrl, publishUrl, username, password);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTRegistryConnectInfoEntity registryConnectInfo = (IGTRegistryConnectInfoEntity)entity;

      String name       = registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.NAME);
      String queryUrl   = registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.QUERY_URL);
      String publishUrl = registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_URL);
      String username   = registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_USER);
      String password   = registryConnectInfo.getFieldString(IGTRegistryConnectInfoEntity.PUBLISH_PASSWORD);
      
      CreateRegistryConnectInfoEvent event = new CreateRegistryConnectInfoEvent(name, queryUrl, publishUrl, username, password);
     
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }    
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_REGISTRY_CONNECT_INFO;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_REGISTRY_CONNECT_INFO;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetRegistryConnectInfoEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter == null)
    {
      return new GetRegistryConnectInfoListEvent();
    }
    else
    {
      return new GetRegistryConnectInfoListEvent(filter);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    return new DeleteRegistryConnectInfoEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultRegistryConnectInfoEntity();
  }
}