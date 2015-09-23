/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsPiListAction.java
 *
 ****************************************************************************
 * Date           	Author              		Changes
 ****************************************************************************
 * Oct 3 2005		Sumedh Chalermkanjana       Created
 */
package com.gridnode.gtas.server.dbarchive.actions;

import java.util.Collection;

import com.gridnode.gtas.server.dbarchive.helpers.*;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.gtas.model.dbarchive.IProcessInstanceMetaInfo;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.gtas.server.dbarchive.model.*;
import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.gtas.model.dbarchive.EsPiEntityFieldID;

/**
 * Note: The implementation of this class is copied from SearchDocumentAction.java
 */
public class GetEsPiListAction extends AbstractGetEntityListAction2
{
  public static final String CURSOR_PREFIX = "EsPiListCursor.";

  public static final String ACTION_NAME = "GetEsPiListAction";

  protected Class getExpectedEventClass()
  {
    return GetEsPiListEvent.class;
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
    return IProcessInstanceMetaInfo.UID;
  }

  protected Collection retrieveEntityKeys(IDataFilter filter) throws Exception
  {
    log("retrieveEntityKeys");
    log("filter = " + filter.getFilterExpr());
    
 		return ActionHelper.getManager().findEsPiKeys(filter);
  }
  
  protected Collection retrieveEntityList(IDataFilter filter) throws Exception
  {
	  log("retrieveEntityList");
	  
  	return ActionHelper.getManager().findEsPiEntityList(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return AbstractEntity.convertEntitiesToMap(
        (ProcessInstanceMetaInfo[])entityList.toArray(new ProcessInstanceMetaInfo[entityList.size()]),
        EsPiEntityFieldID.getEntityFieldID(),
        null);
  }
  
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
//    GetEsPiListEvent getEvent = (GetEsPiListEvent) event;
    return constructEventResponse(getNextList((GetEntityListEvent) event));
  }
  
  /**
   * For testing.
   */
  private void log(String message)
  {
	  Logger.log("[GetEsPiListAcion] " + message);
  }
}
