/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchDocumentAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.actions;

import java.util.Collection;

import com.gridnode.gtas.events.document.SearchDocumentEvent;
import com.gridnode.gtas.server.document.helpers.ActionHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the searching of documents.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchDocumentAction
  extends    AbstractGetEntityListAction2
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4548290962719281510L;
	public static final String CURSOR_PREFIX = "SearchDocumentListCursor.";
  public static final String ACTION_NAME = "SearchDocumentAction";

  protected Long _queryUid;

  protected Class getExpectedEventClass()
  {
    return SearchDocumentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  protected Number getEntityKeyID()
  {
    return GridDocument.UID;
  }

  protected Collection retrieveEntityKeys(IDataFilter filter)
    throws Exception
  {
    Collection entityKeys =
      ActionHelper.getManager().searchDoc(filter, _queryUid);
    return entityKeys;
  }

  /**
   * This method is called from AbstractGetEntityListAction2
   */
  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    Collection entityList = ActionHelper.getManager().findGridDocuments(filter);
    return entityList;
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertGridDocumentToMapObjects(entityList);
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    SearchDocumentEvent searchEvent = (SearchDocumentEvent)event;
    _queryUid = searchEvent.getQueryUid();

    return constructEventResponse(getNextList((GetEntityListEvent) event));

  }
}