/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.GetAlertTriggerEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the retrieving of a AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerAction
  extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4975154277924751784L;
	public static final String ACTION_NAME = "GetAlertTriggerAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertTriggerToMap((AlertTrigger)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAlertTriggerEvent getEvent = (GetAlertTriggerEvent)event;

    AlertTrigger record = ServiceLookupHelper.getGridTalkAlertMgr().findAlertTrigger(
                                   getEvent.getAlertTriggerUID());

    return record;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAlertTriggerEvent getEvent = (GetAlertTriggerEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getAlertTriggerUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetAlertTriggerEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}