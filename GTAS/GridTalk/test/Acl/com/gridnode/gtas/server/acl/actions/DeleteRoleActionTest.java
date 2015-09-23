/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteRoleActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Re-write test case.
 * Jul 17 2003    Neo Sok Lay         Re-write test case.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.DeleteRoleEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityActionResponseData;
import com.gridnode.pdip.framework.rpf.event.EntityListActionResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

/**
 * To test the DeleteRoleAction.
 * <p>
 * The following are tested:
 * <pre>
 * 1. Deletion of a pre-configured default Role.
 * 2. Deletion of a non-default Role that has at least 1 UserAccount assigned.
 * 3. Deletion of a non-default Role that has no UserAccount assigned.
 * </pre>
 * <p>
 * The results for each test:
 * <pre>
 * 1. Error: Deletion is not enabled.
 * 2. Error: Dependency exists in the UserAccount(s)
 * 3. Successful deletion.
 * </pre>
 */
public class DeleteRoleActionTest extends ActionTestHelper
{
  private class ExpectedResult
  {
    boolean _isSuccess;
    int _failCode;
    int _errorType;
    ExpectedResult(boolean success, int errorType, int failCode)
    {
      _isSuccess = success;
      _errorType = errorType;
      _failCode = failCode;
    }
  }

  DeleteRoleEvent[] _events;
  Hashtable[] _eventExpectedResults;

  int _currExpectedResultsIndex = -1;

  public DeleteRoleActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteRoleActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ****************** ActionTestHelper methods ***********************

  protected void unitTest() throws Exception
  {
    deleteCheckFail(_events[0], _sessions[0], _sm[0], false, 0);

    deleteSubjectRoleBySubject(_userUIDs[0]);
    deleteSubjectRoleBySubject(_userUIDs[1]);
      
    deleteCheckSuccess(_events[1], _sessions[0], _sm[0], 1);
  }

