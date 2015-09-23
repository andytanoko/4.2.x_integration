/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MetaInfoFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Enhance to act as a proxy to EntityMetaInfoBean.
 *                                    Lazy load the EntityMetaInfo on demand.
 * Nov 02 2005    Neo Sok Lay         Change to load EMI from session bean MetaInfoBean
 * Dec 22 2005    Neo Sok Lay         To use config property to load from session bean or entity bean.
 *                                    Add method: getFieldMetaInfoByLabel()
 * Feb 07 2007		Alain Ah Ming				Use new error codes                                   
 */
package com.gridnode.pdip.framework.db.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * Loads and caches the loaded EntityMetaInfo(s) for the entities used by
 * the application.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class MetaInfoFactory
  implements Serializable, IMetaInfoFactory
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5485543437232505596L;
	private static final Object    _lock = new Object();
  private static MetaInfoFactory _self = null;

  private HashMap<String,EntityMetaInfo> _entityMap = new HashMap<String,EntityMetaInfo>();
  private HashMap<String,String> _entityNameMap = new HashMap<String,String>();
  private boolean _useEntityBean = true;

  private MetaInfoFactory()
  {
  	loadConfig();
  }

  public static MetaInfoFactory getInstance()
  {
    if (_self == null)
    {
      synchronized(_lock)
      {
        if (_self == null)
          _self = new MetaInfoFactory();
      }
    }
    return _self;
  }
  
  private void loadConfig()
  {
    try
    {
      Configuration dbConfig = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_DATABASE_CONFIG);
      _useEntityBean = dbConfig.getBoolean(IFrameworkConfig.CMP_ON , true);
    }
    catch (Exception ex)
    {
    	Log.error(ILogErrorCodes.CONFIGURATION_LOAD, 
    	          Log.DB, 
    	          "[MetaInfoFactory.loadConfig] Unable to load config. Setting cmp.on to true. Unexpected error: "+ex.getMessage(), 
    	          ex);
    }
  }

  /**
   * Get the EntityMetaInfo for the specified entity.
   *
   * @param objectName Full qualified class name of the entity.
   *
   * @return the EntityMetaInfo of the specified entity, or <b>null</b> if
   * none exists.
   */
  public EntityMetaInfo getEntityMetaInfo(String objectName)
  {
    EntityMetaInfo emi = (EntityMetaInfo)_entityMap.get(objectName);
    if (emi == null)
      emi = loadEntityMetaInfoByObjectName(objectName);

    return emi;
  }

  /**
   * Get the EntityMetaInfo for the specified entity.
   *
   * @param entityName Name of the entity.
   *
   * @return the EntityMetaInfo of the specified entity, or <b>null</b> if
   * none exists.
   */
  public EntityMetaInfo getMetaInfoFor(String entityName)
  {
    String objectName = (String)_entityNameMap.get(entityName);

    EntityMetaInfo emi = (objectName == null) ?
                         loadEntityMetaInfoByEntityName(entityName):
                         (EntityMetaInfo)_entityMap.get(objectName);

    return emi;
  }

  /**
   * Get all currently loaded/cached EntityMetaInfo from the factory.
   *
   * @return All EntityMetaInfo(s) cached.
   */
  public EntityMetaInfo[] getAllMetaInfo()
  {
    return (EntityMetaInfo[])_entityMap.values().toArray(new EntityMetaInfo[_entityMap.size()]);
  }

  /**
   * Add an EntityMetaInfo into the factory. This does not add it to the
   * database. If there is existing EntityMetaInfo cached with the same
   * ObjectName/EntityName, it will be replaced by this metaInfo.
   *
   * @param metaInfo The EntityMetaInfo to add to the factory.
   */
  public void addEntityMetaInfo(EntityMetaInfo metaInfo)
  {
    _entityMap.put(metaInfo.getObjectName(), metaInfo);

    Log.debug(Log.DB, "[MetaInfoFactory.addEntityMetaInfo] "+
      "Add mapping for EntityName "+metaInfo.getEntityName()+
      " to ObjectName "+metaInfo.getObjectName());
    _entityNameMap.put(metaInfo.getEntityName(), metaInfo.getObjectName());
  }

  /**
   * Find the meta info for a field in an entity.
   *
   * @param entityName Name of the entity class
   * @param fieldObjName Declared name of the field in the entity.
   * @return The field meta info found, or <B>null</B> if not found.
   */
  public FieldMetaInfo getMetaInfoFor(String entityName, String fieldObjName)
  {
    FieldMetaInfo fmi = null;
    EntityMetaInfo emi = getMetaInfoFor(entityName);
    if (emi != null)
      fmi = emi.findFieldMetaInfo(fieldObjName);
    return fmi;
  }

  /**
   * Retrieve the fieldmetainfos with the specified label
   * @param label The label
   * @return Collection of FieldMetaInfo objects found.
   */
  public Collection getFieldMetaInfoByLabel(String label)
  {
  	Collection fmi = null;
  	try
  	{
	  	if (_useEntityBean)
	  	{
	  		Collection results = getFieldMetaInfoHome().findByLabel(label);
	  		fmi = new ArrayList();
	  		for (Iterator i=results.iterator(); i.hasNext(); )
	  		{
	  			fmi.add(((IFieldMetaInfoLocalObj)i.next()).getData());
	  		}
	  	}
	  	else
	  	{
	  		fmi = getMetaInfoObj().findFieldMetaInfoByLabel(label);
	  	}
  	}
  	catch (Exception ex)
  	{
      Log.error(ILogErrorCodes.FIELD_META_INFO_RETRIEVE, Log.DB, "[MetaInfoFactory.getFieldMetaInfoByLabel] Error ", ex);
  		
  	}
  	return fmi;
  }
  
  /**
   * Load the EntityMetaInfo from the database and cache it.
   *
   * @param entityName Name of the Entity to load EntityMetaInfo.
   * for.
   */
  private EntityMetaInfo loadEntityMetaInfoByEntityName(String entityName)
  {
    EntityMetaInfo emi = null;

    try
    {
    	if (_useEntityBean)
    	{
        emi = getEntityMetaInfoHome().findByEntityName(entityName).getData();
    	}
    	else
    	{
    		emi = getMetaInfoObj().findByEntityName(entityName);
    	}

      addEntityMetaInfo(emi);
    }
    catch (Throwable t)
    {
      Log.error(ILogErrorCodes.ENTITY_META_INFO_READ,Log.DB, "[MetaInfoFactory.loadEntityMetaInfoByEntityName] Error ", t);
    }

    return emi;
  }

  /**
   * Load the EntityMetaInfo from the database and cache it.
   *
   * @param objectName Full qualified class name of the Entity to load EntityMetaInfo
   * for.
   */
  private EntityMetaInfo loadEntityMetaInfoByObjectName(String objectName)
  {
    EntityMetaInfo emi = null;

    try
    {
    	if (_useEntityBean)
    	{
        emi = getEntityMetaInfoHome().findByPrimaryKey(objectName).getData();
    	}
    	else
    	{
    		emi = getMetaInfoObj().findByObjectName(objectName);
    	}

      addEntityMetaInfo(emi);
    }
    catch (Throwable t)
    {
      Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, Log.DB, "[MetaInfoFactory.loadEntityMetaInfoByObjectName] Error ", t);
    }

    return emi;
  }

  private IMetaInfoObj getMetaInfoObj()
  throws ServiceLookupException
{
  return (IMetaInfoObj)ServiceLocator.instance(
           ServiceLocator.CLIENT_CONTEXT).getObj(
            IMetaInfoHome.class.getName(),
            IMetaInfoHome.class,
            new Object[0]);
}

  /**
   * Obtain the Home interface for the EntityMetaInfo bean.
   * @return the Home interface of the EntityMetaInfo bean.
   */
  private IEntityMetaInfoHome getEntityMetaInfoHome()
    throws ServiceLookupException
  {
    return (IEntityMetaInfoHome)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getHome(
              IEntityMetaInfoHome.class.getName(),
              IEntityMetaInfoHome.class);
  }
  
  private IFieldMetaInfoLocalHome getFieldMetaInfoHome() throws Exception
  {
    return (IFieldMetaInfoLocalHome) ServiceLocator
      .instance(ServiceLocator.LOCAL_CONTEXT)
      .getHome(
        IFieldMetaInfoLocalHome.class.getName(),
        IFieldMetaInfoLocalHome.class);
  }

}