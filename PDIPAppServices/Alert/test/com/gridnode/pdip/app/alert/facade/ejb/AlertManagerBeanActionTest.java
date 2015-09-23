/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertManagerBeanActionTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 */

package com.gridnode.pdip.app.alert.facade.ejb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

/**
 * Document me
 *
 */

public class AlertManagerBeanActionTest
  extends    TestCase
{
  private static final String NAME_1 = "Test";
  private static final String DESCRIPTION_1 = "Test_Description_1";
  private static final Long MSGID_1 = new Long(1);

  private static final String NAME_2 = "Name";
  private static final String DESCRIPTION_2 = "Test Description 2";
  private static final Long MSGID_2 = new Long(2);

  private static final String CLASSNAME = "AlertManagerBeanActionTest";

  IAlertManagerHome home;
  IAlertManagerObj remote;

  public AlertManagerBeanActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AlertManagerBeanActionTest.class);
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

  private void deleteTestAction(String actionName) throws Exception
  {
    Action retrieve = retrieveTestAction(actionName);
    if (retrieve != null)
       remote.deleteAction(new Long(retrieve.getUId()));
  }

  private Action retrieveTestAction(String actionName) throws Exception
  {
    return remote.getActionByActionName(actionName);
  }

  private Action createAction(String actionName, String desc, Long msgId)
  {
    Action action = new Action();
    action.setName(actionName);
    action.setDescr(desc);
    action.setMsgUid(msgId);
    return action;
  }

/*
  private Action addTestAction(String actionName, String category, String role, String trigger, String type, String desc)
          throws Exception
  {
    Action inserted = createAction(actionName, category, role, trigger, type, desc);
    remote.createAction(inserted);
    return remote.getActionByActionName(inserted.getName());
  }
*/

  public void testCreateAction() throws Exception
  {
    try
    {
      Action retrieved = null;

      // test create action.
      Action action1 = createAction(NAME_1, DESCRIPTION_1, MSGID_1);
      checkCreateActionPass(action1, NAME_1);

      // test create duplicate action.
      Action action2 = createAction(NAME_1, DESCRIPTION_1, MSGID_1);
      checkCreateActionFail(action2, "Able to add in duplicate action into the database.");

      // test create 2nd action
      Action action3 = createAction(NAME_2, DESCRIPTION_2, MSGID_2);
      checkCreateActionPass(action3, NAME_2);

      // test create action with same actionName and desc but different msgId.
      Action action4 = createAction(NAME_1, DESCRIPTION_1, MSGID_2);
      checkCreateActionFail(action4, "Able to add in same alertName, Type and " +
                                       "desc but different category.");

      // test create action with same actionName and msgId but different desc.
      Action action5 = createAction(NAME_1, DESCRIPTION_2, MSGID_1);
      checkCreateActionFail(action5, "Able to add in same alertName, desc and action " +
                                       "but different dataType.");

      // test create action with same desc and msgId but different actionName.
      Action action6 = createAction(NAME_2, DESCRIPTION_1, MSGID_1);
      checkCreateActionFail(action6, "Unable to add Action with the same Name being created already ");

    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testCreateAction", "fails", ex);
      throw ex;
    }
    finally
    {
//      deleteTestAction(NAME_1);
      deleteTestAction(NAME_2);
    }
  }

  private void checkCreateActionPass(Action action, String actionName) throws Exception
  {
    remote.createAction(action);
  }

  private void checkCreateActionFail(Action action, String errorMessage)
  {
    try
    {
      remote.createAction(action);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }



  public void testGetActionByActionName() throws Exception
  {
    try
    {
      Action action = createAction(NAME_1, DESCRIPTION_1, MSGID_1);
      checkGetActionByActionNameSuccess(NAME_1, action);

      // Find non-exisitng
      checkGetActionByActionNameFail("Sample", "Deleting non-existing");
    }
    catch (Exception ex)
    {
      AlertLogger.errorLog(CLASSNAME, "testGetAlertByAlertName", "fails", ex);
      throw ex;
    }
    finally
    {
      deleteTestAction(NAME_1);
    }
  }

  private void checkGetActionByActionNameFail(String actionName, String errorMsg) throws Exception
  {
    Action findAction = remote.getActionByActionName(actionName);
    assertNull(errorMsg, findAction);
  }

  private void checkGetActionByActionNameSuccess(String actionName, Action action) throws Exception
  {
    Action findAction = remote.getActionByActionName(actionName);
    assertNotNull(findAction);
//    checkIdenticalAlert(action, findAction, false);
  }
}