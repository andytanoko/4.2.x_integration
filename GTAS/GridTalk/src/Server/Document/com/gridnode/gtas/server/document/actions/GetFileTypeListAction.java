/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFileTypeListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 26 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.GetFileTypeListEvent;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetFileTypeListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5063426623613975867L;
	public static final String CURSOR_PREFIX = "FileTypeListCursor.";
  public static final String ACTION_NAME = "GetFileTypeListAction";

  protected Class getExpectedEventClass()
  {
    return GetFileTypeListEvent.class;
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
    return ActionHelper.getManager().findFileTypes(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertFileTypeToMapObjects(entityList);
  }

}
