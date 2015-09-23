/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReceiveMessageHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 14 2002    Goh Kan Mun             Created
 * OCT 23 2002    Jagadeesh               Modified the interface to include header.
 *                                        And note that eventid is part of header[0].
 */

package com.gridnode.pdip.app.channel.helpers;

import java.io.File;
import java.util.Hashtable;

/**
 * This interface defines the method that the BL needs to implement if they are interested
 * in the Receive of a send message.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IReceiveMessageHandler
{
  /**
   * Method to handle a feedback call.
   */
  public void handlerMessage(
    String[] header,
    String[] dataReceived,
    File[] filesReceived,
    Hashtable additionalHeader) throws Exception;
}