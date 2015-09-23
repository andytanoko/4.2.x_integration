/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCommInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.GetCommInfoEvent;
import com.gridnode.gtas.server.channel.actions.GetCommInfoAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
//import com.gridnode.pdip.app.channel.model.JMSCommInfo;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This Test class handles the test for retrieving of a new CommInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
public class GetCommInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  protected final boolean IS_DEFAULT_TPT = false;
  protected final String PROTOCOL_TYPE = JMSCommInfo.JMS;
  protected final String PROTOCOL_VERSION = JMSCommInfo.JMS_VERSION;
  protected final String IMPL_VERSION = JMSCommInfo.IMPL_VERSION;
  protected final int DEST_TYPE_1 = 1;

  protected final String HOST_1 = "Host 1";
  protected final int PORT_1 = 11;
  protected final String REF_ID_1 = "RefId 1";
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String DESTINATION_1 = "Dest 1";
  protected final String PASSWORD_1 = "PASS 1";
  protected final String USER_1 = "USER 1";

  protected CommInfo _jms1 = null;
  protected Long _jmsUId1 = null;
  protected Long _invalidJmsUId = new Long(-111111111111111111l);


  public GetCommInfoActionTest(String name)
  {
    super(name, "GetCommInfoActionTest", IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(GetCommInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataCommInfo(NAME_1);
  }

  protected void setupData() throws Exception
  {
    // create new data.
//    _jms1 = createTestDataCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_1, DESCRIPTION_1, DESTINATION_1,
//                DEST_TYPE_1, PASSWORD_1, USER_1);
    _jmsUId1 = addTestDataCommInfo(_jms1);
    _jms1 = findTestDataCommInfo(_jmsUId1);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetCommInfoAction action = new GetCommInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private GetCommInfoEvent getCommInfoEvent(Long uId)
    throws Exception
  {
    return new GetCommInfoEvent(uId);
  }

  public void testPerform() throws Exception
  {
    GetCommInfoEvent event = null;

    // Null Event
    checkTestPerformFail(null, true);

    event = getCommInfoEvent(_invalidJmsUId);
    checkTestPerformFail(event, false);

    event = getCommInfoEvent(_jmsUId1);
    checkTestPerformSuccess(event, _jms1);

  }

  protected void checkTestPerformSuccess(IEvent event, CommInfo expected) throws Exception
  {
    Map returnData = (Map) super.checkTestPerformSuccess(event);
    checkReturnData(returnData, expected);
  }

}
