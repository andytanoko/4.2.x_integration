/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTChannelInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTChannelInfoManager extends IGTManager
{
  public IGTChannelInfoEntity getChannelInfoByUID(long uid)
    throws GTClientException;

  public IGTFlowControlInfoEntity newFlowControlInfo() throws GTClientException;
}