/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultWebServiceManager.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 6, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.servicemgmt.*;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultWebServiceManager extends DefaultAbstractManager
  implements IGTWebServiceManager
{
  DefaultWebServiceManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_WEBSERVICE, session);
  }

  protected void doUpdate(IGTEntity entity) throws GTClientException
  {
    try
    {
      IGTWebServiceEntity webService = (IGTWebServiceEntity)entity;

      Long uid = webService.getUidLong();
      String serviceGroup = IGTWebServiceEntity.INTERNAL_GROUP;//webService.getFieldString(webService.SEVICE_GROUP);
      String wsdlUrl = webService.getFieldString(IGTWebServiceEntity.WSDL_URL);
      String endPoint = webService.getFieldString(IGTWebServiceEntity.END_POINT);
      
      
      IEvent event = new UpdateWebServiceEvent(uid,wsdlUrl,endPoint,serviceGroup);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws GTClientException
  {
    try
    {
      IGTWebServiceEntity webService = (IGTWebServiceEntity)entity;
      
      String serviceName = webService.getFieldString(IGTWebServiceEntity.SERVICE_NAME);
      String serviceGroup = IGTWebServiceEntity.INTERNAL_GROUP;//webService.getFieldString(webService.SEVICE_GROUP);
      String wsdlUrl = webService.getFieldString(IGTWebServiceEntity.WSDL_URL);
      String endPoint = webService.getFieldString(IGTWebServiceEntity.END_POINT);

      IEvent event = new CreateWebServiceEvent(wsdlUrl,endPoint,serviceName,serviceGroup);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_WEBSERVICE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_WEBSERVICE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetWebServiceEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetWebServiceListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    return new DeleteWebServiceEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_WEBSERVICE.equals(entityType))
    {
      return new DefaultWebServiceEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

}