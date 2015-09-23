/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KeyGenDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 * Nov 13 2006    Neo Sok Lay         GNDB00027928: change getNextId(String,Collection)
 *                                    to getNextId(String, Long)
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                   
 */

package com.gridnode.pdip.framework.db.keygen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.gridnode.pdip.framework.db.dao.GenericDAO;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * DAO for use by KeyGenBean.
 * @author i00107
 * @version GT 4.0 VAN
 */
public class KeyGenDAO extends GenericDAO
{
	private String _refNameCol,
								_lastRefNumCol;
	
	private String _findByRefNameQuery,
									_insertQuery,
									_updateLastRefNumQuery;
	
	private static KeyGenDAO _self = null;
	
	public static synchronized KeyGenDAO getInstance() throws Exception
	{
		if (_self == null)
		{
			_self = new KeyGenDAO();
		}
		return _self;
	}
	
	private KeyGenDAO() throws Exception
	{
		super("com/gridnode/pdip/framework/db/keygen/ReferenceNum.properties", "ReferenceNum");
	}
	
	protected void initQueries() throws SystemException
	{
		_findByRefNameQuery = getNonNullProperty("query.findByRefName");
		_insertQuery = getNonNullProperty("query.insert");
		_updateLastRefNumQuery = getNonNullProperty("query.updateLastRefNum");
	}
	
	protected void initCols() throws SystemException
	{
		_refNameCol = getNonNullProperty("_refName");
		_lastRefNumCol = getNonNullProperty("_lastRefNum");
	}
	protected Object getDataFromRs(ResultSet rs) throws SQLException
	{
		return new Long(rs.getLong(_lastRefNumCol));
	}
	
	/**
	 * Get the last reference num by reference name
	 * @param refName The reference name
	 * @return The last reference num if reference name is found, or <b>null</b> otherwise.
	 * @throws Exception Unable to perform the retrieval
	 */
	public Long findByRefName(String refName) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    Collection result = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findByRefNameQuery);
      stmt.setString(1, refName);
      result = executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[EntityMetaInfoDAO.findByRefName] with refName["+refName+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    if (result.size() == 0)
    {
    	Log.debug(Log.DB, "ReferenceNum for RefName: "+refName+" not found!");
    	return null;
    }
    else
    {
    	return (Long)result.toArray()[0];
    }
	}

	/**
	 * Create a reference num for the specified reference name
	 * @param refName The reference name
	 * @throws Exception Unable to create a new reference num for the reference name
	 */
	public synchronized void createRefNum(String refName) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _insertQuery);
      stmt.setString(1, refName);
      int result = executeUpdate(stmt);
      Log.debug(Log.DB, "[KeyGenDAO.createRefNum] for refName["+refName+"] result="+result);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[KeyGenDAO.createRefNum] for refName["+refName+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
	}
	
	/**
	 * Get the next reference num for the reference name
	 * @param refName The reference name
	 * @return The next reference num for the reference name. If the reference name does not exist,
	 * it creates a new reference num record.
	 * @throws Exception
	 */
	public synchronized long getNextId(String refName) throws Exception
	{
    return getNextId(refName, null);
    /*
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      Long lastRefNum = findByRefName(refName);
      if (lastRefNum == null) {
      	createRefNum(refName);
      	lastRefNum = new Long(0);
      }
      
      stmt = getPreparedStatement(dbConnection, _updateLastRefNumQuery);
      long nextNum = lastRefNum.longValue() + 1;
      stmt.setLong(1, nextNum);
      stmt.setString(2, refName);
      int result = executeUpdate(stmt);
      Log.debug(Log.DB, "[KeyGenDAO.getNextId] for refName["+refName+"] result="+result);
      return nextNum;
    }
    catch(Exception ex)
    {
      Log.err(Log.DB, "[KeyGenDAO.getNextId]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    */
	}
	
	/**
	 * Get the next reference num for the reference name
	 * @param refName The reference name
	 * @param currMaxId To get the next reference num that is greater than the specified currMaxId.
   * If <b>null</b>, it is the same as calling getNextId(refName).
	 * @return The next reference num for the reference name. 
	 * If the reference name does not exist, it creates a new reference num record.
	 * @throws Exception
	 */
	public synchronized long getNextId(String refName, Long currMaxId) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      Long lastRefNum = findByRefName(refName);
      if (lastRefNum == null) {
      	createRefNum(refName);
      	lastRefNum = new Long(0);
      }
      long nextRefNum;
      if (currMaxId != null && lastRefNum < currMaxId)
      {
        nextRefNum = currMaxId + 1;
      }
      else
      {
        nextRefNum = lastRefNum + 1;
      }
      
      stmt = getPreparedStatement(dbConnection, _updateLastRefNumQuery);
      stmt.setLong(1, nextRefNum);
      stmt.setString(2, refName);
      int result = executeUpdate(stmt);
      Log.debug(Log.DB, "[KeyGenDAO.getNextId] for refName["+refName+"] result="+result);
      return nextRefNum;
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[KeyGenDAO.getNextId]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
	}

  /*
  public synchronized long getNextId(String refName, Collection excludeKeySet) throws Exception
  {
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      Long lastRefNum = findByRefName(refName);
      if (lastRefNum == null) {
        createRefNum(refName);
        lastRefNum = new Long(0);
      }
      long nextRefNum = lastRefNum.longValue() + 1;
      while (excludeKeySet != null &&
          excludeKeySet.contains(new Long(nextRefNum)))
      {
        nextRefNum++;
      }
      
      stmt = getPreparedStatement(dbConnection, _updateLastRefNumQuery);
      stmt.setLong(1, nextRefNum);
      stmt.setString(2, refName);
      int result = executeUpdate(stmt);
      Log.debug(Log.DB, "[KeyGenDAO.getNextId] for refName["+refName+"] result="+result);
      return nextRefNum;
    }
    catch(Exception ex)
    {
      Log.err(Log.DB, "[KeyGenDAO.getNextId]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }
  */
}
