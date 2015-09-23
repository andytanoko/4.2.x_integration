/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.partner.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class contains utitlies methods for the PartnerGroup entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class PartnerGroupEntityHelper implements ICheckConflict
{

  private static PartnerGroupEntityHelper _self = null;

  private PartnerGroupEntityHelper()
  {
    super();
  }

  public static PartnerGroupEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(PartnerGroupEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new PartnerGroupEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity partnerGroup) throws Exception
  {
    Logger.debug("[PartnerGroupEntityHelper.checkDuplicate] Start");
    String name = partnerGroup.getFieldValue(PartnerGroup.NAME).toString();
//    PartnerType partnerType =
//      (PartnerType)partnerGroup.getFieldValue(PartnerGroup.PARTNER_TYPE);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerGroup.NAME, filter.getEqualOperator(),
      name, false);
//    filter.addSingleFilter(filter.getAndConnector(), PartnerGroup.PARTNER_TYPE,
//      filter.getEqualOperator(), partnerType.getKey(), false);

    PartnerGroupEntityHandler handler = PartnerGroupEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (PartnerGroup)results.iterator().next();
    }
    return null;
  }
}