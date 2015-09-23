/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTEmailConfigEntity.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.alert.IEmailConfig;

public interface IGTEmailConfigEntity extends IGTEntity
{
  public static final Number SMTP_SERVER_HOST   = IEmailConfig.SMTP_SERVER_HOST;
  public static final Number SMTP_SERVER_PORT   = IEmailConfig.SMTP_SERVER_PORT;
  public static final Number AUTH_USER          = IEmailConfig.AUTH_USER;
  public static final Number AUTH_PASSWORD      = IEmailConfig.AUTH_PASSWORD;
  public static final Number RETRY_INTERVAL     = IEmailConfig.RETRY_INTERVAL;
  public static final Number MAX_RETRIES        = IEmailConfig.MAX_RETRIES;
  public static final Number SAVE_FAILED_EMAILS = IEmailConfig.SAVE_FAILED_EMAILS;
}
