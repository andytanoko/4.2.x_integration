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

import com.gridnode.pdip.app.alert.model.AlertAction;
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

public class AlertManagerBeanAlertActionTest
  extends    TestCase
{
  private static final Long ALERT_ID_1 = new Long(12);
  private static final Long ACTION_ID_1 = new Long(14);

  private static final Long ALERT_ID_2 = new Long(34);
  private static final Long ACTION_ID_2 = new Long(48);

  private static final String CLASSNAME = "AlertManagerBeanAlertActionTest";

  IAlertManagerHome home;
  IAlertManagerObj remote;

  public AlertManagerBeanAlertActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AlertManagerBeanAlertActionTest.class);
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

  private Collection retrieveTestAlertAction(Long alertId) throws Exception
  {
    return remote.getAlertActionsByAlertUId(alertId);
  }

  private void deleteTestAlertAction(Long alertId) throws Exception
  {
    Collection retrieve = retrieveTestAlertAction(alertId);
    if (retrieve != null && !retrieve.isEmpty())
       remote.deleteAlertAction(new Long(((AlertAction)retrieve.iterator().next()).getUId()));
  }

  private AlertAction createAlertAction(Long alertId, Long actionId)
  {
    AlertAction alertAction = new AlertAction();
    alertAction.setAlertUid(alertId);
    alertAction.setActionUid(actionId);
    return alertAction;
  }

  public void testCreateAlertAction() throws Exception
  {
    try
    {
      AlertAction retrieved = null;

      // test create AlertAction.
      AlertAction action1 = createAlertAction(ALERT_ID_1, ACTION_ID_1);
      checkCreateAlertActionPass(action1, ALERT_ID_1);
      deleteTestAlertAction(ALERT_ID_1);
      // test create 2nd AlertAction
      AlertAction action3 = createAlertAction(ALERT_ID_2, ACTION_ID_2);
      checkCreateAlertActionPass(action3, ALERT_ID_2);

      deleteTestAlertAction(ALERT_ID_2);
      // test create AlertAction
      AlertAction action4 = createAlertAction(ALERT_ID_1, ACTION_ID_2);
      checkCreateAlertActionPass(action4, ALERT_ID_1);

      // test create AlertAction
      AlertAction action5 = createAlertAction(ALERT_ID_2, ACTION_ID_1);
      checkCreateAlertActionPass(action5, ALERT_ID_2);

      deleteTestAlertAction(ALERT_ID_2);
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testCreateAction", "fails", ex);
      throw ex;
    }
  }

  private void checkCreateAlertActionPass(AlertAction action, Long alertId) throws Exception
  {
    remote.createAlertAction(action);
  }

  private void checkCreateAlertActionFail(AlertAction action, String errorMessage)
  {
    try
    {
      remote.createAlertAction(action);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

  public void testGetAlertActionByAlertId() throws Exception
  {
    try
    {
      AlertAction action = createAlertAction(ALERT_ID_1, ACTION_ID_1);
      checkGetAlertActionByAlertIdSuccess(ALERT_ID_1, action);
      deleteTestAlertAction(ALERT_ID_1);
      // Find non-exisitng
      checkGetAlertActionByAlertIdFail(new Long(-999), "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertActionByAlertId", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAlertActionByAlertIdFail(Long alertId, String errorMsg) throws Exception
  {
    Collection findAction = remote.getAlertActionsByAlertUId(alertId);
    assertNull(errorMsg, findAction);

//    remote.deleteAlertAction(new Long(findAction.getUId()));
  }

  private void checkGetAlertActionByAlertIdSuccess(Long alertId, AlertAction action) throws Exception
  {
    Collection findAction = remote.getAlertActionsByAlertUId(alertId);
    assertNotNull(findAction);
    assertTrue(!findAction.isEmpty());
//    checkIdenticalAlert(action, findAction, false);
  }
}