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
 * Apr 03 2012    Tam Wei Xiang       #3496 - BYHOUR, BYTIME is not handled in
 *                                    method parse()
 */

package com.gridnode.pdip.base.time.entities.value;

import com.gridnode.pdip.base.time.entities.model.iCalInt;
import com.gridnode.pdip.base.time.entities.model.iCalValue;
import com.gridnode.pdip.framework.log.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class iCalRecurrenceV extends iCalValueV implements IByRule, IFrenqency, IEntityDAOs
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4124083658294884392L;

	static String LogCat = iCalRecurrenceV.class.getName();

  //date's assocType value def	
  protected int _frequency; //month , year, day, hour? IFrenqency
  protected int _interval = 1;
  //these two field can only choose one.
  protected int _count = 0;
  protected boolean _isDate = false;
  protected Date _until;

  protected int _weekStart = IWeekDay.MONDAY; //week start (defined in  IWeekday)
  protected List _bySecond; // 0..59
  protected List _byMinute; // 0..59
  protected List _byHour; // 0..23
  protected List _byDay; // (1..7(defined in  IWeekday),   +-1..53) format: -nMO
  protected List _byMonthDay; // +-1..31  
  protected List _byYearDay; //+-1..366
  protected List _byWeekNo; // +-1..53
  protected List _byMonth; //1..12
  protected List _bySetPos; //+-1..366Duration

  public iCalRecurrenceV(int kind)
  {
    this();
    if (kind != iCalValueKind.ICAL_RECUR_VALUE)
      throw new IllegalArgumentException();
  }

  public iCalRecurrenceV()
  {
    super(iCalValueKind.ICAL_RECUR_VALUE);
  }

  // ******************* get/set Methods******************
  public int getFrequency()
  {
    return _frequency;
  }
  public void setFrequency(int value)
  {
    _frequency = value;
  }
  public int getInterval()
  {
    return _interval;
  }
  public void setInterval(int value)
  {
    _interval = value;
  }
  public int getCount()
  {
    return _count;
  }
  public void setCount(int value)
  {
    _count = value;
  }
  public boolean getIsDate()
  {
    return _isDate;
  }
  public void setIsDate(boolean value)
  {
    _isDate = value;
  }
  public Date getUntil()
  {
    return _until;
  }
  public void setUntil(Date value)
  {
    _until = value;
  }
  public int getWeekStart()
  {
    return _weekStart;
  }
  public void setWeekStart(int value)
  {
    _weekStart = value;
  }
  public List getBySecond()
  {
    return _bySecond;
  }
  public void setBySecond(List value)
  {
    _bySecond = value;
  }
  public List getByMinute()
  {
    return _byMinute;
  }
  public void setByMinute(List value)
  {
    _byMinute = value;
  }
  public List getByHour()
  {
    return _byHour;
  }
  public void setByHour(List value)
  {
    _byHour = value;
  }
  public List getByDay()
  {
    return _byDay;
  }
  public void setByDay(List value)
  {
    _byDay = value;
  }
  public List getByMonthDay()
  {
    return _byMonthDay;
  }
  public void setByMonthDay(List value)
  {
    _byMonthDay = value;
  }
  public List getByYearDay()
  {
    return _byYearDay;
  }
  public void setByYearDay(List value)
  {
    _byYearDay = value;
  }
  public List getByWeekNo()
  {
    return _byWeekNo;
  }
  public void setByWeekNo(List value)
  {
    _byWeekNo = value;
  }
  public List getByMonth()
  {
    return _byMonth;
  }
  public void setByMonth(List value)
  {
    _byMonth = value;
  }
  public List getBySetPos()
  {
    return _bySetPos;
  }
  public void setBySetPos(List value)
  {
    _bySetPos = value;
  }

  static final int ICAL_RECUR_FREQ = 1;
  static final int ICAL_RECUR_INTERVAL = 2;

  static final int ICAL_RECUR_BYDAY = 15;

  static final ValueFieldInfo[] FieldInfo =
    new ValueFieldInfo[] {
      new ValueFieldInfo("_frequency", "int", ICAL_RECUR_FREQ),
      new ValueFieldInfo("_interval", "int", ICAL_RECUR_INTERVAL),
      new ValueFieldInfo("_count", "int", 3),
      new ValueFieldInfo("_isDate", "boolean", 4),
      new ValueFieldInfo("_until", "Date", 5),
      new ValueFieldInfo("_weekStart", "int", 6),
      new ValueFieldInfo("_bySecond", "int", 7, true),
      new ValueFieldInfo("_byMinute", "int", 8, true),
      new ValueFieldInfo("_byHour", "int", 9, true),
      new ValueFieldInfo("_byMonthDay", "int", 10, true),
      new ValueFieldInfo("_byYearDay", "int", 11, true),
      new ValueFieldInfo("_byWeekNo", "int", 12, true),
      new ValueFieldInfo("_byMonth", "int", 13, true),
      new ValueFieldInfo("_bySetPos", "int", 14, true)};

  public ValueFieldInfo[] getFieldInfos()
  {
    return FieldInfo;
  }

  void convertExtraFieldToValueEntity(List[] res)
  {
    if (_byDay == null || _byDay.isEmpty())
      return;
    int size = _byDay.size();
    for (int i = 0; i < size; i++)
    {
      DayEntry dayEntry = (DayEntry) _byDay.get(i);
      Integer value = new Integer(dayEntry.dayEntryToInt());
      iCalValue entity = new iCalInt(value);
      entity.setValueKind(new Short((short) ICAL_RECUR_BYDAY));
      res[0].add(entity);
    }
  }

  void setExtraValueEntity(List entityList)
  {
    _byDay = new ArrayList();
    int size = entityList.size();
    for (int i = 0; i < size; i++)
    {
      iCalValue valueEntity = (iCalValue) entityList.get(i);
      int valueKind = valueEntity.getValueKind().intValue();
      if (valueKind != ICAL_RECUR_BYDAY)
      {
        Log.warn(LogCat, "Wrong valueEntity with valueKind " + valueKind);
        continue;
      }
      if (!(valueEntity instanceof iCalInt))
      {
        Log.warn(LogCat, "ValueEntity should be of Type iCalInt for _byDay field " + valueEntity.getEntityDescr());
        continue;
      }
      Integer intValue = ((iCalInt) valueEntity).getIntValue();
      if (intValue == null)
        continue;
      int day = intValue.intValue();

      DayEntry dayEntry = new DayEntry(DayEntry.weekNo(day), DayEntry.dayOfWeek(day));
      _byDay.add(dayEntry);
    }

  }

  static class RecurMap
  {
    String str;
    int limit;
    public RecurMap(String astr, int alimit)
    {
      str = astr;
      limit = alimit;
    }
  }
  static RecurMap recurmap[] =
    new RecurMap[] {
      new RecurMap(";BYSECOND=", 60),
      new RecurMap(";BYMINUTE=", 60),
      new RecurMap(";BYHOUR=", 24),
      new RecurMap(";BYDAY=", 7),
      new RecurMap(";BYMONTHDAY=", 31),
      new RecurMap(";BYYEARDAY=", 366),
      new RecurMap(";BYWEEKNO=", 52),
      new RecurMap(";BYMONTH=", 12),
      new RecurMap(";BYSETPOS=", 366),
      };

  static class FreqMap
  {
    String str;
    int freq;
    public FreqMap(int afreq, String astr)
    {
      str = astr;
      freq = afreq;
    }
  }
  static FreqMap[] freqmap =
    new FreqMap[] {
      new FreqMap(IFrenqency.SECONDLY, "SECONDLY"),
      new FreqMap(IFrenqency.MINUTELY, "MINUTELY"),
      new FreqMap(IFrenqency.HOURLY, "HOURLY"),
      new FreqMap(IFrenqency.DAILY, "DAILY"),
      new FreqMap(IFrenqency.WEEKLY, "WEEKLY"),
      new FreqMap(IFrenqency.MONTHLY, "MONTHLY"),
      new FreqMap(IFrenqency.YEARLY, "YEARLY")};

  static String freq2Str(int freq)
  {
    for (int i = 0; i < freqmap.length; i++)
    {
      if (freqmap[i].freq == freq)
        return freqmap[i].str;
    }
    throw new IllegalArgumentException("Freq " + freq);
  }

  static int str2Freq(String str)
  {
    for (int i = 0; i < freqmap.length; i++)
    {
      if (freqmap[i].str.equals(str))
        return freqmap[i].freq;
    }
    throw new IllegalArgumentException("Freq " + str);
  }
  static class WeekMap
  {
    String str;
    int weekDay;
    public WeekMap(int aweekday, String astr)
    {
      str = astr;
      weekDay = aweekday;
    }
  }

  static WeekMap[] weekmap =
    new WeekMap[] {
      new WeekMap(IWeekDay.MONDAY, "MONDAY"),
      new WeekMap(IWeekDay.TUESDAY, "TUESDAY"),
      new WeekMap(IWeekDay.WEDNESDAY, "WEDNESDAY"),
      new WeekMap(IWeekDay.THURSDAY, "THURSDAY"),
      new WeekMap(IWeekDay.FRIDAY, "FRIDAY"),
      new WeekMap(IWeekDay.SATURDAY, "SATURDAY"),
      new WeekMap(IWeekDay.SUNDAY, "SUNDAY")};

  static String weekday2Str(int weekDay)
  {
    for (int i = 0; i < weekmap.length; i++)
    {
      if (weekmap[i].weekDay == weekDay)
        return weekmap[i].str;
    }
    throw new IllegalArgumentException("weekDay " + weekDay);
  }

  static int str2Weekday(String str)
  {
    for (int i = 0; i < weekmap.length; i++)
    {
      if (weekmap[i].str.equals(str))
        return weekmap[i].weekDay;
    }
    throw new IllegalArgumentException("weekDay " + str);
  }

  static String date2Str(Date date, boolean isDate)
  {
    SimpleDateFormat timeFormat = null;
    if (isDate)
      timeFormat = new SimpleDateFormat("yyyyMMdd");
    else
      timeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    timeFormat.setLenient(false);
    return timeFormat.format(date);
  }

  public String toString()
  {

    if (_frequency == NONE_FRENQENCY)
    {
      return null;
    }

    String str = "";
    str += "FREQ=" + freq2Str(_frequency);

    if (_until != null)
    {
      str += ";UNTIL=" + date2Str(_until, _isDate);
    }

    if (_count != 0)
    {
      str += ";COUNT=" + _count;
    }

    if (_interval != 1)
    {
      str += ";INTERVAL=" + _interval;
    }

    List[] byLists = new List[] { _bySecond, _byMinute, _byHour, _byDay, _byMonthDay, _byYearDay, _byWeekNo, _byMonth, _bySetPos };

    int size = recurmap.length;
    for (int i = 0; i < size; i++)
    {
      int limit = recurmap[i].limit;
      List byList = byLists[i];

      if (byList != null && byList.size() > 0)
      {
        str += recurmap[i].str;
        int count = byList.size();
        if (count > limit)
          count = limit;

        for (int j = 0; j < count; j++)
        {
          if (i == 3)
          { /* BYDAY */

            DayEntry dayEntry = (DayEntry) byList.get(j);

            String daystr = weekday2Str(dayEntry.getDay());
            int pos = dayEntry.getWeekNo();

            if (pos != 0)
              str += pos;
            str += daystr;
          }
          else
          {
            int day = ((Integer) byList.get(j)).intValue();
            str += day;
          }

          if (((j + 1) < count) && ((i + 1) < size))
          {
            str += ',';
          }
        }
      }
    }

    return str;
  }

  public iCalValueV parseStr(String valueStr)
  {
    iCalRecurrenceV recur = new iCalRecurrenceV(iCalValueKind.ICAL_RECUR_VALUE);
    recur.parse(valueStr);
    return recur;
  }
  public void parse(String valuestr)
  {
    List list = new ArrayList();
    if (valuestr.length() > 0)
    {
      StringTokenizer st = new StringTokenizer(valuestr, ";", false);
      while (st.hasMoreTokens())
      {
        String elem = st.nextToken();
        if (elem.length() > 0)
        {
          list.add(elem);
        }
      }
    }
    List[] byList = new List[9];

    int size = list.size();
    for (int i = 0; i < size; i++)
    {
      try
      {
        String elem = (String) list.get(i);
        int index = elem.indexOf('=');
        if (index > 0 && index < elem.length() - 1)
        {
          String name = elem.substring(0, index);
          String value = elem.substring(index + 1);
          if ("FREQ".equalsIgnoreCase(name))
          {
            this._frequency = str2Freq(value);
          }
          else if ("COUNT".equalsIgnoreCase(name))
          {
            this._count = Integer.parseInt(value);
          }
          else if ("UNTIL".equalsIgnoreCase(name))
          {
            iCalDateV date = (iCalDateV) new iCalDateV(iCalValueKind.ICAL_DATE_VALUE).parseStr(value);
            this._isDate = date.getIsDate();
            this._until = date.getTime();
          }
          else if ("INTERVAL".equalsIgnoreCase(name))
          {
            this._interval = Integer.parseInt(value);
          }
          else if ("WKST".equalsIgnoreCase(name))
          {
            this._weekStart = str2Weekday(value);
          }
          else if ("BYDAY".equalsIgnoreCase(name))
          {
            if (byList[3] == null)
              byList[3] = new ArrayList();
            byList[3].add(parseWeekArray(value));
          }
          else if("BYHOUR".equalsIgnoreCase(name)) //TWX 20120403 #3496
          {
            if(byList[2] == null)
            {
              byList[2] = new ArrayList();
            }
            
            Integer startHour = new Integer(value);
            byList[2].add(startHour);
            
          }
          else if("BYMINUTE".equalsIgnoreCase(name)) //TWX 20120403 #3496
          {
            if(byList[1] == null)
            {
              byList[1] = new ArrayList();
            }
            Integer startMinute = new Integer(value);
            byList[1].add(startMinute);
          }
          else
          {
            boolean matchfound = false;
            for (int num = 0; num < recurmap.length; num++)
            {
              if (num == 3)
                num++;
              if (recurmap[num].str.equalsIgnoreCase(name))
              {
                if (byList[num] == null)
                  byList[num] = new ArrayList();
                byList[num].add(parseIntArray(value));
                matchfound = true;
                break;
              }
            }
            if (!matchfound)
              Log.warn(LogCat, "iCalRecurrence.parseStr:" + name + "=" + value);
          }
        }
        else
        {
          Log.warn(LogCat, "iCalRecurrence.parseStr:" + elem);
        }
      }
      catch (Exception ex)
      {
        Log.warn(LogCat, "iCalRecurrence.parseStr:", ex);
      }

    }

    _bySecond = byList[0] != null ? byList[0] : null;
    _byMinute = byList[1] != null ? byList[1] : null;
    _byHour = byList[2] != null ? byList[2] : null;
    _byDay = byList[3] != null ? byList[3] : null;
    _byMonthDay = byList[4] != null ? byList[4] : null;
    _byYearDay = byList[5] != null ? byList[5] : null;
    _byWeekNo = byList[6] != null ? byList[6] : null;
    _byMonth = byList[7] != null ? byList[7] : null;
    _bySetPos = byList[8] != null ? byList[8] : null;
  }

  static abstract class ParseFunc
  {
    abstract Object run(String str);
  }

  static List parseIntArray(String listStr)
  {
    return parseValueList(listStr, new ParseFunc()
    {
      Object run(String str)
      {
        return new Integer(str);
      }
    });
  }
  static List parseWeekArray(String listStr)
  {
    return parseValueList(listStr, new ParseFunc()
    {
      Object run(String str)
      {
      	int weekno = 0;
      	int weekday = IWeekDay.MONDAY;
        int sign = 1;
        str = str.trim();
        int start=0, index = 0;

        if (str.charAt(0) == '-')
        {
          sign = -1;
          start++;
        }
        else if (str.charAt(0) == '+')
        {
          sign = 1;
          start++;
        }
        index = start;
        int size = str.length();
        for(; index<size; index++)
        {
        	if(!Character.isDigit(str.charAt(index)))
        	{
        		break;
        	}
        }
        if(index >start)
        {
         weekno = sign* Integer.parseInt(str.substring(start, index-1));
        }
        weekday = str2Weekday(str.substring(index));
        return new DayEntry(weekno, weekday );
      }
    });
  }

  static List parseValueList(String listStr, ParseFunc parseFunc)
  {
    List list = new ArrayList();
    if (listStr.length() > 0)
    {
      StringTokenizer st = new StringTokenizer(listStr, ",", false);
      while (st.hasMoreTokens())
      {
        String elem = st.nextToken();
        if (elem.length() > 0)
        {
          list.add(parseFunc.run(elem));
        }
        else
        {
          Log.warn(LogCat, "iCalRecurrence.parseIntArray:" + elem);
        }
      }
    }
    return list;
  }

}