/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerGroupListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Collection;

import com.gridnode.gtas.events.partner.GetPartnerGroupListEvent;
import com.gridnode.gtas.model.partner.PartnerGroupEntityFieldID;
import com.gridnode.gtas.server.partner.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a list of Partner Group.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetPartnerGroupListAction
  extends    GetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8443515509275041055L;
	private static int _listID = 0;

  public IEventResponse perform(IEvent event)
    throws EventException
  {
    return perform((GetPartnerGroupListEvent)event);
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
    return getManager().findPartnerGroup(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return PartnerGroup.convertEntitiesToMap(
             (PartnerGroup[])entityList.toArray(new PartnerGroup[entityList.size()]),
             PartnerGroupEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Logs the event error in perform().
   *
   * @param ex The Exception that occurs.
   */
  protected void logEventError(Exception ex)
  {
    Logger.debug("[GetPartnerGroupListAction.perform] Event Error ", ex);
  }

  /**
   * Logs the event start in perform().
   */
  protected void logEventStart()
  {
    Logger.debug("[GetPartnerGroupListAction.perform] Start ");
  }

  /**
   * Logs the event ends in perform().
   */
  protected void logEventEnd()
  {
    Logger.debug("[GetPartnerGroupListAction.perform] End ");
  }

  protected String getListIDPrefix()
  {
    return "PartnerGroupListCursor.";
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

}