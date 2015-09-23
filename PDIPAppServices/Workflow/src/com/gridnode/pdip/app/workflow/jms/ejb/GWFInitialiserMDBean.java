/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GWFInitialiserMDBean.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 29, 2004 			Mahesh             	Created
 * Dec 05, 2007       Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 * July27, 2008       Tam Wei Xiang       #69:Remove explicitly checked for redeliverd msg and dropped the message.
 */
package com.gridnode.pdip.app.workflow.jms.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlInitialiserHelper;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

/**
 * This MDB listens to the Initialiser topic for notifications of System Startup activity.<p>
 *
 */
public class GWFInitialiserMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1282510214428780884L;
	private MessageDrivenContext _mdx = null;

  public GWFInitialiserMDBean()
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
    Logger.log("[GWFInitialiserMDBean.onMessage] Enter");
    try
    {
      if(message.getJMSRedelivered())
      {
        Logger.log("[GWFInitialiserMDBean.onMessage] Redelivered msg found. Message: "+message);
      }
      
      String command = message.getStringProperty("command");
      if ("START".equals(command))
      {
        XpdlInitialiserHelper.initializeXpdl();
      }
    }
    catch (Throwable th)
    {
      Logger.error(ILogErrorCodes.WORKFLOW_MDB_ONMESSAGE,
                   "[GWFInitialiserMDBean.onMessage] Error",th);
    }
    finally
    {
      Logger.log("[GWFInitialiserMDBean.onMessage] Exit");
    }
  }

}