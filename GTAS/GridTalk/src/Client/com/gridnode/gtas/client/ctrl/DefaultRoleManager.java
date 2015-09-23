/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultRoleManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2006-05-08     Neo Sok Lay         GNDB00027030: hide User Administrator role
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Arrays;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.acl.AddRoleToUserEvent;
import com.gridnode.gtas.events.acl.CreateRoleEvent;
import com.gridnode.gtas.events.acl.DeleteRoleEvent;
import com.gridnode.gtas.events.acl.GetRoleEvent;
import com.gridnode.gtas.events.acl.GetRoleListEvent;
import com.gridnode.gtas.events.acl.GetRoleListForUserEvent;
import com.gridnode.gtas.events.acl.RemoveRoleFromUserEvent;
import com.gridnode.gtas.events.acl.UpdateRoleEvent;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;


class DefaultRoleManager extends DefaultAbstractManager
  implements IGTRoleManager
{
	private Long[] _excludeUids = {-3L};
	
  DefaultRoleManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ROLE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      UpdateRoleEvent event = new UpdateRoleEvent(
                                        (Long)entity.getFieldValue(IGTRoleEntity.UID),
                                        entity.getFieldString(IGTRoleEntity.ROLE),
                                        entity.getFieldString(IGTRoleEntity.DESCRIPTION));

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
      CreateRoleEvent event = new CreateRoleEvent(
                                        entity.getFieldString(IGTRoleEntity.ROLE),
                                        entity.getFieldString(IGTRoleEntity.DESCRIPTION),
                                        true);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  /**
   * Convenience method
   */
  public IGTRoleEntity getRoleByUID(long uid) throws GTClientException
  {
    return (IGTRoleEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ROLE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ROLE;
  }

  public Collection getRolesForUser(IGTUserEntity user) throws GTClientException
  {
    try
    {
      GetRoleListForUserEvent event = new GetRoleListForUserEvent(
                                            (Long)user.getFieldValue(IGTUserEntity.UID));
      return handleGetListEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error getting roles for user",e);
    }
  }

  public void addRoleToUser(long roleUid, long userUid) throws GTClientException
  {
    try
    {
      AddRoleToUserEvent event = new AddRoleToUserEvent(new Long(userUid),new Long(roleUid));
      handleEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error adding role to user",e);
    }
  }

  public void addRoleToUser(IGTRoleEntity role, IGTUserEntity user) throws GTClientException
  {
    try
    {
      AddRoleToUserEvent event = new AddRoleToUserEvent(
                                        (Long)user.getFieldValue(IGTUserEntity.UID),
                                        (Long)role.getFieldValue(IGTRoleEntity.UID));
      handleEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error adding role to user",e);
    }
  }

  public void removeRoleFromUser(long roleUid, long userUid) throws GTClientException
  {
    try
    {
      RemoveRoleFromUserEvent event = new RemoveRoleFromUserEvent(
                                            new Long(userUid),new Long(roleUid));
      handleEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error removing role from user",e);
    }
  }

  public void removeRoleFromUser(IGTRoleEntity role, IGTUserEntity user)
    throws GTClientException
  {
    try
    {
      RemoveRoleFromUserEvent event = new RemoveRoleFromUserEvent(
                                        (Long)user.getFieldValue(IGTUserEntity.UID),
                                        (Long)role.getFieldValue(IGTRoleEntity.UID));
      handleEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error removing role from user",e);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetRoleEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
  	//NSL20060508 Hide User Administrator role
  	FilterConnector conn = null;
  	if (filter == null)
  	{
  		filter = new DataFilterImpl();
  	}
  	else
  	{
  		conn = filter.getAndConnector();
  	}
  	filter.addDomainFilter(conn, IGTRoleEntity.UID, Arrays.asList(_excludeUids), true);
  	
    return new GetRoleListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  { //20030718AH
    return new DeleteRoleEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultRoleEntity();
  }

}