package com.gridnode.pdip.base.time.entities.value;


public class iCalDurationV extends iCalValueV
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5002352442932283296L;
	protected int _intValue = 0;

  public iCalDurationV(int kind)
  {
    if (kind != iCalValueKind.ICAL_DURATION_VALUE)
      throw new IllegalArgumentException("Wrong Kind=" + kind);
    _kind = (short) kind;
  }

  public iCalDurationV(Integer value)
  {
    _kind = (short) iCalValueKind.ICAL_DURATION_VALUE;
    _intValue = value.intValue();
  }

  public void setIntValue(int value)
  {
    _intValue = value;
  }

  public int getIntValue()
  {
    return _intValue;
  }

  static final int ICAL_DURATION_VALUE = 1;
  static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[] { new ValueFieldInfo("_intValue", "int", ICAL_DURATION_VALUE)};

  public  ValueFieldInfo[] getFieldInfos()
  {
    return FieldInfo;
  }

  public iCalValueV parseStr(String in)
  {
    int value = 0;
    in = in.trim();
    char source[] = new char[in.length()];
    source = in.toCharArray();

    int index = 0;
    boolean negative = false;
    if (source[0] == '-')
    {
      negative = true;
    }

    index = in.indexOf('P');
    int weekId = in.indexOf('W', index);
    if (weekId != -1)
    {
      String weekStr = in.substring(index + 1, weekId);
      int week = Integer.parseInt(weekStr);
      value = week * 7 * 24 * 3600;
    }
    else
    {
      int dayId = in.indexOf('D', index);
      if (dayId != -1)
      {
        String dayStr = in.substring(index + 1, dayId);
        int day = Integer.parseInt(dayStr);
        value += day * 24 * 3600;
        index = dayId;
      }
      index++; //for 'T'
      int hourId = in.indexOf('H', index);
      if (hourId != -1)
      {
        String hourStr = in.substring(index + 1, hourId);
        int hour = Integer.parseInt(hourStr);
        value += hour * 3600;
        index = hourId;
      }
      int minId = in.indexOf('M', index);
      if (minId != -1)
      {
        String minStr = in.substring(index + 1, minId);
        int min = Integer.parseInt(minStr);
        value += min * 60;
        index = minId;
      }
      int secId = in.indexOf('S', index);
      if (secId != -1)
      {
        String secStr = in.substring(index + 1, secId);
        int sec = Integer.parseInt(secStr);
        value += sec;
      }
    }

    if (negative)
      value = -1 * value;
    setIntValue(value);
    return this;
  }

  public String toString()
  {
    int absValue = _intValue > 0 ? _intValue : -1 * _intValue;
    int sec = 0, min = 0, hour = 0, day = 0;
    sec = absValue % 60;
    absValue = absValue / 60;
    min = absValue % 60;
    absValue = absValue / 60;
    hour = absValue % 24;
    day = absValue / 24;

    String str = "";
    if (_intValue < 0)
      str += "-";
    str += "P";
    if (day > 0)
    {
      str += day;
      str += "D";
    }
    if(hour ==0 && min==0 && sec==0)
       return str;
    str += "T";
    if (hour > 0)
    {
      str += hour;
      str += "H";
    }
    if (min > 0)
    {
      str += min;
      str += "M";
    }
    if (sec > 0)
    {
      str += sec;
      str += "S";
    }
    return str;
  }

}
