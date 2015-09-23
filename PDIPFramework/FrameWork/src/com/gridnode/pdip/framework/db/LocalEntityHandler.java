/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocalEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Use ServiceLocator instead of ServiceLookup
 *                                    2. Make the class abstract
 *                                    3. Do not allow subclass to override getHome()
 *                                    4. Remove default implementation for getProxyInterfaceClass()
 */
package com.gridnode.pdip.framework.db;

import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This acts as a Local proxy to the entity bean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class LocalEntityHandler extends AbstractEntityHandler
{
  public LocalEntityHandler(String entityName)
  {
    super(entityName);
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   */
  protected final Object getHome() throws java.lang.Exception
  {
  	Class homeClass = getHomeInterfaceClass();
  	return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(homeClass.getName(), 
  	                                                                     getHomeInterfaceClass());

    //return ServiceLookup.getInstance(ServiceLookup.LOCAL_CONTEXT).lookup(_jndiName);
  }

  /**
   * Get the Local interface class. The class name of the local interface is to
   * be defined in the entityEjbMap.properties file as:
   * <entity_class_name>.local=<local_interface_class_name>
   *
   * @returns The Local interface class.
   *//*
  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    String localInterfaceName = (String)entityEjbMap.get(_entityName+".local");
    return Class.forName(localInterfaceName);
  }*/
}