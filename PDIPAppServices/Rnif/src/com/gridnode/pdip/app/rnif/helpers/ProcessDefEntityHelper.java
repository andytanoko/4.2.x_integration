/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.rnif.helpers;

import com.gridnode.pdip.app.rnif.model.ProcessDef;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the ProcessDef entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ProcessDefEntityHelper implements ICheckConflict
{

  private static ProcessDefEntityHelper _self = null;

  private ProcessDefEntityHelper()
  {
    super();
  }

  public static ProcessDefEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(ProcessDefEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new ProcessDefEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity processDef) throws Exception
  {
    Logger.debug("[ProcessDefEntityHelper.checkDuplicate] Start");
    String processDefName = processDef.getFieldValue(ProcessDef.DEF_NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcessDef.DEF_NAME, filter.getEqualOperator(),
      processDefName, false);

    ProcessDefEntityHandler handler = ProcessDefEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (ProcessDef)results.iterator().next();
    }
    return null;
  }
}