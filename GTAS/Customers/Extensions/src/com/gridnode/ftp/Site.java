package com.gridnode.ftp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Map;
import java.util.HashMap;

public class Site
{
  private String _name = null;
  private Map _keyValues = new HashMap();
  public Site()
  {
  }

  public Site(String name)
  {
    this._name = name;
  }

  public void setProperty(String key,String value)
  {
    _keyValues.put(key.toLowerCase(),value);
  }

  public String getProperty(String key)
  {
    if (key != null && _keyValues.containsKey(key.toLowerCase()))
    	return (String)_keyValues.get(key);
    else
    	return null;
  }

  public String[] getKeys()
  {
    return (String[])_keyValues.keySet().toArray(new String[]{});
  }

}