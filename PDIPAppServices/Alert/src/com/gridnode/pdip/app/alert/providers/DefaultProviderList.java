/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProviderList.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 23 Jan 2003    Neo Sok Lay         Created.
 * 26 Feb 2003    Neo Sok Lay         Add default data providers.
 * 14 Nov 2005    Neo Sok Lay         Implement Serializable
 * 28 Feb 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.providers;

import java.io.Serializable;
import java.util.*;

/**
 * Default implementation of Provider List.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I7
 */
public class DefaultProviderList implements IProviderList, Serializable
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 9180142204535158374L;
	
	private Hashtable<String,IDataProvider> _providers = new Hashtable<String,IDataProvider>();

  /**
   * Create an empty Provider List.
   */
  public DefaultProviderList()
  {
    this(new IDataProvider[0]);
  }

  /**
   * Create a Provider List with one Data Provider.
   *
   * @param provider the Data Provider in this Provider List.
   */
  public DefaultProviderList(IDataProvider provider)
  {
    this(new IDataProvider[]{provider});
  }

  /**
   * Create a Provider List that contain all Data Providers specified.
   *
   * @param dataProviders The IDataProvider(s) to populate.
   */
  public DefaultProviderList(Vector dataProviders)
  {
    initDefaultProviders();

    for (Iterator i=dataProviders.iterator(); i.hasNext(); )
    {
      Object provider = i.next();
      if (provider instanceof IDataProvider)
        addProvider((IDataProvider)provider);
    }
  }

  /**
   * Create a Provider List that contain all Data Providers specified.
   *
   * @param dataProviders The IDataProvider(s) to populate in the Provider List.
   */
  public DefaultProviderList(IDataProvider[] dataProviders)
  {
    initDefaultProviders();
    int count = dataProviders.length;
    for (int i = 0; i < count; i++)
    {
      addProvider(dataProviders[i]);
    }
  }

  public String[] getProviderNames()
  {
    String[] names = _providers.keySet().toArray(
                       new String[_providers.size()]);

    return names;
  }

  public Vector getProviders()
  {
    Vector allProviders = new Vector(_providers.entrySet());
    return allProviders;
  }

  public IDataProvider getProvider(String name)
  {
    return _providers.get(name);
  }

  public void addProvider(IDataProvider provider)
  {
    _providers.put(provider.getName(), provider);
  }

  private void initDefaultProviders()
  {
    SystemDetails system = new SystemDetails();
    addProvider(system);
  }
}