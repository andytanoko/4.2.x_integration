/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPClientManagerFactory.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 * Nov 22 2010    Tam Wei Xiang           #1977 - Add instance variable isBinaryTransfer
 */

package com.gridnode.ftp;

import com.gridnode.ftp.exception.FTPServiceException;

/**
 * This class Encapsulate ftp specific Parameters in a Object, which
 * is used by FTPClient specific classes. A Value Object representation
 * for getter/setter methods.
 */

public class FTPParam
{

  private String _host  = null;
  private String _port = null;
  private String _userName = null;
  private String _password= null;
  private String _fileName = null;
  private String _provider = null;
  private String _filenamePrefix = null;
  private boolean _isFileRename = false;
  private boolean _isMoveFile = false;
  private boolean _isBinaryTransfer = false;
  
  private String _srcFileRenamePrefix = null;
  private int _retryCount = 0;
  private String _actualFileName = null;
  private String _destDirectory = null;
  private String _moveDirectory = null;


  private int _timeout = 0;
  private int _dataTimeout = 0;
  private int _soTimeout = 0;
  
  public FTPParam(String args[]) throws FTPServiceException
  {
    parseCmdLineArgs(args);
  }

  public FTPParam(String host,Integer port,
      String userName, String password,
      String fileName,String actualFileName,
      String destDir
      )
  {
    setHost(host);
    setPort(String.valueOf(port.intValue()));
    setUserName(userName);
    setPassword(password);
    setFileName(fileName);
    setActualFileName(actualFileName);
    setDestinationDir(destDir);
  }

  public void setHost(String host)
  {
    _host = host;
  }

  public String getHost()
  {
    return _host;
  }

  public void setPort(String port)
  {
    _port = port;
  }

  public String getPort()
  {
    return _port;
  }

  public void setUserName(String username)
  {
    _userName = username;
  }

  public String getUserName()
  {
    return _userName;
  }

  public void setPassword(String password)
  {
    _password = password;
  }

  public String getPassword()
  {
    return _password;
  }

  public void setFileName(String filename)
  {
    _fileName = filename;
  }

  public String getFileName()
  {
    return _fileName;
  }

  public void setProvider(String provider)
  {
    _provider = provider;
  }

  public String getProvider()
  {
    return _provider;
  }

  public void setFileNamePrefix(String prefix)
  {
    _filenamePrefix = prefix;
  }

  public String getFileNamePrefix()
  {
    return _filenamePrefix;
  }

  public void setFileRename(boolean rename)
  {
    _isFileRename = rename;
  }

  public boolean isFileRename()
  {
    return _isFileRename;
  }

  public void setSrcFileRenamePrefix(String srcFileNamePrfix)
  {
    _srcFileRenamePrefix = srcFileNamePrfix;
  }

  public String getSrcFileRenamePrefix()
  {
    return _srcFileRenamePrefix;
  }

  public void setRetryCount(int count)
  {
    _retryCount = count;
  }

  public int getRetryCount()
  {
    return _retryCount;
  }

  public void setActualFileName(String afileName)
  {
    _actualFileName = afileName;
  }

  public String getActualFileName()
  {
    return _actualFileName;
  }

  public void setDestinationDir(String destDir)
  {
    _destDirectory = destDir;
  }

  public String getDestinationDir()
  {
    return _destDirectory;
  }

  public void setIsMoveFile(boolean isMove)
  {
    _isMoveFile = isMove;
  }

  public boolean isMoveFile()
  {
    return _isMoveFile;
  }

  public void setMoveDirectory(String dirName)
  {
    _moveDirectory = dirName;
  }

  public String getMoveDirectory()
  {
    return _moveDirectory;
  }

  public int getDataConnTimeout()
  {
    return _dataTimeout;
  }

  public int getSoTimeout()
  {
    return _soTimeout;
  }

  /**
   * Sets the timeout in milliseconds to use when reading from the data connection
   * @param timeout Timeout in milliseconds, 0 means infinity
   */
  public void setDataConnTimeout(int timeout)
  {
    _dataTimeout = timeout;
  }

  
  /**
   * Set the timeout in milliseconds of a currently opened connection
   * @param timeout in milli sec
   */
  public void setSoTimeout(int timeout)
  {
    _soTimeout = timeout;
  }
  
  /**
   * Set timeout when creating a socket.
   * This include trying to connect to the ftp server at the beginnig and
   * trying to connect to the server in order to establish a data connection.
   * @param timeout Timeout in milliseconds, 0 means infinity.
   */
   public void setSocketTimeout(int timeout)
   {
     _timeout = timeout;
   }


  /**
   * Get timeout when creating socket.
   * @return Timeout for creating socket in milliseconds, 0 means infinity.
   */
   public int getSocketTimeout()
   {
     return _timeout;
   }
  
  public boolean isBinaryTransfer()
  {
    return _isBinaryTransfer;
  }

  public void setBinaryTransfer(boolean binaryTransfer)
  {
    _isBinaryTransfer = binaryTransfer;
  }

  private void parseCmdLineArgs(String args[]) throws FTPServiceException
  {
    for(int i=0;i<args.length;i++)
    {
      String cmdArg = args[i];
      if(IFTPConstants.HOST.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setHost(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a host name " +
          "using the "+IFTPConstants.HOST+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }
      }
      else if(IFTPConstants.PORT.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setPort(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a port " +
          "using the "+IFTPConstants.PORT+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }
      }
      else if(IFTPConstants.USER_NAME.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setUserName(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a port when " +
          "using the "+IFTPConstants.USER_NAME+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }

      }
      else if(IFTPConstants.PASSWORD.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setPassword(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a password when " +
          "using the "+IFTPConstants.PASSWORD+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }

      }
      else if(IFTPConstants.FILENAME.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setFileName(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a filename when " +
          "using the "+IFTPConstants.FILENAME+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }
      }
      else if(IFTPConstants.PROVIDER.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setProvider(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a provider when " +
          "using the "+IFTPConstants.PROVIDER+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }
      }
      else if(IFTPConstants.FILENAME_PREFIX.equals(cmdArg))
      {
        try
        {
          String argsValue =  args[i+1];
          setFileNamePrefix(argsValue);
          i++;
        }catch(ArrayIndexOutOfBoundsException aex) {
          String message = "You must specify a provider when " +
          "using the "+IFTPConstants.FILENAME_PREFIX+" argument.";
          System.out.println(message);
          throw new FTPServiceException(message);
        }
      }
    }

  }

}