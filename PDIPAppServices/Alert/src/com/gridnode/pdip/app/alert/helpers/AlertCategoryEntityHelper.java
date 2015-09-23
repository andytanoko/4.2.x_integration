/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertCategoryEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.AlertCategory;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the AlertCategory entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class AlertCategoryEntityHelper implements ICheckConflict
{

  private static AlertCategoryEntityHelper _self = null;

  private AlertCategoryEntityHelper()
  {
    super();
  }

  public static AlertCategoryEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertCategoryEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new AlertCategoryEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity category) throws Exception
  {
    AlertLogger.debugLog("AlertCategoryEntityHelper", "checkDuplicate", "Start");
    String categoryName = category.getFieldValue(AlertCategory.NAME).toString();
    String code = category.getFieldValue(AlertCategory.CODE).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, AlertCategory.NAME, filter.getEqualOperator(),
      categoryName, false);
    filter.addSingleFilter(filter.getAndConnector(), AlertCategory.CODE,
      filter.getEqualOperator(), code, false);

    AlertCategoryEntityHandler handler = AlertCategoryEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (AlertCategory)results.iterator().next();
    }
    return null;
  }
}