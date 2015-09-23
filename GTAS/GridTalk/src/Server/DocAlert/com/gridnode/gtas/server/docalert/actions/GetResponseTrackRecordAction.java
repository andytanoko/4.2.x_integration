/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetResponseTrackRecordAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 28 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.actions;

import java.util.Map;

import com.gridnode.gtas.events.docalert.GetResponseTrackRecordEvent;
import com.gridnode.gtas.server.docalert.helpers.ActionHelper;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the retrieving of a ResponseTrackRecord.
 * The ReminderAlert(s) for the ResponseTrackRecord are returned in the
 * REMINDER_ALERTS field.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetResponseTrackRecordAction
  extends AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7796361780256509907L;
	public static final String ACTION_NAME = "GetResponseTrackRecordAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertResponseTrackRecordToMap((ResponseTrackRecord)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetResponseTrackRecordEvent getEvent = (GetResponseTrackRecordEvent)event;

    ResponseTrackRecord record = ActionHelper.findResponseTrackRecord(
                                   getEvent.getResponseTrackRecordUID());

    return record;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetResponseTrackRecordEvent getEvent = (GetResponseTrackRecordEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getResponseTrackRecordUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetResponseTrackRecordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}