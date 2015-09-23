/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.init.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;
import com.gridnode.gtas.server.backend.helpers.Logger;

/**
 * This MDB will startup the GTAS backend listener.
 *
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class BackendListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1448200539794913617L;
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
    Logger.log("[BackendListenerMDBean.onMessage] Start");

    try
    {
      ActionHelper.getManager().startListener();
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_BACKEND_LISTENER_MDB,
                   "[BackendListenerMDBean.onMessage] Exception : "+ex.getMessage(), ex);
    }
    finally
    {
      Logger.log("[BackendListenerMDBean.onMessage] End");
    }
  }

}