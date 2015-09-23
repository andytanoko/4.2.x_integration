/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertCategoryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-02-04     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.GetAlertCategoryEvent;
import com.gridnode.gtas.events.alert.GetAlertCategoryListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultAlertCategoryManager extends DefaultAbstractManager
  implements IGTAlertCategoryManager
{
  DefaultAlertCategoryManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ALERT_CATEGORY, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("AlertCategories may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("AlertCategories may not be created");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ALERT_CATEGORY;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ALERT_CATEGORY;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAlertCategoryEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAlertCategoryListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("AlertCategories may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ALERT_CATEGORY.equals(entityType))
    {
      return new DefaultAlertCategoryEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}