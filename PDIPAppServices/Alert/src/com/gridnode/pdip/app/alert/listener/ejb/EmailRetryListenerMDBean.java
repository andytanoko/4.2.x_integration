/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailRetryListenerMDBean.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jun 30, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.alert.listener.ejb;

import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.mail.AlertMailService;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.base.time.facade.ejb.*;

public class EmailRetryListenerMDBean extends TimeInvokeMDBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7088545942478729693L;

	public void ejbCreate()
  {
  }

  protected void invoke(AlarmInfo info)
  {
    super.invoke(info);
    try
    {
      AlertMailService mailService = AlertMailService.getInstance();
      mailService.retrySending();
    }
    catch (Throwable th)
    {
      AlertLogger.errorLog(ILogErrorCodes.EMAIL_RETRY_LISTENER,
                           "EmailRetryListenerMDBean","invoke","Error in retrySending: "+th.getMessage(),th);
    }
  }

}
