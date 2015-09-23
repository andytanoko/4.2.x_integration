/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 20 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.server.registration.actions.GetRegistrationInfoActionTest;
import com.gridnode.gtas.server.registration.actions.ConfirmRegistrationInfoActionTest;
import com.gridnode.gtas.server.registration.actions.CancelRegistrationInfoActionTest;
import com.gridnode.gtas.server.registration.actions.ValidateRegistrationInfoActionTest;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all Registration related tests for Actions
 * classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RegistrationActionsTest extends TestSuite
{

  public static Test suiteRegistration()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ValidateRegistrationInfoActionTest.class);
    suite.addTestSuite(CancelRegistrationInfoActionTest.class);
    suite.addTestSuite(ConfirmRegistrationInfoActionTest.class);
    suite.addTestSuite(GetRegistrationInfoActionTest.class);

    return suite;
  }

  public RegistrationActionsTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new RegistrationActionsTest();
    suite.addTest(suiteRegistration());
    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}