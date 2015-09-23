/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCountryCodeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.locale.ICountryCode;

public interface IGTCountryCodeEntity extends IGTEntity
{
  public static final Number NAME           = ICountryCode.NAME;
  public static final Number NUMERICAL_CODE = ICountryCode.NUMERICAL_CODE;
  public static final Number ALPHA_2_CODE   = ICountryCode.ALPHA_2_CODE;
  public static final Number ALPHA_3_CODE   = ICountryCode.ALPHA_3_CODE;
}