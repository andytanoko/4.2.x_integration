/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTypeEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.AlertType;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the AlertType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class AlertTypeEntityHelper implements ICheckConflict
{

  private static AlertTypeEntityHelper _self = null;

  private AlertTypeEntityHelper()
  {
    super();
  }

  public static AlertTypeEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertTypeEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new AlertTypeEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity category) throws Exception
  {
    AlertLogger.debugLog("AlertTypeEntityHelper", "checkDuplicate", "Start");
    String name = category.getFieldValue(AlertType.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AlertType.NAME, filter.getEqualOperator(),
      name, false);

    AlertTypeEntityHandler handler = AlertTypeEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (AlertType)results.iterator().next();
    }
    return null;
  }
}