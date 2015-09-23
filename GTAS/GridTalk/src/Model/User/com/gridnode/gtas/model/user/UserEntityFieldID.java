/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2002    Neo Sok Lay         Created
 * May 03 2002    Neo Sok Lay         Missed out STATE field.
 * Feb 06 2007    Chong SoonFui       Commented IUserAccountState.CAN_DELETE
 */
package com.gridnode.gtas.model.user;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the User module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserEntityFieldID
{
  private Hashtable _table;
  private static UserEntityFieldID _self = null;

  private UserEntityFieldID()
  {
    _table = new Hashtable();

    //UserAccount
    _table.put(IUserAccount.ENTITY_NAME,
      new Number[]
      {
        IUserAccount.ACCOUNT_STATE,
        IUserAccount.EMAIL,
        IUserAccount.ID,
        IUserAccount.NAME,
        IUserAccount.PASSWORD,
        IUserAccount.PHONE,
        IUserAccount.PROPERTY,
        IUserAccount.UID,
      });

    //UserAccountState
    _table.put(IUserAccountState.ENTITY_NAME,
      new Number[]
      {
//        IUserAccountState.CAN_DELETE,
        IUserAccountState.CREATE_BY,
        IUserAccountState.CREATE_TIME,
        IUserAccountState.FREEZE_TIME,
        IUserAccountState.IS_FREEZE,
        IUserAccountState.LAST_LOGIN_TIME,
        IUserAccountState.LAST_LOGOUT_TIME,
        IUserAccountState.NUM_LOGIN_TRIES,
        IUserAccountState.STATE,
        IUserAccountState.UID,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new UserEntityFieldID();
    }
    return _self._table;
  }
}