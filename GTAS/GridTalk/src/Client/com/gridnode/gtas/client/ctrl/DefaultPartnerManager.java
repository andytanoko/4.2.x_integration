/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPartnerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-19     Andrew Hill         Created
 * 2002-08-20     Andrew Hill         BE & Partner virtual fields
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-04-03     Andrew Hill         Make be field mandatory
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.enterprise.GetBizEntityForPartnerEvent;
import com.gridnode.gtas.events.enterprise.GetServerWatchlistEvent;
import com.gridnode.gtas.events.enterprise.SetBizEntityForPartnerEvent;
import com.gridnode.gtas.events.partner.CreatePartnerEvent;
import com.gridnode.gtas.events.partner.DeletePartnerEvent;
import com.gridnode.gtas.events.partner.GetPartnerEvent;
import com.gridnode.gtas.events.partner.GetPartnerListEvent;
import com.gridnode.gtas.events.partner.UpdatePartnerEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultPartnerManager extends DefaultAbstractManager
  implements IGTPartnerManager
{
  DefaultPartnerManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PARTNER, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      boolean enabled = entity.getFieldValue(IGTPartnerEntity.STATE).equals(
                                              IGTPartnerEntity.STATE_ENABLED);
      Long beUid = null;
      Long channelUid = null;
      boolean doUpdateBe = entity.isFieldDirty(IGTPartnerEntity.BE) || entity.isFieldDirty(IGTPartnerEntity.CHANNEL);
      if(doUpdateBe)
      {
        beUid = (Long)entity.getFieldValue(IGTPartnerEntity.BE);
        channelUid = (Long)entity.getFieldValue(IGTPartnerEntity.CHANNEL);
      }
      UpdatePartnerEvent event = new UpdatePartnerEvent(
                                      (Long)entity.getFieldValue(IGTPartnerEntity.UID),
                                      entity.getFieldString(IGTPartnerEntity.NAME),
                                      entity.getFieldString(IGTPartnerEntity.DESCRIPTION),
                                      (Long)entity.getFieldValue(IGTPartnerEntity.PARTNER_TYPE),
                                      (Long)entity.getFieldValue(IGTPartnerEntity.PARTNER_GROUP),
                                      enabled);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateBe)
      {
        updateBeAndChannel((IGTPartnerEntity)entity,beUid,channelUid);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  public void updateBeAndChannel(IGTPartnerEntity entity, Long beUid, Long channelInfoUid)
    throws GTClientException
  {
    try
    {
      SetBizEntityForPartnerEvent event = new SetBizEntityForPartnerEvent(
                                                    entity.getUidLong(),beUid, channelInfoUid);
      handleEvent(event);
      ((AbstractGTEntity)entity).setNewFieldValue(IGTPartnerEntity.BE,beUid);
      ((AbstractGTEntity)entity).setNewFieldValue(IGTPartnerEntity.CHANNEL,channelInfoUid);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating BusinessEntity and Channel of entity " + entity,t);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      boolean enabled = entity.getFieldValue(IGTPartnerEntity.STATE).equals(IGTPartnerEntity.STATE_ENABLED);
      Long beUid = null;
      Long channelUid = null;
      boolean doUpdateBe = entity.isFieldDirty(IGTPartnerEntity.BE) || entity.isFieldDirty(IGTPartnerEntity.CHANNEL);
      if(doUpdateBe)
      {
        beUid = (Long)entity.getFieldValue(IGTPartnerEntity.BE);
        channelUid = (Long)entity.getFieldValue(IGTPartnerEntity.CHANNEL);
      }
      Long partnerGroup = (Long)entity.getFieldValue(IGTPartnerEntity.PARTNER_GROUP);
      CreatePartnerEvent event = new CreatePartnerEvent(
                                      entity.getFieldString(IGTPartnerEntity.PARTNER_ID),
                                      entity.getFieldString(IGTPartnerEntity.NAME),
                                      entity.getFieldString(IGTPartnerEntity.DESCRIPTION),
                                      (Long)entity.getFieldValue(IGTPartnerEntity.PARTNER_TYPE),
                                      partnerGroup,
                                      enabled);
      handleCreateEvent(event, (AbstractGTEntity)entity);
      if(doUpdateBe)
      {
        updateBeAndChannel((IGTPartnerEntity)entity,beUid,channelUid);
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  public IGTPartnerEntity getPartnerByPartnerId(String partnerId) throws GTClientException
  {
    try
    {
      GetPartnerEvent event = new GetPartnerEvent(partnerId);
      return (IGTPartnerEntity)handleGetEvent(event);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to get entity by uid", e);
    }
  }

  public IGTPartnerEntity getPartnerByUID(long uid) throws GTClientException
  {
    return (IGTPartnerEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PARTNER;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PARTNER;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPartnerEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter == null)
    {
      return new GetPartnerListEvent();
    }
    else
    {
      return new GetPartnerListEvent(filter);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePartnerEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultPartnerEntity();
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PARTNER.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];

      //partner.be
      sharedVfmi[0] = new VirtualSharedFMI("partner.be", IGTPartnerEntity.BE);
      sharedVfmi[0].setCollection(false);
      sharedVfmi[0].setValueClass("java.lang.Long");
      //20030403AH - Following problems with the bizCertMapping screen when partners were
      //selected that didnt have an associated BE, it was determined (amh) that all partners
      //should have a BE.
      sharedVfmi[0].setMandatoryCreate(true); //20030403AH
      sharedVfmi[0].setMandatoryUpdate(true); //20030403AH
      Properties detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","businessEntity.uid");
      detail.setProperty("foreign.display","businessEntity.id");
      detail.setProperty("foreign.cached","false");
      IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);

      //partner.channel
      sharedVfmi[1] = new VirtualSharedFMI("partner.channel", IGTPartnerEntity.CHANNEL);
      sharedVfmi[1].setCollection(false);
      sharedVfmi[1].setValueClass("java.lang.Long");
      // Set to be mandatory when applicable (which is when they have selected a BE)
      sharedVfmi[1].setMandatoryCreate(true);
      sharedVfmi[1].setMandatoryUpdate(true);
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","channelInfo.uid");
      detail.setProperty("foreign.display","channelInfo.name");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[1].setConstraint(constraint);
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    UnloadedFieldToken ulft = new UnloadedFieldToken();
    entity.setNewFieldValue(IGTPartnerEntity.BE, ulft);
    entity.setNewFieldValue(IGTPartnerEntity.CHANNEL, ulft);
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      if( (IGTPartnerEntity.BE.equals(fieldId)) || (IGTPartnerEntity.CHANNEL.equals(fieldId)))
      {
        if(entity.isNewEntity())
        {
          entity.setNewFieldValue(IGTPartnerEntity.BE, null);
          entity.setNewFieldValue(IGTPartnerEntity.CHANNEL, null);
        }
        else
        {
          //IForeignEntityConstraint constraint = (IForeignEntityConstraint)
          //                                      getSharedFieldMetaInfo(IGTEntity.ENTITY_PARTNER,
          //                                      fieldId).getConstraint();
          GetBizEntityForPartnerEvent event = new GetBizEntityForPartnerEvent(entity.getUidLong());
          Collection responseCollection = (Collection)handleEvent(event);
          // We have been returned a collection that has two elements.
          // The first is a map for the BE, the second a map for the channel
          if(responseCollection == null)
          {
            throw new java.lang.NullPointerException("null response collection for GetBizEntityForPartnerEvent");
          }
          if(responseCollection.size() != 2)
          {
            throw new GTClientException("response collection for GetBizEntityForPartnerEvent has "
                        + responseCollection.size() + " elements and not 2 elements as expected");
          }
          Iterator i = responseCollection.iterator();
          Map beMap = (Map)i.next();
          Map channelMap = (Map)i.next();
          IGTEntity be = null;
          //As a workaround for flagrant breach of consistency we also check for empty beMap
          //The event doesnt return null for a null be
          //- it returns an empty map. This is not good. If there is no BE it shouldnt return a map for one.
          //evil
          //diabolical
          //downright naughty mate
          //absolutely outrageous!
          if( (beMap != null) && (beMap.isEmpty()) ) beMap = null; //20021016AH
          if(beMap != null)
          {
            IGTManager beMgr = _session.getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
            be = buildEntityFromMap((DefaultAbstractManager)beMgr, IGTEntity.ENTITY_BUSINESS_ENTITY,beMap);
            entity.setNewFieldValue(IGTPartnerEntity.BE, be.getUidLong());
            //@todo: cache the foreign entity
          }
          else
          {
            entity.setNewFieldValue(IGTPartnerEntity.BE, null);
          }
          IGTEntity channel = null;
          if( (channelMap != null) && (channelMap.isEmpty()) ) channelMap = null; //20021016AH
          if(channelMap != null)
          {
            IGTManager channelMgr = _session.getManager(IGTManager.MANAGER_CHANNEL_INFO);
            channel = buildEntityFromMap((DefaultAbstractManager)channelMgr, IGTEntity.ENTITY_CHANNEL_INFO,channelMap);
            entity.setNewFieldValue(IGTPartnerEntity.CHANNEL, channel.getUidLong());
            //@todo: cache the foreign entity
          }
          else
          {
            entity.setNewFieldValue(IGTPartnerEntity.CHANNEL, null);
          }
        }
      }
      else
      {
        throw new java.lang.IllegalStateException("Field " + fieldId + " of entity " + entity
                                                  + " is not load-on-demand");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
    }
  }

  public Object[] getPartnerWatchListData() throws GTClientException
  {
    try
    {
      GetServerWatchlistEvent event = new GetServerWatchlistEvent();
      Object obj = handleEvent(event);
      if(obj == null)
      {
        throw new java.lang.NullPointerException("partner watchlist returned from GTAS is null");
      }
      return (Object[])obj;
    }
    catch(Throwable t)
    {
      throw new GTClientException("error getting server watchlist", t);
    }
  }
}