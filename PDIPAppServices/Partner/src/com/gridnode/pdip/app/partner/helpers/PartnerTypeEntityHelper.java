/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.partner.helpers;

import com.gridnode.pdip.app.partner.model.PartnerType;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the PartnerType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerTypeEntityHelper implements ICheckConflict
{

  private static PartnerTypeEntityHelper _self = null;

  private PartnerTypeEntityHelper()
  {
    super();
  }

  public static PartnerTypeEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerTypeEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new PartnerTypeEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity partnerType) throws Exception
  {
    Logger.debug("[PartnerTypeEntityHelper.checkDuplicate] Start");
    String name = partnerType.getFieldValue(PartnerType.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerType.NAME, filter.getEqualOperator(),
      name, false);

    PartnerTypeEntityHandler handler = PartnerTypeEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (PartnerType)results.iterator().next();
    }
    return null;
  }
}