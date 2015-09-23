/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.listener.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.notify.ConnectionNotification;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This message driven bean is used to perform activities related to change of
 * connection status.
 *
 * @author Koh Han Sing
 *
 * @version 2.2 I1
 * @since 2.2 I1
 */
public class ConnectionMDBean
       implements MessageDrivenBean, MessageListener
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 41891198256843003L;
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

  public void onMessage(Message msg)
  {
    Logger.log("[ConnectionMDBean.onMessage] Enter");
    try
    {
      ConnectionNotification noti = (ConnectionNotification)((ObjectMessage)msg).getObject();

      if (noti.getState() == ConnectionNotification.STATE_ONLINE)
      {
        Logger.log("[ConnectionMDBean.onMessage] STATE_ONLINE");
        if (noti.getNodeType() == ConnectionNotification.TYPE_GM)
        {
        Logger.log("[ConnectionMDBean.onMessage] TYPE_GM");
          //Connect to GridMaster
          getDocumentManager().uploadToGridMaster();
        }
        else if (noti.getNodeType() == ConnectionNotification.TYPE_PARTNER)
        {
        Logger.log("[ConnectionMDBean.onMessage] TYPE_PARTNER");
          //Partner Online
          String nodeId = noti.getNode();
          getDocumentManager().resumeSend(nodeId);
        }
      }
    }
    catch (EJBException ejbEx)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_CHANGE_MDB,
                   "[ConnectionMDBean.onMessage] EJB Exception", ejbEx);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_CHANGE_MDB,
                   "[ConnectionMDBean.onMessage] Exception", ex);
    }
    Logger.log("[ConnectionMDBean.onMessage] End");
  }

  private IDocumentManagerObj getDocumentManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

}