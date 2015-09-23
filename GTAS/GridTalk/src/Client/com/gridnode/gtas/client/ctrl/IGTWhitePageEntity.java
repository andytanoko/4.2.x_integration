/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTWhitePageEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.bizreg.IWhitePage;

public interface IGTWhitePageEntity extends IGTEntity
{
  public static final Number UID                  = IWhitePage.UID;
  public static final Number BE_UID               = IWhitePage.BE_UID;
  public static final Number BUSINESS_DESC        = IWhitePage.BUSINESS_DESC;
  public static final Number DUNS                 = IWhitePage.DUNS;
  public static final Number G_SUPPLY_CHAIN_CODE  = IWhitePage.G_SUPPLY_CHAIN_CODE;
  public static final Number CONTACT_PERSON       = IWhitePage.CONTACT_PERSON;
  public static final Number EMAIL                = IWhitePage.EMAIL;
  public static final Number TEL                  = IWhitePage.TEL;
  public static final Number FAX                  = IWhitePage.FAX;
  public static final Number WEBSITE              = IWhitePage.WEBSITE;
  public static final Number ADDRESS              = IWhitePage.ADDRESS;
  public static final Number PO_BOX               = IWhitePage.PO_BOX;
  public static final Number CITY                 = IWhitePage.CITY;
  public static final Number STATE                = IWhitePage.STATE;
  public static final Number POSTCODE             = IWhitePage.ZIP_CODE;
  public static final Number COUNTRY              = IWhitePage.COUNTRY;
  public static final Number LANGUAGE             = IWhitePage.LANGUAGE;
}