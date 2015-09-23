/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageReceiver.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel;

import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.messaging.Message;

public interface IMessageReceiver
{
  public void init(MessageContext context);

  public void receive() throws ChannelException, SystemException;

  public void invokeListener(Message message) throws Exception;

}