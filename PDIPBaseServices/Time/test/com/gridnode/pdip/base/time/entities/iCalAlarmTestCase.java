// %1023788051027:com.gridnode.pdip.base.time%
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

import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmHome;
import com.gridnode.pdip.base.time.entities.ejb.IiCalAlarmObj;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class iCalAlarmTestCase
  extends TestCase
{
  static String LogCat = "iCalAlarmTest";
  private IiCalAlarmHome entityHome;
  private IiCalAlarmObj entityObject;

  /**
   * Creates a new iCalAlarmTestCase object.
   *
   * @param name DOCUMENT ME!
   */
  public iCalAlarmTestCase(String name)
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
    return new TestSuite(iCalAlarmTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
    try
    {
      entityHome = (IiCalAlarmHome)ServiceLookup.getInstance(
                                       ServiceLookup.CLIENT_CONTEXT).getHome(
                       IiCalAlarmHome.class);
      Log.debug(LogCat, "Object Found ... ");
    }
    catch (Exception ex)
    {
      Log.err(LogCat, " Exception in setUp  : ", ex);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
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
  public void testiCalAlarmCreate()
  {
    Date dtStart = new Date(new Date().getTime() + 1000);
    String receiverId = "GridTalk";
    String category = "Test";
    iCalAlarm valueEntity = null;
    try
    {
      //insert
      Log.debug(LogCat, "In Test1 Case ");
      valueEntity = new iCalAlarm();
      valueEntity.setStartDt(dtStart);
      valueEntity.setReceiverKey(receiverId);
      valueEntity.setCategory(category);
      Log.debug(LogCat, "After Test Case ");
      entityObject = entityHome.create(valueEntity);
      assertNotNull("iCalAlarm Value NULL in iCalAlarmTestCase", entityObject);
      valueEntity = (iCalAlarm)entityObject.getData();
      assertEquals(dtStart, valueEntity.getStartDt());
      assertEquals(receiverId, valueEntity.getReceiverKey());
      assertNull(valueEntity.getDisabled());
      assertNull(valueEntity.getCount());
      assertNull(valueEntity.getDelayPeriod());
      //assertNull(valueEntity.getName());
      assertNull(valueEntity.getRepeat());
      //assertNotNull(valueEntity.getUId());
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "testiCalAlarmCreate failed ", ex);
    }
    //find by filter
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, iCalAlarm.START_DT,
                             filter.getEqualOperator(), dtStart, false);
      Collection col = entityHome.findByFilter(filter);
      assertNotNull("Value NULL in iCalAlarmTestCase FindBy Filter", col);
      Iterator iterator = col.iterator();
      while (iterator.hasNext())
      {
        entityObject = (IiCalAlarmObj)iterator.next();
        valueEntity = (iCalAlarm)entityObject.getData();
        assertEquals(dtStart, valueEntity.getStartDt());
      }
    }
    catch (Throwable ex)
    {
      Log.err(LogCat, "testiCalAlarmFindByFilter Failed ", ex);
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