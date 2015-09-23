/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridMasterAvailabilityMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 * Jan 23 2003    Neo Sok Lay         Set my GNCI.
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import com.gridnode.gtas.server.notify.ConnectionNotification;
import com.gridnode.gtas.server.enterprise.helpers.Logger;

import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This MDB listens to the Notifier topic for notifications of GridMaster
 * availability to send messages to.<p>
 * It keeps track of the currently available (connected) GridMaster, and
 * post instructions sent to the GridMaster Postman queue can be processed to
 * be sent out to the connected GridMaster. <p>
 * On the other hand, if GridMaster is not available, the post instructions will
 * not be processed, and these instructions will be lost when the system
 * shuts down.
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GridMasterAvailabilityMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8490260861485951471L;
	private MessageDrivenContext _mdx = null;

  public GridMasterAvailabilityMDBean()
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
    Logger.debug("[GridMasterAvailabilityMDBean.ejbRemove] Invoked");
  }

  public void ejbCreate()
  {
    Logger.debug("[GridMasterAvailabilityMDBean.ejbCreate] Invoked");
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getPostmanFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      ConnectionNotification notification = (ConnectionNotification)((ObjectMessage)message).getObject();

      if (notification.getNodeType() == ConnectionNotification.TYPE_GM)
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
   * Handle the ConnectionNotification for GridMaster.
   * This method updates the GridMasterState on whether GridMaster is online/offline
   * and what is the NodeID of the GridMaster that is online.<p>
   * Note that if a particular GridMaster is online on one notification, the
   * next notification for offline should be directed to the same GridMaster.
   * <p>In any case, the if GridMaster is offline, the NodeID is not relevant.
   */
  private void handleNotification(ConnectionNotification notification)
    throws Throwable
  {
    FacadeLogger logger = Logger.getPostmanFacadeLogger();
    String methodName   = "handleNotification";
    Object[] params     = new Object[] {notification};

    try
    {
      logger.logEntry(methodName, params);

      // update the GridMasterState.
      GridMasterState.getInstance().setGridMaster(
        notification.getNode(),
        notification.getState() == ConnectionNotification.STATE_ONLINE,
        notification.getMyGNCI());

      // sync be??
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