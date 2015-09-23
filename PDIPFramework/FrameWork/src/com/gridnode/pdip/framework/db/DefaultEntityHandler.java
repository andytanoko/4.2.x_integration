/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2002    Neo Sok Lay         Modify to extend from AbstractEntityHandler.
 * Oct 31 2005    Neo Sok Lay         Implement getHomeInterfaceClass().
 *                                    Override _jndiName in getHome() to get from entityejbmap 
 */
package com.gridnode.pdip.framework.db;

/**
 * <p>Title: </p>
 * <p>Description:
 *            This class acts like a proxy for entity beans.It loads the
 * JNDI name from entityEjbMap.properties file and retervies Home object for the
 * entity using JNDI lookup.
 *
 * This class allows to create the entity,update the entity,remove the entity.
 * It also allows to do findByPrimaryKey and findByFilter which returns
 * reference to Remote Object or Collection of Remote Objects.
 * In order to get entities directly insted of remote reference we need to call
 * getEntityByKey or getEntityByFilter.</p>
 *
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Mahesh
 * @version 1.0
 */

import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class DefaultEntityHandler extends AbstractEntityHandler
{
  protected EJBMetaData _metaData;

  /**
   * Instantiates a entity handler. Default to use local context for locating
   * the entity bean service.
   *
   * @param entityName The class name of the entity
   */
  public DefaultEntityHandler(String entityName)
  {
    super(entityName);
  }

  /**
   * Instantiates a entity handler.
   *
   * @param entityName The class name of the entity
   * @param isLocalContext Whether to use local context for locating the
   * entity bean service.
   */
  public DefaultEntityHandler(String entityName, boolean isLocalContext)
  {
    super(entityName, isLocalContext);
  }

  /**
   * Looks up the Home interface using Local context when <CODE>isLocalContext</CODE>
   * is true, or Client context when <CODE>isLocalContext</CODE> is false.
   *
   * @return The EJBHome interface object.
   */
  protected Object getHome() throws java.lang.Exception
  {
  	Configuration config = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_ENTITY_EJB_MAP_CONFIG);
  	_jndiName = (String)config.getString( _entityName+".jndi");
    Log.debug(Log.DB, "[DefaultEntityHandler.getHome] JndiName = "+_jndiName);
    
    return ServiceLocator.instance(_isLocalContext?
             ServiceLocator.LOCAL_CONTEXT :
             ServiceLocator.CLIENT_CONTEXT).getHome(_jndiName);
  }

  /**
   * Get the Remote interface class.
   *
   * @returns The Remote interface class.
   */
  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    _metaData = ((EJBHome)_home).getEJBMetaData();
    return _metaData.getRemoteInterfaceClass();
  }

	protected Class getHomeInterfaceClass() throws Exception
	{
		return _home.getClass();
	}
  
}