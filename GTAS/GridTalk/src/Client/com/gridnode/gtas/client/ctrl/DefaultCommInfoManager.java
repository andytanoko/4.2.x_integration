/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCommInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2002-12-04     Andrew Hill         Refactored to new CommInfo model
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.channel.CreateCommInfoEvent;
import com.gridnode.gtas.events.channel.DeleteCommInfoEvent;
import com.gridnode.gtas.events.channel.GetCommInfoEvent;
import com.gridnode.gtas.events.channel.GetCommInfoListEvent;
import com.gridnode.gtas.events.channel.UpdateCommInfoEvent;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultCommInfoManager extends DefaultAbstractManager
  implements IGTCommInfoManager
{
  DefaultCommInfoManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_COMM_INFO, session);
  }

  public IGTCommInfoEntity getCommInfoByUID(long uid)
    throws GTClientException
  {
    return (IGTCommInfoEntity)getByUid(uid);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTCommInfoEntity commInfo = (IGTCommInfoEntity)entity;
    try
    {
      Long uid = commInfo.getUidLong();
      String name = commInfo.getFieldString(IGTCommInfoEntity.NAME);
      String description = commInfo.getFieldString(IGTCommInfoEntity.DESCRIPTION);
      String url = commInfo.getFieldString(IGTCommInfoEntity.URL);
      String protocolType = commInfo.getFieldString(IGTCommInfoEntity.PROTOCOL_TYPE);
      Boolean isPartner = (Boolean)commInfo.getFieldValue(IGTCommInfoEntity.IS_PARTNER);

      UpdateCommInfoEvent event = new UpdateCommInfoEvent(uid,name,description,url,protocolType,isPartner);
      handleUpdateEvent(event,(AbstractGTEntity)commInfo);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update CommInfo", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTCommInfoEntity commInfo = (IGTCommInfoEntity)entity;
    try
    {
      String name = commInfo.getFieldString(IGTCommInfoEntity.NAME);
      String description = commInfo.getFieldString(IGTCommInfoEntity.DESCRIPTION);
      String url = commInfo.getFieldString(IGTCommInfoEntity.URL);
      String protocolType = commInfo.getFieldString(IGTCommInfoEntity.PROTOCOL_TYPE);
      Boolean isPartner = (Boolean)commInfo.getFieldValue(IGTCommInfoEntity.IS_PARTNER);

      CreateCommInfoEvent event = new CreateCommInfoEvent(name,description,url,protocolType,isPartner);
      handleCreateEvent(event,(AbstractGTEntity)commInfo);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create CommInfo", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_COMM_INFO;
  }

  public String getEntityType()
  {
    return IGTEntity.ENTITY_COMM_INFO;
  }

  public Collection getAllOfRefIdAndProtocolType(String refId, String protocolType)
    throws GTClientException
  {
    if( (protocolType == null) || ("".equals(protocolType) ) )
    {
      return new ArrayList();
    }

    DataFilterImpl filter = new DataFilterImpl();

    filter.addSingleFilter(null,ICommInfo.PROTOCOL_TYPE,
                           filter.getEqualOperator(),protocolType,false);

    if( (refId != null) && (!"".equals(refId)))
    {
      filter.addSingleFilter(filter.getAndConnector(),ICommInfo.REF_ID,
                           filter.getEqualOperator(),refId,false);
    }

    GetCommInfoListEvent event = new GetCommInfoListEvent(filter);
    return handleGetListEvent(event);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetCommInfoEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetCommInfoListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteCommInfoEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultCommInfoEntity();
  }
}