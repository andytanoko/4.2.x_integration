/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimerInitialiserMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 07 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.time.init.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.pdip.base.time.facade.util.Alarm;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * This MDB listens to the Initialiser topic for Initialisation.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class TimerInitialiserMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2575051587661467132L;
	private transient FacadeLogger _logger = null;
  private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
    _logger = FacadeLogger.getLogger("TimerInitialiserMDBean", "TIME.INIT");
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      _logger.logEntry(methodName, params);

      String command = message.getStringProperty("command");
      if ("START".equals(command))
        startInitialisation();
    }
    catch (Throwable ex)
    {
      _logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      _logger.logExit(methodName, params);
    }
  }

  /**
   * Starts the intialisation sequence for Time module.
   */
  private void startInitialisation()
    throws Throwable
  {
    String methodName   = "startInitialisation";
    Object[] params     = new Object[] {};

    try
    {
      _logger.logEntry(methodName, params);

      // kick start the timer to resume the scheduled alarms.
      Alarm.instance();
    }
    catch (Exception ex)
    {
      _logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      _logger.logExit(methodName, params);
    }
  }


}