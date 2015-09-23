/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBeanRoleTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 22 2002    Goh Kan Mun             Created
 * Feb 08 2007		Alain Ah Ming						Changed error logs to warn logs
 */

package com.gridnode.pdip.base.acl.facade.ejb;

import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Collection;
import java.util.Iterator;

/**
 * Partial test case for testing ACLManagerBean. This class tests methods in the manager
 * that involve only with the <code>Role</code> entity.<P>
 *
 * This tests the following business methods in the UserManagerBean:
 * <LI>testCreateRole()          - mgr.createRole(Role)</LI>
 * <LI>testUpdateRole()          - mgr.updateRole(Role)</LI>
 * <LI>testDeleteRole()          - mgr.deleteRole(UId)</LI>
 * <LI>testGetRoleByRoleName()   - mgr.getRoleByRoleName(roleName)</LI>
 * <LI>testGetRoleByUId()        - mgr.getRoleByUId(uId)</LI>
 * <LI>testGetRolesByFilter()    - mgr.getRolesByFilter(filter)</LI>
 * <P>
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLManagerBeanRoleTest
  extends    TestCase
{
  private static final String ROLE_NAME_1 = "Test Role 1";
  private static final String DESC_1 = "Test Description 1";
  private static final boolean CAN_DELETE_1 = true;

  private static final String ROLE_NAME_2 = "Test Role 2";
  private static final String DESC_2 = "Test Description 2";
  private static final boolean CAN_DELETE_2 = false;

  private static final String CLASSNAME = "ACLManagerBeanRoleTest";

  IACLManagerHome home;
  IACLManagerObj remote;

  public ACLManagerBeanRoleTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ACLManagerBeanRoleTest.class);
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
  }

  private Role createRole(boolean canDelete, String desc, String roleName)
  {
    Role role = new Role();
    role.setCanDelete(canDelete);
    role.setDescr(desc);
    role.setRole(roleName);
    return role;
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
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

  private void checkIdenticalRole(Role role1, Role role2, boolean checkUId)
  {
    assertEquals("Description not the same!", role1.getDescr(), role2.getDescr());
    assertEquals("Role name not the same!", role1.getRole(), role2.getRole());
    assertEquals("canDelete not the same!", role1.canDelete(), role2.canDelete());
    if (checkUId)
       assertEquals("UId not the same!", role1.getUId(), role2.getUId());
  }


  public void testCreateRole() throws Exception
  {
    try
    {
      Role retrieved = null;

      // test create role.
      Role role1 = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkCreateRolePass(role1, ROLE_NAME_1);

      // test create duplicate role.
      Role role2 = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkCreateRoleFail(role2, "Able to add in duplicate role into the database.");

      // test create 2nd role
      Role role3 = createRole(CAN_DELETE_2, DESC_2, ROLE_NAME_2);
      checkCreateRolePass(role3, ROLE_NAME_2);

      // delete previous test
      deleteTestRole(role3.getRole());

      // test create role with same roleName and desc but different canDelete.
      Role role4 = createRole(CAN_DELETE_2, DESC_1, ROLE_NAME_1);
      checkCreateRoleFail(role4, "Able to add in same roleName and desc but different canDelete.");

      // test create role with same roleName and canDelete but different desc.
      Role role5 = createRole(CAN_DELETE_1, DESC_2, ROLE_NAME_1);
      checkCreateRoleFail(role4, "Able to add in same roleName and canDelete but different desc.");

      // test create role with same canDelete and desc but different roleName.
      Role role6 = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_2);
      checkCreateRolePass(role6, ROLE_NAME_2);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testCreateRole", "fails", ex);
      throw ex;
    }
  }

  private void checkCreateRolePass(Role role, String roleName) throws Exception
  {
    remote.createRole(role);
    Role retrieved = retrieveTestRole(roleName);
    checkIdenticalRole(role, retrieved, false);
  }

  private void checkCreateRoleFail(Role role, String errorMessage)
  {
    try
    {
      remote.createRole(role);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testUpdateRole() throws Exception
  {
    try
    {
      Role existing = null;
      Role retrieved = null;

      // test update non-existing role.
      Role role1 = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkUpdateRoleFail(role1, "Able to update non-existing role.");

      // create role in database
      existing = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);

      // test update existing role change desc2.
      existing.setDescr(DESC_2);
      checkUpdateRolePass(existing, ROLE_NAME_1);

      // test update existing role change canDelete but using old version.
      existing.setCanDelete(CAN_DELETE_2);
      checkUpdateRoleFail(existing, ROLE_NAME_1);

      // test update existing role change canDelete using updated version.
      existing = retrieveTestRole(ROLE_NAME_1);
      existing.setCanDelete(CAN_DELETE_2);
      checkUpdateRolePass(existing, ROLE_NAME_1);

      // test update existing role change rolename using updated version.
      existing = retrieveTestRole(ROLE_NAME_1);
      assertNull("Role 2 exist. Test environment not set properly.", retrieveTestRole(ROLE_NAME_2));
      existing.setRole(ROLE_NAME_2);
      checkUpdateRolePass(existing, ROLE_NAME_2);
      assertNull("Role 1 exist. Result not correct. Two copies of same data: Role1 and Role2!", retrieveTestRole(ROLE_NAME_1));
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testUpdateRole", "fails", ex);
      throw ex;
    }
  }

  private void checkUpdateRoleFail(Role role, String errorMessage)
  {
    try
    {
      remote.updateRole(role);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkUpdateRolePass(Role role, String roleName) throws Exception
  {
    remote.updateRole(role);
    Role retrieved = retrieveTestRole(roleName);
    checkIdenticalRole(role, retrieved, false);
  }


  public void testDeleteRole() throws Exception
  {
    try
    {
      Role insert1 = null;
      // Delete non-existing Role
      Role role1 = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkDeleteRoleFail(role1, "delete non-existing role");

      // Create Role.
      insert1 = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);

      // Delete existing role
      checkDeleteRolePass(insert1, ROLE_NAME_1);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testDeleteRole", "fails", ex);
      throw ex;
    }
  }

  private void checkDeleteRolePass(Role role, String roleName) throws Exception
  {
    remote.deleteRole(new Long(role.getUId()));
    Role retrieve = retrieveTestRole(roleName);
    assertNull("Delete not successful.", retrieve);
  }

  private void checkDeleteRoleFail(Role role, String errorMessage) throws Exception
  {
    try
    {
      remote.deleteRole(new Long(role.getUId()));
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetRoleByRoleName() throws Exception
  {
    try
    {
      Role exist = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      // Find exisitng
      Role mock = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkGetRoleByRoleNameSuccess(ROLE_NAME_1, mock);

      deleteTestRole(exist.getRole());
      // Find non-exisitng
      checkGetRoleByRoleNameFail(ROLE_NAME_1, "Deleting non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetRoleByRoleName", "fails", ex);
      throw ex;
    }

  }

  private void checkGetRoleByRoleNameFail(String roleName, String errorMsg)
  {
    try
    {
      Role findRole = remote.getRoleByRoleName(roleName);
      assertNull(errorMsg, findRole);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkGetRoleByRoleNameSuccess(String roleName, Role role) throws Exception
  {
    Role findRole = remote.getRoleByRoleName(roleName);
    assertNotNull(findRole);
    checkIdenticalRole(role, findRole, false);
  }


  public void testGetRoleByUId() throws Exception
  {

    try
    {
      Role exist = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      // Find exisitng
      Role mock = createRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      checkGetRoleByUIdSuccess(exist.getUId(), mock);

      deleteTestRole(exist.getRole());
      // Find non-exisitng
      checkGetRoleByUIdFail(exist.getUId(), "Deleting non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetRoleByUId", "fails", ex);
      throw ex;
    }
  }

  private void checkGetRoleByUIdFail(long uId, String errorMessage)
  {
    try
    {
      Role role = remote.getRoleByUId(new Long(uId));
      assertNull(errorMessage, role);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkGetRoleByUIdSuccess(long uId, Role role)  throws Exception
  {
    Role findRole = remote.getRoleByUId(new Long(uId));
    assertNotNull(findRole);
    checkIdenticalRole(role, findRole, false);
  }


  public void testGetRolesByFilter() throws Exception
  {
    try
    {
      Role exist = addTestRole(CAN_DELETE_1, DESC_1, ROLE_NAME_1);
      Role exist2 = addTestRole(CAN_DELETE_2, DESC_2, ROLE_NAME_2);
      DataFilterImpl filter = null;

      // Find All
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, Role.UID, filter.getEqualOperator(), null, true);
      checkGetRolesByFilterSuccess(exist, exist2, filter);

      // Find None
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, Role.UID, filter.getEqualOperator(), null, false);
      checkGetRolesByFilterFail(exist, exist2, "Find none", filter);

      // Find one
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, Role.ROLE, filter.getEqualOperator(), ROLE_NAME_1, false);
      checkGetRolesByFilterSuccess(exist, null, filter);
      checkGetRolesByFilterFail(null, exist2, "Find 1", filter);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetRolesByFilter", "fails", ex);
      throw ex;
    }
  }

  private void checkGetRolesByFilterFail(Role role1, Role role2, String errorMsg, DataFilterImpl filter)
          throws Exception
  {
    Collection findRoles = remote.getRolesByFilter(filter);
    if (findRoles != null)
    {
      if (role1 != null)
      {
        Iterator iter = findRoles.iterator();
        while (iter.hasNext())
        {
          Role retrieved = (Role) iter.next();
          if (retrieved.getRole().equals(role1.getRole()))
            assertTrue(errorMsg, false);
        }
      }
      if (role2 != null)
      {
        Iterator iter = findRoles.iterator();
        while (iter.hasNext())
        {
          Role retrieved = (Role) iter.next();
          if (retrieved.getRole().equals(role2.getRole()))
            assertTrue(errorMsg, false);
        }
      }
    }
  }

  private void checkGetRolesByFilterSuccess(Role role1, Role role2, DataFilterImpl filter)
          throws Exception
  {
    Collection findRoles = remote.getRolesByFilter(filter);
    if (findRoles != null)
    {
      if (role1 != null)
      {
        Iterator iter = findRoles.iterator();
        boolean found = false;
        while (iter.hasNext())
        {
          Role retrieved = (Role) iter.next();
          if (retrieved.getRole().equals(role1.getRole()))
          {
            checkIdenticalRole(retrieved, role1, false);
            found = true;
          }
        }
        if (!found)
          assertTrue("Role 1 not found!", false);
      }
      if (role2 != null)
      {
        Iterator iter = findRoles.iterator();
        boolean found = false;
        while (iter.hasNext())
        {
          Role retrieved = (Role) iter.next();
          if (retrieved.getRole().equals(role2.getRole()))
          {
            checkIdenticalRole(retrieved, role2, false);
            found = true;
          }
        }
        if (!found)
          assertTrue("Role 2 not found!", false);
      }
    }
    else
    {
      if (role1 != null || role2 != null)
         assertTrue("Data not found!", false);
    }
  }

}