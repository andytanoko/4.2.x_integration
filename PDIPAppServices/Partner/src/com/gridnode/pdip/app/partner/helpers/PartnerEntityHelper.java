/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.partner.helpers;

import com.gridnode.pdip.app.partner.model.Partner;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Partner entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerEntityHelper implements ICheckConflict
{

  private static PartnerEntityHelper _self = null;

  private PartnerEntityHelper()
  {
    super();
  }

  public static PartnerEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new PartnerEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity partner) throws Exception
  {
    Logger.debug("[PartnerEntityHelper.checkDuplicate] Start");
    String partnerId = partner.getFieldValue(Partner.PARTNER_ID).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Partner.PARTNER_ID, filter.getEqualOperator(),
      partnerId, false);

    PartnerEntityHandler handler = PartnerEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Partner)results.iterator().next();
    }
    return null;
  }
}