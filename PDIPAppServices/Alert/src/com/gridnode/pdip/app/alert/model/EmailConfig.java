/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailConfig.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.alert.model;


import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class EmailConfig extends AbstractEntity implements IEmailConfig
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5888960303881223187L;
	
	protected String _smtpServerHost;
  protected Integer _smtpServerPort;
  protected String _authUser;
  protected String _authPassword;
  protected Long _retryInterval;
  protected Integer _maxRetries;
  protected boolean _saveFailedEmails;
  

  public EmailConfig()
  {
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_smtpServerHost).append(':').append(_smtpServerPort).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  
  // ******************** Getters for attributes ***************************  

  public String getAuthPassword()
  {
    return _authPassword;
  }

  public String getAuthUser()
  {
    return _authUser;
  }

  public Integer getMaxRetries()
  {
    return _maxRetries;
  }

  public Long getRetryInterval()
  {
    return _retryInterval;
  }

  public boolean getSaveFailedEmails()
  {
    return _saveFailedEmails;
  }

  public String getSmtpServerHost()
  {
    return _smtpServerHost;
  }

  public Integer getSmtpServerPort()
  {
    return _smtpServerPort;
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

  public void setMaxRetries(Integer maxRetries)
  {
    _maxRetries = maxRetries;
  }

  public void setRetryInterval(Long retryInterval)
  {
    _retryInterval = retryInterval;
  }

  public void setSaveFailedEmails(boolean saveFailedEmails)
  {
    _saveFailedEmails = saveFailedEmails;
  }

  public void setSmtpServerHost(String smtpServerHost)
  {
    _smtpServerHost = smtpServerHost;
  }

  public void setSmtpServerPort(Integer smtpServerPort)
  {
    _smtpServerPort = smtpServerPort;
  }

}
