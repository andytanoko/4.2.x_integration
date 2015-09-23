/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HttpBackendConnStore.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.util.config.ConfigurationStore;

/**
 * @author i00107
 * This class is for retrieving the Http connection info for identified targets.
 */
public class HttpBackendConnStore
{
  private static HttpBackendConnStore _self = null;
  private Hashtable<String, HttpTargetConnInfo> _targetConnInfos;
  private Hashtable<String, Properties> _docHeaders;
  private ProxyAuthConnInfo _proxyAuthConnInfo;
  
  private HttpBackendConnStore()
  {
    _targetConnInfos = new Hashtable<String, HttpTargetConnInfo>();
    _docHeaders = new Hashtable<String, Properties>();
    scheduleRefreshTimer();
  }
  
  public static final synchronized HttpBackendConnStore getInstance()
  {
    if (_self == null)
    {
      _self = new HttpBackendConnStore();
    }
    return _self;
  }
  
  public synchronized ProxyAuthConnInfo getProxyAuthConnectionInfo()
  {
    if (_proxyAuthConnInfo == null)
    {
      _proxyAuthConnInfo = loadProxyAuthConnectionInfo();
    }
    return _proxyAuthConnInfo;
  }
  
  private ProxyAuthConnInfo loadProxyAuthConnectionInfo()
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.HTTP_PROXY_AUTH);
    return new ProxyAuthConnInfo(props);
  }
  
  public synchronized HttpTargetConnInfo getTargetConnectionInfo(String targetId)
  {
    HttpTargetConnInfo conn =  _targetConnInfos.get(targetId);
    if (conn == null)
    {
      conn = loadTargetConnInfo(targetId);
      if (conn != null)
      {
        _targetConnInfos.put(targetId, conn);
      }
    }
    return conn;
  }
  
  private HttpTargetConnInfo loadTargetConnInfo(String targetId)
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.HTTP_TARGET_CONN+targetId);
    
    return (props.isEmpty()) ? null : new HttpTargetConnInfo(props);
  }
  
  public synchronized Properties getDocSpecificHeaders(String docType)
  {
    Properties props = _docHeaders.get(docType);
    if (props == null)
    {
      props = loadDocSpecificHeaders(docType);
      if (props.isEmpty())
      {
        props = loadDefDocHeaders();
      }
      if (props != null)
      {
        _docHeaders.put(docType, props);
      }
    }
    return (Properties)props.clone();
  }
  
  private Properties loadDocSpecificHeaders(String docType)
  {
    return ConfigurationStore.getInstance().getProperties(IConfigCats.HTTP_DOC_HEADERS+docType);
  }

  private Properties loadDefDocHeaders()
  {
    return ConfigurationStore.getInstance().getProperties(IConfigCats.HTTP_DOC_HEADERS_DEF);
  }
  
  private void scheduleRefreshTimer()
  {
    Timer timer = new Timer("RefreshHttpBackendConnStoreTimer", true);
    TimerTask task = new RefreshTimerTask();
    timer.schedule(task, 300000, 300000); //5 min interval
  }
  
  /**
   * A task scheduled to clear the cached properties.
   * Use to refresh the properties every now and then.
   * @author i00107
   */
  private class RefreshTimerTask extends TimerTask
  {
    public void run()
    {
      if (_self != null)
      {
        synchronized(_self)
        {
          _proxyAuthConnInfo = null;
          _targetConnInfos.clear();
          _docHeaders.clear();
        }
      }
    }
  }
  
}
