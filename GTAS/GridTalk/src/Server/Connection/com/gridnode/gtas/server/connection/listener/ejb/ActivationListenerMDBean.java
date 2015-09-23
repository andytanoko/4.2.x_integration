/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationListenerMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 07 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.listener.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.notify.ActivationNotification;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This MDB listens to the Notifier topic for notifications of Activation
 * activities.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7363173760585777711L;
	private MessageDrivenContext _mdx = null;

  public ActivationListenerMDBean()
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
    FacadeLogger logger = Logger.getActivationListenerFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      ActivationNotification notification = (ActivationNotification)((ObjectMessage)message).getObject();

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
   * Handle the ActivationNotification for a GridNode Partner:
   * <p>If activated, inform Partner online.
   * <p>If deactivated, update Partner offline.
   */
  private void handleNotification(ActivationNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getActivationListenerFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      if (notification.getState() == ActivationNotification.STATE_ACTIVATED)
      {
        ServiceLookupHelper.getConnectionService().onPartnerActivated(
          notification.getPartnerNode());
      }
      else
      {
        ServiceLookupHelper.getConnectionService().onPartnerDeactivated(
          notification.getPartnerNode());
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