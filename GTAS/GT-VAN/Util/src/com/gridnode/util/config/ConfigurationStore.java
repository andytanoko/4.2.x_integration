/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigurationStore.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8, 2006    i00107              Created
 * Mar 14, 2007   i00118			  Added updateProperties() for housekeep report
 * Nov 07, 2009   Tam Wei Xiang       #1105 - Added method lockProperty(...)
 */

package com.gridnode.util.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author i00107
 * This class is responsible for retrieving configuration properties for
 * different categories
 */
public class ConfigurationStore
{
  private static ConfigurationStore _self = null;
  private PropertyDAO _dao;
  
  private ConfigurationStore()
  {
    _dao = new PropertyDAO();
  }
  
  public static final synchronized ConfigurationStore getInstance()
  {
    if (_self == null)
    {
      _self = new ConfigurationStore();
    }
    return _self;
  }
  
  /**
   * Get a property
   * @param cat The category of the configuration property
   * @param propKey The property key
   * @param defVal The default value to return if the requested property
   * does not exist.
   * @return The property value.
   */
  public String getProperty(String cat, String propKey, String defVal)
  {
    Property prop = _dao.getProperty(cat, propKey);
    if (prop == null)
    {
      return defVal;
    }
    else
    {
      return prop.getValue();
    }
  }
  
  /**
   * Get a property which is an integer.
   * @param cat The category of the configuration property
   * @param propKey The property key
   * @param defVal The default value to return if the requested property
   * does not exist
   * @return The property value.
   */
  public int getIntProperty(String cat, String propKey, int defVal)
  {
    String val = getProperty(cat, propKey, null);
    if (val == null)
    {
      return defVal;
    }
    else
    {
      try
      {
        return Integer.parseInt(val);
      }
      catch (NumberFormatException ex)
      {
        System.err.println("Invalid number format: "+val);
        return defVal;
      }
    }
  }

  public List<String> getListProperty(String cat, String propKey, String delim)
  {
    List<String> list = new ArrayList<String>();
    String val = getProperty(cat, propKey, null);
    if (val != null)
    {
      StringTokenizer stoken = new StringTokenizer(val, delim);
      while (stoken.hasMoreTokens())
      {
        list.add(stoken.nextToken());
      }
    }
    return list;
  }
  
  
  /**
   * Get the configuration properties for a category
   * @param cat The category of the configuration properties
   * @return The Properties for a category
   */
  public Properties getProperties(String cat)
  {
    Properties props = new Properties();
    List propList = _dao.getPropertyList(cat);
    if (propList != null)
    {
      for (Object obj: propList)
      {
        Property prop = (Property)obj;
        if (prop.getValue() != null)
        {
          props.put(prop.getKey(), prop.getValue());
        }
      }
    }
    
    return props;
  }
  
  /**
   * Update the configuration properties for a category 
   * @param cat The category of the configuration properties
   * @param key The property key of the configuration properties
   * @param value The new value of the configuration properties
   * @return status
   */
  public boolean updateProperties(String cat, String key, String value)
  {
	boolean status = false;
	int result = _dao.updateProperty(cat, key, value);
	if(result != -1)
	{
	  status = true;
	}
	return status;
  }
  
  /**
   * #1105 - TWX 20091107 Acquire a row lock on the Property identified by the given cat and key.
   * @param cat The category of the configuration properties
   * @param key The property key of the configuration properties
   * @return true if the db can lock the given property record. false if other thread already locking the record.
   * @throws Throwable if system level exception occured
   */
  public boolean lockProperty(String cat, String key) throws Throwable
  {
    return _dao.lockPropertyNoWait(cat, key);
  }
}
