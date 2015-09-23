/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataSourceSelector.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 * Nov 09 2005    Neo Sok Lay         Change to use ServiceLocator instead of ServiceLookup
 * Sep 07 2006    Tam Wei Xiang       Added datasource : ArchiveDB
 */
package com.gridnode.pdip.framework.db.dao;

import java.util.List;

import javax.sql.DataSource;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;
/**
 * This class is used for selecting and obtaining the Data sources for
 * database connections. At the moment, three data sources are supported: AppDB,
 * UserDB and ArchiveDB.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class DataSourceSelector
{
  public static final int APPDB  = 0;
  public static final int USERDB = 1;
  public static final int ARCHIVEDB = 2;

  private static final String DATABASE          		= IFrameworkConfig.FRAMEWORK_DATABASE_CONFIG;
  private static final String USERDB_DATASOURCE 		= IFrameworkConfig.USERDB_DATASOURCE;
  private static final String APPDB_DATASOURCE  		= IFrameworkConfig.APPDB_DATASOURCE;
  private static final String ARCHIVEDB_DATASOURCE 	= IFrameworkConfig.ARCHIVEDB_DATASOURCE; 
  private static final String APPDB_DAOS        		= IFrameworkConfig.APPDB_DAOS;
  private static final String ARCHIVEDB_DAOS     		= IFrameworkConfig.ARCHIVEDB_DAOS;
  
  private static Object _lock = new Object();
  private static DataSourceSelector _instance = null;

  private List _appDbDaos;
  private List _archiveDbDaos;
  private DataSource[] _dataSources = new DataSource[3];
  private String[]     _dataSourceNames = new String[3];

  private DataSourceSelector() throws SystemException
  {
    initConfigurations();
  }

  /**
   * Get an instance of the DataSourceSelector.
   *
   * @return An instance of DataSourceSelector.
   * @exception SystemException Error in getting a DataSourceSelector instance
   * probably problem in initializing.
   *
   * @since 2.0 I5
   */
  public static DataSourceSelector getInstance() throws SystemException
  {
    if (_instance == null)
    {
      synchronized(_lock)
      {
        if (_instance == null)
          _instance = new DataSourceSelector();
      }
    }
    return _instance;
  }

  /**
   * Initialize the configurations for the DataSourceSelector.
   *
   * @exception SystemException Unable to get the Database configurations.
   *
   * @since 2.0
   */
  private void initConfigurations() throws SystemException
  {
    try
    {
      Configuration dbConfig = ConfigurationManager.getInstance().getConfig(DATABASE);
      _dataSourceNames[USERDB] = dbConfig.getString(USERDB_DATASOURCE);
      _dataSourceNames[APPDB]  = dbConfig.getString(APPDB_DATASOURCE);
      _dataSourceNames[ARCHIVEDB] = dbConfig.getString(ARCHIVEDB_DATASOURCE);
      _appDbDaos = dbConfig.getList(APPDB_DAOS, ",");
      _archiveDbDaos = dbConfig.getList(ARCHIVEDB_DAOS, ",");
    }
    catch (Exception ex)
    {
      throw new SystemException("Unable to get db configurations", ex);
    }
  }

  /**
   * Get the DataSource for a particular entity.
   *
   * @param entityName The name of the entity (short name).
   * @return The DataSource to handle the entity.
   * @exception SystemException Unable to find the DataSource to handle the
   * entity.
   *
   * @since 2.0 I5
   */
  public DataSource getDataSource(String entityName) throws SystemException
  {
    return getDataSource(getDbID(entityName));
  }

  /**
   * Get the ID for which database that can handle the specified entity.
   *
   * @param entityName The name of the entity (short name).
   * @return And ID which identifies the database that handles the specified
   * entity.
   *
   * @since 2.0 I5
   * @see #APPDB
   * @see #USERDB
   */
  public int getDbID(String entityName)
  {
  	if(_appDbDaos != null && _appDbDaos.contains(entityName))
  	{
  		return APPDB;
  	}
  	else if(_archiveDbDaos != null && _archiveDbDaos.contains(entityName))
  	{
  		return ARCHIVEDB;
  	}
  	else
  	{
  		return USERDB;
  	}
  }

  /**
   * Get the DataSource for the specified database ID.
   *
   * @param dbID The ID of the database for which a DataSource is to be returned.
   * @return The DataSource for the specified database ID.
   * @exception SystemException Error in looking up the DataSource.
   *
   * @since 2.0 I5
   */
  public DataSource getDataSource(int dbID) throws SystemException
  {
    DataSource dataSource = null;
    if (isDbIDValid(dbID))
    {
      if (_dataSources[dbID] == null)
        _dataSources[dbID] = lookupDataSource(dbID);

        dataSource = _dataSources[dbID];
    }
    return dataSource;
  }

  /**
   * Check if the specified database ID is valid.
   *
   * @param dbID The database ID to check.
   * @return <b>true</b> if valid, <b>false</b> otherwise.
   *
   * @since 2.0 I5
   */
  private boolean isDbIDValid(int dbID)
  {
    return dbID >= 0 && dbID < 3;
  }

  /**
   * Lookup a DataSource for the specified database ID.
   *
   * @param dbID The database ID.
   * @return The DataSource found.
   * @exception SystemException Error in looking up the DataSource.
   *
   * @since 2.0 I5
   */
  private DataSource lookupDataSource(int dbID) throws SystemException
  {
    DataSource dataSource = null;
    try
    {
      dataSource =(DataSource)ServiceLocator.instance(
                    ServiceLocator.CLIENT_CONTEXT).getHome(
                      _dataSourceNames[dbID],
                      DataSource.class);
    }
    catch (Exception e)
    {
      throw new SystemException("Problem in getting DataSource " +
                _dataSourceNames[dbID], e);
    }
    return dataSource;
  }
}