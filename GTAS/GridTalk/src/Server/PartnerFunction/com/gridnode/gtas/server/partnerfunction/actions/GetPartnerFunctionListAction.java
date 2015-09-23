/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerFunctionListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.actions;

import java.util.Collection;

import com.gridnode.gtas.events.partnerfunction.GetPartnerFunctionListEvent;
import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetPartnerFunctionListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4108378369822149020L;
	public static final String CURSOR_PREFIX = "PartnerFunctionListCursor.";
  public static final String ACTION_NAME = "GetPartnerFunctionListAction";

  protected Class getExpectedEventClass()
  {
    return GetPartnerFunctionListEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.getManager().findPartnerFunctions(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertPartnerFunctionToMapObjects(entityList);
  }

}
