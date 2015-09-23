/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSetting.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.model;

 
/**
 * This NetworkSetting entity holds the network settings for the GridTalk server
 * host. This entity will be persistent as an Xml file instead of database table.<p>
 * The data model:<PRE>
 * ConnectionLevel   - The Connection level of the GridTalk server host.
 * LocalJmsRouter    - Local Jms Router IP address if host is not behind firewall.
 * HttpProxyServer   - Http proxy server name/ip if host is behind a proxy-restricted
 *                     firewall.
 * HttpProxyPort     - Http proxy port for the proxy server.
 * ProxyAuthUser     - Authentication user name if the proxy requires authentication.
 * ProxyAuthPassword - Authentication password for the authentication user.
 * KeepAliveInterval - Interval in minutes to inform GridMaster of "I'm Alive".
 * ResponseTimeout   - Timeout in minutes to wait for a response from the network.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class NetworkSetting
  extends    AbstractXmlEntity
  implements INetworkSetting
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -408119411590439210L;
	private Short         _connectionLevel = new Short(LEVEL_FIREWALL_NO_PROXY);
  private String        _localJmsRouter;
  private String        _httpProxyServer;
  private Integer       _httpProxyPort;
  private String        _proxyAuthUser;
  private String        _proxyAuthPassword;
  private Integer       _keepAliveInterval = new Integer(10);  // in minutes
  private Integer       _responseTimeout   = new Integer(60);    // in minutes

  public NetworkSetting()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return "Network Setting - connection level: "+getConnectionLevel();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return null;
  }

  // ***************** Getters & Setters *********************************

  public void setConnectionLevel(Short connectionLevel)
  {
    _connectionLevel = connectionLevel;
  }

  public Short getConnectionLevel()
  {
    return _connectionLevel;
  }

  public void setLocalJmsRouter(String localJmsRouter)
  {
    _localJmsRouter = localJmsRouter;
  }

  public String getLocalJmsRouter()
  {
    return _localJmsRouter;
  }

  public void setHttpProxyServer(String httpProxyServer)
  {
    _httpProxyServer = httpProxyServer;
  }

  public String getHttpProxyServer()
  {
    return _httpProxyServer;
  }

  public void setHttpProxyPort(Integer httpProxyPort)
  {
    _httpProxyPort = httpProxyPort;
  }

  public Integer getHttpProxyPort()
  {
    return _httpProxyPort;
  }

  public void setProxyAuthUser(String proxyAuthUser)
  {
    _proxyAuthUser = proxyAuthUser;
  }

  public String getProxyAuthUser()
  {
    return _proxyAuthUser;
  }

  public void setProxyAuthPassword(String proxyAuthPassword)
  {
    _proxyAuthPassword = proxyAuthPassword;
  }

  public String getProxyAuthPassword()
  {
    return _proxyAuthPassword;
  }

  public void setKeepAliveInterval(Integer keepAliveInterval)
  {
    _keepAliveInterval = keepAliveInterval;
  }

  public Integer getKeepAliveInterval()
  {
    return _keepAliveInterval;
  }

  public void setResponseTimeout(Integer responseTimeout)
  {
    _responseTimeout = responseTimeout;
  }

  public Integer getResponseTimeout()
  {
    return _responseTimeout;
  }

}