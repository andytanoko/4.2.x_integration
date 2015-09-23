/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 16 2002    Jagadeesh               Created.
 * OCT 24 2002    Jagadeesh               Added: All service methods to throw ChannelException.
 * Dec 02 2002    Goh Kan Mun             Modified - Add in header for connect and
 *                                                   connectAndListen methods.
 */

package com.gridnode.pdip.app.channel.handler;

import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.framework.exceptions.SystemException;

import java.io.File;

public interface IChannelHandler
{

  public static final int JMS_CHANNEL_HANDLER = 0;
  public static final int HTTP_CHANNEL_HANDLER = 1;

  public void connect(CommInfo info, String[] header)
    throws ChannelException, SystemException;

  public void connectAndListen(CommInfo info, String[] header)
    throws ChannelException, SystemException;

  public void send(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws ChannelException, SystemException;

  public void disconnect(CommInfo info)
    throws ChannelException, SystemException;

  public void ping(CommInfo info) throws ChannelException, SystemException;

  public IReceiveMessageHandler getReceiveMessageHandler(String eventId)
    throws Exception;

  public IReceiveFeedbackHandler getReceiveFeedbackHandler(String eventId)
    throws Exception;

  /*
    public void registerReceiveMessageHandler(String eventId, IReceiveMessageHandler handler)
           throws Exception;
    public void deregisterReceiveMessageHandler(String eventId) throws Exception;
  
    public  IReceiveMessageHandler getReceiveMessageHandler(String eventId) throws Exception;
  
    public void registerReceiveFeedbackHandler(String eventId, IReceiveFeedbackHandler handler)
           throws Exception;
  
    public void deregisterReceiveFeedbackHandler(String eventId) throws Exception;
  
    public  IReceiveFeedbackHandler getReceiveFeedbackHandler(String eventId) throws Exception;
  */

}