/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFTPConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * APR 30 2003    Jagadeesh               Created
 */

package com.gridnode.ftp;


public interface IFTPConfig
{

  public static final String FTP_CLIENT_PROVIDER = "ftp.client.provider";

  public static final String FTP_FILE_PERFIX = "ftp.file.prefix";

  public static final String IS_RENAME_FILE = "ftp.isrename";

  public static final String FTP_PROPERTIES_FILE = "ftpclient.properties";

  public static final String SRC_FILE_PREFIX = "ftp.src.file.prefix";

  public static final String RETRY_COUNT = "ftp.retrycount";

  public static final String PATH_CONFIG_FTP = "userproc.path.work";

  public static final String IS_MOVE = "ftp.ismove";

  public static final String MOVE_FOLDER = "ftp.move.dir";
  
  public static final String DATA_CONN_TIMEOUT = "ftp.data.conn.timeout";
  
  public static final String CURRENT_CONN_TIMEOUT = "ftp.current.conn.timeout";
  
  public static final String CONNECT_TIMEOUT = "ftp.connection.timeout";

/*
  public static final String FTP_HOST_SERVER  = "ftp.host";

  public static final String FTP_PORT = "ftp.port";

  public static final String FTP_USER  = "ftp.user";

  public static final String FTP_PASSWORD = "ftp.password";
*/

 //public static final String FTP_PROPERTIES_FILE = "ftpclient.properties";

}