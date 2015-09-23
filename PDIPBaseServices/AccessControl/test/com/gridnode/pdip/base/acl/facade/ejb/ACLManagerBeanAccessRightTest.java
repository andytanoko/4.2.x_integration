/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBeanAccessRightTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 22 2002    Neo Sok Lay             Created
 * Feb 08 2007		Alain Ah Ming						Change error logs to warning logs
 */
package com.gridnode.pdip.base.acl.facade.ejb;

import com.gridnode.pdip.base.acl.model.*;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.exceptions.*;

import junit.framework.*;

import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.sql.Timestamp;

/**
 * Test case for testing the methods related to AccessRights in
 * ACLManagerBean.<P>
 *
 * This tests the following business methods in the ACLManagerBean:
 * <LI>testCreateAccessRight()          - mgr.createAccessRight(AcessRight)</LI>
 * <LI>testUpdateAccessRight()          - mgr.updateAccessRight(AccessRight)</LI>
 * <LI>testRemoveAccessRight()          - mgr.removeAccessRight(UId)</LI>
 *                                        mgr.removeAccessRightsFromRole(RoleUID)</LI>
 *                                        mgr.removeAccessRightForFeature(Feature)</LI>
 * <LI>testGetAccessRightByUID()        - mgr.getAccessRight(accessRightUID)</LI>
 * <LI>testGetAccessRightsForRole()     - mgr.getAccessRightsForRole(roleUID)</LI>
 * <LI>testGetAccessRightsForFeature()  - mgr.getAccessRightsFor(roleUID,feature)</LI>
 * <LI>testGetAccessRightsForAction()   - mgr.getAccessRightsFor(roleUID,feature,action)</LI>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLManagerBeanAccessRightTest
  extends    TestCase
{
  private static final Long   ROLE_UID_1    = new Long(-100);
  private static final String FEATURE_1     = "Test Feature 1";
  private static final String DESC_1        = "Test Description 1";
  private static final String ACTION_1      = "Test Action 1";
  private static final String DATA_TYPE_1   = "";
  private static final IDataFilter CRITERIA_1 = null;
  private static final boolean CAN_DELETE_1 = true;

  private static final Long   ROLE_UID_2    = new Long(-120);
  private static final String FEATURE_2     = "Test Feature 2";
  private static final String DESC_2        = "Test Description 2";
  private static final String ACTION_2      = "Test Action 2";
  private static final String DATA_TYPE_2   = "Test Data Type 2";
  private static final IDataFilter CRITERIA_2 = new DataFilterImpl();
  private static final boolean CAN_DELETE_2 = true;

  private static final String DATA_TYPE_3   = "Test Data Type 3";

  //for domain type filter
  private static final String CRI_FIELD_ID_1 = "TEST_FIELD_ID_1";
  private static final Collection CRI_LIST_1 = new ArrayList();
  private static final String CRI_VALUE_1_1 = "TEST_VALUE_1_1";
  private static final String CRI_VALUE_1_2 = "TEST_VALUE_1_2";
  private static final String CRI_VALUE_1_3 = "TEST_VALUE_1_3";

  //for range type filter
  private static final String CRI_FIELD_ID_2 = "TEST_FIELD_ID_2";
  private static final Integer CRI_VALUE_2_1 = new Integer(1);
  private static final Long CRI_VALUE_2_2    = new Long(999999);

  //for single type filter
  private static final String CRI_FIELD_ID_3 = "TEST_FIELD_ID_3";
  private static final Boolean CRI_VALUE_3   = Boolean.TRUE;

  static
  {
    CRI_LIST_1.add(CRI_FIELD_ID_1);
    CRI_LIST_1.add(CRI_FIELD_ID_2);
    CRI_LIST_1.add(CRI_FIELD_ID_3);

    CRITERIA_2.addDomainFilter(null, CRI_FIELD_ID_1, CRI_LIST_1, false);

    CRITERIA_2.addRangeFilter(CRITERIA_2.getAndConnector(),
      CRI_FIELD_ID_2, CRI_VALUE_2_1, CRI_VALUE_2_2, false);

    CRITERIA_2.addSingleFilter(CRITERIA_2.getOrConnector(),
      CRI_FIELD_ID_3, CRITERIA_2.getEqualOperator(), CRI_VALUE_3, true);
  }

  private static final String CLASSNAME = "ACLManagerBeanAccessRightTest";

  IACLManagerHome home;
  IACLManagerObj remote;

  public ACLManagerBeanAccessRightTest(String name)
  {
    super(name);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    return new TestSuite(ACLManagerBeanAccessRightTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    ACLLogger.infoLog(CLASSNAME, "setUp", "Enter");
    home = (IACLManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IACLManagerHome.class);
    assertNotNull("Home is null", home);
    remote = home.create();
    assertNotNull("remote is null", remote);
    cleanup();
    ACLLogger.infoLog(CLASSNAME,"setUp", "Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    cleanup();
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  // ************** Test Cases *********************

  public void testCreateAccessRight() throws Exception
  {
    try
    {
      // test create access right.
      AccessRight acr1 = createAccessRight(
                           ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                           CRITERIA_1, CAN_DELETE_1);
      checkCreateAccessRightPass(acr1);

      // test create with roleUID (mandatory field) null
      AccessRight acr2 = createAccessRight(
                           null, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                           CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to add in with roleUID null.");

      // test create with feature (mandatory field) null
      acr2 = createAccessRight(
               ROLE_UID_2, null, DESC_2, ACTION_2, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to add in with feature null.");

      // test create with description (mandatory field) null
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, null, ACTION_2, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to add in with description null.");

      // test create with action (mandatory field) null
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, DESC_2, null, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to add in with action null.");

      //test create duplicate
      acr2 = createAccessRight(
               ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
               CRITERIA_1, CAN_DELETE_1);
      checkCreateAccessRightFail(acr2, "Able to create duplicate with null data type");

      //test create duplicate with non null data type
      acr2 = createAccessRight(
               ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_1);
      checkCreateAccessRightFail(acr2, "Able to create duplicate with non-null data type");

      // test create 2nd access right
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightPass(acr2);

      // test create duplicate with null data type
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_1,
               CRITERIA_1, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to create duplicate with null data type");

      //test create duplicate with same data type
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightFail(acr2, "Able to create duplicate with same data type");

      // test create with different data type only.
      acr2 = createAccessRight(
               ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2 +"a",
               CRITERIA_2, CAN_DELETE_2);
      checkCreateAccessRightPass(acr2);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testCreateAccessRight", "fails", ex);
      throw ex;
    }
  }


  public void testUpdateAccessRight() throws Exception
  {
    try
    {
      AccessRight existing = null;

      // test update non-existing access right.
      AccessRight acr1 = createAccessRight(
                           ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                           CRITERIA_1, CAN_DELETE_1);
      checkUpdateAccessRightFail(acr1, "Able to update non-existing access right.");

      // create access right in database
      existing = addTestAccessRight(
                   ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                   CRITERIA_1, CAN_DELETE_1);

      // test update existing access right change desc2.
      existing.setDescr(DESC_2);
      checkUpdateAccessRightPass(existing);

      // test update existing access right change feature but using old version.
      existing.setFeature(FEATURE_2);
      checkUpdateAccessRightFail(existing, "Concurrent modification not detected!");

      // test update existing role change feature using updated version.
      existing = retrieveTestAccessRight((Long)existing.getKey());
      existing.setFeature(FEATURE_2);
      checkUpdateAccessRightPass(existing);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testUpdateAccessRight", "fails", ex);
      throw ex;
    }
  }

  public void testRemoveAccessRight() throws Exception
  {
    try
    {
      AccessRight insert1 = null;
      AccessRight insert2 = null;
      AccessRight insert3 = null;
      AccessRight insert4 = null;

      // Delete non-existing Role
      AccessRight acr1 = createAccessRight(
                           ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                           CRITERIA_1, CAN_DELETE_1);
      checkDeleteAccessRightFail(acr1, "delete non-existing access right");

      // Create AccessRight.
      insert1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      // Delete existing access right
      checkDeleteAccessRightPass(insert1);

      //add one access right for Role 1, feature 1
      insert1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      //add another for Role 1, feature 2
      insert2 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      //add another for Role 2, feature 1
      insert3 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      //add another for Role 2, feature 2
      insert4 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_1);

      // 1 & 2 will be deleted
      checkDeleteAccessRightByRolePass(
        new Long[] {(Long)insert1.getKey(), (Long)insert2.getKey()},
        ROLE_UID_1, "Access right for role not fully deleted");

      // 3 will be deleted
      checkDeleteAccessRightByFeaturePass(
        new Long[] {(Long)insert3.getKey()},
        FEATURE_1, "Access right for feature not fully deleted");

      // 4 is not deleted
      AccessRight retrieve = retrieveTestAccessRight((Long)insert4.getKey());
      assertNotNull("Access right incorrectly deleted.", retrieve);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testDeleteAccessRight", "fails", ex);
      throw ex;
    }
  }

  public void testGetAccessRightByUID() throws Exception
  {
    try
    {
      AccessRight exist = addTestAccessRight(
                            ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                            CRITERIA_1, CAN_DELETE_1);

      // Find exisitng
      AccessRight mock = createAccessRight(
                            ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                            CRITERIA_1, CAN_DELETE_1);
      checkGetAccessRightByUIDSuccess(
        exist.getUId(), mock);

      deleteTestAccessRight((Long)exist.getKey());

      // Find non-exisitng
      checkGetAccessRightByUIDFail(exist.getUId(), "Deleting non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightByUID", "fails", ex);
      throw ex;
    }

  }

  public void testGetAccessRightsForRole() throws Exception
  {

    try
    {
      AccessRight exist1 = null;
      AccessRight exist2 = null;
      AccessRight exist3 = null;

      exist1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      exist2 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      exist3 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);


      // Find exisitng
      AccessRight mock1 = createAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      AccessRight mock2 = createAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      AccessRight mock3 = createAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      // check only 1 & 3 retrieved
      checkGetAccessRightsByRoleUIDSuccess(
        ROLE_UID_1,
        new long[] {exist1.getUId(), exist3.getUId()},
        new AccessRight[] { mock1, mock3});

      // check only 2 retrieved
      checkGetAccessRightsByRoleUIDSuccess(
        ROLE_UID_2,
        new long[] {exist2.getUId()},
        new AccessRight[] { mock2});

      deleteTestAccessRights(ROLE_UID_1);

      // Find non-exisitng
      checkGetAccessRightsByRoleUIDFail(
        ROLE_UID_1, "Get non exisitng does not return empty collection");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightsForRole", "fails", ex);
      throw ex;
    }
  }

  public void testGetAccessRightsForFeature() throws Exception
  {

    try
    {
      AccessRight exist1 = null;
      AccessRight exist2 = null;
      AccessRight exist3 = null;
      AccessRight exist4 = null;

      exist1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      exist2 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      exist3 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      exist4 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      //mock access rights
      AccessRight mock1 = createAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      AccessRight mock2 = createAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      AccessRight mock3 = createAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      AccessRight mock4 = createAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      // check only 3 & 4 retrieved
      checkGetAccessRightsByFeatureSuccess(
        ROLE_UID_1,
        FEATURE_2,
        new long[] {exist3.getUId(), exist4.getUId()},
        new AccessRight[] { mock3, mock4});

      // check only 1 retrieved
      checkGetAccessRightsByFeatureSuccess(
        ROLE_UID_1,
        FEATURE_1,
        new long[] {exist1.getUId()},
        new AccessRight[] { mock1});

      // check none retrieved
      checkGetAccessRightsByFeatureFail(
        ROLE_UID_2,
        FEATURE_1,
         "Get non exisitng does not return empty collection");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightsForFeature", "fails", ex);
      throw ex;
    }
  }

  public void testGetAccessRightsForAction() throws Exception
  {

    try
    {
      AccessRight exist1 = null;
      AccessRight exist2 = null;
      AccessRight exist3 = null;
      AccessRight exist4 = null;

      exist1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_3,
                  CRITERIA_1, CAN_DELETE_1);

      exist2 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      exist3 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      exist4 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      //mock access rights
      AccessRight mock1 = createAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_3,
                  CRITERIA_1, CAN_DELETE_1);

      AccessRight mock2 = createAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      AccessRight mock3 = createAccessRight(
                  ROLE_UID_1, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      AccessRight mock4 = createAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      // check only 1 & 4 retrieved
      checkGetAccessRightsByActionSuccess(
        ROLE_UID_1,
        FEATURE_1,
        ACTION_1,
        new long[] {exist1.getUId(), exist4.getUId()},
        new AccessRight[] { mock1, mock4});

      // check only 2 retrieved
      checkGetAccessRightsByActionSuccess(
        ROLE_UID_2,
        FEATURE_2,
        ACTION_2,
        new long[] {exist2.getUId()},
        new AccessRight[] { mock2});

      // check none retrieved
      checkGetAccessRightsByActionFail(
        ROLE_UID_1,
        FEATURE_1,
        ACTION_2,
         "Get non exisitng does not return empty collection");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightsForAction", "fails", ex);
      throw ex;
    }
  }

  public void testGetAccessRightsByFilter() throws Exception
  {
    try
    {
      AccessRight exist1, exist2;

      exist1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      exist2 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      DataFilterImpl filter = null;

      // Find All
      checkGetAccessRightsByFilterSuccess(new AccessRight[] {exist1, exist2}, null);

      // Find None
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, AccessRight.UID, filter.getEqualOperator(), null, false);
      checkGetAccessRightsByFilterFail(new AccessRight[] {exist1, exist2}, "Find none", filter);

      // Find one
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, AccessRight.DESCRIPTION, filter.getEqualOperator(), DESC_1, false);
      checkGetAccessRightsByFilterSuccess(new AccessRight[] {exist1}, filter);
      checkGetAccessRightsByFilterFail(new AccessRight[]{exist2}, "Find 1", filter);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightsByFilter", "fails", ex);
      throw ex;
    }
  }

  public void testGetAccessRightsKeysByFilter() throws Exception
  {
    try
    {
      AccessRight exist1, exist2;

      exist1 = addTestAccessRight(
                  ROLE_UID_1, FEATURE_1, DESC_1, ACTION_1, DATA_TYPE_1,
                  CRITERIA_1, CAN_DELETE_1);

      exist2 = addTestAccessRight(
                  ROLE_UID_2, FEATURE_2, DESC_2, ACTION_2, DATA_TYPE_2,
                  CRITERIA_2, CAN_DELETE_2);

      DataFilterImpl filter = null;

      // Find All
      checkGetAccessRightsKeysByFilterSuccess(new long[] {exist1.getUId(), exist2.getUId()}, null);

      // Find None
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, AccessRight.UID, filter.getEqualOperator(), null, false);
      checkGetAccessRightsKeysByFilterFail(new long[] {exist1.getUId(), exist2.getUId()}, "Find none", filter);

      // Find one
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, AccessRight.DESCRIPTION, filter.getEqualOperator(), DESC_1, false);
      checkGetAccessRightsKeysByFilterSuccess(new long[] {exist1.getUId()}, filter);
      checkGetAccessRightsKeysByFilterFail(new long[]{exist2.getUId()}, "Find 1", filter);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetAccessRightsByFilter", "fails", ex);
      throw ex;
    }
  }

  // ********************** Checking methods ************************
  private void checkCreateAccessRightFail(AccessRight acr, String errorMessage)
  {
    try
    {
      remote.createAccessRight(acr);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
      ACLLogger.debugLog(CLASSNAME, "testCreateAccessRight", "exception caught", ex);
    }
  }

  private void checkCreateAccessRightPass(AccessRight acr) throws Exception
  {
    Long uID = remote.createAccessRight(acr);
    AccessRight retrieved = retrieveTestAccessRight(uID);
    assertNotNull("retrieve null", retrieved);
    checkIdenticalAccessRight(acr, retrieved, false);
  }

  private void checkUpdateAccessRightFail(AccessRight acr, String errorMessage)
  {
    try
    {
      remote.updateAccessRight(acr);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
      ACLLogger.debugLog(CLASSNAME, "testUpdateAccessRight", "exception caught", ex);
    }
  }

  private void checkUpdateAccessRightPass(AccessRight acr) throws Exception
  {
    remote.updateAccessRight(acr);
    AccessRight retrieved = retrieveTestAccessRight((Long)acr.getKey());
    assertNotNull("retrieve null", retrieved);
    checkIdenticalAccessRight(acr, retrieved, true);
  }

  private void checkDeleteAccessRightFail(AccessRight acr, String errorMessage) throws Exception
  {
    try
    {
      remote.removeAccessRight(new Long(acr.getUId()));
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
      ACLLogger.debugLog(CLASSNAME, "checkDeleteAccessRightFail", "exception caught", ex);
    }
  }

  private void checkDeleteAccessRightByRolePass(
    Long[] acrUIDs, Long roleUID, String errorMessage) throws Exception
  {
    remote.removeAccessRightsFromRole(roleUID);

    for (int i=0; i<acrUIDs.length; i++)
    {
      try
      {
        AccessRight retrieve = retrieveTestAccessRight(acrUIDs[i]);
        assertNull("Delete by role not successful.", retrieve);
      }
      catch (FindEntityException ex)
      {
      }
    }
  }

  private void checkDeleteAccessRightByFeaturePass(
    Long[] acrUIDs, String feature, String errorMessage) throws Exception
  {
    remote.removeAccessRightsForFeature(feature);

    for (int i=0; i<acrUIDs.length; i++)
    {
      try
      {
        AccessRight retrieve = retrieveTestAccessRight(acrUIDs[i]);
        assertNull("Delete by feature not successful.", retrieve);
      }
      catch (FindEntityException ex)
      {
      }
    }
  }

  private void checkDeleteAccessRightPass(AccessRight acr) throws Exception
  {
    remote.removeAccessRight(new Long(acr.getUId()));
    try
    {
      AccessRight retrieve = retrieveTestAccessRight(new Long(acr.getUId()));
      assertNull("Delete not successful.", retrieve);
    }
    catch (FindEntityException ex)
    {
    }
  }

  private void checkGetAccessRightByUIDFail(long uId, String errorMessage)
    throws Exception
  {
    try
    {
      AccessRight acr = remote.getAccessRight(new Long(uId));
      assertNull(errorMessage, acr);
    }
    catch (FindEntityException ex)
    {
    }
  }

  private void checkGetAccessRightByUIDSuccess(long uId, AccessRight acr)  throws Exception
  {
    AccessRight findAcr = remote.getAccessRight(new Long(uId));
    assertNotNull(findAcr);
    checkIdenticalAccessRight(acr, findAcr, false);
  }

  private void checkGetAccessRightsByRoleUIDFail(
    Long roleUID, String errorMsg)
          throws Exception
  {
    Collection findAcrs = remote.getAccessRightsForRole(roleUID);
    assertTrue(errorMsg, findAcrs.isEmpty());
  }

  private void checkGetAccessRightsByRoleUIDSuccess(
    Long roleUID, long[] acrUIDs, AccessRight[] acrs)
    throws Exception
  {
    Collection findAcrs = remote.getAccessRightsForRole(roleUID);

    for (int i=0; i<acrUIDs.length; i++)
    {
      boolean found = false;
      Iterator iter = findAcrs.iterator();
      while (iter.hasNext())
      {
        AccessRight acr = (AccessRight)iter.next();
        if (acr.getUId() == acrUIDs[i])
        {
          checkIdenticalAccessRight(acr, acrs[i], false);
          found = true;
        }
      }
      if (!found)
        assertTrue("Access right not retrieved by roleUID!", false);
    }
  }

  private void checkGetAccessRightsByFeatureFail(
    Long roleUID, String feature, String errorMsg)
          throws Exception
  {
    Collection findAcrs = remote.getAccessRightsFor(roleUID, feature);
    assertTrue(errorMsg, findAcrs.isEmpty());
  }

  private void checkGetAccessRightsByFeatureSuccess(
    Long roleUID, String feature, long[] acrUIDs, AccessRight[] acrs)
    throws Exception
  {
    Collection findAcrs = remote.getAccessRightsFor(roleUID, feature);

    for (int i=0; i<acrUIDs.length; i++)
    {
      boolean found = false;
      Iterator iter = findAcrs.iterator();
      while (iter.hasNext())
      {
        AccessRight acr = (AccessRight)iter.next();
        if (acr.getUId() == acrUIDs[i])
        {
          checkIdenticalAccessRight(acr, acrs[i], false);
          found = true;
        }
      }
      if (!found)
        assertTrue("Access right not retrieved by roleUID!", false);
    }
  }

  private void checkGetAccessRightsByActionFail(
    Long roleUID, String feature, String action, String errorMsg)
          throws Exception
  {
    Collection findAcrs = remote.getAccessRightsFor(roleUID, feature, action);
    assertTrue(errorMsg, findAcrs.isEmpty());
  }

  private void checkGetAccessRightsByActionSuccess(
    Long roleUID, String feature, String action, long[] acrUIDs, AccessRight[] acrs)
    throws Exception
  {
    Collection findAcrs = remote.getAccessRightsFor(roleUID, feature, action);

    for (int i=0; i<acrUIDs.length; i++)
    {
      boolean found = false;
      Iterator iter = findAcrs.iterator();
      while (iter.hasNext())
      {
        AccessRight acr = (AccessRight)iter.next();
        if (acr.getUId() == acrUIDs[i])
        {
          checkIdenticalAccessRight(acr, acrs[i], false);
          found = true;
        }
      }
      if (!found)
        assertTrue("Access right not retrieved by roleUID!", false);
    }
  }

  private void checkGetAccessRightsByFilterSuccess(
    AccessRight[] existAcrs, DataFilterImpl filter)
          throws Exception
  {
    Collection findAcrs = remote.getAccessRights(filter);

    for (int i=0; i<existAcrs.length; i++)
    {
      Iterator iter = findAcrs.iterator();
      boolean found = false;
      while (iter.hasNext() && !found)
      {
        AccessRight retrieved = (AccessRight) iter.next();
        if (retrieved.getUId() == existAcrs[i].getUId())
        {
          checkIdenticalAccessRight(retrieved, existAcrs[i], false);
          found = true;
        }
      }
      if (!found)
        assertTrue("Access right not retrieved by filter!", false);
    }
  }

  private void checkGetAccessRightsByFilterFail(
    AccessRight[] nonExistAcr,
    String errorMsg, DataFilterImpl filter)
          throws Exception
  {
    Collection findAcrs = remote.getAccessRights(filter);

    for (int i=0; i<nonExistAcr.length; i++)
    {
      Iterator iter = findAcrs.iterator();
      while (iter.hasNext())
      {
        AccessRight retrieved = (AccessRight) iter.next();
        if (retrieved.getUId() == nonExistAcr[i].getUId())
          assertTrue(errorMsg, false);
      }
    }
  }

  private void checkGetAccessRightsKeysByFilterSuccess(
    long[] existAcrUIds, DataFilterImpl filter)
          throws Exception
  {
    Collection findAcrUIds = remote.getAccessRightsKeys(filter);

    for (int i=0; i<existAcrUIds.length; i++)
    {
      Iterator iter = findAcrUIds.iterator();
      boolean found = false;
      while (iter.hasNext() && !found)
      {
        Long retrieved = (Long) iter.next();
        if (retrieved.longValue() == existAcrUIds[i])
        {
          found = true;
        }
      }
      if (!found)
        assertTrue("Access right UID not retrieved by filter!", false);
    }
  }

  private void checkGetAccessRightsKeysByFilterFail(
    long[] nonExistAcrUIds,
    String errorMsg, DataFilterImpl filter)
          throws Exception
  {
    Collection findAcrUIDs = remote.getAccessRightsKeys(filter);

    for (int i=0; i<nonExistAcrUIds.length; i++)
    {
      Iterator iter = findAcrUIDs.iterator();
      while (iter.hasNext())
      {
        Long retrieved = (Long) iter.next();
        if (retrieved.longValue() == nonExistAcrUIds[i])
          assertTrue(errorMsg, false);
      }
    }
  }

  // ******************** Helper methods **********************************
  protected void cleanup() throws Exception
  {
    deleteTestAccessRights(ROLE_UID_1);
    deleteTestAccessRights(ROLE_UID_2);
  }

  private AccessRight createAccessRight(
    Long roleUID, String feature, String description,
    String action, String dataType, IDataFilter criteria,
    boolean canDelete)
  {
    AccessRight accessRight = new AccessRight();
    accessRight.setAction(action);
    accessRight.setCanDelete(canDelete);
    accessRight.setCriteria(criteria);
    accessRight.setDataType(dataType);
    accessRight.setDescr(description);
    accessRight.setFeature(feature);
    accessRight.setRoleUID(roleUID);
    return accessRight;
  }

  private AccessRight addTestAccessRight(
    Long roleUID, String feature, String description,
    String action, String dataType, IDataFilter criteria,
    boolean canDelete) throws Exception
  {
    AccessRight inserted = createAccessRight(roleUID, feature, description,
                             action, dataType, criteria, canDelete);
    Long uID = remote.createAccessRight(inserted);
    return remote.getAccessRight(uID);
  }

  private void checkIdenticalAccessRight(AccessRight acr1, AccessRight acr2,
    boolean checkUId)
  {
    assertEquals("Criteria not the same!", acr1.getCriteria(), acr2.getCriteria());
    assertEquals("DataType not the same!", acr1.getDataType(), acr2.getDataType());
    assertEquals("Action not the same!", acr1.getAction(), acr2.getAction());
    assertEquals("Feature not the same!", acr1.getFeature(), acr2.getFeature());
    assertEquals("Description not the same!", acr1.getDescr(), acr2.getDescr());
    assertEquals("Role UID not the same!", acr1.getRoleUID(), acr2.getRoleUID());
    assertEquals("canDelete not the same!", acr1.canDelete(), acr2.canDelete());
    if (checkUId)
       assertEquals("UId not the same!", acr1.getUId(), acr2.getUId());
  }

  private AccessRight retrieveTestAccessRight(Long accessRightUId)
          throws Exception
  {
    return remote.getAccessRight(accessRightUId);
  }

  private Collection retrieveTestAccessRights(Long roleUId)
          throws Exception
  {
    return remote.getAccessRightsForRole(roleUId);
  }

  private void deleteTestAccessRights(Long roleUId) throws Exception
  {
    remote.removeAccessRightsFromRole(roleUId);
  }

  private void deleteTestAccessRight(Long accessRightUId) throws Exception
  {
    remote.removeAccessRight(accessRightUId);
  }

  private void deleteTestAccessRights(String feature) throws Exception
  {
    remote.removeAccessRightsForFeature(feature);
  }


}