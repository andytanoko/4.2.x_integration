/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionContext.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 29 2002    Neo Sok Lay         Created
 * Nov 29 2002    Neo Sok Lay         Add connection lost info.
 * Dec 02 2002    Neo Sok Lay         Add connection token mechanism.
 * Apr 28 2004    Neo Sok Lay         Add getPreviousTokenType().
 *                                    Synchronize getInstance() and clearContext().
 *                                    Synchronize seizeToken(), getPreviousTokenType()
 *                                    and getCurrentTokenType() on a separate lock object
 *                                    instead of ConnectionContext instance.
 * Oct 17 2005    Neo Sok Lay         Change ConnectionToken.seize() method to
 *                                    protected access to improve performance.                                   
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import java.util.Hashtable;

/**
 * This object represents a Context that will be passed around the Delegates
 * of the Connection process. The Context allows the Delegates to share
 * information that are set and required by the other steps of the process.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class ConnectionContext
{
  public static final String CONN_LOST_LOCAL    = "LOCAL";
  public static final String CONN_LOST_NETWORK  = "NETWORK";

  static final int UNKNOWN_TOKEN                = 0;
  static final int CONNECT_TOKEN                = 1;
  static final int DISCONNECT_TOKEN             = 2;
  static final int RECONNECT_TOKEN              = 3;

  private static final ConnectionToken  _token = new ConnectionToken();
  private static final Object _lock = new Object();
  private static ConnectionContext _self = null;

  private Hashtable _delegates = new Hashtable();
  private NetworkSetting _networkSetting;
  private ConnectionSetupResult _setupResult;
//  private ConnectionStatus _myConnStatus;
  private String           _myNodeID;
  private String   _productKey;
//  private File     _masterCert;
  private String   _connectionLostType;

  private ChannelInfo _connectGmChannel;
  private Long        _keepAliveTimerID;
  private String      _myCommInfoString;

  private ConnectionContext()
  {
  }

  /**
   * Get the instance of the ConnectionContext.
   */
  public static final synchronized ConnectionContext getInstance()
  {
    if (_self == null)
    {
      _self = new ConnectionContext();
    }

    return _self;
  }

  /**
   * Clears the context. This method should be called for each
   * new initiation of the Connection process.
   */
  public static final synchronized void clearContext()
  {
    _self = null;
  }

  /**
   * Set the Delegate to handle acknowledgement of a particular event id.
   *
   * @param eventID The Event ID of the acknowledgement.
   * @param transID The trans ID of the specific message sent.
   * @param delegate The Delegate to handle the acknowledgement.
   */
  public void setDelegate(
    String eventID, String transID, IConnectionSenderDelegate delegate)
  {
    StringBuffer buff = new StringBuffer(eventID).append('-').append(transID);
    _delegates.put(buff.toString(), delegate);
  }

  /**
   * Get the Delegate to handle acknowledgement of a particular event id.
   *
   * @param evenID the Event ID of the acknowledgement
   * @param transID The trans ID of the specific message sent.
   * @return the Delegate that handles the acknowledgement, or <b>null</b> if
   * none can handle. Once returned, the Delegate will be deregistered.
   */
  public IConnectionSenderDelegate getDelegate(String eventID, String transID)
  {
    StringBuffer buff = new StringBuffer(eventID).append('-').append(transID);
    return (IConnectionSenderDelegate)_delegates.remove(buff.toString());
  }

  public void setNetworkSetting(NetworkSetting networkSetting)
  {
    _networkSetting = networkSetting;
  }

  public NetworkSetting getNetworkSetting()
  {
    return _networkSetting;
  }

  public void setConnectionSetupResult(ConnectionSetupResult setupResult)
  {
    _setupResult = setupResult;
  }

  public ConnectionSetupResult getConnectionSetupResult()
  {
    return _setupResult;
  }

//  public void setMyConnStatus(ConnectionStatus myConnStatus)
//  {
//    _myConnStatus = myConnStatus;
//  }
//
//  public ConnectionStatus getMyConnStatus()
//  {
//    return _myConnStatus;
//  }

  public void setMyNodeID(String nodeID)
  {
    _myNodeID = nodeID;
  }

  public String getMyNodeID()
  {
    return _myNodeID;
  }

  public void setProductKey(String prodKey)
  {
    _productKey = prodKey;
  }

  public String getProductKey()
  {
    return _productKey;
  }

//  public void setMasterCert(File certFile)
//  {
//    _masterCert = certFile;
//  }
//
//  public File getMasterCert()
//  {
//    return _masterCert;
//  }

  public void setConnectedGmChannel(ChannelInfo gmChannel)
  {
    _connectGmChannel = gmChannel;
  }

  public ChannelInfo getConnectedGmChannel()
  {
    return _connectGmChannel;
  }

  public void setKeepAliveTimerID(Long id)
  {
    _keepAliveTimerID = id;
  }

  public Long getKeepAliveTimerID()
  {
    return _keepAliveTimerID;
  }

  public void setMyCommInfoString(String commInfoString)
  {
    _myCommInfoString = commInfoString;
  }

  public String getMyCommInfoString()
  {
    return _myCommInfoString;
  }

  public void setConnectionLost(String connLostType)
  {
    _connectionLostType = connLostType;
  }

  public boolean isLocalConnectionLost()
  {
    boolean isLost = false;
    if (_connectionLostType != null)
      isLost = _connectionLostType.equals(CONN_LOST_LOCAL);

    return isLost;
  }

  public boolean isNetworkConnectionLost()
  {
    boolean isLost = false;
    if (_connectionLostType != null)
      isLost = _connectionLostType.equals(CONN_LOST_NETWORK);

    return isLost;
  }

  static ConnectionToken seizeToken(
    AbstractConnectionDelegate requestor, int tokenType)
  {
    synchronized (_lock)
    {
      while (!_token.isFree())
      {
        try
        {
          _token.wait();
        }
        catch (Exception ex)
        {

        }

      }
      _token.seize(requestor, tokenType);
    }

    return _token;
  }

  static int getCurrentTokenType()
  {
    synchronized (_lock)
    {
      return _token.getCurrentType();
    }
  }

  static int getPreviousTokenType()
  {
    synchronized (_lock)
    {
      return _token.getPreviousType();
    }
  }
  
  static class ConnectionToken
  {
    private AbstractConnectionDelegate _tokenOwner;
    private int                        _tokenType   = UNKNOWN_TOKEN;
    private int                        _prevTokenType = UNKNOWN_TOKEN;

    protected ConnectionToken()
    {
    }

    protected synchronized void seize(
      AbstractConnectionDelegate requestor, int tokenType)
    {
      _tokenOwner = requestor;
      _tokenType  = tokenType;
    }

    synchronized void release()
    {
      _prevTokenType = _tokenType;
      _tokenOwner = null;
      _tokenType = UNKNOWN_TOKEN;
      try
      {
        notify();
      }
      catch (Exception ex)
      {

      }

    }

    synchronized boolean isFree()
    {
      return _tokenOwner == null;
    }

    synchronized int getCurrentType()
    {
      return _tokenType;
    }

    synchronized int getPreviousType()
    {
      return _prevTokenType;
    }
  }
}