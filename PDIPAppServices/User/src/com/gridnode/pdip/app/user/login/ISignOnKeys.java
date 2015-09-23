/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISignOnKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Add key for Session manager jndi key.
 */
package com.gridnode.pdip.app.user.login;

/**
 * This interface defines the contants used by the SignOn and SignOff servlet.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public interface ISignOnKeys
{
  /**
   * Parameter name for User ID in http request
   */
  static final String SIGNON_USER_ID    = "UserID";

  /**
   * Parameter name for Password in http request
   */
  static final String SIGNON_PASSWORD   = "Password";

  /**
   * Paramter name for Session ID in http request
   */
  static final String SIGNON_SESSION    = "SessionID";

  /**
   * Paramter name for Application in http request
   */
  static final String SIGNON_APPLICATION= "Application";

  /**
   * Name for Error code in Http response header.
   */
  static final String ERR_CODE          = "ErrorCode";

  /**
   * Value for Error code: No Error, request succeeded
   */
  static final int ERR_NO_ERROR         = 0;

  /**
   * Value for Error code: Some authentication error occurred due to business
   * logic validation. Request not fulfill.
   */
  static final int ERR_AUTH_FAIL        = 1;

  /**
   * Value for Error code: Some error occurred due to services used for performing
   * the signon/off processing.
   *
   */
  static final int ERR_SERVICE_FAIL     = 2;

  /**
   * Value for Error code: Invalid http request format, probably due to
   * incompatible parameter in the request.
   */
  static final int ERR_BAD_REQUEST      = 3;

  /**
   * Jndi name used for looking up the UserManagerBean
   */
  static final String USER_MGR_JNDI = "java:comp/env/ejb/UserManager";

  /**
   * Jndi name used for looking up the SessionManagerBean
   */
  static final String SESSION_MGR_JNDI = "java:comp/env/ejb/SessionManager";

}