/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocAlertRequestMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26 2003    Neo Sok Lay         Created
 * Apr 24 2003    Neo Sok Lay         Changed AlertRequestNotification.
 *                                    Search AlertTrigger for alert to raise.
 * Jun 02 2003    Neo Sok Lay         Raising UserDefined alert should call
 *                                    method with alertUID instead of alertType.
 * Jun 26 2003    Neo Sok Lay         GNDB00014171: Handle CleanUpDocument option
 *                                    after raising the alert.
 * Nov 23 2005    Neo Sok Lay         Change interface in DocAlertManagerBean.                                  
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.gtas.server.docalert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.notify.IAlertTypes;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This MDBean is invoked when there is a request for raising an alert regarding
 * a document. At the moment, the following limitations apply for USER_DEFINED type
 * of alert:
 * <ul>
 * <li>no attachment will be sent for email alerts;</li>
 * <li>the recipients of the alerts are fixed, as specified in the message template</li>
 * </ul>
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I8
 */
public class DocAlertRequestMDBean
  implements MessageDrivenBean,
             MessageListener
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8784903030633646594L;
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

      //if (notification.getCategory() == AlertRequestNotification.CATEGORY_DOCUMENT)
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

      String alertType = notification.getAlertType();
      Long alertUID    = notification.getAlertUID();
      GridDocument gdoc = (GridDocument)notification.getDocument();
      boolean hasProviders = (notification.getProviders() != null);

      if (alertType == null)
        throw new Exception("Alert Type not specified. Unable to raise alert!");

      if (gdoc == null)
        throw new Exception("GridDocument must be specified for document alerts!");

      IDataProvider[] providers = null;
      if (hasProviders)
        providers = (IDataProvider[])notification.getProviders().toArray(new IDataProvider[0]);

      if (IAlertTypes.USER_DEFINED.equals(alertType))
        ServiceLookupHelper.getDocAlertMgr().raiseDocAlert(alertUID, gdoc, null, providers);
        //ServiceLookupHelper.getDocAlertMgr().raiseDocAlert(alertUID, gdoc, null, null, providers);
      else
        ServiceLookupHelper.getDocAlertMgr().raiseDocAlert(alertType, gdoc, providers);
//      ServiceLookupHelper.getDocAlertMgr().raiseDocAlert(
//        gdoc, null, alertName, false, providers);

      if (notification.isCleanUpDocument())
      {
        FileHelper.delTempUdocFile(gdoc);
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