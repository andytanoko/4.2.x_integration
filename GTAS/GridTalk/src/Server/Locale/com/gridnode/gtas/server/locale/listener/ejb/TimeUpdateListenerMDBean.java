/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimeUpdate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.listener.ejb;

import java.util.Properties;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.locale.helpers.Logger;
import com.gridnode.gtas.server.notify.TimeUpdateNotification;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This MDB listens to the Notifier topic for notifications of Time Update
 * activities.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class TimeUpdateListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6472274446919042109L;
	private MessageDrivenContext _mdx = null;

  public TimeUpdateListenerMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getTimeUpdateFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      TimeUpdateNotification notification = (TimeUpdateNotification)((ObjectMessage)message).getObject();

      handleNotification(notification);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the TimeUpdateNotification:
   */
  private void handleNotification(TimeUpdateNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getTimeUpdateFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      if (notification.getActionCode() == TimeUpdateNotification.UPDATE_UTC_TIME)
      {
        Properties props = notification.getTimeUpdateProperties();
        long utcTime = Long.valueOf(props.getProperty("utc", "0")).longValue();
        long localTime = Long.valueOf(props.getProperty("local", "0")).longValue();

        UtcTimeServer.getInstance().setUtcOffset(utcTime, localTime);
      }
    }
    catch (Exception ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }


}