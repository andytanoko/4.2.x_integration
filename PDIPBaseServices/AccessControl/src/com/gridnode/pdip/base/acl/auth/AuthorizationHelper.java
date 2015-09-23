/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuthorizationHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    Neo Sok Lay         Created
 * Jun 10 2002    Neo Sok Lay         Add method to construct AccessPermissionCollection
 *                                    based on a collection of AccessRight
 *                                    objects.
 * Nov 11 2005    Neo Sok Lay         Change constructPermissions() to access PermissionCollection
 *                                    instead of AccessPermissionCollection.                                   
 */
package com.gridnode.pdip.base.acl.auth;

import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.AccessRight;

import java.security.PermissionCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * This class provides helper methods to construct the Principals, Permissions,
 * etc required for authorization.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AuthorizationHelper
{
  /**
   * Construct the principals based on the Role entities.
   *
   * @param roles A Collection of Role entities.
   * @return A Collection of RolePrincipal objects corresponding to each
   * Role entity.
   */
  public static Collection constructPrincipals(Collection roles)
  {
    Collection rolePrincipals = new ArrayList();

    if (roles == null || roles.isEmpty())
      return rolePrincipals;

    for (Iterator i=roles.iterator(); i.hasNext(); )
    {
      Role role = (Role)i.next();
      RolePrincipal principal = new RolePrincipal(
                                    (Long)role.getKey(),
                                    role.getRole());
      rolePrincipals.add(principal);
    }

    return rolePrincipals;
  }

  /**
   * Construct AccessPermission objects based on the AccessRight entities.
   *
   * @param accessRights A Collection of AccessRights entities.
   * @param permCollection A AccessPermissionCollection to contain the
   * constructed AccessPermission objects corresponding to the AccessRight
   * entities.
   */
  public static void constructPermissions(
    Collection accessRights, PermissionCollection permCollection)
  //Collection accessRights, AccessPermissionCollection permCollection)
  {
    if (accessRights == null || accessRights.isEmpty())
      return;

    for (Iterator i=accessRights.iterator(); i.hasNext(); )
    {
      AccessRight acr = (AccessRight)i.next();
      AccessPermission perm = new AccessPermission(
                                    acr.getFeature(),
                                    acr.getAction(),
                                    acr.getDataType(),
                                    acr.getCriteria());
      permCollection.add(perm);
    }
  }
}