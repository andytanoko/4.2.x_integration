/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RolePrincipal.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.acl.auth;

import java.security.Principal;

/**
 * A instance of RolePrincipal represents a particular role of a subject in
 * the application. A subject having a certain RolePrincipal will determine
 * what kind of permissions he/she may have in the application.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class RolePrincipal
  implements Principal, java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2781429005815928446L;
	private Long _uID;
  private String _role;

  public RolePrincipal(Long uID, String role)
  {
    _uID = uID;
    _role = role;
  }

  public boolean equals(Object another)
  {
    if (another == null || !(another instanceof RolePrincipal))
      return false;

    RolePrincipal other = (RolePrincipal)another;
    return (getUID().equals(other.getUID()) &&
           getRole().equals(other.getRole()) &&
           getName().equals(other.getName()));
  }

  public String toString()
  {
    return getName() + ": " + getUID() + "-" + getRole();
  }

  public String getName()
  {
    return "RolePrincipal";
  }

  public Long getUID()
  {
    return _uID;
  }

  public String getRole()
  {
    return _role;
  }
}