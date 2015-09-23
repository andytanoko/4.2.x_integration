/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCompanyProfileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.gridnode.ICompanyProfile;

public interface IGTCompanyProfileEntity extends IGTEntity
{
  public static final Number UID        = ICompanyProfile.UID;
  public static final Number COY_NAME   = ICompanyProfile.COY_NAME;
  public static final Number EMAIL      = ICompanyProfile.EMAIL;
  public static final Number ALT_EMAIL  = ICompanyProfile.ALT_EMAIL;
  public static final Number TEL        = ICompanyProfile.TEL;
  public static final Number ALT_TEL    = ICompanyProfile.ALT_TEL;
  public static final Number FAX        = ICompanyProfile.FAX;
  public static final Number ADDRESS    = ICompanyProfile.ADDRESS;
  public static final Number CITY       = ICompanyProfile.CITY;
  public static final Number STATE      = ICompanyProfile.STATE;
  public static final Number POSTCODE   = ICompanyProfile.ZIP_CODE;
  public static final Number COUNTRY    = ICompanyProfile.COUNTRY;
  public static final Number LANGUAGE   = ICompanyProfile.LANGUAGE;
  public static final Number IS_PARTNER = ICompanyProfile.IS_PARTNER;
}