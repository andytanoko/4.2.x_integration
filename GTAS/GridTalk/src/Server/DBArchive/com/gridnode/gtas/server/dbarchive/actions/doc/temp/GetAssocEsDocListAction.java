/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAssocEsDocListAction.java
 *
 ****************************************************************************
 * Date           	Author              		Changes
 ****************************************************************************
 * Oct 10 2005		Sumedh Chalermkanjana       Created
 */
package com.gridnode.gtas.server.dbarchive.actions.doc.temp;

import java.util.Collection;

import com.gridnode.gtas.model.dbarchive.doc.EsDocEntityFieldID;
import com.gridnode.gtas.server.dbarchive.helpers.*;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction2;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.gtas.server.dbarchive.model.*;
import com.gridnode.gtas.events.dbarchive.doc.temp.*;

public class GetAssocEsDocListAction extends AbstractGetEntityListAction2
{
  public static final String CURSOR_PREFIX = "AssocEsDocListCursor.";

  public static final String ACTION_NAME = "GetAssocEsDocListAction";

  protected Class getExpectedEventClass()
  {
    return GetAssocEsDocListEvent.class;
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
    return IDocumentMetaInfo.UID;
  }

  protected Collection retrieveEntityKeys(IDataFilter filter) throws Exception
  {
    log("retrieveEntityKeys");
    log("filter = " + filter.getFilterExpr());
    
    //TEST
    Object[] orderFields = filter.getOrderFields();
    log("orderFields");
    for (int i = 0; i < orderFields.length; i++)
    {
    	log(i + ": " + orderFields[i]);
    }
    boolean[] sortOrders = filter.getSortOrders();
    log("sortOrders");
    for (int i = 0; i < sortOrders.length; i++)
    {
    	log(i + ": " + sortOrders[i]);
    }
    
 		return ActionHelper.getManager().findAssocEsDocKeys(filter);
  }
  
  protected Collection retrieveEntityList(IDataFilter filter) throws Exception
  {
	  log("retrieveEntityList");
	  
 		return ActionHelper.getManager().findEsDocEntityList(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return AbstractEntity.convertEntitiesToMap(
        (DocumentMetaInfo[])entityList.toArray(new DocumentMetaInfo[entityList.size()]),
        EsDocEntityFieldID.getEntityFieldID(),
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
	  Logger.log("[GetAssocEsDocListAcion] " + message);
  }
}
