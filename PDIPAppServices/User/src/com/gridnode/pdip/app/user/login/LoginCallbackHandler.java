/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginCallbackHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.user.login;

import javax.security.auth.callback.*;
 
/**
 * Default CallbackHandler for the login procedure.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
class LoginCallbackHandler
  implements CallbackHandler
{
  /**@todo to define one interface for a login callbackhandler and this
   * remains as a default implementation. This is to allow customization later
   * on if more login modules are used and different kinds of callbacks required.
   */

  private String _userID;
  private String _password;

  public LoginCallbackHandler()
  {
  }

  public String getLoginUser()
  {
    return _userID;
  }

  /**
   * Set the Login information already garnered from user.
   *
   * @param userId The UserId.
   * @param password The Password.
   */
  void setLoginInfo(String userId, String password)
  {
    _userID = userId;
    _password = password;
  }

  /**
   * This callback handler only handles 2 types of callbacks:<P>
   * NameCallback for UserID, and PasswordCallback for Password. Other
   * types of callbacks are treated as unsupported.
   */
  public void handle(Callback[] callbacks)
    throws java.io.IOException,
           UnsupportedCallbackException
  {
    for (int i=0; i<callbacks.length; i++)
    {
      if (callbacks[i] instanceof NameCallback)
      {
        NameCallback nameCallback = (NameCallback)callbacks[i];
        nameCallback.setName(_userID);
      }
      else if (callbacks[i] instanceof PasswordCallback)
      {
        PasswordCallback passwordCallback = (PasswordCallback)callbacks[i];
        passwordCallback.setPassword(_password.toCharArray());
      }
      else
      {
       	throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
 	    }
    }
  }

}