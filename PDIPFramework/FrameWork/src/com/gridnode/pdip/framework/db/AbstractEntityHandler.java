/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2002    Neo Sok Lay         Created
 * Apr 29 2002    Mahesh              To use ObjectName from EntityMetaInfo since
 *                                    entityName is only ShortName and ObjectName is
 *                                    Class name.
 *                                    Added a new method createNewInstance to create
 *                                    IEntity object.
 * Apr 25 2002    Neo Sok Lay         Add methods to retrieve entity through
 *                                    DAO instead of EntityBean.
 * May 02 2002    Neo Sok Lay         Catch InvocationTargetException on
 *                                    method.invoke() and throw the target
 *                                    exception.
 * May 22 2002    Neo Sok Lay         Add removeByFilter() method to iteratively
 *                                    remove() entities that fulfil a condition.
 *
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * Sep 04 2002    Neo Sok Lay         Moved direct DAO access functionalities to
 *                                    DirectDAOEntityHandler, and then extend from
 *                                    it.
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance. Reflection now support vargs,
 *                                    thus need explicity casting for null args. 
 * Dec 13 2005    Tam Wei Xiang       To add in method updateEntity(IEntity). It will return an
 *                                    IEntity Obj.                                                                      
 * Oct 31 2005    Neo Sok Lay         1. Make getHomeInterfaceClass() abstract.
 *                                    2. Remove all entity ejb map code,
 *                                       all jndi name must be specified in ejb env
 * Aug 30 2006    Tam Wei Xiang       New 'create' method. Bring over from ESTORE stream.                                      
 *                                                                       
 */
package com.gridnode.pdip.framework.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This abstract class acts as a proxy to entity beans. It should load the
 * JNDI name from entityEjbMap.properties file and retrieve the Home object for
 * the entity using JNDI lookup.
 *
 * The interface defines methods to allow the creation of the entity,
 * update of the entity, removal of the entity.
 * Finder methods include <CODE>findByPrimaryKey()</CODE> and <CODE>findByFilter
 * </CODE> which return reference to EJB Object or Collection of EJB Objects.
 * In order to get entities directly instead of EJB Object reference we need to
 * call getEntityByKey or getEntityByFilter.</p>
 *
 * Subclasses should extend to implement for Remote and Local interfaces.
 *
 * @author Neo Sok Lay
 * @version 4.0
 * @since 2.0
 */
