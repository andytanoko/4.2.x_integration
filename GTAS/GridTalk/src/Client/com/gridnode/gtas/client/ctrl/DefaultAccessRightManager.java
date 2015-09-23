/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAccessRightManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 * 2002-07-05     Andrew Hill         Added getAccessRightsForRole() method
 * 2002-07-28     Andrew Hill         Refactored to extend DefaultAbstractManager
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.acl.AddAccessRightEvent;
import com.gridnode.gtas.events.acl.GetAccessRightEvent;
import com.gridnode.gtas.events.acl.GetAccessRightListEvent;
import com.gridnode.gtas.events.acl.ModifyAccessRightEvent;
import com.gridnode.gtas.events.acl.RemoveAccessRightEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;


class DefaultAccessRightManager extends DefaultAbstractManager
  implements IGTAccessRightManager
{
  DefaultAccessRightManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ACCESS_RIGHT, session);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ACCESS_RIGHT.equals(entityType))
    {
      return new DefaultAccessRightEntity();
    }
    else
    {
      throw new GTClientException("This manager cannot create entities of type:" + entityType);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      Long uid = (Long)entity.getFieldValue(IGTAccessRightEntity.UID);
      String description = entity.getFieldString(IGTAccessRightEntity.DESCRIPTION);
      String feature = entity.getFieldString(IGTAccessRightEntity.FEATURE);
      String dataType = entity.getFieldString(IGTAccessRightEntity.DATA_TYPE);
      String action = entity.getFieldString(IGTAccessRightEntity.ACTION);
      IDataFilter filter = (IDataFilter)entity.getFieldValue(IGTAccessRightEntity.CRITERIA);
      ModifyAccessRightEvent event = new ModifyAccessRightEvent(uid,
                                                                description,
                                                                feature,
                                                                action,
                                                                dataType,
                                                                filter);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      String description = entity.getFieldString(IGTAccessRightEntity.DESCRIPTION);
      Long roleUid = (Long)entity.getFieldValue(IGTAccessRightEntity.ROLE);
      String feature = entity.getFieldString(IGTAccessRightEntity.FEATURE);
      String action = entity.getFieldString(IGTAccessRightEntity.ACTION);
      String dataType = entity.getFieldString(IGTAccessRightEntity.DATA_TYPE);
      IDataFilter filter = null;

      AddAccessRightEvent event = null;
      if(dataType.equals(""))
      {
        event = new AddAccessRightEvent(description,
                                        roleUid,
                                        feature,
                                        action);
      }
      else
      {
        event = new AddAccessRightEvent(description,
                                        roleUid,
                                        feature,
                                        action,
                                        dataType,
                                        filter);
      }
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAccessRightEvent(uid);
  }

  protected IEvent getGetListEvent(IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAccessRightListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new RemoveAccessRightEvent(uids);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ACCESS_RIGHT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ACCESS_RIGHT;
  }

  public Collection getAccessRightsForRole(IGTRoleEntity role)
    throws GTClientException
  {

    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter( null,
                            IGTAccessRightEntity.ROLE,
                            filter.getEqualOperator(),
                            role.getFieldValue(IGTRoleEntity.UID),
                            false);
      return (Collection) handleGetListEvent( getGetListEvent(filter) );
    }
    catch(Exception e)
    {
      throw new GTClientException("Error getting list of accessrights for role uid="
                                  + role.getUid(),e);
    }
  }
}