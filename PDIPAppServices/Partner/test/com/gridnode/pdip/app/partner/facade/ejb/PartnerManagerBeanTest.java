/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParterManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 09 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.facade.ejb;

import junit.framework.*;

/**
 * This class will invoke the other PartnerManagerBean test cases to test all the methods
 * in the PartnerManager.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerManagerBeanTest
{
  public static Test suitePartnerType()
  {
    return new TestSuite(PartnerManagerBeanPartnerTypeTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suitePartnerType());
  }
}