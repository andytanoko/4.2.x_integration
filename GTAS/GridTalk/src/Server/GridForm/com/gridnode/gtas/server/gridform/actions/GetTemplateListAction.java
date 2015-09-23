/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTemplateListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jun 26 2002     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.events.gridform.GetTemplateListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.gridform.GFTemplateEntityFieldID;
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
 * This Action class handles the retrieving of a list of Template.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetTemplateListAction
  extends    GetEntityListAction
{
  private static int _listID = 0;

  public IEventResponse perform(IEvent event)
    throws EventException
  {
    return super.perform((GetTemplateListEvent)event);
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
//        ITemplate.STATE,
//        filter.getNotEqualOperator(),
//        new Short(ITemplate.STATE_DELETED),
//        false);
//    }
//    else
//    {
//      filter.addSingleFilter(
//        filter.getAndConnector(),
//        ITemplate.STATE,
//        filter.getNotEqualOperator(),
//        new Short(ITemplate.STATE_DELETED),
//        false);
//    }
    return getManager().findGFTemplates(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return GFTemplate.convertEntitiesToMap(
             (GFTemplate[])entityList.toArray(new GFTemplate[entityList.size()]),
             GFTemplateEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Logs the event error in perform().
   *
   * @param ex The Exception that occurs.
   */
  protected void logEventError(Exception ex)
  {
    Logger.debug("[GetTemplateListAction.perform] Event Error ", ex);
  }

  /**
   * Logs the event start in perform().
   */
  protected void logEventStart()
  {
    Logger.debug("[GetTemplateListAction.perform] Start ");
  }

  /**
   * Logs the event ends in perform().
   */
  protected void logEventEnd()
  {
    Logger.debug("[GetTemplateListAction.perform] End ");
  }

  protected String getListIDPrefix()
  {
    return "TemplateListCursor.";
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

}