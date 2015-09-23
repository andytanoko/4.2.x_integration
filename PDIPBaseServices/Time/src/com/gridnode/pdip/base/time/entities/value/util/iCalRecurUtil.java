// %1023788050574:com.gridnode.pdip.base.time.value.util%
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
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value.util;

import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.model.iCalComponent;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.framework.log.Log;

import sun.misc.Compare;
import sun.misc.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class iCalRecurUtil implements IFrenqency, IByRule
{
  public static final int MAX_YEARS = 50;
  static String LogCat = "iCalRecurUtil";

  static List generateSetMonthly(RecurData recur_data, CalObjTime occ)
  {
    int freq = MONTHLY;
    List occs = generateSetMonthlyBeforeHMS(recur_data, freq, occ, null);
    return generateSetHMS(recur_data, freq, occs);
  }

  static List generateSetMonthlyBeforeHMS(
    RecurData recur_data,
    int freq,
    CalObjTime occ,
    ByRuleFilterFn.FilterFn overrideByDayfilter)
  {
    ByRuleFilterFn.FilterFn byDayFilterFn = overrideByDayfilter;
    if (byDayFilterFn == null)
      byDayFilterFn = ByRuleFilterFn.get(freq, BY_DAY);
    List occs = new ArrayList();
    occs.add(occ);
    occs = ByRuleFilterFn.get(freq, BY_MONTH).cal(recur_data, occs);
    if (!recur_data.isEmpty(BY_MONTH_DAY) && !recur_data.isEmpty(BY_DAY))
    {
      List occs2 = occs;
      occs = ByRuleFilterFn.get(freq, BY_MONTH_DAY).cal(recur_data, occs);
      occs2 = byDayFilterFn.cal(recur_data, occs2);
      occs.addAll(occs2);
    }
    else
    {
      occs = ByRuleFilterFn.get(freq, BY_MONTH_DAY).cal(recur_data, occs);
      occs = byDayFilterFn.cal(recur_data, occs);
    }
    return occs;
  }

  static List generateSetHMS(RecurData recur_data, int freq, List occs)
  {
    for (int rule = BY_HOUR; rule >= BY_SECOND; rule--)
      occs = ByRuleFilterFn.get(freq, rule).cal(recur_data, occs);
    return occs;
  }

  static List generateSetDefault(RecurData recur_data, int freq, CalObjTime occ)
  {
    List occs = new ArrayList();
    occs.add(occ);
    for (int rule = BY_MONTH; rule >= BY_SECOND; rule--)
    {
      ByRuleFilterFn.FilterFn filterFn = null;
      try
      {
        filterFn = ByRuleFilterFn.get(freq, rule);
      }
      catch (IllegalArgumentException t)
      {
        continue;
      }
      occs = filterFn.cal(recur_data, occs);
    }
    return occs;
  }

  static List generateSetYearly(RecurData recur_data, CalObjTime occ)
  {
    List[] occsArrays = new List[4];
    int numOccsArrays = 0;
    List occs = null;

    /* We assume that if BYMONTH or BYWEEKNO is used, then
    the BYDAY modifier applies to those, else it is applied
    independantly.*/
    int freq = YEARLY;
    if (!recur_data.isEmpty(BY_MONTH))
    {
      occs = generateSetMonthlyBeforeHMS(recur_data, freq, occ, ByRuleFilterFn.byDayExpandMonthly);
      occsArrays[numOccsArrays++] = occs;
    }
    if (!recur_data.isEmpty(BY_WEEK_NO))
    {
      occs = new ArrayList();
      occs.add(occ);
      occs = ByRuleFilterFn.get(freq, BY_WEEK_NO).cal(recur_data, occs);
      occs = ByRuleFilterFn.byDayExpandWeekly.cal(recur_data, occs);
      occsArrays[numOccsArrays++] = occs;
    }
    if (!recur_data.isEmpty(BY_YEAR_DAY))
    {
      occs = new ArrayList();
      occs.add(occ);
      occs = ByRuleFilterFn.get(freq, BY_YEAR_DAY).cal(recur_data, occs);
      occsArrays[numOccsArrays++] = occs;
    }

    /* If BYMONTHDAY is set, and BYMONTH is not set, we need to
    expand BYMONTHDAY independantly. */
    if (!recur_data.isEmpty(BY_MONTH_DAY) && recur_data.isEmpty(BY_MONTH))
    {
      occs = new ArrayList();
      occs.add(occ);
      occs = ByRuleFilterFn.get(freq, BY_MONTH_DAY).cal(recur_data, occs);
      occsArrays[numOccsArrays++] = occs;
    }

    /* If BYDAY is set, and BYMONTH and BYWEEKNO are not set, we need to
    expand BYDAY independantly. */
    if (!recur_data.isEmpty(BY_DAY)
      && recur_data.isEmpty(BY_MONTH)
      && recur_data.isEmpty(BY_WEEK_NO))
    {
      occs = new ArrayList();
      occs.add(occ);
      occs = ByRuleFilterFn.get(freq, BY_DAY).cal(recur_data, occs);
      occsArrays[numOccsArrays++] = occs;
    }

    /* Add all the arrays together. If no filters were used we just
    create an array with one element. */
    if (numOccsArrays > 0)
    {
      occs = occsArrays[0];
      for (int i = 1; i < numOccsArrays; i++)
      {
        List occs2 = occsArrays[i];
        occs.addAll(occs2);
      }
    }
    else
    {
      occs = new ArrayList();
      occs.add(occ);
    }
    return generateSetHMS(recur_data, freq, occs);
  }

  /* Returns an unsorted List of CalObjTimes resulting from expanding the
   given recurrence rule within the given interval. Note that it doesn't
   clip the generated occurrences to the interval, i.e. if the interval
   starts part way through the year this function still returns all the
   occurrences for the year. Clipping is done later.
   The finished flag is set to false if there are more occurrences to generate
   after the given interval.*/
  static boolean expandRecurrence(
    CalObjTime event_start,
    iCalRecurrenceV recur,
    CalObjTime intervalStart,
    CalObjTime intervalEnd,
    CalObjTime ruleEndTime,
    List allOccs)
  {
    boolean finished = true;
    RecurData recurData = new RecurData(recur, event_start);
    if (ruleEndTime != null && ruleEndTime.compareTo(intervalStart) < 0)
    {
      return true;
    }
    // Set finished to FALSE if we know there will be more occurrences to do after this interval. */
    if (intervalEnd == null || ruleEndTime == null || ruleEndTime.compareTo(intervalEnd) > 0)
      finished = false;
    CalObjTime[] occarray = new CalObjTime[1];

    /* Get the first period based on the frequency and the interval that
    intersects the interval between start and end. */
    if (ByFreqFindFn
      .getStart(recur.getFrequency())
      .run(event_start, ruleEndTime, recurData, intervalStart, intervalEnd, occarray))
      return finished;
    List occs = null;

    /* Loop until the event ends or we go past the end of the required
    interval. */
    for (;;)
    {
      switch (recur.getFrequency())
      {

        case YEARLY :
          occs = generateSetYearly(recurData, (CalObjTime) occarray[0].clone());
          break;

        case MONTHLY :
          occs = generateSetMonthly(recurData, (CalObjTime) occarray[0].clone());
          break;

        default :
          occs =
            generateSetDefault(recurData, recur.getFrequency(), (CalObjTime) occarray[0].clone());
          break;
      }

      /* Sort the occurrences and remove duplicates. */
      occs = quicksort(occs, CalObjTimeCompare);
      occs = removeDuplicateAndInvalidTimes(occs);

      /* Remove any occs after event_end. */
      int len = occs.size() - 1;
      if (ruleEndTime != null)
      {
        while (len >= 0)
        {
          CalObjTime cotime = (CalObjTime) occs.get(len);
          if (cotime.compareTo(ruleEndTime) <= 0)
            break;
          occs.remove(len);
          len--;
        }
      }
      // Apply the BYSETPOS property.
      occs = ByRuleFilterFn.bySetposFilter.cal(recurData, occs);

      /* Add the occurrences onto the main array. */
      if (len >= 0)
        allOccs.addAll(occs);
      if (ByFreqFindFn
        .getNext(recur.getFrequency())
        .run(occarray[0], ruleEndTime, recurData, intervalEnd))
        break;
    }
    return finished;
  }

  /* This looks up the occurrence time in the sorted rdate_periods array, and
   tries to compute the end time of the occurrence. If no end time or duration
   is set it returns FALSE and the default duration will be used. */
  static boolean getRecurDateEnd(CalObjTime occ, List recurDatePeriods)
  {
    int lower;
    int upper;
    int middle;
    int cmp = 0;
    lower = 0;
    upper = recurDatePeriods.size();
    //Date start = null; 
    Date end = null;
    CalRecurDate rdate = null;
    while (lower < upper)
    {
      middle = (lower + upper) >> 1;
      rdate = (CalRecurDate) recurDatePeriods.get(middle);
      cmp = occ.compareTo(rdate._start);
      if (cmp == 0)
        break;
      else if (cmp < 0)
        upper = middle;
      else
        lower = middle + 1;
    }
    if (cmp == 0)
    {
      Log.warn(LogCat, "Recurrence date not found");
      return false;
    }
    //Long duration = null;
    iCalPeriodV p = rdate._period;
    if (p.getType() == iCalPeriodV.CAL_COMPONENT_PERIOD_DATETIME)
    {
      end = p.getEndTime();
      occ.copy(new CalObjTime(end));
      occ.flags = false;
    }
    else
    {
      occ.addSecond(p.getDuration());
    }
    return true;
  }

  /**
   * Generates one year's worth of recurrence instances.  Returns TRUE if all
   * the callback invocations returned TRUE, or FALSE when any one of them
   * returns FALSE, i.e. meaning that the instance generation should be
   * stopped. This should only output instances whose start time is between
   * chunkStart and chunkEnd (inclusive), or we may generate duplicates
   * when we do the next chunk. (This applies mainly to weekly recurrences,
   * since weeks can span 2 years.) It should also only output instances that
   * are on or after the event's DTSTART property and that intersect the
   * required interval, between intervalStart and intervalEnd.
   */
  boolean generateInstancesForChunk(CalObjTime chunkStart, CalObjTime chunkEnd)
  {
    boolean finished = true;
    List occs = new ArrayList();
    List exOccs = new ArrayList();

    /* The original DTSTART property is included in the occurrence set, but not if we are just generating occurrences for a single rule. */
    if (!_single_rule)
    {
      if (_comp_starttime.compareTo(chunkEnd) >= 0)
        finished = false;
      else if (_comp_starttime.compareTo(chunkStart) >= 0)
        occs.add(_comp_starttime);
    }
    if (_rrules != null)
    {
      int size = _rrules.size();
      for (int i = 0; i < size; i++)
      {
        iCalPropertyV prop = (iCalPropertyV) _rrules.get(i);
        iCalRecurrenceV recur = (iCalRecurrenceV) prop.getValue();
        List tmp_occs = new ArrayList();
        Date recurEndDt = recur.getUntil();
        if (recurEndDt == null)
        {
          recurEndDt = getRuleEndDate(prop);
        }
        CalObjTime ruleEndTime = null;
        if (recurEndDt != null)
          ruleEndTime = new CalObjTime(recurEndDt);
        finished =
          finished
            && expandRecurrence(_comp_starttime, recur, chunkStart, chunkEnd, ruleEndTime, tmp_occs);
        occs.addAll(tmp_occs);
      }
    }

    /* Add on specific RDATE occurrence dates. If they have an end time
    or duration set, flag them as RDATEs, and store a reference to the
    period in the rdate_periods array. Otherwise we can just treat them
    as normal occurrences. */
    List rdate_periods = new ArrayList();
    if (_rdates != null)
    {
      int size = _rdates.size();
      for (int i = 0; i < size; i++)
      {
        Date start = null;
        iCalPropertyV prop = (iCalPropertyV) _rdates.get(i);
        iCalValueV value = prop.getValue();
        iCalParameterV valueType = prop.getParam(iCalParameterKind.ICAL_VALUE_PARAMETER);
        if (valueType == null
          || !valueType.getValue().equals(new Integer(iCalValueKind.ICAL_PERIOD_VALUE)))
        {
          start = ((iCalDateV) value).getTime();
        }
        else
        {
          start = ((iCalPeriodV) value).getStart();
        }
        CalObjTime cotime = new CalObjTime(start);
        cotime.flags = false;

        /* If the cotime is after the current chunk we set finished to false, and we skip it. */
        if (cotime.compareTo(chunkEnd) >= 0)
        {
          finished = false;
          continue;
        }
        if (valueType != null
          && valueType.getValue().equals(new Integer(iCalValueKind.ICAL_PERIOD_VALUE)))
        {
          cotime.flags = true;
          CalRecurDate rdate = new CalRecurDate(cotime, (iCalPeriodV) value);
          rdate_periods.add(rdate);
        }
        occs.add(cotime);
      }
    }
    _genCompleted = finished;

    if (_exrules != null)
    {
      int size = _exrules.size();
      for (int i = 0; i < size; i++)
      {
        iCalPropertyV prop = (iCalPropertyV) _exrules.get(i);
        iCalRecurrenceV recur = (iCalRecurrenceV) prop.getValue();
        List tmp_occs = new ArrayList();
        Date recurEndDt = recur.getUntil();
        if (recurEndDt == null)
        {
          recurEndDt = getRuleEndDate(prop);
        }
        CalObjTime ruleEndTime = null;
        if (recurEndDt != null)
          ruleEndTime = new CalObjTime(recurEndDt);
        expandRecurrence(_comp_starttime, recur, chunkStart, chunkEnd, ruleEndTime, tmp_occs);
        exOccs.addAll(tmp_occs);
      }
    }

    /* Add on specific exception dates. */
    if (_exdates != null)
    {
      int size = _exdates.size();
      for (int i = 0; i < size; i++)
      {
        iCalPropertyV prop = (iCalPropertyV) _exdates.get(i);
        iCalDateV value = (iCalDateV) prop.getValue();
        Date start = value.getTime();
        CalObjTime cotime = new CalObjTime(start);
        boolean isDate = value.getIsDate();
        if (isDate)
        {
          cotime.hour = 0;
          cotime.minute = 0;
          cotime.second = 0;
          cotime.flags = true;
        }
        else
        {
          cotime.flags = false;
        }
        exOccs.add(cotime);
      }
    }
    occs = quicksort(occs, CalObjTimeCompare);
    exOccs = quicksort(exOccs, CalObjTimeCompare);
    rdate_periods = quicksort(rdate_periods, CalRecurDateCompare);
    occs = removeExceptionTimes(occs, exOccs);
    int size = occs.size();
    for (int i = 0; i < size; i++)
    {
      CalObjTime occ = (CalObjTime) occs.get(i);
      Date start_time = occ.toDate();
      if (iCalUtil.beforeBySecond(start_time, _comp_startdt)
        || occ.compareTo(chunkStart) < 0
        || occ.compareTo(chunkEnd) > 0)
      {
        Log.warn(
          LogCat,
          "Time generated is before start time! "
            + "occurence="
            + start_time
            + ";comp_startdt="
            + _comp_startdt
            + ";chunkStart="
            + chunkStart
            + ";chunkEnd="
            + chunkEnd);
        continue;
      }
      CalObjTime endOcc = (CalObjTime) occ.clone();
      if (!endOcc.flags || !getRecurDateEnd(endOcc, rdate_periods))
      {
        endOcc.addSecond(_duration_seconds);
      }
      Date end_time = endOcc.toDate();
      if (iCalUtil.beforeBySecond(end_time, _startDt))
      {
        Log.warn(LogCat, "End Time generated is before start time!");
        continue;
      }
      if (!_callbackFn.onValue(start_time, end_time))
        return false;
    }
    return !finished;
  }

  static class CalRecurDate
  {
    CalObjTime _start;
    iCalPeriodV _period;

    CalRecurDate(CalObjTime start, iCalPeriodV period)
    {
      _start = start;
      _period = period;
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void genInstances()
  {
    _comp_startdt = _comp.getStartDt();
    Date comp_dtend = _comp.getSureEndDt();
    if (_comp_startdt == null)
    {
      Log.warn(
        LogCat,
        "cal_recur_generate_instances_of_rule(): bogus "
          + "component, does not have DTSTART.  Skipping...");
      return;
    }
    if (_startDt == null)
      _startDt = _comp_startdt;
    if (comp_dtend == null)
    {
      comp_dtend = (Date) _comp_startdt.clone();
    }
    Boolean isDate = _comp.getIsDateType();
    if (isDate != null && isDate.booleanValue())
    {
      CalObjTime starttime = new CalObjTime(_comp_startdt);
      CalObjTime endtime = new CalObjTime(comp_dtend);
      if (starttime.compareDate(endtime) == 0)
      {
        endtime.addDays(1);
        comp_dtend = new CalObjTime(endtime.year, endtime.month, endtime.date, 0, 0, 0).toDate();
      }
    }

    /* If there is no recurrence, just call the callback if the event
    intersects the given interval. */
    if (_comp.hasNoRecurrence())
    {
      if ((_endDt == null || iCalUtil.beforeBySecond(_comp_startdt, _endDt))
        && iCalUtil.afterBySecond(comp_dtend, _startDt))
      {
        _callbackFn.onValue(_comp_startdt, comp_dtend);
        _genCompleted = true;
        return;
      }
    }

    /* If a specific recurrence rule is being used, set up a simple list,
    else get the recurrence rules from the component. */
    if (_prop != null)
    {
      _single_rule = true;
      _rrules = new ArrayList();
      _rrules.add(_prop);
    }
    else
    {
      _single_rule = false;
      _rrules = _comp.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
      _rdates = _comp.getPropertyList(iCalPropertyKind.ICAL_RDATE_PROPERTY);
      _exrules = _comp.getPropertyList(iCalPropertyKind.ICAL_EXRULE_PROPERTY);
      _exdates = _comp.getPropertyList(iCalPropertyKind.ICAL_EXDATE_PROPERTY);
      //        /* Make sure all the enddates for the rules are set. */
      ensureRuleListEndDate(_rrules, false);
      ensureRuleListEndDate(_exrules, false);
    }
    CalObjTime intervalStart = new CalObjTime(_startDt);
    CalObjTime intervalEnd = null;
    if (_endDt != null)
      intervalEnd = new CalObjTime(new Date(_endDt.getTime() - 1000));
    _comp_starttime = new CalObjTime(_comp_startdt);
    CalObjTime event_end = new CalObjTime(comp_dtend);
    _duration_seconds = CalObjTime.computeDuration(_comp_starttime, event_end);
    if (iCalUtil.afterBySecond(_startDt, _comp_startdt))
    {
      intervalStart.addSecond(-_duration_seconds);
    }
    int minFreq = getMinimalFreqency();
    genInYearChunk(intervalStart, intervalEnd, minFreq);

  }  

  int getMinimalFreqency()
  {
    if (_rrules == null || _rrules.isEmpty())
      return IFrenqency.YEARLY;
   
    int minfreq = IFrenqency.YEARLY;
    int size = _rrules.size();
    for (int i = 0; i < size; i++)
    {
      iCalPropertyV prop = (iCalPropertyV) _rrules.get(i);
      iCalRecurrenceV recur = (iCalRecurrenceV) prop.getValue();
      int freq = recur.getFrequency();
      if(freq <minfreq)
        minfreq = freq;
    }
    return minfreq;
  }

  void genInYearChunk(CalObjTime intervalStart, CalObjTime intervalEnd, int minFreq)
  {
    CalObjTime chunkStart;
    CalObjTime chunkEnd;
    int maxyear = intervalStart.year + MAX_YEARS;
    for (int year = intervalStart.year;
      (_endDt == null && year <= maxyear) || (_endDt != null && year <= intervalEnd.year);
      year++)
    {
      chunkStart = (CalObjTime) intervalStart.clone();
      chunkStart.year = year;
      if (_endDt != null)
        chunkEnd = (CalObjTime) intervalEnd.clone();
      else
        chunkEnd = new CalObjTime();

      chunkEnd.year = year;
      if (year != intervalStart.year)
      {
        chunkStart.month = 0;
        chunkStart.date = 1;
        chunkStart.hour = 0;
        chunkStart.minute = 0;
        chunkStart.second = 0;
      }
      if (_endDt == null || year != intervalEnd.year)
      {
        chunkEnd.month = 11;
        chunkEnd.date = 31;
        chunkEnd.hour = 23;
        chunkEnd.minute = 59;
        chunkEnd.second = 61;
        chunkEnd.flags = false;
      }
     if(minFreq >= IFrenqency.WEEKLY) 
     {
      if (!generateInstancesForChunk(chunkStart, chunkEnd))
        break;
     }
     else
     {
       if (!genInMonthChunk(chunkStart, chunkEnd, minFreq))
        break;
     }
    }
  }

  boolean genInMonthChunk(CalObjTime intervalStart, CalObjTime intervalEnd,  int minFreq)
  {
    CalObjTime chunkStart;
    CalObjTime chunkEnd;
    for (int month = intervalStart.month;
      (month <= intervalEnd.month);
       month++)
    {
      chunkStart = (CalObjTime) intervalStart.clone();
      chunkStart.month = month;

      chunkEnd = (CalObjTime) intervalEnd.clone();
      chunkEnd.month = month;
      if (month != intervalStart.month)
      {
        chunkStart.date = 1;
        chunkStart.hour = 0;
        chunkStart.minute = 0;
        chunkStart.second = 0;
      }
      if (month != intervalEnd.month)
      {
        chunkEnd.date = CalObjTime.daysInMonth(chunkEnd.year, chunkEnd.month);
        chunkEnd.hour = 23;
        chunkEnd.minute = 59;
        chunkEnd.second = 61;
        chunkEnd.flags = false;
      }
     if(minFreq >= IFrenqency.HOURLY)  
     {
       if (!generateInstancesForChunk(chunkStart, chunkEnd))
        return false;
     }
     else
     {
       if (!genInDailyChunk(chunkStart, chunkEnd, minFreq))
        return false;
     }  
    }
    return true;
  }


  boolean genInDailyChunk(CalObjTime intervalStart, CalObjTime intervalEnd,  int minFreq)
  {
    CalObjTime chunkStart;
    CalObjTime chunkEnd;
    for (int day = intervalStart.date;
      (day <= intervalEnd.date);
       day++)
    {
      chunkStart = (CalObjTime) intervalStart.clone();
      chunkStart.date = day;

      chunkEnd = (CalObjTime) intervalEnd.clone();
      chunkEnd.date = day;
      if (day != intervalStart.date)
      {
        chunkStart.hour = 0;
        chunkStart.minute = 0;
        chunkStart.second = 0;
      }
      if (day != intervalEnd.date)
      {
        chunkEnd.hour = 23;
        chunkEnd.minute = 59;
        chunkEnd.second = 61;
        chunkEnd.flags = false;
      }
     if (!generateInstancesForChunk(chunkStart, chunkEnd))
       return false;
    }
    return true;
  }
  /**
   * Creates a new iCalRecurUtil object.
   * 
   * @param comp DOCUMENT ME!
   * @param prop DOCUMENT ME!
   * @param startDt DOCUMENT ME!
   * @param endDt DOCUMENT ME!
   * @param callbackFn DOCUMENT ME!
   */
  public iCalRecurUtil(
    iCalComponent comp,
    iCalPropertyV prop,
    Date startDt,
    Date endDt,
    IRecurCB callbackFn)
  {
    this._comp = comp;
    this._prop = prop;
    this._startDt = startDt;
    this._endDt = endDt;
    this._callbackFn = callbackFn;
    Log.debug(LogCat, "Constructor called with startDt= " +  startDt +";endDt="+ endDt);
  }

  private iCalComponent _comp;
  private iCalPropertyV _prop;
  private Date _startDt;
  private Date _endDt;
  private IRecurCB _callbackFn;
  //values used in computing
  private boolean _single_rule;
  private int _duration_seconds;
  private CalObjTime _comp_starttime;
  private Date _comp_startdt;
  private List _rrules = null;
  private List _rdates = null;
  private List _exrules = null;
  private List _exdates = null;
  private boolean _genCompleted = false;

  public boolean isGenCompleted()
  {
    return _genCompleted;
  }

  static class EnsureEndDateFn implements IRecurCB
  {
    EnsureEndDateFn(int count)
    {
      this.count = count;
    }

    int count;
    int instances;
    Date endDate;

    public boolean onValue(Date instance_start, Date instance_end)
    {
      instances++;
      if (instances == count)
      {
        endDate = instance_start;
        Log.debug(LogCat, "EndDate gened is " + endDate);
        return false;
      }
      return true;
    }
  }
  boolean ensureRuleListEndDate(List ruleList, boolean refresh)
  {
    boolean changed = false;
    if (ruleList != null)
    {
      int size = ruleList.size();
      for (int i = 0; i < size; i++)
      {
        iCalPropertyV prop = (iCalPropertyV) ruleList.get(i);
        changed |= ensureRuleEndDate(prop, refresh);
      }
    }
    return changed;
  }

  boolean ensureRuleEndDate(iCalPropertyV prop, boolean refresh)
  {
    iCalRecurrenceV rule = (iCalRecurrenceV) prop.getValue();
    if (rule.getCount() == 0)
      return false;
    if (!refresh)
    {
      if (getRuleEndDate(prop) != null)
        return false;
    }
    EnsureEndDateFn cbFn = new EnsureEndDateFn(rule.getCount());
    iCalRecurUtil util = new iCalRecurUtil(_comp, prop, null, null, cbFn);
    util.genInstances();
    setRuleEndDate(prop, cbFn.endDate);
    return true;
  }

  static Date getRuleEndDate(iCalPropertyV prop)
  {
    iCalParameterV param = prop.getParam(iCalParameterKind.ICAL_X_ENDDATE_PARAMETER);
    if (param != null)
    {
      iCalDateV dateV = (iCalDateV) param.getValue();
      return dateV.getTime();
    }
    return null;
  }

  static void setRuleEndDate(iCalPropertyV prop, Date endDate)
  {
    iCalDateV dateV = new iCalDateV(true, endDate);
    iCalParameterV param = prop.getParam(iCalParameterKind.ICAL_X_ENDDATE_PARAMETER);
    if (param != null)
    {
      param.setValue(dateV);
      return;
    }
    param = new iCalParameterV((short) iCalParameterKind.ICAL_X_ENDDATE_PARAMETER);
    param.setValue(dateV);
    prop.addParam(param);
  }

  static Compare CalObjTimeCompare = new Compare()
  {
    public int doCompare(Object obj1, Object obj2)
    {
      return ((CalObjTime) obj1).compareTo((CalObjTime) obj2);
    }
  };
  static Compare CalRecurDateCompare = new Compare()
  {
    public int doCompare(Object obj1, Object obj2)
    {
      return ((CalRecurDate) obj1)._start.compareTo(((CalRecurDate) obj2)._start);
    }
  };

  /**
   * DOCUMENT ME!
   * 
   * @param input DOCUMENT ME!
   * @param compare DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public static List quicksort(List input, Compare compare)
  {
    if (input.isEmpty())
      return input;
    Object[] inputarray = input.toArray();
    Sort.quicksort(inputarray, 0, inputarray.length - 1, compare);
    List res = new ArrayList(inputarray.length - 1);
    for (int i = 0; i < inputarray.length; i++)
      res.add(inputarray[i]);
    return res;
  }

  /* Removes the exceptions from the exOccs array from the occurrences in the
   occs array, and removes any duplicates. Both arrays are sorted. */
  static List removeExceptionTimes(List occs, List exOccs)
  {
    List res = new ArrayList();
    CalObjTime occ;
    CalObjTime prevOcc = null;
    CalObjTime exOcc = null;
    CalObjTime lastOccKept;
    boolean keepOcc;
    boolean isCurTimeException = false;
    if (occs.isEmpty())
      return res;
    int exIndex = 0;
    int occsLen = occs.size();
    int exOccsLen = exOccs.size();
    if (exOccsLen > 0)
      exOcc = (CalObjTime) exOccs.get(0);
    for (int i = 0; i < occsLen; i++)
    {
      occ = (CalObjTime) occs.get(i);
      keepOcc = true;
      // If the occurrence is a duplicate of the previous one, skip it.
      if (prevOcc != null && occ.compareTo(prevOcc) == 0)
      {
        keepOcc = false;
        if (occ.flags && !isCurTimeException)
        {
          lastOccKept = (CalObjTime) res.get(res.size() - 1);
          lastOccKept.flags = true;
        }
      }
      else
      {
        isCurTimeException = false;
        if (exOcc != null)
        {
          while (exOcc != null)
          {
            int cmp = 0;
            // If the exception is an EXDATE with a DATE value, we only have to compare the date.
            if (exOcc.flags)
              cmp = exOcc.compareDate(occ);
            else
              cmp = exOcc.compareTo(occ);
            if (cmp > 0)
              break;
            // Move to the next exception, or set exOcc to NULL when we reach the end of array.
            exIndex++;
            if (exIndex < exOccsLen)
              exOcc = (CalObjTime) exOccs.get(exIndex);
            else
              exOcc = null;
            if (cmp == 0)
            {
              isCurTimeException = true;
              keepOcc = false;
              break;
            }
          }
        }
      }
      if (keepOcc)
      {
        res.add(occ);
      }
      prevOcc = occ;
    }
    return res;
  }

  static List removeDuplicateAndInvalidTimes(List occs)
  {
    CalObjTime occ;
    CalObjTime prevOcc = null;
    int len;
    int i;
    //int j = 0;
    int year;
    int month;
    int days;
    //boolean keep_occ;
    List res = new ArrayList();
    len = occs.size();
    for (i = 0; i < len; i++)
    {
      occ = (CalObjTime) occs.get(i);
      if (prevOcc != null && occ.compareTo(prevOcc) == 0)
        continue;
      year = occ.year;
      month = occ.month;
      days = CalObjTime.daysInMonth(year, month);
      if (occ.date > days)
        continue;
      res.add(occ);
      prevOcc = occ;
    }
    return res;
  }
}