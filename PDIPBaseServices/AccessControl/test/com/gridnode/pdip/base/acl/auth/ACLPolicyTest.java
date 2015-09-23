/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLPolicyTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002    Neo Sok Lay         Created
 *                                    Criteria is ignored for checking for now.
 * Feb 08 2007		Alain Ah Ming				Change error log to warning log                                   
 */
package com.gridnode.pdip.base.acl.auth;

import java.security.Policy;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;

import javax.security.auth.Subject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This test case tests the Access control framework for checking AccessPermissions
 * based on the RolePrincipals associated with a subject.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class ACLPolicyTest extends TestCase
{
  private static final String CLASSNAME = "ACLPolicyTest";

  private static final String ROLE_NAME = "ROLE.";
  private static final String ROLE_DESC = "ROLE_DESC.";

  private static final Long ACR_UID_DUMMY = new Long(-9999);
  private static final String DATA_TYPE_DUMMY = "RUBBISH";

  private static final String FEATURE_DESC = "FEATURE_DESC.";
  private static final String FEATURE      = "FEATURE.";

  private static final String ACR_DESC     = "ACR_DESC.";
  private static final String ACTION       = "ACTION.";
  private static final String DATA_TYPE    = "DATA_TYPE.";

  private static final IDataFilter[] CRITERIA = new DataFilterImpl[]
                                                {
                                                  new DataFilterImpl(),
                                                  new DataFilterImpl(),
                                                  new DataFilterImpl(),
                                                };

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

  private static final ArrayList ACTION_LIST = new ArrayList();
  private static final ArrayList DATATYPE_LIST = new ArrayList();

  static
  {
    CRI_LIST_1.add(CRI_FIELD_ID_1);
    CRI_LIST_1.add(CRI_FIELD_ID_2);
    CRI_LIST_1.add(CRI_FIELD_ID_3);

    CRITERIA[0].addDomainFilter(null, CRI_FIELD_ID_1, CRI_LIST_1, false);

    CRITERIA[1].addRangeFilter(null,
      CRI_FIELD_ID_2, CRI_VALUE_2_1, CRI_VALUE_2_2, false);

    CRITERIA[2].addSingleFilter(null,
      CRI_FIELD_ID_3, CRITERIA[2].getEqualOperator(), CRI_VALUE_3, true);
  }

  IACLManagerHome     _aclHome;
  IACLManagerObj      _aclMgr;
  Long[] _roleUIDs;
  Long[] _acrUIDs;
  RolePrincipal[] _rolePrincipals;

  public ACLPolicyTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ACLPolicyTest.class);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws Exception
  {
    try
    {
      ACLLogger.infoLog(CLASSNAME, "setUp", "Enter");

      lookupACLManager();
      cleanUp();
    }
    finally
    {
      ACLLogger.infoLog(CLASSNAME, "setUp", "Exit");
    }
  }

  protected void tearDown() throws Exception
  {
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    cleanUp();
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  // **************** Test Case ***********************************
/*
  public void testGetPermissions() throws Exception
  {
    ACLLogger.infoLog(CLASSNAME, "testGetPermissions", "Enter");

    prepareTestData();
    ACLPolicy policy = new ACLPolicy();
    Subject subject = new Subject();
    PermissionCollection perms = null;

    Policy.setPolicy(policy);

    //ROLE.0
    subject.getPrincipals().add(_rolePrincipals[0]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.0 Permissions: "+perms);

    //ROLE.0 u ROLE.1
    subject.getPrincipals().add(_rolePrincipals[1]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.0 u ROLE.1 Permissions: "+perms);

    //ROLE.0 u ROLE.1 u ROLE.2
    subject.getPrincipals().add(_rolePrincipals[2]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.0 u ROLE.1 u ROLE.2 Permissions: "+perms);

    //ROLE.0 u ROLE.2
    subject.getPrincipals().remove(_rolePrincipals[1]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.0 u ROLE.2 Permissions: "+perms);

    //ROLE.2
    subject.getPrincipals().remove(_rolePrincipals[0]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.2 Permissions: "+perms);

    //ROLE.1 u ROLE.2
    subject.getPrincipals().add(_rolePrincipals[1]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.2 u ROLE.1 Permissions: "+perms);

    //ROLE.1
    subject.getPrincipals().remove(_rolePrincipals[2]);
    perms = policy.getPermissions(subject, null);
    System.out.println("ROLE.1 Permissions: "+perms);

    ACLLogger.infoLog(CLASSNAME, "testGetPermissions", "Exit");

  }
*/
  public void testCheckPermissions() throws Exception
  {
    ACLLogger.infoLog(CLASSNAME, "testCheckPermissions", "Enter");

    prepareTestData();
    ACLPolicy policy = new ACLPolicy();
    Subject subject = new Subject();
    PermissionCollection perms = null;

    //THE POLICY MUST BE SET TO OVERRIDE THE DEFAULT POLICY IMPLEMENTATION
    Policy.setPolicy(policy);

    //ROLE.0
    checkRole0Perm(subject, true, true, true);

    //ROLE.1
    checkRole1Perm(subject, true, true, true);

    //ROLE.2
    checkRole2Perm(subject, true, true, true);

    //ROLE.0 u ROLE.1
    checkRole0u1Perm(subject);

    //ROLE.0 u ROLE.2
    checkRole0u2Perm(subject);

    //ROLE.1 u ROLE.2
    checkRole1u2Perm(subject);

    //ROLE.0 u ROLE.1 u ROLE.2
    checkRole0u1u2Perm(subject);

    ACLLogger.infoLog(CLASSNAME, "testCheckPermissions", "Exit");

  }

  private void checkRole0Perm(
    Subject subject, boolean clearPrincipal,
    boolean addPrincipal, boolean checkDeny)
    throws Exception
  {
    //ROLE.0
    if (clearPrincipal)
      subject.getPrincipals().clear();
    if (addPrincipal)
      subject.getPrincipals().add(_rolePrincipals[0]);

    // ** granted **
    //full match
    checkGrantSuccess(subject, FEATURE+0, ACTION+0, DATA_TYPE+1, null);
    checkGrantSuccess(subject, FEATURE+0, ACTION+0, DATA_TYPE+0, null);
    checkGrantSuccess(subject, FEATURE+0, ACTION+1, DATA_TYPE+1, null);
    checkGrantSuccess(subject, FEATURE+1, ACTION+2, DATA_TYPE+2, null);
    //data type wildcard
    checkGrantSuccess(subject, FEATURE+1, ACTION+3, DATA_TYPE, null);
    //action wildcard
    checkGrantSuccess(subject, FEATURE+"3.0", ACTION, DATA_TYPE+3, null);
    //action + data type wildcards
    checkGrantSuccess(subject, FEATURE+3, ACTION, DATA_TYPE, null);

    if (!checkDeny) return;

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+1, ACTION+2, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION+2, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+1, ACTION+1, DATA_TYPE+2, null);
    //data type not match on wildcard action
    checkGrantFail(subject, FEATURE+"3.0", ACTION+0, DATA_TYPE+1, null);
    //feature not match
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE, null);
  }

  private void checkRole1Perm(
    Subject subject, boolean clearPrincipal,
    boolean addPrincipal, boolean checkDeny)
    throws Exception
  {
    //ROLE.1
    if (clearPrincipal)
      subject.getPrincipals().clear();
    if (addPrincipal)
      subject.getPrincipals().add(_rolePrincipals[1]);

    // ** granted **
    //full match
    checkGrantSuccess(subject, FEATURE+0, ACTION+1, DATA_TYPE+3, null);
    checkGrantSuccess(subject, FEATURE+"3.0.1", ACTION+3, DATA_TYPE+0, null);
    //feature sub-wildcard
    checkGrantSuccess(subject, FEATURE+"3.x", ACTION+2, DATA_TYPE, null);
    //higher level feature sub-wildcard, grant by FEATURE.3.*
    checkGrantSuccess(subject, FEATURE+"3.0.1", ACTION+2, DATA_TYPE, null);
    //feature sub-wildcard, action wildcard & data type wildcard
    checkGrantSuccess(subject, FEATURE+"3.0.1.x", ACTION, DATA_TYPE, null);

    if (!checkDeny) return;

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+3, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION, DATA_TYPE+3, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION, DATA_TYPE+0, null);
    //action not match, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+1, null, null);
    //action not match on feature sub-wildcard
    checkGrantFail(subject, FEATURE+"3.a", ACTION+1, null, null);
    //feature not match
    checkGrantFail(subject, FEATURE+"3.0.2", ACTION, DATA_TYPE, null);
  }

  private void checkRole2Perm(
    Subject subject, boolean clearPrincipal,
    boolean addPrincipal, boolean checkDeny)
    throws Exception
  {
    //ROLE.2
    if (clearPrincipal)
      subject.getPrincipals().clear();
    if (addPrincipal)
      subject.getPrincipals().add(_rolePrincipals[2]);

    // ** granted **
    //full match
    checkGrantSuccess(subject, FEATURE+2, ACTION+0, DATA_TYPE+1, null);
    checkGrantSuccess(subject, FEATURE+3, ACTION+1, DATA_TYPE+1, null);
    checkGrantSuccess(subject, FEATURE+1, ACTION+2, DATA_TYPE+2, null);
    //action & datatype wildcards
    checkGrantSuccess(subject, FEATURE+1, ACTION, DATA_TYPE, null);

    if (!checkDeny) return;

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+3, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+2, ACTION+0, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+3, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE+1, null);
    //feature not match
    checkGrantFail(subject, FEATURE, ACTION, DATA_TYPE, null);
  }

  private void checkRole0u1Perm(Subject subject)
    throws Exception
  {
    subject.getPrincipals().clear();
    subject.getPrincipals().add(_rolePrincipals[0]);
    subject.getPrincipals().add(_rolePrincipals[1]);

    // ** granted **
    checkRole0Perm(subject, false, false, false);
    checkRole1Perm(subject, false, false, false);

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+1, ACTION+2, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+3, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+1, ACTION, DATA_TYPE+2, null);
    //action not match, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION, DATA_TYPE+0, null);
    //data type not match on wildcard action, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0", ACTION, DATA_TYPE+1, null);
    //action not match on feature sub-wildcard
    checkGrantFail(subject, FEATURE+"3.a", ACTION, DATA_TYPE, null);
    //feature not match
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+"3.0.2", ACTION, DATA_TYPE, null);
  }

  private void checkRole0u2Perm(Subject subject)
    throws Exception
  {
    subject.getPrincipals().clear();
    subject.getPrincipals().add(_rolePrincipals[0]);
    subject.getPrincipals().add(_rolePrincipals[2]);

    // ** granted **
    checkRole0Perm(subject, false, false, false);
    checkRole2Perm(subject, false, false, false);

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+1, ACTION+2, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+2, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+3, ACTION+1, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE+1, null);
    //data type not match on wildcard action
    checkGrantFail(subject, FEATURE+"3.0", ACTION+0, DATA_TYPE, null);
    //feature not match
    checkGrantFail(subject, FEATURE, ACTION, DATA_TYPE, null);
  }

  private void checkRole1u2Perm(Subject subject)
    throws Exception
  {
    subject.getPrincipals().clear();
    subject.getPrincipals().add(_rolePrincipals[1]);
    subject.getPrincipals().add(_rolePrincipals[2]);

    // ** granted **
    checkRole1Perm(subject, false, false, false);
    checkRole2Perm(subject, false, false, false);

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+2, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+3, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+3, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION, DATA_TYPE+3, null);
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+3, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION, DATA_TYPE+0, null);
    //action not match, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+1, DATA_TYPE+0, null);
    //action not match on feature sub-wildcard
    checkGrantFail(subject, FEATURE+"3.a", ACTION+1, null, null);
    //feature not match
    checkGrantFail(subject, FEATURE+"3.0.2", ACTION, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE, ACTION, DATA_TYPE, null);
  }

  private void checkRole0u1u2Perm(Subject subject)
    throws Exception
  {
    subject.getPrincipals().clear();
    subject.getPrincipals().add(_rolePrincipals[0]);
    subject.getPrincipals().add(_rolePrincipals[1]);
    subject.getPrincipals().add(_rolePrincipals[2]);

    // ** granted **
    checkRole0Perm(subject, false, false, false);
    checkRole1Perm(subject, false, false, false);
    checkRole2Perm(subject, false, false, false);

    // ** denied **
    //only data type not match
    checkGrantFail(subject, FEATURE+0, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+0, ACTION+1, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+2, ACTION+0, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION+3, DATA_TYPE, null);
    //action not match
    checkGrantFail(subject, FEATURE+0, ACTION, DATA_TYPE+1, null);
    checkGrantFail(subject, FEATURE+2, ACTION, DATA_TYPE+1, null);
    //action not match, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0.1", ACTION, DATA_TYPE+0, null);
    //data type not match on wildcard action, also not match in FEATURE.3.*
    checkGrantFail(subject, FEATURE+"3.0", ACTION, DATA_TYPE+1, null);
    //action not match on feature sub-wildcard
    checkGrantFail(subject, FEATURE+"3.a", ACTION, DATA_TYPE, null);
    //feature not match
    checkGrantFail(subject, FEATURE+"3.0.2", ACTION, DATA_TYPE, null);
    checkGrantFail(subject, FEATURE, ACTION, DATA_TYPE, null);
  }

  private void checkGrantFail(Subject subject,
    String feature, String action, String dataType, IDataFilter criteria)
  {
    final AccessPermission perm = new AccessPermission(feature, action, dataType, criteria);

    try
    {
      // This serves as as sample for checking the permission for
      // a subject.
      Subject.doAsPrivileged(subject,
        new PrivilegedAction()
        {
          public Object run()
          {
            AccessController.checkPermission(perm);
            System.out.println("Permission granted: "+perm);
            return null;
          }
        },
        null);  // THIS IS VERY IMPORTANT TO PASS NULL FOR AccessControlContext!!
                // PASSING NULL WILL INDICATE THE CALLER CONTEXT IS NOT TAKEN INTO
                // CONSIDERATION FOR CHECKING PERMISSION.
                // PERMISSIONS SHOULD BE SOLELY BASED ON THE ROLES ASSOCIATED
                // WITH THE SUBJECT.

      assertTrue("Permission is incorrectly granted", false);
    }
    catch (SecurityException ex)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGrantFail",
        "SecurityException: "+ex.getMessage());
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog(CLASSNAME, "checkGrantFail", "Unexpected error: "+ex.getMessage());
      assertTrue("Unexpected exception caught", false);
    }
  }

  private void checkGrantSuccess(Subject subject,
    String feature, String action, String dataType, IDataFilter criteria)
  {
    final AccessPermission perm = new AccessPermission(feature, action, dataType, criteria);

    try
    {
      // This serves as as sample for checking the permission for
      // a subject.
      Subject.doAsPrivileged(subject,
        new PrivilegedAction()
        {
          public Object run()
          {
            AccessController.checkPermission(perm);
            return null;
          }
        },
        null);  // THIS IS VERY IMPORTANT TO PASS NULL FOR AccessControlContext!!
                // PASSING NULL WILL INDICATE THE CALLER CONTEXT IS NOT TAKEN INTO
                // CONSIDERATION FOR CHECKING PERMISSION.
                // PERMISSIONS SHOULD BE SOLELY BASED ON THE ROLES ASSOCIATED
                // WITH THE SUBJECT.
    }
    catch (SecurityException ex)
    {
      ACLLogger.warnLog(CLASSNAME, "checkGrantSuccess",
        "SecurityException: "+ex.getMessage());
      assertTrue("Permission is not granted", false);
    }
    catch (Throwable ex)
    {
      ACLLogger.warnLog(CLASSNAME, "checkGrantSuccess", "Unexpected error: "+ex.getMessage());
      assertTrue("Unexpected exception caught", false);
    }
  }

  // *************** Preparation Methods ******************************

  private void prepareTestData()
  {
    createTestRoles();
    createTestFeatures();
    createTestAccessRights();
  }

  private void createTestRoles()
  {
    //ROLE.0, ROLE.1, ROLE.2
    _roleUIDs = new Long[3];
    _rolePrincipals = new RolePrincipal[3];
    for (int i=0; i<3; i++)
    {
      createTestRole(ROLE_NAME+i, ROLE_DESC+i);
      Role role = getTestRole(ROLE_NAME+i);
      _roleUIDs[i] = new Long(role.getUId());
      _rolePrincipals[i] = new RolePrincipal(_roleUIDs[i], ROLE_NAME+i);
    }
  }

  private void createTestFeatures()
  {
    //ACTION.0, ACTION.1, ACTION.2, ACTION.3
    //DATA_TYPE.0, DATA_TYPE.1, DATA_TYPE.2, DATA_TYPE.3
    for (int i=0; i<4; i++)
    {
      ACTION_LIST.add(ACTION+i);
      DATATYPE_LIST.add(DATA_TYPE+i);
    }

    //FEATURE.0, FEATURE.1, FEATURE.2
    for (int i=0; i<3; i++)
      createTestFeature(FEATURE+i, FEATURE_DESC+i, ACTION_LIST, DATATYPE_LIST);

    //FEATURE.3, FEATURE.3.0, FEATURE.3.0.1, FEATURE.3.0.1.2
    String suff = FEATURE+3;
    String desc = FEATURE_DESC;
    for (int i=0; i<3; i++)
    {
      suff += i;
      desc += i;
      createTestFeature(suff, desc, ACTION_LIST, DATATYPE_LIST);
      suff += ".";
      desc += ".";
    }
  }

  private void createTestAccessRights()
  {
    //ROLE.0
    // |_____ FEATURE.0
    // |        |________ ACTION.0
    // |        |           |_______ DATA_TYPE.0 (no criteria)
    // |        |           |_______ DATA_TYPE.1 (criteria1)
    // |        |________ ACTION.1
    // |                    |_______ DATA_TYPE.1 (criteria1)
    // |_____ FEATURE.1
    // |        |________ ACTION.2
    // |        |           |_______ DATA_TYPE.2 (criteria2)
    // |        |________ ACTION.3
    // |        |           |_______ *
    // |_____ FEATURE.3
    // |        |________ *
    // |_____ FEATURE.3.0
    // |        |________ *
    // |                    |_______ DATA_TYPE.3 (criteria3)
    // ROLE.1
    // |_____ FEATURE.0
    // |        |________ ACTION.1
    // |        |           |_______ DATA_TYPE.3 (criteria3)
    // |_____ FEATURE.3.*
    // |        |________ ACTION.2
    // |        |           |_______ *
    // |_____ FEATURE.3.0.1
    // |        |________ ACTION.3
    // |        |           |_______ DATA_TYPE.0 (no criteria)
    // |_____ FEATURE.3.0.1.*
    // |        |________ *
    // ROLE.2
    // |_____ FEATURE.1
    // |        |________ *
    // |        |           |______ *
    // |        |________ ACTION.2
    // |        |           |______ DATA_TYPE.2 (criteria3)
    // |_____ FEATURE.2
    // |        |________ ACTION.0
    // |        |           |______ DATA_TYPE.1 (criteria1)
    // |_____ FEATURE.3
    // |        |________ ACTION.1
    // |        |           |______ DATA_TYPE.1 (criteria2)

    _acrUIDs = new Long[]
    {
      createTestAccessRight(ACR_DESC+0, _roleUIDs[0], FEATURE+0, ACTION+0, DATA_TYPE+0, null),
      createTestAccessRight(ACR_DESC+1, _roleUIDs[0], FEATURE+0, ACTION+0, DATA_TYPE+1, CRITERIA[0]),
      createTestAccessRight(ACR_DESC+2, _roleUIDs[0], FEATURE+0, ACTION+1, DATA_TYPE+1, CRITERIA[0]),
      createTestAccessRight(ACR_DESC+3, _roleUIDs[0], FEATURE+1, ACTION+2, DATA_TYPE+2, CRITERIA[1]),
      createTestAccessRight(ACR_DESC+4, _roleUIDs[0], FEATURE+1, ACTION+3, "*", null),
      createTestAccessRight(ACR_DESC+5, _roleUIDs[0], FEATURE+3, "*", null, null),
      createTestAccessRight(ACR_DESC+6, _roleUIDs[0], FEATURE+"3.0", "*", DATA_TYPE+3, CRITERIA[2]),

      createTestAccessRight(ACR_DESC+7, _roleUIDs[1], FEATURE+0, ACTION+1, DATA_TYPE+3, CRITERIA[2]),
      createTestAccessRight(ACR_DESC+8, _roleUIDs[1], FEATURE+"3.*", ACTION+2, "*", null),
      createTestAccessRight(ACR_DESC+9, _roleUIDs[1], FEATURE+"3.0.1", ACTION+3, DATA_TYPE+0, null),
      createTestAccessRight(ACR_DESC+10, _roleUIDs[1], FEATURE+"3.0.1.*", "*",null, null),

      createTestAccessRight(ACR_DESC+11, _roleUIDs[2], FEATURE+1, "*", "*", null),
      createTestAccessRight(ACR_DESC+12, _roleUIDs[2], FEATURE+1, ACTION+2, DATA_TYPE+2, CRITERIA[2]),
      createTestAccessRight(ACR_DESC+13, _roleUIDs[2], FEATURE+2, ACTION+0, DATA_TYPE+1, CRITERIA[0]),
      createTestAccessRight(ACR_DESC+14, _roleUIDs[2], FEATURE+3, ACTION+1, DATA_TYPE+1, CRITERIA[1]),
    };
  }

  // ******************** Utility methods ********************************

  private void lookupACLManager() throws Exception
  {
    _aclHome = (IACLManagerHome)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getHome(
             IACLManagerHome.class.getName(),
             IACLManagerHome.class);
    assertNotNull("aclHome is null", _aclHome);
    _aclMgr = _aclHome.create();
    assertNotNull("aclMgr is null", _aclMgr);
  }

  private void deleteTestRole(String roleName)
  {
    try
    {
      Role deleted = getTestRole(roleName);
      if (deleted != null)
         _aclMgr.deleteRole(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "deleteTestRole", "Unable to delete test data with rolename = "
                                    + roleName, ex);
    }
  }

  private Role getTestRole(String roleName)
  {
    try
    {
      return _aclMgr.getRoleByRoleName(roleName);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "getTestRole", "Unable to retrieve test data with rolename = "
                                    + roleName, ex);
      return null;
    }
  }

  private void createTestRole(String roleName, String description)
  {
    Role role = new Role();
    role.setDescr(description);
    role.setRole(roleName);

    try
    {
      _aclMgr.createRole(role);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "createTestRole", "Error creating role with rolename = "
                                    + roleName, ex);
      assertTrue("Error in createTestRole", false);
    }
  }


  private void createTestFeature(
    String featureName, String description, ArrayList actionList, ArrayList dataTypeList)
  {
    Feature feature = new Feature();

    feature.setActions(actionList);
    feature.setDataTypes(dataTypeList);
    feature.setDescr(description);
    feature.setFeature(featureName);

    try
    {
      _aclMgr.createFeature(feature);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "createTestFeature", "Error creating feature:"
        + featureName, ex);
      assertTrue("Error in createTestFeature", false);
    }
  }

  private void deleteTestFeature(String featureName)
  {
    try
    {
      Feature deleted = getTestFeature(featureName);
      if (deleted != null)
         _aclMgr.deleteFeature(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "deleteTestFeature", "Unable to delete test feature: "
                                    + featureName, ex);
    }
  }

  private Feature getTestFeature(String featureName)
  {
    try
    {
      return _aclMgr.getFeatureByFeatureName(featureName);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "getTestFeature", "Unable to retrieve test feature: "
                                    + featureName, ex);
      return null;
    }
  }

  private AccessRight getAccessRight(Long acrUID)
  {
    try
    {
      return _aclMgr.getAccessRight(acrUID);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "getAccessRight", "Unable to retrieve test access right: "
                                    + acrUID, ex);
      return null;
    }
  }

  private Long createTestAccessRight(
    String description, Long roleUId, String feature,
    String grantedAction, String grantedDataType, IDataFilter grantCriteria)
  {
    AccessRight acr = new AccessRight();
    acr.setAction(grantedAction);
    acr.setCriteria(grantCriteria);
    acr.setDataType(grantedDataType);
    acr.setDescr(description);
    acr.setFeature(feature);
    acr.setRoleUID(roleUId);

    try
    {
      return _aclMgr.createAccessRight(acr);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "createTestAccessRight",
        "Unable to create accessright", ex);
      assertTrue("Error in createTestAccessRight", false);
      return null;
    }

  }

  private void cleanUp()
  {
   //ROLE.0, ROLE.1, ROLE.2
   for (int i=0; i<3; i++)
      deleteTestRole(ROLE_NAME+i);

    //FEATURE.0, FEATURE.1, FEATURE.2
    for (int i=0; i<3; i++)
      deleteTestFeature(FEATURE+i);

    //FEATURE.3, FEATURE.3.0, FEATURE.3.0.1, FEATURE.3.0.1.2
    String suff = FEATURE+3;
    for (int i=0; i<3; i++)
    {
      suff += i;
      deleteTestFeature(suff);
      suff += ".";
    }
  }

}