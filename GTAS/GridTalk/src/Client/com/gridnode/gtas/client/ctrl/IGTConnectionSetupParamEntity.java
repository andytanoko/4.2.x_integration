/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTConnectionSetupParamEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-31     Andrew Hill         Created
 * 2002-11-14     Andrew Hill         Added SECURITY_PASSWORD vfield
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.connection.*;

public interface IGTConnectionSetupParamEntity extends IGTEntity
{
  // Field ids
  public static final Number CURRENT_LOCATION           = IConnectionSetupParam.CURRENT_LOCATION;
  public static final Number SERVICING_ROUTER           = IConnectionSetupParam.SERVICING_ROUTER;
  public static final Number ORIGINAL_LOCATION          = IConnectionSetupParam.ORIGINAL_LOCATION;
  public static final Number ORIGINAL_SERVICING_ROUTER  = IConnectionSetupParam.ORIGINAL_SERVICING_ROUTER;

  // Virtual fields
  public static final Number SECURITY_PASSWORD      = new Integer(-10);
}