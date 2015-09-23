/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2003    Neo Sok Lay         Created
 * Mar 03 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.providers;

import java.util.HashSet;
import java.util.Set;

/**
 * An EmailCode represents a mapping of a code to a list of alert email recipients.
 * An alert email recipient can be represent by any one of the following: <P>
 * <UL>
 *   <LI>Email address - Plain email address of the intended recipient</LI>
 *   <LI>User ID       - UserID of the UserAccount of the intended recipient.
 *                       The Email field in the UserAccount will be
 *                       used as the email address of the intended recipient.</LI>
 *   <LI>Role Name     - Role of the Users that are the intended recipients.
 *                       The Email field in each UserAccount having
 *                       the Role will be used as the email address of the
 *                       intended recipients.</LI>
 * </UL>
 * <P>
 * Each EmailCode has an <code>id</code> which represents the code of the mapping.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class EmailCode
{
  private String _id;
  private Set<String> _userRef = new HashSet<String>();
  private Set<String> _roleRef = new HashSet<String>();
  private Set<String> _emailRef = new HashSet<String>();

  public String getId()
  {
    return _id;
  }

  public void setId(String id)
  {
    _id = id;
  }

  public Set<String> getUserRef()
  {
    return _userRef;
  }

  public void setUserRef(Set<String> userRef)
  {
    _userRef = userRef;
  }

  public void setRoleRef(Set<String> roleRef)
  {
    _roleRef = roleRef;
  }

  public Set<String> getRoleRef()
  {
    return _roleRef;
  }

  public void setEmailRef(Set<String> emailRef)
  {
    _emailRef = emailRef;
  }

  public Set<String> getEmailRef()
  {
    return _emailRef;
  }


}