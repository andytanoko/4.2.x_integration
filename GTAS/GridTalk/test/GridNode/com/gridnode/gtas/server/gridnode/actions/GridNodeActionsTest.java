/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all GridNode related tests for Actions
 * classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GridNodeActionsTest extends TestSuite
{

  public static Test suiteCoyProfile()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(SaveMyCompanyProfileActionTest.class);
    suite.addTestSuite(GetMyCompanyProfileActionTest.class);

    return suite;
  }

  public static Test suiteGnCategory()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetGnCategoryActionTest.class);
    suite.addTestSuite(GetGnCategoryListActionTest.class);

    return suite;
  }

  public GridNodeActionsTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new GridNodeActionsTest();
    suite.addTest(suiteCoyProfile());
    suite.addTest(suiteGnCategory());
    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}