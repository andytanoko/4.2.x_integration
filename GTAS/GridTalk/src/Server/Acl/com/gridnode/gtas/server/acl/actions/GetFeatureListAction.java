/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFeatureListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityListAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Collection;

import com.gridnode.gtas.events.acl.GetFeatureListEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class GetFeatureListAction extends AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4393737655712031204L;
	public static final String CURSOR_PREFIX = "FeatureListCursor.";
  public static final String ACTION_NAME = "GetFeatureListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    //ignore filter for the moment
    return ActionHelper.getACLManager().getAllFeatures();
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertFeaturesToMapObjects(entityList);
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
    return GetFeatureListEvent.class;
  }
}