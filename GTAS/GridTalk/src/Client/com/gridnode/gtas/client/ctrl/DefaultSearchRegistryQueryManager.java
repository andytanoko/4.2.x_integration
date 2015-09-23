/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSearchRegistryQueryManager.java
 *
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-10-10     Neo Sok Lay         Commented away overriding of ChannelInfo's
 *                                    sharedFMI. Changed to SearchedChannelInfo.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.FakeForeignEntityConstraint;
import com.gridnode.gtas.events.enterprise.*;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultSearchRegistryQueryManager extends DefaultAbstractManager
  implements IGTSearchRegistryQueryManager
{
  DefaultSearchRegistryQueryManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_SEARCH_REGISTRY_QUERY, session);

    // publishBusinessEntity isnt the primary entity so we need to do this explicitly
    loadFmi(IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Submitted searches may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTSearchRegistryQueryEntity query = (IGTSearchRegistryQueryEntity)entity;
    try
    {
      IGTSearchRegistryCriteriaEntity criteria = (IGTSearchRegistryCriteriaEntity)query.getFieldValue(IGTSearchRegistryQueryEntity.CRITERIA);
      Map map = new HashMap();
      map.put(IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC,      criteria.getFieldValue(IGTSearchRegistryCriteriaEntity.BUS_ENTITY_DESC));
      map.put(IGTSearchRegistryCriteriaEntity.DUNS,                 criteria.getFieldValue(IGTSearchRegistryCriteriaEntity.DUNS));
      map.put(IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS,  criteria.getFieldValue(IGTSearchRegistryCriteriaEntity.MESSAGING_STANDARDS));
      map.put(IGTSearchRegistryCriteriaEntity.MATCH,                criteria.getFieldValue(IGTSearchRegistryCriteriaEntity.MATCH));
      map.put(IGTSearchRegistryCriteriaEntity.QUERY_URL,            criteria.getFieldValue(IGTSearchRegistryCriteriaEntity.QUERY_URL));

      SubmitRegistrySearchEvent event = new SubmitRegistrySearchEvent(map);
      Long searchId = (Long)handleEvent(event);
      ((AbstractGTEntity)query).setNewFieldValue(IGTSearchRegistryQueryEntity.SEARCH_ID, searchId);
      if(searchId == null)
      {
        throw new NullPointerException("null searchId returned by gtas");
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to submit query", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_SEARCH_REGISTRY_QUERY;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long searchId)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new RetrieveRegistrySearchEvent(searchId);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No event exists to retrieve list of searches from gtas");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    throw new UnsupportedOperationException("Explicitly deleting searches is not supported");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY.equals(entityType))
    {
      return new DefaultPublishBusinessEntityEntity();
    }
    else if(IGTEntity.ENTITY_SEARCH_REGISTRY_CRITERIA.equals(entityType))
    {
      return new DefaultSearchRegistryCriteriaEntity();
    }
    else if(IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY.equals(entityType))
    {
      return new DefaultSearchRegistryQueryEntity();
    }
    else if(IGTEntity.ENTITY_SEARCHED_BUSINESS_ENTITY.equals(entityType))
    {
      return new DefaultSearchedBusinessEntityEntity();
    }
    else if (IGTEntity.ENTITY_SEARCHED_CHANNEL_INFO.equals(entityType))
    {
      return new DefaultSearchedChannelInfoEntity();
    }
    else
    {
      throw new UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  boolean isVirtual(String entityType)
  {
    return IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY.equals(entityType);
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[2];
      sharedVfmi[0] = new VirtualSharedFMI( "publishBusinessEntity.be",
                                            IGTPublishBusinessEntityEntity.BE);
      sharedVfmi[1] = new VirtualSharedFMI( "publishBusinessEntity.registryConnectInfo",
                                            IGTPublishBusinessEntityEntity.REGISTRY_CONNECT_INFO);
      sharedVfmi[0].setMandatoryCreate(true);
      sharedVfmi[0].setCollection(true);
      sharedVfmi[0].setConstraint(new FakeForeignEntityConstraint(IGTEntity.ENTITY_BUSINESS_ENTITY,
                                                                  "uid",
                                                                  "id"));
      sharedVfmi[1].setMandatoryCreate(true);
      sharedVfmi[1].setConstraint(new FakeForeignEntityConstraint(IGTEntity.ENTITY_REGISTRY_CONNECT_INFO,
                                                                  "name",
                                                                  "publishUrl"));
      return sharedVfmi;
    }
    else
    {
      return super.defineVirtualFields(entityType);
    }
  }
/*20031010NSL
  protected DefaultSharedFMI[] getSharedFmi(String entityType)
  {
    DefaultSharedFMI[] sharedFmi = super.getSharedFmi(entityType);
    try
    {
      if(sharedFmi == null)
      {
        loadFmi(entityType);
        sharedFmi = super.getSharedFmi(entityType);
      }

      if(IGTEntity.ENTITY_CHANNEL_INFO.equals(entityType))
      {
        for(int i = 0; i < sharedFmi.length; i++)
        {
          if(IGTChannelInfoEntity.PACKAGING_PROFILE.equals(sharedFmi[i].getFieldId()))
          {
            FieldMetaInfo gtasFmi = sharedFmi[i].getGtasFmi();
            gtasFmi.setConstraints("type=embedded\r\nembedded.type=packagingInfo");
            sharedFmi[i] = new DefaultSharedFMI(gtasFmi);
          }
          else if(IGTChannelInfoEntity.SECURITY_PROFILE.equals(sharedFmi[i].getFieldId()))
          {
            FieldMetaInfo gtasFmi = sharedFmi[i].getGtasFmi();
            gtasFmi.setConstraints("type=embedded\r\nembedded.type=securityInfo");
            sharedFmi[i] = new DefaultSharedFMI(gtasFmi);
          }
          else if(IGTChannelInfoEntity.TPT_COMM_INFO_UID.equals(sharedFmi[i].getFieldId()))
          {
            FieldMetaInfo gtasFmi = sharedFmi[i].getGtasFmi();
            gtasFmi.setConstraints("type=embedded\r\nembedded.type=commInfo");
            sharedFmi[i] = new DefaultSharedFMI(gtasFmi);
          }
        }
      }
    }
    catch(Throwable t)
    {
      ; // ignore
    }
    return sharedFmi;
  }
*/
  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("getAll() is not supported for SearchGridNodeQuery");
  }

  public IGTSearchRegistryCriteriaEntity newSearchRegistryCriteria() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SEARCH_REGISTRY_CRITERIA);
    entity.setNewEntity(true);
    return (IGTSearchRegistryCriteriaEntity)entity;
  }

  public IGTSearchRegistryQueryEntity newSearchRegistryQuery() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SEARCH_REGISTRY_QUERY);
    entity.setNewEntity(true);
    return (IGTSearchRegistryQueryEntity)entity;
  }

  public IGTPublishBusinessEntityEntity newPublishBusinessEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_PUBLISH_BUSINESS_ENTITY);
    entity.setNewEntity(true);
    return (IGTPublishBusinessEntityEntity)entity;
  }

  public IGTSearchedBusinessEntityEntity newSearchedBusinessEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SEARCHED_BUSINESS_ENTITY);
    entity.setNewEntity(true);
    return (IGTSearchedBusinessEntityEntity)entity;
  }

  public IGTSearchedChannelInfoEntity newSearchedChannelInfo() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SEARCHED_CHANNEL_INFO);
    entity.setNewEntity(true);
    return (IGTSearchedChannelInfoEntity)entity;
  }

  public void publishBusinessEntity(Collection businessEntitiyUids, String registryConnectInfoName) throws GTClientException
  {
    try
    {
      PublishBusinessEntityEvent event = new PublishBusinessEntityEvent(businessEntitiyUids, registryConnectInfoName);
      //Object obj = 
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error publishing business entity from registry: registryConnectInfoName=" + registryConnectInfoName, t);
    }
  }

  public void configureBizEntity(Long searchId, Collection uuids) throws GTClientException
  {
    try
    {
      ConfigureBizEntityFromRegistryEvent event = new ConfigureBizEntityFromRegistryEvent(searchId, uuids);
      //Object obj = 
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error configuring business entity from registry: searchId=" + searchId, t);
    }
  }

  public Collection getMessagingStandards() throws GTClientException
  {
    try
    {
      GetMessagingStandardsEvent event = new GetMessagingStandardsEvent();
      Object obj = handleEvent(event);
      if(obj instanceof Collection)
      {
        return (Collection)obj;
      }
      else
        throw new IllegalStateException("Expecting a collection from GetMessagingStandardsEvent()");
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting messaging standards", t);
    }
  }
}