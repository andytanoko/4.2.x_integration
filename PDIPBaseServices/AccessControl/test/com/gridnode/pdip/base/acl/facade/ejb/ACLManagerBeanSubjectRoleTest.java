/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBeanSubjectRoleTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 22 2002    Goh Kan Mun             Created
 * Feb 08 2007		Alain Ah Ming						Changed error logs to warn logs
 */

package com.gridnode.pdip.base.acl.facade.ejb;

import com.gridnode.pdip.base.acl.model.SubjectRole;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Timestamp;

/**
 * Partial test case for testing ACLManagerBean. This class tests methods in the manager
 * that involve only with the <code>SubjectRole</code> entity.<P>
 *
 * This tests the following business methods in the UserManagerBean:
 * <LI>testAssignRoleToSubject()         - mgr.assignRoleToSubject(roleUId, subjectUId, subjectType)</LI>
 * <LI>testGetRolesForSubject()          - mgr.getRolesForSubject(subjectUId, subjectType)</LI>
 * <LI>testGetSubjectRole()              - mgr.getSubjectRole(subjectUId, subjectType, roleUId)</LI>
 * <LI>testGetSubjectRolesForSubject()   - mgr.getSubjectRolesForSubject(subjectUId, subjectType)</LI>
 * <LI>testRemoveSubjectRoleForSubject() - mgr.removeSubjectRoleForSubject(subjectUId, subjectType)</LI>
 * <LI>testUnassignRoleFromSubject()     - mgr.retrieveTestSubjectRole(subjectUId, subjectType, roleUId)</LI>
 * <P>
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLManagerBeanSubjectRoleTest
  extends    TestCase
{
  private static final String ROLE_NAME_1 = "Test Role 1";
  private static final String DESC_1 = "Test Description 1";
  private static final boolean CAN_DELETE_1 = true;

  private static final String ROLE_NAME_2 = "Test Role 2";
  private static final String DESC_2 = "Test Description 2";
  private static final boolean CAN_DELETE_2 = false;

  private static final Long SUBJECT_UID_2 = new Long(-12);
  private static final String SUBJECT_TYPE_2 = "Subject Type 1";

  private static final Long SUBJECT_UID_1 = new Long(-22);
  private static final String SUBJECT_TYPE_1 = "Subject Type 2";

  private static final String CLASSNAME = "ACLManagerBeanSubjectRoleTest";

  IACLManagerHome home;
  IACLManagerObj remote;

  public ACLManagerBeanSubjectRoleTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ACLManagerBeanSubjectRoleTest.class);
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

  protected void cleanup() throws Exception
  {
    deleteTestRole(ROLE_NAME_1);
    deleteTestRole(ROLE_NAME_2);
    deleteTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1);
    deleteTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1);
    deleteTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2);
    deleteTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_2);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  private Role createRole(boolean canDelete, String desc, String roleName)
  {
    Role role = new Role();
    role.setCanDelete(canDelete);
    role.setDescr(desc);
    role.setRole(roleName);
    return role;
  }

  private void deleteTestRole(String roleName) throws Exception
  {
    Role retrieve = retrieveTestRole(roleName);
    if (retrieve != null)
       remote.deleteRole(new Long(retrieve.getUId()));
  }

  private Role retrieveTestRole(String roleName) throws Exception
  {
    return remote.getRoleByRoleName(roleName);
  }

  private Role addTestRole(boolean canDelete, String desc, String roleName) throws Exception
  {
    Role inserted = createRole(canDelete, desc, roleName);
    remote.createRole(inserted);
    return remote.getRoleByRoleName(inserted.getRole());
  }

  private SubjectRole addTestSubjectRole(Long subjectUId, String subjectType, Long roleUId) throws Exception
  {
    remote.assignRoleToSubject(roleUId, subjectUId, subjectType);
    return retrieveTestSubjectRole(subjectUId, subjectType, roleUId);
  }

  private Collection retrieveTestSubjectRole(Long subjectUId, String subjectType)
          throws Exception
  {
    return remote.getSubjectRolesForSubject(subjectUId, subjectType);
  }

  private SubjectRole retrieveTestSubjectRole(Long subjectUId, String subjectType, Long roleUId)
          throws Exception
  {
    return remote.getSubjectRole(subjectUId, subjectType, roleUId);
  }

  private void deleteTestSubjectRole(Long subjectUId, String subjectType, Long roleUId) throws Exception
  {
    remote.unassignRoleFromSubject(roleUId, subjectUId, subjectType);
  }

  private void deleteTestSubjectRole(Long subjectUId, String subjectType) throws Exception
  {
    Collection c = retrieveTestSubjectRole(subjectUId, subjectType);
    if (c != null && !c.isEmpty())
       remote.removeSubjectRoleForSubject(subjectUId, subjectType);
  }

  private void checkIdenticalSubjectRole(Long roleUId, Long subjectUId, String subjectType,
          SubjectRole retrieved)
  {
    assertEquals("Role Uid is different", retrieved.getRole(), roleUId.longValue());
    assertEquals("Subject Uid is different", retrieved.getSubject(), subjectUId.longValue());
    assertEquals("Subject Type is different", retrieved.getSubjectType(), subjectType);
  }


  public void testAssignRoleToSubject() throws Exception
  {
    try
    {
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_2, DESC_2, ROLE_NAME_2);

      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());

      // Creating new
      checkAssignRoleToSubjectSuccess(role1UId, SUBJECT_UID_1, SUBJECT_TYPE_1);

      // Creating existing
      checkAssignRoleToSubjectFail(role1UId, SUBJECT_UID_1, SUBJECT_TYPE_1, "Creating Existing");

      // Creating with different subject uid
      checkAssignRoleToSubjectSuccess(role1UId, SUBJECT_UID_2, SUBJECT_TYPE_1);

      // Creating with different subject type
      checkAssignRoleToSubjectSuccess(role1UId, SUBJECT_UID_1, SUBJECT_TYPE_2);

      // Creating with different role
      checkAssignRoleToSubjectSuccess(role2UId, SUBJECT_UID_1, SUBJECT_TYPE_1);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testAssignRoleToSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkAssignRoleToSubjectSuccess(Long roleUId, Long subjectUId, String subjectType) throws Exception
  {
    remote.assignRoleToSubject(roleUId, subjectUId, subjectType);
    SubjectRole retrieved = retrieveTestSubjectRole(subjectUId, subjectType, roleUId);
    checkIdenticalSubjectRole(roleUId, subjectUId, subjectType, retrieved);
  }

  private void checkAssignRoleToSubjectFail(Long roleUId, Long subjectUId, String subjectType,
          String errorMessage)
  {
    try
    {
      remote.assignRoleToSubject(roleUId, subjectUId, subjectType);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testUnassignRoleFromSubject() throws Exception
  {
    try
    {
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Long role1UId = new Long(role1.getUId());
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);

      // Delete existing
      checkUnassignRoleFromSubjectSuccess(role1UId, SUBJECT_UID_1, SUBJECT_TYPE_1);

      // Delete nonexisting
      checkUnassignRoleFromSubjectFail(role1UId, SUBJECT_UID_1, SUBJECT_TYPE_1, "Delete non existing!");

      // Delete 2nd existing
      checkUnassignRoleFromSubjectSuccess(role1UId, SUBJECT_UID_2, SUBJECT_TYPE_1);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testUnassignRoleFromSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkUnassignRoleFromSubjectSuccess(Long roleUId, Long subjectUId, String subjectType) throws Exception
  {
    remote.unassignRoleFromSubject(roleUId, subjectUId, subjectType);
    SubjectRole retrieved = retrieveTestSubjectRole(subjectUId, subjectType, roleUId);
    assertNull("Delete not successful.", retrieved);
  }

  private void checkUnassignRoleFromSubjectFail(Long roleUId, Long subjectUId, String subjectType,
          String errorMessage)
  {
    try
    {
      remote.unassignRoleFromSubject(roleUId, subjectUId, subjectType);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetRolesForSubject() throws Exception
  {
    try
    {
      // Setup
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_2);
      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2, role2UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role2UId);

      long[] roleUId = null;

      // Get subject uid 1, subject type 1
      roleUId = new long[2];
      roleUId[0] = role1UId.longValue();
      roleUId[1] = role2UId.longValue();
      checkGetRolesForSubjectSuccess(roleUId, SUBJECT_UID_1, SUBJECT_TYPE_1);

      // Get subject uid 2, subject type 1
      roleUId = new long[1];
      roleUId[0] = role1UId.longValue();
      checkGetRolesForSubjectSuccess(roleUId, SUBJECT_UID_2, SUBJECT_TYPE_1);

      // Get subject uid 1, subject type 2
      roleUId = new long[1];
      roleUId[0] = role2UId.longValue();
      checkGetRolesForSubjectSuccess(roleUId, SUBJECT_UID_1, SUBJECT_TYPE_2);

      // Get subject uid 2, subject type 2
      roleUId = new long[2];
      roleUId[0] = role1UId.longValue();
      roleUId[1] = role2UId.longValue();
      checkGetRolesForSubjectFail(roleUId, SUBJECT_UID_2, SUBJECT_TYPE_2, "Non-existing!");

      // Get subject uid 2, subject type 2
      checkGetRolesForSubjectSuccess(null, SUBJECT_UID_2, SUBJECT_TYPE_2);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetRolesForSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkGetRolesForSubjectSuccess(long[] roleUId, Long subjectUId, String subjectType) throws Exception
  {
    Collection c = remote.getRolesForSubject(subjectUId, subjectType);
    if (roleUId != null && roleUId.length != 0)
    {
      assertNotNull("The retrieved Collection is null.", c);
      //Check both specified and retrieved size.
      assertEquals("The retrieved size is not the same as the specified.", roleUId.length, c.size());
    }
    else
    {
      if (c != null)
         assertEquals("The retrieved is not empty but expected empty.", 0, c.size());
      return;
    }
    Iterator iter = c.iterator();
    Role role = null;
    // List to keep track whether the specified role is in the retrieved data.
    boolean[] isThere = new boolean[roleUId.length];
    // Resetting list.
    for (int i = 0; i < roleUId.length; i++)
      isThere[i] = false;
    while (iter.hasNext())
    {
      boolean found = false;
      role = (Role) iter.next();
      for (int i = 0; i < roleUId.length; i++)
      {
        if (roleUId[i] == role.getUId())
        {
          isThere[i] = true;
          found = true;
          break;
        }
      }
      assertTrue("Role not found!", found);
    }
    // Check all specified is present.
    for (int i = 0; i < isThere.length; i++)
      assertTrue("Specified roleId not found: " + roleUId[i], isThere[i]);
  }

  private void checkGetRolesForSubjectFail(long[] roleUId, Long subjectUId, String subjectType,
          String errorMessage) throws Exception
  {
    Collection c = null;
    try
    {
      c =  remote.getRolesForSubject(subjectUId, subjectType);
    }
    catch (Exception ex)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetRolesForSubjectFail", "Returning because "+
                                   "of exception!", ex);
      return;
    }

    //Check both specified and retrieved size.
    if (c == null || c.size() == 0)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetRolesForSubjectFail", "Returning because the retrieved data is null!");
      return;
    }
    if ((roleUId == null && c.size() > 0) || (c.size() != roleUId.length))
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetRolesForSubjectFail",
                                   "Returning because the size is different: retrieved = " + c.size()
                                   + ", expected = " + roleUId.length);
      return;
    }

    Iterator iter = c.iterator();
    Role role = null;
    // List to keep track whether the specified role is in the retrieved data.
    boolean[] isThere = new boolean[roleUId.length];
    // Resetting list.
    for (int i = 0; i < isThere.length; i++)
      isThere[i] = false;
    while (iter.hasNext())
    {
      boolean found = false;
      role = (Role) iter.next();
      for (int i = 0; i < roleUId.length; i++)
      {
        if (roleUId[i] == role.getUId())
        {
          isThere[i] = true;
          found = true;
          break;
        }
      }
      if (!found)
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetRolesForSubjectFail",
                                   "Returning because item retrieved is not in expected: " +
                                   "retrieved = " + role.getEntityDescr());
        return;
      }
    }
    // Check all specified is present.
    for (int i = 0; i < isThere.length; i++)
      if (!isThere[i])
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetRolesForSubjectFail",
                                   "Returning because item expected is not in retrieved: " +
                                   "expected roleUId = " + roleUId[i]);
        return;
      }
    assertTrue("All specified roleId found.", false);
  }


  public void testRemoveSubjectRoleForSubject() throws Exception
  {
    try
    {
      // Setup
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_2);
      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2, role2UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role2UId);

      // 1
      checkRemoveSubjectRoleForSubjectSuccess(SUBJECT_UID_2, SUBJECT_TYPE_1);

      // Non-Existing
      checkRemoveSubjectRoleForSubjectFail(SUBJECT_UID_2, SUBJECT_TYPE_2, "Non-Existing");

      // 2
      checkRemoveSubjectRoleForSubjectSuccess(SUBJECT_UID_1, SUBJECT_TYPE_1);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testRemoveSubjectRoleForSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkRemoveSubjectRoleForSubjectSuccess(Long subjectUId, String subjectType) throws Exception
  {
    remote.removeSubjectRoleForSubject(subjectUId, subjectType);
    Collection c = retrieveTestSubjectRole(subjectUId, subjectType);
    if (c != null)
    {
      if (c.size() != 0)
        assertTrue("Return size is not the 0: " + c.size(), false);
    }
  }

  private void checkRemoveSubjectRoleForSubjectFail(Long subjectUId, String subjectType,
          String errorMessage)
  {
    try
    {
      remote.removeSubjectRoleForSubject(subjectUId, subjectType);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetSubjectRole() throws Exception
  {
    try
    {
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_2, DESC_2, ROLE_NAME_2);
      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());

      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_2, role1UId);

      //Retrieve existing.
      checkGetSubjectRoleSuccess(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      checkGetSubjectRoleSuccess(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      checkGetSubjectRoleSuccess(SUBJECT_UID_1, SUBJECT_TYPE_2, role1UId);
      checkGetSubjectRoleSuccess(SUBJECT_UID_2, SUBJECT_TYPE_2, role1UId);

      //Retrieve non-existing.
      checkGetSubjectRoleFail(SUBJECT_UID_1, SUBJECT_TYPE_1, role2UId, "Retrieving non-existing");
      checkGetSubjectRoleFail(SUBJECT_UID_2, SUBJECT_TYPE_1, role2UId, "Retrieving non-existing");
      checkGetSubjectRoleFail(SUBJECT_UID_1, SUBJECT_TYPE_2, role2UId, "Retrieving non-existing");
      checkGetSubjectRoleFail(SUBJECT_UID_2, SUBJECT_TYPE_2, role2UId, "Retrieving non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetSubjectRole", "fails", ex);
      throw ex;
    }

  }

  private void checkGetSubjectRoleSuccess(Long subjectUId, String subjectType, Long roleUId)
          throws Exception
  {
    SubjectRole subjectRole = remote.getSubjectRole(subjectUId, subjectType, roleUId);
    assertNotNull(subjectRole);
    assertEquals("Subject uId not the same.", subjectUId.longValue(), subjectRole.getSubject());
    assertEquals("Subject type not the same.", subjectType, subjectRole.getSubjectType());
    assertEquals("Role uId not the same.", roleUId.longValue(), subjectRole.getRole());
  }

  private void checkGetSubjectRoleFail(Long subjectUId, String subjectType, Long roleUId,
          String errorMessage)
  {
    try
    {
      SubjectRole subjectRole = remote.getSubjectRole(subjectUId, subjectType, roleUId);
      assertNull(errorMessage, subjectRole);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetSubjectRolesForSubject() throws Exception
  {
    try
    {
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_2, DESC_2, ROLE_NAME_2);
      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());

      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role2UId);

      long[] expectedRoleUId = null;

      expectedRoleUId = new long[1];
      expectedRoleUId[0] = role1UId.longValue();
      checkGetSubjectRolesForSubjectSuccess(SUBJECT_UID_2, SUBJECT_TYPE_1, expectedRoleUId);

      expectedRoleUId = new long[1];
      expectedRoleUId[0] = role1UId.longValue();
      checkGetSubjectRolesForSubjectSuccess(SUBJECT_UID_1, SUBJECT_TYPE_2, expectedRoleUId);

      expectedRoleUId = new long[2];
      expectedRoleUId[0] = role1UId.longValue();
      expectedRoleUId[1] = role2UId.longValue();
      checkGetSubjectRolesForSubjectSuccess(SUBJECT_UID_1, SUBJECT_TYPE_1, expectedRoleUId);

      expectedRoleUId = null;
      checkGetSubjectRolesForSubjectFail(SUBJECT_UID_1, SUBJECT_TYPE_2, expectedRoleUId);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetSubjectRolesForSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkGetSubjectRolesForSubjectSuccess(Long subjectUId, String subjectType,
          long[] roleUId) throws Exception
  {
    Collection c = remote.getSubjectRolesForSubject(subjectUId, subjectType);
    if (roleUId != null && roleUId.length > 0)
      assertNotNull(c);
    else
    {
      if (c != null)
        assertEquals(0, c.size());
      return;
    }
    assertEquals("Retrieved size is not excepcted.", roleUId.length, c.size());
    boolean[] retrieved = new boolean[roleUId.length];
    Iterator iter = c.iterator();
    SubjectRole subjectRole = null;
    boolean found = false;
    while (iter.hasNext())
    {
      found = false;
      subjectRole = (SubjectRole) iter.next();
      for (int i = 0; i < roleUId.length; i++)
      {
        if (subjectRole.getRole() == roleUId[i])
        {
          retrieved[i] = true;
          found = true;
          break;
        }
      }
      if (!found)
        assertTrue("SubjectRole with roleUId = " + subjectRole.getRole() + " not found in expected list", false);
    }
    for (int i = 0; i < roleUId.length; i++)
    {
      if (!retrieved[i])
        assertTrue("SubjectRole with roleUId = " + roleUId[i] + " not found in retrieved list", false);
    }
  }

  private void checkGetSubjectRolesForSubjectFail(Long subjectUId, String subjectType,
          long[] roleUId)
  {
    Collection c = null;
    try
    {
      c = remote.getSubjectRolesForSubject(subjectUId, subjectType);
    }
    catch (Exception ex)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectRolesForSubjectFail", "Returning because" +
                                   " of exception encountered", ex);
      return;
    }
    //Check both specified and retrieved size.
    if (c == null || c.size() == 0)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectRolesForSubjectFail", "Returning because" +
                                   " the retrieved data is null!");
      return;
    }
    if ((roleUId == null && c.size() > 0) || (c.size() != roleUId.length))
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectRolesForSubjectFail",
                                   "Returning because the size is different: retrieved = " +
                                   c.size() + ", expected = " +
                                   ((roleUId == null) ? 0 : roleUId.length));
      return;
    }
    Iterator iter = c.iterator();
    SubjectRole subjectRole = null;
    // List to keep track whether the specified role is in the retrieved data.
    boolean[] isThere = new boolean[roleUId.length];
    boolean found = false;
    // Resetting list.
    for (int i = 0; i < isThere.length; i++)
      isThere[i] = false;
    while (iter.hasNext())
    {
      found = false;
      subjectRole = (SubjectRole) iter.next();
      for (int i = 0; i < roleUId.length; i++)
      {
        if (roleUId[i] == subjectRole.getRole())
        {
          isThere[i] = true;
          found = true;
          break;
        }
      }
      if (!found)
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetSubjectRolesForSubjectFail",
                                   "Returning because item retrieved is not in expected: " +
                                   "retrieved = " + subjectRole.getEntityDescr());
        return;
      }
    }
    // Check all specified is present.
    for (int i = 0; i < isThere.length; i++)
      if (!isThere[i])
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetSubjectRolesForSubjectFail",
                                   "Returning because item expected is not in retrieved: " +
                                   "expected roleUId = " + roleUId[i]);
        return;
      }
    assertTrue("All specified roleId found.", false);
  }

  public void testGetSubjectUIdsForRole() throws Exception
  {
    try
    {
      // Setup
      Role role1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role role2 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_2);
      Long role1UId = new Long(role1.getUId());
      Long role2UId = new Long(role2.getUId());
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_2, SUBJECT_TYPE_1, role1UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_2, role2UId);
      addTestSubjectRole(SUBJECT_UID_1, SUBJECT_TYPE_1, role2UId);

      Long[] subjectsUId = null;

      // Get role uid 1, subject type 1
      subjectsUId = new Long[2];
      subjectsUId[0] = SUBJECT_UID_1;
      subjectsUId[1] = SUBJECT_UID_2;
      checkGetSubjectUIdsForRoleSuccess(subjectsUId, role1UId, SUBJECT_TYPE_1);

      // Get role uid 2, subject type 1
      subjectsUId = new Long[1];
      subjectsUId[0] = SUBJECT_UID_1;
      checkGetSubjectUIdsForRoleSuccess(subjectsUId, role2UId, SUBJECT_TYPE_1);

      // Get role uid 2, subject type 2
      subjectsUId = new Long[1];
      subjectsUId[0] = SUBJECT_UID_1;
      checkGetSubjectUIdsForRoleSuccess(subjectsUId, role2UId, SUBJECT_TYPE_2);

      // Get role uid 1, subject type 2
      subjectsUId = new Long[2];
      subjectsUId[0] = SUBJECT_UID_1;
      subjectsUId[1] = SUBJECT_UID_2;
      checkGetSubjectUIdsForRoleFail(subjectsUId, role1UId, SUBJECT_TYPE_2, "Non-existing!");

      // Get role uid 1, subject type 2
      checkGetSubjectUIdsForRoleSuccess(null, role1UId, SUBJECT_TYPE_2);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetRolesForSubject", "fails", ex);
      throw ex;
    }
  }

  private void checkGetSubjectUIdsForRoleSuccess(Long[] subjectsUId, Long roledUId, String subjectType) throws Exception
  {
    Collection c = remote.getSubjectUIdsForRole(roledUId, subjectType);
    if (subjectsUId != null && subjectsUId.length != 0)
    {
      assertNotNull("The retrieved Collection is null.", c);
      //Check both specified and retrieved size.
      assertEquals("The retrieved size is not the same as the specified.", subjectsUId.length, c.size());
    }
    else
    {
      if (c != null)
         assertEquals("The retrieved is not empty but expected empty.", 0, c.size());
      return;
    }
    Iterator iter = c.iterator();

    // List to keep track whether the specified subjectUID is in the retrieved data.
    boolean[] isThere = new boolean[subjectsUId.length];
    // Resetting list.
    for (int i = 0; i < subjectsUId.length; i++)
      isThere[i] = false;
    Long subjectUId = null;
    while (iter.hasNext())
    {
      boolean found = false;
      subjectUId = (Long) iter.next();
      for (int i = 0; i < subjectsUId.length; i++)
      {
        if (subjectsUId[i].equals(subjectUId))
        {
          isThere[i] = true;
          found = true;
          break;
        }
      }
      assertTrue("SubjectUId not found! " + subjectUId, found);
    }
    // Check all specified is present.
    for (int i = 0; i < isThere.length; i++)
      assertTrue("Specified roleId not found: " + subjectsUId[i], isThere[i]);
  }

  private void checkGetSubjectUIdsForRoleFail(Long[] subjectsUId, Long roleUId, String subjectType,
          String errorMessage) throws Exception
  {
    Collection c = null;
    try
    {
      c =  remote.getSubjectUIdsForRole(roleUId, subjectType);
    }
    catch (Exception ex)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectUIdsForRoleFail", "Returning because "+
                                   "of exception!", ex);
      return;
    }

    //Check both specified and retrieved size.
    if (c == null || c.size() == 0)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectUIdsForRoleFail", "Returning because the retrieved data is null!");
      return;
    }
    if ((subjectsUId == null && c.size() > 0) || (c.size() != subjectsUId.length))
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetSubjectUIdsForRoleFail",
                                   "Returning because the size is different: retrieved = " + c.size()
                                   + ", expected = " + subjectsUId.length);
      return;
    }

    Iterator iter = c.iterator();
    // List to keep track whether the specified role is in the retrieved data.
    boolean[] isThere = new boolean[subjectsUId.length];
    // Resetting list.
    for (int i = 0; i < isThere.length; i++)
      isThere[i] = false;
    Long subjectUId = null;
    while (iter.hasNext())
    {
      boolean found = false;
      subjectUId = (Long) iter.next();
      for (int i = 0; i < subjectsUId.length; i++)
      {
        if (subjectsUId[i].equals(subjectUId))
        {
          isThere[i] = true;
          found = true;
          break;
        }
      }
      if (!found)
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetSubjectUIdsForRoleFail",
                                   "Returning because item retrieved is not in expected: " +
                                   "retrieved subjectUid = " + subjectUId);
        return;
      }
    }
    // Check all specified is present.
    for (int i = 0; i < isThere.length; i++)
      if (!isThere[i])
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetSubjectUIdsForRoleFail",
                                   "Returning because item expected is not in retrieved: " +
                                   "expected subjectUId = " + subjectsUId[i]);
        return;
      }
    assertTrue("All specified subjectUId found.", false);
  }

}