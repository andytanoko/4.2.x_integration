/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 7, 2004    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.listener.ejb;

import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.SyncResourceDelegate;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.notify.ConnectionNotification;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Listener for Online connection notification for GM and Partner GridNodes.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class ConnectionListenerMDBean
  implements MessageDrivenBean, MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -400096741321159097L;
	private MessageDrivenContext _mdx = null;

  public ConnectionListenerMDBean()
  {
  }

  /**
   * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
   */
  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws EJBException
  {
    _mdx = ctx;
  }

  /**
   * @see javax.ejb.MessageDrivenBean#ejbRemove()
   */
  public void ejbRemove()
  {
    _mdx = null;
  }

  public void ejbCreate()
  {
  }

  /**
   * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
   */
  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getConnectionListenerFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      ConnectionNotification notification = (ConnectionNotification)((ObjectMessage)message).getObject();

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
   * Handles the ConnectionNotification: Perform synchronization of unsync resources to the
   * online GridNode.
   * 
   * @param notification The notification object.
   * @throws Throwable Error handling the notification.
   */
  private void handleNotification(ConnectionNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getConnectionListenerFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      // Attempt to synchronize as long as the node comes online
      // or it is a partner node whereby the synchronization will be via GM if GM is online
      if (notification.getState() == ConnectionNotification.STATE_ONLINE || notification.getNodeType() == ConnectionNotification.TYPE_PARTNER)
        SyncResourceDelegate.syncResourcesToEnterprise(IResourceTypes.BUSINESS_ENTITY, notification.getNode());
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
