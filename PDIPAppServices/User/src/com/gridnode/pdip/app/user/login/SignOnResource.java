/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SignOnResource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Add getSubject() method.
 */
package com.gridnode.pdip.app.user.login;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * A resource allocated per session whenever a signon request is received.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class SignOnResource
{
  private String                _sessionID;
  private String                _application;
  private LoginContext          _loginContext;
  private LoginCallbackHandler  _callbackHandler;
  private boolean               _validLogin;

  /**
   * Construct a SignOnResource object for a session.
   *
   * @param sessionID The SessionID of the session.
   * @param application The application that the session belongs to.
   *
   * @since 2.0
   */
  public SignOnResource(String sessionID, String application)
  {
    _sessionID = sessionID;
    _application = application;
  }

  /**
   * Get the Application of the session.
   */
  public String getApplication()
  {
    return _application;
  }

  /**
   * Get the SessionID of the session
   */
  public String getSessionID()
  {
    return _sessionID;
  }

  /**
   * Get the UserID of the user who signs on for that session.
   *
   * @return UserID of signed on user, or <B>null</B> if none.
   */
  public String getSignOnUser()
  {
    String signOnUser = null;

    if (_validLogin)
    {
      signOnUser = _callbackHandler.getLoginUser();
    }

    return signOnUser;
  }

  /**
   * Checks if there is any valid signon for that session.
   *
   * @return <B>true</B> if an user has signed on to that session, <B>false</B>
   * if none.
   */
  public boolean isValidSignOn()
  {
    return _validLogin;
  }

  /**
   * Get the Subject associated with this SignOnResource.
   *
   * @return Subject obtained from LoginContext associated with this SignOnResource,
   * or <B>null</B> if no LoginContext associated.
   */
  public Subject getSubject()
  {
    if (_loginContext != null)
      return _loginContext.getSubject();
    return null;
  }

  /**
   * Sign on a user to that session.
   *
   * @param userID User ID of the user.
   * @param password Password provided by the user, encrypted by PasswordMask.
   *
   * @exception LoginException There is another user already signed on to the
   * session, or authentication fails.
   */
  public void login(String userID, String password)
    throws LoginException
  {
    if (_validLogin)
    {
      throw new LoginException("Another user has already signed on for the Session");
    }
    else
    {
      if (_callbackHandler == null)
      {
        _callbackHandler = new LoginCallbackHandler();
        _loginContext = new LoginContext(_application, _callbackHandler);
      }
      _callbackHandler.setLoginInfo(userID, password);

      _loginContext.login();
      _validLogin = true;
    }
  }

  /**
   * Sign off the user, if any, from the session.
   *
   * @exception LoginException Error during logout processing.
   */
  public void logout()
    throws LoginException
  {
    if (_validLogin)
    {
      _loginContext.logout();
      _validLogin = false;

      _loginContext = null;
      _callbackHandler = null;
    }
  }
}