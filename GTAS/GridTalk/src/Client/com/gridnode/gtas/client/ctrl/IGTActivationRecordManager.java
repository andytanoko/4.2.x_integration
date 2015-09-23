/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTActivationRecordManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-12-26     Andrew Hill         abort(), deny(), approve()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTActivationRecordManager extends IGTManager
{
  //20021226AH - Convienience methods for doing multiple records at one time
  public void deny(long[] uids) throws GTClientException;
  public void abort(long[] uids) throws GTClientException;
}