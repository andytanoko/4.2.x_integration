/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 13, 2006   i00107              Created
 * Mar 05, 2007		Alain Ah Ming				Added Error Codes
 */

package com.gridnode.util.alert;

import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import com.gridnode.util.StringUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.exceptions.ILogErrorCodes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.mail.EmailSender;

/**
 * @author i00107
 * This class provides the functions to trigger alerts. The manner the
 * alerts will be sent out depends on configuration on the type of the
 * alerts.
 */
public class AlertUtil
{
  public static final int DELIVERY_UNKNOWN = -1;
  public static final int DELIVERY_BY_EMAIL = 1;
  public static final int DELIVERY_BY_SMS = 2;
  public static final int DELIVERY_BY_LOG = 3;
  
  private Logger _logger = LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "AlertUtil");
  private EmailSender _emailSender;
  
  private static AlertUtil _self = null;
  /**
   * 
   */
  public AlertUtil()
  {
    _emailSender = new EmailSender();
    _emailSender.useDefaultSendProperties();
  }

  public static final synchronized AlertUtil getInstance()
  {
    if (_self == null)
    {
      _self = new AlertUtil();
    }
    
    return _self;
  }
  
  public void sendAlert(String alertKey, Object...substVals)
  {
    _logger.logEntry("sendAlert", new Object[] {alertKey});
    AlertConfig conf = getAlertConfig(alertKey);
    
    int deliveryMode = conf.getDeliveryMode(DELIVERY_UNKNOWN);
    switch (deliveryMode)
    {
      case DELIVERY_BY_EMAIL :
        sendByEmail(conf, substVals);
        break;
      case DELIVERY_BY_SMS :
        sendBySms(conf, substVals);
        break;
      case DELIVERY_BY_LOG :
        sendByLog(conf, substVals);
        break;
      case DELIVERY_UNKNOWN :
      default :
        _logger.logMessage("sendAlert", null, "Unknown delivery mode ["+deliveryMode+"] specified for alert: "+alertKey);
        break;
        
    }
    _logger.logExit("sendAlert", new Object[] {alertKey});

  }
  
  private AlertConfig getAlertConfig(String alertKey)
  {
    Properties props = ConfigurationStore.getInstance().getProperties(alertKey);
    return new AlertConfig(props);
  }
  
  private void sendByEmail(AlertConfig conf, Object...substVals)
  {
    String subject = conf.getSubject();
    String message = conf.getMessage();
    List<String> recipients = conf.getRecipients(); // the recipients referred to groups
 
    String mtdName = "sendByEmail";
    if (recipients == null || recipients.isEmpty())
    {
      _logger.logError(ILogErrorCodes.EMAIL_ALERT_MISSING_PARAMS, mtdName, null, "No recipients configured for the alert!", null);
      return;
    }
    
    if (StringUtil.isBlank(subject))
    {
      _logger.logError(ILogErrorCodes.EMAIL_ALERT_MISSING_PARAMS, mtdName, null, "No subject configured for the alert!", null);
      return;
    }
    
    if (StringUtil.isBlank(message))
    {
      _logger.logError(ILogErrorCodes.EMAIL_ALERT_MISSING_PARAMS, mtdName, null, "No message confiigured for the alert!", null);
    }
    
    String formattedSubject = MessageFormat.format(subject, substVals);
    String formattedMsg = MessageFormat.format(message, substVals);

    _emailSender.send(recipients, formattedSubject, formattedMsg);
  }
  
  private void sendByLog(AlertConfig conf, Object...substVals)
  {
    //TODO send alert by logging
  }
  
  private void sendBySms(AlertConfig conf, Object...substVals)
  {
    //TODO send alert by SMS
  }
}
