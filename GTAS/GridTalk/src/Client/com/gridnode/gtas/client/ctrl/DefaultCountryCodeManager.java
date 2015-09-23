/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCountryCodeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.locale.GetCountryCodeListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;


class DefaultCountryCodeManager extends DefaultAbstractManager
  implements IGTCountryCodeManager
{
  DefaultCountryCodeManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_COUNTRY_CODE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Country codes may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Country codes may not be created");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_COUNTRY_CODE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_COUNTRY_CODE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new java.lang.UnsupportedOperationException("Country codes do not have a uid");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetCountryCodeListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("Country codes may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_COUNTRY_CODE.equals(entityType))
    {
      return new DefaultCountryCodeEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }
}