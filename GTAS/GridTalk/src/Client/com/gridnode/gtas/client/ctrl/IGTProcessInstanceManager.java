/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcessInstanceManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTProcessInstanceManager extends IGTManager
{
  public void delete(Collection instUids, Boolean deleteGridDoc)
    throws GTClientException;

  public void abort(Collection instUids, String reason)
    throws GTClientException;
}