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
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.enterprise.ISearchedBusinessEntity;

public interface IGTSearchedBusinessEntityEntity extends IGTEntity
{
  //States
  public static final Integer STATE_NEW_BE      = new Integer(ISearchedBusinessEntity.STATE_NEW_BE);
  public static final Integer STATE_MY_BE       = new Integer(ISearchedBusinessEntity.STATE_MY_BE);
  public static final Integer STATE_PARTNER_BE  = new Integer(ISearchedBusinessEntity.STATE_PARTNER_BE);

  //Fields
  //public static final Number UID                = ISearchedBusinessEntity.UID;
  public static final Number UUID               = ISearchedBusinessEntity.UUID;
  public static final Number ENTERPRISE_ID      = ISearchedBusinessEntity.ENTERPRISE_ID;
  public static final Number ID                 = ISearchedBusinessEntity.ID;
  public static final Number DESCRIPTION        = ISearchedBusinessEntity.DESCRIPTION;
  public static final Number BE_STATE           = ISearchedBusinessEntity.STATE;
  public static final Number CHANNELS           = ISearchedBusinessEntity.CHANNELS;
  public static final Number WHITE_PAGE         = ISearchedBusinessEntity.WHITE_PAGE;
}
