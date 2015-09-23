/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.connection;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
import com.gridnode.gtas.client.utils.StaticUtils;

public class NetworkSettingAForm extends GTActionFormBase
{
  private String _connectionLevel;
  private String _localJmsRouter;
  private String _httpProxyServer;
  private String _httpProxyPort;
  private String _proxyAuthUser;
  private String _proxyAuthPassword;
  private String _keepAliveInterval;
  private String _responseTimeout;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    
  }

  public String getConnectionLevel()
  { return _connectionLevel; }

  public Short getConnectionLevelShort()
  { return StaticUtils.shortValue(_connectionLevel); }

  public void setConnectionLevel(String connectionLevel)
  { _connectionLevel=connectionLevel; }

  public String getLocalJmsRouter()
  { return _localJmsRouter; }

  public void setLocalJmsRouter(String localJmsRouter)
  { _localJmsRouter=localJmsRouter; }

  public String getHttpProxyServer()
  { return _httpProxyServer; }

  public void setHttpProxyServer(String httpProxyServer)
  { _httpProxyServer=httpProxyServer; }

  public String getHttpProxyPort()
  { return _httpProxyPort; }

  public void setHttpProxyPort(String httpProxyPort)
  { _httpProxyPort=httpProxyPort; }

  public String getProxyAuthUser()
  { return _proxyAuthUser; }

  public void setProxyAuthUser(String proxyAuthUser)
  { _proxyAuthUser=proxyAuthUser; }

  public String getProxyAuthPassword()
  { return _proxyAuthPassword; }

  public void setProxyAuthPassword(String proxyAuthPassword)
  { _proxyAuthPassword=proxyAuthPassword; }

  public String getKeepAliveInterval()
  { return _keepAliveInterval; }

  public void setKeepAliveInterval(String keepAliveInterval)
  { _keepAliveInterval=keepAliveInterval; }

  public String getResponseTimeout()
  { return _responseTimeout; }

  public void setResponseTimeout(String responseTimeout)
  { _responseTimeout=responseTimeout; }
}