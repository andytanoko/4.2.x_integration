/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommInfoFormatter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28 2002    Neo Sok Lay         Created
 * Jan 23 2003    Neo Sok Lay         GNDB00012533: Fix number of RouterIps to 3.
 */
package com.gridnode.gtas.server.connection.helpers;

/**
 * This is a helper class that helps convert communication information to and
 * from String format. This is to cater for backward compatibility with GridTalk
 * 1.x transport.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I6
 */
public class CommInfoFormatter
{
  /**
   * Current version of communication info format.
   */
  public static final String CURRENT_COMMINFO_VERSION = "030000";

  private static final String SEPARATOR                = ":";
  private static final String NULL_STRING              = "_NULL!+&yh=-ja%^_";

  /**
   * These information are needed to connect to the JMS Router Network:
   * <li>RouterIPs - IPs of the JMS Routers</li>
   * <li>Connection Level - My connection level (0 - no firewall, 1 - behind firewall)</li>
   * <li>Network Router Auth - User and Password to connect to the JMS Router Network</li>
   */
  private String[] _routerIps = new String[3];
  private int _connectionLevel = 0;
  private String[] _networkRouterAuth = new String[2];

  /**
   * This specifies how long the GridMaster should wait before timing the connection
   * out.
   */
  private int _keepAliveInterval;

  /**
   * My IP.
   */
  private String _ip;

  /**
   * These information are needed by partners to send messages to me.
   * <li>Node ID</li>
   * <li>isOnline - are we online or not?</li>
   * <li>Connected Gm Node ID - Node ID of the Gm connected to</li>
   * <li>Network Topic - Topic to send to<li>
   * <li>Local Router Topic - Topic used for receiving. For Level-0 GTs only.</li>
   * <li>Local Router Auth - Used for connecting to the local Jms Router of
   * a Level-0 GT (either by himself to receive or by partner to send messages).
   */
  private String _nodeID;
  private boolean _isOnline = false;
  private String _connectedGmNodeID;
  private String _networkTopic;
  private String _localRouterTopic;
  private String[] _localRouterAuth = new String[2];

  /**
   * CommInfo version
   */
  private String _commInfoVersion = CURRENT_COMMINFO_VERSION;

  /**
   * Type of GridNode: 1=GM, 2=GT
   */
  private String _nodeType;

  private int _currentPos = 0;

  private CommInfoFormatter()
  {
  }

  private CommInfoFormatter(String commInfoString)
  {
    parseString(commInfoString);
  }

  /**
   * Construct an instance of CommInfoFormatter for the communication info
   * of GridMaster.
   */
  public static CommInfoFormatter newGmCommInfoFormatter(
    String nodeID, String topic)
  {
    CommInfoFormatter formatter = new CommInfoFormatter();

    formatter._nodeID = nodeID;
    formatter._networkTopic = topic;
    formatter._nodeType = "1";

    return formatter;
  }

  /**
   * Construct an instance of CommInfoFormatter for the communication info
   * of this GridTalk.
   */
  public static CommInfoFormatter newMyGtCommInfoFormatter(
    String nodeID, String ip,
    String[] networkRouterAuth,
    String[] routerIps,
    String[] localRouterAuth,
    int connectionLevel, int keepAliveInterval,
    boolean isOnline, String gmNodeID,
    String networkTopic, String localRouterTopic)
  {
    CommInfoFormatter formatter = new CommInfoFormatter();

    formatter._nodeID = nodeID;
    formatter._ip     = ip;
    formatter._networkRouterAuth = networkRouterAuth;
    //formatter._routerIps = routerIps;

    int length = formatter._routerIps.length;
    if (length > routerIps.length)
      length = routerIps.length;
    System.arraycopy(routerIps, 0, formatter._routerIps, 0, length);
    for (int i=routerIps.length; i<formatter._routerIps.length; i++)
      formatter._routerIps[i] = null;

    formatter._localRouterAuth = localRouterAuth;
    formatter._connectionLevel = connectionLevel;
    formatter._keepAliveInterval = keepAliveInterval;
    formatter._isOnline = isOnline;
    formatter._connectedGmNodeID = gmNodeID;
    formatter._networkTopic = networkTopic;
    formatter._localRouterTopic = localRouterTopic;
    formatter._nodeType = "2";

    return formatter;
  }

  /**
   * Construct an instance of CommInfoFormatter for the communication info
   * of a partner GridTalk.
   */
  public static CommInfoFormatter newPartnerCommInfoFormatter(
    String nodeID, String ip,
    String[] localRouterAuth,
    int connectionLevel, int keepAliveInterval,
    boolean isOnline, String gmNodeID,
    String networkTopic, String localRouterTopic,
    String commInfoVersion)
  {
    CommInfoFormatter formatter = new CommInfoFormatter();

    formatter._nodeID = nodeID;
    formatter._ip     = ip;
    formatter._localRouterAuth = localRouterAuth;
    formatter._connectionLevel = connectionLevel;
    formatter._isOnline = isOnline;
    formatter._networkTopic = networkTopic;
    formatter._localRouterTopic = localRouterTopic;
    formatter._commInfoVersion = commInfoVersion;
    formatter._nodeType = "2";

    return formatter;
  }

