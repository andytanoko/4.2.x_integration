/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTBizCertMappingEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partnerprocess.IBizCertMapping;

//import com.gridnode.gtas.client.GTClientException;

public interface IGTBizCertMappingEntity extends IGTEntity
{
  //Fields
  public static final Number UID          = IBizCertMapping.UID;
  public static final Number CAN_DELETE   = IBizCertMapping.CAN_DELETE;
  public static final Number PARTNER_ID   = IBizCertMapping.PARTNER_ID;
  public static final Number PARTNER_CERT = IBizCertMapping.PARTNER_CERT;
  public static final Number OWN_CERT     = IBizCertMapping.OWN_CERT;

}
