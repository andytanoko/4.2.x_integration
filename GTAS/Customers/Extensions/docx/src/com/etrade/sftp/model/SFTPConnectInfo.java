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
 * File: SFTPConnectInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17, 2010   <Developer Name>       Created
 */
package com.etrade.sftp.model;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Eric Loh
 * @version
 * @since
 */
public class SFTPConnectInfo
{
  private String hostName = null; //can be hostName text or ip address
  private String port = null; //port number in String format
  
  private String userName = null;
  private char[] password = null; //optional, if not present private key files must be provided
  
  private ArrayList<File> privateKeyFiles = null; //Change to arrayList to allow adding
  
  private String remoteFolder = null; //can be used for both upload or download
  private String localFolder = null; //TODO: used for download to specify where to deposit downloaded files
  
  private String remoteFileName = null; //used for upload

  public SFTPConnectInfo()
  {
    super();
  }

  /**
   * @return
   */
  public String getHostName()
  {
    return hostName;
  }

  /**
   * @param hostName
   */
  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  /**
   * @return
   */
  public String getPort()
  {
    return port;
  }

  /**
   * @param port
   */
  public void setPort(String port)
  {
    this.port = port;
  }

  /**
   * @return
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * @param userName
   */
  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  /**
   * @return
   */
  public char[] getPassword()
  {
    return password;
  }

  /**
   * @param password
   */
  public void setPassword(char[] password)
  {
    this.password = password;
  }

  /**
   * @return
   */
  public ArrayList<File> getPrivateKeyFiles()
  {
    return privateKeyFiles;
  }

  /**
   * @param privateKeyFiles
   */
  public void setPrivateKeyFiles(ArrayList<File> privateKeyFiles)
  {
    this.privateKeyFiles = privateKeyFiles;
  }

  /**
   * Add a private key file
   * @param privateKeyFiles
   */
  public void addPrivateKeyFile(File privateKeyFile)
  {
    if (this.privateKeyFiles == null)
    {
      this.privateKeyFiles = new ArrayList<File>();
    }
    this.privateKeyFiles.add(privateKeyFile);
  }

  /**
   * @param index
   * @return
   */
  public File getPrivateKeyFile(int index)
  {
    if (this.privateKeyFiles == null)
    {
      return null;
    }
    else
    {
      return this.privateKeyFiles.get(index); //TODO: will throw index out of bounds if specified index does not exists
    }
  }

  /**
   * Clear all private key files
   */
  public void clearPrivateKeyFiles()
  {
    if (this.privateKeyFiles != null)
    {
      this.privateKeyFiles.clear();
    }
    this.privateKeyFiles = null; //set to null
  }

  /**
   * @return
   */
  public String getRemoteFolder()
  {
    return remoteFolder;
  }

  /**
   * @param remoteFolder
   */
  public void setRemoteFolder(String remoteFolder)
  {
    this.remoteFolder = remoteFolder;
  }

  /**
   * @return
   */
  public String getRemoteFileName()
  {
    return remoteFileName;
  }

  /**
   * @param remoteFileName
   */
  public void setRemoteFileName(String remoteFileName)
  {
    this.remoteFileName = remoteFileName;
  }

  /**
   * @return
   */
  public String getLocalFolder()
  {
    return localFolder;
  }

  /**
   * @param localFolder
   */
  public void setLocalFolder(String localFolder)
  {
    this.localFolder = localFolder;
  }
}
