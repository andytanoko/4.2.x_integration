package com.gridnode.pdip.base.time.entities.value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class iCalDateV extends iCalValueV
{ 
  // public static final int	CAL_COMPONENT_VALUE_DATETIME = 0;
  // public	static final int	CAL_COMPONENT_VALUE_DATE =1;

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8585003385220961936L;
	/* Period of time, can have explicit start/end times or start/duration instead */
  //	protected  boolean  _isDate =false;
  protected Date _time = null;

  public iCalDateV(int kind)
  {
    super(kind);
  }

  public iCalDateV(boolean isDate, Date date)
  {
    super(iCalValueKind.ICAL_DATE_VALUE);
    if (!isDate)
      _kind = (short) iCalValueKind.ICAL_DATETIME_VALUE;
    _time = date;
  }

  public boolean getIsDate()
  {
    return (_kind == iCalValueKind.ICAL_DATE_VALUE);
  }
  public Date getTime()
  {
    return _time;
  }

  static final int ICAL_DATE_VALUE = 1;
  static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[] { new ValueFieldInfo("_time", "Date", ICAL_DATE_VALUE)};

  public ValueFieldInfo[] getFieldInfos()
  {
    return FieldInfo;
  }
  static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");
  static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

  static {
    timeFormat.setLenient(false);
    dateTimeFormat.setLenient(false);
  }

  public iCalValueV parseStr(String in) throws ParseException
  {
    Date when = null;
    if (in.indexOf("T") == -1)
    {
      when = timeFormat.parse(in);
      return new iCalDateV(true, when);
    }
    else
    {
      if (in.indexOf("Z") != -1)
      {
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        in = in.substring(0, 15);
      }
      when = dateTimeFormat.parse(in);
      return new iCalDateV(false, when);
    }
  }

  public String toString()
  {
    if (getIsDate())
      return timeFormat.format(getTime());
    else
      return dateTimeFormat.format(getTime());
  }
}