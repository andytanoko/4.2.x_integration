package com.gridnode.pdip.base.time.entities.helpers;

import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;

import java.util.Date;

public class iCalUtil
{
  static public iCalEvent createPseudoEvent(
    Date startDate,
    Date endDate,
    iCalRecurrenceV[] recurs,
    iCalRecurrenceV[] exceptRecurs)
  {
    iCalEvent event = new iCalEvent();
    event.setStartDt(startDate);
    event.setEndDt(endDate);
    if (recurs == null)
      return event;
    iCalPropertyV prop = null;
    iCalRecurrenceV recur = null;
    for (int i = 0; i < recurs.length; i++)
    {
      recur = recurs[i];
      prop = new iCalPropertyV((short) iCalPropertyKind.ICAL_RRULE_PROPERTY);
      prop.setValue(recur);
      event.addProperty(prop);
    }
    if (exceptRecurs == null)
      return event;
    for (int i = 0; i < exceptRecurs.length; i++)
    {
      recur = exceptRecurs[i];
      prop = new iCalPropertyV((short) iCalPropertyKind.ICAL_EXRULE_PROPERTY);
      prop.setValue(recur);
      event.addProperty(prop);
    }

    return event;
  }
  //used internally by the alarm module.
  
  public static boolean beforeBySecond(Date date1, Date date2)
  {
    long time1 = date1.getTime();
    time1 = time1/1000;
    long time2 = date2.getTime();
    time2 = time2/1000;
    return time1<time2;
  }
  
  public static boolean afterBySecond(Date date1, Date date2)
  {
    long time1 = date1.getTime();
    time1 = time1/1000;
    long time2 = date2.getTime();
    time2 = time2/1000;
    return time1>time2;
  }
  
  public static int compareBySecond(Date date1, Date date2)
  {
    long time1 = date1.getTime();
    time1 = time1/1000;
    long time2 = date2.getTime();
    time2 = time2/1000;
    return (time1<time2 ? -1 : (time1==time2 ? 0 : 1));
  }
  
  public static Date getTimeInSecod(Date date)
  {
    long time = date.getTime();
    time = time/1000;
    time = time*1000;
    return new Date(time);
  }
  

}
