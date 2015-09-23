/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseCreatedListenerMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.listener.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.notify.EnterpriseCreatedNotification;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This MDB listens to the Notifier topic for notifications of EnterpriseCreated
 * activity.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class EnterpriseCreatedListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4387806894772050435L;
	private MessageDrivenContext _mdx = null;

  public EnterpriseCreatedListenerMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getEnterpriseCreatedListenerFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      EnterpriseCreatedNotification notification = (EnterpriseCreatedNotification)((ObjectMessage)message).getObject();

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
   * Handle the EnterpriseCreatedNotification
   */
  private void handleNotification(EnterpriseCreatedNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getEnterpriseCreatedListenerFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      CompanyProfile profile = (CompanyProfile)notification.getProfile();

      ServiceLookupHelper.getEnterpriseHierarchyMgr().onCreateMyEnterprise(
        notification.getEnterpriseId(), profile);
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