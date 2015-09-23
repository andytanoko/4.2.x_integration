package com.gridnode.gtas.server.acl.helpers;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.events.user.UserLogoutEvent;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.actions.UserLoginAction;
import com.gridnode.gtas.server.user.actions.UserLogoutAction;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.SubjectRole;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ActionTestHelper extends TestCase
{
  protected static final int ERROR_TYPE_NA = -1;
  protected static final int ERROR_TYPE_SYSTEM = SystemException.SYSTEM;
  protected static final int ERROR_TYPE_APPLICATION = ApplicationException.APPLICATION;

  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_STRING   = "dummystring";

  protected static final String ADMIN_ROLE     = "Administrator";

  protected static final String ROLE_NAME = "test Role Name";
  protected static final String ROLE_DESC = "test Role Description";
  protected static final String NEW_ROLE_NAME = "newtest Role Name";
  protected static final String NEW_ROLE_DESC = "newtest Role Description";
  protected static final String UPD_ROLE_NAME = "updtest Role Name";
  protected static final String UPD_ROLE_DESC = "updtest Role Description";

  protected static final String USER_ID   = "testuserid";
  protected static final String USER_NAME = "testusername";
  protected static final String PASSWORD  = "testpassword";
  protected static final String EMAIL     = "testemail@gridnode.com";
  protected static final String PHONE     = "testphone";
  protected static final String PROPERTY  = "testproperty";
  protected static final String ADMIN_USERID = "admin";

  protected IUserManagerObj _userMgr;
  protected IACLManagerObj  _aclMgr;
  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";

  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected Long[]              _roleUIDs;
  protected Role[]              _roles;
  protected Long[]              _userUIDs;
  protected UserAccount[]       _users;

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

  protected void createRoles(int numRoles) throws Exception
  {
    _roles = new Role[numRoles];
    _roleUIDs = new Long[numRoles];
    for (int i=0; i<numRoles; i++)
    {
      createRole(ROLE_NAME+i, ROLE_DESC+i, true);
      _roles[i] = getRoleByName(ROLE_NAME+i);
      _roleUIDs[i] = new Long(_roles[i].getUId());
    }
  }

  protected void deleteRoles(int numRoles)
  {
    for (int i=0; i<numRoles; i++)
    {
      deleteRole(ROLE_NAME+i);
    }
  }

  protected void deleteUsers(int numUsers)
  {
    for (int i=0; i<numUsers; i++)
    {
      deleteUser(USER_ID+i);
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

  protected void createUsers(int numUsers)
  {
    _userUIDs = new Long[numUsers];
    _users    = new UserAccount[numUsers];

    for (int i=0; i<numUsers; i++)
    {
      createUser(USER_ID+i, USER_NAME+i);
      _users[i] = findUserAccountByUserId(USER_ID+i);
      _userUIDs[i] = new Long(_users[i].getUId());
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
    _sessionMgr.authSession(session, ADMIN_USERID);
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
    long startTime = System.currentTimeMillis();

    sm.setAttribute(IAttributeKeys.SESSION_ID, session);
    sm.setAttribute(IAttributeKeys.USER_ID, ADMIN_USERID);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    
    long endTime = System.currentTimeMillis();
    System.out.println("Event TimeToPerform (ms): "+(endTime-startTime));    
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
    assertEquals("event status is incorrect", false, response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
//    assertNotNull("Error reason is null", response.getErrorReason());
//    assertEquals("Error type is incorrect",
//      ApplicationException.APPLICATION, response.getErrorType());
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
    checkFailEffect(response, event, sm);
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

  protected void deleteRole(String roleName)
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.deleteRole] Enter ");
      Role deleted = getRoleByName(roleName);
      if (deleted != null)
         _aclMgr.deleteRole(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.deleteRole] Exit ");
    }
  }

  protected Role getRoleByName(String roleName)
  {
    Role role = null;
    try
    {
      Log.debug("TEST", "[ActionTestHelper.getRoleByName] Enter ");
      role = _aclMgr.getRoleByRoleName(roleName);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getRoleByName] Error ", ex);
      assertTrue("unable to getRoleByName", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getRoleByName] Exit ");
    }
    return role;
  }

  protected Role getRoleByUId(long uId)
  {
    Role role = null;
    try
    {
      Log.debug("TEST", "[ActionTestHelper.getRoleByUId] Enter ");
      role = _aclMgr.getRoleByUId(new Long(uId));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getRoleByUId] Error ", ex);
      assertTrue("unable to getRoleByUId", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getRoleByUId] Exit ");
    }

    return role;
  }

  protected Long getDefaultRoleUID() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Role.CAN_DELETE, filter.getEqualOperator(),
      Boolean.FALSE, false);
    
    Collection roleUIDs = _aclMgr.getRoleKeysByFilter(filter);   
    
    return (Long)roleUIDs.toArray()[0];
  }
  
  protected void createRole(String roleName, String description, boolean canDelete)
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.createRole] Enter ");

      Role role = new Role();
      role.setCanDelete(canDelete);
      role.setDescr(description);
      role.setRole(roleName);

      _aclMgr.createRole(role);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.createRole] Error ", ex);
      assertTrue("Error in createRole", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.createRole] Exit ");
    }
  }

  protected void updateRole(String roleName, String description, boolean canDelete,
               double version, long uId)
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.updateRole] Enter ");

      Role role = getRoleByUId(uId);

      role.setCanDelete(canDelete);
      role.setDescr(description);
      role.setRole(roleName);
      role.setVersion(version);

      _aclMgr.updateRole(role);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.updateRole] Error ", ex);
      assertTrue("Error in updateRole", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.updateRole] Exit ");
    }
  }

  protected void createSubjectRole(Long userUId, Long roleUId)
  {
    try
    {
      _aclMgr.assignRoleToSubject(roleUId, userUId, UserAccount.ENTITY_NAME);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.createSubjectRoleToDb] Error ", ex);
      assertTrue("Error in createSubjectRole", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.createSubjectRole] Exit");
    }
  }


  protected void deleteSubjectRoleBySubject(Long userUId)
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.deleteSubjectRoleBySubject] Enter ");
      _aclMgr.removeSubjectRoleForSubject(userUId, UserAccount.ENTITY_NAME);
    }
    catch (Exception ex)
    {
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.deleteSubjectRoleBySubject] Exit");
    }
  }

  protected SubjectRole getSubjectRole(
    Long userUId, Long roleUId,
    String userId, String roleName)
  {
    SubjectRole subjectRole = null;
    try
    {
      Log.debug("TEST", "[ActionTestHelper.getSubjectRole] Enter ");

      if (userUId == null)
      {
        UserAccount acct = findUserAccountByUserId(userId);
        Role role = getRoleByName(roleName);
        subjectRole = _aclMgr.getSubjectRole(
                        new Long(acct.getUId()), UserAccount.ENTITY_NAME,
                        new Long(role.getUId()));
      }
      else
        subjectRole = _aclMgr.getSubjectRole(
                        userUId, UserAccount.ENTITY_NAME, roleUId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getSubjectRole] Error ", ex);
      assertTrue("unable to getSubjectRole", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getSubjectRole] Exit ");
    }
    return subjectRole;
  }

  protected Collection getRolesForSubject(Long userUId, String userId)
  {
    Collection roles = null;
    try
    {
      Log.debug("TEST", "[ActionTestHelper.getRolesForSubject] Enter ");
      if (userUId == null)
      {
        UserAccount acct = findUserAccountByUserId(userId);
        roles = _aclMgr.getRolesForSubject(
                 new Long(acct.getUId()),
                 UserAccount.ENTITY_NAME);
      }
      else
        roles = _aclMgr.getRolesForSubject(
                  userUId,
                  UserAccount.ENTITY_NAME);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getRolesForSubject] Error", ex);
      assertTrue("unable to getRolesForSubject", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getRolesForSubject] Exit ");
    }
    return roles;
  }

  protected Collection getSubjectUIdsForRole(Long roleUId, String roleName)
  {
    Collection uIDs = null;

    try
    {
      Log.debug("TEST", "[ActionTestHelper.getSubjectUIdsForRole] Enter ");
      if (roleUId == null)
      {
        Role role = getRoleByName(roleName);
        uIDs = _aclMgr.getSubjectUIdsForRole(new Long(role.getUId()), UserAccount.ENTITY_NAME);
      }
      else
        uIDs = _aclMgr.getSubjectUIdsForRole(roleUId, UserAccount.ENTITY_NAME);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getSubjectUIdsForRole] Error", ex);
      assertTrue("unable to getSubjectUIdsForRole", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getSubjectUIdsForRole] Exit ");
    }

    return uIDs;
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
  protected void checkFailEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }
}