/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTCommInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-10-07     Andrew Hill         Partner_Cat
 * 2002-12-04     Andrew Hill         Refactor to new CommInfo model
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.ICommInfo;

public interface IGTCommInfoEntity extends IGTProfileEntity
{
  // Types for PROTOCOL_TYPE field
  public static final String PROTOCOL_TYPE_JMS  = ICommInfo.JMS;
  public static final String PROTOCOL_TYPE_HTTP = ICommInfo.HTTP;
  public static final String PROTOCOL_TYPE_SOAP = ICommInfo.SOAP;
  public static final String PROTOCOL_TYPE_UPC  = ICommInfo.UPC;

  // Entity fields
  public static final Number UID = ICommInfo.UID;
  public static final Number NAME = ICommInfo.NAME;
  public static final Number DESCRIPTION = ICommInfo.DESCRIPTION;
  public static final Number IS_DEFAULT_TPT = ICommInfo.IS_DEFAULT_TPT;
  public static final Number PROTOCOL_TYPE = ICommInfo.PROTOCOL_TYPE;
  public static final Number TPT_IMPL_VERSION = ICommInfo.TPT_IMPL_VERSION;
  public static final Number REF_ID = ICommInfo.REF_ID;
  public static final Number IS_PARTNER = ICommInfo.IS_PARTNER;
  public static final Number IS_DISABLED = ICommInfo.IS_DISABLE;
  public static final Number PARTNER_CAT = ICommInfo.PARTNER_CAT;
  public static final Number URL = ICommInfo.URL;
}