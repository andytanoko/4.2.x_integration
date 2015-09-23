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
 * File: FTPAttachmentInfo.java
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
public class FTPAttachmentInfo
{
  private String _host;
  private int _port;
  private String _username;
  private String _password;
  private String _folder;
  private String _fileExtension;
  private boolean _isEnforceAttachment;
  private boolean _isDeleteAttachment;
  private boolean _isMultipleAttachment;
  
  public FTPAttachmentInfo(String host, int port, String username, String password, String folder, String fileExtension, 
      boolean isEnforceAttachment, boolean isDeleteAttachment, boolean isMultipleAttachment)
  {
    setHost(host);
    setPort(port);
    setUsername(username);
    setPassword(password);
    setFolder(folder);
    setFileExtension(fileExtension);
    setIsEnforceAttachment(isEnforceAttachment);
    setIsDeleteAttachment(isDeleteAttachment);
    setIsMultipleAttachment(isMultipleAttachment);      
  }
  
  public void setHost(String host)
  {
    _host = host;
  }
  
  public String getHost()
  {
    return _host;
  }
  
  public void setPort(int port)
  {
    _port  = port;
  }
  
  public int getPort()
  {
    return _port;
  }
  
  public void setUsername(String username)
  {
    _username = username;
  }
  
  public String getUsername()
  {
    return _username;
  }
  
  public void setPassword(String password)
  {
    _password = password;
  }
  
  public String getPassword()
  {
    return _password;
  }
  
  public void setFolder(String folder)
  {
    _folder = folder;
  }
  
  public String getFolder()
  {
    return _folder;
  }
  
  public void setFileExtension(String fileExtension)
  {
    _fileExtension = fileExtension;
  }
  
  public String getFileExtension()
  {
    return _fileExtension;
  }
  
  public void setIsEnforceAttachment(boolean isEnforceAttachment)
  {
    _isEnforceAttachment = isEnforceAttachment;
  }
  
  public boolean getIsEnforceAttachment()
  {
    return _isEnforceAttachment;
  }
  
  public void setIsDeleteAttachment(boolean isDeleteAttachment)
  {
    _isDeleteAttachment = isDeleteAttachment;
  }
  
  public boolean getIsDeleteAttachment()
  {
    return _isDeleteAttachment;
  }
  
  public void setIsMultipleAttachment(boolean isMultipleAttachment)
  {
    _isMultipleAttachment = isMultipleAttachment;
  }
  
  public boolean getIsMultipleAttachment()
  {
    return _isMultipleAttachment;
  }
  
  public String toString()
  {
    return "FTPAttachmentInfo: host="+getHost()+" port="+getPort()+" username="+getUsername()+" password="+getPassword()+" folder="+getFolder()+" fileExtension="+getFileExtension()+" isEnforceAttachment="+getIsEnforceAttachment()+" isDeleteAttachment="+getIsDeleteAttachment()+" isMultipleAttachment="+getIsMultipleAttachment();
  }
}