/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTPartnerFunctionEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partnerfunction.IPartnerFunction;

public interface IGTPartnerFunctionEntity extends IGTEntity
{
  //TRIGGER_ON Values
  public static final Integer TRIGGER_ON_IMPORT = IPartnerFunction.TRIGGER_IMPORT;
  public static final Integer TRIGGER_ON_RECEIVE = IPartnerFunction.TRIGGER_RECEIVE;
  public static final Integer TRIGGER_ON_MANUAL_SEND = IPartnerFunction.TRIGGER_MANUAL_SEND;
  public static final Integer TRIGGER_ON_MANUAL_EXPORT = IPartnerFunction.TRIGGER_MANUAL_EXPORT;

  //Field ids
  public static final Number UID                  = IPartnerFunction.UID;
  public static final Number ID                   = IPartnerFunction.PARTNER_FUNCTION_ID;
  public static final Number DESCRIPTION          = IPartnerFunction.DESCRIPTION;
  public static final Number TRIGGER_ON           = IPartnerFunction.TRIGGER_ON;
  public static final Number WORKFLOW_ACTIVITIES  = IPartnerFunction.WORKFLOW_ACTIVITIES;
}