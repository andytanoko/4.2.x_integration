/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryServiceManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.provider;

/**
 * This interface defines the methods for implementing a RegistryServiceManager
 * that manages the RegistryService(s) for a particular registry provider implementation.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryServiceManager
{
  /**
   * Gets an instance of RegistryService using the specified connection information.
   * 
   * @param connectionInfo The Connection information to use to connect to the
   * target registry provider.
   * @return An instance of RegistryService, if connection is successful.
   * @throws Exception Error connecting to the registry
   */
  IRegistryService getRegistryService(String[] connectionInfo) throws Exception;
  
  /**
   * Removes a RegistryService. This essentially closes the connection to the
   * target registry.
   * 
   * @param connectionInfo The connection information that was used to instantiate
   * the RegistryService.
   */
  void removeRegistryService(String[] connectionInfo);
}
