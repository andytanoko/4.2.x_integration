/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPartnerTypeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 19 2002    Daniel D'Cotta      Created
 * 2002-07-28     ANdrew Hill         Refactored to extend from AbstractDefaultManager
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.partner.CreatePartnerTypeEvent;
import com.gridnode.gtas.events.partner.DeletePartnerTypeEvent;
import com.gridnode.gtas.events.partner.GetPartnerTypeEvent;
import com.gridnode.gtas.events.partner.GetPartnerTypeListEvent;
import com.gridnode.gtas.events.partner.UpdatePartnerTypeEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultPartnerTypeManager extends DefaultAbstractManager
  implements IGTPartnerTypeManager
{
  DefaultPartnerTypeManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_PARTNER_TYPE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      UpdatePartnerTypeEvent event = new UpdatePartnerTypeEvent(
                                         (Long)entity.getFieldValue(IGTPartnerTypeEntity.UID),
                                         entity.getFieldString(IGTPartnerTypeEntity.DESCRIPTION));
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
      CreatePartnerTypeEvent event = new CreatePartnerTypeEvent(
                                         entity.getFieldString(IGTPartnerTypeEntity.PARTNER_TYPE),
                                         entity.getFieldString(IGTPartnerTypeEntity.DESCRIPTION));
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PARTNER_TYPE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PARTNER_TYPE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPartnerTypeEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter == null)
    {
      return new GetPartnerTypeListEvent();
    }
    else
    {
      return new GetPartnerTypeListEvent(filter);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePartnerTypeEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultPartnerTypeEntity();
  }


}