  /**
   * Construct an instance of CommInfoFormatter from a formatted commInfo
   * string.
   */
  public static CommInfoFormatter toCommInfo(String commInfoString)
  {
    return new CommInfoFormatter(commInfoString);
  }

  /**
   * Parse the given string into communication info fields.
   *
   * @param commInfoString the string to parse.
   */
  private void parseString(String commInfoString)
  {
    _currentPos = 0;

    _commInfoVersion = getNextItem(commInfoString, _currentPos);
    _nodeID          = getNextItem(commInfoString, _currentPos);

    String nextField;

    _ip = getNextItem(commInfoString, _currentPos);

    for (int i=0; i<_routerIps.length; i++)
      _routerIps[i] = getNextItem(commInfoString, _currentPos);

    for (int i=0; i<_networkRouterAuth.length; i++)
      _networkRouterAuth[i] = getNextItem(commInfoString, _currentPos);

    _connectionLevel = Integer.parseInt(getNextItem(commInfoString, _currentPos));

    // first time connect auth
    getNextItem(commInfoString, _currentPos);
    getNextItem(commInfoString, _currentPos);

    for (int i=0; i<_localRouterAuth.length; i++)
      _localRouterAuth[i] = getNextItem(commInfoString, _currentPos);

    _nodeType = getNextItem(commInfoString, _currentPos);

    _keepAliveInterval = Integer.parseInt(getNextItem(commInfoString, _currentPos));
    _isOnline = Boolean.valueOf(getNextItem(commInfoString, _currentPos)).booleanValue();
    _connectedGmNodeID = getNextItem(commInfoString, _currentPos);

    // connect topic
    getNextItem(commInfoString, _currentPos);

    _networkTopic = getNextItem(commInfoString, _currentPos);
    _localRouterTopic = getNextItem(commInfoString, _currentPos);

    // gm network topic
    getNextItem(commInfoString, _currentPos);
    // auto-config topic
    getNextItem(commInfoString, _currentPos );
  }

  public boolean isOnline()
  {
    return _isOnline;
  }

  public String getGridNodeID()
  {
    return _nodeID;
  }

  public String getIP()
  {
    return _ip;
  }

  public void setIP(String ip)
  {
    _ip = ip;
  }

  public String[] getNetworkRouterAuth()
  {
    return _networkRouterAuth;
  }

  public String[] getLocalRouterAuth()
  {
    return _localRouterAuth;
  }

  public String getNetworkTopic()
  {
    return _networkTopic;
  }

  public String getLocalRouterTopic()
  {
    return _localRouterTopic;
  }

  public int getConnectionLevel()
  {
    return _connectionLevel;
  }

  public String getConnectedGmNodeID()
  {
    return _connectedGmNodeID;
  }

  public void setConnectedGmNodeID(String gmNodeID)
  {
    _connectedGmNodeID = gmNodeID;
  }

  public String getCommInfoVersion()
  {
    return _commInfoVersion;
  }

  /**
   * Convert the Communication info fields into String format.
   *
   * @return formatted CommInfo string.
   */
  public String toString()
  {
    StringBuffer buff = new StringBuffer();

    setNextItem(buff, _commInfoVersion);
    setNextItem(buff, _nodeID);
    setNextItem(buff, _ip);

    for (int i=0; i<_routerIps.length; i++)
      setNextItem(buff, _routerIps[i]);
    for (int i=0; i<_networkRouterAuth.length; i++)
      setNextItem(buff, _networkRouterAuth[i]);

    int mappedConnLevel = _connectionLevel;
    if (_connectionLevel > 1)
      mappedConnLevel -= 1;
    setNextItem(buff, String.valueOf(mappedConnLevel));

    // auto-config auth
    for (int i=0; i<2; i++)
      setNextItem(buff, null);

    for (int i=0; i<_localRouterAuth.length; i++)
      setNextItem(buff, _localRouterAuth[i]);

    setNextItem(buff, _nodeType);

    setNextItem(buff, String.valueOf(_keepAliveInterval));
    setNextItem(buff, String.valueOf(_isOnline));
    setNextItem(buff, _connectedGmNodeID);

    // connect topic
    setNextItem(buff, null);

    setNextItem(buff, _networkTopic);
    setNextItem(buff, _localRouterTopic);

    // gm network topic
    setNextItem(buff, null);
    // auto config topic
    setNextItem(buff, null);

    return buff.toString();
  }

  private void setNextItem(StringBuffer buff, String data)
  {
    if (data == null)
      data = NULL_STRING;

    buff.append(data.length()).append(SEPARATOR).append(data);
  }

  private String getNextItem(String val, int currentPos)
  {
    // end position for data length = start position of separator
    int endPos = val.indexOf(SEPARATOR, currentPos);
    int len = Integer.parseInt(val.substring(currentPos, endPos));

    // start position of data = end position of separator
    int startPos = endPos + SEPARATOR.length();
    endPos = startPos + len;

    String data = val.substring(startPos, endPos);

    if (data.compareTo(NULL_STRING)==0)
      data = null;

    // advance the pointer
    _currentPos = endPos;

    return data;
  }
}