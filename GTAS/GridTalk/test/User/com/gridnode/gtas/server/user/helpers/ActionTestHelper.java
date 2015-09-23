package com.gridnode.gtas.server.user.helpers;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.actions.UserLoginAction;
import com.gridnode.gtas.server.user.actions.UserLogoutAction;

import com.gridnode.pdip.app.user.model.*;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.*;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class ActionTestHelper extends TestCase
{
  protected static final String ID        = "testuserid";
  protected static final String NAME      = "testusername";
  protected static final String PASSWORD  = "testpassword";
  protected static final String EMAIL     = "testemail@gridnode.com";
  protected static final String PHONE     = "testphone";
  protected static final String PROPERTY  = "testproperty";
  protected static final short LOGIN_TRIES = 5;
  protected static final boolean IS_FREEZE = true;
  protected static final Date FREEZE_TIME = new Timestamp(System.currentTimeMillis());

  protected static final String UPD_NAME     = "updusername";
  protected static final String UPD_PASSWORD = "updpassword";
  protected static final String UPD_EMAIL    = "updemail@gridnode.com";
  protected static final String UPD_PHONE    = "updphone";
  protected static final String UPD_PROPERTY = "updproperty";
  protected static final boolean UPD_RESET_TRIES = false;
  protected static final boolean UPD_UNFREEZE    = false;
  protected static final short UPD_STATE         = UserAccountState.STATE_ENABLED;

  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_PASSWORD = "dummypassword";

  protected static final String ADMIN_ROLE     = "Administrator";

  protected IUserManagerObj _userMgr;
  protected IACLManagerObj  _aclMgr;
  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";

  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected Long[]              _uIDs;
  protected UserAccount[]       _accts;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _userMgr = ActionHelper.getUserManager();
      _aclMgr  = ActionHelper.getACLManager();
      _sessionMgr = getSessionMgr();

      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[ActionTestHelper.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[ActionTestHelper.tearDown] Exit");
  }

  protected void createSessions(int numSessions) throws Exception
  {
    _sessions = new String[numSessions];
    for (int i=0; i<numSessions; i++)
      _sessions[i] = openSession();
  }

  protected void createStateMachines(int numSM)
  {
    _sm = new StateMachine[numSM];
    for (int i=0; i<numSM; i++)
      _sm[i] = new StateMachine(null, null);
  }

  protected void createUsers(int numUsers) throws Exception
  {
    _accts = new UserAccount[numUsers];
    _uIDs = new Long[numUsers];
    for (int i=0; i<numUsers; i++)
    {
      createUser(ID+i, NAME+i);
      _accts[i] = findUserAccountByUserId(ID+i);
      _uIDs[i] = new Long(_accts[i].getUId());
    }
  }

  protected void deleteUsers(int numUsers)
  {
    for (int i=0; i<numUsers; i++)
    {
      deleteUser(ID+i);
    }
  }

  public void testPerform() throws Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
      prepareTestData();

      unitTest();

      cleanTestData();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Exit ");
    }

  }

  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected void deleteUser(String id)
  {
    //find existing acct
    try
    {
      Log.log("TEST", "[ActionTestHelper.deleteUser] Enter");

      UserAccount acct = _userMgr.findUserAccount(id);
      if (acct != null)
      {
        _userMgr.deleteUserAccount((Long)acct.getKey(), false);
      }
    }
    catch (Exception ex)
    {
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.deleteUser] Exit");
    }
  }

  protected void createUser(String id, String name)
  {
    createUser(id, name, PASSWORD, EMAIL, PHONE, PROPERTY);
  }

  protected void createUser(
    String id, String name, String password, String email, String phone,
    String property)
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.createUser] Enter");

      UserAccount acct = new UserAccount();
      acct.setId(id);
      acct.setPassword(new PasswordMask(password));
      acct.setEmail(email);
      acct.setPhone(phone);
      acct.setProperty(property);
      acct.setUserName(name);

      _userMgr.createUserAccount(acct);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createUser]", t);
      assertTrue("Error in createUser", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createUser] Exit");
    }
  }

  protected UserAccount findUserAccountByUserId(String userId)
  {
    UserAccount acct = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUserId] Enter");

      acct = _userMgr.findUserAccount(userId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountByUserId]", ex);
      assertTrue("Error in findUserAccountByUserId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUserId] Exit");
    }
    return acct;
  }

  protected UserAccount findUserAccountByUId(Long uId)
  {
    UserAccount acct = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUId] Enter");

      acct = _userMgr.findUserAccount(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountByUId]", ex);
      assertTrue("Error in findUserAccountByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUId] Exit");
    }
    return acct;
  }

  protected Collection findUserAccountKeys(IDataFilter filter)
  {
    Log.log("TEST", "[ActionTestHelper.findUserAccountKeys] Enter");

    Collection keys = null;
    try
    {
      keys = _userMgr.findUserAccountsKeys(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountKeys]", ex);
      assertTrue("Error in findUserAccountKeys", false);
    }

    Log.log("TEST", "[ActionTestHelper.findUserAccountKeys] Exit");
    return keys;
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
  }

  protected BasicEventResponse performEvent(
    IEJBAction action, IEvent event, String session, StateMachine sm)
    throws Exception
  {
    sm.setAttribute(IAttributeKeys.SESSION_ID, session);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  protected void signon(
    String session, String user, String password, StateMachine sm)
    throws Exception
  {
    BasicEventResponse response = null;
    try
    {
      UserLoginEvent event = new UserLoginEvent(user, password);
      sm.setAttribute(IAttributeKeys.APPLICATION, APPLICATION);

      response = performEvent(new UserLoginAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.signon]", ex);
      assertTrue("Event error", false);
    }

    //check response
    assertNotNull("response is null", response);
    assertTrue("event is not successful", response.isEventSuccessful());
  }

  protected void signoff(String session, String user, StateMachine sm)
  {
    BasicEventResponse response = null;
    try
    {
      sm.setAttribute(IAttributeKeys.USER_ID, user);
      response = performEvent(
                   new UserLogoutAction(),
                   new UserLogoutEvent(),
                   session,
                   sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.signoff]", ex);
      assertTrue("Error in signoff", false);
    }

    assertNotNull("response is null", response);
    assertTrue("event status in incorrect", response.isEventSuccessful());
  }

  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    //check response
    assertNotNull("response is null", response);
    assertTrue("event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertEquals("Error type is incorrect",
      ApplicationException.APPLICATION, response.getErrorType());
  }

  protected void assertResponsePass(BasicEventResponse response, short msgCode)
  {
    assertNotNull("response is null", response);
    assertTrue("event is not successful", response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
  }

  protected void checkFail(
    IEvent event, String session, StateMachine sm, boolean eventEx,
    short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[ActionTestHelper.checkFail]" +
        " Returning fail due to EventException: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected event exception caught: "+ex.getMessage(), false);

      Log.log("TEST", "[ActionTestHelper.checkFail] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkFail] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponseFail(response, msgCode);
  }

  protected void checkSuccess(
    IEvent event, String session, StateMachine sm, short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkSuccess] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, msgCode);

    checkActionEffect(response, event, sm);
  }

  protected Long setupAdminRole() throws Exception
  {
    Long roleUID = null;
    Role adminRole = _aclMgr.getRoleByRoleName(ADMIN_ROLE);
    if (adminRole == null)
    {
      roleUID = createRole(ADMIN_ROLE, ADMIN_ROLE);
      createAccessRights(roleUID, "*", "*", "*", null);
    }
    else
    {
      roleUID = (Long)adminRole.getKey();
      if (_aclMgr.getAccessRightsFor(roleUID, "*").isEmpty())
        createAccessRights(roleUID, "*", "*", "*", null);
    }

    return roleUID;
  }

  private Long createRole(String roleName, String roleDescr)
    throws Exception
  {
    Role role = new Role();
    role.setRole(roleName);
    role.setDescr(roleDescr);
    return _aclMgr.createRole(role);
  }

  private void createAccessRights(
    Long roleUId, String feature, String action, String dataType, IDataFilter criteria)
    throws Exception
  {
    AccessRight acr = new AccessRight();
    acr.setAction(action);
    acr.setDataType(dataType);
    acr.setCriteria(criteria);
    acr.setFeature(feature);
    acr.setRoleUID(roleUId);
    _aclMgr.createAccessRight(acr);
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}