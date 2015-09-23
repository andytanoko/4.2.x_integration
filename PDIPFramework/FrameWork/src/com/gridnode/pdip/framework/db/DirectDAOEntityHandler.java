/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DirectDAOEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 * Dec 11 2002    Neo Sok Lay         Use MetaInfoFactory instead of EntityMetaInfoLoader.
 * Feb 26 2004    Mahesh              Added getEntityCount method
 * Feb 28 2006    Neo Sok Lay         Use generics
 * Feb 07 2007		Alain Ah Ming				Use new log error codes
 */
package com.gridnode.pdip.framework.db;

import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

import java.util.Collection;

/**
 * Provides direct data access functionality for an entity. Direct data access
 * is only provided for read-only purpose. Thus, only retrieval services are
 * provided. The functionality of this class is moved from AbstractEntityHandler
 * class.
 *
 * @author Neo Sok Lay
 * @version 4.0 GT VAN
 * @since 2.0 I5
 */
public class DirectDAOEntityHandler
{
  protected String _entityName;
  protected String _objectName;

  /**
   * Instantiates a entity handler. Default to use local context for locating
   * the entity bean service.
   *
   * @param entityName The class name of the entity
   */
  public DirectDAOEntityHandler(String entityName)
  {
    _entityName = entityName;
    try
    {
      _objectName = MetaInfoFactory.getInstance().getMetaInfoFor(_entityName).getObjectName();
    }
    catch(Exception ex)
    {
      Log.error(ILogErrorCodes.DATA_HANDLER_INITIALIZE, Log.DB, "[DirectDAOEntityHandler.<init>] Failed to initialize EJB interfaces: "+entityName, ex);
    }
  }

  /**
   * This methoad retrieves an entity object for read only purpose.
   *
   * @param uId The key of the entity object.
   * @return The entity retrieved.
   * @exception Exception Error in the retrieval.
   *
   * @since 2.0 I5
   */
  public IEntity getEntityByKeyForReadOnly(Long uId) throws Exception
  {
    IEntity entity = getDAO().load(uId);
    return entity;
  }

  /**
   * This method retrieves a Collection of entity objects for readonly purpose.
   *
   * @param filter The filtering condition for the retrieval.
   * @return A Collection of IEntity objects retrieved.
   * @exception Exception Error in the retrieval.
   *
   * @since 2.0 I5
   */
  public Collection getEntityByFilterForReadOnly(IDataFilter filter)
    throws Exception
  {
    Collection entities = getDAO().getEntityByFilter(filter);
    return entities;
  }

  /**
   * This method retrieve a Collection of the primary keys of the entity
   * objects for read-only purpose.
   *
   * @param filter The filtering condition for the retrieval
   * @return A Collection of primary keys (Long).
   */
  public Collection<Long> getKeyByFilterForReadOnly(IDataFilter filter)
    throws Exception
  {
    Collection<Long> keys = getDAO().findByFilter(filter);
    return keys;
  }

  /**
   * This method returns count of entities
   *
   * @param filter The filtering condition for the retrieval
   * @return A integer count  
   */
  public int getEntityCount(IDataFilter filter) throws Exception
  {
    return getDAO().getEntityCount(filter);
  }
  
  /**
   * Get the DataAccessObject for the entity that this handler is for.
   *
   * @return The DAO.
   */
  protected IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(_entityName);
  }
}