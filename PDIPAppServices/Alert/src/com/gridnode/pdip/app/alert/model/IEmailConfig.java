/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEmailConfig.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.alert.model;

public interface IEmailConfig
{
  /**
   * Name of EmailConfig entity.
   */
  public static final String ENTITY_NAME = "EmailConfig";

  public static final Number UID = new Integer(0);

  public static final Number SMTP_SERVER_HOST = new Integer(1);
  public static final Number SMTP_SERVER_PORT = new Integer(2);
  public static final Number AUTH_USER = new Integer(3);
  public static final Number AUTH_PASSWORD = new Integer(4);
  public static final Number RETRY_INTERVAL = new Integer(5);
  public static final Number MAX_RETRIES = new Integer(6);
  public static final Number SAVE_FAILED_EMAILS = new Integer(7);
  
}
