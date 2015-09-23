// %1023788051434:com.gridnode.pdip.base.time.value%
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
package com.gridnode.pdip.base.time.entities.value;

import com.gridnode.pdip.base.time.entities.model.iCalEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimeIntervalTestCase
  extends TestCase
  implements IByRule
{
  static String LogCat = "TimeIntervalTestCase";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new TimeIntervalTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public TimeIntervalTestCase(String name)
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
    return new TestSuite(TimeIntervalTestCase.class);
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
  public void testHasIntersection()
  {
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 100000);
    TimeInterval interval1 = new TimeInterval(date1, date2);
    TimeInterval interval2 = new TimeInterval(new Date(date2.getTime() + 100), 
                                              new Date(date2.getTime() + 200));
    assertEquals("test1", false, interval1.hasIntersection(interval2));
    assertEquals("test2", null, interval1.union(interval2));
    interval2 = new TimeInterval(new Date(date2.getTime() - 100), 
                                 new Date(date2.getTime() + 200));
    assertEquals("test3", true, interval1.hasIntersection(interval2));
    assertEquals("test4", 
                 new TimeInterval(date1, new Date(date2.getTime() + 200)), 
                 interval1.union(interval2));
    interval2 = new TimeInterval(date2, new Date(date2.getTime() + 200));
    assertEquals("test5", true, interval1.hasIntersection(interval2));
    assertEquals("test6", 
                 new TimeInterval(date1, new Date(date2.getTime() + 200)), 
                 interval1.union(interval2));
    interval2 = new TimeInterval(new Date(date1.getTime() - 100), date1);
    assertEquals("test7", true, interval1.hasIntersection(interval2));
    assertEquals("test8", 
                 new TimeInterval(new Date(date1.getTime() - 100), date2), 
                 interval1.union(interval2));
  }

  /**
   * DOCUMENT ME!
   */
  public void testAddToList()
  {
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 100000);
    TimeInterval interval1 = new TimeInterval(date1, date2);
    TimeInterval interval2 = new TimeInterval(new Date(date2.getTime() + 10000), 
                                              new Date(date2.getTime() + 20000));
    assertEquals("test1", false, interval1.hasIntersection(interval2));
    assertEquals("test2", null, interval1.union(interval2));
    TimeInterval interval3 = new TimeInterval(new Date(date2.getTime() - 10000), 
                                              new Date(date2.getTime() + 5000));
    assertEquals("test3", true, interval1.hasIntersection(interval3));
    TimeInterval union3 = new TimeInterval(date1, 
                                           new Date(date2.getTime() + 5000));
    assertEquals("test4", union3, interval1.union(interval3));
    TimeInterval interval4 = new TimeInterval(date2, 
                                              new Date(date2.getTime() + 8000));
    assertEquals("test5", true, interval1.hasIntersection(interval4));
    TimeInterval union4 = new TimeInterval(date1, 
                                           new Date(date2.getTime() + 8000));
    assertEquals("test6", union4, interval1.union(interval4));
    TimeInterval interval5 = new TimeInterval(new Date(date1.getTime() - 10000), 
                                              date1);
    assertEquals("test7", true, interval1.hasIntersection(interval5));
    assertEquals("test8", 
                 new TimeInterval(new Date(date1.getTime() - 10000), date2), 
                 interval1.union(interval5));
    List arrayList = new ArrayList();
    interval1.addToList(arrayList);
    arrayList = interval1.addToList(arrayList);
    assertEquals("1", interval1, arrayList.get(0));
    arrayList = interval2.addToList(arrayList);
    assertEquals("2", interval2, arrayList.get(1));
    arrayList = interval3.addToList(arrayList);
    assertEquals("3", union3, arrayList.get(0));
    arrayList = interval4.addToList(arrayList);
    assertEquals("4", union4, arrayList.get(0));
    arrayList = interval5.addToList(arrayList);
    assertEquals("size of list not correct", 2, arrayList.size());
    assertEquals("First", 
                 new TimeInterval(new Date(date1.getTime() - 10000), 
                                  new Date(date2.getTime() + 8000)), 
                 arrayList.get(0));
    assertEquals("Second", interval2, arrayList.get(1));
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