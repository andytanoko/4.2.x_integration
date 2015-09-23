/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15 2002    Neo Sok Lay         Created
 * Aug 30 2002    Neo Sok Lay         Add test suite for sending Be.
 */
package com.gridnode.gtas.server.enterprise;

import com.gridnode.gtas.server.enterprise.actions.*;
import com.gridnode.gtas.server.enterprise.facade.ejb.ResourceLinkManagerBeanTest;
import com.gridnode.gtas.server.enterprise.listener.ejb.ResourceChangeListenerMDBeanTest;
import com.gridnode.gtas.server.enterprise.sync.handlers.SyncBizEntityHandlerTest;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * This test suite class collects all Enterprise Management related tests for
 * Actions classes.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class EnterpriseTest extends TestSuite
{

  public static Test suiteBeChannel()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetChannelListForBizEntityActionTest.class);
    suite.addTestSuite(SetChannelListForBizEntityActionTest.class);

    return suite;
  }

  public static Test suiteUserBe()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetBizEntityListForUserActionTest.class);
    suite.addTestSuite(SetBizEntityListForUserActionTest.class);

    return suite;
  }

  public static Test suitePartnerBe()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(GetBizEntityForPartnerActionTest.class);
    suite.addTestSuite(SetBizEntityForPartnerActionTest.class);

    return suite;
  }

  public static Test suiteSendBe()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(SendBusinessEntityActionTest.class);
    suite.addTestSuite(SyncBizEntityHandlerTest.class);

    return suite;
  }

  public static Test suiteEJBeans()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ResourceLinkManagerBeanTest.class);
    suite.addTestSuite(ResourceChangeListenerMDBeanTest.class);

    return suite;
  }

  public EnterpriseTest()
  {
    super();
  }

  public static Test allSuite()
  {
    TestSuite suite = new EnterpriseTest();
    suite.addTest(suiteEJBeans());
    suite.addTest(suiteBeChannel());
    suite.addTest(suitePartnerBe());
    suite.addTest(suiteUserBe());
    suite.addTest(suiteSendBe());
    return suite;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(allSuite());
  }

}