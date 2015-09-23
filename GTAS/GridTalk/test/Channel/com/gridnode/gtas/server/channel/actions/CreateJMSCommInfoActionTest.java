/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddJMSCommInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 10 2002    Goh Kan Mun             Created
 * Jul 12 2002    Goh Kan Mun             Modified - Change of Class name from Add to Create.
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.CreateCommInfoEvent;
import com.gridnode.gtas.server.channel.actions.CreateCommInfoAction;
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
public class CreateJMSCommInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  protected final boolean IS_DEFAULT_TPT = false;
  protected final String PROTOCOL_TYPE = JMSCommInfo.JMS;
  protected final String PROTOCOL_VERSION = JMSCommInfo.JMS_VERSION;
  protected final String IMPL_VERSION = JMSCommInfo.IMPL_VERSION;
  protected final int DEST_TYPE_1 = 1;
  protected final int DEST_TYPE_2 = 2;
  protected final int INVALID_DEST_TYPE = -1;
  protected final int INVALID_PORT = -1;

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

  protected final String HOST_3 = "Host 3";
  protected final int PORT_3 = 33;
  protected final String REF_ID_3 = "RefId 3";
  protected final String NAME_3 = "Name 3";
  protected final String DESCRIPTION_3 = "Desc 3";
  protected final String DESTINATION_3 = "Dest 3";
  protected final String PASSWORD_3 = "PASS 3";
  protected final String USER_3 = "USER 3";

  protected final String HOST_4 = "Host 4";
  protected final int PORT_4 = 44;
  protected final String REF_ID_4 = "RefId 4";
  protected final String NAME_4 = "Name 4";
  protected final String DESCRIPTION_4 = "Desc 4";
  protected final String DESTINATION_4 = "Dest 4";
  protected final String PASSWORD_4 = "PASS 4";
  protected final String USER_4 = "USER 4";

  protected final String HOST_5 = "Host 5";
  protected final int PORT_5 = 55;
  protected final String REF_ID_5 = "RefId 5";
  protected final String NAME_5 = "Name 5";
  protected final String DESCRIPTION_5 = "Desc 5";
  protected final String DESTINATION_5 = "Dest 5";
  protected final String PASSWORD_5 = "PASS 5";
  protected final String USER_5 = "USER 5";

  protected final String HOST_6 = "Host 6";
  protected final int PORT_6 = 66;
  protected final String REF_ID_6 = "RefId 6";
  protected final String NAME_6 = "Name 6";
  protected final String DESCRIPTION_6 = "Desc 6";
  protected final String DESTINATION_6 = "Dest 6";
  protected final String PASSWORD_6 = "PASS 6";
  protected final String USER_6 = "USER 6";

  protected final String HOST_7 = "Host 7";
  protected final int PORT_7 = 77;
  protected final String REF_ID_7 = "RefId 7";
  protected final String NAME_7 = "Name 7";
  protected final String DESCRIPTION_7 = "Desc 7";
  protected final String DESTINATION_7 = "Dest 7";
  protected final String PASSWORD_7 = "PASS 7";
  protected final String USER_7 = "USER 7";

  protected final String HOST_8 = "Host 8";
  protected final int PORT_8 = 88;
  protected final String REF_ID_8 = "RefId 8";
  protected final String NAME_8 = "Name 8";
  protected final String DESCRIPTION_8 = "Desc 8";
  protected final String DESTINATION_8 = "Dest 8";
  protected final String PASSWORD_8 = "PASS 8";
  protected final String USER_8 = "USER 8";

  protected final String HOST_9 = "Host 9";
  protected final int PORT_9 = 99;
  protected final String REF_ID_9 = "RefId 9";
  protected final String NAME_9 = "Name 9";
  protected final String DESCRIPTION_9 = "Desc 9";
  protected final String DESTINATION_9 = "Dest 9";
  protected final String PASSWORD_9 = "PASS 9";
  protected final String USER_9 = "USER 9";

  protected final String HOST_10 = "Host 10";
  protected final int PORT_10 = 1010;
  protected final String REF_ID_10 = "RefId 10";
  protected final String NAME_10 = "Name 10";
  protected final String DESCRIPTION_10 = "Desc 10";
  protected final String DESTINATION_10 = "Dest 10";
  protected final String PASSWORD_10 = "PASS 10";
  protected final String USER_10 = "USER 10";

  protected final String HOST_11 = "Host 11";
  protected final int PORT_11 = 1111;
  protected final String REF_ID_11 = "RefId 11";
  protected final String NAME_11 = "Name 11";
  protected final String DESCRIPTION_11 = "Desc 11";
  protected final String DESTINATION_11 = "Dest 11";
  protected final String PASSWORD_11 = "PASS 11";
  protected final String USER_11 = "USER 11";

  protected final String HOST_12 = "Host 12";
  protected final int PORT_12 = 1212;
  protected final String REF_ID_12 = "RefId 12";
  protected final String NAME_12 = "Name 12";
  protected final String DESCRIPTION_12 = "Desc 12";
  protected final String DESTINATION_12 = "Dest 12";
  protected final String PASSWORD_12 = "PASS 12";
  protected final String USER_12 = "USER 12";

  protected final String HOST_13 = "Host 13";
  protected final int PORT_13 = 1313;
  protected final String REF_ID_13 = "RefId 13";
  protected final String NAME_13 = "Name 13";
  protected final String DESCRIPTION_13 = "Desc 13";
  protected final String DESTINATION_13 = "Dest 13";
  protected final String PASSWORD_13 = "PASS 13";
  protected final String USER_13 = "USER 13";

  protected final String HOST_14 = "Host 14";
  protected final int PORT_14 = 1414;
  protected final String REF_ID_14 = "RefId 14";
  protected final String NAME_14 = "Name 14";
  protected final String DESCRIPTION_14 = "Desc 14";
  protected final String DESTINATION_14 = "Dest 14";
  protected final String PASSWORD_14 = "PASS 14";
  protected final String USER_14 = "USER 14";

  protected final String HOST_15 = "Host 15";
  protected final int PORT_15 = 1515;
  protected final String REF_ID_15 = "RefId 15";
  protected final String NAME_15 = "Name 15";
  protected final String DESCRIPTION_15 = "Desc 15";
  protected final String DESTINATION_15 = "Dest 15";
  protected final String PASSWORD_15 = "PASS 15";
  protected final String USER_15 = "USER 15";

  protected final String HOST_16 = "Host 16";
  protected final int PORT_16 = 1616;
  protected final String REF_ID_16 = "RefId 16";
  protected final String NAME_16 = "Name 16";
  protected final String DESCRIPTION_16 = "Desc 16";
  protected final String DESTINATION_16 = "Dest 16";
  protected final String PASSWORD_16 = "PASS 16";
  protected final String USER_16 = "USER 16";

  protected final String HOST_17 = "Host 17";
  protected final int PORT_17 = 1717;
  protected final String REF_ID_17 = "RefId 17";
  protected final String NAME_17 = "Name 17";
  protected final String DESCRIPTION_17 = "Desc 17";
  protected final String DESTINATION_17 = "Dest 17";
  protected final String PASSWORD_17 = "PASS 17";
  protected final String USER_17 = "USER 17";

  protected final String HOST_18 = "Host 18";
  protected final int PORT_18 = 1818;
  protected final String REF_ID_18 = "RefId 18";
  protected final String NAME_18 = "Name 18";
  protected final String DESCRIPTION_18 = "Desc 18";
  protected final String DESTINATION_18 = "Dest 18";
  protected final String PASSWORD_18 = "PASS 18";
  protected final String USER_18 = "USER 18";

  protected final String HOST_19 = "Host 19";
  protected final int PORT_19 = 1919;
  protected final String REF_ID_19 = "RefId 19";
  protected final String NAME_19 = "Name 19";
  protected final String DESCRIPTION_19 = "Desc 19";
  protected final String DESTINATION_19 = "Dest 19";
  protected final String PASSWORD_19 = "PASS 19";
  protected final String USER_19 = "USER 19";

  public CreateJMSCommInfoActionTest(String name)
  {
    super(name, "CreateJMSCommInfoActionTest", IErrorCode.CREATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(CreateJMSCommInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataCommInfo(NAME_1);
    deleteTestDataCommInfo(NAME_2);
    deleteTestDataCommInfo(NAME_3);
    deleteTestDataCommInfo(NAME_4);
    deleteTestDataCommInfo(NAME_5);
    deleteTestDataCommInfo(NAME_6);
    deleteTestDataCommInfo(NAME_7);
    deleteTestDataCommInfo(NAME_8);
    deleteTestDataCommInfo(NAME_9);
    deleteTestDataCommInfo(NAME_10);
    deleteTestDataCommInfo(NAME_11);
    deleteTestDataCommInfo(NAME_12);
    deleteTestDataCommInfo(NAME_13);
    deleteTestDataCommInfo(NAME_14);
    deleteTestDataCommInfo(NAME_15);
    deleteTestDataCommInfo(NAME_16);
    deleteTestDataCommInfo(NAME_17);
    deleteTestDataCommInfo(NAME_18);
    deleteTestDataCommInfo(NAME_19);
  }

  protected void setupData() throws Exception
  {
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    CreateCommInfoAction action = new CreateCommInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private CreateCommInfoEvent createJMSCommInfoEvent(CommInfo info,
          String destination, int destType, String password, String user)
    throws Exception
  {
    return new CreateCommInfoEvent(info.getName(), info.getDescription(), 
           info.getURL(), info.getProtocolType(), new Boolean(info.isPartner()));
  }

  public void testPerform() throws Exception
  {
    CreateCommInfoEvent event = null;

    // Null Event
    checkTestPerformFail(null, true);

//    {
//      // create new data.
//      JMSCommInfo jms1 = createTestDataCommInfo(HOST_1, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_1, DESCRIPTION_1, DESTINATION_1,
//                  DEST_TYPE_1, PASSWORD_1, USER_1);
//      event = createJMSCommInfoEvent(jms1, DESTINATION_1, DEST_TYPE_1, PASSWORD_1, USER_1);
//      checkTestPerformSuccess(event, jms1);
//    }
//    {
//      // invalid destType.
//      JMSCommInfo jms2 = createTestDataCommInfo(HOST_2, IMPL_VERSION, PORT_2, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_2, IS_DEFAULT_TPT, NAME_2, DESCRIPTION_2, DESTINATION_2,
//                  DEST_TYPE_1, PASSWORD_2, USER_2);
//      event = createJMSCommInfoEvent(jms2, DESTINATION_2, INVALID_DEST_TYPE, PASSWORD_2, USER_2);
//      checkTestPerformFail(event, true);
//    }
//    {
//      // duplicate destType.
//      JMSCommInfo jms3 = createTestDataCommInfo(HOST_3, IMPL_VERSION, PORT_3, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_3, IS_DEFAULT_TPT, NAME_3, DESCRIPTION_3, DESTINATION_3,
//                  DEST_TYPE_1, PASSWORD_3, USER_3);
//      event = createJMSCommInfoEvent(jms3, DESTINATION_3, DEST_TYPE_1, PASSWORD_3, USER_3);
//      checkTestPerformSuccess(event, jms3);
//    }
//
//    try
//    {
//      // null host.
//      JMSCommInfo jms4 = createTestDataCommInfo(null, IMPL_VERSION, PORT_4, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_4, IS_DEFAULT_TPT, NAME_4, DESCRIPTION_4, DESTINATION_4,
//                  DEST_TYPE_1, PASSWORD_4, USER_4);
//      event = createJMSCommInfoEvent(jms4, DESTINATION_4, DEST_TYPE_1, PASSWORD_4, USER_4);
//      assertTrue("Host is null", false);
//    }
//    catch (EventException ex)
//    {
//    }
//
//    {
//      // duplicate host.
//      JMSCommInfo jms5 = createTestDataCommInfo(HOST_1, IMPL_VERSION, PORT_5, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_5, IS_DEFAULT_TPT, NAME_5, DESCRIPTION_5, DESTINATION_5,
//                  DEST_TYPE_1, PASSWORD_5, USER_5);
//      event = createJMSCommInfoEvent(jms5, DESTINATION_5, DEST_TYPE_1, PASSWORD_5, USER_5);
//      checkTestPerformSuccess(event, jms5);
//    }
//    {
//      // invalid port.
//      JMSCommInfo jms6 = createTestDataCommInfo(HOST_6, IMPL_VERSION, INVALID_PORT, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_6, IS_DEFAULT_TPT, NAME_6, DESCRIPTION_6, DESTINATION_6,
//                  DEST_TYPE_1, PASSWORD_6, USER_6);
//      event = createJMSCommInfoEvent(jms6, DESTINATION_6, DEST_TYPE_1, PASSWORD_6, USER_6);
//      checkTestPerformSuccess(event, jms6);
//    }
//    {
//      // duplicate port.
//      JMSCommInfo jms7 = createTestDataCommInfo(HOST_7, IMPL_VERSION, PORT_1, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_7, IS_DEFAULT_TPT, NAME_7, DESCRIPTION_7, DESTINATION_7,
//                  DEST_TYPE_1, PASSWORD_7, USER_7);
//      event = createJMSCommInfoEvent(jms7, DESTINATION_7, DEST_TYPE_1, PASSWORD_7, USER_7);
//      checkTestPerformSuccess(event, jms7);
//    }
//    {
//      // null reference id.
//      JMSCommInfo jms8 = createTestDataCommInfo(HOST_8, IMPL_VERSION, PORT_8, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, null, IS_DEFAULT_TPT, NAME_8, DESCRIPTION_8, DESTINATION_8,
//                  DEST_TYPE_1, PASSWORD_8, USER_8);
//      event = createJMSCommInfoEvent(jms8, DESTINATION_8, DEST_TYPE_1, PASSWORD_8, USER_8);
//      checkTestPerformSuccess(event, jms8);
//    }
//    {
//      // duplicate reference Id.
//      JMSCommInfo jms9 = createTestDataCommInfo(HOST_9, IMPL_VERSION, PORT_9, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_1, IS_DEFAULT_TPT, NAME_9, DESCRIPTION_9, DESTINATION_9,
//                  DEST_TYPE_1, PASSWORD_9, USER_9);
//      event = createJMSCommInfoEvent(jms9, DESTINATION_9, DEST_TYPE_1, PASSWORD_9, USER_9);
//      checkTestPerformSuccess(event, jms9);
//    }
//
//    try
//    {
//      // null name.
//      JMSCommInfo jms10 = createTestDataCommInfo(HOST_10, IMPL_VERSION, PORT_10, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_10, IS_DEFAULT_TPT, null, DESCRIPTION_10, DESTINATION_10,
//                  DEST_TYPE_1, PASSWORD_10, USER_10);
//      event = createJMSCommInfoEvent(jms10, DESTINATION_10, DEST_TYPE_1, PASSWORD_10, USER_10);
//      assertTrue("Name is null", false);
//    }
//    catch (EventException ex)
//    {
//    }
//
//    {
//      // duplicate name.
//      JMSCommInfo jms11 = createTestDataCommInfo(HOST_11, IMPL_VERSION, PORT_11, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_11, IS_DEFAULT_TPT, NAME_1, DESCRIPTION_11, DESTINATION_11,
//                  DEST_TYPE_1, PASSWORD_11, USER_11);
//      event = createJMSCommInfoEvent(jms11, DESTINATION_11, DEST_TYPE_1, PASSWORD_11, USER_11);
//      checkTestPerformFail(event, false);
//    }
//
//    {
//      // null description
//      JMSCommInfo jms12 = createTestDataCommInfo(HOST_12, IMPL_VERSION, PORT_12, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_12, IS_DEFAULT_TPT, NAME_12, null, DESTINATION_12,
//                  DEST_TYPE_1, PASSWORD_12, USER_12);
//      event = createJMSCommInfoEvent(jms12, DESTINATION_12, DEST_TYPE_1, PASSWORD_12, USER_12);
//      checkTestPerformSuccess(event, jms12);
//    }
//
//    {
//      // duplicate description
//      JMSCommInfo jms13 = createTestDataCommInfo(HOST_13, IMPL_VERSION, PORT_13, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_13, IS_DEFAULT_TPT, NAME_13, DESCRIPTION_1, DESTINATION_13,
//                  DEST_TYPE_1, PASSWORD_13, USER_13);
//      event = createJMSCommInfoEvent(jms13, DESTINATION_13, DEST_TYPE_1, PASSWORD_13, USER_13);
//      checkTestPerformSuccess(event, jms13);
//    }
//    {
//      // null destination.
//      JMSCommInfo jms14 = createTestDataCommInfo(HOST_14, IMPL_VERSION, PORT_14, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_14, IS_DEFAULT_TPT, NAME_14, DESCRIPTION_14, DESTINATION_14,
//                  DEST_TYPE_1, PASSWORD_14, USER_14);
//      event = createJMSCommInfoEvent(jms14, null, DEST_TYPE_1, PASSWORD_14, USER_14);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate destination.
//      JMSCommInfo jms15 = createTestDataCommInfo(HOST_15, IMPL_VERSION, PORT_15, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_15, IS_DEFAULT_TPT, NAME_15, DESCRIPTION_15, DESTINATION_1,
//                  DEST_TYPE_1, PASSWORD_15, USER_15);
//      event = createJMSCommInfoEvent(jms15, DESTINATION_1, DEST_TYPE_1, PASSWORD_15, USER_15);
//      checkTestPerformSuccess(event, jms15);
//    }
//
//    {
//      // null password.
//      JMSCommInfo jms16 = createTestDataCommInfo(HOST_16, IMPL_VERSION, PORT_16, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_16, IS_DEFAULT_TPT, NAME_16, DESCRIPTION_16, DESTINATION_16,
//                  DEST_TYPE_1, PASSWORD_16, USER_16);
//      event = createJMSCommInfoEvent(jms16, DESTINATION_16, DEST_TYPE_1, null, USER_16);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate password.
//      JMSCommInfo jms17 = createTestDataCommInfo(HOST_17, IMPL_VERSION, PORT_17, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_17, IS_DEFAULT_TPT, NAME_17, DESCRIPTION_17, DESTINATION_17,
//                  DEST_TYPE_1, PASSWORD_1, USER_17);
//      event = createJMSCommInfoEvent(jms17, DESTINATION_17, DEST_TYPE_1, PASSWORD_1, USER_17);
//      checkTestPerformSuccess(event, jms17);
//    }
//
//    {
//      // null user.
//      JMSCommInfo jms18 = createTestDataCommInfo(HOST_18, IMPL_VERSION, PORT_18, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_18, IS_DEFAULT_TPT, NAME_18, DESCRIPTION_18, DESTINATION_18,
//                  DEST_TYPE_1, PASSWORD_18, USER_18);
//      event = createJMSCommInfoEvent(jms18, DESTINATION_18, DEST_TYPE_1, PASSWORD_18, null);
//      checkTestPerformFail(event, true);
//    }
//
//    {
//      // duplicate user.
//      JMSCommInfo jms19 = createTestDataCommInfo(HOST_19, IMPL_VERSION, PORT_19, PROTOCOL_TYPE,
//                  PROTOCOL_VERSION, REF_ID_19, IS_DEFAULT_TPT, NAME_19, DESCRIPTION_19, DESTINATION_19,
//                  DEST_TYPE_1, PASSWORD_19, USER_1);
//      event = createJMSCommInfoEvent(jms19, DESTINATION_19, DEST_TYPE_1, PASSWORD_19, USER_1);
//      checkTestPerformSuccess(event, jms19);
//    }

  }

  protected void checkTestPerformSuccess(IEvent event, CommInfo expected) throws Exception
  {
    Object returnData = super.checkTestPerformSuccess(event);
    checkWithDB(expected);
  }

}
