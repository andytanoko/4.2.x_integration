/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17 2002    Neo Sok Lay         Created
 * Jun 04 2002    Neo Sok Lay         Test with session & roles.
 */
package com.gridnode.pdip.app.user.facade.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.Handle;
import javax.security.auth.Subject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.app.user.exceptions.AuthenticateUserException;
import com.gridnode.pdip.app.user.login.AuthSubject;
import com.gridnode.pdip.app.user.login.UserPrincipal;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.base.acl.auth.RolePrincipal;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.model.SessionData;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

/**
 * Test case for testing UserManagerBean<P>
 *
 * This tests the following business methods in the UserManagerBean:<P>
 * <LI>testCreateUserAccount()          - mgr.createUserAccount(UserAccount)</LI>
 * <LI>testUpdateUserAccount()          - mgr.updateUserAccount(UserAccount)</LI>
 * <LI>testMarkDeleteUserAccount()      - mgr.deleteUserAccount(UID, true)</LI>
 * <LI>testFindUserAccountByUserId()    - mgr.findUserAccount(UserID)</LI>
 * <LI>testFindUserAccountsByState()    - mgr.findUserAccounts(State)</LI>
 * <LI>testFindUserAccountsByUserName() - mgr.findUserAccounts(UserName)</LI>
 * <P>
 *
 * The following test cases tests the mgr.login(Application,SessionID,UserID,Password)
 * and mgr.logout(SessionID) methods:<P>
 * <LI>testBadSignOnInfo() - tests for invalid user id, invalid password, multiple
 * login to same session</LI>
 * <LI>testFreezeAcct() - tests that the account will be freezed for login after
 * the maximum number of wrong credential tries allowed is exceeded. When the
 * freeze timeouts, login with correct credential should be allowed.</LI>
 * <LI>testGoodSignOnOff() - tests for happy scenario signon and signoff.</LI>
 * <LI>testLoginTries() - tests that the login tries is incremented after each
 * attempt of invalid password signon. Without exceeding the maximum tries allowed,
 * a valid signon should be allowed.</LI>
 * <P>
 * No test cases for the following methods in the UserManagerBean:<P>
 * <LI>mgr.findUserAccount(UID)</LI>
 * <LI>mgr.findUserAccounts(DataFilter)</LI>
 * <LI>mgr.findUserAccountsKeys(DataFilter)</LI>
 * <LI>mgr.deleteUserAccount(UID, false) -- implicitly tested by deleteTestData()</LI>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserManagerBeanTest
  extends    TestCase
{
  static final String TEST_ID = "testuserid";
  static final String TEST_NAME = "testusername";
  static final String TEST_PASSWORD = "testpassword";
  static final String TEST_EMAIL = "testemail@gridnode.com";
  static final String TEST_PHONE = "testphone";
  static final String TEST_PROPERTY = "testproperty";

  static final String UPD_NAME = "updusername";
  static final String UPD_PASSWORD = "updpassword";
  static final String UPD_EMAIL = "updemail@gridnode.com";
  static final String UPD_PHONE = "updphone";
  static final String UPD_PROPERTY = "updproperty";
  static final short  UPD_LOGIN_TRIES = 3;
  static final short  UPD_STATE = UserAccountState.STATE_DISABLED;
  static final boolean UPD_IS_FREEZE = true;
  static final Date    UPD_FREEZE_TIME = new Timestamp(System.currentTimeMillis());
  static final Date    UPD_LAST_LOGIN  = new Timestamp(System.currentTimeMillis());
  static final Date    UPD_LAST_LOGOUT = new Timestamp(System.currentTimeMillis());

  static final String ROLE_1     = "Role1";
  static final String ROLE_DESC_1= "Role 1";
  static final String ROLE_2     = "Role2";
  static final String ROLE_DESC_2= "Role 2";

  static final int    MAX_LOGIN_TRIES = 3;   //note: this is dependent on option
                                             //set in login config
  static final int    FREEZE_TIMEOUT  = 10 * 60 * 1000; //10 minutes

  IUserManagerHome    _userHome;
  IUserManagerObj     _userMgr;
  ISessionManagerHome _sessionHome;
  ISessionManagerObj  _sessionMgr;
  IACLManagerHome     _aclHome;
  IACLManagerObj      _aclMgr;

  UserAccount acct;
  UserAccountState acctState;
  Handle handle;
  ArrayList _openedSessions = new ArrayList();

  public UserManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UserManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[UserManagerBeanTest.setUp] Enter");

      lookupUserMgr();
      lookupSessionMgr();
      lookupACLMgr();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[UserManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[UserManagerBeanTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[UserManagerBeanTest.tearDown] Exit");
  }

  // *********************** test cases **************************** //

  // **************** Login / Logout *******************************
   public void testBadSignOnInfo() throws Exception
  {
    try
    {
      Log.log("TEST", "[UserManagerBeanTest.testBadSignOnInfo] Enter");

      createTestData(null, null);
      createTestData(TEST_ID+1, TEST_NAME+1);

      //multi login to same session --> fail
      String session1 = openSession();
      signon(session1, TEST_ID, TEST_PASSWORD);
      signonForAuthError(session1, TEST_ID+1, TEST_PASSWORD);
      signoff(session1, TEST_ID);

      //wrong cred
      String session2 = openSession();
      signonForAuthError(session2, TEST_ID, TEST_PASSWORD+"9999");
      //wrong user
      signonForAuthError(session2,  TEST_ID+9999, TEST_PASSWORD);
    }
    finally
    {
      Log.log("TEST", "[UserManagerBeanTest.testBadSignOnInfo] Exit");
    }
  }


  public void testLoginTries() throws Exception
  {
    try
    {
      Log.log("TEST", "[UserManagerBeanTest.testLoginTries] Enter");

      createTestData(null, null);

      String session = openSession();

      //try until reach tries allowed
      for (int i=0; i<MAX_LOGIN_TRIES-1; i++)
      {
	signonForAuthError(session, TEST_ID,  TEST_PASSWORD+"9999");
      }

      //check that account is NOT freezed
      acct = findUserAccountByUserId(TEST_ID);
      acctState = acct.getAccountState();

      assertTrue("IsFreeze is incorrect", !acctState.isFreeze());
      assertEquals("NumLoginTries is incorrect", MAX_LOGIN_TRIES-1, acctState.getNumLoginTries());

      //login should be allowed
      signon(session, TEST_ID, TEST_PASSWORD);
      signoff(session, TEST_ID);
    }
    finally
    {
      Log.log("TEST", "[UserManagerBeanTest.testLoginTries] Exit");
    }
  }

   public void testFreezeAcct() throws Exception
  {
    try
    {
      Log.log("TEST", "[UserManagerBeanTest.testFreezeAcct] Enter");

      createTestData(null, null);

      String session = openSession();

      //try until reach tries allowed
      for (int i=0; i<MAX_LOGIN_TRIES; i++)
      {
        signonForAuthError(session, TEST_ID,  TEST_PASSWORD+"9999");
      }

      //check that account is freezed
      acct = findUserAccountByUserId(TEST_ID);
      acctState = acct.getAccountState();
      Log.debug("TEST", "AcctState after freeze:"+acctState);

      assertTrue("IsFreeze is incorrect", acctState.isFreeze());
      assertNotNull("Freeze time is not updated", acctState.getFreezeTime());
      assertEquals("NumLoginTries is incorrect", MAX_LOGIN_TRIES, acctState.getNumLoginTries());

      //check that next login with correct info cannot succeed also
      session = openSession();
      signonForAuthError(session, TEST_ID, TEST_PASSWORD);
      //wait until timeout
      waitForTimeout();

      //this time must allow
      signon(session, TEST_ID, TEST_PASSWORD);
      signoff(session, TEST_ID);
    }
    finally
    {
      Log.log("TEST", "[UserManagerBeanTest.testFreezeAcct] Exit");
    }
  }

   public void testGoodSignOnOff() throws Exception
  {
    try
    {
      Log.log("TEST", "[UserManagerBeanTest.testGoodSignOnOff] Enter");

      createTestData(null, null);

      //no roles
      String session1 = openSession();
      signon(session1, TEST_ID, TEST_PASSWORD);
      checkAuthSession(session1, TEST_ID, new ArrayList());
      signoff(session1, TEST_ID);
      checkSessionDataRemoved(session1);

      //2 roles
      acct = findUserAccountByUserId(TEST_ID);
      assignRole(new Long(acct.getUId()), ROLE_1, ROLE_DESC_1);
      assignRole(new Long(acct.getUId()), ROLE_2, ROLE_DESC_2);

      ArrayList roles = new ArrayList();
      roles.add(ROLE_1);
      roles.add(ROLE_2);

      String session2 = openSession();
      signon(session2, TEST_ID, TEST_PASSWORD);
      checkAuthSession(session2, TEST_ID, roles);
      signoff(session2, TEST_ID);
      checkSessionDataRemoved(session2);

      unassignRoles(new Long(acct.getUId()));
    }
    finally
    {
      Log.log("TEST", "[UserManagerBeanTest.testGoodSignOnOff] Exit");
    }
  }

  // *************** Management **********************************

   public void testMarkDeleteUserAccount()
  {
    Log.log("TEST", "[UserManagerBeanTest.testDeleteUserAccount] Enter");

    createTestData(null, null);
    acct = findUserAccountByUserId(TEST_ID);

    //mark delete
    try
    {
      _userMgr.deleteUserAccount((Long)acct.getKey(), true);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.testDeleteUserAccount]", ex);
      assertTrue("Error in remote.deleteUserAccount", false);
    }

    //check that state is correct
    acct = findUserAccountByUserId(TEST_ID);
    acctState = acct.getAccountState();
    assertEquals("State is not 'deleted'", UserAccountState.STATE_DELETED,
      acctState.getState());

    Log.log("TEST", "[UserManagerBeanTest.testDeleteUserAccount] Exit");
  }

  public void testUpdateUserAccount()
  {
    Log.log("TEST", "[UserManagerBeanTest.testUpdateUserAccount] Enter");

    createTestData(null, null);

    acct = findUserAccountByUserId(TEST_ID);
    acctState = acct.getAccountState();

    Object uid = acct.getKey();
    Date createTime = acctState.getCreateTime();
    String createBy = acctState.getCreateBy();
    double version = acct.getVersion();

    //updates
    updateAcct(acct, acctState);
    try
    {
      _userMgr.updateUserAccount(acct);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.testUpdateUserAccount]", ex);
      assertTrue("Error in remote.updateUserAccount", false);
    }

    acct = findUserAccountByUserId(TEST_ID);
    acctState = acct.getAccountState();

    //check acct profile
    assertEquals("UID is inccorect", uid, acct.getKey());
    assertEquals("Id incorrect", TEST_ID, acct.getId());
    assertEquals("Password incorrect", maskPassword(UPD_PASSWORD), acct.getPassword());
    assertEquals("Name is incorrect", UPD_NAME, acct.getUserName());
    assertEquals("Phone is incorrect", UPD_PHONE, acct.getPhone());
    assertEquals("Email is incorrect", UPD_EMAIL, acct.getEmail());
    assertEquals("Property is incorrect", UPD_PROPERTY, acct.getProperty());
    assertTrue("Version is not incremented", version < acct.getVersion());

    //check acct state
    assertEquals("UserId in acct state incorrect", TEST_ID, acctState.getUserID());
    assertEquals("State is incorrect", UPD_STATE, acctState.getState());
    assertEquals("Login tries is incorrect", UPD_LOGIN_TRIES, acctState.getNumLoginTries());
    assertEquals("IsFreeze is incorrect", UPD_IS_FREEZE, acctState.isFreeze());
    assertEquals("Last login time is incorrect", UPD_LAST_LOGIN.getTime(), acctState.getLastLoginTime().getTime());
    assertEquals("Last logout time is incorrect", UPD_LAST_LOGOUT.getTime(), acctState.getLastLogoutTime().getTime());
    assertTrue("Can delete is incorrect", acctState.canDelete());
    assertEquals("CreateTime is incorrect", createTime.getTime(), acctState.getCreateTime().getTime());
    assertEquals("CreateBy is incorrect", createBy, acctState.getCreateBy());
    assertEquals("Freeze time is incorrect", UPD_FREEZE_TIME.getTime(), acctState.getFreezeTime().getTime());

    Log.log("TEST", "[UserManagerBeanTest.testUpdateUserAccount] Exit");
  }

  public void testCreateUserAccount()
  {
    Log.log("TEST", "[UserManagerBeanTest.testCreateUserAccount] Enter");

    createTestData(null, null);
    //check retrieved record
    UserAccount acct = findUserAccountByUserId(TEST_ID);

    Log.debug("TEST", "Found Acct: "+acct);
    assertNotNull("Acct retrieved is null", acct);
    UserAccountState acctState = acct.getAccountState();
    Log.debug("TEST", "Found AcctState: "+acctState);
    assertNotNull("UID is null", acct.getFieldValue(UserAccount.UID));
    assertEquals("Id incorrect", TEST_ID, acct.getId());
    assertEquals("Password incorrect", maskPassword(TEST_PASSWORD), acct.getPassword());
    assertEquals("Name is incorrect", TEST_NAME, acct.getUserName());
    assertEquals("Phone is incorrect", TEST_PHONE, acct.getPhone());
    assertEquals("Email is incorrect", TEST_EMAIL, acct.getEmail());
    assertEquals("Property is incorrect", TEST_PROPERTY, acct.getProperty());
    assertNotNull("Acct state is null", acctState);
    assertEquals("UserId in acct state incorrect", TEST_ID, acctState.getUserID());
    assertEquals("State is incorrect", UserAccountState.STATE_ENABLED, acctState.getState());
    assertEquals("Login tries is incorrect", 0, acctState.getNumLoginTries());
    assertEquals("IsFreeze is inccorrect", false, acctState.isFreeze());
    assertNull("Freeze time is not null", acctState.getFreezeTime());
    assertNotNull("Create time is null", acctState.getCreateTime());
    assertNull("Last login time is incorrect", acctState.getLastLoginTime());
    assertNull("Last logout time is incorrect", acctState.getLastLogoutTime());
    assertTrue("Can delete is incorrect", acctState.canDelete());

    Log.log("TEST", "[UserManagerBeanTest.testCreateUserAccount] Exit");
  }

  // ************** Finders ******************************************

  public void testFindUserAccountByUserId()
  {
    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountByUserId] Enter");
    createTestData(null, null);

    acct = findUserAccountByUserId(TEST_ID);

    Log.log("TEST", "Found Acct: "+acct);
    assertNotNull("Acct retrieved is null", acct);

    acctState = acct.getAccountState();
    Log.log("TEST", "Found AcctState: "+acctState);
    assertNotNull("UID is null", acct.getFieldValue(UserAccount.UID));

    assertEquals("Id incorrect", TEST_ID, acct.getId());
    assertEquals("UserId incorrect", TEST_ID, acctState.getUserID());

    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountByUserId] Exit");
  }

  public void testFindUserAccountsByUserName()
  {
    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountsByUserName] Enter");

    createTestData(null, null);
    //create another account with same name
    createTestData(TEST_ID+1, null);
    //create another account with different name
    createTestData(TEST_ID+2, TEST_NAME+2);

    Collection accts = null;
    try
    {
      accts = _userMgr.findUserAccounts(TEST_NAME);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.testFindUserAccountsByUserName]", ex);
      assertTrue("Error in remote.findUserAccounts", false);
    }

    assertNotNull("Returned Collection is null", accts);
    assertEquals("Returned result size incorrect", 2, accts.size());

    UserAccount[] acctsArray = (UserAccount[])accts.toArray(new UserAccount[2]);
    for (int i=0; i<acctsArray.length; i++)
    {
      Log.log("TEST", "Retrieved Acct: "+acctsArray[i]);
      assertEquals("UserName incorrect", TEST_NAME, acctsArray[i].getUserName());
    }

    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountsByUserName] Exit");
  }

  public void testFindUserAccountsByState()
  {
    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountsByState] Enter");

    createTestData(null, null);
    //create another account with same name
    createTestData(TEST_ID+1, TEST_NAME+1);
    //create another account with different name
    createTestData(TEST_ID+2, TEST_NAME+2);

    acct = findUserAccountByUserId(TEST_ID+1);
    acctState = acct.getAccountState();
    acctState.setState(UserAccountState.STATE_DISABLED);
    try
    {
      _userMgr.updateUserAccount(acct);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.testFindUserAccountsByState]", ex);
      assertTrue("Error in remote.updateUserAccount", false);
    }

    Collection accts = null;
    try
    {
      accts = _userMgr.findUserAccounts(UserAccountState.STATE_ENABLED);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.testFindUserAccountsByState]", ex);
      assertTrue("Error in remote.findUserAccounts", false);
    }

    assertNotNull("Returned Collection is null", accts);
    assertEquals("Returned result size incorrect", 2, accts.size());

    UserAccount[] acctsArray = (UserAccount[])accts.toArray(new UserAccount[2]);
    for (int i=0; i<acctsArray.length; i++)
    {
      Log.debug("TEST", "Retrieved Acct: "+acctsArray[i]);
      acctState = acctsArray[i].getAccountState();
      assertEquals("State incorrect", UserAccountState.STATE_ENABLED,
        acctState.getState());
    }

    Log.log("TEST", "[UserManagerBeanTest.testFindUserAccountsByState] Exit");
  }


  // ******************  utility methods ****************************
  private void lookupUserMgr() throws Exception
  {
    _userHome = (IUserManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IUserManagerHome.class);
    assertNotNull("UserManagerHome is null", _userHome);
    _userMgr = _userHome.create();
    assertNotNull("UserManagerObj is null", _userMgr);
  }

  private void lookupSessionMgr() throws Exception
  {
    _sessionHome = (ISessionManagerHome)ServiceLookup.getInstance(
                     ServiceLookup.CLIENT_CONTEXT).getHome(
                     ISessionManagerHome.class);
    assertNotNull("SessionManagerHome is null", _sessionHome);
    _sessionMgr = _sessionHome.create();
    assertNotNull("SessionManagerObj is null", _sessionMgr);
  }

  private void lookupACLMgr() throws Exception
  {
    _aclHome = (IACLManagerHome)ServiceLookup.getInstance(
                     ServiceLookup.CLIENT_CONTEXT).getHome(
                     IACLManagerHome.class);
    assertNotNull("ACLManagerHome is null", _aclHome);
    _aclMgr = _aclHome.create();
    assertNotNull("ACLManagerObj is null", _aclMgr);
  }

  private void cleanUp() throws Exception
  {
    deleteTestData(TEST_ID);
    deleteTestData(TEST_ID+1);
    deleteTestData(TEST_ID+2);
    closeAllSessions();
  }

  private void deleteTestSession(String session)
  {
    try
    {
      _userMgr.logout(session);
    }
    catch (Exception ex)
    {
    }
  }

  private void deleteTestData(String id)
  {
    Log.log("TEST", "[UserManagerBeanTest.deleteTestData] Enter");

    //find existing acct
    try
    {
      UserAccount acct = _userMgr.findUserAccount(id);
      if (acct != null)
      {
        Log.log("TEST", "Found acct to delete: "+acct.getKey());
        _userMgr.deleteUserAccount((Long)acct.getKey(), false);
      }
    }
    catch (Exception ex)
    {
      Log.log("TEST", "Unable to delete acct: "+id, ex);
    }
    Log.log("TEST", "[UserManagerBeanTest.deleteTestData] Exit");
  }

  private void createTestData(String id, String name)
  {
    Log.log("TEST", "[UserManagerBeanTest.createTestData] Enter");

    acct = new UserAccount();
    acct.setId((id==null)?TEST_ID:id);
    acct.setPassword(new PasswordMask(TEST_PASSWORD));
    acct.setEmail(TEST_EMAIL);
    acct.setPhone(TEST_PHONE);
    acct.setProperty(TEST_PROPERTY);
    acct.setUserName((name==null)?TEST_NAME:name);

    try
    {
      _userMgr.createUserAccount(acct);
      //NOTE: This acct will not be updated with the latest field changes
      //because this is remote invocation. The server side is only a copy of
      //this acct instance.
    }
    catch (Exception ex)
    {
      Log.err("TEST", "UserManagerBeanTest.createTestData]", ex);
      assertTrue("Error in createTestData", false);
    }
    Log.log("TEST", "[UserManagerBeanTest.createTestData] Exit");
  }

  private UserAccount findUserAccountByUserId(String userId)
  {
    Log.log("TEST", "[UserManagerBeanTest.findUserAccountByUserId] Enter");

    UserAccount acct = null;
    try
    {
      acct = _userMgr.findUserAccount(userId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.findUserAccountByUserId]", ex);
      assertTrue("Error in remote.findUserAccount", false);
    }

    Log.log("TEST", "[UserManagerBeanTest.findUserAccountByUserId] Exit");
    return acct;
  }


  private String maskPassword(String password)
  {
    PasswordMask mask = new PasswordMask(password, acct.getPasswordMaskLength());
    return mask.toString();
  }

  private void updateAcct(UserAccount acct, UserAccountState acctState)
  {
    acct.setEmail(UPD_EMAIL);
    acct.setPassword(new PasswordMask(UPD_PASSWORD));
    acct.setPhone(UPD_PHONE);
    acct.setProperty(UPD_PROPERTY);
    acct.setUserName(UPD_NAME);

    acctState.setFreezeTime(UPD_FREEZE_TIME);
    acctState.setIsFreeze(UPD_IS_FREEZE);
    acctState.setLastLoginTime(UPD_LAST_LOGIN);
    acctState.setLastLogoutTime(UPD_LAST_LOGOUT);
    acctState.setNumLoginTries(UPD_LOGIN_TRIES);
    acctState.setState(UPD_STATE);
  }

  private void signon(String session, String user, String password)
  {
    try
    {
      _userMgr.login("gridtalk", session, user, new PasswordMask(password).toString());
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.signon]", ex);
      assertTrue("Error in remote.login", false);
    }

    acct = findUserAccountByUserId(user);
    acctState = acct.getAccountState();
    Log.debug("TEST", "AcctState after login:"+acctState);

    assertNotNull("Login Time is incorrect", acctState.getLastLoginTime());
    assertEquals("NumLoginTries is incorrect", 0, acctState.getNumLoginTries());
    assertEquals("IsFreeze is incorrect", false, acctState.isFreeze());
  }

  private void signonForAuthError(String session, String user, String password)
  {
    boolean authEx = false;
    try
    {
      _userMgr.login("gridtalk", session, user, new PasswordMask(password).toString());
    }
    catch (AuthenticateUserException ex)
    {
      Log.debug("TEST", "[UserManagerBeanTest.signonForAuthError]", ex);
      authEx = true;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.signonForAuthError]", ex);
      assertTrue("Error in remote.login", false);
    }

    assertTrue("No authenticate exception thrown", authEx);
  }

  private void signoff(String session, String user)
  {
    try
    {
      _userMgr.logout(session);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UserManagerBeanTest.signoff]", ex);
      assertTrue("Error in remote.logout", false);
    }

    acct = findUserAccountByUserId(user);
    acctState = acct.getAccountState();

    Log.debug("TEST", "AcctState after logout:"+acctState);
    assertNotNull("Logout Time is incorrect", acctState.getLastLogoutTime());
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

  private void checkAuthSession(String session, String userId, Collection roles)
    throws Exception
  {
    assertTrue("Session is not authenticated!", _sessionMgr.isAuthSession(session));
    SessionData savedData = _sessionMgr.getSessionData(session,AuthSubject.KEY);
    assertNotNull("Subject not saved!", savedData);

    Subject subject = new AuthSubject(savedData).getSubject();
    assertNotNull("Subject is null", subject);
    UserPrincipal[] userPrincipals = (UserPrincipal[])subject.getPrincipals(UserPrincipal.class).toArray(new UserPrincipal[0]);
    RolePrincipal[] rolePrincipals = (RolePrincipal[])subject.getPrincipals(RolePrincipal.class).toArray(new RolePrincipal[0]);

    assertEquals("UserPrincipal set size incorrect", 1, userPrincipals.length);
    assertEquals("RolePrincipal set size incorrect", roles.size(), rolePrincipals.length);

    assertEquals("", userId, userPrincipals[0].toString());

    for (int i=0; i<rolePrincipals.length; i++)
    {
      assertTrue("Unexpected RolePrincipal returned", roles.contains(rolePrincipals[i].getRole()));
    }

  }

  private void checkSessionDataRemoved(String session) throws Exception
  {
    SessionData savedData = _sessionMgr.getSessionData(session,AuthSubject.KEY);
    assertNull("Subject not removed!", savedData.getDataContents());
  }

  private String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    return session;
  }

  private void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  private void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
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

/*
  public static void testSerialize()
  {
    try
    {
      UserPrincipal p= new UserPrincipal("aaa");

//      LinkedList list = new LinkedList();
//      list.add(p);
      Subject subject = new Subject();
      subject.getPrincipals().add(p);
//      byte[] contents = convertToDataContents(subject);
//      Object obj = getObject(contents);
//      System.out.println("Object deserialized: "+obj);
      AuthSubject authSub = new AuthSubject(subject);
      subject = authSub.getSubject();
      System.out.println("Deserialized Subject: "+subject);
      System.out.println("DeSerialized UserPrincipal: "+subject.getPrincipals(UserPrincipal.class));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private static byte[] convertToDataContents(Object obj)
  {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;

    try
    {
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));

      objOS.writeObject(obj);
      objOS.close();

      //return the object in byte content
      return baos.toByteArray();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally
    {
      try
      {
        if (objOS != null) objOS.close();
      }
      catch (Exception ex) {}
    }

  }

  public static Object getObject(byte[] contents)
  {
    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;

    try
    {
      //read
      bais = new ByteArrayInputStream(contents);
      objIS = new ObjectInputStream(new BufferedInputStream(bais));
      Object ret = objIS.readObject();
      objIS.close();

      return ret;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally
    {
      try
      {
        if (objIS != null) objIS.close();
      }
      catch (Exception ex) {}
    }
  }
*/
  // *********************** Runner *****************************

  public static void main(String args[])
  {
    //UserManagerBeanTest.testSerialize();
    junit.textui.TestRunner.run(suite());
  }


}