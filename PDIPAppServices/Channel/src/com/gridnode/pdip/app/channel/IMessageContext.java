/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMessageContext.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * AUG 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel;

public interface IMessageContext
{
  public static final String INBOUND_MESSAGE = "INBOUND_MESSAGE";
  public static final String OUTBOUND_MESSAGE = "OUTBOUND_MESSAGE";
  public static final String FEEDBACK_MESSAGE = "FEEDBACK_MESSAGE";
  public static final String CHANNEL_INFO = "CHANNEL_INFO";
}