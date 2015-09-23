/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistryConnectInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         Refactored to return a Collection of
 *                                    RegistryConnectInfo Maps instead of
 *                                    RegistryConnectInfoList Map.
 *                                    Extends AbstractGetEntityListAction.
 * Sep 24 2003    Neo Sok Lay         Retrieve RegistryConnectInfo(s) from
 *                                    BizRegManager instead of RegistryConnectionInfoEntityHandler.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoListEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This action handles the GetRegistryConnectInfoListEvent.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class GetRegistryConnectInfoListAction extends AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5648168002401889948L;
	public static final String CURSOR_PREFIX = "RegistryConnectInfoListCursor.";
  private static final String ACTION_NAME = "GetRegistryConnectInfoListAction";
  
  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.getBizRegManager().findRegistryConnectInfos(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertRegConnInfosToMapObjects(entityList);
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getActionName()
   */
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#getExpectedEventClass()
   */
  protected Class getExpectedEventClass()
  {
    return GetRegistryConnectInfoListEvent.class;
  }

}
