/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGridNodeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.gridnode.IGridNode;

public interface IGTGridNodeEntity extends IGTEntity
{
  //Constants for STATE
  public static final Short STATE_ME        = new Short(IGridNode.STATE_ME);
  public static final Short STATE_DELETED   = new Short(IGridNode.STATE_DELETED);
  public static final Short STATE_ACTIVE    = new Short(IGridNode.STATE_ACTIVE);
  public static final Short STATE_INACTIVE  = new Short(IGridNode.STATE_INACTIVE);
  public static final Short STATE_GM        = new Short(IGridNode.STATE_GM);

  // Field ids
  public static final Number UID                = IGridNode.UID;
  public static final Number ID                 = IGridNode.ID;
  public static final Number NAME               = IGridNode.NAME;
  public static final Number CATEGORY           = IGridNode.CATEGORY;
  public static final Number STATE              = IGridNode.STATE;
  public static final Number ACTIVATION_REASON  = IGridNode.ACTIVATION_REASON;
  public static final Number DT_CREATED         = IGridNode.DT_CREATED;
  public static final Number DT_REQ_ACTIVATE    = IGridNode.DT_REQ_ACTIVATE;
  public static final Number DT_ACTIVATED       = IGridNode.DT_ACTIVATED;
  public static final Number DT_DEACTIVATED     = IGridNode.DT_DEACTIVATED;
  public static final Number COY_PROFILE_UID    = IGridNode.COY_PROFILE_UID;
}