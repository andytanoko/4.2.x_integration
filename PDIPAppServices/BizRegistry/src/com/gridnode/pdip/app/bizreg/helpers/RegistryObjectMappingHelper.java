/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryObjectMappingHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 19 2003    Neo Sok Lay         Created
 * Oct 01 2003    Neo Sok Lay         Add methods:
 *                                    - getByProprietaryIfExists
 *                                    - findObjectsKeyProprietary
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.bizreg.helpers;

import com.gridnode.pdip.app.bizreg.entities.ejb.IRegistryObjectMappingLocalHome;
import com.gridnode.pdip.app.bizreg.entities.ejb.IRegistryObjectMappingLocalObj;
import com.gridnode.pdip.app.bizreg.pub.model.RegistryObjectMapping;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a helper class for managing the persistency of RegistryObjectMapping entity.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryObjectMappingHelper extends LocalEntityHandler
{
  private static final RegistryObjectMappingHelper _self = new RegistryObjectMappingHelper(RegistryObjectMapping.ENTITY_NAME);

  /**
   * Constructs an RegistryObjectMappingHelper instance.
   * 
   * @param entityName
   */
  private RegistryObjectMappingHelper(String entityName)
  {
    super(entityName);
    EntityHandlerFactory.putEntityHandler(entityName,
       true, this);
  }

  /**
   * Gets an instance of RegistryObjectMappingHelper.
   * 
   * @return An instance of RegistryObjectMappingHelper
   */
  public static RegistryObjectMappingHelper getInstance()
  {
    return _self;
  }
  
  /**
   * Retrieves a RegistryObjectMapping based on the specified information.
   * 
   * @param registryObjectType The RegistryObjectType value
   * @param registryObjectKey The RegistryObjectKey value
   * @param queryUrl The QueryUrl value
   * @return The RegistryObjectMapping retrieved based on the specified information if exists. Otherwise, returns <b>null</b>.
   * @throws Throwable Error in retrieval from database.
   */
  public RegistryObjectMapping getIfExists(String registryObjectType, String registryObjectKey, String queryUrl)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryObjectMapping.REGISTRY_OBJECT_TYPE,
      filter.getEqualOperator(), registryObjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.REGISTRY_QUERY_URL,
      filter.getEqualOperator(), queryUrl, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.REGISTRY_OBJECT_KEY,
      filter.getEqualOperator(), registryObjectKey, false);
    Collection results = getEntityByFilterForReadOnly(filter);

    return (RegistryObjectMapping)getOneObject(results);
  }
  
  /**
   * Retrieves a RegistryObjectMapping based on the specified proprietary information.
   * 
   * @param proprietaryObjectType The ProprietaryObjectType value
   * @param proprietaryObjectKey The ProprietaryObjectKey value
   * @param queryUrl The QueryUrl value
   * @return The RegistryObjectMapping retrieved based on the specified information if exists. Otherwise, returns <b>null</b>.
   * @throws Throwable Error in retrieval from database.
   */
  public RegistryObjectMapping getByProprietaryIfExists(String proprietaryObjectType, String proprietaryObjectKey, String queryUrl)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryObjectMapping.PROPRIETARY_OBJECT_TYPE,
      filter.getEqualOperator(), proprietaryObjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.REGISTRY_QUERY_URL,
      filter.getEqualOperator(), queryUrl, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.PROPRIETARY_OBJECT_KEY,
      filter.getEqualOperator(), proprietaryObjectKey, false);
    Collection results = getEntityByFilterForReadOnly(filter);

    return (RegistryObjectMapping)getOneObject(results);
  }
  
  /**
   * Creates a RegistryObjectMapping entity based on the specified information.
   * 
   * @param registryObjectType The RegistryObjectType value.
   * @param registryObjectKey The RegistryObjectKey value.
   * @param queryUrl The QueryUrl value.
   * @param publishUrl The PublishUrl value.
   * @param proprietaryObjectType The ProprietaryObjectType value.
   * @param proprietaryObjectKey The ProprietaryObjectKey value.
   * @param isPublished The IsPublishedObject value.
   * @throws Throwable Error in creating the entity in database.
   */
  public void create(String registryObjectType, String registryObjectKey, String queryUrl, String publishUrl,
    String proprietaryObjectType, String proprietaryObjectKey, boolean isPublished)
    throws Throwable
  {
    RegistryObjectMapping mapping = new RegistryObjectMapping();
    mapping.setProprietaryObjectKey(proprietaryObjectKey);
    mapping.setProprietaryObjectType(proprietaryObjectType);
    mapping.setPublishedObject(isPublished);
    mapping.setRegistryObjectKey(registryObjectKey);
    mapping.setRegistryObjectType(registryObjectType);
    mapping.setRegistryPublishUrl(publishUrl);
    mapping.setRegistryQueryUrl(queryUrl);
    mapping.setState(RegistryObjectMapping.STATE_SYNCHRONIZED);
        
    create(mapping);
  }
  
  /**
   * Finds for a RegistryObjectMapping entity based on the specified information and that 
   * IsPublishedObject=<b>true</b> and State=<b>Synchronized</b>.
   * 
   * @param proprietaryObjectType The ProprietaryObjectType value.
   * @param proprietaryObjectKey The ProprietaryObjectKey value.
   * @param queryUrl The QueryUrl value.
   * @return The RegistryObjectMapping entity found based on the criteria.
   * @throws Throwable Error in retrieving from database.
   */
  public RegistryObjectMapping findPublishedObject(String proprietaryObjectType, String proprietaryObjectKey, String queryUrl)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryObjectMapping.REGISTRY_QUERY_URL,
      filter.getEqualOperator(), queryUrl, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.PROPRIETARY_OBJECT_TYPE,
      filter.getEqualOperator(), proprietaryObjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.PROPRIETARY_OBJECT_KEY,
      filter.getEqualOperator(), proprietaryObjectKey, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.STATE,
      filter.getEqualOperator(), new Short(RegistryObjectMapping.STATE_SYNCHRONIZED), false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.IS_PUBLISHED_OBJECT,
      filter.getEqualOperator(), Boolean.TRUE, false);
    
    Collection results = getEntityByFilterForReadOnly(filter);
    
    return (RegistryObjectMapping)getOneObject(results);
  }
  
  /**
   * Finds for RegistryObjectMapping entities based on the specified information and that 
   * IsPublishedObject=<b>true</b> and State=<b>Synchronized</b>.
   * 
   * @param proprietaryObjectType The ProprietaryObjectType value.
   * @param proprietaryObjectKey The ProprietaryObjectKey value.
   * @return Collection of RegistryObjectMapping entities found based on the criteria.
   * @throws Throwable Error in retrieving from database.
   */
  public Collection findPublishedObjects(String proprietaryObjectType, String proprietaryObjectKey)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryObjectMapping.PROPRIETARY_OBJECT_TYPE,
      filter.getEqualOperator(), proprietaryObjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.PROPRIETARY_OBJECT_KEY,
      filter.getEqualOperator(), proprietaryObjectKey, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.STATE,
      filter.getEqualOperator(), new Short(RegistryObjectMapping.STATE_SYNCHRONIZED), false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.IS_PUBLISHED_OBJECT,
      filter.getEqualOperator(), Boolean.TRUE, false);
    
    Collection results = getEntityByFilterForReadOnly(filter);
    
    return results;
  }

  /**
   * Finds for RegistryObjectMapping entities based on the specified information.
   * 
   * @param proprietaryObjectType The ProprietaryObjectType value.
   * @param proprietaryObjectKey The ProprietaryObjectKey value.
   * @param state The state of the mappings to find.
   * @return Collection of RegistryObjectMapping entities found based on the criteria.
   * @throws Throwable Error in retrieving from database.
   */
  public Collection findObjectsByProprietary(
    String proprietaryObjectType, String proprietaryObjectKey, short state)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, RegistryObjectMapping.PROPRIETARY_OBJECT_TYPE,
      filter.getEqualOperator(), proprietaryObjectType, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.PROPRIETARY_OBJECT_KEY,
      filter.getEqualOperator(), proprietaryObjectKey, false);
    filter.addSingleFilter(filter.getAndConnector(), RegistryObjectMapping.STATE,
      filter.getEqualOperator(), new Short(state), false);
    
    Collection results = getEntityByFilterForReadOnly(filter);
    
    return results;
  }
  
  /**
   * Pend a registry object for deletion at all registries that register it.
   * 
   * @param proprietaryObjectType The proprietary object type for the registry object.
   * @param proprietaryObjectKey The proprietary object key for the registry object.
   * @throws Throwable Error in performing the operation.
   */
  public void pendDeletePublishedObject(String proprietaryObjectType, String proprietaryObjectKey)
    throws Throwable
  {
    Collection mappings = findPublishedObjects(proprietaryObjectType, proprietaryObjectKey);
    for (Iterator i=mappings.iterator(); i.hasNext();)
    {
      RegistryObjectMapping mapping = (RegistryObjectMapping)i.next();
      mapping.setState(RegistryObjectMapping.STATE_PENDING_DELETE);
      update(mapping);
    }
  }

  /**
   * Pend a registry object for update to all registries that register it.
   * 
   * @param proprietaryObjectType The proprietary object type for the registry object.
   * @param proprietaryObjectKey The proprietary object key for the registry object.
   * @throws Throwable Error in performing the operation.
   */
  public void pendUpdatePublishedObject(String proprietaryObjectType, String proprietaryObjectKey)
    throws Throwable
  {
    Collection mappings = findPublishedObjects(proprietaryObjectType, proprietaryObjectKey);
    for (Iterator i=mappings.iterator(); i.hasNext();)
    {
      RegistryObjectMapping mapping = (RegistryObjectMapping)i.next();
       mapping.setState(RegistryObjectMapping.STATE_PENDING_UPDATE);
      update(mapping);
    }
  }
  
  /**
   * Get one element from the specified collection, if it is not empty.
   * 
   * @param coll The collection to get element from.
   * @return The first element returned from the collection's iterator, or <b>null</b> if the collection is empty.
   */
  private Object getOneObject(Collection coll)
  {
    Object obj = null;
    if (coll != null && !coll.isEmpty())
    {
      obj = coll.iterator().next();
    }
    
    return obj;
  }
  /**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getHome()
   *//*
  protected Object getHome() throws Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IRegistryObjectMappingLocalHome.class.getName(),
    IRegistryObjectMappingLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IRegistryObjectMappingLocalHome.class;
	}

	/**
   * @see com.gridnode.pdip.framework.db.AbstractEntityHandler#getProxyInterfaceClass()
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IRegistryObjectMappingLocalObj.class;
  }

}
