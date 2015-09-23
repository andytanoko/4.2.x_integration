/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetActivationRecordAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import java.util.Map;

import com.gridnode.gtas.events.activation.GetActivationRecordEvent;
import com.gridnode.gtas.server.activation.helpers.ActionHelper;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the retrieving of one ActivationRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetActivationRecordAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5011747835900729281L;
	public static final String ACTION_NAME = "GetActivationRecordAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertActivationRecordToMap((ActivationRecord)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetActivationRecordEvent getEvent = (GetActivationRecordEvent)event;

    ActivationRecord record = ServiceLookupHelper.getActivationManager().findActivationRecord(
                                getEvent.getRecordUID());

    record.getActivationDetails().populateBeLists();

    return record;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetActivationRecordEvent getEvent = (GetActivationRecordEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getRecordUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetActivationRecordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}