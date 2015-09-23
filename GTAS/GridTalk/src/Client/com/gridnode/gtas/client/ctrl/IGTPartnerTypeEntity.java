/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerTypeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-11     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partner.IPartnerType;

public interface IGTPartnerTypeEntity extends IGTEntity
{
  public static final Number UID          = IPartnerType.UID;
  public static final Number PARTNER_TYPE = IPartnerType.NAME;
  public static final Number DESCRIPTION  = IPartnerType.DESCRIPTION;
  public static final Number CAN_DELETE   = IPartnerType.CAN_DELETE;
}