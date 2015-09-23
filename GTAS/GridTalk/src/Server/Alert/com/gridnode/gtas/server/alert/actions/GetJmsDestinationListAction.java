/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetJmsDestinationListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.events.alert.GetJmsDestinationListEvent;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityListEvent;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import java.util.BitSet;

public class GetJmsDestinationListAction
  extends    AbstractGetEntityListAction
{ 
	public static final String CURSOR_PREFIX = "JmsDestinationListCursor.";
  public static final String ACTION_NAME = "GetJmsDestinationListAction";

//  private static final BitSet _stateSet = new BitSet();
//  static
//  {
//    _stateSet.set((int)BusinessEntity.STATE_DELETED);
//  }

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
  	return ServiceLookupHelper.getAlertManager().getJmsDestinations(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
  	return ActionHelper.convertJmsDestinationsToMapObjects(entityList);
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
    return GetJmsDestinationListEvent.class;
  }

}