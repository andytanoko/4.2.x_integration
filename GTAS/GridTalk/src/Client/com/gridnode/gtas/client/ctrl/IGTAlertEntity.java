/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAlertEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IAlert;

public interface IGTAlertEntity extends IGTEntity
{
  // Fields 
  public static final Number UID          = IAlert.UID;
  public static final Number NAME         = IAlert.NAME;
  public static final Number DESCRIPTION  = IAlert.DESCRIPTION;
  public static final Number TYPE         = IAlert.TYPE;
  public static final Number ACTIONS      = IAlert.BIND_ACTIONS_UIDS;
}
