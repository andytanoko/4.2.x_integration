/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelSelector.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 * July 30 2004    Jagadeesh		 Modified: Fix bug GNDB00025178,To ensure that the channel
 * 					 retrieved belongs to its own and not partner.
 * Sept 02 2004	   Jagadeesh		 Modified : Bug Fix GNDB00025265,to retry
 * 					 to get Channel,by removing Channel Name
 * 					 from common headers.
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.*;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

import java.util.Map;

public final class MessageHandlerFactory
{
  private static final String CLASS_NAME = "MessageHandlerFactory";

  public static IMessageSender getMessageSender(MessageContext msgContext)
    throws Exception
  {
    IMessageSender messageSender = null;

    Message message =
      (Message) msgContext.getAttribute(IMessageContext.OUTBOUND_MESSAGE);
    ChannelInfo info =
      (ChannelInfo) msgContext.getAttribute(IMessageContext.CHANNEL_INFO);

    Map header = message.getCommonHeaders();
    String eventId = (String) header.get(ICommonHeaders.MSG_EVENT_ID);

    boolean toRelay = isRelay(eventId);
    if (toRelay)
    {
      info = getRelayChannelInfo();
    }

    if (toRelay)
    {
      messageSender = new BackwardCompatibleRelayMessageSender();
    }
    else
    {
      if (isBackwardCompatibleChannel(info))
      {
        messageSender = new BackwardCompatibleMessageSender();
      }
      else
      {
        messageSender = new DefaultMessageSender();
      }
    }
    /*
    if (isBackwardCompatibleChannel(info))
    {
    if (toRelay)
    {
    messageSender = new BackwardCompatibleRelayMessageSender();
    }
    else
    {
    messageSender = new BackwardCompatibleMessageSender();
    }
    }
    else
    {
    messageSender = new DefaultMessageSender();
    }
    */
    ChannelLogger.debugLog(
      CLASS_NAME,
      "getMessageSender()",
      "[Return Instance of" + messageSender.getClass().getName() + "]");

    msgContext.setAttribute(IMessageContext.CHANNEL_INFO, info);
    messageSender.init(msgContext);
    return messageSender;
  }

  public static IMessageReceiver getMessageReceiver(MessageContext msgContext)
    throws Exception
  {
    IMessageReceiver messageReceiver = null;
    ChannelInfo info = null;

    Message message =
      (Message) msgContext.getAttribute(IMessageContext.INBOUND_MESSAGE);

    Map header = message.getCommonHeaders();

    String eventId = (String) header.get(ICommonHeaders.MSG_EVENT_ID);
    //String myRefId = (String) header.get(ICommonHeaders.RECIPENT_BE_GNID);
    String senderRefId = (String) header.get(ICommonHeaders.SENDER_BE_GNID);
    String channelName = (String) header.get(ICommonHeaders.COMM_CHANNEL);
    String currentSenderId = (String) header.get(ICommonHeaders.MSG_PROCESS_ID);

    if ((channelName != null))
      //&& (myRefId != null)) //commented on 31st when testing from http-gtas send.
    {
      //We retrieve ChannelInfo, based on ChannelName and isPartner flag, which is false.
      //Since the sender or partner specifies which of our (recipient) available Channel's is used
      //while sending, and isPartner flag is false, since at recipient the channel is his own and
      //hence the partner flag is set to false.
      //30-07-2004
      info = ChannelServiceDelegate.getChannelInfoByNameAndPartnerFlag(channelName,false);
      //getChannelInfo(channelName, myRefId);
      if (info == null)
      {
        ChannelLogger.infoLog(CLASS_NAME,"getMessageReceiver()",
        "[Retry To Create Channel-Handle Message as TP]");
        //Remove Channel Name header, and retry.
        //To enable 2.3.4 to receive messages.
        header.remove(ICommonHeaders.COMM_CHANNEL);
        message.setCommonHeaders(header);
        info = ChannelServiceDelegate.getChannelInfo(message);
        if (info == null)
        {
          ChannelLogger.warnLog(
                    CLASS_NAME,
                    "getMessageReceiver()",
                    "[Message cannot be handled as Recipeint did not configured the Channel]["
                      + channelName
                      + "]");
          return null;
        }
      }
      messageReceiver = new DefaultMessageReceiver();
    }
    else if (senderRefId != null)
    {
      if (isRelay(eventId))
      {
        info = getRelayChannelInfo();
        messageReceiver = new BackwardCompatibleRelayMessageReceiver();
      }
      else
      {
        //Since filePart Events can be send by other GT(acting as Relay) - For future upgrade.
        if (isSenderRelay(eventId, currentSenderId, senderRefId))
        {
          info = getChannelInfo(currentSenderId);
          messageReceiver = new BackwardCompatibleRelayMessageReceiver();
        }
        else
        {
          info = getChannelInfo(senderRefId);
          messageReceiver = new BackwardCompatibleMessageReceiver();
        }
      }
    }
    else
    {
      info = ChannelServiceDelegate.getChannelInfo(message);
      if (info != null)
        messageReceiver = new DefaultMessageReceiver();
      else
      {
        ChannelLogger.warnLog(
          CLASS_NAME,
          "getMessageReceiver()",
          "[Unable to construct a Channel. For Message]");
        return null;
      }
      /*@todo construct channel info based on profile settings in common headers*/

    }
    ChannelLogger.debugLog(
      CLASS_NAME,
      "getMessageReceiver()",
      "[Return Instance of" + messageReceiver.getClass().getName() + "]");
    msgContext.setAttribute(IMessageContext.CHANNEL_INFO, info);
    messageReceiver.init(msgContext);
    return messageReceiver;
  }

  private static boolean isBackwardCompatibleChannel(ChannelInfo info)
  {
    if (info.getTptCommInfo().getTptImplVersion().startsWith("02"))
      return true;
    return false;
  }

  private static boolean isRelay(String eventId)
  {
    return GNCompatibleEventRegistry.getInstance().isRelay(eventId);
  }

  private static ChannelInfo getRelayChannelInfo() throws Exception
  {
    return ChannelServiceDelegate.getRelayChannelInfo();
  }

  /*
  private static ChannelInfo getChannelInfo(String channelName, String refId)
    throws Exception
  {
    return ChannelServiceDelegate.getChannelInfoByNameAndRefId(
      channelName,
      refId);
  }*/

  private static ChannelInfo getChannelInfo(String refId) throws Exception
  {
    return ChannelServiceDelegate.getChannelInfoByRefId(refId);
  }

  private static boolean isSenderRelay(
    String eventId,
    String currentSenderId,
    String senderNodeId)
  {
    if (!GNCompatibleEventRegistry.getInstance().isRelayEvent(eventId))
      return false;
    if (currentSenderId == null
      || currentSenderId.equals("0")
      || currentSenderId.equals(""))
      return false;
    else
      return (!currentSenderId.equals(senderNodeId));
  }

}
