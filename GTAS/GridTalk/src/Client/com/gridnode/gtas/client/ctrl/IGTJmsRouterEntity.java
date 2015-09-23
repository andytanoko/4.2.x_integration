/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTJmsRouterEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-31     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.connection.*;

public interface IGTJmsRouterEntity extends IGTEntity
{
  // Field ids
  public static final Number UID        = IJmsRouter.UID;
  public static final Number NAME       = IJmsRouter.NAME;
  public static final Number IP_ADDRESS = IJmsRouter.IP_ADDRESS;

}