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
package com.gridnode.gtas.server.acl.helpers;

import com.gridnode.gtas.model.acl.AccessRightEntityFieldID;
import com.gridnode.gtas.model.acl.FeatureEntityFieldID;
import com.gridnode.gtas.model.acl.RoleEntityFieldID;
import com.gridnode.gtas.model.user.UserEntityFieldID;

import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.*;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;

import java.util.Collection;
import java.util.Map;

/**
 * This helper class provides methods to:<P>
 * <LI>obtain manager proxy objects</LI>
 * <LI>verify existence of (with retrieval) entities</LI>
 * <LI>convert entities to Map objects</LI>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public final class ActionHelper
{
  // ******************** Get Manager Helpers ******************************

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
   * Obtain the EJBObject for the UserManagerBean.
   *
   * @return The EJBObject to the UserManagerBean.
   * @exception ServiceLookupException Error in looking up UserManagerBean.
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

  // ******************** Verification Helpers **************************

  /**
   * Verify the existence of a Role in the database base on the specified
   * uID.
   *
   * @param uID The UID of the Role to verify.
   * @return The Role that has the specified uID.
   * @exception Exception Bad Role UID. No Role exists with the specified uID.
   *
   * @since 2.0
   */
  public static Role verifyRole(Long uID) throws Exception
  {
    try
    {
      return getACLManager().getRoleByUId(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad Role UID: "+uID);
    }
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
    Role role = null;
    try
    {
      role = getACLManager().getRoleByRoleName(roleName);
    }
    catch (FindEntityException ex)
    {
    }

    if (role == null)
      throw new Exception("Bad Role Name: "+roleName);

    return role;
  }

  /**
   * Verify the existence in database of the role specified by the uId or name.
   *
   * @param uId The UID of the Role. If not null, verification will be based
   * on this value.
   * @param name The Name of the Role. If uId is null, and this is not empty,
   * verification will be based on this value.
   * @return The retrieved Role if the specified role exists in database.
   * @exception Exception The specified role in event does not exists in the
   * database, or both parameters are null/empty.
   *
   * @since 2.0
   */
  public static Role verifyRole(Long uId, String name) throws Exception
  {
    if (uId != null)
      return verifyRole(uId);
    else if (!isEmpty(name))
      return verifyRole(name);
    throw new Exception("Role not specified!");
  }

  /**
   * Verify the existence of a certain combination of Feature, Action, and
   * DataType in the Feature database. To exist, a Feature must exist having
   * the specified Feature name. The action and data type (if not null) must
   * be one of the available actions or data types listed in the corresponding
   * Feature record.
   *
   * @param featureName The name of the feature.
   * @param action The Action.
   * @param dataType The DataType.
   * @exception Exception Bad Feature, Bad Action, or Bad DataType.
   *
   * @since 2.0
   */
  public static void verifyFeatureActionDataType(
    String featureName, String action, String dataType) throws Exception
  {
    Feature feature = null;
    try
    {
      feature = getACLManager().getFeatureByFeatureName(featureName);
    }
    catch (FindEntityException ex)
    {
    }

    if (feature == null)
      throw new Exception("Bad Feature: "+featureName);

    if (!feature.getActions().contains(action))
      throw new Exception("Bad Action:"+action + " for Feature: "+featureName);

    if (!isEmpty(dataType))
    {
      if (!feature.getDataTypes().contains(dataType))
        throw new Exception(
                    "Bad DataType: "+dataType +
                    " for Feature/Action: "+ featureName + "/" + action);
    }
  }

  /**
   * Verify the existence of a AccessRight in the database base on the specified
   * uID.
   *
   * @param uID The UID of the AccessRight to verify.
   * @return The AccessRight that has the specified uID.
   * @exception Exception Bad AccessRight UID. No AccessRight exists with the
   * specified uID.
   *
   * @since 2.0
   */
  public static AccessRight verifyAccessRight(Long uID) throws Exception
  {
    try
    {
      return getACLManager().getAccessRight(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad AccessRight UID: "+uID);
    }
  }

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
    UserAccount acct = null;

    try
    {
      acct = getUserManager().findUserAccount(userID);
    }
    catch (FindEntityException ex)
    {
    }

    if (acct == null)
      throw new Exception("Bad User ID: "+userID);

    return acct;
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

  // ****************** Conversion helpers ******************************

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
   * Convert a collection of AccessRights to Map objects.
   *
   * @param acrList The collection of AccessRights to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of AccessRights.
   *
   * @since 2.0
   */
  public static Collection convertAccessRightsToMapObjects(Collection acrList)
  {
    return AccessRight.convertEntitiesToMap(
             (AccessRight[])acrList.toArray(new AccessRight[acrList.size()]),
             AccessRightEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an AccessRight to Map object.
   *
   * @param acr The AccessRight to convert.
   * @return A Map object converted from the specified AccessRight.
   *
   * @since 2.0
   */
  public static Map convertAccessRightToMap(AccessRight acr)
  {
    return AccessRight.convertToMap(
             acr,
             AccessRightEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Roles to Map objects.
   *
   * @param roleList The collection of Roles to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Roles.
   *
   * @since 2.0
   */
  public static Collection convertRolesToMapObjects(Collection roleList)
  {
    return Role.convertEntitiesToMap(
             (Role[])roleList.toArray(new Role[roleList.size()]),
             RoleEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an Role to Map object.
   *
   * @param role The Role to convert.
   * @return A Map object converted from the specified Role.
   *
   * @since 2.0
   */
  public static Map convertRoleToMap(Role role)
  {
    return Role.convertToMap(
             role,
             RoleEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Features to Map objects.
   *
   * @param featureList The collection of Features to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Features.
   *
   * @since 2.0
   */
  public static Collection convertFeaturesToMapObjects(Collection featureList)
  {
    return Feature.convertEntitiesToMap(
             (Feature[])featureList.toArray(new Feature[featureList.size()]),
             FeatureEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an Feature to Map object.
   *
   * @param feature The Feature to convert.
   * @return A Map object converted from the specified Feature.
   *
   * @since 2.0
   */
  public static Map convertFeatureToMap(Feature feature)
  {
    return Feature.convertToMap(
             feature,
             FeatureEntityFieldID.getEntityFieldID(),
             null);
  }

}