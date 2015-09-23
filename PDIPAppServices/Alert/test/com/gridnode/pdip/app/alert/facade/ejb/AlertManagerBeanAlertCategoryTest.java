/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertManagerBeanAlertActionTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.facade.ejb;

import com.gridnode.pdip.app.alert.model.AlertCategory;
import com.gridnode.pdip.app.alert.helpers.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;


import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Document me
 *
 */

public class AlertManagerBeanAlertCategoryTest
  extends    TestCase
{
  private static final String CODE_1 = "CODE 1";
  private static final String NAME_1 = "Name 1";
  private static final String DESCR_1 = "Description 1";

  private static final String CODE_2 = "CODE 2";
  private static final String NAME_2 = "Name 2";
  private static final String DESCR_2 = "Description 2";

  private static final String CLASSNAME = "AlertManagerBeanAlertCategoryTest";

  IAlertManagerHome home;
  IAlertManagerObj remote;

  public AlertManagerBeanAlertCategoryTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AlertManagerBeanAlertCategoryTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    home = (IAlertManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IAlertManagerHome.class);
    assertNotNull("Home is null", home);
    remote = home.create();
    assertNotNull("remote is null", remote);
    //createTestData();
    //cleanup();
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }
  private AlertCategory createAlertCategory(String code, String name, String description)
  {
    AlertCategory category = new AlertCategory();
    category.setCode(code);
    category.setName(name);
    category.setDescr(description);
    return category;
  }

  public void testCreateAlertCategory() throws Exception
  {
    try
    {
      AlertCategory retrieved = null;

      // test create
      AlertCategory category1 = createAlertCategory(CODE_1, NAME_1, DESCR_1);
System.out.println("Inside testCreateAlertCategory Case 1");
      checkCreateAlertCategoryPass(category1, CODE_1);

//      deleteTestAlertCategory(ALERT_ID_2);
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testCreateAlertCategory", "fails", ex);
      throw ex;
    }
  }

  private void checkCreateAlertCategoryPass(AlertCategory category, String code) throws Exception
  {
System.out.println("Inside checkCreateAlertCategoryPass Case 1");
    remote.createAlertCategory(category);
  }

  private void checkCreateAlertCategoryFail(AlertCategory category, String errorMessage)
  {
    try
    {
      remote.createAlertCategory(category);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

/*
  public void testGetAlertCategoryByUId() throws Exception
  {
    try
    {
      AlertCategory category1 = createAlertCategory(CODE_1, NAME_1, DESCR_1);
      checkGetAlertCategoryByCodeSuccess(CODE_1, category1);
      // Find non-exisitng
      checkGetAlertCategoryByCodeFail(CODE_1, "Deleting non-existing", category1);
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertActionByAlertId", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAlertCategoryByCodeFail(String code, String errorMsg, AlertCategory cat) throws Exception
  {
    AlertCategory findCat = remote.getAlertCategoryByCategoryUId(new Long(cat.getUId()));
    assertNull(errorMsg, findCat);
//    remote.deleteAlertAction(new Long(findAction.getUId()));
  }

  private void checkGetAlertCategoryByCodeSuccess(String code, AlertCategory category) throws Exception
  {
    AlertCategory findCat = remote.getAlertCategoryByCategoryUId(new Long(category.getUId()));
    assertNotNull(category);
//    checkIdenticalAlert(action, findAction, false);
  }
*/
}