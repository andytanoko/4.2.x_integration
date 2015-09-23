/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTActionEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IAction;
 
public interface IGTActionEntity extends IGTEntity
{
  // Fields
  public static final Number UID          = IAction.UID;
  public static final Number NAME         = IAction.NAME;
  public static final Number DESCRIPTION  = IAction.DESCRIPTION;
  public static final Number MSG_ID       = IAction.MSG_UID;

}
