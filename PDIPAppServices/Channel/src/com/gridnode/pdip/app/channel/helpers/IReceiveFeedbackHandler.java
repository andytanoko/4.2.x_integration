/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReceiveFeedbackHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 14 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.app.channel.helpers;

/**
 * This interface defines the method that the BL needs to implement if they are interested
 * in the feedback of a send message.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IReceiveFeedbackHandler
{
  /**
   * Method to handle a feedback call.
   */
  public void handlerFeedback(String[] header, boolean success, String message) throws Throwable;
}