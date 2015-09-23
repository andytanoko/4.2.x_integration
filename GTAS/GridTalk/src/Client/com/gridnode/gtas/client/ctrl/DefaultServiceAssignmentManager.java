/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultServiceAssignmentManager.java
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

class DefaultServiceAssignmentManager extends DefaultAbstractManager
  implements IGTServiceAssignmentManager
{
  DefaultServiceAssignmentManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_SERVICE_ASSIGNMENT, session);
  }

  protected void doUpdate(IGTEntity entity) throws GTClientException
  {
    try
    {
      IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)entity;

      Long uid = serviceAssignment.getUidLong();
      String userType = IGTServiceAssignmentEntity.PARTNER_TYPE;//serviceAssignment.getFieldString(serviceAssignment.USER_TYPE);
      String password = serviceAssignment.getFieldString(IGTServiceAssignmentEntity.PASSWORD);
      Collection wsdlUIds = (Collection)serviceAssignment.getFieldValue(IGTServiceAssignmentEntity.WEBSERVICE_UIDS);
      IEvent event = new UpdateServiceAssignmentEvent(uid,password,userType,wsdlUIds);

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
      IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)entity;
      String userName = serviceAssignment.getFieldString(IGTServiceAssignmentEntity.USER_NAME);
      String userType = IGTServiceAssignmentEntity.PARTNER_TYPE;//serviceAssignment.getFieldString(serviceAssignment.USER_TYPE);
      String password = serviceAssignment.getFieldString(IGTServiceAssignmentEntity.PASSWORD);
      Collection wsdlUIds = (Collection)serviceAssignment.getFieldValue(IGTServiceAssignmentEntity.WEBSERVICE_UIDS);

      IEvent event = new CreateServiceAssignmentEvent(userName,password,userType,wsdlUIds);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_SERVICE_ASSIGNMENT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SERVICE_ASSIGNMENT;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetServiceAssignmentEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetServiceAssignmentListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    return new DeleteServiceAssignmentEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_SERVICE_ASSIGNMENT.equals(entityType))
    {
      return new DefaultServiceAssignmentEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

}