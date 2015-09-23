/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetActivationRecordListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.events.activation.GetActivationRecordListEvent;
import com.gridnode.gtas.server.activation.helpers.ActionHelper;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import java.util.Iterator;

/**
 * This Action class handles the retrieving of a list of ActivationRecord(s).
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetActivationRecordListAction
  extends    AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9210041838426463999L;
	public static final String CURSOR_PREFIX = "ActivationRecordListCursor.";
  public static final String ACTION_NAME = "GetActivationRecordListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ServiceLookupHelper.getActivationManager().findActivationRecordsByFilter(
             filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    for (Iterator i=entityList.iterator(); i.hasNext(); )
    {
      ActivationRecord record = (ActivationRecord)i.next();
      record.getActivationDetails().populateBeLists();
    }

    return ActionHelper.convertActRecordsToMapObjects(entityList);
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  // ***************** AbstractGridTalkAction methods ***********************

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetActivationRecordListEvent.class;
  }

}