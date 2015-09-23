/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAccessRightListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.AccessRight;
import com.gridnode.pdip.base.acl.model.Feature;

import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.gtas.events.acl.GetAccessRightListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ACLLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import junit.framework.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

/**
 * This test case tests the functionality of the GetAccessRightListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAccessRightListActionTest extends TestCase
{
  private static final String CLASSNAME = "GetAccessRightListActionTest";

  private static final String ROLE_NAME = "Role Name";
  private static final String DESCRIPTION = "Description";

  private static final Long ACR_UID_DUMMY = new Long(-9999);
  private static final String DATA_TYPE_DUMMY = "RUBBISH";

  private static final String FEATURE     = "Test Feature";
  private static final String DESC        = "Test Description";
  private static final String ACTION      = "Test Action";
  private static final String DATA_TYPE   = "Test Data Type ";
  private static final IDataFilter CRITERIA = new DataFilterImpl();
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

  private static final int NUM_ROWS = 9;
  private static final int MAX_ROWS_PER_PAGE = 4;
  private static final int START_ROW = 3;

  static
  {
    CRI_LIST_1.add(CRI_FIELD_ID_1);
    CRI_LIST_1.add(CRI_FIELD_ID_2);
    CRI_LIST_1.add(CRI_FIELD_ID_3);

    CRITERIA.addDomainFilter(null, CRI_FIELD_ID_1, CRI_LIST_1, false);

    CRITERIA.addRangeFilter(CRITERIA.getAndConnector(),
      CRI_FIELD_ID_2, CRI_VALUE_2_1, CRI_VALUE_2_2, false);

    CRITERIA.addSingleFilter(CRITERIA.getOrConnector(),
      CRI_FIELD_ID_3, CRITERIA.getEqualOperator(), CRI_VALUE_3, true);

    ACTION_LIST.add(ACTION);

    for (int i=0; i<NUM_ROWS; i++)
      DATATYPE_LIST.add(DATA_TYPE+i);
  }

  IACLManagerHome     _aclHome;
  IACLManagerObj      _aclMgr;
  IUserManagerHome    _userHome;
  IUserManagerObj     _userMgr;
  ISessionManagerHome _sessionHome;
  ISessionManagerObj  _sessionMgr;

  ArrayList             _openedSessions = new ArrayList();
  GetAccessRightListEvent[] _events;
  StateMachine[]        _sm;
  String[]              _sessions;

  private DataFilterImpl        FILTER;
  private Long ROLE_UID;
  private Long[] ACR_UIDS;
  private String LIST_ID;
  private Collection ACR_LIST;

  public GetAccessRightListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetAccessRightListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws Exception
  {
    try
    {
      ACLLogger.infoLog(CLASSNAME, "setUp", "Enter");

      lookupACLManager();
      lookupSessionManager();
//      lookupUserManager();
      cleanup();
    }
    finally
    {
      ACLLogger.infoLog(CLASSNAME, "setUp", "Exit");
    }
  }

  protected void tearDown() throws Exception
  {
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    cleanup();
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  public void testPerform() throws Exception
  {
   try
    {
      ACLLogger.infoLog(CLASSNAME, "testPerform", "Enter");

      prepareTestData();
      getRejectedByAction();
      getAcceptedByManager();
      cleanupTestData();

    }
    finally
    {
      ACLLogger.infoLog(CLASSNAME, "testPerform", "Exit");
    }
  }


  private void getRejectedByAction() throws Exception
  {
    // **  REJECTED BY ACTION

    // null event.
    checkGetAccessRightListFail(null, _sessions[0], _sm[0], true);

    // invalid session id
    checkGetAccessRightListFail(_events[0], "AAA", _sm[0], true);

    // not auth session
    checkGetAccessRightListFail(_events[0], _sessions[1], _sm[0], true);

    // invalid auth subject (session name)
    checkGetAccessRightListFail(_events[0], _sessions[0], _sm[1], true);
  }

  private void getAcceptedByManager() throws Exception
  {
    // *** ACCEPTED BY Manager

    // test get with no limit
    checkGetAccessRightListSuccess(_events[1], _sessions[0], _sm[0], ACR_LIST, 0, ACR_LIST.size());

    // test get with limit
    checkGetAccessRightListSuccess(_events[2], _sessions[0], _sm[0], ACR_LIST, 0, MAX_ROWS_PER_PAGE);

    // test get with start, limit
    checkGetAccessRightListSuccess(_events[3], _sessions[0], _sm[0], ACR_LIST, START_ROW,
      NUM_ROWS-START_ROW>=MAX_ROWS_PER_PAGE? MAX_ROWS_PER_PAGE : NUM_ROWS-START_ROW);

    // test get with cursor
    checkGetAccessRightListSuccess(_events[4], _sessions[0], _sm[0], ACR_LIST, 0, MAX_ROWS_PER_PAGE);
    for (int i=0; i<NUM_ROWS; i+=MAX_ROWS_PER_PAGE)
    {
      GetAccessRightListEvent getEvent =
        new GetAccessRightListEvent(LIST_ID, MAX_ROWS_PER_PAGE, i);
        checkGetAccessRightListSuccess(getEvent, _sessions[0], _sm[0], ACR_LIST,
        i, NUM_ROWS-i>=MAX_ROWS_PER_PAGE? MAX_ROWS_PER_PAGE : NUM_ROWS-i);
    }
  }

  private void checkGetAccessRightListSuccess(
    IEvent event, String session, StateMachine sm,
    Collection resultSet, int expStart, int expRows)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new GetAccessRightListAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      ACLLogger.errorLog(CLASSNAME, "checkGetAccessRightListSuccess", "Error Exit", ex);
      assertTrue("Event Exception", false);
    }
    assertNotNull("Response is null", response);
    assertTrue("Event status is incorrect", response.isEventSuccessful());
    assertEquals("Msg code incorrect", response.getMessageCode(), IErrorCode.NO_ERROR);
    assertNull("Error reason is not null", response.getErrorReason());
    assertNull("Error trace is not null", response.getErrorTrace());
    assertEquals("error type is not null", response.getErrorType(), -1);

    Object returnedData = response.getReturnData();
    assertNotNull("Returned Data is null", returnedData);
    assertTrue("ReturnData not of correct type", returnedData instanceof EntityListResponseData);

    checkAccessRightRetrieved(resultSet, expStart, expRows, (EntityListResponseData)returnedData);
  }

  private void checkAccessRightRetrieved(
    Collection resultSet, int expStart, int expRows, EntityListResponseData returnData)
  {
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) returnData;
    int remainingRows = resultSet.size() - expStart - expRows;
    assertEquals("Rows remaining is incorrect", remainingRows, listData.getRowsRemaining());
    assertEquals("start row is incorrect", expStart, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());
    LIST_ID = listData.getListID();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    int numPage = (int)Math.ceil((double) (resultSet.size() - expStart) / (double)MAX_ROWS_PER_PAGE);
    assertEquals("Entity list count is incorrect", expRows, entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] acrArray = resultSet.toArray();
    for (int i = 0; i < expRows; i++ )
    {
      checkAccessRight(entityObjs[i], (AccessRight) acrArray[expStart + i]);
    }
  }

  private void checkAccessRight(Object returnedObj, AccessRight expectedAcr)
  {
    assertTrue("Returned entity data type is incorrect", returnedObj instanceof Map);
    Map returnedAcr = (Map)returnedObj;
    assertEquals("ACR UID is incorrect", new Long(expectedAcr.getUId()), returnedAcr.get(AccessRight.UID));
    assertEquals("Action is different", expectedAcr.getAction(), returnedAcr.get(AccessRight.ACTION));
    if (expectedAcr.getCriteria() == null)
      assertNull("Criteria should be null", returnedAcr.get(AccessRight.CRITERIA));
    else
    {
      assertNotNull("Criteria should not be null", returnedAcr.get(AccessRight.CRITERIA));
      assertEquals("Critieria is different", expectedAcr.getCriteria().getFilterExpr(),
        ((DataFilterImpl)returnedAcr.get(AccessRight.CRITERIA)).getFilterExpr());
    }
    assertEquals("Data type is different", expectedAcr.getDataType(), returnedAcr.get(AccessRight.DATA_TYPE));
    assertEquals("Description is different", expectedAcr.getDescr(), returnedAcr.get(AccessRight.DESCRIPTION));
    assertEquals("Feature is different", expectedAcr.getFeature(), returnedAcr.get(AccessRight.FEATURE));
    assertEquals("Role is different", expectedAcr.getRoleUID(), returnedAcr.get(AccessRight.ROLE));
  }

  private void checkGetAccessRightListFail(
    IEvent event, String session, StateMachine sm, boolean eventEx)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new GetAccessRightListAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      ACLLogger.infoLog(CLASSNAME, "checkGetAccessRightListFail", "Fail due to event exception: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected Event Exception", false);
      ACLLogger.infoLog(CLASSNAME, "checkGetAccessRightListFail", "Exit");
      return;
    }
    catch (Exception ex)
    {
      ACLLogger.errorLog(CLASSNAME, "checkGetAccessRightListFail", "Error Exit", ex);
      assertTrue("Event Exception", false);
    }
    assertNotNull("Response is null", response);
    assertTrue("Event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.FIND_ENTITY_LIST_ERROR, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertEquals("error type is incorrect", response.getErrorType(), ApplicationException.APPLICATION);
  }

  // ************************ Utility Methods *********************

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

//  private void lookupUserManager() throws Exception
//  {
//    _userHome = (IUserManagerHome)ServiceLocator.instance(
//             ServiceLocator.CLIENT_CONTEXT).getHome(
//             IUserManagerHome.class.getName(),
//             IUserManagerHome.class);
//    assertNotNull("userHome is null", _userHome);
//    _userMgr = _userHome.create();
//    assertNotNull("userMgr is null", _userMgr);
//  }

  private void lookupSessionManager() throws Exception
  {
    _sessionHome = (ISessionManagerHome)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getHome(
             ISessionManagerHome.class.getName(),
             ISessionManagerHome.class);
    assertNotNull("sessionHome is null", _sessionHome);
    _sessionMgr = _sessionHome.create();
    assertNotNull("sessionMgr is null", _sessionMgr);
  }

  private void prepareTestData() throws Exception
  {
    createTestRoles();
    createTestFeatures();
    createTestAccessRights();

    FILTER = new DataFilterImpl();
    FILTER.addSingleFilter(null, AccessRight.ROLE, FILTER.getEqualOperator(),
      ROLE_UID, false);
    FILTER.setOrderFields(new Number[]{AccessRight.UID});

    _sm = new StateMachine[]
    {
      new StateMachine(null, null),
      new StateMachine(null, null),
    };

    _sessions = new String[]
    {
      openSession("TEST_USER"),
      openSession(null),
    };

    _sm[0].setAttribute(IAttributeKeys.USER_ID, "TEST_USER");
    _sm[1].setAttribute(IAttributeKeys.USER_ID, "RUBBISH");

    _events = new GetAccessRightListEvent[]
    {
      //0 used in getRejectedByAction
      createEvent(FILTER),

      //1-4 used in getAcceptedByManager
      createEvent(FILTER),
      createEvent(FILTER, MAX_ROWS_PER_PAGE),
      createEvent(FILTER, MAX_ROWS_PER_PAGE, START_ROW),
      createEvent(FILTER, MAX_ROWS_PER_PAGE, 0),
    };

    ACR_LIST = _aclMgr.getAccessRights(FILTER);
  }

  private void createTestAccessRights()
  {
    ACR_UIDS = new Long[NUM_ROWS];
    for (int i=0; i<NUM_ROWS; i++)
    {
      ACR_UIDS[i] = createTestAccessRight(
                      DESC, ROLE_UID, FEATURE, ACTION, DATA_TYPE+i, CRITERIA);
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
      ACLLogger.errorLog(CLASSNAME, "createTestAccessRight",
        "Unable to create accessright", ex);
      assertTrue("Error in createTestAccessRight", false);
      return null;
    }

  }

  private void createTestRoles()
  {
    createTestRole(ROLE_NAME, DESCRIPTION);
    Role role = getTestRole(ROLE_NAME);
    ROLE_UID = new Long(role.getUId());
  }

  private void createTestFeatures()
  {
    createTestFeature(FEATURE, DESCRIPTION, ACTION_LIST, DATATYPE_LIST);
  }

  private void cleanupTestData()
  {
    try
    {
      closeAllSessions();
    }
    catch (Exception ex)
    {
    }
  }

  private void cleanup()
  {
    deleteTestRole(ROLE_NAME);
    deleteTestFeature(FEATURE);
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
      ACLLogger.errorLog(CLASSNAME, "deleteTestRole", "Unable to delete test data with rolename = "
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
      ACLLogger.errorLog(CLASSNAME, "getTestRole", "Unable to retrieve test data with rolename = "
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
      ACLLogger.errorLog(CLASSNAME, "createTestRole", "Error creating role with rolename = "
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
      ACLLogger.errorLog(CLASSNAME, "createTestFeature", "Error creating feature:"
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
      ACLLogger.errorLog(CLASSNAME, "deleteTestFeature", "Unable to delete test feature: "
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
      ACLLogger.errorLog(CLASSNAME, "getTestFeature", "Unable to retrieve test feature: "
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
      ACLLogger.errorLog(CLASSNAME, "getTestAcr", "Unable to retrieve test access right: "
                                    + acrUID, ex);
      return null;
    }
  }

  private BasicEventResponse performEvent(
    IEJBAction action,
    IEvent event, String session, StateMachine sm)
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

  private GetAccessRightListEvent createEvent()
    throws Exception
  {
    return new GetAccessRightListEvent();
  }

  private GetAccessRightListEvent createEvent(DataFilterImpl filter)
  {
    return new GetAccessRightListEvent(filter);
  }

  private GetAccessRightListEvent createEvent(DataFilterImpl filter, int maxRow)
  {
    return new GetAccessRightListEvent(filter, maxRow);
  }

  private GetAccessRightListEvent createEvent(DataFilterImpl filter, int maxRow, int startRow)
  {
    return new GetAccessRightListEvent(filter, maxRow, startRow);
  }

  private GetAccessRightListEvent createEvent(String listID, int maxRow, int startRow)
    throws Exception
  {
    return new GetAccessRightListEvent(listID, maxRow, startRow);
  }

  private String openSession(String sessionName) throws Exception
  {
    String sessionID = _sessionMgr.openSession();
    if (sessionName != null)
    {
      _sessionMgr.authSession(sessionID, sessionName);
    }
    return sessionID;
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
}