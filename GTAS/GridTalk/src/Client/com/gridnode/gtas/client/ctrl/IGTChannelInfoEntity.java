/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTChannelInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-21     Andrew Hill         Created
 * 2002-09-19     Andrew Hill         Add extra fields for security and packaging
 * 2002-10-07     Andrew Hill         Add PARTNER_CAT
 * 2003-12-22     Daniel D'Cotta      Added embeded FlowControlInfo entity
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.IChannelInfo;

public interface IGTChannelInfoEntity extends IGTProfileEntity
{
  // Types for TPT_PROTOCOL_TYPE field
  public static final String TPT_PROTOCOL_TYPE_JMS  = "JMS";
  public static final String TPT_PROTOCOL_TYPE_HTTP = "HTTP";
  public static final String TPT_PROTOCOL_TYPE_SOAP = "HTTP-SOAP";
  public static final String TPT_PROTOCOL_TYPE_UPC  = "UPC";

  // Entity fields
  public static final Number UID = IChannelInfo.UID;
  public static final Number NAME = IChannelInfo.NAME;
  public static final Number DESCRIPTION = IChannelInfo.DESCRIPTION;
  public static final Number REF_ID = IChannelInfo.REF_ID;
  public static final Number TPT_PROTOCOL_TYPE = IChannelInfo.TPT_PROTOCOL_TYPE;
  public static final Number TPT_COMM_INFO_UID = IChannelInfo.TPT_COMM_INFO;
  public static final Number IS_PARTNER = IChannelInfo.IS_PARTNER;
  public static final Number PACKAGING_PROFILE = IChannelInfo.PACKAGING_PROFILE;
  public static final Number SECURITY_PROFILE = IChannelInfo.SECURITY_PROFILE;
  public static final Number PARTNER_CAT = IChannelInfo.PARTNER_CAT;
  public static final Number IS_DISABLE = IChannelInfo.IS_DISABLE;

/*
  // The following can be factored out when metainfo is refactored in I4 :-)
  public String getTptProtocolTypeLabelKey() throws GTClientException;
  public String getTptProtocolTypeLabelKey(String tptProtocolType) throws GTClientException;
  public Collection getAllowedTptProtocolTypes() throws GTClientException;
*/

  public static final Number FLOW_CONTROL_INFO = IChannelInfo.FLOWCONTROL_PROFILE;
}