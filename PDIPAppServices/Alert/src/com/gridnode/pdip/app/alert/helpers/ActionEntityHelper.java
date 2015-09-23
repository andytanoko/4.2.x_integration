/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.Action;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Action entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ActionEntityHelper implements ICheckConflict
{

  private static ActionEntityHelper _self = null;

  private ActionEntityHelper()
  {
    super();
  }

  public static ActionEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(ActionEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new ActionEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity action) throws Exception
  {
    AlertLogger.debugLog("ActionEntityHelper", "checkDuplicate", "Start");
    String actionName = action.getFieldValue(Action.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Action.NAME, filter.getEqualOperator(),
      actionName, false);

    ActionEntityHandler handler = ActionEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Action)results.iterator().next();
    }
    return null;
  }
}