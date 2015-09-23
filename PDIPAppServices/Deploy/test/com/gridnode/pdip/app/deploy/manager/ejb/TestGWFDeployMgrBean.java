// %1023962283239:com.gridnode.pdip.app.deployment.ejb.manager%
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
 * Jun 10 2002   Mathew	        Created
 * Jun 13 2002   Mathew         Repackaged
 */


package com.gridnode.pdip.app.deploy.manager.ejb;

import com.gridnode.pdip.app.deploy.manager.bpss.*;
import com.gridnode.pdip.framework.j2ee.*;

import fixture.*;

import java.io.*;

import junit.framework.*;

public class TestGWFDeployMgrBean
  extends TestCase
{
  public static IGWFDeployMgrHome home;
  public static IGWFDeployMgrObj remote;

  /**
   * Creates a new TestGWFDeployMgrBean object.
   *
   * @param s DOCUMENT ME!
   */
  public TestGWFDeployMgrBean(String s)
  {
    super(s);
  }

  protected void setUp()
  {
    try
    {
      home = (IGWFDeployMgrHome)ServiceLookup.getInstance(
                                    ServiceLookup.CLIENT_CONTEXT).getHome(
                 IGWFDeployMgrHome.class);
      remote = home.create();
      assertNotNull("Session Object Not Null ", remote);
    }
    catch (Exception ex)
    {
      System.out.println(" Exception in SetUp  : " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  /*  public void testSingleDeploy() {
      File processSpec= new File("d:/development/PDIPGridFlow/Deploy/data/ebBPSStest.xml");
      try {
        boolean booleanRet = remote.load(processSpec);
        assertTrue(booleanRet);
        if (booleanRet) {
          System.out.println("Loaded xml file successfully");
          remote.deploy();
        } else {
          System.out.println("Failed to load xml file");
          System.exit(1);
        }
      }
      catch (Exception ex) {
        System.out.println(" Exception in testLoad  : "+ex.getMessage());
        ex.printStackTrace();
      }
    }
  */

  /**
   * DOCUMENT ME!
   */
  public void testMultiVersionDeploy()
  {
    boolean[] canFind = {true, true, true, true};
    String[] version = {"1.1", "1.2", "1.0", "1.1"};
    String entryName = "Product Fulfillment";
    //String entryType = "com.gridnode.gridflow.entity.BpssBinaryCollaboration";
    String entryType = "BpssBinaryCollaboration";
    for (int i = 0; i < 4; i++)
    {
      System.out.println("Deploy testing loop: " + String.valueOf(i + 1));
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/ebBPSStest" +
                             String.valueOf(i + 1).trim() + ".xml");
      try
      {
        remote.deployBpss(processSpec);
        if (canFind[i])
        {
            assertNotNull(TestUtilities.getSpec("[1234-5678-901234]",
                                            version[i], entryName,
                                            entryType));
        }
        else
        {
            assertNull(TestUtilities.getSpec("[1234-5678-901234]", version[i],
                                         entryName, entryType));
        }
      }
      catch (Exception ex)
      {
        System.out.println(" Exception in testLoad  : " + ex.getMessage());
        ex.printStackTrace();
      }
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testMultiVersionUndeploy()
  {
    boolean[] undeployResult = {true, true, true, true};
    String[] version = {"1.1", "1.2", "1.0", "1.1"};
    String entryName = "Firm Order";
    //String entryType = "com.gridnode.gridflow.entity.BpssBinaryCollaboration";
    String entryType = "BpssBinaryCollaboration";
    for (int i = 0; i < 4; i++)
    {
      System.out.println("Undeploy testing loop: " + String.valueOf(i + 1));
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/ebBPSStest" +
                             String.valueOf(i + 1).trim() + ".xml");
      try
      {
        if (undeployResult[i])
        {
            assertTrue(remote.undeployBpss(processSpec) >= 0);
        }
        else
        {
            assertTrue(remote.undeployBpss(processSpec) < 0);
        }
      }
      catch (Exception ex)
      {
        System.out.println(" Exception in testLoad  : " + ex.getMessage());
        ex.printStackTrace();
      }
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testMultiPartyCollaborationDeploy()
  {
    try
    {
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/ebBPSStest5.xml");
      System.out.println(processSpec);
      remote.deployBpss(processSpec);
    }
    catch (Exception e)
    {
      System.out.println("Failed to load xml file ebBPSStest5.xml");
      e.printStackTrace();
    }
  }

  public void testMultiPartyCollaborationUnDeploy()
  {
    try
    {
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/ebBPSStest5.xml");
      System.out.println(processSpec);
      remote.undeployBpss(processSpec);
    }
    catch (Exception e)
    {
      System.out.println("Failed to load xml file ebBPSStest5.xml");
      e.printStackTrace();
    }
  }

  public void ttestXpdlDeploy() throws Exception {
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/test_xpdldatatypes.xml");
                             //"/data/test_xpdl.xml");

    remote.deployXpdl(processSpec);
  }

  public void testXpdlUnDeploy() throws Exception {
      File processSpec = new File(
                             System.getProperty("user.dir") +
                             "/data/test_xpdldatatypes.xml");
                             //"/data/test_xpdl.xml");

      remote.undeployXpdl(processSpec);

  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public static Test suite()
  {
    return new TestSuite(TestGWFDeployMgrBean.class);
  }

  /**
   * DOCUMENT ME!
   *
   * @param args DOCUMENT ME!
   */
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }
}