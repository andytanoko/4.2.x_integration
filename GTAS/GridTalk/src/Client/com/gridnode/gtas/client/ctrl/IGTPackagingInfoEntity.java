/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPackagingInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2003-11-21     Daniel D'Cotta      Added AS2 support & some constants
 * 2006-01-19			SC									Unsupport PackagingInfo.ZIP and PackagingInfo.ZIP_THRESHOLD
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.IPackagingInfo;

public interface IGTPackagingInfoEntity extends IGTProfileEntity
{
  // constants
  public static final String DEFAULT_ENVELOPE_TYPE  = IPackagingInfo.DEFAULT_ENVELOPE_TYPE; // 20031121 DDJ
  public static final String RNIF1_ENVELOPE_TYPE    = IPackagingInfo.RNIF1_ENVELOPE_TYPE;   // 20031121 DDJ
  public static final String RNIF2_ENVELOPE_TYPE    = IPackagingInfo.RNIF2_ENVELOPE_TYPE;   // 20031121 DDJ
  public static final String AS2_ENVELOPE_TYPE      = IPackagingInfo.AS2_ENVELOPE_TYPE;     // 20031121 DDJ

  public static final Short CATEGORY_OTHERS     = IPackagingInfo.CATEGORY_OTHERS;   // 20031121 DDJ
  public static final Short CATEGORY_GRIDTALK   = IPackagingInfo.CATEGORY_GRIDTALK; // 20031121 DDJ

  // fields
  public static final Number UID                = IPackagingInfo.UID;
  public static final Number NAME               = IPackagingInfo.NAME;
  public static final Number DESCRIPTION        = IPackagingInfo.DESCRIPTION;
  public static final Number ENVELOPE           = IPackagingInfo.ENVELOPE;
//  public static final Number ZIP                = IPackagingInfo.ZIP;
//  public static final Number ZIP_THRESHOLD      = IPackagingInfo.ZIPTHRESHOLD;
  public static final Number IS_PARTNER         = IPackagingInfo.IS_PARTNER;
  public static final Number IS_DISABLED        = IPackagingInfo.IS_DISABLE;
  public static final Number PARTNER_CAT        = IPackagingInfo.PARTNER_CAT;
  public static final Number REF_ID             = IPackagingInfo.REF_ID;
  public static final Number PKG_INFO_EXTENSION = IPackagingInfo.PKG_INFO_EXTENSION; // 20031121 DDJ
  
}