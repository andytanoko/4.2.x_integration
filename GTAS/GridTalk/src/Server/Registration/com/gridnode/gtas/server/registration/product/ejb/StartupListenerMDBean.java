/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StartupListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 18 2004    Neo Sok Lay         Created
 * Mar 28 2006    Neo Sok Lay         Add broadcasting of license state
 */
package com.gridnode.gtas.server.registration.product.ejb;

import com.gridnode.gtas.server.registration.helpers.AlertUtil;
import com.gridnode.gtas.server.registration.helpers.ScheduleHelper;
import com.gridnode.gtas.server.registration.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.registration.product.RegistrationLogic;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * This MDB listens to the Initialiser topic for Initialisation.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since GT 2.2 I5
 */
public class StartupListenerMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2177359744742618240L;
	private transient FacadeLogger _logger = null;
  private MessageDrivenContext _mdx = null;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
    _logger = FacadeLogger.getLogger("StartupListenerMDBean", "REG.INIT");
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
      {
        ScheduleHelper.upgradeCheckLicenseSchedule();
        AlertUtil.notifyLicenseState(ServiceLookupHelper.getRegistrationServiceBean().getRegistrationInfo()); //NSL20060328
      }
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

}
