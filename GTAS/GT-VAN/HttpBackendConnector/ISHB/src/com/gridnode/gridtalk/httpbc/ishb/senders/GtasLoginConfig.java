/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendSendConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 12, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.util.Properties;

import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;

/**
 * @author i00107
 * The configuration containing the properties required for backend send.
 */
public class GtasLoginConfig extends Properties
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 472673464625506628L;
  
  /**
   * The prefix for a customer login user property key.
   */
  public static final String LOGIN_USER = "login.user";
  
  /**
   * The prefix for a customer login user property key.
   */
  public static final String LOGIN_PWD = "login.pwd";
  
  
  /**
   * The property key for the root directory to used by the client when storing files
   */
  public static final String CLIENT_ROOT_DIR = "client.root.dir";
  
  /**
   * @param defaults
   */
  public GtasLoginConfig(Properties defaults)
  {
    super(defaults);
  }

  /**
   * Get the command template for the specified target. 
   * @param targetId The identifier for the target. This can be based on the partner that the document
   * will be sent to.
   * @return The command template obtained for the target. <b>null</b> if no such template is found.
   */
  public String getLoginUser()
  {
    return getProperty(LOGIN_USER, null);
  }
  
  public String getLoginPwd()
  {
    return getProperty(LOGIN_PWD, null);
  }
  
  /**
   * Get the working directory for the backend sender program.
   * @return The working directory obtained. If not configured, will use the system temp directory.
   */
  public String getGtasUserRootDir()
  {
    return getProperty(CLIENT_ROOT_DIR, null) + "/" + getLoginUser() + "/in";
  }

  public String getClientRootPathKey()
  {
    return IDocumentPathConfig.PATH_TEMP;
  }
  
  public String getGtasSubPath()
  {
    return getLoginUser() + "/in";
  }

}
