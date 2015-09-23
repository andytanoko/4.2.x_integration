/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMailManager.java
 *
 * ****************************************************************************
 * Date           Author           Changes
 * ****************************************************************************
 * 06 Dec 2002    Srinath	      Creation
 */

package com.gridnode.pdip.framework.mail;


public interface IMailManager
{
  public static final String MAIL_CONFIG_NAME = "mail";

  public static final String SMTP_SERVER = "smtp.server.id";  //SMTP Server
  public static final String SMTP_USERNAME = "smtp.server.username";  //SMTP User
  public static final String SMTP_PASSWORD = "smtp.server.password";  //SMTP Password
}