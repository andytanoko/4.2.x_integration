/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEmailConfiguration.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 6, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.alert.helpers;

public interface IEmailConfiguration
{
  public static final String EMAIL_PATH          = "alert.path.email";
  public static final String EMAIL_CONFIG_FILE   = "email-config.xml";
  public static final String EMAIL_RETRY_PATH  = "alert.path.retry";
  public static final String EMAIL_FAILED_PATH = "alert.path.failed";
  
  public static final String ALERT_MESSAGEID_KEY   = "alert.messageId";
  public static final String ALERT_RETRYCOUNT_KEY   = "alert.retryCount";
}