  protected void cleanUp()
  {
    deleteRoles(2);
    deleteUsers(2);
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(2);
    createUsers(2);
    createSubjectRole(_userUIDs[0], _roleUIDs[0]);
    createSubjectRole(_userUIDs[1], _roleUIDs[0]);
    Long defaultRoleUID = getDefaultRoleUID();
    
    Collection roleUIDs = new ArrayList();
    roleUIDs.add(getDefaultRoleUID());
    for (int i=0; i<_roleUIDs.length; i++)
      roleUIDs.add(_roleUIDs[i]);
          
    createSessions(1);
    createStateMachines(1);

    _events = new DeleteRoleEvent[]
              {
                deleteRoleEvent(roleUIDs),
                //success: after unassigning user
                deleteRoleEvent(_roleUIDs[0]),
              };
    
    ExpectedResult[][] expectedResults = new ExpectedResult[][]
    {
      { 
        new ExpectedResult(false, ERROR_TYPE_APPLICATION, IErrorCode.DELETE_NOT_ENABLED_ERROR),
        new ExpectedResult(false, ERROR_TYPE_APPLICATION, IErrorCode.DEPENDENCIES_EXIST_ERROR),
        new ExpectedResult(true, ERROR_TYPE_NA, IErrorCode.NO_ERROR),
      },
      {
        new ExpectedResult(true, ERROR_TYPE_NA, IErrorCode.NO_ERROR),
      },
    };

    _eventExpectedResults = new Hashtable[_events.length];
    for (int i=0; i<_events.length; i++)
    {
      _eventExpectedResults[i] = new Hashtable();
      
      Object[] uids = _events[i].getUids().toArray();
      for (int j=0; j<uids.length; j++)
      {
        _eventExpectedResults[i].put(uids[j], expectedResults[i][j]);
      }       
    }
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected IEJBAction createNewAction()
  {
    return new DeleteRoleAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertTrue("ReturnData is of incorrect type", returnData instanceof EntityListActionResponseData);

    EntityListActionResponseData listResponse = (EntityListActionResponseData)returnData;
    DeleteRoleEvent delEvent = (DeleteRoleEvent)event;
    
    // check the response data
    assertEquals("Response data count do not match", delEvent.getUids().size(), listResponse.getEntityDescriptors().length);

    Hashtable allExpected = _eventExpectedResults[_currExpectedResultsIndex];
    EntityActionResponseData[] result = listResponse.getResponseDataList(); 
    ExpectedResult expected;
    for (int i=0; i<result.length; i++)
    {
      System.out.println("\nExamining results for "+result[i].getDescription());
      expected = (ExpectedResult)allExpected.get(result[i].getKey());
      assertNotNull("ExpectedResult is not setup!", expected);             
      assertEquals("Success flag is incorrect", expected._isSuccess, result[i].isSuccess());
      assertEquals("Error type is incorrect", expected._errorType, result[i].getErrorType());
      assertEquals("Fail code is incorrect", expected._failCode, result[i].getFailCode());
    
      checkRoleInDb((Long)result[i].getKey());
    }
  }


  // ************************ Own methods ********************

  private DeleteRoleEvent deleteRoleEvent(Long roleUId) throws Exception
  {
    return new DeleteRoleEvent(roleUId);
  }

  private DeleteRoleEvent deleteRoleEvent(Collection roleUIds) throws Exception
  {
    return new DeleteRoleEvent(roleUIds);
  }

  private void deleteCheckSuccess(
    IEvent event, String session, StateMachine sm, int resultIndex)
  {
    _currExpectedResultsIndex = resultIndex; 
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void deleteCheckFail(
    IEvent event, String session, StateMachine sm, boolean eventEx, int resultIndex)
  {
    _currExpectedResultsIndex = resultIndex;
    checkFail(event, session, sm, eventEx, IErrorCode.DELETE_ENTITY_LIST_ERROR);
  }

  private void checkRoleInDb(Long dbRoleUId)
  {
    try
    {
      Role dbRole = getRoleByUId(dbRoleUId.longValue());
      assertNull("Data still exist in DB!", dbRole);
    }
    catch (Throwable ex)
    {
      Log.debug("TEST",
        "[DeleteRoleActionTest.checkRoleInDb] caught exception:"+ex.getMessage());
    }
  }


  /**
   * @see com.gridnode.gtas.server.acl.helpers.ActionTestHelper#checkFailEffect(BasicEventResponse, IEvent, StateMachine)
   */
  protected void checkFailEffect(
    BasicEventResponse response,
    IEvent event,
    StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertTrue("ReturnData is of incorrect type", returnData instanceof EntityListActionResponseData);

    EntityListActionResponseData listResponse = (EntityListActionResponseData)returnData;
    DeleteRoleEvent delEvent = (DeleteRoleEvent)event;
    
    // check the response data
    assertEquals("Response data count do not match", delEvent.getUids().size(), listResponse.getEntityDescriptors().length);

    Hashtable allExpected = _eventExpectedResults[_currExpectedResultsIndex];
    EntityActionResponseData[] result = listResponse.getResponseDataList(); 
    ExpectedResult expected;
    for (int i=0; i<result.length; i++)
    {
      System.out.println("\nExamining results for "+result[i].getDescription());
      expected = (ExpectedResult)allExpected.get(result[i].getKey());
      assertNotNull("ExpectedResult is not setup!", expected);             

      assertEquals("Success flag is incorrect", expected._isSuccess, result[i].isSuccess());
      if (!expected._isSuccess)
      {
        assertEquals("Error type is incorrect", expected._errorType, result[i].getErrorType());
        assertEquals("Fail code is incorrect", expected._failCode, result[i].getFailCode());
      }
      if (IErrorCode.DEPENDENCIES_EXIST_ERROR == expected._failCode)
      {
        assertTrue("Dependent set should not be empty", !result[i].getDependentSet().isEmpty());
        System.out.println("DependentSet>> "+result[i].getDependentSet());
      }
    }    
  }

}