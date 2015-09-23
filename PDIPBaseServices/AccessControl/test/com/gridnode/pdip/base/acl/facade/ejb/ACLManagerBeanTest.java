/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * May 22 2002    Goh Kan Mun             Modified - to be able to run each entity test
 *                                                   case separately and this class
 *                                                   will invoke all the other entity
 *                                                   test cases.
 * Jun 07 2002    Neo Sok Lay             Add suite AccessRight.
 */

package com.gridnode.pdip.base.acl.facade.ejb;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test case for testing ACLManagerBean<P>
 *
 * This class will invoke the other ACLManagerBean test cases to test all the methods in the
 * ACLManager.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class ACLManagerBeanTest
{
  private static final String CLASSNAME = "ACLManagerBeanTest";

  IACLManagerHome home;
  IACLManagerObj remote;

  public static Test suiteRole()
  {
    return new TestSuite(ACLManagerBeanRoleTest.class);
  }

  public static Test suiteFeature()
  {
    return new TestSuite(ACLManagerBeanFeatureTest.class);
  }

  public static Test suiteSubjectRole()
  {
    return new TestSuite(ACLManagerBeanSubjectRoleTest.class);
  }

  public static Test suiteAccessRight()
  {
    return new TestSuite(ACLManagerBeanAccessRightTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suiteRole());
    junit.textui.TestRunner.run(suiteSubjectRole());
    junit.textui.TestRunner.run(suiteFeature());
    junit.textui.TestRunner.run(suiteAccessRight());
  }

}