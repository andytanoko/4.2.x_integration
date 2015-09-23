/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertManagerBeanAlertTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.facade.ejb;

import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.helpers.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;


import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.*;

/**
 * Document me
 *
 */

public class AlertManagerBeanAlertTest
  extends    TestCase
{
  private static final String NAME_1 = "Name of Alert";
  private static final Long TYPE_1 = new Long(3);
  private static final Long CATEGORY_1 = new Long(1);
  private static final String TRIGGER_1 = "0";
  private static final String DESCRIPTION_1 = "description for the alert 1";

  private static final String NAME_2 = "Name";
  private static final Long TYPE_2 = new Long(2);
  private static final Long CATEGORY_2 = new Long(2);
  private static final String TRIGGER_2 = "2";
  private static final String DESCRIPTION_2 = "Test Description 2";

  private static final String CLASSNAME = "AlertManagerBeanAlertTest";

  IAlertManagerHome home;
  IAlertManagerObj remote;

  public AlertManagerBeanAlertTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AlertManagerBeanAlertTest.class);
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

  private Alert createAlert(String alertName, Long type, Long category, String trigger, String desc)
  {
    Alert alert = new Alert();
    alert.setName(alertName);
    alert.setAlertType(type);
    alert.setCategory(category);
    alert.setTrigger(trigger);
    alert.setDescr(desc);
    return alert;
  }

  private Alert retrieveTestAlert(String alertName) throws Exception
  {
    return remote.getAlertByAlertName(alertName);
  }

  private void deleteTestAlert(String alertName) throws Exception
  {
    Alert retrieve = retrieveTestAlert(alertName);
    if (retrieve != null)
       remote.deleteAlert(new Long(retrieve.getUId()));
  }

  private Alert addTestAlert(String alertName, Long type, Long category, String trigger, String desc)
          throws Exception
  {
    Alert inserted = createAlert(alertName, type, category, trigger, desc);
    remote.createAlert(inserted);
    return remote.getAlertByAlertName(inserted.getName());
  }


  private void checkIdenticalAlert(Alert alert1, Alert alert2, boolean checkUId)
  {
    assertEquals("Name not the same!", alert1.getName(), alert2.getName());
    //assertEquals("Type not the same!", alert1.getType(), alert2.getType());
    assertEquals("Category not the same!", alert1.getCategory(), alert2.getCategory());
    assertEquals("Trigger not the same!", alert1.getTrigger(), alert2.getTrigger());
    assertEquals("Description not the same!", alert1.getDescr(), alert2.getDescr());
    if (checkUId)
       assertEquals("UId not the same!", alert1.getUId(), alert2.getUId());
  }


  private boolean isIdenticalAlert(Alert alert1, Alert alert2, boolean checkUId)
  {
    if ((alert1.getDescr().equals(alert2.getDescr())) &&
        (alert1.getName().equals(alert2.getName())) &&
        (alert1.getCategory().equals(alert2.getCategory())) &&
        (alert1.getTrigger().equals(alert2.getTrigger())))  // &&
        //(alert1.getType().equals(alert2.getType())))
    {
      if (checkUId)
      {
        if (alert1.getUId() == alert2.getUId())
          return true;
        else
          return false;
      }
      else
        return true;
    }
    return false;
  }


  public void testCreateAlert() throws Exception
  {
    try
    {
      Alert retrieved = null;

      // test create alert.
      Alert alert1 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkCreateAlertPass(alert1, NAME_1);

      // test create duplicate alert.
      Alert alert2 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkCreateAlertFail(alert2, "Able to add in duplicate alert into the database.");

      // test create 2nd alert
      Alert alert3 = createAlert(NAME_2, TYPE_2, CATEGORY_2, TRIGGER_2, DESCRIPTION_2);
      checkCreateAlertPass(alert3, NAME_2);

      // delete previous test
      deleteTestAlert(alert3.getName());

      // test create alert with same alertName, category, trigger and desc but different type.
      Alert alert4 = createAlert(NAME_1, TYPE_2, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkCreateAlertFail(alert4, "Able to add in same alertName, category, trigger and " +
                                       "desc but different Type.");

      // test create alert with same alertName, type, desc and trigger but different category.
      Alert alert5 = createAlert(NAME_1, TYPE_1, CATEGORY_2, TRIGGER_1, DESCRIPTION_1);
      checkCreateAlertFail(alert5, "Able to add in same alertName, type, desc " +
                                       "and trigger but different category.");

      // test create alert with same alertName, type, category and desc but different trigger.
      Alert alert6 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_2, DESCRIPTION_1);
      checkCreateAlertFail(alert6, "Able to add in same alertName, type, category and  " +
                                       "desc but different trigger");

      // test create alert with same alertName, type, category and trigger but different desc.
      Alert alert7 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_2);
      checkCreateAlertFail(alert7, "Able to add in same alertName, Type, category and " +
                                       "trigger but different desc.");

    // test create alert with same desc, Type, trigger and category but different alertName.
      Alert alert8 = createAlert(NAME_2, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkCreateAlertPass(alert8, NAME_2);
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testCreateAlert", "fails", ex);
      throw ex;
    }
    finally
    {
      deleteTestAlert(NAME_1);
      deleteTestAlert(NAME_2);
    }
  }

  private void checkCreateAlertPass(Alert alert, String alertName) throws Exception
  {
    remote.createAlert(alert);
    Alert retrieved = retrieveTestAlert(alertName);
    checkIdenticalAlert(alert, retrieved, false);
  }

  private void checkCreateAlertFail(Alert alert, String errorMessage)
  {
    try
    {
      remote.createAlert(alert);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testUpdateAlert() throws Exception
  {
    try
    {
      Alert existing = null;

      // test update non-existing alert.
      Alert alert1 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkUpdateAlertFail(alert1, "Able to update non-existing alert.");

      // create alert in database
      existing = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);

      // test update existing alert change desc2.
      existing.setDescr(DESCRIPTION_2);

      // test update existing alert change dataType using updated version.
      existing = retrieveTestAlert(NAME_1);
      existing.setTrigger(TRIGGER_2);

      // test update existing alert change type but using old version.
 //     existing.setType(TYPE_1);
 //    checkUpdateAlertFail(existing, NAME_1);


      // test update existing alert change category using updated version.
      existing = retrieveTestAlert(NAME_1);
      existing = remote.getAlertByAlertUId(new Long(existing.getUId()));
      existing.setCategory(CATEGORY_2);
      //checkUpdateAlertPass(existing, NAME_1);

      // test update existing alert change alertName using updated version.
      existing = retrieveTestAlert(NAME_1);
      assertNull("Alert 2 exist. Test environment not set properly.", retrieveTestAlert(NAME_2));
      existing.setName(NAME_2);
      checkUpdateAlertPass(existing, NAME_2);
      assertNull("Alert 1 exist. Result not correct. Two copies of same data: Name1 and Name2!", retrieveTestAlert(NAME_1));

      //delete all alerts created
      deleteTestAlert(existing.getName());
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testUpdateAlert", "fails", ex);
      throw ex;
    }
  }



  private void checkUpdateAlertFail(Alert alert, String errorMessage)
  {
    try
    {
      remote.updateAlert(alert);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkUpdateAlertPass(Alert alert, String alertName) throws Exception
  {
    remote.updateAlert(alert);
    Alert retrieved = retrieveTestAlert(alertName);
    checkIdenticalAlert(alert, retrieved, false);
  }

  public void testDeleteAlert() throws Exception
  {
    try
    {
      Alert insert1 = null;
      // Delete non-existing Alert
      Alert alert1 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkDeleteAlertFail(alert1, "delete non-existing alert");

      // Create Alert.
      insert1 = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);

      // Delete existing alert
      checkDeleteAlertPass(insert1, NAME_1);

    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testDeleteAlert", "fails", ex);
      throw ex;
    }
  }

  private void checkDeleteAlertPass(Alert alert, String alertName) throws Exception
  {
    remote.deleteAlert(new Long(alert.getUId()));
    Alert retrieve = retrieveTestAlert(alertName);
    assertNull("Delete not successful.", retrieve);
  }

  private void checkDeleteAlertFail(Alert alert, String errorMessage) throws Exception
  {
    try
    {
      remote.deleteAlert(new Long(alert.getUId()));
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetAlertByAlertName() throws Exception
  {
    try
    {
      Alert exist = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      // Find exisitng
      Alert mock = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkGetAlertByAlertNameSuccess(NAME_1, mock);

      deleteTestAlert(exist.getName());
      // Find non-exisitng
      checkGetAlertByAlertNameFail(NAME_1, "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertByAlertName", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAlertByAlertNameFail(String alertName, String errorMsg) throws Exception
  {
    Alert findAlert = remote.getAlertByAlertName(alertName);
    assertNull(errorMsg, findAlert);
  }

  private void checkGetAlertByAlertNameSuccess(String alertName, Alert alert) throws Exception
  {
    Alert findAlert = remote.getAlertByAlertName(alertName);
    assertNotNull(findAlert);
    checkIdenticalAlert(alert, findAlert, false);
  }


  public void testGetAlertByAlertUId() throws Exception
  {
    try
    {
      Alert exist = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      // Find exisitng
      Alert mock = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkGetAlertByAlertUIdSuccess(exist.getUId(), mock);

      deleteTestAlert(exist.getName());
      // Find non-exisitng
      checkGetAlertByAlertUIdFail(exist.getUId(), "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertByAlertUId", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAlertByAlertUIdFail(long uId, String errorMessage)
  {
    try
    {
      Alert alert = remote.getAlertByAlertUId(new Long(uId));
      assertNull(errorMessage, alert);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkGetAlertByAlertUIdSuccess(long uId, Alert alert)  throws Exception
  {
    Alert findAlert = remote.getAlertByAlertUId(new Long(uId));
    assertNotNull(findAlert);
    checkIdenticalAlert(alert, findAlert, false);
  }

/*
  public void testGetAllAlerts() throws Exception
  {
    try
    {
      Alert exist1 = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      Alert exist2 = addTestAlert(NAME_2, TYPE_2, CATEGORY_2, TRIGGER_2, DESCRIPTION_2);

      Alert mock1 = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      Alert mock2 = createAlert(NAME_2, TYPE_2, CATEGORY_2, TRIGGER_2, DESCRIPTION_2);

      Alert[] alerts = null;

      //Check existing 2
      alerts = new Alert[2];
      alerts[0] = mock1;
      alerts[1] = mock2;
      checkGetAllAlertsPass(alerts);

      // Delete alert1
      deleteTestAlert(NAME_1);

      // Check Existing alert1
      alerts = new Alert[1];
      alerts[0] = mock1;
      checkGetAllAlertsFail(alerts);

      // Check Existing alert2
      alerts = new Alert[1];
      alerts[0] = mock2;
      checkGetAllAlertsPass(alerts);

      // Delete alert2
      deleteTestAlert(NAME_2);

      // Check Existing alert1
      alerts = new Alert[1];
      alerts[0] = mock1;
      checkGetAllAlertsFail(alerts);

      // Check Existing alert2
      alerts = new Alert[1];
      alerts[0] = mock2;
      checkGetAllAlertsFail(alerts);

      // Check non-Existing
      alerts = null;
      checkGetAllAlertsPass(alerts);
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testFindAlerts", "fails", ex);
      throw ex;
    }
    finally
    {
      deleteTestAlert(NAME_1);
      deleteTestAlert(NAME_2);
    }
  }

  private void checkGetAllAlertsFail(Alert[] alerts)
          throws Exception
  {
    Collection findAlerts = remote.getAllAlerts();
    if (findAlerts != null)
    {
      if (alerts != null)
      {
        if (alerts.length != findAlerts.size())
        {
          AlertLogger.infoLog(CLASSNAME, "checkGetAllAlertsFail",
                            "Fail because the expected size(" + alerts.length +
                            ") and the retrieved size(" + findAlerts.size() +
                            ") are not the same.");
          return;
        }
        boolean found = false;
        boolean[] isThere = new boolean[alerts.length];
        // Reset isThere.
        for (int i = 0; i < isThere.length; i++)
            isThere[i] = false;
        Iterator iter = findAlerts.iterator();
        while (iter.hasNext())
        {
          found = false;
          Alert retrieved = (Alert) iter.next();
          for (int i = 0; i < alerts.length; i++)
          {
            if (retrieved.getName().equals(alerts[i].getName()))
            {
              if (isIdenticalAlert(retrieved, alerts[i], false))
              {
                isThere[i] = true;
                found = true;
                break;
              }
            }
          }
          if (!found)
          {
            AlertLogger.infoLog(CLASSNAME, "checkGetAllAlertsFail",
                              "Fail because retrieved alert(" +
                              retrieved.getEntityDescr() + ") not found");
            return;
          }
        }
        for (int i = 0; i < isThere.length; i++)
        {
          if (!isThere[i])
          {
            AlertLogger.infoLog(CLASSNAME, "checkGetAllAlertsFail",
                              "Fail because expected alert(" +
                              alerts[i].getEntityDescr() + ") not found");
            return;
          }
        }
      }
      else //alerts == null
      {
        AlertLogger.infoLog(CLASSNAME, "checkGetAllAlertsFail",
                          "Fail because the expected size(null" +
                          ") and the retrieved size(" + findAlerts.size() +
                          ") are not the same.");
      }
    }
    else // findAlerts == null
    {
      if (alerts != null && alerts.length > 0)
      {
        AlertLogger.infoLog(CLASSNAME, "checkGetAllAlertsFail",
                          "Fail because the expected size(" + alerts.length +
                          ") and the retrieved size(null" +
                          ") are not the same.");
      }
      else
        assertTrue("Expected list and retrieved list are both empty!", false);
    }
    assertTrue("Expected list and retrieved list are the same! Should not be the same!!!", false);
  }

  private void checkGetAllAlertsPass(Alert[] alerts)
          throws Exception
  {
    Collection findAlerts = remote.getAllAlerts();
    if (findAlerts != null && findAlerts.size() > 0)
    {
      if (alerts != null)
      {
        assertEquals("Retrieved size is not expected!", alerts.length, findAlerts.size());
        Alert retrieved = null;
        boolean found = false;
        boolean[] isThere = new boolean[alerts.length];
        // Reset isThere.
        for (int i = 0; i < isThere.length; i++)
          isThere[i] = false;
        Iterator iter = findAlerts.iterator();
        while (iter.hasNext())
        {
          found = false;
          retrieved = (Alert) iter.next();
          for (int i = 0; i < alerts.length; i++)
          {
            if (retrieved.getName().equals(alerts[i].getName()))
            {
              checkIdenticalAlert(alerts[i], retrieved, false);
              isThere[i] = true;
              found = true;
              break;
            }
          }
          if (!found)
            assertTrue("Alert " + retrieved.getEntityDescr() + " not expected!", false);
        }
        for (int i = 0; i < isThere.length; i++)
          assertTrue("Alert " + alerts[i].getEntityDescr() +
                     " not in the retrieved list!", isThere[i]);
      }
      else //alerts == null
      {
        assertTrue("Expected empty list but retrieved not empty!" +
                   " Could be caused by already existing data.", false);
      }
    }
    else // findAlerts == null
    {
      if (alerts != null && alerts.length > 0)
        assertTrue("Data not found!", false);
    }
  }

*/

  public void testGetAlertByCategoryName() throws Exception
  {
    try
    {
      Alert exist = addTestAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      // Find exisitng
      Alert mock = createAlert(NAME_1, TYPE_1, CATEGORY_1, TRIGGER_1, DESCRIPTION_1);
      checkGetAlertByCategoryNameSuccess("Test1", mock);

      deleteTestAlert(exist.getName());
      // Find non-exisitng
      checkGetAlertByCategoryNameFail(NAME_1, "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertByCategoryName", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAlertByCategoryNameFail(String categoryCode, String errorMsg) throws Exception
  {
    Alert findAlert = remote.getAlertByCategoryCode(categoryCode);
    assertNull(errorMsg, findAlert);
  }

  private void checkGetAlertByCategoryNameSuccess(String categoryCode, Alert alert) throws Exception
  {
    Alert findAlert = remote.getAlertByCategoryCode(categoryCode);
    assertNotNull(findAlert);
    checkIdenticalAlert(alert, findAlert, false);
  }
}