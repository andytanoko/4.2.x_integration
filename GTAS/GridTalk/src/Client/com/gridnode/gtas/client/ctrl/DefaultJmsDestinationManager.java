/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultJmsDestinationManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.alert.CreateJmsDestinationEvent;
import com.gridnode.gtas.events.alert.DeleteJmsDestinationEvent;
import com.gridnode.gtas.events.alert.GetJmsDestinationEvent;
import com.gridnode.gtas.events.alert.GetJmsDestinationListEvent;
import com.gridnode.gtas.events.alert.UpdateJmsDestinationEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.AssertUtil;

class DefaultJmsDestinationManager extends DefaultAbstractManager
  implements IGTJmsDestinationManager
{
	DefaultJmsDestinationManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_JMS_DESTINATION, session);
  }

//  protected void setDefaultFieldValues(AbstractGTEntity entity)
//    throws GTClientException
//  {
//    entity.setNewFieldValue(IGTBusinessEntityEntity.STATE,IGTBusinessEntityEntity.STATE_NORMAL);
//  }

  
  

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
    	IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
    	Long uid = (Long) jd.getFieldValue(IGTJmsDestinationEntity.UID);
    	String name = jd.getFieldString(IGTJmsDestinationEntity.NAME);
    	Integer type = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.TYPE);
    	String jndiName = jd.getFieldString(IGTJmsDestinationEntity.JNDI_NAME);
    	Integer deliveryMode = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.DELIVERY_MODE);
    	Integer priority = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.PRIORITY);
    	String connectionFactoryJndi = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI);
    	String connectionUser = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_USER);
    	String connectionPassword = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_PASSWORD);
    	Properties lookupProperties = (Properties) jd.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
    	Integer retryInterval = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.RETRY_INTERVAL);
    	Integer maximumRetries = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.MAXIMUM_RETRIES);
    	
    	UpdateJmsDestinationEvent event = new UpdateJmsDestinationEvent(uid,
    	                             	                                 name, type, jndiName, deliveryMode, priority, 
    	                            	                                 connectionFactoryJndi, connectionUser, connectionPassword, 
    	                            	                                 lookupProperties, retryInterval, maximumRetries);
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
    	IGTJmsDestinationEntity jd = (IGTJmsDestinationEntity) entity;
    	String name = jd.getFieldString(IGTJmsDestinationEntity.NAME);
    	Integer type = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.TYPE);
    	String jndiName = jd.getFieldString(IGTJmsDestinationEntity.JNDI_NAME);
    	Integer deliveryMode = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.DELIVERY_MODE);
    	Integer priority = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.PRIORITY);
    	String connectionFactoryJndi = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_FACTORY_JNDI);
    	String connectionUser = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_USER);
    	String connectionPassword = jd.getFieldString(IGTJmsDestinationEntity.CONNECTION_PASSWORD);
    	Properties lookupProperties = (Properties) jd.getFieldValue(IGTJmsDestinationEntity.LOOKUP_PROPERTIES);
    	Integer retryInterval = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.RETRY_INTERVAL);
    	Integer maximumRetries = (Integer) jd.getFieldValue(IGTJmsDestinationEntity.MAXIMUM_RETRIES);
    	
    	CreateJmsDestinationEvent event = new CreateJmsDestinationEvent(name, type, jndiName, deliveryMode, priority, 
    	                            	                                 connectionFactoryJndi, connectionUser, connectionPassword, 
    	                            	                                 lookupProperties, retryInterval, maximumRetries);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

//  public void updateChannels(IGTBusinessEntityEntity entity, Collection channels)
//    throws GTClientException
//  {
//    if(entity == null)
//    {
//      throw new java.lang.NullPointerException("null entity reference");
//    }
//    if(channels == null)
//    {
//      channels = new Vector(0);
//    }
//    try
//    {
//      SetChannelListForBizEntityEvent event = new SetChannelListForBizEntityEvent(entity.getUidLong(),channels);
//      handleEvent(event);
//    }
//    catch(Throwable t)
//    {
//      throw new GTClientException("Error updating channels list for BusinessEntity entity",t);
//    }
//  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_JMS_DESTINATION;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_JMS_DESTINATION;
  }

  protected IEvent getGetEvent(Long uid)
    throws EventException
  {
    return new GetJmsDestinationEvent(uid);
  }

  protected IEvent getGetListEvent(IDataFilter filter)
    throws EventException
  {
    return new GetJmsDestinationListEvent(filter);
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    return new DeleteJmsDestinationEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
  	AssertUtil.assertTrue(IGTEntity.ENTITY_JMS_DESTINATION.equals(entityType));
  	return new DefaultJmsDestinationEntity();
  	
//    if(IGTEntity.ENTITY_BUSINESS_ENTITY.equals(entityType))
//    {
//      return new DefaultBusinessEntityEntity();
//    }
//    else if(IGTEntity.ENTITY_WHITE_PAGE.equals(entityType))
//    {
//      return new DefaultWhitePageEntity();
//    }
//    else if(IGTEntity.ENTITY_DOMAIN_IDENTIFIER.equals(entityType))
//    {
//      return new DefaultDomainIdentifierEntity();
//    }
//    throw new java.lang.IllegalArgumentException("Manager:" + this + " cannot create entities of type " + entityType);
  }

