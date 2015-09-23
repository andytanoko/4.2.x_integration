/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetArchiveAction.java
 *
 ****************************************************************************
 * Date           	Author              		Changes
 ****************************************************************************
 * Oct 3 2005		Sumedh Chalermkanjana       Created
 */
package com.gridnode.gtas.server.dbarchive.actions;

import java.util.Collection;

import com.gridnode.gtas.model.dbarchive.ArchiveEntityFieldID;
import com.gridnode.gtas.server.dbarchive.helpers.*;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.gtas.server.dbarchive.model.*;
import com.gridnode.gtas.events.dbarchive.*;


public class GetArchiveListAction extends AbstractGetEntityListAction2
{
  public static final String CURSOR_PREFIX = "ArchiveListCursor.";

  public static final String ACTION_NAME = "GetArchiveListAction";

  protected Class getExpectedEventClass()
  {
    return GetArchiveListEvent.class;
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
    return IArchiveMetaInfo.UID;
  }

  protected Collection retrieveEntityKeys(IDataFilter filter) throws Exception
  {
    log("retrieveEntityKeys");
       
 		return ArchiveMetaInfoEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
  }
  
  protected Collection retrieveEntityList(IDataFilter filter) throws Exception
  {
	  log("retrieveEntityList");
	  try{
  	return ArchiveMetaInfoEntityHandler.getInstance().getEntityByFilter(filter);
	  }catch (Throwable t){
	    throw new Exception(t);
	  }
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return AbstractEntity.convertEntitiesToMap(
        (ArchiveMetaInfo[])entityList.toArray(new ArchiveMetaInfo[entityList.size()]),
        ArchiveEntityFieldID.getEntityFieldID(),
        null);
  }
  
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    return constructEventResponse(getNextList((GetEntityListEvent) event));
  }
  
  /**
   * For testing.
   */
  private void log(String message)
  {
	  Logger.log("[GetArchiveListAcion] " + message);
  }
}
