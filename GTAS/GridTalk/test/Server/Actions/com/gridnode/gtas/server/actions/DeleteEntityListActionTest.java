/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteEntityListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 21 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EntityActionResponseData;
import com.gridnode.pdip.framework.rpf.event.EntityListActionResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Hashtable;

/**
 * This is the base test class for DeleteEntityList type of actions.
 * Implementation test classes should implement the following methods:
 * <pre>
 * cleanTestData()
 * cleanUp()
 * getActionClass()
 * getClassName()
 * getEventClass()
 * getEntityTestHelper()
 * prepareTestData()
 * unitTest()
 * setupManagers()
 * </pre>
 * @author Neo Sok Lay
 * @version GT 2.2 I1
 */
public abstract class DeleteEntityListActionTest extends EntityActionTest
{
  /**
   * Constructor for DeleteEntityListActionTest.
   * @param name
   */
  public DeleteEntityListActionTest(String name)
  {
    super(name);
  }

  /**
   * @see com.gridnode.gtas.server.actions.EntityActionTest#createTestEvent(IEntity)
   */
  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    return null;
  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#getDefaultMsgCode()
   */
  protected short getDefaultMsgCode()
  {
    return IErrorCode.DELETE_ENTITY_LIST_ERROR;
  }
  
 
  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#assertResponseFail(BasicEventResponse, short)
   */
  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    assertNotNull("Null Response", response);
    assertEquals("Incorrect Event Status", false, response.isEventSuccessful());
    assertEquals("Incorrect Msg Code", msgCode, response.getMessageCode());
  }

  protected void deleteCheckSuccess(
    String session, StateMachine sm, String testID)
  {
    _currTestID = testID; 
    checkSuccess((IEvent)getTestData(testID), session, sm, IErrorCode.NO_ERROR);
  }

  protected void deleteCheckFail(String session, StateMachine sm, boolean eventEx, String testID)
  {
    _currTestID = testID;
    checkFail((IEvent)getTestData(testID), session, sm, eventEx, IErrorCode.DELETE_ENTITY_LIST_ERROR);
  }

  protected void checkSuccessEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertTrue("ReturnData is of incorrect type", returnData instanceof EntityListActionResponseData);

    EntityListActionResponseData listResponse = (EntityListActionResponseData)returnData;
    DeleteEntityListEvent delEvent = (DeleteEntityListEvent)event;
    
    // check the response data
    assertEquals("Response data count do not match", delEvent.getUids().size(), listResponse.getEntityDescriptors().length);

    Hashtable allExpected = (Hashtable)_resultDataSet.get(_currTestID);
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
    
      assertEntityDeletedInDb((Long)result[i].getKey());
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
    DeleteEntityListEvent delEvent = (DeleteEntityListEvent)event;
    
    // check the response data
    assertEquals("Response data count do not match", delEvent.getUids().size(), listResponse.getEntityDescriptors().length);

    Hashtable allExpected = (Hashtable)_resultDataSet.get(_currTestID);
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

  protected void assertEntityDeletedInDb(Long uid)
  {
    assertNull("Fail to delete entity in Db", getEntityMayNotExist(uid));
  }

  protected DeleteEntityListEvent createEvent(Collection uIds) throws Exception
  {
    return (DeleteEntityListEvent)createTestEvent(getEventClass(), 
      new Class[] {Collection.class}, new Object[]{uIds});
  }
  
}
