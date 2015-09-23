// %1023788051715:com.gridnode.pdip.base.time.entities.value.util%
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

import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.framework.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class iCalRecurUtilTestCase
  extends TestCase
  implements IByRule
{
  static String LogCat = "iCalRecurUtilTestCase";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new iCalRecurUtilTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public iCalRecurUtilTestCase(String name)
  {
    super(name);
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public static Test suite()
  {
    return new TestSuite(iCalRecurUtilTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void testSortCal()
  {
    List list = genRandomTimeList(123456789, 10);
    Log.debug(LogCat, "List before sort is " + list);
    list = iCalRecurUtil.quicksort(list, iCalRecurUtil.CalObjTimeCompare);
    Log.debug(LogCat, "List after sort is " + list);
    assertListSorted(list);
  }

  protected void assertListSorted(List list)
  {
    int size = list.size();
    for (int i = 0; i < (size - 1); i++)
    {
      CalObjTime time1 = (CalObjTime)list.get(i);
      CalObjTime time2 = (CalObjTime)list.get(i + 1);
      assertTrue("i=" + i, time1.compareTo(time2) < 0);
    }
  }

  protected List genRandomTimeList(long seed, int listSize)
  {
    List cotimeList = new ArrayList();
    Random random = new Random(seed);
    for (int i = 0; i < listSize; i++)
    {
      int year = 1960 + random.nextInt(51);
      int month = random.nextInt(12);
      int date = 1 + random.nextInt(31);
      int hour = random.nextInt(24);
      int minute = random.nextInt(60);
      int second = random.nextInt(60);
      CalObjTime time = new CalObjTime(year, month, date, hour, minute, second);
      cotimeList.add(time);
    }
    return cotimeList;
  }

  /**
   * DOCUMENT ME!
   */
  public void testRemoveException()
  {
    try
    {
      List list = genRandomTimeList(123456789, 10);
      List exlist = genRandomTimeList(321456789, 10);
      CalObjTime time = (CalObjTime)list.get(3);
      list.add(3, time);
      list = iCalRecurUtil.removeDuplicateAndInvalidTimes(list);
      exlist = iCalRecurUtil.removeDuplicateAndInvalidTimes(exlist);
      Log.debug(LogCat, "List after removeDuplicateAndInvalidTimes is " + 
                list);
      Log.debug(LogCat, 
                "ExList after removeDuplicateAndInvalidTimes is " + exlist);
      assertEquals("removeDuplicateAndInvalidTimes", 10, list.size());
      time = (CalObjTime)list.get(0);
      exlist.add(3, time.clone());
      time = (CalObjTime)list.get(1);
      exlist.add(2, time.clone());
      list = iCalRecurUtil.quicksort(list, iCalRecurUtil.CalObjTimeCompare);
      exlist = iCalRecurUtil.quicksort(exlist, iCalRecurUtil.CalObjTimeCompare);
      Log.debug(LogCat, "List after quicksort is " + list);
      Log.debug(LogCat, "ExList after quicksort is " + exlist);
      List oldList = new ArrayList(list);
      list = iCalRecurUtil.removeExceptionTimes(list, exlist);
      Log.debug(LogCat, "List after removeExceptionTimes is " + list);
      Log.debug(LogCat, "ExList after removeExceptionTimes is " + exlist);
      assertEquals("removeExceptionTimes_1", 8, list.size());
      //empty exlist
      list = new ArrayList(oldList);
      exlist = new ArrayList();
      list = iCalRecurUtil.removeExceptionTimes(list, exlist);
      assertEquals("removeExceptionTimes_2", 10, list.size());
      //same content
      list = new ArrayList(oldList);
      exlist = new ArrayList(list);
      list = iCalRecurUtil.removeExceptionTimes(list, exlist);
      assertEquals("removeExceptionTimes_3", 0, list.size());
      //    time = (CalObjTime)list.get(3);
      //    list.add(3, time);
      //    list = iCalRecurUtil.removeExceptionTimes(list, exlist);
      //    Log.debug(LogCat, "List after remove is " + list);
    }
    catch (Throwable t)
    {
      Log.err(LogCat, "", t);
    }
  }

  /**
   * DOCUMENT ME!
   * 
   * @param args DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public static void main(String[] args)
                   throws Exception
  {
    junit.textui.TestRunner.run(suite());
  }
}