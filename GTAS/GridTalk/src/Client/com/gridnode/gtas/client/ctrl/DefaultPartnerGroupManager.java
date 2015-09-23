/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPartnerGroupManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.partner.CreatePartnerGroupEvent;
import com.gridnode.gtas.events.partner.DeletePartnerGroupEvent;
import com.gridnode.gtas.events.partner.GetPartnerGroupEvent;
import com.gridnode.gtas.events.partner.GetPartnerGroupListEvent;
import com.gridnode.gtas.events.partner.UpdatePartnerGroupEvent;
import com.gridnode.gtas.model.partner.IPartnerGroup;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.EventException;


class DefaultPartnerGroupManager extends DefaultAbstractManager
  implements IGTPartnerGroupManager
{
  DefaultPartnerGroupManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PARTNER_GROUP, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      UpdatePartnerGroupEvent event = new UpdatePartnerGroupEvent(
                                            (Long)entity.getFieldValue(IGTPartnerGroupEntity.UID),
                                            entity.getFieldString(IGTPartnerGroupEntity.NAME),
                                            entity.getFieldString(IGTPartnerGroupEntity.DESCRIPTION));
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
      CreatePartnerGroupEvent event = new CreatePartnerGroupEvent(
                                      entity.getFieldString(IGTPartnerGroupEntity.NAME),
                                      entity.getFieldString(IGTPartnerGroupEntity.DESCRIPTION),
                                      (Long)entity.getFieldValue(IGTPartnerGroupEntity.PARTNER_TYPE));
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
  public IGTPartnerGroupEntity getPartnerGroupByUID(long uid) throws GTClientException
  {
    return (IGTPartnerGroupEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PARTNER_GROUP;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PARTNER_GROUP;
  }

  public IGTPartnerGroupEntity getPartnerGroupByName(String name)
    throws GTClientException
  {
    try
    {
      GetPartnerGroupEvent event = new GetPartnerGroupEvent(name);
      return (IGTPartnerGroupEntity)handleGetEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to get entity by uid", e);
    }
  }

  public Collection getPartnerGroupsForPartnerType(long partnerTypeUid)
    throws GTClientException
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter( null,
                              IPartnerGroup.PARTNER_TYPE,
                              filter.getEqualOperator(),
                              new Long(partnerTypeUid),
                              false);
      GetPartnerGroupListEvent event = new GetPartnerGroupListEvent(filter);
      return handleGetListEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error retrieving partner groups with partner type:"
                                  + partnerTypeUid,e);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPartnerGroupEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter == null)
    {
      return new GetPartnerGroupListEvent();
    }
    else
    {
      return new GetPartnerGroupListEvent(filter);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePartnerGroupEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultPartnerGroupEntity();
  }
}