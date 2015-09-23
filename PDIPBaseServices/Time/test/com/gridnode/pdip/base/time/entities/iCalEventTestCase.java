// %1023788051215:com.gridnode.pdip.base.time%
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
package com.gridnode.pdip.base.time.entities;

import com.gridnode.pdip.base.time.entities.ejb.IiCalEventHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalEventObj;
import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.base.time.entities.value.exchange.GenMime;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class iCalEventTestCase extends TestCase
{
  static String LogCat= "iCalTest";
  private IiCalEventHome entityHome;
  private IiCalEventObj entityObject;

  /**
   * Creates a new iCalEventTestCase object.
   *
   * @param name DOCUMENT ME!
   */
  public iCalEventTestCase(String name)
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
    return new TestSuite(iCalEventTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
    try
    {
      entityHome=
        (IiCalEventHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(
          IiCalEventHome.class);
      Log.debug(LogCat, "Object Found ... ");
    }
    catch (Exception ex)
    {
      Log.err(LogCat, " Exception in SetUp  : ", ex);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
    Log.debug(LogCat, "ShutDown called !");
    try
    {
      if (entityObject != null)
        entityHome.remove(entityObject.getPrimaryKey());
    }
    catch (Exception ex)
    {
      Log.err(LogCat, " Exception in shutDown  : ", ex);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testiCalEventCreate()
  {
    Date dtStart= iCalUtil.getTimeInSecod(new Date());
    Integer owner= new Integer(1000);
    iCalEvent valueEntity= null;
    int freq= IFrenqency.HOURLY;
    int interval= 2;
    try
    {
      //insert
      Log.debug(LogCat, "In Test1 Case ");
      valueEntity= new iCalEvent();
      valueEntity.setStartDt(dtStart);
      valueEntity.setOwnerId(owner);

      iCalValueV value= new iCalURIV("Test Text Value");
      iCalPropertyV desc= new iCalPropertyV(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY);
      desc.setValue(value);
      valueEntity.addProperty(desc);

      iCalRecurrenceV recur= new iCalRecurrenceV();
      recur.setFrequency(freq);
      recur.setInterval(interval);
      recur.setUntil(new Date());
      List byDay= new ArrayList();
      byDay.add(new DayEntry(IWeekDay.MONDAY));
      byDay.add(new DayEntry(2, IWeekDay.MONDAY));
      byDay.add(new DayEntry(-2, IWeekDay.MONDAY));
      recur.setByDay(byDay);
      List byHour= new ArrayList();
      byHour.add(new Integer(10));
      byHour.add(new Integer(11));
      byHour.add(new Integer(12));
      recur.setByHour(byHour);
      iCalPropertyV prop= new iCalPropertyV((short) iCalPropertyKind.ICAL_RRULE_PROPERTY);
      iCalParameterV param= new iCalParameterV((short) iCalParameterKind.ICAL_ROLE_PARAMETER);
      param.setValue(recur);
      prop.addParam(param);
      prop.setValue(recur);
      valueEntity.addProperty(prop);
      String mime= GenMime.genComponent(valueEntity);
      Log.debug(LogCat, "Event with before add to db is " + mime);

      Log.debug(LogCat, "After Test Case ");
      entityObject= entityHome.create(valueEntity);
      mime= GenMime.genComponent(valueEntity);
      assertNotNull("WorkListEntity Value NULL in iCalEventTestCase", entityObject);
      valueEntity= (iCalEvent) entityObject.getData();
      Log.debug(LogCat, "Event with Primary Key " + valueEntity.getKey() + " is " + mime);

      //        assertEquals(dtStart, valueEntity.getStartDt());
      assertEquals(owner, valueEntity.getOwnerId());
      List propList= valueEntity.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
      assertNotNull(propList);
      prop= (iCalPropertyV) propList.get(0);
      recur= (iCalRecurrenceV) prop.getValue();
      Log.log(LogCat, "Recurrence is " + recur);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "testiCalEventCreate failed ", ex);
    }
    for (int i= 0; i < 2; i++)
    {
      //find by filter
      try
      {
        Log.log(LogCat, "findByFilter");
        IDataFilter filter= new DataFilterImpl();
        filter.addSingleFilter(null, iCalEvent.START_DT, filter.getEqualOperator(), dtStart, false);
        Collection col= entityHome.findByFilter(filter);
        assertNotNull("Value NULL in iCalEventTestCase FindBy Filter", col);
        Iterator iterator= col.iterator();
        while (iterator.hasNext())
        {
          entityObject= (IiCalEventObj) iterator.next();
          valueEntity= (iCalEvent) entityObject.getData();
          String mime= GenMime.genComponent(valueEntity);
          Log.debug(LogCat, "Event with Primary Key " + valueEntity.getKey() + " is " + mime);

          assertEquals(dtStart, valueEntity.getStartDt());
          List propList= valueEntity.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
          assertNotNull(propList);
          iCalPropertyV prop= (iCalPropertyV) propList.get(0);
          iCalRecurrenceV recur= (iCalRecurrenceV) prop.getValue();
          Log.log(LogCat, "Recurrence is " + recur);
          List paramList= prop.getParamList(iCalParameterKind.ICAL_ROLE_PARAMETER);
          assertNotNull(paramList);
          iCalParameterV param= (iCalParameterV) paramList.get(0);
          recur= (iCalRecurrenceV) param.getValue();
          Log.log(LogCat, "Param's Recurrence is " + recur);
        }
      }
      catch (Throwable ex)
      {
        Log.err(LogCat, "testiCalEventFindByFilter Failed ", ex);
      }
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testLoad()
  {
    Date dtStart= new Date();
    Integer owner= new Integer(1000);
    iCalEvent valueEntity= null;
    int freq= IFrenqency.HOURLY;
    int interval= 2;
    long start= 1;
    long end= 40;
    //find by filter
    for (long i= start; i < end; i++)
    {
      try
      {

        extractByPrimaryKey(new Long(i));
      }
      catch (Throwable ex)
      {
        Log.err(LogCat, "testiCalEventFindByFilter Failed ", ex);
      }
    }
  }

  private void extractByPrimaryKey(Long primKey) throws FinderException, RemoteException
  {
    iCalEvent valueEntity;
    Log.log(LogCat, "findByPrimary Key");
    entityObject= entityHome.findByPrimaryKey(primKey);
    valueEntity= (iCalEvent) entityObject.getData();
    List propList= valueEntity.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
    assertNotNull(propList);
    iCalPropertyV prop= (iCalPropertyV) propList.get(0);
    iCalRecurrenceV recur= (iCalRecurrenceV) prop.getValue();
    Log.log(LogCat, "Recurrence is " + recur);
    //    List paramList= prop.getParamList(iCalParameterKind.ICAL_ROLE_PARAMETER);
    //    assertNotNull(paramList);
    //    iCalParameterV param= (iCalParameterV) paramList.get(0);
    //    recur= (iCalRecurrenceV) param.getValue();
    //    Log.log(LogCat, "Param's Recurrence is " + recur);
    String mime= GenMime.genComponent(valueEntity);
    Log.debug(LogCat, "Event with Primary Key " + primKey + " is " + mime);

  }

  /**
   * DOCUMENT ME!
   *
   * @param args DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public static void main(String[] args) throws Exception
  {
    junit.textui.TestRunner.run(suite());
  }
}