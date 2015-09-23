/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessControlActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.acl.actions;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all access control related tests for Actions
 * classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AccessControlActionsTest extends TestSuite
{

  public static Test suiteRole()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(CreateRoleActionTest.class);
    suite.addTestSuite(DeleteRoleActionTest.class);
    suite.addTestSuite(UpdateRoleActionTest.class);
    suite.addTestSuite(GetRoleActionTest.class);
    suite.addTestSuite(GetRoleListActionTest.class);

    return suite;
  }

  public static Test suiteFeature()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetFeatureListActionTest.class);
    //missing GeteatureActionTest.class

    return suite;
  }

  public static Test suiteAccessRight()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(AddAccessRightActionTest.class);
    suite.addTestSuite(RemoveAccessRightActionTest.class);
    suite.addTestSuite(ModifyAccessRightActionTest.class);
    suite.addTestSuite(GetAccessRightActionTest.class);
    suite.addTestSuite(GetAccessRightListActionTest.class);

    return suite;
  }

  public static Test suiteSubjectRole()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(AddRoleToUserActionTest.class);
    suite.addTestSuite(RemoveRoleFromUserActionTest.class);
    suite.addTestSuite(GetRoleListForUserActionTest.class);
    suite.addTestSuite(GetUserListForRoleActionTest.class);

    return suite;
  }

  public AccessControlActionsTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new AccessControlActionsTest();
    suite.addTest(suiteAccessRight());
    suite.addTest(suiteFeature());
    suite.addTest(suiteRole());
    suite.addTest(suiteSubjectRole());

    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}