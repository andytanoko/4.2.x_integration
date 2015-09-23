/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.userprocedure.helpers;

import java.util.Collection;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class contains utitlies methods for the UserProcedure entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class UserProcedureEntityHelper implements ICheckConflict
{
  private static UserProcedureEntityHelper _self = null;

  private UserProcedureEntityHelper()
  {
    super();
  }

  public static UserProcedureEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(UserProcedureEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new UserProcedureEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity userProcedure)
    throws Exception
  {
    Logger.debug("[UserProcedureEntityHelper.checkDuplicate] Start");
    String name = userProcedure.getFieldValue(UserProcedure.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserProcedure.NAME, filter.getEqualOperator(),
      name, false);

    UserProcedureEntityHandler handler = UserProcedureEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (UserProcedure)results.iterator().next();
    }
    return null;
  }
}