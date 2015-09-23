/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DbArchiveMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06, 2004   Mahesh              Created
 * Oct 11 2005    Neo Sok Lay         Change for J2EE compliance
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */ 
package com.gridnode.gtas.server.dbarchive.listener.ejb;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.dbarchive.listener.DBArchiveListener;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;

public class DbArchiveMDBean extends DBArchiveListener implements MessageDrivenBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9194189200381809836L;
	private MessageDrivenContext m_context;
  
  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    m_context = ctx;
  }

  public void ejbCreate()
  {
    Logger.log("[DbArchiveMDBean.ejbCreate] Enter");
  }

  public void ejbRemove()
  {
    Logger.log("[DbArchiveMDBean.ejbRemove] Enter");
  }
  
  public void onMessage(Message msg)
  {
    Logger.log("[DbArchiveMDBean.onMessage] Enter");
    try
    {
      if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        Logger.log("[DbArchiveMDBean.onMessage] Redelivered msg found, ignored it. Message: "+msg);
        return;
      }
      super.onMessage(msg);
    }
    catch(Throwable th)
    {
      Logger.error(ILogErrorCodes.GT_DB_ARCHIVE_MDB,
                   "[DbArchiveMDBean.onMessage] Error ",th);
    }
  }
}
