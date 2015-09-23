package com.gridnode.pdip.base.rnif.helper;

import junit.framework.*;
import junit.runner.BaseTestRunner;

/**
 * TestSuite that runs all the sample tests
 *
 */
public class AllTests
{
   public AllTests(String arg0)
  {
    
  }
  public  Test suite ( )
  {
    TestSuite suite= new TestSuite("All Process JUnit Tests");
    suite.addTest(new TestSuite(RNPackagerTest.class));
    return suite;
  }
    public static void main(String[] args)
    {
       // junit.swingui.TestRunner.run(AllTests.class);
      junit.textui.TestRunner.run(RNPackagerTest.class);
    }


}