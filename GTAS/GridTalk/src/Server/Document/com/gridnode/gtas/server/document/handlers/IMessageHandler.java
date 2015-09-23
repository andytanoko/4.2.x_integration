/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageHandler.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Oct 29 2003      Guo Jianyu              Created
 */
package com.gridnode.gtas.server.document.handlers;

import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import com.gridnode.gtas.server.document.model.GridDocument;

/**
 * This is an abstract message handler at GTAS layer. Its main functionality
 * includes preprocessing documents before they are sent, and postprocessing
 * documents after they are received.
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since GT 2.3
 */
public interface IMessageHandler extends IReceiveMessageHandler
{
  /**
   * This method should be extended by specific message handlers to preprocess
   * documents before they are sent.
   */
  public String[] preSend(GridDocument gDoc, ChannelInfo channelInfo);

}