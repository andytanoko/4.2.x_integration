package com.gridnode.ftp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */
/**
 * This interface defines constants used, in xml config file.
 * @version 1.0
 */

public interface ISiteConfig
{
  public static final String REMOTE_UPLOAD_FOLDER="remote_upload_folder";

  public static final String REMOTE_DOWNLOAD_FOLDER="remote_download_folder";

  public static final String LOCAL_DOWNLOAD_FOLDER = "local_download_folder";

  public static final String REMOTE_MOVE_FOLDER = "remote_move_folder";

  public static final String FTP_HOST = "host";

  public static final String FTP_PORT = "port";

  public static final String USERNAME = "username";

  public static final String PASSWORD = "password";

  public static final String PROXY_USERNAME = "proxy_username";

  public static final String PROXY_PASSWORD = "proxy_password";

  public static final String IS_PROXY = "isproxy";

  public static final String IS_PULL = "ispull";

  public static final String PROVIDER = "provider";

  public static final String SITE = "site";

  public static final String FOLDER = "folder";

  public static final String NAME = "name";

  public static final String COMMUNICATION = "communication";

  public static final String CONFIG_NAME = "ftpservice.xml";

  public static final String CONFIG_LOC = "data/userproc/ftpclient/";

  public static final String RETRY_COUNT = "retry_count";

  public static final String DEFAULT_SITE = "default";

  public static final String IS_SOCKS = "issocks";

  public static final String FTP_PROXY_HOST = "proxy_host";

  public static final String FTP_PROXY_PORT = "proxy_port";

  public static final String IS_MOVE = "ismove";

  public static final String LOCAL_UPLOAD_FOLDER = "local_upload_folder";

  public static final String LOCAL_UPLOAD_MOVE_FOLDER = "local_upload_move_folder";

  public static final String IS_RENAME = "isrename";

  public static final String UPLOAD_RENAME_PREFIX = "upload_rename_prefix";

  public static final String DATABASE = "database";

  public static final String DATABASE_SEQUENCE_NAME = "dbsequence_name";

  public static final String DATABASE_INSERT_QUERY = "dbinsert_query";

  public static final String DATABASE_SELECT_QUERY =  "dbselect_query";

  public static final String DATABASE_UPDATE_QUERY = "dbupdate_query";

  public static final String DATABASE_PAD_LENGTH = "pad_length";

}