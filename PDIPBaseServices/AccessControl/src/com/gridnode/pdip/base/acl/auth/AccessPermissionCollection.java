/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessPermission.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 31 2005    Neo Sok Lay         Created. Moved from AccessPermission.java
 */
package com.gridnode.pdip.base.acl.auth;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * An AccessPermissionCollection stores a set of AccessPermission
 * permissions.
 */
public final class AccessPermissionCollection
  extends   PermissionCollection
  implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2894897274981960828L;
	private Hashtable _permissions;
  private boolean   _allAllowed;

  /**
   * Creates an empty AccessPermissionCollection object.
   */
  public AccessPermissionCollection()
  {
    _permissions = new Hashtable();
    _allAllowed = false;
  }

  /**
   * Adds a permission to the AccessPermissions. The key for the hash is
   * the name.
   *
   * @param permission the Permission object to add.
   *
   * @exception IllegalArgumentException - if the permission is not a
   *                                       AccessPermission or the permission
   *                                       contains uncloneable data.
   *
   * @exception SecurityException - if this AccessPermissionCollection
   *                                object has been marked readonly
   */
  public void add(Permission permission)
  {
    if (!(permission instanceof AccessPermission))
      throw new IllegalArgumentException("invalid permission: "+ permission);

    if (isReadOnly())
      throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");

    AccessPermission perm = (AccessPermission) permission;

    AccessPermission existing =
        (AccessPermission)_permissions.get(perm.getName());

    if (existing != null)
    {
      try
      {
        _permissions.put(perm.getName(), new AccessPermission(
          perm.getName(), AccessPermission.unionActions(
            existing.getActionsAccessMap(),
            perm.getActionsAccessMap())));
      }
      catch (CloneNotSupportedException ex)
      {
        throw new IllegalArgumentException("Permission contains uncloneable objects: "+ex.getMessage());
      }
    }
    else
    {
      _permissions.put(perm.getName(), permission);
    }

    if (!_allAllowed)
    {
      if (perm.getName().equals("*"))
        _allAllowed = true;
    }
  }

  /**
   * Check and see if this set of permissions implies the permissions
   * expressed in "permission".
   *
   * @param permission the Permission object to compare
   *
   * @return true if "permission" is a proper subset of a permission in
   * the set, false if not.
   */
  public boolean implies(Permission permission)
  {
    if (!(permission instanceof AccessPermission))
      return false;

    AccessPermission x;

    // short circuit if the "*" Permission was added
    if (_allAllowed)
    {
      x = (AccessPermission)_permissions.get("*");
      if (x != null && x.implies(permission))
        return true;
    }

    // strategy:
    // Check for full match first. Then work our way up the
    // name looking for matches on a.b.*

    String name = permission.getName();

    x = (AccessPermission)_permissions.get(name);
    if (x != null && x.implies(permission))
      return true;

    // work our way up the tree...
    int last, offset;

    offset = name.length()-1;

    while ((last = name.lastIndexOf(".", offset)) != -1)
    {
      //replace last sub-feature by "*"
      name = name.substring(0, last+1) + "*";
      //System.out.println("check "+name);
      x = (AccessPermission)_permissions.get(name);

      if (x != null && x.implies(permission))
        return true;
      offset = last -1;
    }

    // we don't have to check for "*" as it was already checked
    // at the top (all_allowed), so we just return false
    return false;
  }

  /**
   * Returns an enumeration of all the AccessPermission objects in the
   * container.
   *
   * @return an enumeration of all the AccessPermission objects.
   */
  public Enumeration elements()
  {
    return _permissions.elements();
  }

  public String toString()
  {
    String lineSep = System.getProperty("line.separator");
    StringBuffer buff = new StringBuffer("AccessPermissionCollection: ");
    buff.append(_permissions.size()).append(" Permissions").append(lineSep);


    for (Enumeration e=elements(); e.hasMoreElements(); )
    {
      buff.append('\t').append(e.nextElement()).append(lineSep);
    }

    return buff.toString();
  }
}