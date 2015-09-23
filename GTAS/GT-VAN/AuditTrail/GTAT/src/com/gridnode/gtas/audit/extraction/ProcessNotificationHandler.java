/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessNotificationHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Oct 26, 2007    Tam Wei Xiang       To handle the failure of sending the TXMR event
 *                                     to ISAT component in case the clustering fail over
 *                                     is in processing.
 */
package com.gridnode.gtas.audit.extraction;

import java.util.Properties;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.extraction.collator.AbstractInfoCollator;
import com.gridnode.gtas.audit.extraction.collator.AuditInfoCollatorFactory;
import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsRetrySender;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ProcessNotificationHandler
{
  private static ProcessNotificationHandler _handler = new ProcessNotificationHandler();
  private static String CLASS_NAME = "ProcessNotificationHandler";
  private static Properties _jmsSenderProperties = new Properties();
  private Logger _log = null;
  
  private ProcessNotificationHandler()
  {
    _log = getLogger();
  }
  
  public static ProcessNotificationHandler getInstance()
  {
    synchronized (_jmsSenderProperties)
    {
      if(_jmsSenderProperties.size() == 0)
      {
        _jmsSenderProperties = getJMSSenderProperties();
      }
    }
    return _handler;
  }
  
  public void handleNotification(AbstractNotification notification) throws AuditInfoCollatorException
  {
    String methodName = "handleNotification";
    
    _log.debugMessage(methodName, null, "handleNotification "+notification);
    AuditTrailData trailData = getCollator(notification).gatherInfo(notification);
    _log.debugMessage(methodName, null, "AuditTrailData is "+trailData);
    
    sendToQueue(trailData);
    
  }
  
  private AbstractInfoCollator getCollator(AbstractNotification notification)
  {
    return AuditInfoCollatorFactory.getInstance().getAuditInfoCollator(notification);
  }
  
  private void sendToQueue(AuditTrailData trailData) throws AuditInfoCollatorException
  {
    String methodName = "sendToQueue";
    Object[] params = new Object[]{trailData};
    try
    {
      JmsRetrySender retrySender = new JmsRetrySender();
      
      if(retrySender.isSendViaDefMode())
      {
        JmsSender sender = new JmsSender();
        sender.setSendProperties(_jmsSenderProperties);
        sender.send(trailData); //send to RemoteEventQueue: Listener (AuditTrailMDBean)
      }
      else
      {
        JndiFinder finder = new JndiFinder(null, LoggerManager.getOneTimeInstance());
        finder.setJndiSuffix("_AT");
        retrySender.setJNDIFinder(finder);
        retrySender.retrySend(trailData, null, IAuditTrailConstant.FAILED_JMS_CAT, _jmsSenderProperties);
      }
    }
    catch(Exception ex)
    {
      _log.logWarn(methodName, params, "Error in sending the AuditTrailData to the ", ex);
      throw new AuditInfoCollatorException("Error in sending to queue", ex);
    }
  }
  
  private static Properties getJMSSenderProperties()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    Properties pro = configStore.getProperties(IGTATConstant.JMS_CATEGORY);
    return pro; 
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}
