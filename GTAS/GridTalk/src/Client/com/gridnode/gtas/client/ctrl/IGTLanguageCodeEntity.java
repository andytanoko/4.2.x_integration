/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTLanguageCodeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.locale.ILanguageCode;

public interface IGTLanguageCodeEntity extends IGTEntity
{
  public static final Number NAME             = ILanguageCode.NAME;
  public static final Number ALPHA_2_CODE     = ILanguageCode.ALPHA_2_CODE;
  public static final Number B_APLPHA_3_CODE  = ILanguageCode.B_ALPHA_3_CODE;
  public static final Number T_ALPHA_3_CODE   = ILanguageCode.T_ALPHA_3_CODE;
}