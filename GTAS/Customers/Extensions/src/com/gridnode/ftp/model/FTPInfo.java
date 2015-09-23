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
 * File: FTPInfo.java
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
public class FTPInfo
{
  private FTPServerInfo serverInfo;
  private FTPFolderInfo folderInfo;
  
  public FTPInfo(FTPServerInfo serverInfo, FTPFolderInfo folderInfo)
  {
    setServerInfo(serverInfo);
    setFolderInfo(folderInfo);
  }

  public FTPFolderInfo getFolderInfo()
  {
    return folderInfo;
  }

  public void setFolderInfo(FTPFolderInfo folderInfo)
  {
    this.folderInfo = folderInfo;
  }

  public FTPServerInfo getServerInfo()
  {
    return serverInfo;
  }

  public void setServerInfo(FTPServerInfo serverInfo)
  {
    this.serverInfo = serverInfo;
  }
  
  
}
