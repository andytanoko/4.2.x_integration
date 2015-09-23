/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAccessRightEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.acl.IAccessRight;

public interface IGTAccessRightEntity extends IGTEntity
{
  public static final Number UID          = IAccessRight.UID;
  public static final Number ROLE         = IAccessRight.ROLE;
  public static final Number FEATURE      = IAccessRight.FEATURE;
  public static final Number DESCRIPTION  = IAccessRight.DESCRIPTION;
  public static final Number ACTION       = IAccessRight.ACTION;
  public static final Number DATA_TYPE    = IAccessRight.DATA_TYPE;
  public static final Number CRITERIA     = IAccessRight.CRITERIA;
  public static final Number CAN_DELETE   = IAccessRight.CAN_DELETE;
}