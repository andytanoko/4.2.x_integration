/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.GetCommInfoListEvent;
import com.gridnode.gtas.server.channel.actions.GetCommInfoListAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
//import com.gridnode.pdip.app.channel.model.JMSCommInfo;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This Test class handles the test for creation of a new ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
public class GetCommInfoListActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  protected final boolean IS_DEFAULT_TPT = false;
  protected final String PROTOCOL_TYPE = JMSCommInfo.JMS;
  protected final String PROTOCOL_VERSION = JMSCommInfo.JMS_VERSION;
  protected final String IMPL_VERSION = JMSCommInfo.IMPL_VERSION;
  protected final int DEST_TYPE_1 = 1;
  protected final int DEST_TYPE_2 = 2;

  protected final String HOST_1 = "Host 1";
  protected final int PORT_1 = 11;
  protected final String REF_ID_1 = "RefId 1";
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String DESTINATION_1 = "Dest 1";
  protected final String PASSWORD_1 = "PASS 1";
  protected final String USER_1 = "USER 1";

  protected final String HOST_2 = "Host 2";
  protected final int PORT_2 = 22;
  protected final String REF_ID_2 = "RefId 2";
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String DESTINATION_2 = "Dest 2";
  protected final String PASSWORD_2 = "PASS 2";
  protected final String USER_2 = "USER 2";

  protected final String NAME_3 = "Name 3";
  protected final String NAME_4 = "Name 4";
  protected final String NAME_5 = "Name 5";
  protected final String NAME_6 = "Name 6";
  protected final String NON_EXIST_NAME = "Non Exist Name";

  private CommInfo _jms1 = null;
  private CommInfo _jms2 = null;
  private CommInfo _jms3 = null;
  private CommInfo _jms4 = null;
  private CommInfo _jms5 = null;
  private CommInfo _jms6 = null;

  private Long _jmsUId1 = null;
  private Long _jmsUId2 = null;
  private Long _jmsUId3 = null;
  private Long _jmsUId4 = null;
  private Long _jmsUId5 = null;
  private Long _jmsUId6 = null;

  public GetCommInfoListActionTest(String name)
  {
    super(name, "GetCommInfoListActionTest", IErrorCode.CREATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(GetCommInfoListActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataCommInfo(NAME_1);
    deleteTestDataCommInfo(NAME_2);
    deleteTestDataCommInfo(NAME_3);
    deleteTestDataCommInfo(NAME_4);
    deleteTestDataCommInfo(NAME_5);
    deleteTestDataCommInfo(NAME_6);
  }

  protected void setupData() throws Exception
  {
//    _jms1 = createTestDataJMSCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_1, DESCRIPTION_1, DESTINATION_1,
//                DEST_TYPE_1, PASSWORD_1, USER_1);
//    _jmsUId1 = addTestDataCommInfo(_jms1);
//    _jms1 = findTestDataCommInfo(_jmsUId1);
//
//    _jms2 = createTestDataJMSCommInfo(HOST_2, IMPL_VERSION, PORT_2, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_2, IS_DEFAULT_TPT, NAME_2, DESCRIPTION_2, DESTINATION_2,
//                DEST_TYPE_2, PASSWORD_2, USER_2);
//    _jmsUId2 = addTestDataCommInfo(_jms2);
//    _jms2 = findTestDataCommInfo(_jmsUId2);
//
//    _jms3 = createTestDataJMSCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_3, DESCRIPTION_1, DESTINATION_1,
//                DEST_TYPE_1, PASSWORD_1, USER_1);
//    _jmsUId3 = addTestDataCommInfo(_jms3);
//    _jms3 = findTestDataCommInfo(_jmsUId3);
//
//    _jms4 = createTestDataJMSCommInfo(HOST_2, IMPL_VERSION, PORT_2, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_2, IS_DEFAULT_TPT, NAME_4, DESCRIPTION_2, DESTINATION_2,
//                DEST_TYPE_2, PASSWORD_2, USER_2);
//    _jmsUId4 = addTestDataCommInfo(_jms4);
//    _jms4 = findTestDataCommInfo(_jmsUId4);
//
//    _jms5 = createTestDataJMSCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_5, DESCRIPTION_1, DESTINATION_1,
//                DEST_TYPE_1, PASSWORD_1, USER_1);
//    _jmsUId5 = addTestDataCommInfo(_jms5);
//    _jms5 = findTestDataCommInfo(_jmsUId5);
//
//    _jms6 = createTestDataJMSCommInfo(HOST_2, IMPL_VERSION, PORT_2, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_2, IS_DEFAULT_TPT, NAME_6, DESCRIPTION_2, DESTINATION_2,
//                DEST_TYPE_2, PASSWORD_2, USER_2);
//    _jmsUId6 = addTestDataCommInfo(_jms6);
//    _jms6 = findTestDataCommInfo(_jmsUId6);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetCommInfoListAction action = new GetCommInfoListAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private GetCommInfoListEvent getCommInfoListEvent()
    throws Exception
  {
    return new GetCommInfoListEvent();
  }

  private GetCommInfoListEvent getCommInfoListEvent(IDataFilter filter)
    throws Exception
  {
    return new GetCommInfoListEvent(filter);
  }

  private GetCommInfoListEvent getCommInfoListEvent(IDataFilter filter, int maxRows)
    throws Exception
  {
    return new GetCommInfoListEvent(filter, maxRows);
  }

  private GetCommInfoListEvent getCommInfoListEvent(IDataFilter filter, int maxRows,
          int startRow) throws Exception
  {
    return new GetCommInfoListEvent(filter, maxRows, startRow);
  }

  private GetCommInfoListEvent getCommInfoListEvent(String listID, int maxRows,
          int startRow) throws Exception
  {
    return new GetCommInfoListEvent(listID, maxRows, startRow);
  }

  public void testPerform() throws Exception
  {
    GetCommInfoListEvent event = null;
    Collection expected = null;
    IDataFilter filter = null;

    // Null Event
    checkTestPerformFail(null, true);

    // Retrieve all
    event = getCommInfoListEvent();
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected);

    //Retrieve 1 (unique name)
    filter = createFilter(NAME_1, null, null);
    event = getCommInfoListEvent(filter);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected);

    //Retrieve All with host = HOST_2
    filter = createFilter(null, HOST_2, null);
    event = getCommInfoListEvent(filter);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected);

    // Retrieve maximum of 3 rows.
    filter = createFilter(null, null, PROTOCOL_TYPE);
    event = getCommInfoListEvent(filter, 3);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected, 3);

    // Retrieve maximum of 10 rows (only has 6 rows).
    filter = createFilter(null, null, PROTOCOL_TYPE);
    event = getCommInfoListEvent(filter, 10);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected, 10);

    // Retrieve 2nd and 3rd rows
    filter = createFilter(null, null, PROTOCOL_TYPE);
    event = getCommInfoListEvent(filter, 2, 1);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected, 2, 1);
    // Using the List Id, retrieve 3nd and 4th rows
    event = getCommInfoListEvent(_listId, 4, 2);
    expected = findTestDataCommInfo(filter);
    checkTestPerformSuccess(event, expected, 4, 2);

    // Retrieved Non-existing
    filter = createFilter(NON_EXIST_NAME, null, null);
    event = getCommInfoListEvent(filter);
    expected = new Vector();
    checkTestPerformSuccess(event, expected);
  }

  private IDataFilter createFilter(String name, String host, String protocolType)
  {
    DataFilterImpl filter = null;
    filter = addFilter(CommInfo.NAME, name, filter);
    //filter = addFilter(CommInfo.HOST, host, filter);
    filter = addFilter(CommInfo.PROTOCOL_TYPE, protocolType, filter);
    return filter;
  }

}
