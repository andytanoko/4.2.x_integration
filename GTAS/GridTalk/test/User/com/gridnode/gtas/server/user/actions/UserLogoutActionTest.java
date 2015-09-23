/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserLogoutActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Neo Sok Lay         Created
 * Jun 06 2002    Neo Sok Lay         Test with session data.
 *                                    Re-write test cases.
 * Jun 21 2002    Neo Sok Lay         Re-org test cases.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.app.user.login.UserPrincipal;
import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.base.acl.auth.RolePrincipal;
import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;

import javax.security.auth.Subject;

import junit.framework.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.sql.Timestamp;

/**
 * This test cases tests the UserLgoutAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLogoutActionTest extends ActionTestHelper
{
  static final String ROLE_1     = "Role1";
  static final String ROLE_DESC_1= "Role 1";
  static final String ROLE_2     = "Role2";
  static final String ROLE_DESC_2= "Role 2";

  static final ArrayList ROLES_1 = new ArrayList();

  public UserLogoutActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UserLogoutActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // **************** ActionTestHelper methods ************************

  protected void unitTest() throws Exception
  {
    signon(_sessions[0], _accts[0].getId(), PASSWORD, _sm[0]);

    //null event
    signoffCheckFail(null, _sessions[0], _accts[0].getId(), _sm[0], true);
    //invalid session id
    signoffCheckFail(createTestEvent(), "", _accts[0].getId(), _sm[0], true);
    //invalid user
    signoffCheckFail(createTestEvent(), _sessions[0], "", _sm[0], true);

    //non existent user
    signoffCheckFail(createTestEvent(), _sessions[0], "9999", _sm[0], true);
    //successful signoff
    signoffCheckSuccess(createTestEvent(), _sessions[0], _accts[0].getId(), _sm[0]);

    //signoff again on removed session
    signoffCheckFail(createTestEvent(), _sessions[0],_accts[0].getId(), _sm[0], false);
    //signoff on session not authenticated
    signoffCheckFail(createTestEvent(), _sessions[1], _accts[0].getId(), _sm[0], true);

    //signon allowed after signoff
    //multiple sessions signoff
    signon(_sessions[1], _accts[0].getId(), PASSWORD, _sm[1]);
    signon(_sessions[2], _accts[0].getId(), PASSWORD, _sm[2]);
    signoffCheckSuccess(createTestEvent(), _sessions[1], _accts[0].getId(), _sm[1]);
    //signoff at one session should not affect the other session
    checkPrincipals(_sm[2], ROLES_1);
    signoffCheckSuccess(createTestEvent(), _sessions[2], _accts[0].getId(), _sm[2]);
  }

  protected void prepareTestData() throws Exception
  {
    //prepare test data
    createUsers(1);

    //assign roles for TEST_ID
    UserAccount acct = findUserAccountByUserId(_accts[0].getId());
    assignRole(new Long(acct.getUId()), ROLE_1, ROLE_DESC_1);
    assignRole(new Long(acct.getUId()), ROLE_2, ROLE_DESC_2);

    ROLES_1.add(ROLE_1);
    ROLES_1.add(ROLE_2);

    createSessions(3);
    createStateMachines(3);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void cleanUp()
  {
    deleteUsers(1);
    deleteTestRole(ROLE_1);
    deleteTestRole(ROLE_2);
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UserAccount acct = findUserAccountByUserId(_accts[0].getId());
    UserAccountState acctState = acct.getAccountState();

    assertNotNull("Logout Time is incorrect", acctState.getLastLogoutTime());

    checkSessionDataRemoved(sm);
  }

  protected IEJBAction createNewAction()
  {
    return new UserLogoutAction();
  }

  // ******************* Own methods *******************************

  private void deleteTestRole(String roleName)
  {
    try
    {
      Role role = _aclMgr.getRoleByRoleName(roleName);
      if (role != null)
        _aclMgr.deleteRole(new Long(role.getUId()));
    }
    catch (Exception ex)
    {

    }
  }

  private UserLogoutEvent createTestEvent()
  {
    return new UserLogoutEvent();
  }

  private void signoffCheckSuccess(
    UserLogoutEvent event, String session, String user, StateMachine sm)
    throws Exception
  {
    sm.setAttribute(IAttributeKeys.USER_ID, user);
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void signoffCheckFail(
    UserLogoutEvent event, String session, String user, StateMachine sm,
    boolean eventEx) throws Exception
  {
    sm.setAttribute(IAttributeKeys.USER_ID, user);
    checkFail(event, session, sm, eventEx, IErrorCode.LOGOUT_ERROR);
  }

  private void checkSessionDataRemoved(StateMachine sm)
  {
    assertNull("Principals not removed", sm.getAttribute(IAttributeKeys.PRINCIPALS));
    assertNull("User ID not removed", sm.getAttribute(IAttributeKeys.USER_ID));
  }

  private void checkPrincipals(StateMachine sm, Collection roles)
  {
    Set principals = (Set)sm.getAttribute(IAttributeKeys.PRINCIPALS);
    String user = (String)sm.getAttribute(IAttributeKeys.USER_ID);
    assertNotNull("Principals is null", principals);

    Subject subject = new Subject();
    subject.getPrincipals().addAll(principals);

    UserPrincipal[] userPrincipals = (UserPrincipal[])subject.getPrincipals(UserPrincipal.class).toArray(new UserPrincipal[0]);
    RolePrincipal[] rolePrincipals = (RolePrincipal[])subject.getPrincipals(RolePrincipal.class).toArray(new RolePrincipal[0]);

    assertEquals("UserPrincipal set size incorrect", 1, userPrincipals.length);
    assertEquals("RolePrincipal set size incorrect", roles.size(), rolePrincipals.length);

    assertEquals("", user, userPrincipals[0].toString());

    for (int i=0; i<rolePrincipals.length; i++)
    {
      assertTrue("Unexpected RolePrincipal returned", roles.contains(rolePrincipals[i].getRole()));
    }
  }

  private void assignRole(Long userUID, String roleName, String roleDescr)
    throws Exception
  {
    Role role = new Role();
    role.setRole(roleName);
    role.setDescr(roleDescr);
    _aclMgr.createRole(role);
    role = _aclMgr.getRoleByRoleName(roleName);
    _aclMgr.assignRoleToSubject(new Long(role.getUId()), userUID, UserAccount.ENTITY_NAME);
  }
}