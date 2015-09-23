/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.server.alert.model.AlertTrigger;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the AlertTrigger entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class AlertTriggerEntityHelper implements ICheckConflict
{

  private static AlertTriggerEntityHelper _self = null;

  private AlertTriggerEntityHelper()
  {
    super();
  }

  public static AlertTriggerEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(AlertTriggerEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new AlertTriggerEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity alertTrigger) throws Exception
  {
    Logger.debug("[AlertTriggerEntityHelper.checkDuplicate] Start");

    AlertTrigger trigger = (AlertTrigger)alertTrigger;
    DataFilterImpl filter = new DataFilterImpl();

    filter.addSingleFilter(null, AlertTrigger.LEVEL, filter.getEqualOperator(),
                           trigger.getFieldValue(AlertTrigger.LEVEL), false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.ALERT_TYPE,
                           filter.getEqualOperator(), trigger.getAlertType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.DOC_TYPE,
                           filter.getEqualOperator(), trigger.getDocumentType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_TYPE,
                           filter.getEqualOperator(), trigger.getPartnerType(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_GROUP,
                           filter.getEqualOperator(), trigger.getPartnerGroup(),
                           false);

    filter.addSingleFilter(filter.getAndConnector(), AlertTrigger.PARTNER_ID,
                           filter.getEqualOperator(), trigger.getPartnerId(),
                           false);

    AlertTriggerEntityHandler handler = AlertTriggerEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (AlertTrigger)results.iterator().next();
    }
    return null;
  }
}