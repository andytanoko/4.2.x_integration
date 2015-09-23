/**
 * This software is the proprietary information of GridNode Pte Ltd.
 *7 Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractChannelActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
//import com.gridnode.pdip.app.channel.model.JMSCommInfo;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.BasicTypedException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.AssertionFailedError;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * This Abstract class contains common methods for all Channel test.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

abstract public class AbstractChannelActionTest
  extends TestCase
{
  public String CLASSNAME = null;
  private IChannelManagerHome _channelHome = null;
  private IChannelManagerObj _channelRemote = null;
  private ISessionManagerHome _sessionHome = null;
  private ISessionManagerObj _sessionRemote = null;
  protected String _openSession = null;
  protected static final String _user = "123";
  protected short _errorMsgCode = 0;
  protected String _listId = null;

  public AbstractChannelActionTest(String name, String classname, short errorCode)
  {
    super(name);
    CLASSNAME = classname;
    _errorMsgCode = errorCode;
  }

  protected void setUp() throws Exception
  {
    try
    {
      ChannelLogger.infoLog(CLASSNAME, "setUp", "Enter");
      getChannelManager();
      getSessionManager();
      openSession();
      cleanupData();
      setupData();
    }
    finally
    {
      ChannelLogger.infoLog(CLASSNAME, "setUp", "Exit");
    }
  }

  protected void tearDown() throws Exception
  {
    ChannelLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    closeSession();
    cleanupData();
    ChannelLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  abstract protected void cleanupData() throws Exception;
  abstract protected void setupData() throws Exception;

  private void getChannelManager() throws Exception
  {
    if (_channelRemote == null || _channelHome == null)
    {
      _channelHome = (IChannelManagerHome)ServiceLocator.instance(
               ServiceLocator.CLIENT_CONTEXT).getHome(
               IChannelManagerHome.class.getName(),
               IChannelManagerHome.class);
      assertNotNull("home is null", _channelHome);
      _channelRemote = _channelHome.create();
      assertNotNull("remote is null", _channelRemote);
    }
  }

  private void getSessionManager() throws Exception
  {
    if (_sessionRemote == null || _sessionHome == null)
    {
      _sessionHome = (ISessionManagerHome)ServiceLocator.instance(
               ServiceLocator.CLIENT_CONTEXT).getHome(
               ISessionManagerHome.class.getName(),
               ISessionManagerHome.class);
      assertNotNull("home is null", _sessionHome);
      _sessionRemote = _sessionHome.create();
      assertNotNull("remote is null", _sessionRemote);
    }
  }

  //---------------------------------------------------------------------------------------------
  //  Methods to manipulate test data.
  //---------------------------------------------------------------------------------------------

  protected final CommInfo createTestDataCommInfo(String host, String implVersion,
            int port, String protocolType, Hashtable protocolDetail, String protocolVersion,
            String refId, boolean isDefaultTpt, String name, String description)
  {
    CommInfo info = new CommInfo();
    info.setDescription(description);
    //info.setHost(host);
    info.setIsDefaultTpt(isDefaultTpt);
    info.setName(name);
    //info.setPort(port);
    //info.setProtocolDetail(protocolDetail);
    info.setProtocolType(protocolType);
    //info.setProtocolVersion(protocolVersion);
    info.setRefId(refId);
    info.setTptImplVersion(implVersion);
    info.setURL(host+":"+port);
    return info;
  }
/*
  protected final JMSCommInfo createTestDataJMSCommInfo(String host, String implVersion,
            int port, String protocolType, String protocolVersion,
            String refId, boolean isDefaultTpt, String name, String description, String destination,
            int destType, String password, String user)
  {
    CommInfo info = new CommInfo();
    info.setDescription(description);
    //info.setDestination(destination);
    //info.setDestType(destType);
    //info.setHost(host);
    info.setIsDefaultTpt(isDefaultTpt);
    info.setName(name);
    //info.setPassword(password);
    //info.setPort(port);
    info.setProtocolType(protocolType);
    //info.setProtocolVersion(protocolVersion);
    info.setRefId(refId);
    info.setTptImplVersion(implVersion);
    //info.setUser(user);
    return info;
  }
*/
  protected final Long addTestDataCommInfo(CommInfo info) throws Exception
  {
    return _channelRemote.createCommInfo(info);
  }

  protected final void deleteTestDataCommInfo(String name) throws Exception
  {
    CommInfo info = findTestDataCommInfo(name);
    if (info != null)
      deleteTestDataCommInfo(new Long(info.getUId()));
  }

  protected final void deleteTestDataCommInfo(Long uId) throws Exception
  {
    _channelRemote.deleteCommInfo(uId);
  }

  protected final Collection findTestDataCommInfo(IDataFilter filter) throws Exception
  {
    return _channelRemote.getCommInfo(filter);
  }

  protected final CommInfo findTestDataCommInfo(String name) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.NAME, filter.getEqualOperator(), name, false);
    Collection c = _channelRemote.getCommInfo(filter);
    if (c.size() == 0)
      return null;
    else if (c.size() == 1)
      return (CommInfo) c.iterator().next();
    else
      throw new Exception("Unexpected size: " + c.size());
  }

  protected final CommInfo findTestDataCommInfo(Long uId) throws Exception
  {
    return _channelRemote.getCommInfo(uId);
  }


  protected final ChannelInfo createTestDataChannelInfo(String description, CommInfo commInfo,
            String refId, String type, String name)
  {
    ChannelInfo info = new ChannelInfo();
    info.setDescription(description);
    info.setName(name);
    info.setReferenceId(refId);
    info.setTptCommInfo(commInfo);
    info.setTptProtocolType(type);
    return info;
  }

  protected final Long addTestDataChannelInfo(ChannelInfo info) throws Exception
  {
    return _channelRemote.createChannelInfo(info);
  }

  protected final void deleteTestDataChannelInfo(String name) throws Exception
  {
    ChannelInfo info = findTestDataChannelInfo(name);
    if (info != null)
       deleteTestDataChannelInfo(new Long(info.getUId()));
  }

  protected final void deleteTestDataChannelInfo(Long uId) throws Exception
  {
    _channelRemote.deleteChannelInfo(uId);
  }

  protected final ChannelInfo findTestDataChannelInfo(Long uId) throws Exception
  {
    return _channelRemote.getChannelInfo(uId);
  }

  protected final Collection findTestDataChannelInfo(IDataFilter filter) throws Exception
  {
    ChannelLogger.infoLog(CLASSNAME, "findTestDataChannelInfoByFilter ", "Enter");
    return _channelRemote.getChannelInfo(filter);
  }

  protected final ChannelInfo findTestDataChannelInfo(String name) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.NAME, filter.getEqualOperator(), name, false);
    Collection c = _channelRemote.getChannelInfo(filter);
    if (c.size() == 0)
      return null;
    else if (c.size() == 1)
      return (ChannelInfo) c.iterator().next();
    else
      throw new Exception("Unexpected size: " + c.size());
  }

  protected final void updateTestDataChannelInfo(ChannelInfo info) throws Exception
  {
    _channelRemote.updateChannelInfo(info);
  }

