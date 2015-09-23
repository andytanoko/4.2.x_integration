/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 25 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.setup;

import com.gridnode.gtas.server.connection.helpers.IConnectionConfig;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.db.DataObject;

import java.io.File;

/**
 * This data object contains the properties required for Connectio Setup.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupProperties extends DataObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4748365262713203846L;
	private String _preSetupEventId;
  private String _preSetupAckEventId;
  private String _setupEventId;
  private String _setupAckEventId;
  private String _postSetupEventId;

  private String _setupSendTopic;
  private String _setupListenTopic;
  private String _setupListenUser;
  private String _setupListenPassword;

  private String _msgFormatVersion;
  private String _commInfoVersion;

  private String _sendChannelRefId;
  private String _sendChannelName;
  private String _sendChannelDesc;
  private String _sendChannelPkgName;
  private String _sendChannelPkgDesc;
  private String _sendChannelSecName;
  private String _sendChannelSecDesc;
  private String _sendChannelCommName;
  private String _sendChannelCommDesc;

  private String _masterChannelName;
  private String _masterChannelDesc;
  private String _masterChannelPkgName;
  private String _masterChannelPkgDesc;
  private String _masterChannelSecName;
  private String _masterChannelSecDesc;
  private String _masterChannelCommName;
  private String _masterChannelCommDesc;
  private Integer _masterChannelCommPort;

  private String _gmChannelName;
  private String _gmChannelDesc;
  private String _gmChannelPkgName;
  private String _gmChannelPkgDesc;
  private String _gmChannelSecName;
  private String _gmChannelSecDesc;
  private String _gmChannelCommName;
  private String _gmChannelCommDesc;
  private Integer _gmChannelCommPort;
  private String _gmChannelCommPassword;
  private String _gmCategory;

  private Integer _servicingRouterPort;

  private boolean _isTest;

  public ConnectionSetupProperties()
  {
  }

  public void setPreSetupEventId(String eventId)
  {
    _preSetupEventId = eventId;
  }

  public String getPreSetupEventId()
  {
    return _preSetupEventId;
  }

  public void setPreSetupAckEventId(String eventId)
  {
    _preSetupAckEventId = eventId;
  }

  public String getPreSetupAckEventId()
  {
    return _preSetupAckEventId;
  }

  public void setSetupEventId(String eventId)
  {
    _setupEventId = eventId;
  }

  public String getSetupEventId()
  {
    return _setupEventId;
  }

  public void setSetupAckEventId(String eventId)
  {
    _setupAckEventId = eventId;
  }

  public String getSetupAckEventId()
  {
    return _setupAckEventId;
  }

  public void setPostSetupEventId(String eventId)
  {
    _postSetupEventId = eventId;
  }

  public String getPostSetupEventId()
  {
    return _postSetupEventId;
  }

  public void setSetupSendTopic(String topic)
  {
    _setupSendTopic = topic;
  }

  public String getSetupSendTopic()
  {
    return _setupSendTopic;
  }

  public void setSetupListenTopic(String topic)
  {
    _setupListenTopic = topic;
  }

  public String getSetupListenTopic()
  {
    return _setupListenTopic;
  }

  public void setMsgFormatVersion(String version)
  {
    _msgFormatVersion = version;
  }

  public String getMsgFormatVersion()
  {
    return _msgFormatVersion;
  }

  public void setServicingRouterPort(Integer port)
  {
    _servicingRouterPort = port;
  }

  public Integer getServicingRouterPort()
  {
    return _servicingRouterPort;
  }

  public void setCommInfoVersion(String version)
  {
    _commInfoVersion = version;
  }

  public String getCommInfoVersion()
  {
    return _commInfoVersion;
  }

  public void setSendChannelRefId(String refId)
  {
    _sendChannelRefId = refId;
  }

  public String getSendChannelRefId()
  {
    return _sendChannelRefId;
  }

  public void setSendChannelPkgName(String name)
  {
    _sendChannelPkgName = name;
  }

  public String getSendChannelPkgName()
  {
    return _sendChannelPkgName;
  }

  public void setSendChannelPkgDesc(String desc)
  {
    _sendChannelPkgDesc = desc;
  }

  public String getSendChannelPkgDesc()
  {
    return _sendChannelPkgDesc;
  }

  public void setSendChannelSecName(String name)
  {
    _sendChannelSecName = name;
  }

  public String getSendChannelSecName()
  {
    return _sendChannelSecName;
  }

  public void setSendChannelSecDesc(String desc)
  {
    _sendChannelSecDesc = desc;
  }

  public String getSendChannelSecDesc()
  {
    return _sendChannelSecDesc;
  }

  public void setSendChannelCommName(String name)
  {
    _sendChannelCommName = name;
  }

  public String getSendChannelCommName()
  {
    return _sendChannelCommName;
  }

  public void setSendChannelCommDesc(String desc)
  {
    _sendChannelCommDesc = desc;
  }

  public String getSendChannelCommDesc()
  {
    return _sendChannelCommDesc;
  }

  public void setSendChannelName(String name)
  {
    _sendChannelName = name;
  }

  public String getSendChannelName()
  {
    return _sendChannelName;
  }

  public void setSendChannelDesc(String desc)
  {
    _sendChannelDesc = desc;
  }

  public String getSendChannelDesc()
  {
    return _sendChannelDesc;
  }

  public void setSetupListenUser(String user)
  {
    _setupListenUser = user;
  }

  public String getSetupListenUser()
  {
    return _setupListenUser;
  }

  public void setSetupListenPassword(String password)
  {
    _setupListenPassword = password;
  }

  public String getSetupListenPassword()
  {
    return _setupListenPassword;
  }

  public void setMasterChannelPkgName(String name)
  {
    _masterChannelPkgName = name;
  }

  public String getMasterChannelPkgName()
  {
    return _masterChannelPkgName;
  }

  public void setMasterChannelPkgDesc(String desc)
  {
    _masterChannelPkgDesc = desc;
  }

  public String getMasterChannelPkgDesc()
  {
    return _masterChannelPkgDesc;
  }

  public void setMasterChannelSecName(String name)
  {
    _masterChannelSecName = name;
  }

  public String getMasterChannelSecName()
  {
    return _masterChannelSecName;
  }

  public void setMasterChannelSecDesc(String desc)
  {
    _masterChannelSecDesc = desc;
  }

  public String getMasterChannelSecDesc()
  {
    return _masterChannelSecDesc;
  }

  public void setMasterChannelCommName(String name)
  {
    _masterChannelCommName = name;
  }

  public String getMasterChannelCommName()
  {
    return _masterChannelCommName;
  }

  public void setMasterChannelCommDesc(String desc)
  {
    _masterChannelCommDesc = desc;
  }

  public String getMasterChannelCommDesc()
  {
    return _masterChannelCommDesc;
  }

  public void setMasterChannelCommPort(Integer port)
  {
    _masterChannelCommPort = port;
  }

  public Integer getMasterChannelCommPort()
  {
    return _masterChannelCommPort;
  }

  public void setMasterChannelName(String name)
  {
    _masterChannelName = name;
  }

  public String getMasterChannelName()
  {
    return _masterChannelName;
  }

  public void setMasterChannelDesc(String desc)
  {
    _masterChannelDesc = desc;
  }

  public String getMasterChannelDesc()
  {
    return _masterChannelDesc;
  }

  public void setGmChannelPkgName(String name)
  {
    _gmChannelPkgName = name;
  }

  public String getGmChannelPkgName()
  {
    return _gmChannelPkgName;
  }

  public void setGmChannelPkgDesc(String desc)
  {
    _gmChannelPkgDesc = desc;
  }

  public String getGmChannelPkgDesc()
  {
    return _gmChannelPkgDesc;
  }

  public void setGmChannelSecName(String name)
  {
    _gmChannelSecName = name;
  }

  public String getGmChannelSecName()
  {
    return _gmChannelSecName;
  }

  public void setGmChannelSecDesc(String desc)
  {
    _gmChannelSecDesc = desc;
  }

  public String getGmChannelSecDesc()
  {
    return _gmChannelSecDesc;
  }

  public void setGmChannelCommName(String name)
  {
    _gmChannelCommName = name;
  }

  public String getGmChannelCommName()
  {
    return _gmChannelCommName;
  }

  public void setGmChannelCommDesc(String desc)
  {
    _gmChannelCommDesc = desc;
  }

  public String getGmChannelCommDesc()
  {
    return _gmChannelCommDesc;
  }

  public void setGmChannelCommPort(Integer port)
  {
    _gmChannelCommPort = port;
  }

  public Integer getGmChannelCommPort()
  {
    return _gmChannelCommPort;
  }

  public void setGmChannelCommPassword(String password)
  {
    _gmChannelCommPassword = password;
  }

  public String getGmChannelCommPassword()
  {
    return _gmChannelCommPassword;
  }

  public void setGmChannelName(String name)
  {
    _gmChannelName = name;
  }

  public String getGmChannelName()
  {
    return _gmChannelName;
  }

  public void setGmChannelDesc(String desc)
  {
    _gmChannelDesc = desc;
  }

  public String getGmChannelDesc()
  {
    return _gmChannelDesc;
  }

  public void setGmCategory(String category)
  {
    _gmCategory = category;
  }

  public String getGmCategory()
  {
    return _gmCategory;
  }

  public void setIsTest(boolean isTest)
  {
    _isTest = isTest;
  }

  public boolean getIsTest()
  {
    return _isTest;
  }

  /**
   * Loads the Connection Setup Properties from the xml properties file.
   *
   * @throws Exception The Connection Configuration file could not be loaded.
   * Cannot find the name of the Connection Setup Properties file to load.
   */
  public static ConnectionSetupProperties load() throws Throwable
  {
    Configuration config = ConfigurationManager.getInstance().getConfig(
                             IConnectionConfig.CONFIG_NAME);
    if (config == null)
      throw new Exception("Connection configuration file not found!");

    File file = FileUtil.getFile(
                  IConnectionConfig.PATH_CONNECTION,
                  config.getString(IConnectionConfig.SETUP_PROPERTIES_ENTITY));

    return (ConnectionSetupProperties)new ConnectionSetupProperties().deserialize(
                                          file.getAbsolutePath());
  }

  /**
   * Get the default set of Connection Setup Properties. This should be called
   * only if load() fails to return the ConnectionSetupProperties from the
   * properties file.
   *
   * @return the Connection Setup Properties with default values.
   */
  public static ConnectionSetupProperties getDefaultProperties()
  {
    ConnectionSetupProperties config = new ConnectionSetupProperties();

    config.setPreSetupEventId("60");
    config.setPreSetupAckEventId("61");
    config.setSetupEventId("62");
    config.setSetupAckEventId("63");
    config.setPostSetupEventId("64");

    config.setSetupSendTopic("gmprime");
    config.setSetupListenTopic("gtpublic");

    config.setMsgFormatVersion("1");
    config.setServicingRouterPort(new Integer(443));
    config.setCommInfoVersion("020000");

    config.setSendChannelRefId("900"); //node id for gmprime
    config.setSendChannelPkgName("CON.SETUP.PKG");
    config.setSendChannelPkgDesc("Packaging Profile for Connection Setup");
    config.setSendChannelSecName("CON.SETUP.SEC");
    config.setSendChannelSecDesc("Security Profile for Connection Setup");
    config.setSendChannelCommName("CON.SETUP.COMM");
    config.setSendChannelCommDesc("Communication Profile for Connection Setup");
    config.setSendChannelName("CON.SETUP.CHANNEL");
    config.setSendChannelDesc("Communication Channel for Connection Setup");

    config.setSetupListenUser("gtpublic");
    config.setSetupListenPassword("gridtalk");

    config.setMasterChannelPkgName("MPKG");
    config.setMasterChannelPkgDesc("GridTalk Master Packaging Profile");
    config.setMasterChannelSecName("MSEC");
    config.setMasterChannelSecDesc("GridTalk Master Security Profile");
    config.setMasterChannelCommName("MCOMM");
    config.setMasterChannelCommDesc("GridTalk Master Communication Profile");
    config.setMasterChannelCommPort(new Integer(443));
    config.setMasterChannelName("MC");
    config.setMasterChannelDesc("GridTalk Master Communication Channel");

    config.setGmChannelPkgName("GM.PKG");
    config.setGmChannelPkgDesc("GridMaster Packaging Profile");
    config.setGmChannelSecName("GM.SEC");
    config.setGmChannelSecDesc("GridMaster Security Profile");
    config.setGmChannelCommName("GM.COMM");
    config.setGmChannelCommDesc("GridMaster Communication Profile");
    config.setGmChannelCommPassword("gridnode");
    config.setGmChannelCommPort(new Integer(443));
    config.setGmChannelName("GMC");
    config.setGmChannelDesc("GridMaster Communication Channel");
    config.setGmCategory("GNM");

    return config;
  }

  public static void main(String[] args) throws Exception
  {
    ConnectionSetupProperties props = getDefaultProperties();
    props.serialize("conn-setup-props.xml");
  }
}