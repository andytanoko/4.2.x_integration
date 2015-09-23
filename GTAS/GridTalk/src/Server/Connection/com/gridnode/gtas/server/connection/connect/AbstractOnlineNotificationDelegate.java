/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractOnlineNotificationDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2002    Neo Sok Lay         Created
 * Dec 04 2003    Neo Sok Lay         Change in construction of JMSCommInfo.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;

/**
 * Abstract class for Online notification delegates.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public abstract class AbstractOnlineNotificationDelegate extends AbstractConnectionDelegate
{
  protected CommInfoFormatter _partnerCI;
  protected ChannelInfo       _partnerChannel;

  protected AbstractOnlineNotificationDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  protected void updatePartnerOnline() throws Throwable
  {
    // update partner comminfo, connection status to online
    DelegateHelper.updateCommInfo(_partnerChannel.getTptCommInfo());

    updatePartnerOnline(_partnerCI.getGridNodeID(), _partnerCI.getConnectedGmNodeID());
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
    _eventID = DelegateHelper.getProperties().getOnlineNotificationEventId();
    _ackEventID = DelegateHelper.getProperties().getOnlineNotificationAckEventId();
  }

  /**
   * Retrieve the master channel for the partner GridTalk.
   *
   * @return The ChannelInfo for the partner Master channel.
   */
  protected ChannelInfo retrievePartnerChannel() throws Throwable
  {
    ChannelInfo sendChannel = DelegateHelper.retrieveMasterChannel(_partnerCI.getGridNodeID());

    if (sendChannel != null)
    {
      CommInfo commInfo = sendChannel.getTptCommInfo();
      //String gmHost = new JMSCommInfo(
      //                  _ctx.getConnectedGmChannel().getTptCommInfo().getURL()).getHost();
      JMSCommInfo jmsInfo = new JMSCommInfo();
      jmsInfo.setURL(_ctx.getConnectedGmChannel().getTptCommInfo().getURL());
      String gmHost = jmsInfo.getHost();
      updatePartnerCommInfo(commInfo, gmHost);
      sendChannel.setTptCommInfo(commInfo);
    }

    return sendChannel;
  }


  private void updatePartnerCommInfo(CommInfo commInfo, String gmHost)
    throws Exception
  {
    //JMSCommInfo jmsInfo = new JMSCommInfo(commInfo.getURL());
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setURL(commInfo.getURL());

    jmsInfo.setHost(_partnerCI.getIP());
    if (_partnerCI.getConnectionLevel() == 0)
    {
      jmsInfo.setHost(_partnerCI.getIP());
      jmsInfo.setDestination(_partnerCI.getLocalRouterTopic());
      jmsInfo.setUserName(_partnerCI.getLocalRouterAuth()[0]);
      jmsInfo.setPassword(_partnerCI.getLocalRouterAuth()[1]);
    }
    else
    {
      jmsInfo.setHost(gmHost);
      jmsInfo.setDestination(_partnerCI.getNetworkTopic());
      jmsInfo.setUserName(_ctx.getConnectionSetupResult().getNetworkRouterUser());
      jmsInfo.setPassword(_ctx.getConnectionSetupResult().getNetworkRouterPassword());
    }
    commInfo.setURL(jmsInfo.toURL());
    commInfo.setTptImplVersion(_partnerCI.getCommInfoVersion());
  }

}