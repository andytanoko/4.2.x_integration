/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFrameworkConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * JUL 30 2002    Jagadeesh               Created
 * Sep 04 2002    Neo Sok Lay             Additional keys for Database Config.
 * Dec 26 2002    Neo Sok Lay             Additional keys for Time Config.
 * Oct 31 2005    Neo Sok Lay             Add FRAMEWORK_LOCALJNDI_CONFIG.
 * Dec 22 2005    Neo Sok Lay             Add property key for CMP_ON.
 * Sep 07 2006    Tam Wei Xiang           Add ARCHIVEDB_DAOS, ARCHIVEDB_DATASOURCE
 * Jan 12 2007    Neo Sok Lay             Add FRAMEWORK_PROXY_CONFIG
 */
package com.gridnode.pdip.framework.config;

public interface IFrameworkConfig
{

  public static final String FRAMEWORK_DATABASE_CONFIG = "database";

  public static final String FRAMEWORK_ENTITY_EJB_MAP_CONFIG = "entity.ejb.map";

  public static final String FRAMEWORK_WEBDAV_CONFIG = "webdav";

  public static final String FRAMEWORK_JNDI_CONFIG = "jndi";

  public static final String FRAMEWORK_LOCALJNDI_CONFIG = "jndi.local";
  
  public static final String FRAMEWORK_JMS_CONFIG = "jms";

  public static final String FRAMEWORK_LOG_CONFIG = "log";

  public static final String FRAMEWORK_TIME_CONFIG  = "time";

  public static final String WEBDAV_SERVER =  "webdav.server.";

  public static final String FRAMEWORK_PROXY_CONFIG = "proxy";
  
  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for UserDB datasource.
   */
  public static final String USERDB_DATASOURCE="userdb.datasource";

  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for AppDB datasource.
   */
  public static final String APPDB_DATASOURCE  = "appdb.datasource";
  
  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for ArchiveDB datasource.
   */
  public static final String ARCHIVEDB_DATASOURCE = "archivedb.datasource";
  
  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for AppDB DAOs.
   */
  public static final String APPDB_DAOS        = "appdb.daos";
  
  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for ArchiveDB DAOs.
   */
  public static final String ARCHIVEDB_DAOS    = "archivedb.daos";
  
  /**
   * Property key in FRAMEWORK_DATABASE_CONFIG for turning on CMP.
   */
  public static final String CMP_ON = "cmp.on";
}