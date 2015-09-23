/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRoleEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-07     Andrew Hill         Created
 * 2005-03-14     Andrew Hill         Added constant for "admin" role name
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.acl.IRole;

public interface IGTRoleEntity extends IGTEntity
{
  /**
   * The name (ROLE) of the preconfigured role that grants all access rights
   * to administrators. (20050314AH)
   */
  public static final String ADMIN = "Administrator";
  
  public static final Number UID          = IRole.UID;
  public static final Number ROLE         = IRole.ROLE;
  public static final Number DESCRIPTION  = IRole.DESCRIPTION;
  public static final Number CAN_DELETE   = IRole.CAN_DELETE;
}