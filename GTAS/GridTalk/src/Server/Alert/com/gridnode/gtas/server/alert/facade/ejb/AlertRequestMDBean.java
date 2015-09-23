/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertRequestMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 24 2003    Neo Sok Lay         Created
 * Jun 16 2003    Neo Sok Lay         Alert trigger disabled but alert still
 *                                    raised. This happens to all Level 0
 *                                    effective triggers.
 * May 27 2004    Neo Sok Lay         GNDB00024897: Raise specified alert 
 *                                    without finding the alert trigger.
 */
package com.gridnode.gtas.server.alert.facade.ejb;

import com.gridnode.gtas.server.alert.helpers.Logger;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This MDBean is invoked when there is a request for raising an alert using
 * Level 0 alert trigger.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.4
 * @since 2.1
 */
public class AlertRequestMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5016080528235559883L;
	private MessageDrivenContext _mdx = null;

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
    FacadeLogger logger = Logger.getAlertRequestFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};


    try
    {
      logger.logEntry(methodName, params);
      
      AlertRequestNotification notification =
        (AlertRequestNotification)((ObjectMessage)message).getObject();

      handleNotification(notification);
    }
    catch (Throwable t)
    {
      logger.logMessage(methodName, params, t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * Handle the AlertRequestNotification.
   */
  private void handleNotification(AlertRequestNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getAlertRequestFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      String  alertType    = notification.getAlertType();
      boolean hasProviders = (notification.getProviders() != null);
      Long alertUID = notification.getAlertUID();

      if (alertType == null)
        throw new Exception("Alert type not specified. Unable to raise alert!");

      IDataProvider[] providers = null;
      if (hasProviders)
        providers = (IDataProvider[])notification.getProviders().toArray(new IDataProvider[0]);

      if (alertUID == null)
        raiseAlert(alertType, providers);
      else
        raiseAlert(alertUID, providers);
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

  private void raiseAlert(String alertType, IDataProvider[] providers)
  {
    FacadeLogger logger = Logger.getAlertRequestFacadeLogger();
    String methodName   = "raiseAlert";
    Object[] params     = new Object[] {alertType, providers};

    try
    {
      logger.logEntry(methodName, params);

      // search alert trigger (level 0, alert type)
      AlertTrigger trigger = ServiceLookupHelper.getGridTalkAlertMgr().findAlertTrigger(
                               AlertTrigger.LEVEL_0, alertType, null, null,
                               null, null);
      if (trigger != null && trigger.isEnabled())
      {
        // create provider list
        DefaultProviderList providerList = new DefaultProviderList(providers);
        ServiceLookupHelper.getAlertManager().triggerAlert(
          trigger.getAlertUID(), providerList, (String)null);
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

  private void raiseAlert(Long alertUID, IDataProvider[] providers)
  {
    FacadeLogger logger = Logger.getAlertRequestFacadeLogger();
    String methodName   = "raiseAlert";
    Object[] params     = new Object[] {alertUID, providers};

    try
    {
      logger.logEntry(methodName, params);

      DefaultProviderList providerList = new DefaultProviderList(providers);
      ServiceLookupHelper.getAlertManager().triggerAlert(
        alertUID, providerList, (String)null);
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