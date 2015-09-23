/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserPrincipal.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2002    Neo Sok Lay         Created
 * Nov 08 2005    Neo Sok Lay         Change getName() to return userId
 */
package com.gridnode.pdip.app.user.login;

import java.security.Principal;

/**
 * Principal for User identity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserPrincipal
  implements Principal,
             java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4336346366550129540L;
	private String _userID;

  /**
   * Construct a UserPrincipal
   *
   * @param userID The user ID of the user
   */
  public UserPrincipal(String userID)
  {
    _userID = userID;
  }

  /**
   * Get the name of this Principal. This name will be used to fetch the permissions
   * in the security Policy.
   */
  public String getName()
  {
    //return "UserPrincipal";
  	return _userID;
  }

  /**
   * Description of this particular UserPrincipal object.
   */
  public String toString()
  {
    return _userID;
  }
}