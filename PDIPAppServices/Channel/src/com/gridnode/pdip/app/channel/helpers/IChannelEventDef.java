/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelEventDef.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * OCT 31 2002     Jagadeesh                Created
 */

package com.gridnode.pdip.app.channel.helpers;

/**
 * This interface defines the GridEvents used by Applications to
 * handle the message accordingly.
 */
public interface IChannelEventDef
{

  public static final int _SETUP_AUTO_CONFIG = 60;
  public static final int _SETUP_AUTO_CONFIG_ACK = 61;
  /**
   * The Auto Config Request itself
   */
  public static final int _REQUEST_AUTO_CONFIG = 62;
  public static final int _REQUEST_AUTO_CONFIG_ACK = 63;

  /**
   * Msg to acknowledge an Auto Config Request
   */
  public static final int _FINAL_ACK_AUTO_CONFIG = 64;

  public static final int _SEND_TO_GTAS = 10;

}