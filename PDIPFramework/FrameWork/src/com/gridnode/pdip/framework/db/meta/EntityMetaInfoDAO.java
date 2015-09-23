/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityMetaInfoDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception
 */

package com.gridnode.pdip.framework.db.meta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import com.gridnode.pdip.framework.db.dao.GenericDAO;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * DAO for use by MetaInfoBean to access the EntityMetaInfo dataset
 * @author i00107
 * @since
 * @version GT 4.0 VAN
 */
public class EntityMetaInfoDAO extends GenericDAO
{
	private String _objectNameCol,
								_entityNameCol,
								_sqlNameCol;
	
	private String _findByObjectNameQuery,
							_findByEntityNameQuery,
							_findAllQuery;
	
	private static EntityMetaInfoDAO _self = null;
	
	public static synchronized EntityMetaInfoDAO getInstance() throws Exception
	{
		if (_self == null)
		{
			_self = new EntityMetaInfoDAO();
		}
		return _self;
	}
	
	private EntityMetaInfoDAO() throws Exception
	{
		super("com/gridnode/pdip/framework/db/meta/EntityMetaInfo.properties", "EntityMetaInfo");
	}
	
	protected void initQueries() throws SystemException
	{
		_findByObjectNameQuery = getNonNullProperty("query.findByObjectName");
		_findByEntityNameQuery = getNonNullProperty("query.findByEntityName");
		_findAllQuery = getNonNullProperty("query.findAll");
	}
	
	protected void initCols() throws SystemException
	{
		_objectNameCol = getNonNullProperty("_objectName");
		_entityNameCol = getNonNullProperty("_entityName");
		_sqlNameCol = getNonNullProperty("_sqlName");
	}

  protected Object getDataFromRs(ResultSet rs) throws Exception
  {
  	EntityMetaInfo entityMetaInfo = new EntityMetaInfo();
  	entityMetaInfo.setEntityName(rs.getString(_entityNameCol));
  	entityMetaInfo.setObjectName(rs.getString(_objectNameCol));
  	entityMetaInfo.setSqlName(rs.getString(_sqlNameCol));
  	return entityMetaInfo;
  }
  
  /**
   * Find EntityMetaInfo by object name
   * @param objectName The object name
   * @return The EntityMetaInfo found
   * @throws Exception If no entity metainfo could be found for the objectName
   */
	public EntityMetaInfo findByObjectName(String objectName) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    Collection result = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findByObjectNameQuery);
      stmt.setString(1, objectName);
      result = executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[EntityMetaInfoDAO.findByObjectName] with objectName["+objectName+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    if (result.size() == 0)
    {
    	throw new SystemException("EntityMetaInfo for ObjectName: "+objectName+" not found!", null);
    }
    return (EntityMetaInfo)result.toArray()[0];
	}
	
	/**
	 * Find EntityMetaInfo using the entityName
	 * @param entityName The entity name
	 * @return The EntityMetaInfo found
	 * @throws Exception If no entity metainfo could be found using the entity name
	 */
	public EntityMetaInfo findByEntityName(String entityName) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    Collection result = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findByEntityNameQuery);
      stmt.setString(1, entityName);
      result = executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[EntityMetaInfoDAO.findByEntityName] with entityName["+entityName+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    if (result.size() == 0)
    {
    	throw new SystemException("EntityMetaInfo for EntityName: "+entityName+" not found!", null);
    }
    return (EntityMetaInfo)result.toArray()[0];
		
	}
	
	/**
	 * Retrieve all EntityMetaInfo(s) in the system
	 * @return Collection of EntityMetaInfo found
	 * @throws Exception Unable to perform retrieval
	 */
	public Collection findAll() throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findAllQuery);
      return executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[EntityMetaInfoDAO.findAll]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
	}
}
