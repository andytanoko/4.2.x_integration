/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProviderList.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 23 Jan 2003    Neo Sok Lay         Created.
 * 17 Apr 2003    Neo Sok Lay         Add addProvider() method.
 */
package com.gridnode.pdip.app.alert.providers;

import java.util.Vector;

/**
 * Interface for providing list of IDataProviders.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IProviderList
{
  /**
   * Returns the Names of all Data Providers in this Provider list.
   *
   * @return           all the providers for this Provider List
   * @since            2.0 I7
   */
  public String[] getProviderNames();

  /**
   * Return a particular <code>IDataProvider</code>.
   *
   * @param            name the name of the data provider to return.
   * @return           the provider
   * @since            2.0 I7
   */
  public IDataProvider getProvider(String name);

  /**
   * Get all the Data Provider(s) in this Provider List.
   */
  public Vector getProviders();

  /**
   * Add a data provider to the list.
   *
   * @param provider The data provider to add.
   */
  public void addProvider(IDataProvider provider);

}