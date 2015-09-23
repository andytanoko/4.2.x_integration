/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingConstants
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 04-OCT-2002    Jagadeesh           Created.
 * 17 Jan 2003    Goh Kan Mun         Modified - Add fields for packaging path.
 * 30 Jan 2003    Goh Kan Mun         Modified - Add constant for split event config name
 *                                               and split event and ack.
 *
 * 17 Aug 2003   Jagadeesh            Modified - Add constant for Additional Header.
 */


package com.gridnode.pdip.base.packaging.helper;

public interface IPackagingConstants
{

  public static final String CHANNEL_NAME = "ChannelName";

	public static final String ADDITIONAL_HEADER = "AdditionalHeader";

  public static final String UNDEFINDED_CHANNEL =  "Undefined Channel";

  public static final String PACKAGING_PATH = "packaging.path.temp";

  public static final String TPTIMPL_02000_CONFIG_NAME = "tptimpl.02000.split.event";

  public static final String TPTIMPL_02000_SPLIT_EVENT_AND_ACK = "tptimpl.02000.splitEventAndAck";

}