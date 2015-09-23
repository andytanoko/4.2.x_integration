/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcessMappingEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partnerprocess.IProcessMapping;
 
public interface IGTProcessMappingEntity extends IGTEntity
{

  // Entity fields
  public static final Number UID                = IProcessMapping.UID;
  public static final Number CAN_DELETE         = IProcessMapping.CAN_DELETE;
  public static final Number PROCESS_DEF        = IProcessMapping.PROCESS_DEF;
  public static final Number IS_INITIATING_ROLE = IProcessMapping.IS_INITIATING_ROLE;
  public static final Number DOC_TYPE           = IProcessMapping.DOC_TYPE;
  public static final Number SEND_CHANNEL_UID   = IProcessMapping.SEND_CHANNEL_UID;
  public static final Number PARTNER_ID         = IProcessMapping.PARTNER_ID;
}