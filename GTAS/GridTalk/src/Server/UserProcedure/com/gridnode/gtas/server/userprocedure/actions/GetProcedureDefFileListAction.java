/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcedureDefFileListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 */



package com.gridnode.gtas.server.userprocedure.actions;

import java.util.Collection;

import com.gridnode.gtas.events.userprocedure.GetProcedureDefFileListEvent;
import com.gridnode.gtas.model.userprocedure.ProcedureDefFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class GetProcedureDefFileListAction extends GetEntityListAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3685846245351108314L;
	private static final String CLASS_NAME = "GetProcedureDefFileListAction";
  private static int _listID = 0;

  public IEventResponse perform(IEvent event) throws EventException
  {
    return perform((GetProcedureDefFileListEvent)event);
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
   Logger.debug("["+CLASS_NAME+"]"+ "perform Start" );
  }

  protected void logEventError(Exception ex)
  {
    Logger.warn("["+CLASS_NAME+"]"+ "Event Error ",ex);
  }

  protected Collection retrieveEntityList(IDataFilter filter) throws java.lang.Exception
  {
     return getManager().getProcedureDefinitionFile(filter);
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

  protected void logEventEnd()
  {
    Logger.debug("["+CLASS_NAME+"]"+" perform End " );
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ProcedureDefFile.convertEntitiesToMap(
             (ProcedureDefFile[])entityList.toArray(
               new ProcedureDefFile[entityList.size()]),
               ProcedureDefFileEntityFieldID.getEntityFieldID(),
               null);
  }

  protected String getListIDPrefix()
  {
    return "ProcedureDefFileListCursor.";
  }
}


