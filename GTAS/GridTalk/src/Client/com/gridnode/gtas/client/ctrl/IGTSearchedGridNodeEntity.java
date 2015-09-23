/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchedGridNodeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.activation.ISearchedGridNode;

public interface IGTSearchedGridNodeEntity extends IGTEntity
{
  //Constants for STATE
  public static final Short STATE_ME          = new Short(ISearchedGridNode.STATE_ME);
  public static final Short STATE_ACTIVE      = new Short(ISearchedGridNode.STATE_ACTIVE);
  public static final Short STATE_INACTIVE    = new Short(ISearchedGridNode.STATE_INACTIVE);
  public static final Short STATE_PENDING     = new Short(ISearchedGridNode.STATE_PENDING);

  // Field Ids
  public static final Number GRIDNODE_ID    = ISearchedGridNode.GRIDNODE_ID;
  public static final Number GRIDNODE_NAME  = ISearchedGridNode.GRIDNODE_NAME;
  public static final Number STATE          = ISearchedGridNode.STATE;
}