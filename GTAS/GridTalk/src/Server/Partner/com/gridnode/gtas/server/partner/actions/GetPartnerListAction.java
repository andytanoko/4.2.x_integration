/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
* Jun 08 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.partner.actions;

import java.util.Collection;

import com.gridnode.gtas.events.partner.GetPartnerListEvent;
import com.gridnode.gtas.model.partner.PartnerEntityFieldID;
import com.gridnode.gtas.server.partner.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GetEntityListAction;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.model.IPartner;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a list of Partner.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public class GetPartnerListAction
  extends    GetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4353122302611308935L;
	private static int _listID = 0;

  public IEventResponse perform(IEvent event)
    throws EventException
  {
    return perform((GetPartnerListEvent)event);
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
    if (filter == null)
    {
      filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        IPartner.STATE,
        filter.getNotEqualOperator(),
        new Short(IPartner.STATE_DELETED),
        false);
    }
    else
    {
      filter.addSingleFilter(
        filter.getAndConnector(),
        IPartner.STATE,
        filter.getNotEqualOperator(),
        new Short(IPartner.STATE_DELETED),
        false);
    }
    return getManager().findPartner(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return Partner.convertEntitiesToMap(
             (Partner[])entityList.toArray(new Partner[entityList.size()]),
             PartnerEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Logs the event error in perform().
   *
   * @param ex The Exception that occurs.
   */
  protected void logEventError(Exception ex)
  {
    Logger.debug("[GetPartnerListAction.perform] Event Error ", ex);
  }

  /**
   * Logs the event start in perform().
   */
  protected void logEventStart()
  {
    Logger.debug("[GetPartnerListAction.perform] Start ");
  }

  /**
   * Logs the event ends in perform().
   */
  protected void logEventEnd()
  {
    Logger.debug("[GetPartnerListAction.perform] End ");
  }

  protected String getListIDPrefix()
  {
    return "PartnerListCursor.";
  }

  protected String getNewListID()
  {
    return String.valueOf(_listID++);
  }

}