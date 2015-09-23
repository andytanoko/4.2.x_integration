/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.bizreg.IRegistryConnectInfo;

public interface IGTRegistryConnectInfoEntity extends IGTEntity
{
  //Fields
  public static final Number UID              = IRegistryConnectInfo.UID;
  public static final Number NAME             = IRegistryConnectInfo.NAME;
  public static final Number QUERY_URL        = IRegistryConnectInfo.QUERY_URL;
  public static final Number PUBLISH_URL      = IRegistryConnectInfo.PUBLISH_URL;
  public static final Number PUBLISH_USER     = IRegistryConnectInfo.PUBLISH_USER;
  public static final Number PUBLISH_PASSWORD = IRegistryConnectInfo.PUBLISH_PASSWORD;
}
