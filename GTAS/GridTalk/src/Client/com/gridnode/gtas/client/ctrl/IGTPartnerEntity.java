/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partner.IPartner;

public interface IGTPartnerEntity extends IGTEntity
{
  //States
  public static final Short STATE_DISABLED = new Short(IPartner.STATE_DISABLED);
  public static final Short STATE_ENABLED  = new Short(IPartner.STATE_ENABLED);
  public static final Short STATE_DELETED  = new Short(IPartner.STATE_DELETED);

  //Fields
  public static final Number UID              = IPartner.UID;
  public static final Number PARTNER_ID       = IPartner.PARTNER_ID;
  public static final Number NAME             = IPartner.NAME;
  public static final Number DESCRIPTION      = IPartner.DESCRIPTION;
  public static final Number PARTNER_TYPE     = IPartner.PARTNER_TYPE;
  public static final Number PARTNER_GROUP    = IPartner.PARTNER_GROUP;
  public static final Number CREATE_TIME      = IPartner.CREATE_TIME;
  public static final Number CREATE_BY        = IPartner.CREATE_BY;
  public static final Number STATE            = IPartner.STATE;

  //Virtual fields
  public static final Number BE               = new Integer(-10);
  public static final Number CHANNEL          = new Integer(-20);
}
