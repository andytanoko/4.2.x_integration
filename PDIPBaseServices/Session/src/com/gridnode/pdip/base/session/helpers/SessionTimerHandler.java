/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionTimerHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 23 2002    Ooi Hui Linn         Created
 * Nov 10 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 */
package com.gridnode.pdip.base.session.helpers;

import java.util.Date;

import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class SessionTimerHandler
{

  public SessionTimerHandler()
  {
  }

  static public Long addTimer(Long timerInterval) throws Exception
  {
    IiCalTimeMgrObj calTimeMgr = getMgrObj();
    //    Collection alarmList = getMgrObj().findAlarm();   //Wishful thinking

    iCalAlarm alarm = new iCalAlarm();
    alarm.setCategory("SessionTimer");
    alarm.setReceiverKey("SessionTimerBean");
    alarm.setSenderKey("SessionTimerHandler");
    alarm.setStartDt(new Date());
        //alarm.setRepeat(null);  //repeat == null means repeat indefinitely
    alarm.setDelayPeriod(timerInterval);

    alarm = calTimeMgr.addAlarm(alarm);
    return (Long) alarm.getKey();
  }
  /*
  static public void updateTimer() throws Exception
  {
    IiCalTimeMgrObj calTimeMgr = getMgrObj();

  }*/

//  static public void findTimer()
//  {
//    IiCalTimeMgrObj calTimeMgr = getMgrObj();
//
//  }

  static protected IiCalTimeMgrObj getMgrObj() throws Exception
  {
    //return (IiCalTimeMgrObj) getHome().create();
  	return (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IiCalTimeMgrHome.class.getName(),
  	                                                                                      IiCalTimeMgrHome.class,
  	                                                                                      new Object[0]);
  }

  //static protected IiCalTimeMgrHome getHome()
  //{
  //  return (IiCalTimeMgrHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IiCalTimeMgrHome.class);
  //}
}