/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCommInfoManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTCommInfoManager extends IGTManager
{
  public IGTCommInfoEntity getCommInfoByUID(long uid)
    throws GTClientException;

  public Collection getAllOfRefIdAndProtocolType(String refId, String protocolType)
    throws GTClientException;
}