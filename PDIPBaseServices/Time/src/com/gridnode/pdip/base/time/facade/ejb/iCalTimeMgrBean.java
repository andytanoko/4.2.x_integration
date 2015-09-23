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
 * Oct 27 2003    Neo Sok Lay         Fix defect: GNDB00015963
 * Feb 11 2004    Koh Han Sing        Added taskId
 * Oct 24 2005    Neo Sok Lay         EJB 1.0 allowed the business methods to throw 
 *                                    the java.rmi.RemoteException to indicate a non-application exception. 
 *                                    This practice was deprecated in EJB 1.1 - an EJB 1.1 or EJB 2.0 
 *                                    compliant enterprise bean should throw the javax.ejb.EJBException or 
 *                                    another RuntimeException to indicate non-application exceptions to the Container.
 * Jan 29 2007    Neo Sok Lay         Change to use Queue instead of Topic for alarm msg.                                   
 * Feb 01 2007    Neo Sok Lay         Add loadMissedTasks() and loadTasks() - factored out from
 *                                    Alarm.
 */

package com.gridnode.pdip.base.time.facade.ejb;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.Reader;
import java.io.Serializable;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.Destination;

import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmObj;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventObj;
import com.gridnode.pdip.base.time.entities.helpers.AlarmCaculator;
import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.model.IiCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.base.time.entities.value.exchange.GenMime;
import com.gridnode.pdip.base.time.entities.value.exchange.ParseMime;
import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.time.facade.util.Alarm;
import com.gridnode.pdip.base.time.facade.util.AlarmAction;
import com.gridnode.pdip.base.time.facade.util.JMSSender;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.log.Log;


