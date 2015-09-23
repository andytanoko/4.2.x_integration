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

import com.gridnode.pdip.app.alert.model.Alert;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Alert entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class AlertEntityHelper implements ICheckConflict
{

  private static AlertEntityHelper _self = null;

  private AlertEntityHelper()
  {
    super();
  }

  public static AlertEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new AlertEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity alert) throws Exception
  {
    AlertLogger.debugLog("AlertEntityHelper", "checkDuplicate", "Start");
    String alertName = alert.getFieldValue(Alert.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Alert.NAME, filter.getEqualOperator(),
      alertName, false);

    AlertEntityHandler handler = AlertEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Alert)results.iterator().next();
    }
    return null;
  }
}