/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDefinitionListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jun 26 2002     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.events.gridform.GetDefinitionListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.gridform.GFDefinitionEntityFieldID;
import com.gridnode.gtas.server.gridform.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;

import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerHome;
import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerObj;
import com.gridnode.pdip.app.gridform.model.*;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 * This Action class handles the retrieving of a list of Definition.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetDefinitionListAction
  extends    GetEntityListAction
{
  private static int _listID = 0;

  public IEventResponse perform(IEvent event)
    throws EventException
  {
    return super.perform((GetDefinitionListEvent)event);
  }

   private IGFManagerObj getManager()
    throws ServiceLookupException
  {
    return (IGFManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IGFManagerHome.class.getName(),
               IGFManagerHome.class,
               new Object[0]);
  }

  // ****************** GetEntityListAction methods ***************************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
//    if (filter == null)
//    {
//      filter = new DataFilterImpl();
//      filter.addSingleFilter(
//        null,
//        IDefinition.STATE,
//        filter.getNotEqualOperator(),
//        new Short(IDefinition.STATE_DELETED),
//        false);
//    }
//    else
//    {
//      filter.addSingleFilter(
//        filter.getAndConnector(),
//        IDefinition.STATE,
//        filter.getNotEqualOperator(),
//        new Short(IDefinition.STATE_DELETED),
//        false);
//    }
    return getManager().findGFDefinitions(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return GFDefinition.convertEntitiesToMap(
             (GFDefinition[])entityList.toArray(new GFDefinition[entityList.size()]),
             GFDefinitionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Logs the event error in perform().
   *
   * @param ex The Exception that occurs.
   */
  protected void logEventError(Exception ex)
  {
    Logger.debug("[GetDefinitionListAction.perform] Event Error ", ex);
  }

  /**
   * Logs the event start in perform().
   */
  protected void logEventStart()
  {
    Logger.debug("[GetDefinitionListAction.perform] Start ");
  }

  /**
   * Logs the event ends in perform().
   */
  protected void logEventEnd()
  {
    Logger.debug("[GetDefinitionListAction.perform] End ");
  }

  protected String getListIDPrefix()
  {
    return "DefinitionListCursor.";
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

}