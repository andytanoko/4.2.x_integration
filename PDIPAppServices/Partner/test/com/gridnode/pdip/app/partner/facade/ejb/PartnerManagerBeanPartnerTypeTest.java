/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerManagerBeanPartnerTypeTest.java
 *
 *********************************************************************************************
 * Date           Author              Changes
 *********************************************************************************************
 * Jun 07 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.facade.ejb;

import com.gridnode.pdip.app.partner.model.PartnerType;

// import framework related package
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import junit.framework.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Partial test case for testing PartnerManagerBean. This class tests methods in the manager
 * that involve only with the <code>PartnerType</code> entity.<P>
 *
 * This tests the following business methods in the PartnerManagerBean:
 * <LI>testCreate()     - mgr.createPartnerType(PartnerType)</LI>
 * <LI>testUpdate()     - mgr.updatePartnerType(PartnerType)</LI>
 * <LI>testDelete()     - mgr.deletePartnerType(UId)</LI>
 * <LI>testGetByName()  - mgr.findPartnerTypeByName(Name)</LI>
 * <P>
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public class PartnerManagerBeanPartnerTypeTest extends TestCase
{
  static final String NEW_NAME        = "3PL";
  static final String NEW_DESCRIPTION = "Third Party Logistic";

  static final String UPD_NAME        = "3PL";
  static final String UPD_DESCRIPTION = "3rd Party Logistic";

  IPartnerManagerHome home;
  IPartnerManagerObj  remote;

  private PartnerType partnerType    = null;
  private PartnerType newPartnerType = new PartnerType();
  private PartnerType updPartnerType = new PartnerType();

  public PartnerManagerBeanPartnerTypeTest(String name)
  {
    super(name);
    initTestEnvironment();
  }

  public static Test suite()
  {
    return new TestSuite(PartnerManagerBeanPartnerTypeTest.class);
  }

  public void initTestEnvironment()
  {
    try
    {
      home = (IPartnerManagerHome)ServiceLocator.instance(
               ServiceLocator.LOCAL_CONTEXT).getHome(
                IPartnerManagerHome.class.getName(),
                IPartnerManagerHome.class);
      assertNotNull("Home is null", home);

      // initialise remote object here
      remote = home.create();
      assertNotNull("remote is null", remote);

      // initialise test data for create
      newPartnerType.setName(NEW_NAME);
      newPartnerType.setDescription(NEW_DESCRIPTION);

      // initialise test data for update
      updPartnerType.setName(UPD_NAME);
      updPartnerType.setDescription(UPD_DESCRIPTION);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "Error initialising test environment", ex);
    }
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.setUp] Enter");
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.setUp] Exit");
  }

  protected void tearDown() throws Exception
  {
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.tearDown] Exit");
  }


  public void testCreate() throws Exception
  {
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testCreate] Enter");
    try
    {
      remote.createPartnerType(newPartnerType);
      assertOnCreate();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerManagerBeanPartnerTypeTest.testCreate]", ex);
      throw ex;
    }
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testCreate] Exit");
  }

  public void testUpdate() throws Exception
  {
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testUpdate] Enter");
    try
    {
      remote.createPartnerType(newPartnerType);
      partnerType = remote.findPartnerTypeByName(NEW_NAME);
      updPartnerType.setKey(partnerType.getKey());
      remote.updatePartnerType(updPartnerType);
      assertOnUpdate();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerManagerBeanPartnerTypeTest.testUpdate]", ex);
      throw ex;
    }
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testUpdate] Exit");
  }
//
  public void testDelete() throws Exception
  {
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testDelete] Enter");
    try
    {
      remote.createPartnerType(newPartnerType);
      partnerType = remote.findPartnerTypeByName(NEW_NAME);
      remote.deletePartnerType((Long)partnerType.getKey());
      assertOnDelete();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[PartnerManagerBeanPartnerTypeTest.testDelete]", ex);
      throw ex;
    }
    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.testDelete] Exit");
  }

  private void assertOnCreate() throws Exception
  {
    partnerType = remote.findPartnerTypeByName(NEW_NAME);
    assertNotNull("Null Record", partnerType);
    assertEquals("Mistmatch Name", NEW_NAME, partnerType.getName());
    assertEquals("Mistmatch Description", NEW_DESCRIPTION, partnerType.getDescription());
  }

  private void assertOnUpdate() throws Exception
  {
    partnerType = remote.findPartnerTypeByName(UPD_NAME);
    assertNotNull("Null Record", partnerType);
    assertEquals("Mistmatch Name", UPD_NAME, partnerType.getName());
    assertEquals("Mistmatch Description", UPD_DESCRIPTION, partnerType.getDescription());
  }

  private void assertOnDelete() throws Exception
  {
    partnerType = remote.findPartnerTypeByName(NEW_NAME);
    assertNull("Delete not successful.", partnerType);
  }

  private void cleanUp() throws Exception
  {
    Collection collection = remote.findPartnerType((DataFilterImpl)null);
    if ((collection == null) ||(collection.isEmpty()))
      return;

    Log.log("TEST", "[PartnerManagerBeanPartnerTypeTest.cleanUp] No. of Records: "
            + collection.size());
    for (Iterator i=collection.iterator(); i.hasNext();)
      remote.deletePartnerType((Long)((PartnerType)i.next()).getKey());
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }
}