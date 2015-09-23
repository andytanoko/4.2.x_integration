/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTBusinessEntityEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2002-10-11     Andrew Hill         canSend()
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.client.GTClientException;

public interface IGTBusinessEntityEntity extends IGTProfileEntity
{
  public boolean canSend() throws GTClientException;

  // Fields
  public static final Number UID                = IBusinessEntity.UID;
  public static final Number ENTERPRISE_ID      = IBusinessEntity.ENTERPRISE_ID;
  public static final Number ID                 = IBusinessEntity.ID;
  public static final Number DESCRIPTION        = IBusinessEntity.DESCRIPTION;
  public static final Number IS_PARTNER         = IBusinessEntity.IS_PARTNER;
  public static final Number IS_PUBLISHABLE     = IBusinessEntity.IS_PUBLISHABLE;
  public static final Number IS_SYNC_TO_SERVER  = IBusinessEntity.IS_SYNC_TO_SERVER;
  public static final Number STATE              = IBusinessEntity.STATE;
  public static final Number CAN_DELETE         = IBusinessEntity.CAN_DELETE;
  public static final Number WHITE_PAGE         = IBusinessEntity.WHITE_PAGE;
  public static final Number PARTNER_CAT        = IBusinessEntity.PARTNER_CATEGORY;

  public static final Number DOMAIN_IDENTIFIERS = IBusinessEntity.DOMAIN_IDENTIFIERS; // 20040102 DDJ

  // Virtual fields
  public static final Number CHANNELS           = new Integer(-10);

  /**
   * States for STATE field
   */
  public static final Integer STATE_NORMAL    = new Integer(IBusinessEntity.STATE_NORMAL);
  public static final Integer STATE_DELETED   = new Integer(IBusinessEntity.STATE_DELETED);
  public static final Integer STATE_ACTIVE    = new Integer(IBusinessEntity.STATE_ACTIVE);
  public static final Integer STATE_INACTIVE  = new Integer(IBusinessEntity.STATE_INACTIVE);
  public static final Integer STATE_PENDING   = new Integer(IBusinessEntity.STATE_PENDING);
}