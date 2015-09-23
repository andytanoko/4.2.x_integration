/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.helpers;

import com.gridnode.gtas.server.backend.model.Rfc;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Rfc entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class RfcEntityHelper implements ICheckConflict
{

  private static RfcEntityHelper _self = null;

  private RfcEntityHelper()
  {
    super();
  }

  public static RfcEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(RfcEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new RfcEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity rfc) throws Exception
  {
    Logger.debug("[RfcEntityHelper.checkDuplicate] Start");
    String name = rfc.getFieldValue(Rfc.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Rfc.NAME, filter.getEqualOperator(),
      name, false);

    RfcEntityHandler handler = RfcEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Rfc)results.iterator().next();
    }
    return null;
  }
}