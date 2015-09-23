/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJMSCommInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.UpdateCommInfoEvent;
import com.gridnode.gtas.server.channel.actions.UpdateCommInfoAction;
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
 * This Test class handles the test for creation of a new ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateJMSCommInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);
  // Value that are not used in the event.
  protected final boolean IS_DEFAULT_TPT = false;
  protected final String PROTOCOL_TYPE = JMSCommInfo.JMS;
  protected final String PROTOCOL_VERSION = JMSCommInfo.JMS_VERSION;
  protected final String IMPL_VERSION = JMSCommInfo.IMPL_VERSION;
  protected final int DEST_TYPE_1 = 1;
  protected final int DEST_TYPE_2 = 2;
  // Invalid value for the event.
  protected final int INVALID_DEST_TYPE = -1;
  protected final int INVALID_PORT = -1;
  protected final int INVALID_UID = -19013;
  //Value to create CommInfo1
  protected final String HOST_1 = "Host 1";
  protected final int PORT_1 = 11;
  protected final String REF_ID_1 = "RefId 1";
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String DESTINATION_1 = "Dest 1";
  protected final String PASSWORD_1 = "PASS 1";
  protected final String USER_1 = "USER 1";
  //Value to create CommInfo2
  protected final String HOST_2 = "Host 2";
  protected final int PORT_2 = 22;
  protected final String REF_ID_2 = "RefId 2";
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String DESTINATION_2 = "Dest 2";
  protected final String PASSWORD_2 = "PASS 2";
  protected final String USER_2 = "USER 2";
  //Value to create CommInfo3
  protected final String HOST_3 = "Host 3";
  protected final int PORT_3 = 33;
  protected final String REF_ID_3 = "RefId 3";
  protected final String NAME_3 = "Name 3";
  protected final String DESCRIPTION_3 = "Desc 3";
  protected final String DESTINATION_3 = "Dest 3";
  protected final String PASSWORD_3 = "PASS 3";
  protected final String USER_3 = "USER 3";
  // exisitng database record.
  protected JMSCommInfo _jms1 = null;
  protected JMSCommInfo _jms2 = null;
  protected Long _jmsUId1 = null;
  protected Long _jmsUId2 = null;

  public UpdateJMSCommInfoActionTest(String name)
  {
    super(name, "UpdateJMSCommInfoActionTest", IErrorCode.UPDATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(UpdateJMSCommInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataCommInfo(NAME_1);
    deleteTestDataCommInfo(NAME_2);
    deleteTestDataCommInfo(NAME_3);
  }

  protected void setupData() throws Exception
  {
    // create new data.
//    _jms1 = createTestDataJMSCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_1, DESCRIPTION_1, DESTINATION_1,
//                DEST_TYPE_1, PASSWORD_1, USER_1);
//    _jmsUId1 = addTestDataCommInfo(_jms1);
//    _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//    _jms2 = createTestDataJMSCommInfo(HOST_2, IMPL_VERSION, PORT_2, PROTOCOL_TYPE,
//                PROTOCOL_VERSION, REF_ID_2, IS_DEFAULT_TPT, NAME_2, DESCRIPTION_2, DESTINATION_2,
//                DEST_TYPE_2, PASSWORD_2, USER_2);
//    _jmsUId2 = addTestDataCommInfo(_jms2);
//    _jms2 = getJMSCommInfoFromDB(_jmsUId2);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    UpdateCommInfoAction action = new UpdateCommInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private UpdateCommInfoEvent updateJMSCommInfoEvent(CommInfo info,
          String destination, int destType, String password, String user)
    throws Exception
  {
    return new UpdateCommInfoEvent(new Long(info.getUId()), info.getName(), info.getDescription(),    		info.getURL(), info.getProtocolType(),
           new Boolean(info.isPartner()));
  }

  public void testPerform() throws Exception
  {
    UpdateCommInfoEvent event = null;

    // Null Event
    checkTestPerformFail(null, true);
//    {
//      // non-existing.
//      JMSCommInfo jms3 = createTestDataJMSCommInfo(HOST_3, IMPL_VERSION, PORT_3, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_3, IS_DEFAULT_TPT, NAME_3, DESCRIPTION_3, DESTINATION_3,
//                  DEST_TYPE_1, PASSWORD_3, USER_3);
//      jms3.setUId(INVALID_UID);
//      event = updateJMSCommInfoEvent(jms3, DESTINATION_3, DEST_TYPE_1, PASSWORD_3, USER_3);
//      checkTestPerformFail(event, true);
//    }
//    {
//      // invalid destType.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, INVALID_DEST_TYPE, PASSWORD_1, USER_1);
//      checkTestPerformFail(event, true);
//    }
//    {
//      // duplicate destType.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_2, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }

//    try
//    {
//      // null host.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setHost(null);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      assertTrue("Host is null", false);
//    }
//    catch (EventException ex)
//    {
//    }
//
//    {
//      // duplicate host.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setHost(HOST_2);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // invalid port.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setPort(INVALID_PORT);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // duplicate port.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setPort(PORT_2);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // null reference id.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setRefId(null);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // duplicate reference Id.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setRefId(REF_ID_2);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    try
//    {
//      // null name.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setName(null);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      assertTrue("Name is null", false);
//    }
//    catch (EventException ex)
//    {
//    }
//
//    {
//      // duplicate name.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setName(NAME_2);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformFail(event, false);
//    }
//
//    {
//      // null description
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setDescription(null);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // duplicate description
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      _jms1.setDescription(DESCRIPTION_2);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // null destination.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, null, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate destination.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_2, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // null password.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, null, USER_1);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate password.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_2, USER_1);
//      checkTestPerformSuccess(event, _jms1);
//    }
//
//    {
//      // null user.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, null);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate user.
//      _jms1 = getJMSCommInfoFromDB(_jmsUId1);
//      event = updateJMSCommInfoEvent(_jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_2);
//      checkTestPerformSuccess(event, _jms1);
//    }

  }

  protected void checkTestPerformSuccess(IEvent event, CommInfo expected) throws Exception
  {
    Object returnData = super.checkTestPerformSuccess(event);
    checkWithDB(expected);
  }

//  protected JMSCommInfo getJMSCommInfoFromDB(Long uId) throws Exception
//  {
//    return new JMSCommInfo(findTestDataCommInfo(uId));
//  }

}
