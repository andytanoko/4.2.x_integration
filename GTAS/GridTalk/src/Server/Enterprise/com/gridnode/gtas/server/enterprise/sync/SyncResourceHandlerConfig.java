/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncResourceHandlerConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.sync;

import java.util.List;

/**
 * Configuration for a SyncResourceHandler.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public final class SyncResourceHandlerConfig
{
  private String  _handlerClass;
  private String  _resType;
  private List    _sendOrRouteMsgID;
  private List    _sendOnlyMsgID;
  private List    _routeMsgID;
  private List    _receiveOnlyMsgID;

  private boolean _isTest;
  private String  _serDir;

  /**
   * Construct a SyncResourceHandlerConfig object.
   *
   * @param handlerClass Class name of the SyncResourceHandler.
   * @param resType The resource type that the SyncResourceHandler can handle.
   * @param sendOrRouteMsgID
   * @param sendOnlyMsgID
   * @param routeMsgID
   * @param receiveOnlyMsgID
   *
   * @since 2.0 I4
   */
  public SyncResourceHandlerConfig(
    String handlerClass, String resType, List sendOrRouteMsgID,
    List sendOnlyMsgID, List routeMsgID, List receiveOnlyMsgID)
  {
    _handlerClass     = handlerClass;
    _resType          = resType;
    _sendOrRouteMsgID = sendOrRouteMsgID;
    _sendOnlyMsgID    = sendOnlyMsgID;
    _routeMsgID       = routeMsgID;
    _receiveOnlyMsgID = receiveOnlyMsgID;
  }

  public String getHandlerClass()
  {
    return _handlerClass;
  }

  public String getResourceType()
  {
    return _resType;
  }

  public List getSendOrRouteMsgID()
  {
    return _sendOrRouteMsgID;
  }

  public List getSendOnlyMsgID()
  {
    return _sendOnlyMsgID;
  }

  public List getRouteMsgID()
  {
    return _routeMsgID;
  }

  public List getReceiveOnlyMsgID()
  {
    return _receiveOnlyMsgID;
  }

  void setTest(boolean isTest)
  {
    _isTest = isTest;
  }

  public boolean isTest()
  {
    return _isTest;
  }

  void setSerializeDir(String serDir)
  {
    _serDir = serDir;
  }

  public String getSerializeDir()
  {
    return _serDir;
  }

}