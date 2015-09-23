/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttributeKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??? ?? ????    Neo Sok Lay         Created
 * May 24 2002    Jared Low           Added a key for the GridForm module.
 * Jul 31 2002    Neo Sok Lay         Add key for Enterprise ID.
 * Aug 16 2002    Koh Han Sing        Add key for User name.
 */
package com.gridnode.gtas.server.rdm;

public interface IAttributeKeys
{
  static final String APPLICATION= "Application";
  static final String SESSION_ID = "SessionID";
  static final String USER_ID    = "UserID";
  static final String PRINCIPALS = "Principals";
  static final String ENTERPRISE_ID = "EnterpriseID";
  static final String USER_NAME = "UserName";

  //for entity list cursors, to be appended with list ID
  //static final String USER_LIST_CURSOR = "UserListCursor.";

  // GridForm.
  static final String FORM_OBJECT = "GFForm";
}