/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultChannelInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-09-17     Andrew Hill         Add fields isMaster,isPartner,packagingProfile,securityProfile
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.channel.CreateChannelInfoEvent;
import com.gridnode.gtas.events.channel.DeleteChannelInfoEvent;
import com.gridnode.gtas.events.channel.GetChannelInfoEvent;
import com.gridnode.gtas.events.channel.GetChannelInfoListEvent;
import com.gridnode.gtas.events.channel.UpdateChannelInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultChannelInfoManager extends DefaultAbstractManager
  implements IGTChannelInfoManager
{

  DefaultChannelInfoManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_CHANNEL_INFO, session);
  }

  public IGTChannelInfoEntity getChannelInfoByUID(long uid)
    throws GTClientException
  {
    return (IGTChannelInfoEntity)getByUid(uid);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)entity;
    try
    {
      Long uid = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.UID);
      String name = channelInfo.getFieldString(IGTChannelInfoEntity.NAME);
      String description = channelInfo.getFieldString(IGTChannelInfoEntity.DESCRIPTION);
      String tptProtocolType = channelInfo.getFieldString(IGTChannelInfoEntity.TPT_PROTOCOL_TYPE);
      Long commInfoUid = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.TPT_COMM_INFO_UID);
      Long packagingInfo = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.PACKAGING_PROFILE);
      Long securityInfo = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.SECURITY_PROFILE);
      HashMap flowControlInfo = convertFlowControlInfoEntityToMap((IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO));

      UpdateChannelInfoEvent event = new UpdateChannelInfoEvent(uid,
                                                                name,
                                                                description,
                                                                tptProtocolType,
                                                                commInfoUid,
                                                                packagingInfo,
                                                                securityInfo,
                                                                flowControlInfo);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTChannelInfoEntity channelInfo = (IGTChannelInfoEntity)entity;
    try
    {
      String name = channelInfo.getFieldString(IGTChannelInfoEntity.NAME);
      String description = channelInfo.getFieldString(IGTChannelInfoEntity.DESCRIPTION);
      String tptProtocolType = channelInfo.getFieldString(IGTChannelInfoEntity.TPT_PROTOCOL_TYPE);
      Long commInfoUid = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.TPT_COMM_INFO_UID);
      Long packagingInfo = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.PACKAGING_PROFILE);
      Long securityInfo = (Long)channelInfo.getFieldValue(IGTChannelInfoEntity.SECURITY_PROFILE);
      Boolean isPartner = (Boolean)channelInfo.getFieldValue(IGTChannelInfoEntity.IS_PARTNER);
      HashMap flowControlInfo = convertFlowControlInfoEntityToMap((IGTFlowControlInfoEntity)channelInfo.getFieldValue(IGTChannelInfoEntity.FLOW_CONTROL_INFO));

      CreateChannelInfoEvent event = new CreateChannelInfoEvent(name,
                                                                description,
                                                                tptProtocolType,
                                                                commInfoUid,
                                                                isPartner,
                                                                packagingInfo,
                                                                securityInfo,
                                                                flowControlInfo);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_CHANNEL_INFO;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_CHANNEL_INFO;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetChannelInfoEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetChannelInfoListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteChannelInfoEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_CHANNEL_INFO.equals(entityType))
    {
      return new DefaultChannelInfoEntity();
    }
    else if(IGTEntity.ENTITY_FLOW_CONTROL_INFO.equals(entityType))
    {
      return new DefaultFlowControlInfoEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public IGTFlowControlInfoEntity newFlowControlInfo() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_FLOW_CONTROL_INFO);
    entity.setNewEntity(true);
    return (IGTFlowControlInfoEntity)entity;
  }
  
  protected HashMap convertFlowControlInfoEntityToMap(IGTFlowControlInfoEntity flowControlInfo) throws GTClientException
  {
    Boolean isZip           = (Boolean)flowControlInfo.getFieldValue(IGTFlowControlInfoEntity.IS_ZIP);
    Integer zipThreshold    = (Integer)flowControlInfo.getFieldValue(IGTFlowControlInfoEntity.ZIP_THRESHOLD);
    Boolean isSplit         = (Boolean)flowControlInfo.getFieldValue(IGTFlowControlInfoEntity.IS_SPLIT);
    Integer splitSize       = (Integer)flowControlInfo.getFieldValue(IGTFlowControlInfoEntity.SPLIT_SIZE);
    Integer splitThreshold  = (Integer)flowControlInfo.getFieldValue(IGTFlowControlInfoEntity.SPLIT_THRESHOLD);
  
    Map flowControlInfoMap = new HashMap();
    flowControlInfoMap.put(IGTFlowControlInfoEntity.IS_ZIP,          isZip);
    flowControlInfoMap.put(IGTFlowControlInfoEntity.ZIP_THRESHOLD,   zipThreshold);
    flowControlInfoMap.put(IGTFlowControlInfoEntity.IS_SPLIT,        isSplit);
    flowControlInfoMap.put(IGTFlowControlInfoEntity.SPLIT_SIZE,      splitSize);
    flowControlInfoMap.put(IGTFlowControlInfoEntity.SPLIT_THRESHOLD, splitThreshold);
    return (HashMap)flowControlInfoMap; // 20040105 DDJ: return HashMap instead of Map because of event
  }
}