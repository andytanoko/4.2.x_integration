/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRNHeaderConstants.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 11 2002    Qingsong              Created
 */

package com.gridnode.pdip.base.transport.helpers;


public interface IRNHeaderConstants extends ITransportConstants
{
  /**
   * Gridnode RosettaNet
   */
  public static final String GN_MESSAGE_RESPONSE_KEY = "x-GN-RN-Response";

  /**
   * RosettaNet defined
   */
  public static final String CONTENT_TYPE_KEY = "Content-Type";
  public static final String TYPE_KEY = "type";

  public static final String RN_VERSION_KEY = "x-RN-Version";
  public static final String RN_RESPONSE_TYPE_KEY = "x-RN-Response-Type";
  public static final String RN_RESPONSE_TYPE_SYNC = "sync";
  public static final String RN_RESPONSE_TYPE_ASYNC = "async";

}