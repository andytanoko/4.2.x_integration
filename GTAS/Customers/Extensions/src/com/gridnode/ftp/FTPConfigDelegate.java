/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFTPClientManager.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 03 2003    Jagadeesh               Created
 */


package com.gridnode.ftp;

import com.gridnode.ftp.exception.FTPServiceException;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPConfigDelegate
{

  private static Properties _ftpProps = null;
  public FTPConfigDelegate()
  {
  }

  public static Properties getFTPConfig() throws FTPServiceException
  {
    try
    {
      if (_ftpProps == null)
      {
        File file = FileUtil.getFile(IFTPConfig.PATH_CONFIG_FTP,
                                     IFTPConfig.FTP_PROPERTIES_FILE
                                     );
        _ftpProps = new Properties();
        _ftpProps.load(new FileInputStream(file));
      }
      return _ftpProps;
    }
    catch(IOException ex)
    {
      throw new FTPServiceException("Could Not Initilize FTP Properties");
    }
    catch(FileAccessException ex)
    {
      throw new FTPServiceException("Could Not Initilize FTP Properties");
    }

  }

}