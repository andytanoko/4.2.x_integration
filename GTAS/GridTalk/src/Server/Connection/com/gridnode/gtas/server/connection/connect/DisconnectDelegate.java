/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DisconnectDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 * Nov 29 2002    Neo Sok Lay         Handle Disconnect due to connection lost
 *                                    - the Disconnect message will not be sent to
 *                                      the GridMaster.
 *                                    Add token-based execution.
 * Apr 28 2003    Neo Sok Lay         Add start/end of disconnect activity,
 *                                    lost connection event.
 * Apr 27 2004    Neo Sok Lay         Reset the GMChannel for normal disconnect
 *                                    Do not perform disconnect if status is 
 *                                    already offline.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.exceptions.DisconnectionException;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.io.File;

/**
 * This Delegate handles the Disconnection from the currently connected
 * GridMaster. On successful completion, the GridTalk will become offline.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class DisconnectDelegate extends AbstractConnectionDelegate
{
  private boolean _connectionLost;
  private ConnectionContext.ConnectionToken _myToken;

  public DisconnectDelegate(ConnectionContext ctx)
    throws Throwable
  {
    this(ctx, false);
  }

  public DisconnectDelegate(
    ConnectionContext ctx, boolean connectionLost)
    throws Throwable
  {
    super(ctx);

    _connectionLost = connectionLost;
  }

  protected void getEventIDs() throws java.lang.Throwable
  {
    _eventID = DelegateHelper.getProperties().getDisconnectEventId();
    _ackEventID = DelegateHelper.getProperties().getDisconnectAckEventId();
  }

  public void execute() throws java.lang.Throwable
  {
    Logger.debug("[DisconnectDelegate.execute] Enter");

    // don't need to grab token to perform if connection lost
    // the token owner is someone else.
    if (!_connectionLost)
      _myToken = ConnectionContext.seizeToken(this, ConnectionContext.DISCONNECT_TOKEN);

    try
    {
      //don't need to do anything if already disconnected
      if (isMyGtOnline())
      {
        updateMyGtDisconnecting();
        performDisconnect();
      }
      else
        Logger.debug("[DisconnectDelegate.execute] Already disconnected from GridMaster");
    }
    finally
    {
      if (_myToken != null)
        _myToken.release();
      Logger.debug("[DisconnectDelegate.execute] Exit");
    }
  }

  /**
   * Perform the Disconnect procedure:<p>
   * <pre>
   * 1. Stop the Keep alive timer
   * 2. Update all partners offline
   * 3. if procedure triggered due to connection lost, proceed to step 4.
   *    otherwise,
   * 3a.  Send disconnect request to the GridMaster
   * 3b.  Upon receipt of acknowledgement, proceed to step 4.
   * 4. Disconnect GridTalk from Jms Router listen to
   * 5. Update GridMaster offline
   * 6. Update this GridTalk offline
   */
  protected void performDisconnect() throws Throwable
  {
    stopKeepAliveTimer();

    String[] partnerNodeIDs = updatePartnersOffline();
    String gmNodeID         = _ctx.getConnectedGmChannel().getReferenceId();

    if (_connectionLost)
    {
      //log the lost connection event
      logLostConnection(gmNodeID);

      disconnectGridTalk();
    }
    else
    {
      disconnectFromGm(partnerNodeIDs);

      // no matter successful ack or not, will set to offline
      synchronized (_lock)
      {
        disconnectGridTalk();
      }

      //reset the GMChannel to prevent any more activity with GM
      _ctx.setConnectedGmChannel(null);
      
      if (!_success)        // if not successful ack, throw exception
        throw new DisconnectionException(_failureReason);
    }
  }

  /**
   * Invoked when feedback is received for the disconnect message.
   *
   * @param success Whether the disconnect message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    if (!success)
    {
      synchronized (_lock)
      {
        _resultsReturned = true;
        _success = false;
        _failureReason = message;

        _helper.wakeUp();
      }
    }
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws java.lang.Throwable
  {
    synchronized (_lock)
    {
      _resultsReturned = true;

      try
      {
        // collate the results
        Logger.debug("[DataPayLoad Recivied Status ="+dataPayload[0]+"]");
        Boolean status = Boolean.valueOf(dataPayload[0]);
        _success = status.booleanValue();
        if (!_success)
          _failureReason = "GridMaster fails to disconnect GridTalk";
      }
      catch (Throwable t)
      {
        _success = false;
        _failureReason = t.getMessage();
        throw t;
      }
      finally
      {
        _helper.wakeUp();
      }
    }
  }

  /**
   * Disconnect the GridTalk from the Jms router and update the connection
   * statuses (of Gm and this GridTalk) accordingly.
   */
  private void disconnectGridTalk() throws Throwable
  {
    disconnectFromRouter();

    updateGmOffline(_ctx.getConnectedGmChannel().getReferenceId(),
      !_connectionLost);
    updateMyGtOffline();
  }

  /**
   * Update all active partners to offline.
   *
   * @return the GridNodeIDs of the partners.
   */
  private String[] updatePartnersOffline()
  {
    String[] nodeIDs = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
        new Short(GridNode.STATE_ACTIVE), false);

      Object[] nodes = ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(
                         filter).toArray();
      nodeIDs = new String[nodes.length];
      for (int i=0; i<nodes.length; i++)
      {
        GridNode node = (GridNode)nodes[i];

        updatePartnerOffline(node.getID());
        //clear comminfo??
        nodeIDs[i] = node.getID();
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[DisconnectDelegate.updatePartnersOffline] Error ", ex);
    }
    return nodeIDs;

  }

  /**
   * Disconnect from GridMaster
   *
   * @param partnerNodeIDs GridNodeIDs of the partners to notify offline.
   */
  private void disconnectFromGm(String[] partnerNodeIDs) throws Throwable
  {
    String gmNodeID = _ctx.getConnectedGmChannel().getReferenceId();
    //log the start of disconnect activity
    logStartDisconnect(gmNodeID);

    updateGmDisconnecting(gmNodeID);

    try
    {
      String[] dataPayload = partnerNodeIDs;
      if (partnerNodeIDs != null && partnerNodeIDs.length==0)
        dataPayload = null;

      registerAndSend(dataPayload, null, _ctx.getConnectedGmChannel());

      // *** wait for result(connectTimeout)
      if (!_resultsReturned)
        _helper.sleep(DelegateHelper.getProperties().getConnectTimeout());
    }
    catch (Throwable t)
    {
      _failureReason = "Error sending Disconnect request to GridMaster!";
      _success = false;
      //throw t;
    }

    //log the end of disconnect activity
    logEndDisconnect(gmNodeID, _success);
  }

  /**
   * Disconnect from the jms routers that this GridTalk has connected to.
   */
  private void disconnectFromRouter()
  {
    try
    {
      //retrieve myCommInfo
      CommInfo myCommInfo =  DelegateHelper.retrieveMasterChannel(
                               _ctx.getMyNodeID()).getTptCommInfo();
      boolean level0 = _ctx.getNetworkSetting().getConnectionLevel().shortValue()
                       == NetworkSetting.LEVEL_NO_FIREWALL;

      if (level0)
      {
        CommInfo gmCommInfo = _ctx.getConnectedGmChannel().getTptCommInfo();

        if (!_ctx.isLocalConnectionLost()) //don't do if connection already closed
          DelegateHelper.disconnect(myCommInfo);

        if (!_connectionLost || _ctx.isNetworkConnectionLost())
          DelegateHelper.disconnect(gmCommInfo);
      }
      else
      {
        if (!_connectionLost) //don't do if connection already closed
          DelegateHelper.disconnect(myCommInfo);
      }
    }
    catch (Throwable t)
    {
      Logger.debug("[DisconnectDelegate.disconnectFromRouter] Error: "+t.getMessage());
    }
  }

}