package com.gridnode.pdip.base.time.entities.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.gridnode.pdip.base.time.entities.ejb.IiCalEventHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventObj;
import com.gridnode.pdip.base.time.entities.ejb.IiCalTodoHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalTodoObj;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalComponent;
import com.gridnode.pdip.base.time.entities.value.IRelated;
import com.gridnode.pdip.base.time.entities.value.util.IRecurCB;
import com.gridnode.pdip.base.time.entities.value.util.iCalRecurUtil;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class AlarmCaculator
{
  protected static final String LogCat = AlarmCaculator.class.getName();

  private iCalAlarm _alarm;
  private iCalComponent _parent;

  public AlarmCaculator(iCalAlarm value)
  {
    this(value, null);
  }

  public AlarmCaculator(iCalAlarm value, iCalComponent parent)
  {
    _alarm = value;
    _parent = parent;
  }

  /** Methods to caculate _alarm occurence time */
  public Date caculateNextDueTime()
  {
    Boolean isDisabled = _alarm.getDisabled();
    Date dueTime = null;
    if (Boolean.TRUE.equals(isDisabled))
      dueTime = null;
    else
    {
      boolean isAbsolute = _alarm.isAbsolute();
      if (isAbsolute)
      {
        dueTime = calculateAbsoluteDate();
      }
      else
      {
        dueTime = caculateRelativeDate();
      }
    }
    _alarm.setNextDueTime(dueTime);
    return dueTime;
  }

  Date calculateAbsoluteDate()
  {
    Integer count = _alarm.getCount();
    if (count == null || count.equals(new Integer(0)))
    {
      //the first time
      Date dueDt = _alarm.getStartDt();
      //assume it is the start date
      Date now = iCalUtil.getTimeInSecod(new Date());
      if (dueDt == null) //if the startDate is not set, use default one
      {
        _alarm.setStartDt(now);
        dueDt = _alarm.getStartDt();
      }
      else if (dueDt.before(now))
        dueDt = now;
      return dueDt;
    }
    Long delayPeriod = _alarm.getDelayPeriod();
    if (delayPeriod == null)
      return null;
    Integer repeat = _alarm.getRepeat();
    if (repeat == null || repeat.compareTo(count) >= 0)
    {
      long start = _alarm.getStartDt().getTime();
      long delay = delayPeriod.longValue() * 1000;
      long now = new Date().getTime();
      long times = (now + delay - start) / delay;
      long due = start + delay * times;
      return new Date(due);
    }
    return null;
  }

  Date caculateRelativeDate()
  {
    String recurListStr = _alarm.getRecurListStr();
    Date curDate = null;
    List dateList = null;
    if (recurListStr != null)
    {
      dateList = str2DateList(recurListStr);
    }
    else
    {
      Date dtStart = str2Date(_alarm.getCurRecur());
      dateList = genRecurAlarms(dtStart);
    }

    if (dateList == null || dateList.isEmpty())
      curDate = null;
    else
    {
      curDate = (Date) dateList.get(0);
      dateList.remove(0);
    }
    return setAlarmTime(dateList, curDate);
  }

  Date setAlarmTime(List dateList, Date curDate)
  {
    String recurListStr;
    if (dateList == null || dateList.isEmpty())
      recurListStr = null;
    else
      recurListStr = dateList2Str(dateList);
    _alarm.setRecurListStr(recurListStr);
    _alarm.setCurRecur(date2Str(curDate));
    Date dueTime = null;
    if (curDate != null)
      dueTime = getAbsoluteDueTime(curDate);
    return dueTime;
  }

  Date getAbsoluteDueTime(Date anchorTime)
  {
    long startDur = 0;
    if (_alarm.getStartDuration() != null)
      startDur = _alarm.getStartDuration().longValue() * 1000;
    Date dueTime = new Date(anchorTime.getTime() + startDur);
    return dueTime;
  }

  List genRecurAlarms(Date dtStart)
  {
    if (Boolean.TRUE.equals(_alarm.getIsRecurComplete()))
      return null;
    iCalComponent parent = getParent();
    if (parent == null)
    {
      Log.warn(LogCat, "Can not found _alarm's Parent" + _alarm);
      return null;
    }
    return genAlarmFromParent(parent, dtStart);
  } //periodStart is not counted in the result.
  List genAlarmFromParent(iCalComponent parent, Date periodStart)
  {
    boolean isRecurComplete = false;
    boolean fromStart = true;
    if (IRelated.END == _alarm.getRelated().intValue())
      fromStart = false;
    int maxRecur = MAX_RECUR;
    List resArray = new ArrayList();
    IRecurCB cbFn = new CallBackFn(maxRecur, fromStart, resArray);
    //int tryTime = 0; 
    iCalRecurUtil icalUtil = new iCalRecurUtil(parent, null, periodStart, null, cbFn);
    icalUtil.genInstances();
    isRecurComplete = icalUtil.isGenCompleted();
    //      if((resArray.isEmpty() && (!isRecurComplete) && (tryTime++ < MAX_RECUR_RETRY_NUM))
    //        dtStart.setYear(dtStart.getYear() + iCalRecurUtil.MAX_YEARS);
    //      else
    //        break;
    if (!resArray.isEmpty())
    {
    	/** incude the parent's start time is the include parent start time is set and it is after the period start*
    	 */
      if(Boolean.TRUE.equals(_alarm.getIncludeParentStartTime()))
      {
      	 Date compStartDt = parent.getStartDt();
      	 if(periodStart == null || iCalUtil.afterBySecond(compStartDt, periodStart))
      	 {
      	 	 if(!resArray.contains(compStartDt))
					  resArray.add(0, compStartDt);
      	 }
      }
      if (((Date) resArray.get(0)).equals(periodStart))
        resArray.remove(0);
    }
    Boolean completed = isRecurComplete ? Boolean.TRUE : Boolean.FALSE;
    _alarm.setIsRecurComplete(completed);
    return resArray;
  }

  iCalComponent getParent()
  {
    if (_parent != null)
      return _parent;
    try
    {
      //assume its parent is a iCalEvent .
      Long parentUid = _alarm.getParentUid();
      if (parentUid == null)
        return null;
      Short parentKind = _alarm.getParentKind();
      iCalComponent parent = null;
      if (iCalComponent.KIND_TODO.equals(parentKind))
      {
        IiCalTodoObj todoObj = getTodoHome().findByPrimaryKey(parentUid);
        if (todoObj != null)
          parent = (iCalComponent) todoObj.getData();
      }
      else if (iCalComponent.KIND_EVENT.equals(parentKind))
      {
        IiCalEventObj eventObj = getEventHome().findByPrimaryKey(parentUid);
        if (eventObj != null)
          parent = (iCalComponent) eventObj.getData();
      }
      return parent;
    }
    catch (Exception ex)
    {
      Log.warn(LogCat, "Exception occured in finding _alarm parent", ex);
    }
    return null;
  }

  static class CallBackFn implements IRecurCB
  {
    CallBackFn(int count, boolean fromStart, List resArray)
    {
      this.count = count;
      this.resArray = resArray;
    }

    int count;
    int instances;
    List resArray;
    boolean fromStart = true;
    public boolean onValue(Date instance_start, Date instance_end)
    {
      if (instances == count)
      {
        return false;
      }
      instances++;
      Date res = fromStart ? instance_start : instance_end;
      resArray.add(res);
      return true;
    }
  }

  private IiCalEventHome _eventHome = null;
  private IiCalTodoHome _todoHome = null;
  private IiCalEventHome getEventHome() throws ServiceLookupException
  {
    if (_eventHome == null)
      _eventHome =
        (IiCalEventHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
          IiCalEventHome.class);
    return _eventHome;
  }

  private IiCalTodoHome getTodoHome() throws ServiceLookupException
  {
    if (_todoHome == null)
      _todoHome =
        (IiCalTodoHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
          IiCalTodoHome.class);
    return _todoHome;
  }

  public List caculateMissedAlarm(Date now)
  {
    String recurListStr = _alarm.getRecurListStr();
    List dateList = null;
    Date nextDate = _alarm.getNextDueTime();
    List missedList = new ArrayList();
    if (nextDate != null)
      missedList.add(nextDate);
    if (recurListStr != null)
    {
      dateList = str2DateList(recurListStr);
    }
    else
    {
      dateList = genRecurAlarms(nextDate);
    }
    
    if (dateList != null && !dateList.isEmpty()) //twx 20090815
    {
      findMissedAlarm(dateList, missedList, now);
      while (dateList.isEmpty())
      {
        if (Boolean.TRUE.equals(_alarm.getIsRecurComplete()))
          break;
        Date lastRecur = (Date) missedList.get(missedList.size() - 1);
        dateList = genRecurAlarms(lastRecur);
        findMissedAlarm(dateList, missedList, now);
      }
      if (dateList.isEmpty())
        nextDate = null;
      else
        nextDate = (Date) dateList.get(dateList.size() - 1);
    }
    else
      nextDate = null;
    nextDate = setAlarmTime(dateList, nextDate);
    _alarm.setNextDueTime(nextDate);
    return missedList;
  }

  void findMissedAlarm(List dateList, List missedList, Date now)
  {
    int size = dateList.size();
    for (int i = 0; i < size; i++)
    {
      Date date = (Date) dateList.get(0);
      Date alarmTime = getAbsoluteDueTime(date);
      if (iCalUtil.beforeBySecond(alarmTime, now))
      {
        dateList.remove(0);
        missedList.add(date);
      }
      else
        return;
    }
  }

  static String dateList2Str(List dateList)
  {
    if (dateList == null || dateList.isEmpty())
      return null;
    int size = dateList.size();
    StringBuffer strBuf = new StringBuffer();
    strBuf.append(size + "[,");
    for (int i = 0; i < size; i++)
    {
      Date date = (Date) dateList.get(i);
      if (date != null)
        strBuf.append(date2Str(date));
      strBuf.append(",");
    }
    strBuf.append("]");
    return strBuf.toString();
  }

  public static List str2DateList(String str)
  {
    if (str == null)
      return new ArrayList();
    List res = new ArrayList();
    try
    {
      String subStr = str.trim();
      if ("".equals(subStr))
        return res;
      StringTokenizer tokenizer = new StringTokenizer(subStr, "[],");
      //int size = 
      Integer.parseInt(tokenizer.nextToken());
      while (tokenizer.hasMoreTokens())
      {
        String intervalStr = tokenizer.nextToken();
        Date date = str2Date(intervalStr);
        if (date != null)
          res.add(date);
      }
    }
    catch (Throwable ex)
    {
      Log.warn("base.time", "error in read str2DateList, str = " + str, ex);
    }
    return res;
  }

  public static Date str2Date(String str)
  {
    if (str == null)
      return null;
    Date res = null;
    try
    {
      String subStr = str.trim();
      if ("".equals(subStr))
        return res;
      res = df.parse(subStr);
    }
    catch (Throwable ex)
    {
      Log.warn("base.time", "error in read str2Date, str = " + str, ex);
    }

    return res;
  }
  public static String date2Str(Date date)
  {
    if (date == null)
      return null;
    return df.format(date);
  }

  public static final int MAX_RECUR = 50;
  public static final int MAX_RECUR_RETRY_NUM = 3;
  // try how many times before regard the alarm as finished
  static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
}