//  protected void loadField(Number fieldId, AbstractGTEntity entity)
//    throws GTClientException
//  {
//    try
//    {
//      if(IGTBusinessEntityEntity.CHANNELS.equals(fieldId))
//      {
//        if(entity.isNewEntity())
//        {
//          entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, null);
//        }
//        else
//        {
//          IForeignEntityConstraint constraint = (IForeignEntityConstraint)
//                                                getSharedFieldMetaInfo(IGTEntity.ENTITY_BUSINESS_ENTITY,
//                                                fieldId).getConstraint();
//          GetChannelListForBizEntityEvent event = new GetChannelListForBizEntityEvent(entity.getUidLong());
//          EntityListResponseData results = (EntityListResponseData)handleEvent(event);
//          Collection channels = (Collection)results.getEntityList();
//          channels = processMapCollection(constraint,channels);
//          channels = extractKeys(constraint,channels);
//          entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, channels);
//        }
//      }
//      else
//      {
//        throw new java.lang.IllegalStateException("Field " + fieldId + " of entity " + entity
//                                                  + " is not load-on-demand");
//      }
//    }
//    catch(Throwable t)
//    {
//      throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
//    }
//  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_JMS_DESTINATION.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[1];

      sharedVfmi[0] = new VirtualSharedFMI("jmsDestination.lookupProperties", IGTJmsDestinationEntity.VIRTUAL_LOOKUP_PROPERTIES);
      sharedVfmi[0].setCollection(true);
      sharedVfmi[0].setValueClass("java.util.ArrayList");
      sharedVfmi[0].setElementClass("java.lang.Long");
      Properties detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","lookupProperties.name");
      detail.setProperty("foreign.display","lookupProperties.name");
      detail.setProperty("foreign.cached","false");
//      detail.setProperty("foreign.additionalDisplay","description"); //20021227AH
      IForeignEntityConstraint constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

//  void initVirtualEntityFields(String entityType,
//                        AbstractGTEntity entity,
//                        Map fieldMap)
//    throws GTClientException
//  {
//    entity.setNewFieldValue(IGTBusinessEntityEntity.CHANNELS, new UnloadedFieldToken());
//  }

//  public IGTDomainIdentifierEntity newDomainIdentifier() throws GTClientException
//  {
//    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_DOMAIN_IDENTIFIER);
//    entity.setNewEntity(true);
//    return (IGTDomainIdentifierEntity)entity;
//  }
  
//  protected List convertDomainIdentifierEntitiesToMaps(List domainIdentifierEntities) throws GTClientException
//  {
//    List domainIdentifierMaps = new ArrayList(domainIdentifierEntities.size());
//    
//    Iterator i = domainIdentifierEntities.iterator();
//    while(i.hasNext())
//    {
//      IGTDomainIdentifierEntity domainIdentifier = (IGTDomainIdentifierEntity)i.next();
//      String type   = domainIdentifier.getFieldString(IGTDomainIdentifierEntity.TYPE);
//      String value  = domainIdentifier.getFieldString(IGTDomainIdentifierEntity.VALUE);
//      Object uid  = domainIdentifier.getFieldValue(IGTDomainIdentifierEntity.UID);
//
//      Map map = new HashMap();
//      map.put(IGTDomainIdentifierEntity.TYPE, type);
//      map.put(IGTDomainIdentifierEntity.VALUE, value);
//      map.put(IGTDomainIdentifierEntity.UID, uid);
//      
//      domainIdentifierMaps.add(map);
//    }
//    
//    return domainIdentifierMaps;
//  }
}