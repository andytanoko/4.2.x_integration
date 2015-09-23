/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGridNodeActivationEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.activation.IGridNodeActivation;

public interface IGTGridNodeActivationEntity extends IGTEntity
{
  public static final Number ACTIVATE_REASON    = IGridNodeActivation.ACTIVATE_REASON;
  public static final Number REQUESTOR_BE_LIST  = IGridNodeActivation.REQUESTOR_BE_LIST;
  public static final Number APPROVER_BE_LIST   = IGridNodeActivation.APPROVER_BE_LIST;

}