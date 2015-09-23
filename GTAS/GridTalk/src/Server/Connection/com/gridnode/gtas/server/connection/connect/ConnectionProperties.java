/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 * Dec 02 2002    Neo Sok Lay         Add properties for Reconnect.
 * Feb 06 2004    Neo Sok Lay         Add AutoConnectOnStartup property.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.helpers.IConnectionConfig;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.db.DataObject;

import java.io.File;

/**
 * This data object contains the necessary properties to carry out the
 * Connection process.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class ConnectionProperties extends DataObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -752767284980827930L;

	/**
   * Timeout to receiving acknowledgement for keep alive message. In Minutes.
   */
  private int _keepAliveTimeout;

  /**
   * Timeout to receive acknowledgement for connect message. In Minutes.
   */
  private int _connectTimeout;

  /**
   * Maximum number of attempts for reconnection. If < 0, reconnection will
   * be attempted indefinitely until re-established or interrupted by
   * disconnect or normal connect.
   */
  private int _reconnectRetries;

  /**
   * Time interval before next reconnect is attempted after an unsuccessful
   * attempt. In Seconds.
   */
  private int _reconnectInterval;

  /**
   * Whether to Auto connect upon system startup.
   */
  private boolean _autoConnectOnStartup;
  
  private String _connectEventId;
  private String _connectAckEventId;
  private String _keepAliveEventId;
  private String _keepAliveAckEventId;
  private String _retrievePendingEventId;
  private String _retrievePendingAckEventId;
  private String _retrievePartnerStatusEventId;
  private String _retrievePartnerStatusAckEventId;
  private String _onlineNotificationEventId;
  private String _onlineNotificationAckEventId;
  private String _disconnectEventId;
  private String _disconnectAckEventId;
  private String _offlineNotificationEventId;
  private String _offlineNotificationAckEventId;
  private String _expiredNotificationEventId;
  private String _confirmAliveEventId;
  private String _confirmAliveAckEventId;

  private boolean _isTest = false;

  public ConnectionProperties()
  {
  }

  public void setKeepAliveTimeout(int timeout)
  {
    _keepAliveTimeout = timeout;
  }

  public int getKeepAliveTimeout()
  {
    if (_keepAliveTimeout <= 0)
      _keepAliveTimeout = 3;
    return _keepAliveTimeout;
  }

  public void setConnectTimeout(int timeout)
  {
    _connectTimeout = timeout;
  }

  public int getConnectTimeout()
  {
    if (_connectTimeout <= 0)
      _connectTimeout = 2;
    return _connectTimeout;
  }

  public void setIsTest(boolean isTest)
  {
    _isTest = isTest;
  }

  public boolean getIsTest()
  {
    return _isTest;
  }

  public void setConnectEventId(String eventId)
  {
    _connectEventId = eventId;
  }

  public String getConnectEventId()
  {
    return _connectEventId;
  }

  public void setConnectAckEventId(String eventId)
  {
    _connectAckEventId = eventId;
  }

  public String getConnectAckEventId()
  {
    return _connectAckEventId;
  }

  public void setKeepAliveEventId(String eventId)
  {
    _keepAliveEventId = eventId;
  }

  public String getKeepAliveEventId()
  {
    return _keepAliveEventId;
  }

  public void setKeepAliveAckEventId(String eventId)
  {
    _keepAliveAckEventId = eventId;
  }

  public String getKeepAliveAckEventId()
  {
    return _keepAliveAckEventId;
  }

  public void setRetrievePendingEventId(String eventId)
  {
    _retrievePendingEventId = eventId;
  }

  public String getRetrievePendingEventId()
  {
    return _retrievePendingEventId;
  }

  public void setRetrievePendingAckEventId(String eventId)
  {
    _retrievePendingAckEventId = eventId;
  }

  public String getRetrievePendingAckEventId()
  {
    return _retrievePendingAckEventId;
  }

  public void setRetrievePartnerStatusEventId(String eventId)
  {
    _retrievePartnerStatusEventId = eventId;
  }

  public String getRetrievePartnerStatusEventId()
  {
    return _retrievePartnerStatusEventId;
  }

  public void setRetrievePartnerStatusAckEventId(String eventId)
  {
    _retrievePartnerStatusAckEventId = eventId;
  }

  public String getRetrievePartnerStatusAckEventId()
  {
    return _retrievePartnerStatusAckEventId;
  }

  public void setOnlineNotificationEventId(String eventId)
  {
    _onlineNotificationEventId = eventId;
  }

  public String getOnlineNotificationEventId()
  {
    return _onlineNotificationEventId;
  }

  public void setOnlineNotificationAckEventId(String eventId)
  {
    _onlineNotificationAckEventId = eventId;
  }

  public String getOnlineNotificationAckEventId()
  {
    return _onlineNotificationAckEventId;
  }

  public void setDisconnectEventId(String eventId)
  {
    _disconnectEventId = eventId;
  }

  public String getDisconnectEventId()
  {
    return _disconnectEventId;
  }

  public void setDisconnectAckEventId(String eventId)
  {
    _disconnectAckEventId = eventId;
  }

  public String getDisconnectAckEventId()
  {
    return _disconnectAckEventId;
  }

  public void setOfflineNotificationEventId(String eventId)
  {
    _offlineNotificationEventId = eventId;
  }

  public String getOfflineNotificationEventId()
  {
    return _offlineNotificationEventId;
  }

  public void setOfflineNotificationAckEventId(String eventId)
  {
    _offlineNotificationAckEventId = eventId;
  }

  public String getOfflineNotificationAckEventId()
  {
    return _offlineNotificationAckEventId;
  }

  public void setExpiredNotificationEventId(String eventId)
  {
    _expiredNotificationEventId = eventId;
  }

  public String getExpiredNotificationEventId()
  {
    return _expiredNotificationEventId;
  }

  public void setConfirmAliveEventId(String eventId)
  {
    _confirmAliveEventId = eventId;
  }

  public String getConfirmAliveEventId()
  {
    return _confirmAliveEventId;
  }

  public void setConfirmAliveAckEventId(String eventId)
  {
    _confirmAliveAckEventId = eventId;
  }

  public String getConfirmAliveAckEventId()
  {
    return _confirmAliveAckEventId;
  }

  public void setReconnectRetries(int numTries)
  {
    _reconnectRetries = numTries;
  }

  public int getReconnectRetries()
  {
    return _reconnectRetries;
  }

  public void setReconnectInterval(int seconds)
  {
    _reconnectInterval = seconds;
  }

  public int getReconnectInterval()
  {
    if (_reconnectInterval <= 0)
      _reconnectInterval = 10;
    return _reconnectInterval;
  }

  public void setAutoConnectOnStartup(boolean autoConnect)
  {
    _autoConnectOnStartup = autoConnect;
  }
  
  public boolean getAutoConnectOnStartup()
  {
    return _autoConnectOnStartup;
  }
  
  /**
   * Loads the Connection Setup Properties from the xml properties file.
   *
   * @throws Exception The Connection Configuration file could not be loaded.
   * Cannot find the name of the Connection Setup Properties file to load.
   */
  public static ConnectionProperties load() throws Throwable
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(
                             IConnectionConfig.CONFIG_NAME);
    if (config == null)
      throw new Exception("Connection configuration file not found!");

    File file = FileUtil.getFile(
                  IConnectionConfig.PATH_CONNECTION,
                  config.getString(IConnectionConfig.CONN_PROPERTIES_ENTITY));

    return (ConnectionProperties)new ConnectionProperties().deserialize(
                                          file.getAbsolutePath());
  }

  public static ConnectionProperties getDefaultProperties()
  {
    ConnectionProperties props = new ConnectionProperties();

    props.setConnectTimeout(2); //minutes
    props.setKeepAliveTimeout(3); //minutes
    props.setReconnectInterval(10); //seconds
    props.setReconnectRetries(10);
    props.setAutoConnectOnStartup(false);

    props.setConnectEventId("0");
    props.setConnectAckEventId("1");
    props.setDisconnectEventId("2");
    props.setDisconnectAckEventId("3");
    props.setRetrievePendingEventId("4");
    props.setRetrievePendingAckEventId("5");
    props.setKeepAliveEventId("6");
    props.setKeepAliveAckEventId("7");

    props.setRetrievePartnerStatusEventId("1600");
    props.setRetrievePartnerStatusAckEventId("1601");
    props.setOnlineNotificationEventId("1602");
    props.setOnlineNotificationAckEventId("1603");
    props.setOfflineNotificationEventId("1604");
    props.setOfflineNotificationAckEventId("1605");

    props.setConfirmAliveEventId("8000");
    props.setConfirmAliveAckEventId("8001");

    props.setExpiredNotificationEventId("61200");

    return props;
  }

  public static void main(String[] args) throws Exception
  {
    ConnectionProperties props = getDefaultProperties();
    props.serialize("conn-props.xml");
  }

}