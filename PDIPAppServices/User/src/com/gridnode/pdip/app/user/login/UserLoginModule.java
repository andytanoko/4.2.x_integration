/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserLoginModule.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2002    Neo Sok Lay         Created
 * May 31 2002    Neo Sok Lay         Assign RolePrincipals to authenticated
 *                                    user.
 * Jun 14 2002    Neo Sok Lay         Password checking in User Account.
 */
package com.gridnode.pdip.app.user.login;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.helpers.Logger;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.base.acl.auth.RolePrincipal;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This <code>LoginModule</code> is used for user login authentication.
 * <p>
 * If the user successfully authenticates itself,
 * a <code>UserPrincipal</code> with the user's userID is added to the Subject.
 * RolePrincipals representing the Roles that the user has been assigned are also
 * added to the Subject.
 * <p>
 * Options recognized by this LoginModule are:<P>
 * <LI><CODE>maxWrongCred</CODE> - Maximum number of times that the user can
 * provide incorrect credential before the account is freezed. If not set, this
 * is default to 3.</LI>
 * <LI><CODE>freezeTimeout</CODE> - Time in minutes for the account freezing
 * time. If not set, this is default to 10.</LI>
 * This LoginModule also recognizes the debug option.
 * If set to true in the login Configuration, debug messages will be output to
 * the Logger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLoginModule
  implements LoginModule
{
  private static final String OPT_DEBUG          = "debug";
  private static final String OPT_FREEZE_TIMEOUT = "freezeTimeout";
  private static final String OPT_MAX_WRONG_CRED = "maxWrongCred";

  private Subject _subject;
  private CallbackHandler _callbackHandler;
  private Map _sharedState;
  private Map _options;
  private int _freezeTimeout = 10; //minutes
  private int _maxWrongCred = 3;

  // configurable option
  private boolean _debug = false;

  // the authentication status
  private boolean _succeeded = false;
  private boolean _commitSucceeded = false;

  // userID and password
  private String _userID;
  private char[] _password;
  private Long   _userUID;

  // principals
  private UserPrincipal _userPrincipal;
  private Collection    _rolePrincipals;

  // private credentials
  private PasswordCredential _passwordCred;

  /**
   * Default Constructor required for LoginContext to instantiate.
   */
  public UserLoginModule()
  {
  }

  /**
   * Initialize this <code>LoginModule</code>.
   *
   * @param subject the <code>Subject</code> to be authenticated.
   * @param callbackHandler a <code>CallbackHandler</code> for communicating
   *			with the end user (prompting for userIDs and
   *			passwords, for example).
   * @param sharedState shared <code>LoginModule</code> state.
   * @param options options specified in the login
   *			<code>Configuration</code> for this particular
   *			<code>LoginModule</code>.
   *
   * @since 2.0
   */
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map sharedState, Map options)
  {
    _subject = subject;
    _callbackHandler = callbackHandler;
    _sharedState = sharedState;
    _options = options;

    // initialize any configured options
    _debug = "true".equalsIgnoreCase((String)options.get(OPT_DEBUG));
    String timeoutStr = (String)options.get(OPT_FREEZE_TIMEOUT);
    if (timeoutStr != null)
      _freezeTimeout = Integer.parseInt(timeoutStr);
    String maxWrongStr = (String)options.get(OPT_MAX_WRONG_CRED);
    if (maxWrongStr != null)
      _maxWrongCred = Integer.parseInt(maxWrongStr);
  }

  /**
   * Authenticate the user by prompting for a userID and password.
   * <P>
   * Callbacks used:<BR>
   * <LI><CODE>NameCallback</CODE> - For User ID</LI>
   * <LI><CODE>PasswordCallback</CODE> - For User Password</LI>
   *
   * @return true if authentication succeeds (this <code>LoginModule</code>
   * should not be ignored).
   *
   * @exception FailedLoginException if the authentication fails. <p>
   *
   * @exception LoginException if this <code>LoginModule</code>
   *		is unable to perform the authentication.
   *
   * @since 2.0
   */
  public boolean login() throws LoginException
  {
    // prompt for a userID and password
    if (_callbackHandler == null)
      throw new LoginException("Error: no CallbackHandler available " +
        "to garner authentication information from the user");

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("Login userID: ");
    callbacks[1] = new PasswordCallback("Login password: ", false);

    try
    {
      _callbackHandler.handle(callbacks);
      _userID = ((NameCallback)callbacks[0]).getName();
      char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
      if (tmpPassword == null)
      {
        // treat a NULL password as an empty password
        tmpPassword = new char[0];
      }
      _password = new char[tmpPassword.length];
      System.arraycopy(tmpPassword, 0, _password, 0, tmpPassword.length);
      ((PasswordCallback)callbacks[1]).clearPassword();
    }
    catch (java.io.IOException ex)
    {
      throw new LoginException(ex.toString());
    }
    catch (UnsupportedCallbackException ex)
    {
      throw new LoginException("Error: " + ex.getCallback().toString() +
      " not available to garner authentication information " +
      "from the user");
    }

    // print debugging information
    if (_debug)
    {
      Logger.debug("[UserLoginModule.login] user entered userID: "+_userID);
      Logger.debug("[UserLoginModule.login] user entered password: "+
        new String(_password));
    }

    try
    {
      authenticate();

      // authentication succeeded!!!
      if (_debug)
        Logger.debug("[UserLoginModule.login] Authentication succeeded");

      _succeeded = true;
      return true;
    }
    catch (LoginException ex)
    {
      // authentication failed
      if (_debug)
        Logger.debug("[UserLoginModule.login] Authentication failed", ex);

      clearPrivateState();
      _succeeded = false;
      throw ex;
    }
  }

  /**
   * This method is called if the LoginContext's overall authentication
   * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   * LoginModules succeeded).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> method),
   * then this method associates a <code>UserPrincipal</code> and all his
   * <code>RolePrincipal</code>s with the <code>Subject</code> located in the
   * <code>LoginModule</code>.
   * If this LoginModule's own authentication attempted failed, then this
   * method removes any state that was originally saved.
   *
   * @exception LoginException if the commit fails.
   *
   * @return true if this LoginModule's own login and commit attempts succeeded,
   * or false otherwise.
   *
   * @since 2.0
   */
  public boolean commit() throws LoginException
  {
    if (!_succeeded)
    {
      return false;
    }
    else
    {
      // add a Principal (authenticated identity)
      // to the Subject

      // assume the user we authenticated is the UserPrincipal
      _userPrincipal = new UserPrincipal(_userID);
      //if (!_subject.getPrincipals(UserPrincipal.class).contains(_userPrincipal))
      _subject.getPrincipals().add(_userPrincipal);

      // add the Password private credential
      _passwordCred = new PasswordCredential(_password);
      //if (!_subject.getPrivateCredentials(PasswordCredential.class).contains(_passwordCred))
      _subject.getPrivateCredentials().add(_passwordCred);

      if (_debug)
        Logger.debug("[UserLoginModule.commit] Added UserPrincipal to Subject");

      // add Role principals
      try
      {
        _rolePrincipals = getRolePrincipals();
        _subject.getPrincipals().addAll(_rolePrincipals);
        if (_debug)
          Logger.debug("[UserLoginModule.commit] Added RolePrincipals to Subject");
      }
      catch (Exception ex)
      {
        Logger.warn("[UserLoginModule.commit] Unable to get RolePrincipals for Subject",
          ex);

        _commitSucceeded = false;
        return false;
      }

      // in any case, clean out state
      _userID = null;
      clearPassword();

      _commitSucceeded = true;
      return true;
    }
  }

  /**
   * <p> This method is called if the LoginContext's overall authentication
   * failed. (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   * LoginModules did not succeed).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> and
   * <code>commit</code> methods), then this method cleans up any state that
   * was originally saved.
   *
   * @exception LoginException if the abort fails.
   *
   * @return false if this LoginModule's own login and/or commit attempts
   * failed, and true otherwise.
   *
   * @since 2.0
   */
  public boolean abort() throws LoginException
  {
    if (!_succeeded)
    {
      return false;
    }
    else if (_succeeded && !_commitSucceeded)
    {
      // login succeeded but commit failed or overall authentication failed
      clearPrivateState();
      _succeeded = false;
    }
    else
    {
      // overall authentication succeeded and commit succeeded,
      // but someone else's commit failed
      logout();
    }
    return true;
  }

  /**
   * Logout the user.
   *
   * <p> This method removes the <code>UserPrincipal</code> and
   * <code>RolePrincipal</code>s that were added by the <code>commit</code>
   * method.
   *
   * @exception LoginException if the logout fails.
   *
   * @return true in all cases since this <code>LoginModule</code>
   * should not be ignored.
   *
   * @since 2.0
   */
  public boolean logout() throws LoginException
  {
    _subject.getPrincipals(UserPrincipal.class).remove(_userPrincipal);
    _subject.getPrincipals(RolePrincipal.class).removeAll(_rolePrincipals);
    _subject.getPrivateCredentials(PasswordCredential.class).remove(_passwordCred);

    clearPrivateState();
    _succeeded = false;
    _commitSucceeded = false;

    if (_debug)
      Logger.debug("[UserLoginModule.logout] Logout Subject");

    return true;
  }

  // ******************** Private methods *********************************
  /**
   * Actual authentication procedure. If the method safely returns, it means
   * authentication passes.
   *
   * @param LoginException Authentication fails.
   */
  private void authenticate() throws LoginException
  {
    UserAccount acct = null;
    boolean authenticate = false;
    IUserManagerObj mgr = null;
    try
    {
      mgr = getManager();
      acct = mgr.findUserAccount(_userID);
    }
    catch (Exception ex)
    {
      throw new LoginException(ex.getLocalizedMessage());
    }

    if (acct != null)
    {
      UserAccountState accountState = acct.getAccountState();

      //check whether account is deleted/disabled
      if (accountState.getState() != UserAccountState.STATE_ENABLED)
         throw new AccountExpiredException("Account is disabled/expired, signon not allowed!");

      //check whether account freezed
      if (accountState.isFreeze() &&
          !isFreezeTimeout(accountState.getFreezeTime()))
      {
        //have not timeout, fail
        throw new FailedLoginException("Account is freezed, signon not allowed!");
      }

      //check password, assumes that the password is already masked
      //but need to apply the mask length for the actual masked password.
      /**
      PasswordMask mask = PasswordMask.newMaskedPassword(String.valueOf(_password));
      mask.applyMaskLength(acct.getPasswordMaskLength());

      if (mask.toString().equals(acct.getPassword()))
      */
      if (acct.isPasswordMatch(String.valueOf(_password)))
      {
        authenticate = true;
        _userUID = (Long)acct.getKey();
      }
      else
      {
        //wrong credential, increment num login tries
        accountState.setNumLoginTries((short)(accountState.getNumLoginTries() + 1));

        //if exceed max num tries allowed, freeze account
        if (_maxWrongCred <= accountState.getNumLoginTries())
        {
          accountState.setIsFreeze(true);
          accountState.setFreezeTime(new java.sql.Timestamp(System.currentTimeMillis()));
	  Logger.debug("Trying to freeze account "+ acct.getId());

        }

        //update account
        try
        {
          mgr.updateUserAccount(acct);
	  Logger.debug("Freezed account updated "+ acct.getId());
	  UserAccount db_acct = mgr.findUserAccount(acct.getId());
	  Logger.debug("Freezed account freezed value "+ db_acct.getId() + " = " + db_acct.getAccountState().isFreeze());
        }
        catch (Exception ex)
        {
	  Logger.debug("Freezed account update has exception "+ acct.getId(), ex);
          throw new LoginException(ex.getLocalizedMessage());
        }
      }
    }

    if (!authenticate)
      throw new FailedLoginException("UserID/Password incorrect!");
  }

  /**
   * Clear the private state stored by this LoginModule.
   */
  private void clearPrivateState()
  {
    clearPassword();
    _userID = null;

    if (_passwordCred != null)
      _passwordCred.clearPassword();

    _userPrincipal = null;
    _passwordCred = null;
    _rolePrincipals = null;
  }

  /**
   * Clear the password stored by this LoginModule
   */
  private void clearPassword()
  {
    if (_password != null)
    {
      for (int i = 0; i < _password.length; i++)
        _password[i] = ' ';
      _password = null;
    }
  }

  /**
   * Checks whether the an account freeze time is over.
   * This checks against a freezeTimeout option initialized into this LoginModule.
   *
   * @param freezeTime Freeze time of the account.
   */
  private boolean isFreezeTimeout(Date freezeTime)
  {
    //current time
    GregorianCalendar cal =  new GregorianCalendar();
    //substract the timeout (minutes)
    cal.roll(GregorianCalendar.MINUTE, (0-_freezeTimeout));

    //if still later than freeze time means the freeze is timeout.
    return (cal.getTime().after(freezeTime));
  }

  /**
   * Get the UserManagerBean
   */
  private IUserManagerObj getManager() throws Exception
  {
    return (IUserManagerObj)ServiceLocator.instance(
               ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserManagerHome.class.getName(),
               IUserManagerHome.class,
               new Object[0]);
  }

  /**
   * Get the ACLManagerBean
   */
  private IACLManagerObj getACLManager() throws Exception
  {
    return (IACLManagerObj)ServiceLocator.instance(
               ServiceLocator.CLIENT_CONTEXT).getObj(
               IACLManagerHome.class.getName(),
               IACLManagerHome.class,
               new Object[0]);
  }

  /**
   * Get the RolePrincipals for the authenticated user.
   */
  private Collection getRolePrincipals() throws Exception
  {
    return getACLManager().getPrincipalsForSubject(
             _userUID, UserAccount.ENTITY_NAME);
  }
}
