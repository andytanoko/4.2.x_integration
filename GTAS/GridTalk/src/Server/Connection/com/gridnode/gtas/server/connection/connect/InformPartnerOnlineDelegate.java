/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InformPartnerOnlineDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.io.File;

/**
 * This Delegate class handles the Inform Partner Online step of the
 * Connection process.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class InformPartnerOnlineDelegate extends AbstractConnectionDelegate
{
  private String _partnerNodeID;

  /**
   * Use for informing all partners that this GridTalk is online.
   *
   * @param ctx Current ConnectionContext.
   */
  public InformPartnerOnlineDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  /**
   * Use for informing a partner that this GridTalk is online.
   *
   * @param ctx Current ConnectionContext.
   * @param partnerNodeID GridNode ID of the partner to inform.
   */
  public InformPartnerOnlineDelegate(ConnectionContext ctx, String partnerNodeID)
    throws Throwable
  {
    this(ctx);
    _partnerNodeID = partnerNodeID;
  }

  public void execute() throws Throwable
  {
    Logger.debug("[InformPartnerOnlineDelegate.execute] Enter");

    if (_partnerNodeID == null)
      retrievePartnerStatus();
    else
      retrievePartnerStatus(_partnerNodeID);
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    CommInfoFormatter formatter = null;

    if (dataPayload != null)
    {
      for (int i=0; i<dataPayload.length; i++)
      {
        formatter = CommInfoFormatter.toCommInfo(dataPayload[i]);

        if (formatter.isOnline())
        {
          sendOnlineNotification(formatter);
        }
        else
        {
          updatePartnerOffline(formatter.getGridNodeID());
        }
      }
    }
  }

  /**
   * Invoked when feedback is received for the inform-partner-online message.
   *
   * @param success Whether the inform-partner-online message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.log("Received feedback for Inform Partner Online: success="+success +
      ", "+message);
  }

  /**
   * Retrieve the Partner CommInfo and Online status: Send Request Partner CommInfo
   * message.
   */
  private void retrievePartnerStatus() throws Throwable
  {
    String[] dataPayload = getPartnerNodeIDs();
    if (dataPayload != null)
    {
      registerAndSend(dataPayload, null, _ctx.getConnectedGmChannel());
    }
  }

  /**
   * Retrieve the Partner CommInfo and Online status of a particular GridNode.
   *
   * @param nodeID GridNodeID of the GridNode to retrieve comminfo for.
   */
  private void retrievePartnerStatus(String nodeID) throws Throwable
  {
    String[] dataPayload = new String[] {nodeID};

    registerAndSend(dataPayload, null, _ctx.getConnectedGmChannel());
  }

  /**
   * Retrieve the GridNodeIDs of all active partner GridTalks.
   *
   * @return Array of GridNodeIDs.
   */
  private String[] getPartnerNodeIDs()
  {
    String[] nodeIDs = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
        new Short(GridNode.STATE_ACTIVE), false);

      Object[] nodes = ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(
                         filter).toArray();
      if (nodes.length > 0)
      {
        nodeIDs = new String[nodes.length];

        for (int i=0; i<nodeIDs.length; i++)
        {
          GridNode partner = (GridNode)nodes[i];
          nodeIDs[i] = partner.getID();
        }
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[RetrievePartnerStatusDelegate.getPartnerNodeIDs] Error ", ex);
    }

    return nodeIDs;
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
    _eventID = _helper.getProperties().getRetrievePartnerStatusEventId();
    _ackEventID = _helper.getProperties().getRetrievePartnerStatusAckEventId();
  }

  /**
   * Send Online Notification message to a partner GridNode.
   *
   * @param partnerCIFormatter The Communication information of the partner GridNode.
   */
  private void sendOnlineNotification(CommInfoFormatter partnerCIFormatter)
  {
    try
    {
      new SendOnlineNotificationDelegate(_ctx, partnerCIFormatter).execute();
    }
    catch (Throwable t)
    {
      Logger.error(ILogErrorCodes.GT_CHANNEL_SEND_ONLINE_NOTIFICATIONS_ERROR,
                   "[InformatPartnerOnlineDelegate.sendOnlineNotification] Error ", t);
    }
  }
}