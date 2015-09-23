// %1023788047496:com.gridnode.pdip.base.time.util%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 * Apr 07 2003    Neo Sok Lay         loadMissed() invoked outside of LoadTask.
 *                                    Intial LoadTask to start 5 seconds later
 *                                    than loadMissed(). -- this is to prevent
 *                                    missed alarms from being triggered twice
 *                                    or redundant scheduling of the same missed
 *                                    alarm.
 * Oct 17 2005   Neo Sok Lay          Change _alarmHome to protected access to
 *                                    improve performance.
 * Nov 10 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 * Feb 01 2007    Neo Sok Lay         Remove loading of tasks.
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.facade.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmObj;
import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class Alarm implements AlarmAction
{

  static final String LogCat = Alarm.class.getName();
  //static final long TimeWindow = 1000 * 60 * 60; //1 hour
  //static final long PreLoadTime = 5000; //1 second
  private final Timer _timer = new Timer(true);
  private final HashMap<Long,AlarmTask> _alarmTaskList = new HashMap<Long,AlarmTask>();
  private int _alarmTaskNum = 0;
  //private LoadTask _loadTask = null;
  private Date _nextLoadTime = null;

  protected IiCalAlarmHome _alarmHome = null;
  private static Alarm _alarm = null;

  private Date getNextLoadTime()
  {
    //return _loadTask==null? null: _loadTask.getLoadTime();
    return _nextLoadTime;
  }

  public void setNextLoadTime(Date nextLoadTime)
  {
    _nextLoadTime = nextLoadTime;
  }
  
  private Alarm() throws ServiceLookupException
  {
    _alarmHome =
      (IiCalAlarmHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
        IiCalAlarmHome.class);
    //initialLoad();
    Log.debug(LogCat, "A New Alarm instance is created");
  }

//  private void initialLoad()
//  {
//    Date now = iCalUtil.getTimeInSecod(new Date());
//    /*080403NSL To perform loadMissed() outside of LoadTask
//                and initial LoadTask should start in 5 (PreloadTime) seconds time
//
//    _loadTask = new LoadTask(true, now);
//    _timer.schedule(_loadTask, now);
//    */
//    Date loadTime = new Date(now.getTime() + PreLoadTime*2);
//    loadMissed(now);
//    addLoadTask(loadTime);
//  }

//  private void addLoadTask(Date nextLoadTime)
//  {
//    //_loadTask = new LoadTask(false, nextLoadTime);
//    _loadTask = new LoadTask(nextLoadTime);
//    Date scheduleTime = new Date(nextLoadTime.getTime()- PreLoadTime);
//    _timer.schedule(_loadTask, scheduleTime);
//  }

  //void loadTask(boolean cacuMissed, Date curLoadTime)
//  void loadTask(Date curLoadTime)
//  {
//    try
//    {
//      Date nextLoadTime = iCalUtil.getTimeInSecod( new Date(curLoadTime.getTime() + TimeWindow)) ;
//      addLoadTask(nextLoadTime);
//
//      /*030408NSL Don't loadMissed in here
//      if(cacuMissed)
//       loadMissed(curLoadTime);
//      */
//      Log.debug(
//        LogCat,
//        "Load Alarm Task for [" + curLoadTime + "--" + nextLoadTime + "] at" + new Date());
//      IDataFilter filter = new DataFilterImpl();
////      filter.addRangeFilter(null, iCalAlarm.NEXT_DUE_TIME, _nextLoadTime, nextLoadTime, false);
//      filter.addSingleFilter(null, iCalAlarm.NEXT_DUE_TIME, filter.getLessOperator(), getNextLoadTime(), false);
//
//      Collection alarmList = _alarmHome.findByFilter(filter);
//      if (alarmList != null || !alarmList.isEmpty())
//      {
//        Iterator iter = alarmList.iterator();
//        for (; iter.hasNext();)
//        {
//          IiCalAlarmObj alarmObj = (IiCalAlarmObj) iter.next();
//          iCalAlarm alarm = (iCalAlarm) alarmObj.getData();
//          addAlarm((Long)alarm.getKey(), alarm.getNextDueTime());
//        }
//      }
//      Log.debug(LogCat, "Alarm's Load Task exit !");
//    }
//    catch (Throwable ex)
//    {
//      Log.err(LogCat, "Error in Alarm's Load Task", ex);
//    }
//  }


//  private void loadMissed(Date anchorTime)
//  {
//    Log.debug(LogCat, "Alarm's loadMissed called at " + new Date());
//    //load missed alarms
//    try
//    {
//      IDataFilter filter = new DataFilterImpl();
//      filter.addSingleFilter(null, iCalAlarm.NEXT_DUE_TIME, filter.getLessOperator(), anchorTime, false);
//      Collection alarmList = _alarmHome.findByFilter(filter);
//      if (alarmList != null || !alarmList.isEmpty())
//      {
//        IiCalTimeMgrObj timeMgr = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).
//                 getObj(IiCalTimeMgrHome.class.getName(), IiCalTimeMgrHome.class, new Object[0]);
//        Iterator iter = alarmList.iterator();
//        for (; iter.hasNext();)
//        {
//          IiCalAlarmObj alarmObj = (IiCalAlarmObj) iter.next();
//          timeMgr.alarmMissed(alarmObj, anchorTime);
//        }
//      }
//      Log.debug(LogCat, "Alarm's loadMissed finised at " + new Date());
//    }
//    catch (Throwable ex)
//    {
//      Log.err(LogCat, "Error in Alarm's loadMissed", ex);
//    }
//  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  static public synchronized Alarm instance() throws Exception
  {
    if (_alarm == null)
    {
      synchronized (Alarm.class)
      {
        if (_alarm == null)
          _alarm = new Alarm();
      }
    }
    return _alarm;
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarm DOCUMENT ME!
   */
  public void addAlarm(Long alarmUid, Date dueTime)
  {
    cancelAlarm(alarmUid);
    Date dueDate = dueTime;
    Log.debug(LogCat, "addAlarm called for alarm " + alarmUid + " at " + dueDate +" _nextLoadTime==" + getNextLoadTime());
    if (dueDate != null && isInTimeWindow(dueDate))
    {
      AlarmTask event = new AlarmTask(alarmUid);
      synchronized (_timer)
      {
        _timer.schedule(event, dueDate);
        _alarmTaskList.put(alarmUid, event);
        _alarmTaskNum++;
      }
      Log.debug(LogCat, "alarm scheduled for alarm " + alarmUid + " at " + dueDate);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarm DOCUMENT ME!
   */
  public void updateAlarm(Long alarmUid, Date dueTime)
  {
    Log.debug(LogCat, "updateAlarm callled for " + alarmUid + " with dueTime=" + dueTime);
    addAlarm(alarmUid, dueTime);
  }

  boolean isInTimeWindow(Date dueDate)
  {
    Date nextLoadTime = getNextLoadTime();
    if (nextLoadTime != null)
    {
      return dueDate.before(nextLoadTime);
    }
    return false;
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarmUid DOCUMENT ME!
   */
  public void cancelAlarm(Long alarmUid)
  {
    AlarmTask event = getTimerTaskFor(alarmUid);
    if (event != null)
    {
      synchronized (_timer)
      {
        event.cancel();
        _alarmTaskNum--;
        _alarmTaskList.remove(event.alarmUid);
      }
      Log.debug(LogCat, "alarm canceled for alarm " + alarmUid);
    }
  }

  AlarmTask getTimerTaskFor(Long alarmUid)
  {
    return (AlarmTask) _alarmTaskList.get(alarmUid);
  }


  class AlarmTask extends TimerTask
  {
    Long alarmUid = null;

    AlarmTask(Long alarmid)
    {
      this.alarmUid = alarmid;
    }

    public void run()
    {
      //UserTransaction userTx = null;
      try
      {
        Log.debug(LogCat, "alarm trigger for alarm " + alarmUid + " at " + new Date());
        IiCalAlarmObj alarmObj = null;
        try
        {
          alarmObj = _alarmHome.findByPrimaryKey(alarmUid);
        }
        catch (Exception ex)
        {
          Log.warn(
            LogCat,
            "error in retrieve alaram object from DB with Uid = "
              + alarmUid
              + "; it may already been removed!");
        }
        if (alarmObj != null)
        {
          Alarm.instance().cancelAlarm(alarmUid);
          IiCalTimeMgrObj timeMgr = (IiCalTimeMgrObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).
                 getObj(IiCalTimeMgrHome.class.getName(), IiCalTimeMgrHome.class, new Object[0]);

          timeMgr.alarmTriggered(alarmObj, new Date(this.scheduledExecutionTime()));
//          userTx = (UserTransaction) new InitialContext().lookup("UserTransaction");
//          userTx.begin();
//          userTx.commit();
        }
      }
      catch (Throwable ex)
      {
        Log.error(ILogErrorCodes.TIME_RUN_ALARM_ERROR,
                  LogCat, "[Alarm.run] Error in sending alarm message to trigger action: "+ex.getMessage(), ex);
        /*
        if (userTx != null)
          try
          {
            userTx.rollback();
          }
          catch (SystemException ex2)
          {
            Log.err(LogCat, "Error in Rollbak the transaction", ex2);
          }
          */
      }
    }
  }
/*
  class LoadTask extends TimerTask
  {
    //private boolean _caculateMissed = false;
    private Date _loadTime;
    //LoadTask(boolean cacuMissed, Date loadTime)
    LoadTask(Date loadTime)
    {
      //_caculateMissed = cacuMissed;
      if(loadTime == null)
        throw new IllegalArgumentException("Load Task's loadTime can not be null!");
      _loadTime = loadTime;
    }

    public Date getLoadTime()
    {
      return _loadTime;
    }

    public void run()
    {
      //Alarm.this.loadTask(_caculateMissed, _loadTime);
      Alarm.this.loadTask(_loadTime);
    }
  }*/
}
