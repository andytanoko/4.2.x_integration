/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ClientTestSuite.java.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 19/05/2003     Andrew Hill         Created 
 */
package com.gridnode.gtas.client;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.gridnode.gtas.client.utils.TestDateUtils;
import com.gridnode.gtas.client.utils.TestStaticUtils;
import com.gridnode.gtas.client.web.strutsbase.TestFieldValidator;
public class ClientTestSuite
{
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(ClientTestSuite.class);
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for com.gridnode.gtas.client");
    //$JUnit-BEGIN$
    suite.addTestSuite(TestFieldValidator.class);
    suite.addTestSuite(TestStaticUtils.class);
    suite.addTestSuite(TestDateUtils.class);
    //$JUnit-END$
    return suite;
  }
}
