/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserLoginActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Neo Sok Lay         Created
 * Jun 05 2002    Neo Sok Lay         Test with session data.
 *                                    Rewrite test cases.
 * Jun 19 2002    Neo Sok Lay         Re-org test cases.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;
import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.app.user.login.UserPrincipal;
import com.gridnode.pdip.app.user.login.AuthSubject;
import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.base.acl.auth.RolePrincipal;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.session.model.SessionData;
import com.gridnode.pdip.base.session.model.SessionAudit;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
//import com.gridnode.pdip.framework.db.dao.*;
//import com.gridnode.pdip.framework.db.filter.*;

import junit.framework.*;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import java.sql.Timestamp;

/**
 * This test cases tests the UserLoginAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLoginActionTest extends ActionTestHelper
{
  static final String ROLE_1     = "Role1";
  static final String ROLE_DESC_1= "Role 1";
  static final String ROLE_2     = "Role2";
  static final String ROLE_DESC_2= "Role 2";

  static final ArrayList ROLES_1 = new ArrayList();
  static final ArrayList ROLES_2 = new ArrayList();

  //note: these are dependent on options set in login config
  static final int    MAX_LOGIN_TRIES = 3;
  static final int    FREEZE_TIMEOUT  = 10 * 60 * 1000; //10 minutes

  UserLoginEvent[] _events;
  Hashtable        _roles = new Hashtable();

  public UserLoginActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UserLoginActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ****************** ActionTestHelper methods ****************************
  protected void unitTest() throws Exception
  {
    //session[0]
    signonRejectByAction();
    signonBadLoginInfo();
    signonMaxTriesNotReached();   //signon(ID0)
    signonToSameSession();
    //session[1]
    signonMaxTriesReached();
    signonFreezeTimeout();        //signon(ID1)
    //session[2],session[3]
    signonToMultiSessions(); //signon(ID0),signon(ID1)
    //session[0],session[1]
    signonAfterSignoff();   //signon(ID0),signon(ID1)
  }

  private void signonRejectByAction()
  {
    Log.log("TEST", "[UserLoginActionTest.signonRejectByAction] Start ");
    // ***** REJECTED BY ACTION ***********
    //null event
    signonCheckFail(null, _sessions[0], _sm[0], true);

    //invalid session
    signonCheckFail(_events[0], "", _sm[0], true);
    Log.log("TEST", "[UserLoginActionTest.signonRejectByAction] End ");
  }

  private void signonBadLoginInfo()
  {
    Log.log("TEST", "[UserLoginActionTest.signonBadLoginInfo] Start ");
    // ***** REJECTED DUE TO BAD LOGIN INFO
    //invalid user
    signonCheckFail(_events[4], _sessions[0], _sm[0], false);

    //invalid password
    signonCheckFail(_events[5], _sessions[0], _sm[0], false);
    Log.log("TEST", "[UserLoginActionTest.signonBadLoginInfo] End ");
  }

  private void signonMaxTriesNotReached()
  {
    Log.log("TEST", "[UserLoginActionTest.signonMaxTriesNotReached] Start ");
    // ******* ACCOUNT NOT FREEZED IF MAX TRIES NOT REACHED
    for (int i=0; i<MAX_LOGIN_TRIES-1; i++)
    {
      signonCheckFail(_events[2], _sessions[0], _sm[0], false);
    }
    //check that account is NOT freezed
    checkAccount(_accts[0].getId(), false, MAX_LOGIN_TRIES-1);
    //login should still be allowed
    signonCheckSuccess(_events[0], _sessions[0], _sm[0]);
    Log.log("TEST", "[UserLoginActionTest.signonMaxTriesNotReached] End ");
  }

  private void signonToSameSession()
  {
    Log.log("TEST", "[UserLoginActionTest.signonToSameSession] Start ");
    // ***** LOGIN TO SAME SESSION SHOULD NOT BE ALLOWED
    //same user
    signonCheckFail(_events[0], _sessions[0], _sm[0], false);
    //different user
    signonCheckFail(_events[1], _sessions[0], _sm[0], false);
    Log.log("TEST", "[UserLoginActionTest.signonToSameSession] End ");
  }

  private void signonMaxTriesReached()
  {
    Log.log("TEST", "[UserLoginActionTest.signonMaxTriesReached] Start ");
    // ***** ACCOUNT SHOULD BE FREEZED IF MAX TRIES REACHED
    for (int i=0; i<MAX_LOGIN_TRIES; i++)
    {
      signonCheckFail(_events[3], _sessions[1], _sm[1], false);
    }
    //check that account is freezed
    checkAccount(_accts[1].getId(), true, MAX_LOGIN_TRIES);
    //check that next login with correct info cannot succeed also
    signonCheckFail(_events[1], _sessions[1], _sm[1], false);
    Log.log("TEST", "[UserLoginActionTest.signonMaxTriesReached] End ");
  }

  private void signonFreezeTimeout()
  {
    Log.log("TEST", "[UserLoginActionTest.signonFreezeTimeout] Start ");
    // ***** SHOULD BE ACCEPTED AFTER FREEZE TIMEOUT
    //wait until timeout
    waitForTimeout();
    //this time must allow
    signonCheckSuccess(_events[1], _sessions[1], _sm[1]);
    Log.log("TEST", "[UserLoginActionTest.signonFreezeTimeout] End ");
  }

  private void signonToMultiSessions()
  {
    Log.log("TEST", "[UserLoginActionTest.signonToMultiSessions] Start ");
    // ***** LOGIN TO MULTIPLE SESSIONS SHOULD BE ALLOWED
    signonCheckSuccess(_events[0], _sessions[2], _sm[2]);
    signonCheckSuccess(_events[1], _sessions[3], _sm[2]);
    Log.log("TEST", "[UserLoginActionTest.signonToMultiSessions] End ");
  }

  private void signonAfterSignoff()
  {
    Log.log("TEST", "[UserLoginActionTest.signonAfterSignoff] Start ");
    // ***** SIGNON PERMITTED AFTER SIGNOFF FROM SESSION
    signoff(_sessions[0], _accts[0].getId(), _sm[0]);
    signoff(_sessions[1], _accts[1].getId(), _sm[1]);
    signonCheckSuccess(_events[0], _sessions[1], _sm[1]);
    signonCheckSuccess(_events[1], _sessions[0], _sm[0]);
    Log.log("TEST", "[UserLoginActionTest.signonAfterSignoff] End ");
  }

  protected void prepareTestData() throws Exception
  {
    //prepare test data
    createUsers(3);

    //assign roles for ID0
    assignRole(new Long(_accts[0].getUId()), ROLE_1, ROLE_DESC_1);
    assignRole(new Long(_accts[0].getUId()), ROLE_2, ROLE_DESC_2);
    ROLES_1.add(ROLE_1);
    ROLES_1.add(ROLE_2);
    _roles.put(_accts[0].getId(), ROLES_1);
    _roles.put(_accts[1].getId(), ROLES_2);
    _roles.put(_accts[2].getId(), ROLES_2);

    createSessions(4);
    createStateMachines(4);

    _events = new UserLoginEvent[]
    {
      createLoginEvent(_accts[0].getId(), PASSWORD),
      createLoginEvent(_accts[1].getId(), PASSWORD),
      createLoginEvent(_accts[0].getId(), "9999"),
      createLoginEvent(_accts[1].getId(), "9999"),
      createLoginEvent("9999", PASSWORD),
      createLoginEvent(_accts[2].getId(), "9999"),
    };
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void cleanUp()
  {
    deleteUsers(3);
    deleteTestRole(ROLE_1);
    deleteTestRole(ROLE_2);
//    deleteTestSessions();
  }

  protected IEJBAction createNewAction()
  {
    return new UserLoginAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UserLoginEvent loginEvent = (UserLoginEvent)event;

    UserAccount acct = findUserAccountByUserId(loginEvent.getUserID());
    UserAccountState acctState = acct.getAccountState();

    assertNotNull("Login Time is incorrect", acctState.getLastLoginTime());
    assertEquals("NumLoginTries is incorrect", 0, acctState.getNumLoginTries());
    assertEquals("IsFreeze is incorrect", false, acctState.isFreeze());

    checkAuthSession((Collection)_roles.get(loginEvent.getUserID()), sm);
  }

  // ******************** Own methods **********************************

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

  /*
  private void deleteTestSessions()
  {
    try
    {
      IEntityDAO dao = EntityDAOFactory.getInstance().getDAOFor(SessionAudit.ENTITY_NAME);

      ArrayList list = new ArrayList();
      for (int i=0; i<_sessions.length; i++)
        list.add(_sessions[i]);
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, SessionAudit.SESSION_ID, list, false);
      Collection sessions = dao.getEntityByFilter(filter);
      for (Iterator i=sessions.iterator(); i.hasNext(); )
      {
        SessionAudit audit = (SessionAudit)i.next();
        dao.remove(new Long(audit.getUId()));
      }
    }
    catch (Exception ex)
    {

    }

  }
*/

  private UserLoginEvent createLoginEvent(String userID, String password)
    throws Exception
  {
    return new UserLoginEvent(userID, password);
  }

  private void signonCheckSuccess(
    UserLoginEvent event, String session, StateMachine sm)
  {
    sm.setAttribute(IAttributeKeys.APPLICATION, APPLICATION);
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void signonCheckFail(
    UserLoginEvent event, String session, StateMachine sm, boolean eventEx)
  {
    sm.setAttribute(IAttributeKeys.APPLICATION, APPLICATION);
    checkFail(event, session, sm, eventEx, IErrorCode.LOGIN_ERROR);
  }

  private void checkAccount(String userID, boolean isFreeze, int numTries)
  {
    UserAccount acct = findUserAccountByUserId(userID);
    UserAccountState acctState = acct.getAccountState();

    assertEquals("IsFreeze is incorrect", isFreeze, acctState.isFreeze());
    assertEquals("NumLoginTries is incorrect", numTries, acctState.getNumLoginTries());
    if (isFreeze)
      assertNotNull("Freeze time is not updated", acctState.getFreezeTime());
  }

  private void waitForTimeout()
  {
    try
    {
      System.out.println("Waiting for "+FREEZE_TIMEOUT+" ms timeout....");
      Thread.sleep(FREEZE_TIMEOUT);
    }
    catch (InterruptedException ex)
    {

    }
  }

  private void checkAuthSession(Collection roles, StateMachine sm)
  {
    Set principals = (Set)sm.getAttribute(IAttributeKeys.PRINCIPALS);
    String userId = (String)sm.getAttribute(IAttributeKeys.USER_ID);

    assertNotNull("Principals is null", principals);

    Subject subject = new Subject();
    subject.getPrincipals().addAll(principals);

    UserPrincipal[] userPrincipals = (UserPrincipal[])subject.getPrincipals(UserPrincipal.class).toArray(new UserPrincipal[0]);
    RolePrincipal[] rolePrincipals = (RolePrincipal[])subject.getPrincipals(RolePrincipal.class).toArray(new RolePrincipal[0]);

    assertEquals("UserPrincipal set size incorrect", 1, userPrincipals.length);
    assertEquals("RolePrincipal set size incorrect", roles.size(), rolePrincipals.length);

    assertEquals("User principal is incorrect", userId, userPrincipals[0].toString());

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

  private void unassignRoles(Long userUID) throws Exception
  {
    Collection roles = _aclMgr.getRolesForSubject(userUID, UserAccount.ENTITY_NAME);
    _aclMgr.removeSubjectRoleForSubject(userUID, UserAccount.ENTITY_NAME);

    for (Iterator i=roles.iterator(); i.hasNext(); )
    {
      Role role = (Role)i.next();
      _aclMgr.deleteRole(new Long(role.getUId()));
    }
  }

}