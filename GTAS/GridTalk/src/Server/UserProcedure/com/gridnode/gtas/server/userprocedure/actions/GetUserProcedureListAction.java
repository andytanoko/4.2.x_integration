/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserProcedureListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 */
package com.gridnode.gtas.server.userprocedure.actions;

import java.util.Collection;

import com.gridnode.gtas.events.userprocedure.GetUserProcedureListEvent;
import com.gridnode.gtas.model.userprocedure.UserProcedureEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class GetUserProcedureListAction extends GetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4192467498137489333L;
	private static final String CLASS_NAME = "GetUserProcedureListAction";
  private static int _listID = 0;

  public IEventResponse perform(IEvent event) throws EventException
  {
    return perform((GetUserProcedureListEvent)event);
  }

  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }

  // ********************* Methods from GetEntityListAction ******************

  protected void logEventStart()
  {
    Logger.debug("["+CLASS_NAME+"] perform Start");
  }

  protected void logEventError(Exception ex)
  {
    Logger.warn("["+CLASS_NAME+ "] perform Event Error ",ex);
  }

  protected Collection retrieveEntityList(IDataFilter filter) throws java.lang.Exception
  {
    return getManager().getUserProcedure(filter);
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

  protected void logEventEnd()
  {
    Logger.debug("["+CLASS_NAME+"] perform End");
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return UserProcedure.convertEntitiesToMap(
             (UserProcedure[])entityList.toArray(new UserProcedure[entityList.size()]),
             UserProcedureEntityFieldID.getEntityFieldID(),
             null);
  }

  protected String getListIDPrefix()
  {
    return "UserProcedureListCursor.";
  }
}



