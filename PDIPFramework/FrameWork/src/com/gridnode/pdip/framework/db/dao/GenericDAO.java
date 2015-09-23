/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenericDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2005    i00107              Created
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception
 */

package com.gridnode.pdip.framework.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

import javax.sql.DataSource;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * Implementation for generic DAO access base on properties
 * @author i00107
 * @since
 * @version GT 4.0 VAN
 */
public abstract class GenericDAO
{

	protected Properties _props;
	protected String _dataName;
	
	/**
	 * Construct a DAO
	 * @param daoPropsName The name of the properties to load and configure the DAO. The properties is loaded from the
	 * classpath.
	 * @param dataName The Name of the dataset to access
	 * @throws Exception
	 */
	protected GenericDAO(String daoPropsName, String dataName) throws Exception
	{
		_dataName = dataName;
		_props = new Properties();
		_props.load(getClass().getClassLoader().getResourceAsStream(daoPropsName));
		initQueries();
		initCols();
	}
	
	/**
	 * Initialise the query statements used by the DAO
	 * @throws Exception Unable to load the query statements
	 */
	protected abstract void initQueries() throws Exception;

	/**
	 * Init the columns of the dataset
	 * @throws Exception Unable to init the columns of the dataset
	 */
	protected abstract void initCols() throws Exception;
	
	/**
	 * Get the value of a property from the dao properties file
	 * @param propertyName The name of the property
	 * @return The value of the property defined in the dao properties file
	 * @throws SystemException Property is not defined in the dao properties file
	 */
	protected String getNonNullProperty(String propertyName) throws SystemException
	{
		String val = _props.getProperty(propertyName, null);
		if (val == null)
		{
			throw new SystemException(propertyName+" not found!", null);
		}
		return val;
	}
	
	/**
	 * Get the values of multiple properties from the dao properties file
	 * @param propertyNames Names of the properties
	 * @return The values of the corresponding property defined in the dao properties file
	 * @throws SystemException Any of the property is not defined in the dao properties file
	 */
	protected String[] getNonNullProperties(String[] propertyNames) throws SystemException
	{
		String[] vals = new String[propertyNames.length];
		for (int i=0; i<propertyNames.length; i++)
		{
			vals[i] = getNonNullProperty(propertyNames[i]);
		}
		return vals;
	}
	
	/**
	 * Get the value of a property from the dao properties file.
	 * @param propertyName The name of the property
	 * @return The value of the property defined in the dao properties file, or <b>null</b>
	 * if the property is not defined
	 */
	protected String getNullableProperty(String propertyName)
	{
		return _props.getProperty(propertyName, null);
	}

	/**
	 * Get the values of multiple properties from the dao properties file
	 * @param propertyNames Names of the properties
	 * @return The values of the corresponding property defined in the dao properties file.
	 * If a property is not defined, its corresponding value is <b>null</b>
	 */
	protected String[] getNullableProperties(String[] propertyNames)
	{
		String[] vals = new String[propertyNames.length];
		for (int i=0; i<propertyNames.length; i++)
		{
			vals[i] = getNullableProperty(propertyNames[i]);
		}
		return vals;
	}

	/**
	 * The data source used to access the dataset
	 * @return
	 * @throws SystemException
	 */
	protected DataSource getDataSource() throws SystemException
	{
		return DataSourceSelector.getInstance().getDataSource(_dataName);
	}
	
	/**
	 * Get a connection to the datasource
	 * @return A connection to the datasource
	 * @throws Exception Unable to get connection to the datasource
	 */
	protected Connection getConnection() throws Exception
  {
    return getDataSource().getConnection();
  }

	/**
	 * Close the statement and connection
	 * @param stmt The statement to close
	 * @param conn The connection to clone
	 */
	protected void releaseResources(Statement stmt,Connection conn)
  {
    closeStatement(stmt);
    closeConnection(conn);
  }

	/**
	 * Close the result set
	 * @param rs The result set to close
	 */
	protected void closeResultSet(ResultSet rs)
  {
    try
    {
      if(rs!=null)
        rs.close();
    }
    catch(Exception e)
    {
    }
  }

	/**
	 * Close the statement
	 * @param stmt The statement to close
	 */
	protected void closeStatement(Statement stmt)
  {
    try
    {
      if(stmt!=null)
          stmt.close();
    }
    catch(Exception e)
    {
    }
  }

	/**
	 * Close the connection
	 * @param conn The connection to close
	 */
	protected void closeConnection(Connection conn)
  {
    try
    {
      if(conn!=null)
        conn.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

	/**
	 * Get a prepared statement based on the specified query statement
	 * @param dbConnection The datasource connection to use
	 * @param sqlQuery The query statement
	 * @return A prepared statement
	 * @throws Exception unable to create a preparedstatment from the specified query statement
	 */
	protected PreparedStatement getPreparedStatement(Connection dbConnection,String sqlQuery) throws Exception
  {
  	PreparedStatement stmt = dbConnection.prepareStatement(sqlQuery);
  	return stmt;
  }

	/**
	 * Execute the prepared statement as an update query
	 * @param stmt The prepared statement to execute
	 * @return The return value of the update
	 * @see {@link PreparedStatement.executeUpdate}
	 * @throws Exception
	 */
  protected int executeUpdate(PreparedStatement stmt) throws Exception
  {
    return stmt.executeUpdate();
  }

  /**
   * Get the value of a column from the result set as a boolean value
   * @param col The name of the column
   * @param rs The result set
   * @return The column value as boolean.
   * @throws Exception
   */
  protected boolean getBoolean(String col, ResultSet rs) throws Exception
  {
  	Boolean bool = (Boolean)AbstractEntity.convert(rs.getObject(col), "java.lang.Boolean");
  	return bool.booleanValue();
  }

  /**
   * Execute the prepared statement as a select query i.e. returns data
   * @param stmt The prepared statement to execute
   * @return Collection of data objects retrieved
   * @throws Exception
   */
  protected Collection executeQuery(PreparedStatement stmt) throws Exception
  {
    Vector entityCol=new Vector();
    ResultSet rs=null;
    try
    {
      rs=stmt.executeQuery();
      while(rs!=null && rs.next())
      {
        entityCol.add(getDataFromRs(rs));
      }
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[GenericDAO.executeQuery] Database error ", e);
      throw e;
    }
    finally
    {
      closeResultSet(rs);
    }
    return entityCol;
  }

  /**
   * Convert the current result row to a data object
   * @param rs The result set
   * @return A data object constructed from the values of the current result set
   * @throws Exception Unable to construct data object from the result set
   */
  protected abstract Object getDataFromRs(ResultSet rs) throws Exception;
}
