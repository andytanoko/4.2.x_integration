/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerTypeListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Collection;

import com.gridnode.gtas.events.partner.GetPartnerTypeListEvent;
import com.gridnode.gtas.model.partner.PartnerTypeEntityFieldID;
import com.gridnode.gtas.server.partner.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a list of Partner Type.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetPartnerTypeListAction
  extends    GetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2273884972272016996L;
	private static int _listID = 0;

  public IEventResponse perform(IEvent event)
    throws EventException
  {
    return perform((GetPartnerTypeListEvent)event);
  }

   private IPartnerManagerObj getManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IPartnerManagerHome.class.getName(),
               IPartnerManagerHome.class,
               new Object[0]);
  }

  // ****************** GetEntityListAction methods ***************************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return getManager().findPartnerType(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return PartnerType.convertEntitiesToMap(
             (PartnerType[])entityList.toArray(new PartnerType[entityList.size()]),
             PartnerTypeEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Logs the event error in perform().
   *
   * @param ex The Exception that occurs.
   */
  protected void logEventError(Exception ex)
  {
    Logger.debug("[GetPartnerTypeListAction.perform] Event Error ", ex);
  }

  /**
   * Logs the event start in perform().
   */
  protected void logEventStart()
  {
    Logger.debug("[GetPartnerTypeListAction.perform] Start ");
  }

  /**
   * Logs the event ends in perform().
   */
  protected void logEventEnd()
  {
    Logger.debug("[GetPartnerTypeListAction.perform] End ");
  }

  protected String getListIDPrefix()
  {
    return "PartnerTypeListCursor.";
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

}