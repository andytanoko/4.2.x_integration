/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTNetworkSettingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTNetworkSettingManager extends IGTManager
{
  public IGTNetworkSettingEntity getNetworkSetting() throws GTClientException;
}