//  protected void checkReturnData(EntityListResponseData returnData, Collection expectedData)
//            throws Exception
//  {
//    assertNotNull("returnData is null", returnData);
//    Collection entityList = returnData.getEntityList();
//    assertNotNull("Entity list is null", entityList);
//    assertEquals("Size are not the same:", expectedData.size(), entityList.size());
//    Object[] entityObjs = entityList.toArray();
//    Object[] expectedDataArray = expectedData.toArray();
//    for (int i = 0; i < entityObjs.length; i++ )
//    {
//      checkReturnData((Map)entityObjs[i], expectedDataArray[i]);
//    }
//  }
//
  protected void checkReturnData(EntityListResponseData returnData, Collection expectedData,
            int maxRows, int startRow)
            throws Exception
  {
    assertNotNull("returnData is null", returnData);
    Collection entityList = returnData.getEntityList();
    int allRows = expectedData.size();
    assertNotNull("Entity list is null", entityList);
    assertNotNull("Entity list is null", returnData.getListID());
    assertEquals("Start row are not the same.", startRow, returnData.getStartRow());
    // Check Size retrieved.
    if (allRows == 0)
    {
      assertEquals("Entity list is not empty", 0, entityList.size());
      return;
    }
    int firstRow = startRow;
    int rowsRetrieved = Math.min((allRows - startRow), maxRows);
    if (rowsRetrieved < 0)
       rowsRetrieved = allRows;
    assertEquals("Retrieved size is not expected:", rowsRetrieved, entityList.size());
    assertEquals("Remaining rows are not the same:", (allRows-startRow-rowsRetrieved), returnData.getRowsRemaining());
    //Compare objects.
    Object[] entityObjs = entityList.toArray();
    Object[] expectedDataArray = expectedData.toArray();
    for (int i = 0; i < rowsRetrieved; i++)
    {
      checkReturnData((Map)entityObjs[i], expectedDataArray[startRow + i]);
    }
  }

  protected void checkReturnData(Map returnData, Object expectedData) throws Exception
  {
    assertNotNull("returnData is null", returnData);
    if (expectedData instanceof CommInfo)
      checkReturnCommInfo(returnData, (CommInfo) expectedData);
    else if (expectedData instanceof ChannelInfo)
      checkReturnChannelInfo(returnData, (ChannelInfo) expectedData);
    else
      throw new Exception("Unrecognized Type: " + expectedData.getClass());
  }

  private final void checkReturnCommInfo(Map returnData, CommInfo expectedInfo)
  {
//    assertEquals("IsTptDefaults are different.",
//                                expectedInfo2.isDefaultTpt(), info1.get(ICommInfo.));
    assertEquals("Descriptions are different.", expectedInfo.getDescription(),
                        returnData.get(ICommInfo.DESCRIPTION));
//    assertEquals("Hosts are different.", expectedInfo.getHost(),
//                        returnData.get(ICommInfo.HOST));
    assertEquals("Names are different.", expectedInfo.getName(),
                        returnData.get(ICommInfo.NAME));
//    assertEquals("Ports are different.", new Integer(expectedInfo.getPort()),
//                        returnData.get(ICommInfo.PORT));
//    assertEquals("Protocol Details are different.", expectedInfo.getProtocolDetail(),
//                        returnData.get(ICommInfo.PROTOCOL_DETAIL));
    assertEquals("Protocol Types are different.", expectedInfo.getProtocolType(),
                        returnData.get(ICommInfo.PROTOCOL_TYPE));
//    assertEquals("Protocol Versions are different.", expectedInfo.getProtocolVersion(),
//                        returnData.get(ICommInfo.PROTOCOL_VERSION));
    assertEquals("Reference Ids are different.", expectedInfo.getRefId(),
                        returnData.get(ICommInfo.REF_ID));
    assertEquals("Implementaion versions are different.", expectedInfo.getTptImplVersion(),
                        returnData.get(ICommInfo.TPT_IMPL_VERSION));
  }

  private final void checkReturnChannelInfo(Map returnData, ChannelInfo expectedInfo)
  {
    assertEquals("Descriptions are different.", expectedInfo.getDescription(),
                               returnData.get(IChannelInfo.DESCRIPTION));
    assertEquals("Names are different.", expectedInfo.getName(),
                               returnData.get(IChannelInfo.NAME));
    assertEquals("ReferenceIds are different.", expectedInfo.getReferenceId(),
                               returnData.get(IChannelInfo.REF_ID));
    checkReturnCommInfo((Map) returnData.get(IChannelInfo.TPT_COMM_INFO),
                               expectedInfo.getTptCommInfo());
    assertEquals("Type are different.", expectedInfo.getTptProtocolType(),
                               returnData.get(IChannelInfo.TPT_PROTOCOL_TYPE));
  }

  //--------------------------------------------------------------------------------------------
  // Helper methods to test find methods.
  //--------------------------------------------------------------------------------------------

  protected final boolean isEmpty (Collection c)
  {
    return (c == null || c.size() <= 0);
  }

  protected final boolean isEmpty (Object[] o)
  {
    return (o == null || o.length <= 0);
  }

  protected final void checkWithDB(Object expectedObj2) throws Exception
  {
    if (expectedObj2 instanceof CommInfo)
    {
      CommInfo expected = (CommInfo) expectedObj2;
      checkIdenticalCommInfo(findTestDataCommInfo(expected.getName()), expected, false);
    }
    else if (expectedObj2 instanceof ChannelInfo)
    {
      ChannelInfo expected = (ChannelInfo) expectedObj2;
      checkIdenticalChannelInfo(findTestDataChannelInfo(expected.getName()), expected, false);
    }
    else
      throw new Exception("Objects not of the same type or unrecognize type: " + expectedObj2.getClass());
  }

  protected final void checkIdentical(Object obj1, Object expectedObj2, boolean checkUId) throws Exception
  {
    if ((obj1 instanceof CommInfo) && (expectedObj2 instanceof CommInfo))
       checkIdenticalCommInfo((CommInfo) obj1, (CommInfo) expectedObj2, checkUId);
    else if ((obj1 instanceof ChannelInfo) && (expectedObj2 instanceof ChannelInfo))
       checkIdenticalChannelInfo((ChannelInfo) obj1, (ChannelInfo) expectedObj2, checkUId);
    else
      throw new Exception("Objects not of the same type or unrecognize type!");
  }

  protected final void checkIdenticalCommInfo(CommInfo info1, CommInfo expectedInfo2, boolean checkUId)
  {
    assertEquals("IsTptDefaults are different.", expectedInfo2.isDefaultTpt(), info1.isDefaultTpt());
    assertEquals("Descriptions are different.", expectedInfo2.getDescription(), info1.getDescription());
    //assertEquals("Hosts are different.", expectedInfo2.getHost(), info1.getHost());
    assertEquals("Names are different.", expectedInfo2.getName(), info1.getName());
    //assertEquals("Ports are different.", expectedInfo2.getPort(), info1.getPort());
    assertEquals("Protocol Types are different.", expectedInfo2.getProtocolType(), info1.getProtocolType());
    //assertEquals("Protocol Versions are different.", expectedInfo2.getProtocolVersion(), info1.getProtocolVersion());
    assertEquals("Reference Ids are different.", expectedInfo2.getRefId(), info1.getRefId());
    assertEquals("Implementaion versions are different.", expectedInfo2.getTptImplVersion(), info1.getTptImplVersion());
    if (checkUId)
      assertEquals("UId are different.", expectedInfo2.getUId(), info1.getUId());
  }

  protected final void checkIdenticalChannelInfo(ChannelInfo info1, ChannelInfo expectedInfo2, boolean checkUId)
  {
    assertEquals("Descriptions are different.", expectedInfo2.getDescription(), info1.getDescription());
    assertEquals("Names are different.", expectedInfo2.getName(), info1.getName());
    assertEquals("ReferenceIds are different.", expectedInfo2.getReferenceId(), info1.getReferenceId());
    assertEquals("CommInfos are different.", expectedInfo2.getTptCommInfo(), info1.getTptCommInfo());
    assertEquals("Type are different.", expectedInfo2.getTptProtocolType(), info1.getTptProtocolType());
    if (checkUId)
      assertEquals("UIds are different.", expectedInfo2.getUId(), info1.getUId());
  }

  protected final void checkIdenticalResultList(Collection resultList, Object[] expectedList)
            throws Exception
  {
    if (isEmpty(resultList) && isEmpty(expectedList))
      return;
    else if (isEmpty(resultList))
      assertTrue("Return result is empty but expected result size is " + expectedList.length, false);
    else if (isEmpty(expectedList))
      assertTrue("Expected result is empty but return result size is " + resultList.size(), false);
    assertEquals("Expected result is not the same as the result.", + expectedList.length, resultList.size());
    Iterator iter = resultList.iterator();
    Object result = null;
    boolean[] expectedResultFound = new boolean[expectedList.length];
    for (int i = 0; i < expectedResultFound.length; i++)
      expectedResultFound[i] = false;
    while (iter.hasNext())
    {
      result = iter.next();
      for (int i = 0; i < expectedList.length; i++)
      {
        try
        {
          checkIdentical(result, expectedList[i], false);
          expectedResultFound[i] = true;
          break;
        }
        catch (AssertionFailedError er)
        {
        }
      }
    }
    for (int i = 0; i < expectedResultFound.length; i++)
      assertTrue("Expected item [" + i + "] not found!", expectedResultFound[i]);
  }

  protected final void checkNonIdenticalResultList(Collection resultList, Object[] expectedList)
            throws Exception
  {
    // Check both not empty.
    if (isEmpty(resultList) && isEmpty(expectedList))
      assertTrue("Return result and expected result are both empty!", false);
    //Check not the same size.

    if (resultList.size() != expectedList.length)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetResultFail",
            "Expected size and return result size is not the same!");
      return;
    }
    Iterator iter = resultList.iterator();
    Object result = null;
    boolean[] expectedResultFound = new boolean[expectedList.length];
    for (int i = 0; i < expectedResultFound.length; i++)
      expectedResultFound[i] = false;
    while (iter.hasNext())
    {
      result = iter.next();
      for (int i = 0; i < expectedList.length; i++)
      {
        try
        {
          checkIdentical(result, expectedList[i], false);
          expectedResultFound[i] = true;
          break;
        }
        catch (AssertionFailedError er)
        {
        }
      }
    }
    for (int i = 0; i < expectedResultFound.length; i++)
      if (expectedResultFound [i] == false)
         return;
    assertTrue("Expected and result are the same!", false);
  }

  protected Object checkTestPerformSuccess(IEvent event) throws Exception
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(event);
    }
    catch (Exception ex)
    {
      ChannelLogger.errorLog(CLASSNAME, "testPerformSuccess", "Error Exit", ex);
      assertTrue("Event Exception", false);
    }
    assertNotNull("Response is null", response);
    assertTrue("Event status is incorrect", response.isEventSuccessful());
    assertEquals("Msg code incorrect", response.getMessageCode(), IErrorCode.NO_ERROR);
    assertNull("Error reason is not null", response.getErrorReason());
    assertNull("Error trace is not null", response.getErrorTrace());
    assertEquals("Error type is not null", response.getErrorType(), -1);
    return response.getReturnData();
  }

  protected void checkTestPerformFail(IEvent event, boolean eventException)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(event);
    }
    catch (EventException ex)
    {
      if (!eventException)
      {
        ChannelLogger.errorLog(CLASSNAME, "testPerformFail", "Unexpected Event Exception: ", ex);
        assertTrue("Unexpected Event Exception", false);
      }
      ChannelLogger.infoLog(CLASSNAME, "testPerformFail", "Fail due to event exception: "+ex.getMessage());
      return;
    }
    catch (Exception ex)
    {
      ChannelLogger.debugLog(CLASSNAME, "**********", "Exception class:"+ex.getClass().getName());
      ChannelLogger.errorLog(CLASSNAME, "testPerformFail", "Error Exit", ex);
      assertTrue("Other Exception", false);
    }
    assertNotNull("Response is null", response);
    assertTrue("Event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", _errorMsgCode, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertNotNull("Error trace is null", response.getErrorTrace());
//    assertEquals("error type is incorrect", response.getErrorType(), BasicTypedException.APPLICATION);
  }

  abstract protected BasicEventResponse performEvent(IEvent event) throws Exception;

  protected void openSession() throws Exception
  {
    _openSession = _sessionRemote.openSession();
    _sessionRemote.authSession(_openSession, _user);
  }

  protected void closeSession() throws Exception
  {
    _sessionRemote.closeSession(_openSession);
    _sessionRemote.deleteSessions(new Date());
  }

  protected void checkTestPerformSuccess(IEvent event, Collection expected) throws Exception
  {
    EntityListResponseData returnData = (EntityListResponseData) checkTestPerformSuccess(event);
    checkReturnData(returnData, expected, -1 , 0);
    _listId = returnData.getListID();
  }

  protected void checkTestPerformSuccess(IEvent event, Collection expected, int maxRows) throws Exception
  {
    EntityListResponseData returnData = (EntityListResponseData) checkTestPerformSuccess(event);
    checkReturnData(returnData, expected, maxRows, 0);
    _listId = returnData.getListID();
  }

  protected void checkTestPerformSuccess(IEvent event, Collection expected, int maxRows, int startRow) throws Exception
  {
    EntityListResponseData returnData = (EntityListResponseData) checkTestPerformSuccess(event);
    checkReturnData(returnData, expected, maxRows, startRow);
    _listId = returnData.getListID();
  }

  protected DataFilterImpl addFilter(Object field, Object value, DataFilterImpl filter)
  {
    if (value != null)
    {
      if (filter == null)
      {
        filter = new DataFilterImpl();
        filter.addSingleFilter(null,
                               field,
                               filter.getEqualOperator(),
                               value,
                               false);
      }
      else
        filter.addSingleFilter(filter.getAndConnector(),
                               field,
                               filter.getEqualOperator(),
                               value,
                               false);
    }
    return filter;
  }

}