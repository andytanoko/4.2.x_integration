/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 19 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.user.helpers;

import com.gridnode.gtas.model.user.UserEntityFieldID;

import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;

import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

import java.util.Collection;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the User module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public final class ActionHelper
{
  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the UserManagerBean.
   *
   * @return The EJBObject to the UserManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IUserManagerObj getUserManager()
    throws ServiceLookupException
  {
    return (IUserManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IUserManagerHome.class.getName(),
      IUserManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for ACLManagerBean.
   *
   * @return The EJBObject for ACLManagerBean.
   * @exception ServiceLookupException Error in looking up ACLManagerBean.
   *
   * @since 2.0
   */
  public static IACLManagerObj getACLManager()
    throws ServiceLookupException
  {
    return (IACLManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IACLManagerHome.class.getName(),
             IACLManagerHome.class,
             new Object[0]);
  }

  /**
   * Obtain the GridNodeManagerBean by doing a Jndi lookup.
   *
   * @return The proxy interface to the GridNodeManagerBean.
   * @exception ServiceLookupException Error in looking up the GridNodeManagerBean.
   *
   * @since 2.0
   */
  public static IGridNodeManagerObj getGridNodeManager() throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IGridNodeManagerHome.class.getName(),
             IGridNodeManagerHome.class,
             new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of an UserAccount based on the specified uID.
   *
   * @param uID The UID of the UserAccount.
   * @return The UserAccount retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the UserManagerBean.
   * @exception Exception Bad User UID. No UserAccount exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static UserAccount verifyUserAccount(Long uID)
    throws Exception
  {
    try
    {
      return getUserManager().findUserAccount(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad User UID: "+uID);
    }
  }

  /**
   * Verify the existence of an UserAccount based on the specified userID.
   *
   * @param userID The UserID of the UserAccount.
   * @return The UserAccount retrieved using the specified userID.
   * @exception ServiceLookupException Error in obtaining the UserManagerBean.
   * @exception Exception Bad User ID. No UserAccount exists with the specified
   * userID.
   *
   * @since 2.0
   */
  public static UserAccount verifyUserAccount(String userID)
    throws Exception
  {
    try
    {
      return getUserManager().findUserAccount(userID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad User ID: "+userID);
    }
  }

  /**
   * Verify the existence in database of the user specified by the UID or
   * User ID.
   *
   * @param uID The UID of the UserAccount. If not null, verification will be
   * based on this value.
   * @param userId The UserID of the UserAccount. If uID is null and this value
   * is not empty, verification will be based on this value.
   * @return The retrieved UserAccount if the specified user is exists in
   * database.
   * @exception Exception The specfied user does not exists in the database, or
   * both parameters are null/empty.
   *
   * @since 2.0
   */
  public static UserAccount verifyUserAccount(Long uID, String userId)
    throws Exception
  {
    if (uID != null)
      return verifyUserAccount(uID);
    else if (!isEmpty(userId))
      return verifyUserAccount(userId);
    throw new Exception("No user specified!");
  }

  /**
   * Verify the existence of a Role in the database base on the specified
   * roleName.
   *
   * @param roleName The name of the Role to verify.
   * @return The Role that has the specified roleName.
   * @exception Exception Bad Role Name. No Role exists with the specified roleName.
   *
   * @since 2.0
   */
  public static Role verifyRole(String roleName) throws Exception
  {
    try
    {
      return getACLManager().getRoleByRoleName(roleName);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Role Name: "+roleName);
    }
  }

  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
  public static boolean isEmpty(String val)
  {
    return val==null || val.trim().length() == 0;
  }

  // ******************** Conversion Helpers ******************************

  /**
   * Convert a collection of UserAccounts to Map objects.
   *
   * @param userList The collection of UserAccounts to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of UserAccounts.
   *
   * @since 2.0
   */
  public static Collection convertUsersToMapObjects(Collection userList)
  {
    return UserAccount.convertEntitiesToMap(
             (UserAccount[])userList.toArray(new UserAccount[userList.size()]),
             UserEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an UserAccount to Map object.
   *
   * @param userAcct The UserAccount to convert.
   * @return A Map object converted from the specified UserAccount.
   *
   * @since 2.0
   */
  public static Map convertUserAccountToMap(UserAccount userAcct)
  {
    return UserAccount.convertToMap(
             userAcct,
             UserEntityFieldID.getEntityFieldID(),
             null);
  }


}