/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: FTPServerInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.ftp.model;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class FTPServerInfo
{
  private String _hostname;
  private int _port;
  private String _username;
  private String _password;
  private boolean _pushDirectToGT;
  private String _pushGTUsername;
  private String _destFolder;
  private String _filenameFormat;
  private String _appendFilename;
  private String _embeddedContentTag;
  private String _embeddedContentTagFormat;
  private boolean _isUsePassiveMode = false; //indicate whether we should explicitly use FTP Passive mode

  
  public FTPServerInfo(String hostname, int port, String username, String password, String destFolder, boolean pushDirectToGT, String gtUsername, String gtPassword)
  {
    setHostname(hostname);
    setPort(port);
    setPassword(password);
    setUsername(username);
    setPushDirectToGT(pushDirectToGT);
    setPushGTUsername(gtUsername);
    setDestFolder(destFolder);
  }

  public String toString()
  {
    return "hostname:" + getHostname() + " port:" + getPort() + " username:" + getUsername() + " password:" + getPassword() + 
    " destFolder:" + getDestFolder() + " pushDirectToGT:" + getPushDirectToGT() + " pushGTUsername:" + getPushGTUsername() + 
    " usePassiveMode:"+isUsePassiveMode();
  }
  
  public String getPushGTUsername()
  {
    return _pushGTUsername;
  }
  
  public void setPushGTUsername(String gtUsername)
  {
    _pushGTUsername = gtUsername;
  }
  
  public boolean getPushDirectToGT()
  {
    return _pushDirectToGT;
  }

  public void setPushDirectToGT(boolean pushDirectToGT)
  {
    _pushDirectToGT = pushDirectToGT;
  }
  
  public String getAppendFilename()
  {
    return _appendFilename;
  }

  public void setAppendFilename(String filename)
  {
    _appendFilename = filename;
  }

  public String getFilenameFormat()
  {
    return _filenameFormat;
  }

  public void setFilenameFormat(String format)
  {
    _filenameFormat = format;
  }

  public String getHostname()
  {
    return _hostname;
  }

  public void setHostname(String _hostname)
  {
    this._hostname = _hostname;
  }

  public String getPassword()
  {
    return _password;
  }

  public void setPassword(String _password)
  {
    this._password = _password;
  }

  public int getPort()
  {
    return _port;
  }

  public void setPort(int _port)
  {
    this._port = _port;
  }

  public String getUsername()
  {
    return _username;
  }

  public void setUsername(String _username)
  {
    this._username = _username;
  }

  public String getDestFolder()
  {
    return _destFolder;
  }

  public void setDestFolder(String folder)
  {
    _destFolder = folder;
  }

  public String getEmbeddedContentTag()
  {
    return _embeddedContentTag;
  }

  public void setEmbeddedContentTag(String contentTag)
  {
    _embeddedContentTag = contentTag;
  }

  public String getEmbeddedContentTagFormat()
  {
    return _embeddedContentTagFormat;
  }

  public void setEmbeddedContentTagFormat(String contentTagFormat)
  {
    _embeddedContentTagFormat = contentTagFormat;
  }
  
  public boolean isUsePassiveMode()
  {
    return _isUsePassiveMode;
  }

  public void setUsePassiveMode(boolean usePassiveMode)
  {
    _isUsePassiveMode = usePassiveMode;
  }
}
