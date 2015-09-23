/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAs2PackagingInfoExtensionEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-11-21     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.channel.IAS2PackagingInfoExtension;

public interface IGTAs2PackagingInfoExtensionEntity extends IGTEntity
{
  // fields
  //public static final Number UID            = IAS2PackagingInfoExtension.UID;
  public static final Number IS_ACK_REQ     = IAS2PackagingInfoExtension.IS_ACK_REQ;
  public static final Number IS_ACK_SIGNED  = IAS2PackagingInfoExtension.IS_ACK_SIGNED;
  public static final Number IS_NRR_REQ     = IAS2PackagingInfoExtension.IS_NRR_REQ;
  public static final Number IS_ACK_SYN     = IAS2PackagingInfoExtension.IS_ACK_SYN;
  public static final Number RETURN_URL     = IAS2PackagingInfoExtension.RETURN_URL;
}