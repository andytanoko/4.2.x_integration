/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PasswordCredential.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 12 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.user.login;

import java.io.Serializable;

/**
 * This is a Private credential object for Password.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class PasswordCredential
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5645518088509498637L;
	private char[] _password;

  /**
   * Creates a PasswordCredential.
   *
   * @param password The password.
   */
  public PasswordCredential(char[] password)
  {
    _password = new char[password.length];
    System.arraycopy(password, 0, _password, 0, password.length);
  }

  /**
   * Get the password wrapped by this PasswordCredential object.
   */
  public char[] getPassword()
  {
    char[] tmpPassword = new char[_password.length];
    System.arraycopy(_password, 0, tmpPassword, 0, _password.length);

    return tmpPassword;
  }

  /**
   * Clear the password wrapped in this PasswordCredential object.
   */
  void clearPassword()
  {
    if (_password != null)
    {
      for (int i = 0; i < _password.length; i++)
        _password[i] = ' ';
      _password = null;
    }
  }

  public String toString()
  {
    return "PasswordCredential: "+ new String(_password);
  }
}