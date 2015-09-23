/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KeepAliveTimerDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 * Dec 10 2002    Neo Sok Lay         Cancel all existing KeepAlive timers before
 *                                    adding new one.
 * Feb 20 2003    Neo Sok Lay         Do not send KeepAlive immediately. Should
 *                                    be sometime near connection expire time.
 *                                    This is to prevent the alarm from being
 *                                    trigger twice at the same time.
 */
package com.gridnode.gtas.server.connection.connect;

import java.util.Date;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
 
/**
 * This Delegate class Handles the Keep Alive step of the Connection process.
 * It would start a timer for sending Keep Alive message periodically to the
 * connected GridMaster for the connected session.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I6
 */
public class KeepAliveTimerDelegate extends AbstractConnectionDelegate
{
  public KeepAliveTimerDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[KeepAliveTimerDelegate.execute] Enter");

    // cancel all existing keep alive timers
    cancelTimer();

    // add alarm with keepaliveinterval
    iCalAlarm alarm = new iCalAlarm();
    alarm.setCategory("KeepAliveTimer");
    alarm.setReceiverKey("KeepAliveTimerMDBean");
    alarm.setSenderKey("KeepAliveTimerDelegate");

    long interval = _ctx.getNetworkSetting().getKeepAliveInterval().longValue()*60L;

    // don't start immediately, wait till near expire time.
    alarm.setStartDt(new Date(_helper.getCurrentTimestamp().getTime()+(interval*1000)));
    alarm.setRepeat(null);  //repeat == null means repeat indefinitely
    alarm.setDelayPeriod(new Long(interval));   //in seconds

    alarm = ServiceLookupHelper.getTimeManager().addAlarm(alarm);
    _ctx.setKeepAliveTimerID((Long)alarm.getKey());
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
  }

  /**
   * Cancel the KeepAliveTimer(s).
   */
  public void cancelTimer() throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, iCalAlarm.CATEGORY, filter.getEqualOperator(),
      "KeepAliveTimer", false);

    ServiceLookupHelper.getTimeManager().cancelAlarmByFilter(filter);
  }

}