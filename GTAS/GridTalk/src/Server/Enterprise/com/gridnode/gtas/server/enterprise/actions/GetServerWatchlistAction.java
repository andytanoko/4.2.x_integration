/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetServerWatchlistAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.gridnode.gtas.events.enterprise.GetServerWatchlistEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.model.enterprise.IServerWatchlist;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;


/**
 * This Action class processes a GetServerWatchlistEvent to obtain
 * the current connection status of the GridTalk and all active partners.
 * <p>The event response will return, if event is successful, an array of the
 * following:<p>
 * <pre>
 * GridTalk Connection Status - Short
 * Array of Partner Infos and Connection Statuses (null if no active partner)
 *   Array of the following
 *   - Partner Name (String)
 *   - Partner ID   (String)
 *   - Partner UID  (Long)
 *   - Connection Status (Short)
 * Connected GridMaster Info (null if not connected)
 *   Array of the following
 *   - GridMaster Name
 *   - GridMaster Node ID
 *   - GridMaster Node UID
 * </pre>
 * <p>The indices to access the above information are specified in the
 * {@link IServerWatchlist} interface.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetServerWatchlistAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8626526756668994587L;

	public static final String ACTION_NAME = "GetServerWatchlistAction";

  private static final Hashtable _mePartnerStatusMap = new Hashtable();
  static
  {
    _mePartnerStatusMap.put(IServerWatchlist.STATUS_CONNECTING, IServerWatchlist.STATUS_DETERMINING);
    _mePartnerStatusMap.put(IServerWatchlist.STATUS_DISCONNECTING, IServerWatchlist.STATUS_DETERMINING);
    _mePartnerStatusMap.put(IServerWatchlist.STATUS_RECONNECTING, IServerWatchlist.STATUS_DETERMINING);
    _mePartnerStatusMap.put(IServerWatchlist.STATUS_OFFLINE, IServerWatchlist.STATUS_OFFLINE);
  }

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetServerWatchlistEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetServerWatchlistEvent getEvent = (GetServerWatchlistEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {

                      };

    return constructEventResponse(
             IErrorCode.SERVER_WATCHLIST_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetServerWatchlistEvent getEvent = (GetServerWatchlistEvent) event;

    Object[] watchlist = new Object[3];

    // if connected, get connected gm node
    watchlist[IServerWatchlist.MAIN_INDEX_GRIDMASTER] = getConnectedGm();

    // get my status
    Short myStatus = IServerWatchlist.STATUS_OFFLINE;
    if (watchlist[IServerWatchlist.MAIN_INDEX_GRIDMASTER] != null)
      myStatus = determineMyStatus();

    watchlist[IServerWatchlist.MAIN_INDEX_STATUS] = myStatus;

    // get partners
    watchlist[IServerWatchlist.MAIN_INDEX_PARTNER] = getPartnerList(myStatus);

    return constructEventResponse(watchlist);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************** Own methods ***********************************

  private Short determineMyStatus()
  {
    ConnectionStatus connStatus = null;
    try
    {
      GridNode gn = ServiceLookupHelper.getGridNodeManager().findMyGridNode();
      connStatus = ActionHelper.getConnectionStatus(gn.getID());
    }
    catch (Throwable ex)
    {
      Logger.warn("[GetServerWatchlistAction.getMyConnStatus] Error ", ex);
    }

    if (connStatus == null)
      return IServerWatchlist.STATUS_OFFLINE;

    Short status = IServerWatchlist.STATUS_OFFLINE;
    switch (connStatus.getStatusFlag())
    {
      case ConnectionStatus.STATUS_CONNECTING:
           status = IServerWatchlist.STATUS_CONNECTING;
           break;
      case ConnectionStatus.STATUS_DISCONNECTING:
           status = IServerWatchlist.STATUS_DISCONNECTING;
           break;
      case ConnectionStatus.STATUS_RECONNECTING:
           status = IServerWatchlist.STATUS_RECONNECTING;
           break;
      case ConnectionStatus.STATUS_OFFLINE:
           status = IServerWatchlist.STATUS_OFFLINE;
           break;
      case ConnectionStatus.STATUS_ONLINE:
           status = IServerWatchlist.STATUS_ONLINE;
           break;
    }
    return status;
  }

  private Object[] getPartnerList(Short myStatus)
  {
    Object[] partnerBes = ActionHelper.getPartnerBusinessEntities().toArray();

    if (partnerBes.length == 0)
      return null;

    HashMap connStatusTable = new HashMap();
    ArrayList partnerList = new ArrayList();
    for (int i=0; i<partnerBes.length; i++)
    {
      BusinessEntity be = (BusinessEntity)partnerBes[i];
      Object[] partners = ActionHelper.getPartners(
                              ActionHelper.getPartnersForBusinessEntity(
                              (Long)be.getKey())).toArray();

      Short status = (Short)connStatusTable.get(be.getEnterpriseId());
      if (status == null)
      {
        status = determinePartnerStatus(be.getEnterpriseId(), myStatus);
        connStatusTable.put(be.getEnterpriseId(), status);
      }

      for (int j=0; j<partners.length; j++)
      {
        Object[] list = new Object[4];
        Partner p = (Partner)partners[j];
        list[IServerWatchlist.PARTNER_INDEX_ID]     = p.getPartnerID();
        list[IServerWatchlist.PARTNER_INDEX_NAME]   = p.getName();
        list[IServerWatchlist.PARTNER_INDEX_UID]    = p.getKey();
        list[IServerWatchlist.PARTNER_INDEX_STATUS] = status;
        partnerList.add(list);
      }
    }

    if (partnerList.size() == 0)
      return null;

    return partnerList.toArray();
  }

  /**
   * Determine the connection status of a partner base on the enterpriseId it
   * belongs to.
   *
   * @param enterpriseId GridNode ID - if the partner is a GridTalk partner,
   * or <b>null</b> or empty if not.
   */
  private Short determinePartnerStatus(String enterpriseId, Short myStatus)
  {
    if (enterpriseId == null || enterpriseId.trim().length() == 0)
      return IServerWatchlist.STATUS_UNKNOWN;

    ConnectionStatus connStatus = ActionHelper.getConnectionStatus(enterpriseId);
    if (connStatus == null)
      return IServerWatchlist.STATUS_UNKNOWN;

    Short status = (Short)_mePartnerStatusMap.get(myStatus);

    if (status == null) // to determine base on partner node connection status
    {
      switch (connStatus.getStatusFlag())
      {
        case ConnectionStatus.STATUS_EXPIRED:
             status = IServerWatchlist.STATUS_EXPIRED;
             break;
        case ConnectionStatus.STATUS_OFFLINE:
             status = IServerWatchlist.STATUS_OFFLINE;
             break;
        case ConnectionStatus.STATUS_ONLINE:
             status = IServerWatchlist.STATUS_ONLINE;
             break;
        default:
             status = IServerWatchlist.STATUS_UNKNOWN;
             break;
      }
    }

    return status;
  }

 private Object[] getConnectedGm()
  {
    Object[] gm = null;
    try
    {
      IPostOfficeObj postOffice = ServiceLookupHelper.getPostOffice();
      if (postOffice.isGridMasterPostOfficeOpened())
      {
        GridNode gmNode = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(
                            postOffice.getOpenedGridMasterPostOfficeID());

        gm = new Object[3];
        gm[IServerWatchlist.GRIDMASTER_INDEX_NAME]   = gmNode.getName();
        gm[IServerWatchlist.GRIDMASTER_INDEX_NODEID] = gmNode.getID();
        gm[IServerWatchlist.GRIDMASTER_INDEX_UID]    = gmNode.getKey();
      }
    }
    catch (Throwable t)
    {
      Logger.warn("[GetServerWatchlistAction.getConnectedGm] Error ", t);
    }

    return gm;
  }

}