/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.helpers;

import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the PartnerFunction entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerFunctionEntityHelper implements ICheckConflict
{

  private static PartnerFunctionEntityHelper _self = null;

  private PartnerFunctionEntityHelper()
  {
    super();
  }

  public static PartnerFunctionEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerFunctionEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new PartnerFunctionEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity partnerFunction) throws Exception
  {
    Logger.debug("[PartnerFunctionEntityHelper.checkDuplicate] Start");
    String partnerFunctionId =
      partnerFunction.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerFunction.PARTNER_FUNCTION_ID,
      filter.getEqualOperator(), partnerFunctionId, false);

    PartnerFunctionEntityHandler handler =
      PartnerFunctionEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (PartnerFunction)results.iterator().next();
    }
    return null;
  }
}