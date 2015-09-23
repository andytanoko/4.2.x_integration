/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTConnectionSetupResultEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-31     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.connection.*;

public interface IGTConnectionSetupResultEntity extends IGTEntity
{
  //Constants for STATUS field
  public static final Short STATUS_NOT_DONE = new Short(IConnectionSetupResult.STATUS_NOT_DONE);
  public static final Short STATUS_SUCCESS  = new Short(IConnectionSetupResult.STATUS_SUCCESS);
  public static final Short STATUS_FAILURE  = new Short(IConnectionSetupResult.STATUS_FAILURE);

  // Field ids
  public static final Number STATUS                 = IConnectionSetupResult.STATUS;
  public static final Number SETUP_PARAMETERS       = IConnectionSetupResult.SETUP_PARAMETERS;
  public static final Number FAILURE_REASON         = IConnectionSetupResult.FAILURE_REASON;
  public static final Number AVAILABLE_GRIDMASTERS  = IConnectionSetupResult.AVAILABLE_GRIDMASTERS;
  public static final Number AVAILABLE_ROUTERS      = IConnectionSetupResult.AVAILABLE_ROUTERS;
}