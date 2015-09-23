/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAttachmentListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 14 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.GetAttachmentListEvent;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of Attachments.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAttachmentListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8268204304369743239L;
	public static final String CURSOR_PREFIX = "AttachmentListCursor.";
  public static final String ACTION_NAME = "GetAttachmentListAction";

  protected Class getExpectedEventClass()
  {
    return GetAttachmentListEvent.class;
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
    Collection gdocs = ActionHelper.getManager().findAttachments(filter);
    return gdocs;
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertAttachmentToMapObjects(entityList);
  }

}
