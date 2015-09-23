/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 * Apr 25 2002    Neo Sok Lay         Add methods to retrieve for read only
 *                                    purpose.
 * Apr 29 2002    Neo Sok Lay         Change to Remote interface.
 * May 15 2002    Neo Sok Lay         Add Login and Logout services.
 *                                    Change exceptions thrown.
 * May 20 2002    Neo Sok Lay         Add finder method to filter accounts
 *                                    by state in addition to filter condition.
 * Jun 13 2002    Neo Sok Lay         Return UID of the created user account.
 * Oct 20 2005    Neo Sok Lay         Business methods of the remote interface 
 *                                    must throw java.rmi.RemoteException.
 *                                    - The business method findUserAccounts 
 *                                      does not throw java.rmi.RemoteException
 */
package com.gridnode.pdip.app.user.facade.ejb;

import java.rmi.RemoteException;
import java.util.BitSet;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.user.exceptions.AuthenticateUserException;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for UserManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0
 */
public interface IUserManagerObj
  extends        EJBObject
{
  /**
   * Create a new User account.
   *
   * @param userAccount The UserAccount entity.
   * @return The UID of the created account.
   */
  public Long createUserAccount(UserAccount userAccount)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * Update a User account.
   *
   * @param userAccount The UserAccount entity with changes.
   */
  public void updateUserAccount(UserAccount userAccount)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Delete a User account. This will not physically delete the account
   * from the database. This will only mark the account as deleted.
   *
   * @param accountUId The UID of the User Account to delete.
   */
  public void deleteUserAccount(Long accountUId, boolean markDeleteOnly)
    throws DeleteEntityException, SystemException, RemoteException;

  //public void changePassword(String userId, String currentPwd, String newPasswd);

  //public void enableAccount(String userId);

 // public void disableAccount(String userId);

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
    throws AuthenticateUserException, SystemException, RemoteException;

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
    throws AuthenticateUserException, SystemException, RemoteException;

  /**
   * Find a User account using the Account UID.
   *
   * @param uID The UID of the account to find.
   * @return The User account found, or <B>null</B> if none exists with that
   * UID.
   */
  public UserAccount findUserAccount(Long uID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a User account using the UserID.
   *
   * @param userId The User ID of the account to find.
   * @return The User account found, or <B>null</B> if none exists with that
   * name.
   */
  public UserAccount findUserAccount(String userId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of User accounts using the User Name.
   *
   * @param userName The User Name of the accounts to find.
   * @return A Collection of User accounts found, or empty collection if none
   * exists.
   */
  public Collection findUserAccounts(String userName)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of User accounts using the account State.
   *
   * @param accountState The account State of the User accounts to find.
   * @return A Collection of User accounts found, or empty collection if none
   * exists.
   */
  public Collection findUserAccounts(short accountState)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of User accounts that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of User accounts found, or empty collection if none
   * exists.
   * @exception FindUserAccountException Error in executing the finder.
   */
  public Collection findUserAccounts(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the User Accounts that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of User accounts found, or empty
   * collection if none.
   * @excetpion FindUserAccountException Error in executing the finder.
   */
  public Collection findUserAccountsKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

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
    throws FindEntityException, SystemException, RemoteException;

}