/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultFeatureManager.java
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
import com.gridnode.gtas.events.acl.GetFeatureEvent;
import com.gridnode.gtas.events.acl.GetFeatureListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;


class DefaultFeatureManager extends DefaultAbstractManager
  implements IGTFeatureManager
{
  DefaultFeatureManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_FEATURE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Features may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Features may not be created");
  }

  public IGTFeatureEntity getFeatureByFeature(String feature) throws GTClientException
  {
    try
    {
      GetFeatureEvent event = new GetFeatureEvent(feature);
      return (IGTFeatureEntity)handleGetEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to get feature by feature", e);
    }
  }

  /**
   * Convenience method
   */
  public IGTFeatureEntity getFeatureByUID(long uid) throws GTClientException
  {
    return (IGTFeatureEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_FEATURE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_FEATURE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetFeatureEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter != null)
    {
      throw new UnsupportedOperationException("GetFeatureListEvent does not support specification of a filter");
    }
    return new GetFeatureListEvent();
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.RuntimeException("Features may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultFeatureEntity();
  }


}