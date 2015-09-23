/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EStoreHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 1, 2005    Tam Wei Xiang       Created
 * Oct 05 2005    Lim Soon Hsiung     Added EStoreException helper methods and method 
 *   to get Alert manager
 * Jan 19 2005    Tam Wei Xiang       Added in new properties for email alert
 * Sep 08 2006    Tam Wei Xiang       The all estore config will be moved into dbarchive.config.
 *                                    We only retain the estore.folder.maxFile properties, all
 *                                    other are discarded.
 */
package com.gridnode.gtas.server.dbarchive.helpers;

/**
*	A helper class that allows us to retrieve the dbarchive's propertiy
*
* @author Tam Wei Xiang
* 
* @version 1.0
* @since 1.0
*/

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class EStoreHelper
{ 
	private static EStoreHelper _esHelper;
	private Configuration _dbarchiveConfig;
	private final static String CONFIG_DBARCHIVE = "dbarchive";
	private final String ESTORE_FOLDER_TOTAL_FILE = "dbarchive.estore.folder.maxFile";
	
	private EStoreHelper() throws Exception 
	{
		_dbarchiveConfig = ConfigurationManager.getInstance().getConfig(CONFIG_DBARCHIVE);
		
		if(_dbarchiveConfig == null)
		{
			throw new Exception("dbarchive configuration file not found.");
		}
	}
	
	public static synchronized EStoreHelper getInstance() throws Exception
	{
		if(_esHelper == null)
		{
			_esHelper = new EStoreHelper();
		}
		return _esHelper;
	}
	
	public int getTotalFileInFolder()
	{
		return _dbarchiveConfig.getInt(ESTORE_FOLDER_TOTAL_FILE);
	}
	
  public static IAlertManagerObj getAlertManager()  throws ServiceLookupException 
  {
    return (IAlertManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
    IAlertManagerHome.class.getName(),
    IAlertManagerHome.class,
      new Object[0]);
  } 
}
