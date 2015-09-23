/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocaleActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all Locale related tests for Actions
 * classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LocaleActionsTest extends TestSuite
{

  public static Test suiteCountry()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetCountryCodeActionTest.class);
    suite.addTestSuite(GetCountryCodeListActionTest.class);

    return suite;
  }

  public static Test suiteLanguage()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetLanguageCodeActionTest.class);
    suite.addTestSuite(GetLanguageCodeListActionTest.class);

    return suite;
  }

  public LocaleActionsTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new LocaleActionsTest();
    suite.addTest(suiteCountry());
    suite.addTest(suiteLanguage());

    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}