/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.helpers;

import com.gridnode.gtas.server.backend.model.Port;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Port entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PortEntityHelper implements ICheckConflict
{

  private static PortEntityHelper _self = null;

  private PortEntityHelper()
  {
    super();
  }

  public static PortEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(PortEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new PortEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity port) throws Exception
  {
    Logger.debug("[PortEntityHelper.checkDuplicate] Start");
    String name = port.getFieldValue(Port.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Port.NAME, filter.getEqualOperator(),
      name, false);

    PortEntityHandler handler = PortEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Port)results.iterator().next();
    }
    return null;
  }
}