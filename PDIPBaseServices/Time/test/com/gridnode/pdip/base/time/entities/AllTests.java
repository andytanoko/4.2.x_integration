// %1023788051059:com.gridnode.pdip.base.time%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities;

//import com.gridnode.pdip.base.time.entities.ejb.iCalComponentBeanTestCase;

import junit.framework.*;

import junit.runner.BaseTestRunner;

/**
 * TestSuite that runs all the sample tests
 */
public class AllTests
{

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite("All TimeEvent JUnit Tests");
    suite.addTest(new TestSuite(iCalEventTestCase.class));
 //   suite.addTest(new TestSuite(iCalComponentBeanTestCase.class));
    return suite;
  }
  
  public static void main(String[] args) throws Exception
  {
    junit.textui.TestRunner.run(suite());
  }  
}