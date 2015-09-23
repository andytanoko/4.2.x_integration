/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.model.Trigger;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the Trigger entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class TriggerEntityHelper implements ICheckConflict
{

  private static TriggerEntityHelper _self = null;

  private TriggerEntityHelper()
  {
    super();
  }

  public static TriggerEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(TriggerEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new TriggerEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity trigger) throws Exception
  {
    Logger.debug("[TriggerEntityHelper.checkDuplicate] Start");
    Integer triggerLevel = (Integer)trigger.getFieldValue(Trigger.TRIGGER_LEVEL);
    Integer triggerType = (Integer)trigger.getFieldValue(Trigger.TRIGGER_TYPE);
    String docType = null;
    String partnerType = null;
    String partnerGroup = null;
    String partnerId = null;

    if (trigger.getFieldValue(Trigger.DOC_TYPE) != null)
    {
      docType = trigger.getFieldValue(Trigger.DOC_TYPE).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_TYPE) != null)
    {
      partnerType = trigger.getFieldValue(Trigger.PARTNER_TYPE).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_GROUP) != null)
    {
      partnerGroup = trigger.getFieldValue(Trigger.PARTNER_GROUP).toString();
    }
    if (trigger.getFieldValue(Trigger.PARTNER_ID) != null)
    {
      partnerId = trigger.getFieldValue(Trigger.PARTNER_ID).toString();
    }

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      Trigger.TRIGGER_LEVEL,
      filter.getEqualOperator(),
      triggerLevel,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.TRIGGER_TYPE,
      filter.getEqualOperator(),
      triggerType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.DOC_TYPE,
      filter.getEqualOperator(),
      docType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_TYPE,
      filter.getEqualOperator(),
      partnerType,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_GROUP,
      filter.getEqualOperator(),
      partnerGroup,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      Trigger.PARTNER_ID,
      filter.getEqualOperator(),
      partnerId,
      false);

    TriggerEntityHandler handler = TriggerEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (Trigger)results.iterator().next();
    }
    return null;
  }
}