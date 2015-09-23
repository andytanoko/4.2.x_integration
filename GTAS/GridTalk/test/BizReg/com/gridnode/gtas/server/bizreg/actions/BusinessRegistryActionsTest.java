/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessRegistryActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 31 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all Business Registry related tests for Actions
 * classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class BusinessRegistryActionsTest extends TestSuite
{

  public static Test suiteBusinessEntity()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(CreateBusinessEntityActionTest.class);
    suite.addTestSuite(DeleteBusinessEntityActionTest.class);
    suite.addTestSuite(UpdateBusinessEntityActionTest.class);
    suite.addTestSuite(GetBusinessEntityActionTest.class);
    suite.addTestSuite(GetBusinessEntityListActionTest.class);

    return suite;
  }

  public BusinessRegistryActionsTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new BusinessRegistryActionsTest();
    suite.addTest(suiteBusinessEntity());

    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}