package com.gridnode.pdip.base.appinterface.data;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class AppDefinitionDoc implements java.io.Serializable {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6082140277561433948L;
	public AppDefinitionDoc() {
    _params = new Hashtable();
  }

  public String getAppName() {
    return _appName;
  }

  public void setAppName(String name) {
    try {
      Class.forName(name);
      _appName = name;
    }
    catch (ClassNotFoundException ex) {
      ex.printStackTrace();
      _appName = null;
    }
  }

  public Object getParam(String key) {
    return _params.get(key);
  }

  public void setParam(String key, Object param) {
    _params.put(key, param);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for(Enumeration en = _params.keys();en.hasMoreElements();)
    {
      Object key = en.nextElement();
      sb.append(key).append("=").append(_params.get(key)).append(",");
    }
    sb.append("]");
    return sb.toString();
  }
  
  private Hashtable _params; //parameters for the adaptor
  private String _appName; //full class name of the adaptor
}