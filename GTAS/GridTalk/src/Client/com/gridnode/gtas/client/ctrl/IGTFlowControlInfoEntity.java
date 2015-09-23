/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFlowControlInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-12-22     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.IFlowControlInfo;

public interface IGTFlowControlInfoEntity extends IGTEntity
{
  public static final Number IS_ZIP           = IFlowControlInfo.IS_ZIP;
  public static final Number ZIP_THRESHOLD    = IFlowControlInfo.ZIP_THRESHOLD;
  public static final Number IS_SPLIT         = IFlowControlInfo.IS_SPLIT;
  public static final Number SPLIT_SIZE       = IFlowControlInfo.SPLIT_SIZE;
  public static final Number SPLIT_THRESHOLD  = IFlowControlInfo.SPLIT_THRESHOLD;
}