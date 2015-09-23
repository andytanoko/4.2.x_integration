/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchedChannelInfoEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-10     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.enterprise.ISearchedChannelInfo;

public interface IGTSearchedChannelInfoEntity extends IGTEntity
{
  // Entity fields
  public static final Number NAME = ISearchedChannelInfo.NAME;
  public static final Number DESCRIPTION = ISearchedChannelInfo.DESCRIPTION;
  public static final Number REF_ID = ISearchedChannelInfo.REF_ID;
  public static final Number TPT_PROTOCOL_TYPE = ISearchedChannelInfo.TPT_PROTOCOL_TYPE;
  public static final Number TPT_COMM_INFO = ISearchedChannelInfo.TPT_COMM_INFO;
  public static final Number PACKAGING_PROFILE = ISearchedChannelInfo.PACKAGING_PROFILE;
  public static final Number SECURITY_PROFILE = ISearchedChannelInfo.SECURITY_PROFILE;

}