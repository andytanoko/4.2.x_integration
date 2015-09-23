package com.gridnode.ftp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: GridNode Pte Ltd</p>
 * @author not attributable
 * @version 1.0
 */

public interface IDBConfig
{
  public static final String CONFIG_NAME = "dbconfig.properties";

  public static final String CONFIG_LOC = "data/userproc/ftpclient/";

  public static final String JDBC_DRIVERS = "jdbc.drivers";

  public static final String SAX_DRIVER = "org.xml.sax.driver";

  public static final String JDBC_DRIVER_CLASS = "jdbc.driver.class";

  public static final String POOLING_JDBC_DRIVER = "jdbc.pooling.driver";

  public static final String DB_SEQUENCE_NAME = "db.sequence.name";

  public static final String DB_SELECT_QUERY = "db.select.query";

  public static final String DB_UPDATE_QUERY = "db.update.query";

  public static final String DB_INSERT_QUERY = "db.insert.query";

  public static final String PAD_LENGTH = "pad.length";

  public static final String DB_URL = "db.url";

}