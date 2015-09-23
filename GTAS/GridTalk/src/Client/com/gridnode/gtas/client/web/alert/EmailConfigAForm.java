/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailConfigAForm.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class EmailConfigAForm extends GTActionFormBase
{
  
  private String _smtpServerHost;
  private String _smtpServerPort;
  private String _authUser;
  private String _authPassword;
  private String _retryInterval;
  private String _maxRetries;
  private String _saveFailedEmails;
    
    
  
  // ******************** Getters for attributes ***************************  

  public String getAuthPassword()
  {
    return _authPassword;
  }

  public String getAuthUser()
  {
    return _authUser;
  }

  public String getMaxRetries()
  {
    return _maxRetries;
  }

  public Integer getMaxRetriesAsInteger()
  {
    return StaticUtils.integerValue(_maxRetries);
  }

  public String getRetryInterval()
  {
    return _retryInterval;
  }

  public Long getRetryIntervalAsLong()
  {
    return StaticUtils.longValue(_retryInterval);
  }

  public String getSaveFailedEmails()
  {
    return _saveFailedEmails;
  }

  public Boolean isSaveFailedEmailsAsBoolean()
  {
    return StaticUtils.booleanValue(_saveFailedEmails);
  }

  public String getSmtpServerHost()
  {
    return _smtpServerHost;
  }

  public String getSmtpServerPort()
  {
    return _smtpServerPort;
  }

  public Integer getSmtpServerPortAsInteger()
  {
    return StaticUtils.integerValue(_smtpServerPort);
  }
      
  // ******************** Setters for attributes ***************************
  
  public void setAuthPassword(String authPassword)
  {
    _authPassword = authPassword;
  }

  public void setAuthUser(String authUser)
  {
    _authUser = authUser;
  }

  public void setMaxRetries(String maxRetries)
  {
    _maxRetries = maxRetries;
  }

  public void setRetryInterval(String retryInterval)
  {
    _retryInterval = retryInterval;
  }

  public void setSaveFailedEmails(String saveFailedEmails)
  {
    _saveFailedEmails = saveFailedEmails;
  }

  public void setSmtpServerHost(String smtpServerHost)
  {
    _smtpServerHost = smtpServerHost;
  }

  public void setSmtpServerPort(String smtpServerPort)
  {
    _smtpServerPort = smtpServerPort;
  }

}
