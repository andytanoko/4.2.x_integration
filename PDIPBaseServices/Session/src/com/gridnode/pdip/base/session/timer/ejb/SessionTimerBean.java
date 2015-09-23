/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionTimerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 13 2002    Ooi Hui Linn         Created
 * Nov 10 2005    Neo Sok Lay        Use ServiceLocator instead of ServiceLookup
 */
package com.gridnode.pdip.base.session.timer.ejb;

import com.gridnode.pdip.base.session.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.helpers.Logger;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
public class SessionTimerBean extends TimeInvokeMDBean
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1690690293943673974L;

	public SessionTimerBean()
  {
  }

  protected void invoke(AlarmInfo info)
  {
    try
    {
      Logger.debug("[SessionTimerBean.invoke] Alarm invoked " + info);
      //ISessionManagerHome home = (ISessionManagerHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(ISessionManagerHome.class);
      //ISessionManagerObj remote = home.create();
      ISessionManagerObj remote = (ISessionManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ISessionManagerHome.class.getName(),
                                                                                                                    ISessionManagerHome.class,
                                                                                                                    new Object[0]);
      int closedSessions = remote.closeInactiveSessions(remote.getPropertySessionMaxIdleTime());
      Logger.debug("[SessionTimerBean.invoke] Closed inactive session = " + closedSessions);
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.SESSION_TIMER_BEAN_INVOKE,
                   "[SessionTimerBean.invoke] Error while closing inactive session: "+ex.getMessage(), ex);
    }
  }

}