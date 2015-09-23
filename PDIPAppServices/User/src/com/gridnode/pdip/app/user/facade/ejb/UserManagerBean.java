/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 * May 03 2002    Neo Sok Lay         Catch ApplicationException instead of
 *                                    FinderException for findUserAccount() method.
 * May 05 2002    Neo Sok Lay         Add login and logout services.
 *                                    Change exceptions thrown.
 * May 20 2002    Neo Sok Lay         Add finder method to filter accounts
 *                                    by state in addition to filter condition.
 * Jun 13 2002    Neo Sok Lay         Return UID of created user account.
 * Jan 08 2003    Neo Sok Lay         Did not check for null filter before
 *                                    logging filterExpr.
 *                                    Refactor to use FacadeLogger for logging.
 */
package com.gridnode.pdip.app.user.facade.ejb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.user.exceptions.AuthenticateUserException;
import com.gridnode.pdip.app.user.helpers.HttpRequestHelper;
import com.gridnode.pdip.app.user.helpers.Logger;
import com.gridnode.pdip.app.user.helpers.UserEntityHandler;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.util.PasswordMask;

/**
 * This bean manages the User accounts.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0
 */
public class UserManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2353683486454454536L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ************************ Implementing methods in IUserManagerLocalObj

  /**
   * Create a new User account.
   *
   * @param userAccount The UserAccount entity.
   * @return The UID of the created account.
   */
  public Long createUserAccount(UserAccount userAccount)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "createUserAccount";
    Object[] params     = new Object[] {userAccount};

    Long key          = null;
    try
    {
      logger.logEntry(methodName, params);

      key = (Long)getEntityHandler().createUserAccount(userAccount).getKey();
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return key;
  }

  /**
   * Update a User account.
   *
   * @param userAccount The UserAccount entity with changes.
   */
  public void updateUserAccount(UserAccount userAccount)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "updateUserAccount";
    Object[] params     = new Object[] {
                            userAccount};

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().update(userAccount);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Delete a User account.
   *
   * @param accountUId The UID of the User account to delete.
   * @param markDeleteOnly <B>true</B> to only mark the account as deleted
   * without physically deleting the account from the database. <B>false</B>
   * otherwise.
   */
  public void deleteUserAccount(Long accountUId, boolean markDeleteOnly)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "deleteUserAccount";
    Object[] params     = new Object[] {
                            accountUId,
                            markDeleteOnly? Boolean.TRUE : Boolean.FALSE};

    try
    {
      logger.logEntry(methodName, params);

      if (markDeleteOnly)
        getEntityHandler().markDeleteUserAccount(accountUId);
      else
        getEntityHandler().deleteUserAccount(accountUId);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Login into an application.
   * <P>
   * This method calls to a Servlet to process the login request.
   *
   * @param application The name of the application to logon to. This is the
   * application name of the Login configuration to pickup during Login.
   * Refer to JAAS documentation.
   * @param sessionID Unique SessionID of the session that the client is having.
   * @param userID UserID of the client to signon.
   * @param password Password obtained from the client, encrypted using
   * <CODE>PasswordMask</CODE>
   *
   * @exception AuthenticateUserException Error during login process.
   * @see com.gridnode.pdip.framework.util.PasswordMask
   */
  public void login(String application, String sessionID, String userID, String password)
    throws AuthenticateUserException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "login";
    Object[] params     = new Object[] {
                            application,
                            sessionID,
                            userID,
                            new PasswordMask(password)};

    try
    {
      logger.logEntry(methodName, params);

      HttpRequestHelper.postSignOnRequest(application, sessionID, userID, password);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new AuthenticateUserException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Logout of a session.
   * <P>
   * This method calls to a Servlet to process the logout request.
   *
   * @param sessionID Unique sessionID of the session that the client intends
   * to logout of.
   *
   * @exception AuthenticateUserException Error during logout process.
   */
  public void logout(String sessionID)
    throws AuthenticateUserException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "logout";
    Object[] params     = new Object[] {
                            sessionID};

    try
    {
      logger.logEntry(methodName, params);

      HttpRequestHelper.postSignOffRequest(sessionID);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new AuthenticateUserException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Find a User account using the UserID.
   *
   * @param userId The User ID of the account to find.
   * @return The User account found, or <B>null</B> if none exists with that
   * name.
   */
  public UserAccount findUserAccount(String userId)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccount";
    Object[] params     = new Object[] {
                            userId};

    UserAccount acct = null;

    try
    {
      logger.logEntry(methodName, params);

      acct = getEntityHandler().findByUserId(userId);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return acct;
  }

  /**
   * Find a User account using the Account UID.
   *
   * @param uID The UID of the account to find.
   * @return The User account found
   * @exception FindUserAccountException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public UserAccount findUserAccount(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccount";
    Object[] params     = new Object[] {
                            uID};

    UserAccount acct = null;

    try
    {
      logger.logEntry(methodName, params);

      acct = (UserAccount)getEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return acct;
  }

  /**
   * Find a number of User accounts using the User Name.
   *
   * @param userName The User Name of the accounts to find.
   * @return A Collection of User accounts found, or empty collection if none
   * exists.
   */
  public Collection findUserAccounts(String userName)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccounts";
    Object[] params     = new Object[] {
                            userName};

    Collection accts = null;

    try
    {
      logger.logEntry(methodName, params);

      accts = getEntityHandler().findByUserName(userName);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return accts;
  }

  /**
   * Find a number of User accounts using the account State.
   *
   * @param accountState The account State of the User accounts to find.
   * @return A Collection of User accounts found, or empty collection if none
   * exists.
   */
  public Collection findUserAccounts(short accountState)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccounts";
    Object[] params     = new Object[] {
                            new Short(accountState)};

    Collection accts = null;

    try
    {
      logger.logEntry(methodName, params);

      accts = getEntityHandler().findByAccountState(accountState);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return accts;
  }

  /**
   * Find a number of User accounts that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of User accounts found, or empty collection if none
   * exists.
   * @exception FindUserAccountException Error in executing the finder.
   */
  public Collection findUserAccounts(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccounts";
    Object[] params     = new Object[] {
                            filter};

    Collection accts = null;

    try
    {
      logger.logEntry(methodName, params);

      accts = getEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return accts;
  }

  /**
   * Find the keys of the User Accounts that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of User accounts found, or empty
   * collection if none.
   * @excetpion FindUserAccountException Error in executing the finder.
   */
  public Collection findUserAccountsKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccountsKeys";
    Object[] params     = new Object[] {
                            filter};

    Collection acctKeys = null;

    try
    {
      logger.logEntry(methodName, params);

      acctKeys = getEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return acctKeys;
  }

  /**
   * Find a number of User accounts that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @param excludeStateSet A BitSet containing a bit corresponding to a state, such
   * that if the bit is true, the accounts of that state will be excluded from
   * the result set.
   * <P>For example, to exclude those accounts that are marked deleted,<P>
   * <PRE>
   * BitSet stateSet = new BitSet();
   * stateSet.set(IUserAccountState.STATE_DELETED);
   * </PRE>
   * @return a Collection of User accounts found, or empty collection if none
   * exists.
   * @exception FindUserAccountException Error in executing the finder.
   */
  public Collection findUserAccounts(IDataFilter filter, BitSet excludeStateSet)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getManagerFacadeLogger();
    String methodName   = "findUserAccounts";
    Object[] params     = new Object[] {
                            filter,
                            excludeStateSet};

    Collection accts = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      Collection initAccts = getEntityHandler().getEntityByFilterForReadOnly(filter);

      for (Iterator i=initAccts.iterator(); i.hasNext(); )
      {
        UserAccount acct = (UserAccount)i.next();
        if (!excludeStateSet.get(acct.getAccountState().getState()))
        {
          accts.add(acct);
        }
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return accts;
  }

  private UserEntityHandler getEntityHandler()
  {
     return UserEntityHandler.getInstance();
  }
}