public class iCalTimeMgrBean implements SessionBean, AlarmAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6522300164226701012L;
	static final String CATEGORY= "Category";
  static final String SENDER_KEY= "SenderKey";
  static final String RECEIVER_KEY= "ReceiverKey";
  static final String TASK_ID= "TaskId";
  
  static final String LogCat= "BASE.TIME";
  private SessionContext _sessionCtx= null;
  private IiCalAlarmHome _alarmHome= null;
  private IiCalEventHome _eventHome= null;

  /**
   * DOCUMENT ME!
   *
   * @param parm1 DOCUMENT ME!
   */
  public void setSessionContext(SessionContext parm1)
  {
    _sessionCtx= parm1;
  }

  /**
   * DOCUMENT ME!
   *
   * @throws CreateException DOCUMENT ME!
   */
  public void ejbCreate() throws CreateException
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbActivate()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbPassivate()
  {
  }

  /**
  * DOCUMENT ME!
  *
  * @param alarm DOCUMENT ME!
  * @return DOCUMENT ME!
  * @throws Exception DOCUMENT ME!
  */
  public iCalAlarm getAlarm(Long alarmUid) throws Exception
  {
    IiCalAlarmObj _alarmObj= null;
    try
    {
      Log.debug(LogCat, "Inside getAlarm, with alarmUid " + alarmUid);
      _alarmObj= getAlarmHome().findByPrimaryKey(alarmUid);
      iCalAlarm res= (iCalAlarm) _alarmObj.getData();
      return res;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in getAlarm(alarmUid) ", e);
      throw e;
    }
  }

  /**
   * Find the keys of the iCalAlarms that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of iCalAlarm entity
   */
  public List findAlarms(IDataFilter filter) throws Exception
  {
    List<iCalAlarm> alarms= new ArrayList<iCalAlarm>();
    try
    {
      Log.log(LogCat, "[findAlarms] filter: " + filter==null?null:filter.getFilterExpr());
      Collection alarmObjs= getAlarmHome().findByFilter(filter);
      if (alarmObjs == null)
      {
        System.out.println("[findAlarms] alarmObjs == null");
        return alarms;
      }
      System.out.println("[findAlarms] alarmObjs.size() ="+alarmObjs.size());
      Iterator iter= alarmObjs.iterator();
      IiCalAlarmObj _alarmObj= null;
      while (iter.hasNext())
      {
        _alarmObj= (IiCalAlarmObj) iter.next();
        iCalAlarm alarm= (iCalAlarm) _alarmObj.getData();
        alarms.add(alarm);
      }
    }
    catch (Exception ex)
    {
      Log.warn(LogCat, "[findAlarms] Error ", ex);
      throw ex;
    }
    return alarms;
  }


  public iCalEvent getEvent(Long eventUid) throws Exception
  {
    IiCalEventObj _eventObj= null;
    try
    {
      Log.debug(LogCat, "Inside getEvent, with eventUid " + eventUid);
      _eventObj= getEventHome().findByPrimaryKey(eventUid);
      iCalEvent res= (iCalEvent) _eventObj.getData();
      return res;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in getEvent(eventUid) ", e);
      throw e;
    }
  }

  /**
   * Find the keys of the iCalEvents that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of iCalEvent entity
   */
  public List findEvents(IDataFilter filter) throws Exception
  {
    List<iCalEvent> events= new ArrayList<iCalEvent>();
    try
    {
      Log.log(LogCat, "[findEvents] filter: " + filter.getFilterExpr());
      Collection eventObjs= getEventHome().findByFilter(filter);
      if (eventObjs == null)
        return events;
      Iterator iter= eventObjs.iterator();
      IiCalEventObj eventObj= null;
      while (iter.hasNext())
      {
        eventObj= (IiCalEventObj) iter.next();
        iCalEvent event= (iCalEvent) eventObj.getData();
        events.add(event);
      }
    }
    catch (Exception ex)
    {
      Log.warn(LogCat, "[findEvents] Error ", ex);
      throw ex;
    }
    return events;
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarm DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public iCalAlarm addAlarm(iCalAlarm alarm) throws Exception
  {
    IiCalAlarmObj _alarmObj= null;
    try
    {
      Log.debug(LogCat, "Inside addAlarm, with alarm " + alarm);
      _alarmObj= getAlarmHome().create(alarm);
      alarm= (iCalAlarm) _alarmObj.getData();
      sendAddAlarmMsg(alarm);
      return alarm;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in addAlarm(alarm) ", e);
      throw e;
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarm DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public iCalAlarm updateAlarm(iCalAlarm alarm, boolean resetTime)
    throws Exception
  {
    IiCalAlarmObj _alarmObj= null;
    try
    {
      Log.debug(LogCat, "Inside updateAlarm, with alarm " + alarm + ";resetTime " + resetTime);
      _alarmObj= getAlarmHome().findByPrimaryKey((Long) alarm.getKey());
      if (!resetTime)
      {
        _alarmObj.setData(alarm);
        alarm= (iCalAlarm) _alarmObj.getData();
        return alarm;
      }

      iCalAlarm oldAlarm= (iCalAlarm) _alarmObj.getData();
      Date oldDueDate= oldAlarm.getNextDueTime();
      if (!oldAlarm.isAbsolute())
      {
        alarm.setRecurListStr(null);
				alarm.setIsRecurComplete(Boolean.FALSE);
				alarm.setCurRecur(null);
//				String curRecur = alarm.getCurRecur();
//				if(curRecur != null)
//				{
//					Date now = new Date();
//					Date curRecurDate = AlarmCaculator.str2Date(curRecur);
//			    if(iCalUtil.beforeBySecond(curRecurDate, now))
//					  alarm.setCurRecur(AlarmCaculator.date2Str(now));
//				}
      }
      _alarmObj.setData(alarm);
      alarm= (iCalAlarm) _alarmObj.getData();

      Date newDueDate= alarm.getNextDueTime();
      if (oldDueDate == newDueDate)
        return alarm;
      if (oldDueDate == null)
      {
        sendAddAlarmMsg(alarm);
      }
      else
        if (!oldDueDate.equals(newDueDate))
        {
          sendUpdateAlarmMsg(alarm);
        }
      return alarm;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in updateAlarm(alarm) ", e);
      throw e;
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param alarmUid DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void cancelAlarm(Long alarmUid) throws Exception
  {
    Log.debug(LogCat, "Inside cancelAlarm, with alarmUid " + alarmUid);
    iCalAlarm alarm= getAlarm(alarmUid);
    cancelAlarm(alarm);
  }

  /**
   * DOCUMENT ME!
   *
   * @param filter DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void cancelAlarmByFilter(IDataFilter filter) throws Exception
  {
    try
    {
      Log.debug(LogCat, "Inside cancelAlarmByFilter, with filter " + filter);
      Collection alarmObjList= getAlarmHome().findByFilter(filter);
      if (alarmObjList == null)
        return;
      Iterator iter= alarmObjList.iterator();
      IiCalAlarmObj _alarmObj= null;
      while (iter.hasNext())
      {
        _alarmObj= (IiCalAlarmObj) iter.next();
        iCalAlarm alarm= (iCalAlarm) _alarmObj.getData();
        cancelAlarm(alarm);
      }
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in cancelAlarmByFilter(alarm) ", e);
      throw e;
    }
  }

  private void cancelAlarm(iCalAlarm alarm) throws Exception
  {
    if (Boolean.TRUE.equals(alarm.getIsPseudoParent()))
    {
      Long parentUid= alarm.getParentUid();
      if (parentUid != null)
        getEventHome().remove(parentUid);
    }
    Long alarmUid= (Long) alarm.getKey();
//    Object[] value= new Object[] { CANCEL, alarmUid };
//    sendMsgTo(value, _sessionCtx);
    Alarm.instance().cancelAlarm(alarmUid);
    _alarmHome.remove(alarmUid);
  }

  /**
   * DOCUMENT ME!
   *
   * @param event DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public iCalEvent addEvent(iCalEvent event) throws Exception
  {
    IiCalEventObj _eventObj= null;
    try
    {
      event.setKind(iCalEvent.KIND_EVENT);
      Log.debug(LogCat, "Inside addEvent, with event " + event);
      _eventObj= getEventHome().create(event);
      iCalEvent res= (iCalEvent) _eventObj.getData();
      return res;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in addEvent(alarm) ", e);
      throw e;
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param event DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void updateEvent(iCalEvent event, boolean resetAlarm) throws Exception
  {
    IiCalEventObj _eventObj= null;
    try
    {
      Log.debug(LogCat, "Inside updateEvent, with event " + event + " resetAlarm " + resetAlarm);
      _eventObj= getEventHome().findByPrimaryKey((Long) event.getKey());
      _eventObj.setData(event);
      event = (iCalEvent)_eventObj.getData();

      if (!resetAlarm)
      {
        Log.debug(LogCat, "Inside updateEvent, updateEvent finieshed!");
        return;
      }

      Log.debug(LogCat, "Inside updateEvent, update the relevent alarms!");
      IDataFilter filter= new DataFilterImpl();
      filter.addSingleFilter(
        null,
        IiCalAlarm.PARENT_UID,
        filter.getEqualOperator(),
        event.getKey(),
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        IiCalAlarm.PARENT_KIND,
        filter.getEqualOperator(),
        iCalEvent.KIND_EVENT,
        false);
      List alarms= findAlarms(filter);
      for (int i= 0; i < alarms.size(); i++)
      {
        updateAlarm((iCalAlarm) alarms.get(i), true);
      }
      return;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in updateEvent(event) ", e);
      throw e;
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param eventUid DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void deleteEvent(Long eventUid) throws Exception
  {
    try
    {
      Log.debug(LogCat, "Inside deleteEvent, with eventUid " + eventUid);
      IDataFilter filter= new DataFilterImpl();
      filter.addSingleFilter(
        null,
        IiCalAlarm.PARENT_UID,
        filter.getEqualOperator(),
        eventUid,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        IiCalAlarm.PARENT_KIND,
        filter.getEqualOperator(),
        iCalEvent.KIND_EVENT,
        false);
      cancelAlarmByFilter(filter);
      getEventHome().remove(eventUid);
      return;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in deleteEvent(event) ", e);
      throw e;
    }
  }

  public List parseMime(Reader in) throws Exception
  {
    ParseMime parser= new ParseMime();
    return parser.parse(in);
  }

  public String genMime(List compList) throws Exception
  {
    return GenMime.genCalendar(compList);
  }

  //newly added for the recurrent alarms

  public iCalAlarm addRecurredAlarm(
    iCalAlarm alarm,
    Date startDate,
    Date endDate,
    iCalRecurrenceV[] recurs,
    iCalRecurrenceV[] exceptRecurs)
    throws Exception
  {
    try
    {
      iCalEvent event= iCalUtil.createPseudoEvent(startDate, endDate, recurs, exceptRecurs);
      event= addEvent(event);
      alarm.setPseudoParent((Long) event.getKey());
      Log.debug(
        LogCat,
        "Inside addRecurredAlarm, with  eventUid= " + (Long) event.getKey() + ", alarm " + alarm);
      alarm= addAlarm(alarm);

      return alarm;
    }
    catch (Exception e)
    {
      Log.warn(LogCat, "Error in addRecurredAlarm(alarm) ", e);
      throw e;
    }
  }

  //Time module internal methods
  public void alarmTriggered(IiCalAlarmObj alarmObj, Date dueTime)
  {
    try
    {
      iCalAlarm alarmEntity= (iCalAlarm) alarmObj.getData();
      Serializable value= dueTime;
      Log.debug(LogCat, "alarmTriggered for the repeated Alarm " + alarmEntity.getKey());
      sendAlarmMsgWithProperty(alarmEntity, JMSSender.instance().invokeDest, INVOKE, value);
      //recaculate for the repeated alarm
      alarmEntity.increaseCount();
      alarmObj.setData(alarmEntity);
      alarmEntity= (iCalAlarm) alarmObj.getData();
      Date dueDate= alarmEntity.getNextDueTime();
      if (dueDate != null)
      {
        //        Object[] value= new Object[] { ADD, alarm };
        //        sendMsgTo(value, _sessionCtx);
        Log.debug(
          LogCat,
          "call addAlarm for the repeated Alarm " + alarmEntity.getKey() + " at " + dueDate);
        sendAddAlarmMsg(alarmEntity);
      }
    }
    catch (Throwable e)
    {
      Log.error(ILogErrorCodes.TIME_ALARM_TRIGGER_ERROR,
                LogCat, "[iCalTimeMgrBean.alarmTriggered] Error while triggering alarm: "+e.getMessage(), e);
    }
  }

  public void alarmMissed(IiCalAlarmObj alarmObj, Date now)
  {
    try
    {
      iCalAlarm alarm= (iCalAlarm) alarmObj.getData();
      Log.log(LogCat, "alarmMissed for Alarm " + alarm.getKey() + " alarm nextDue date: "+alarm.getNextDueTime()); //TWX #2115 
      if (alarm.isAbsolute())
      {
        //       DO NOTHING
      }
      else
      {
        List missedAlarmList= new AlarmCaculator(alarm).caculateMissedAlarm(now);
        Object[] missedAlarmArray= missedAlarmList.toArray();

        Object[] value= missedAlarmArray;
        sendAlarmMsgWithProperty(alarm, JMSSender.instance().missedDest, MISSED, value);
      }
      alarmObj.setData(alarm);

      /*031027NSL do not re-schedule any alarm here, LoadTask would take care of re-scheduling
      alarm= (iCalAlarm) alarmObj.getData();
      Date dueDate= alarm.getNextDueTime();
      if (dueDate != null)
      {
        //        Object[] value= new Object[] { ADD, alarm };
        //        sendMsgTo(value, _sessionCtx);
        Log.debug(
          LogCat,
          "call addAlarm for the repeated Alarm" + alarm.getKey() + " at " + dueDate);
        sendAddAlarmMsg(alarm);
      }
      */
    }
    catch (Throwable e)
    {
      Log.error(ILogErrorCodes.TIME_ALARM_MISSED_ERROR,
                LogCat, "[iCalTimeMgrBean.alarmMissed] Error while missed alarm: "+e.getMessage(), e);
    }
  }

  //private methods
  private void sendAddAlarmMsg(iCalAlarm alarm) throws Exception
  {
    Long alarmUid= (Long) alarm.getKey();
    Date dueTime= alarm.getNextDueTime();
    if (dueTime == null)
      return;
//    Object[] value= new Object[] { ADD, alarmUid, dueTime };
//    sendMsgTo(value, _sessionCtx);
     Alarm.instance().addAlarm(alarmUid, dueTime);
  }

  private void sendUpdateAlarmMsg(iCalAlarm alarm) throws Exception
  {
    Long alarmUid= (Long) alarm.getKey();
    Date dueTime= alarm.getNextDueTime();
//    Object[] value= new Object[] { UPDATE, alarmUid, dueTime };
//    sendMsgTo(value, _sessionCtx);
		Alarm.instance().updateAlarm(alarmUid, dueTime);
  }
  /*
  private void sendMsgTo(Serializable value, EJBContext ctx)
  {
    JMSSender.instance().sendMsgTo(JMSSender.instance().scheduleTopic, value, ctx);
  }*/

  private void sendAlarmMsgWithProperty(
    iCalAlarm alarmEntity,
    Destination dest,
    Integer messageType,
    Serializable value)
  {
    String senderKey= alarmEntity.getSenderKey();
    String receiverKey= alarmEntity.getReceiverKey();
    String category= alarmEntity.getCategory();
    String taskId = alarmEntity.getTaskId();
    HashMap<String,String> propertyMap= new HashMap<String,String>();
    propertyMap.put(CATEGORY, category);
    propertyMap.put(SENDER_KEY, senderKey);
    propertyMap.put(RECEIVER_KEY, receiverKey);
    propertyMap.put(TASK_ID, taskId);
    Object[] additionalInfo=
      new Object[] { alarmEntity.getKey(), senderKey, receiverKey, category, taskId };
    Object[] res= new Object[] { messageType, value, additionalInfo };
    JMSSender.instance().sendMsgTo(dest, res, propertyMap, _sessionCtx);
  }

  private IiCalAlarmHome getAlarmHome() throws ServiceLookupException
  {
    if (_alarmHome == null)
      _alarmHome=
        (IiCalAlarmHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
          IiCalAlarmHome.class);
    return _alarmHome;
  }

  private IiCalEventHome getEventHome() throws ServiceLookupException
  {
    if (_eventHome == null)
      _eventHome=
        (IiCalEventHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
          IiCalEventHome.class);
    return _eventHome;
  }

  /**
   * Load the missed alarms which are due at the anchorTime
   * @param anchorTime Alarms due to trigger before this time but not triggered
   * are considered missed, and will be invoked.
   */
  public void loadMissedTasks(Date anchorTime, Date nextLoadTime)
  {
    Log.debug(LogCat, "Alarm's loadMissed called at " + new Date()+", retrieve scheduled task earlier than "+anchorTime);
    //load missed alarms
    try
    {
      anchorTime = iCalUtil.getTimeInSecod(anchorTime);
      
      nextLoadTime = iCalUtil.getTimeInSecod(nextLoadTime);
      Alarm.instance().setNextLoadTime(nextLoadTime); //init the next loading time
      
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, iCalAlarm.NEXT_DUE_TIME, filter.getLessOperator(), anchorTime, false);
      Collection alarmList = getAlarmHome().findByFilter(filter);
      if (alarmList != null || !alarmList.isEmpty())
      {
        Iterator iter = alarmList.iterator();
        for (; iter.hasNext();)
        {
          IiCalAlarmObj alarmObj = (IiCalAlarmObj) iter.next();
          alarmMissed(alarmObj, anchorTime);
        }
      }
      Log.debug(LogCat, "loadMissedTasks() finised at " + new Date());
    }
    catch (Throwable ex)
    {
      Log.error(ILogErrorCodes.TIME_LOAD_TASK_ERROR, 
                LogCat, "[iCalTimeMgrBean.loadMissedTasks] Error while loading missed tasks: "+ex.getMessage(), ex);
    }
  }
  
  /**
   * Load tasks for alarm scheduling. The alarms due to trigger between the lastLoadTime and curLoadTime will be
   * scheduled.
   *  
   * @param lastLoadTime The starting time range
   * @param curLoadTime The ending time range
   */
  public void loadTasks(Date lastLoadTime, Date curLoadTime)
  {
    try
    {
      Date rangeStart = iCalUtil.getTimeInSecod(lastLoadTime);
      Date rangeEnd = iCalUtil.getTimeInSecod(curLoadTime) ;
      Log.debug(
        LogCat,
        "Load Alarm Task for [" + rangeStart + "--" + rangeEnd + "] at " + new Date());
      
      Alarm.instance().setNextLoadTime(rangeEnd);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, iCalAlarm.NEXT_DUE_TIME, filter.getGreaterOrEqualOperator(), rangeStart, false);
      filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.NEXT_DUE_TIME, filter.getLessOperator(), rangeEnd, false);

      Collection alarmList = getAlarmHome().findByFilter(filter);
      if (alarmList != null || !alarmList.isEmpty())
      {
        Iterator iter = alarmList.iterator();
        for (; iter.hasNext();)
        {
          IiCalAlarmObj alarmObj = (IiCalAlarmObj) iter.next();
          iCalAlarm alarm = (iCalAlarm) alarmObj.getData();
          Alarm.instance().addAlarm((Long)alarm.getKey(), alarm.getNextDueTime());
        }
      }
      Log.debug(LogCat, "loadTasks() exit !");
    }
    catch (Throwable ex)
    {
      Log.error(ILogErrorCodes.TIME_LOAD_TASK_ERROR ,LogCat, "[iCalTimeMgrBean.loadTasks] Error while loading tasks()", ex);
    }
  }
}
