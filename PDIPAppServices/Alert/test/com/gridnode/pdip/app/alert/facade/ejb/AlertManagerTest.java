package com.gridnode.pdip.app.alert.facade.ejb;

import junit.framework.*;

public class AlertManagerTest
{

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(new TestSuite(AlertManagerBeanAlertTest.class));
    junit.textui.TestRunner.run(new TestSuite(AlertManagerBeanActionTest.class));
    junit.textui.TestRunner.run(new TestSuite(AlertManagerBeanAlertActionTest.class));
    junit.textui.TestRunner.run(new TestSuite(AlertManagerBeanMessageTemplateTest.class));
    junit.textui.TestRunner.run(new TestSuite(AlertManagerBeanAlertCategoryTest.class));
  }
}