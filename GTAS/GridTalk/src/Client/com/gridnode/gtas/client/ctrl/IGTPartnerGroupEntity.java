/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerGroupEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partner.IPartnerGroup;

public interface IGTPartnerGroupEntity extends IGTEntity
{
  public static final Number UID          = IPartnerGroup.UID;
  public static final Number PARTNER_TYPE = IPartnerGroup.PARTNER_TYPE;
  public static final Number DESCRIPTION  = IPartnerGroup.DESCRIPTION;
  public static final Number NAME         = IPartnerGroup.NAME;
}