public abstract class AbstractEntityHandler
  extends DirectDAOEntityHandler
{
  /*NSL20051031
  public static String ENTITY_EJB_MAP = IFrameworkConfig.FRAMEWORK_ENTITY_EJB_MAP_CONFIG;
  public static Properties entityEjbMap =
    ConfigurationManager.getInstance().getConfig(
      IFrameworkConfig.FRAMEWORK_ENTITY_EJB_MAP_CONFIG).getProperties();
  */    
  protected String _jndiName;
  protected boolean _isLocalContext;
  protected Object _home;

  protected Class _homeInterfaceClass;
  protected Class _proxyInterfaceClass;

  protected Method _create;
  protected Method _create2;
  protected Method _remove;
  protected Method _findByPrimaryKey;
  protected Method _findByFilter;
  protected Method _getData;
  protected Method _setData;

  /**
   * Instantiates a entity handler. Default to use local context for locating
   * the entity bean service.
   *
   * @param entityName The class name of the entity
   */
  public AbstractEntityHandler(String entityName)
  {
    this(entityName, true);
  }

  /**
   * Instantiates a entity handler.
   *
   * @param entityName The class name of the entity
   * @param isLocalContext Whether to use local context for locating the
   * entity bean service.
   */
  public AbstractEntityHandler(String entityName, boolean isLocalContext)
  {
    super(entityName);
  	String mn = "<init>";
    _isLocalContext = isLocalContext;
    try
    {
    	/*NSL20051031
      _jndiName = (String)entityEjbMap.get( _entityName+".jndi");
      Log.debug(Log.DB, "[AbstractEntityHandler.<init>] JndiName = "+_jndiName);
      */
    	_jndiName = "java:comp/env/ejb/" + _entityName; //use ejb ref
      //290402MAHESH: Changed to use entityMetaInfo.getObjectName() since entityName is only the short name
      //_entity=(IEntity)Class.forName(entityName).newInstance();
      _home = getHome();
			initInterfaceClasses();
	    initMethods();
		}
		catch (Exception e)
		{
      logError(ILogErrorCodes.DATA_HANDLER_INITIALIZE, mn, "Failed to initialize EJB interfaces: "+e.getMessage(), e);
		}
  }

  /**
   * This method creates the entity by calling the create method on
   * home interface of that entity and returns EJB object
   * @param entity The entity to create
   * @return The EJBObject or EJBLocalObject
   * @throws Exception
   */
  public Object create(IEntity entity) throws Throwable
  {
    try
    {
      return _create.invoke(_home, new Object[]{entity});
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }
  
  /**
   * TWX 30082006 Bring over from ESTORE stream. 
   * If bool is true, UID in entity is used. If bool is false, a new UID is
   * generated for entity.
   */
  public Object create(IEntity entity, Boolean bool) throws Throwable
  {
    try
    {
      return _create2.invoke(_home, new Object[]
      { entity, bool });
    } catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }
  
  /**
   * This method removes the entity by calling the remove method on
   * remote inteface of that entity
   * @param uId The Key of the entity
   * @throws Exception
   */
  public void remove(Long uId) throws Throwable
  {
    Object obj = findByPrimaryKey(uId);
    try
    {
      _remove.invoke(obj,(Object[])null);
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }

  /**
   * This method calls the remove method on the proxy interface of those
   * entitys that fulfill the specified filter.
   *
   * @param filter The condition for remove.
   * @exception Throwable
   */
  public void removeByFilter(IDataFilter filter) throws Throwable
  {
    Collection deleteList = findByFilter(filter);
    try
    {
      for (Iterator i=deleteList.iterator(); i.hasNext(); )
      {
        Object obj = i.next();
        _remove.invoke(obj, (Object[])null);
      }
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }

  }
  /**
   * This method updates the entity by calling the setData method on
   * remote/local interface of that entity
   * @param entity The entity to update
   * @throws Exception
   */
  public void update(IEntity entity) throws Throwable
  {
    Object obj = findByPrimaryKey((Long)entity.getKey());
    setData(obj, entity);
  }
  
  /**
   * Same functionality as update(IEntity entity), except it returns the entity obj
   * it just perform the update.
   * @param entity entity The entity to update
   * @return
   * @throws Throwable
   */
  public IEntity updateEntity(IEntity entity) throws Throwable
  {
  	Object obj = findByPrimaryKey((Long)entity.getKey());
    setData(obj, entity);
    return getData(obj);
  }
  
  /**
   * This method calls findByPrimaryKey on home interface of that entity
   * and returns EJB object
   * @param uId The key of the entity to find.
   * @return The EJBObject or EJBLocalObject.
   * @throws Exception
   */
  public Object findByPrimaryKey(Long uId) throws Throwable
  {
    try
    {
      return _findByPrimaryKey.invoke(_home, new Object[]{uId});
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }

  /**
   * This method calls findByFilter on home inteface of that entity
   * and returns collection of EJB objects
   * @param filter The data filter for retrieving entities.
   * @return A collection of EJBObjects or EJBLocalObjects.
   * @throws Exception
   */
  public Collection findByFilter(IDataFilter filter) throws Throwable
  {
    try
    {
      return (Collection)_findByFilter.invoke(_home,new Object[]{filter});
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }

  /**
   * This method creates the entity by calling the create method on
   * home interface of that entity and returns entity object
   * @param entity The entity to create.
   * @return The created entity, with UID set.
   * @throws Exception
   */
  public IEntity createEntity(IEntity entity) throws Throwable
  {
    Object obj=create(entity);
    return getData(obj);
  }

  /**
   * This method calls findByPrimaryKey on home interface of that entity
   * and returns entity object
   * @param uId The Key of the entity.
   * @return The entity.
   * @throws Exception
   */
  public IEntity getEntityByKey(Long uId) throws Throwable
  {
    Object obj=findByPrimaryKey(uId);
    return getData(obj);
  }


  /**
   * This method calls findByFilter on home interface of that entity
   * and returns collection of entity objects
   * @param filter The data filter for retrieving entity objects.
   * @return A collection of the entity objects that fulfil the data filter
   * conditions.
   */
  public Collection getEntityByFilter(IDataFilter filter) throws Throwable
  {
    Vector entityVect = new Vector();
    Collection coll = findByFilter(filter);
    Iterator iterator=coll.iterator();
    while(iterator.hasNext())
    {
      entityVect.add(getData(iterator.next()));
    }

    return entityVect;
  }

  /**
   * Get the entity (value object) from the EJB object.
   *
   * @param obj The EJBObject or EJBLocalObject
   * @return The value object (Entity).
   */
  protected IEntity getData(Object obj) throws Throwable
  {
    try
    {
      return (IEntity)_getData.invoke(obj, (Object[])null);
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }

  /**
   * Sets the value object into the entity bean.
   *
   * @param obj The EJB object for the entity bean.
   * @param entity The value object.
   * @exception Exception
   */
  protected void setData(Object obj,IEntity entity) throws Throwable
  {
    try
    {
      _setData.invoke(obj,new Object[]{entity});
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }
  }

  /**
   * This method creates new instance of the entity.This is a helper
   * method used to create the Entity object when we have only short name.
   * @return IEntity
   * @throws Exception
   */
  public IEntity createNewInstance() throws Exception
  {
    Object obj=null;
    try
    {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      if (loader == null)
      {
         loader = Class.forName(_objectName).getClassLoader();
      }
      obj=loader.loadClass(_objectName).newInstance();
    }
    catch(Exception ex)
    {
      Log.error(ILogErrorCodes.ENTITY_CREATE, Log.DB, "[AbstractEntityHandler.createNewInstance] Error loading class: "+_objectName, ex);
    }
    return (IEntity)obj;
  }


  protected void initInterfaceClasses() throws Exception
  {
    _homeInterfaceClass = getHomeInterfaceClass();
    _proxyInterfaceClass = getProxyInterfaceClass();
  }

  /**
   * Init the default methods for handling entity beans services.
   * Methods include:<PRE>
   * home.create
   * home.findByPrimaryKey
   * home.findByFilter
   * proxy.getData
   * proxy.setData
   * proxy.remove
   * </PRE>
   */
  protected void initMethods()
  {
  	Class iEntity = IEntity.class;
    _create = getMethod(_homeInterfaceClass,"create", new Class[]{iEntity});
    _create2 = getMethod(_homeInterfaceClass, "create", new Class[]{iEntity, Boolean.class});
    _findByPrimaryKey = getMethod(_homeInterfaceClass,"findByPrimaryKey", new Class[]{Long.class});
    _findByFilter = getMethod(_homeInterfaceClass,"findByFilter", new Class[]{IDataFilter.class});
    _getData = getMethod(_proxyInterfaceClass,"getData", null);
    _setData = getMethod(_proxyInterfaceClass,"setData", new Class[]{iEntity});
    _remove = getMethod(_proxyInterfaceClass,"remove", null);
  }

  /**
   * This method should lookup the Home interface for the EntityBean.
   *
   * @return The Home interface object for the EntityBean.
   */
  protected abstract Object getHome() throws Exception;

  /**
   * This method should return the Home interface class for the EntityBean.
   *
   * @return The Home interface class for the EntityBean.
   */ /* NSL20051031 subclasses must provide the Home interface class
  protected Class getHomeInterfaceClass() throws java.lang.Exception
  {
    return _home.getClass();
  }*/
  protected abstract Class getHomeInterfaceClass() throws Exception;

  /**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected abstract Class getProxyInterfaceClass() throws Exception;

  /**
   * TWX 30082006
   * @deprecated This method can potentially return a wrong Method obj if theClass contain more than one method with the same methodName.
   *             Pls use getMethod which allows specify the parameter types
   * 
   * Get the Method from a Class.
   *
   * @param theClass The class to get Method from.
   * @param methodName The name of the method.
   * @return The Method obtained from theClass, or <B>null</B> if no method
   * exists with the specified methodName.
   */
  @Deprecated
  protected Method getMethod(Class theClass,String methodName)
  {
    Method method = null;
    Method[] methods = theClass.getDeclaredMethods();
    for(int i=0; i<methods.length && method==null; i++)
    {
      if (methods[i].getName().equals(methodName))
        method = methods[i];
    }

    if (method==null)
    {
      Class[] interfaces = theClass.getInterfaces();
      for(int i=0; interfaces!=null && i<interfaces.length && method==null; i++)
      {
        method = getMethod(interfaces[i], methodName);
      }
    }
    return method;
  }
  
  /**
   * TWX 30082006 Bring over from ESTORE stream 
   * 
   * Get the Method from a Class with specified parameter types.
   * 
   * @param theClass The class to get Method from.
   * @param methodName The name of the method.
   * @param args contains parameter types.
   * @return The Method obtained from theClass with specified parameter types or
   *         <B>null</B> if no method exists with the specified methodName and
   *         specifid parameter types.
   */
  protected Method getMethod(Class theClass, String methodName, Class[] args)
  {
  	Method method = null;
  	try
    {
  		method = theClass.getDeclaredMethod(methodName, args);
    }
    catch(NoSuchMethodException ex)
    {
    	
    }

    if (method == null)
    {
      Class[] interfaces = theClass.getInterfaces();
      for (int i = 0; interfaces != null && i < interfaces.length
          && method == null; i++)
      {
        method = getMethod(interfaces[i], methodName, args);
      }
    }
    return method;
  }
  
  protected void logError(String errorCode, String methodName, String message, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(this.getClass().getSimpleName()).append(".").append(methodName).append("] ").append(message);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }
}


