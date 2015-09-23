/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTHouseKeepingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTHouseKeepingManager extends IGTManager
{
  public IGTHouseKeepingEntity getHouseKeeping() throws GTClientException;
  public void saveHouseKeeping(IGTHouseKeepingEntity houseKeeping) throws GTClientException;
    
}