/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateChannelInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 * Jul 12 2002    Goh Kan Mun             Modified - Change of Class name from Add to Create.
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.CreateChannelInfoEvent;
import com.gridnode.gtas.server.channel.actions.CreateChannelInfoAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import java.util.HashMap;
import java.util.Hashtable;

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
public class CreateChannelInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  // static value for creating commInfo 1.
  protected final String COMM_HOST_1 = "HOST 1";
  protected final String COMM_IMPL_VER_1 = "TPTVE1";
  protected final int COMM_PORT_1 = 123;
  protected final String COMM_PROTOCOL_TYPE_1 = CreateChannelInfoEvent.JMS;
  protected Hashtable COMM_PROTOCOL_DETAIL_1 = new Hashtable();
  protected final String COMM_PROTOCOL_VERSION_1 = "PROVE1";
  protected final String COMM_REF_ID_1 = "REF_ID 1";
  protected final boolean COMM_IS_DEFAULT_TPT_1 = false;
  protected final String COMM_NAME_1 = "NAME 1";
  protected final String COMM_DESCRIPTION_1 = "DESCRIPTION 1";

  // static value for creating commInfo 2.
  protected final String COMM_HOST_2 = "HOST 2";
  protected final String COMM_IMPL_VER_2 = "TPTVE2";
  protected final int COMM_PORT_2 = 234;
  protected final String COMM_PROTOCOL_TYPE_2 = CreateChannelInfoEvent.JMS;
  protected Hashtable COMM_PROTOCOL_DETAIL_2 = new Hashtable();
  protected final String COMM_PROTOCOL_VERSION_2 = "PROVE2";
  protected final String COMM_REF_ID_2 = "REF_ID 2";
  protected final boolean COMM_IS_DEFAULT_TPT_2 = false;
  protected final String COMM_NAME_2 = "NAME 2";
  protected final String COMM_DESCRIPTION_2 = "DESCRIPTION 2";

  // static value for creating channel info 1.
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String PROTOCOL_TYPE_1 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_1 = "Ref Id 1";

  // static value for creating channel info 2.
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String PROTOCOL_TYPE_2 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_2 = "Ref Id 2";

  // static value for creating channel info 3.
  protected final String NAME_3 = "Name 3";
  protected final String DESCRIPTION_3 = "Desc 3";
  protected final String PROTOCOL_TYPE_3 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_3 = "Ref Id 3";

  // static value for creating channel info 4.
  protected final String NAME_4 = "Name 4";
  protected final String DESCRIPTION_4 = "Desc 4";
  protected final String PROTOCOL_TYPE_4 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_4 = "Ref Id 4";

  // static value for creating channel info 5.
  protected final String NAME_5 = "Name 5";
  protected final String DESCRIPTION_5 = "Desc 5";
  protected final String PROTOCOL_TYPE_5 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_5 = "Ref Id 5";

  // static value for creating channel info 6.
  protected final String NAME_6 = "Name 6";
  protected final String DESCRIPTION_6 = "Desc 6";
  protected final String PROTOCOL_TYPE_6 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_6 = "Ref Id 6";

  // static value for creating channel info 7.
  protected final String NAME_7 = "Name 7";
  protected final String DESCRIPTION_7 = "Desc 7";
  protected final String PROTOCOL_TYPE_7 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_7 = "Ref Id 7";

  // static value for creating channel info 8.
  protected final String NAME_8 = "Name 8";
  protected final String DESCRIPTION_8 = "Desc 8";
  protected final String PROTOCOL_TYPE_8 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_8 = "Ref Id 8";

  // static value for creating channel info 9.
  protected final String NAME_9 = "Name 9";
  protected final String DESCRIPTION_9 = "Desc 9";
  protected final String PROTOCOL_TYPE_9 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_9 = "Ref Id 9";

  // static value for creating channel info 10.
  protected final String NAME_10 = "Name 10";
  protected final String DESCRIPTION_10 = "Desc 10";
  protected final String PROTOCOL_TYPE_10 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_10 = "Ref Id 10";

  // static value for creating channel info 11.
  protected final String NAME_11 = "Name 11";
  protected final String DESCRIPTION_11 = "Desc 11";
  protected final String PROTOCOL_TYPE_11 = CreateChannelInfoEvent.JMS;
  protected final String REF_ID_11 = "Ref Id 11";

  public CreateChannelInfoActionTest(String name)
  {
    super(name, "CreateChannelInfoActionTest", IErrorCode.CREATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(CreateChannelInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(NAME_1);
    deleteTestDataChannelInfo(NAME_2);
    deleteTestDataChannelInfo(NAME_3);
    deleteTestDataChannelInfo(NAME_4);
    deleteTestDataChannelInfo(NAME_5);
    deleteTestDataChannelInfo(NAME_6);
    deleteTestDataChannelInfo(NAME_7);
    deleteTestDataChannelInfo(NAME_8);
    deleteTestDataChannelInfo(NAME_9);
    deleteTestDataChannelInfo(NAME_10);
    deleteTestDataChannelInfo(NAME_11);
    deleteTestDataCommInfo(COMM_NAME_1);
    deleteTestDataCommInfo(COMM_NAME_2);
    COMM_PROTOCOL_DETAIL_1 = new Hashtable();
    COMM_PROTOCOL_DETAIL_2 = new Hashtable();
  }

  protected void setupData() throws Exception
  {
    COMM_PROTOCOL_DETAIL_1.put("TEST", "test value");
    COMM_PROTOCOL_DETAIL_2.put("TEST 2", "test value 2");
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    CreateChannelInfoAction action = new CreateChannelInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private CreateChannelInfoEvent createChannelInfoEvent(ChannelInfo info)
    throws Exception
  {
    return new CreateChannelInfoEvent(info.getName(), info.getDescription(),
            info.getTptProtocolType(), new Long(info.getTptCommInfo().getUId()), 
            		new Boolean(info.isPartner()), new Long(info.getPackagingProfile().getUId()),
            		new Long(info.getSecurityProfile().getUId()),
            		new HashMap());
  }

  public void testPerform() throws Exception
  {
    CreateChannelInfoEvent event = null;
    CommInfo commInfo1 = createTestDataCommInfo(COMM_HOST_1, COMM_IMPL_VER_1, COMM_PORT_1,
             COMM_PROTOCOL_TYPE_1, COMM_PROTOCOL_DETAIL_1, COMM_PROTOCOL_VERSION_1, COMM_REF_ID_1,
             COMM_IS_DEFAULT_TPT_1, COMM_NAME_1, COMM_DESCRIPTION_1);
    Long commInfoUId1 = addTestDataCommInfo(commInfo1);
    commInfo1 = findTestDataCommInfo(commInfoUId1);

    CommInfo commInfo2 = createTestDataCommInfo(COMM_HOST_2, COMM_IMPL_VER_2, COMM_PORT_2,
             COMM_PROTOCOL_TYPE_2, COMM_PROTOCOL_DETAIL_2, COMM_PROTOCOL_VERSION_2, COMM_REF_ID_2,
             COMM_IS_DEFAULT_TPT_2, COMM_NAME_2, COMM_DESCRIPTION_2);
    Long commInfoUId2 = addTestDataCommInfo(commInfo2);
    commInfo2 = findTestDataCommInfo(commInfoUId2);

    CommInfo invalidCommInfo = createTestDataCommInfo(COMM_HOST_2, COMM_IMPL_VER_2, COMM_PORT_2,
             COMM_PROTOCOL_TYPE_2, COMM_PROTOCOL_DETAIL_2, COMM_PROTOCOL_VERSION_2, COMM_REF_ID_2,
             COMM_IS_DEFAULT_TPT_2, COMM_NAME_2, COMM_DESCRIPTION_2);
    invalidCommInfo.setUId(99999999999l);
    // Creating with null event.
    checkTestPerformFail(null, true);

    // Creating new.
    ChannelInfo c1 = createTestDataChannelInfo(DESCRIPTION_1, commInfo1, REF_ID_1, PROTOCOL_TYPE_1, NAME_1);
    event = createChannelInfoEvent(c1);
    checkTestPerformSuccess(event, c1);

    // Creating with invalid commInfoUId.
    ChannelInfo c2 = createTestDataChannelInfo(DESCRIPTION_2, invalidCommInfo, REF_ID_2, PROTOCOL_TYPE_2, NAME_2);
    event = createChannelInfoEvent(c2);
    checkTestPerformFail(event, true);

    // Creating with duplicate commInfoUId.
    ChannelInfo c3 = createTestDataChannelInfo(DESCRIPTION_3, commInfo1, REF_ID_3, PROTOCOL_TYPE_3, NAME_3);
    event = createChannelInfoEvent(c3);
    checkTestPerformSuccess(event);

    // Creating with new commInfoUId.
    ChannelInfo c4 = createTestDataChannelInfo(DESCRIPTION_4, commInfo2, REF_ID_4, PROTOCOL_TYPE_4, NAME_4);
    event = createChannelInfoEvent(c4);
    checkTestPerformSuccess(event);

    // Creating with null name.
    try
    {
      ChannelInfo c4a = createTestDataChannelInfo(DESCRIPTION_5, commInfo1, REF_ID_5, PROTOCOL_TYPE_5, null);
      event = createChannelInfoEvent(c4a);
      assertTrue("Null Name", false);
    }
    catch (EventException e)
    {

    }

    // Creating with duplicate name.
    ChannelInfo c5 = createTestDataChannelInfo(DESCRIPTION_5, commInfo1, REF_ID_5, PROTOCOL_TYPE_5, NAME_1);
    event = createChannelInfoEvent(c5);
    checkTestPerformFail(event, false);

    // Creating with null description.
    ChannelInfo c6 = createTestDataChannelInfo(null, commInfo1, REF_ID_6, PROTOCOL_TYPE_6, NAME_6);
    event = createChannelInfoEvent(c6);
    checkTestPerformSuccess(event);

    // Creating with duplicate description.
    ChannelInfo c7 = createTestDataChannelInfo(DESCRIPTION_1, commInfo1, REF_ID_7, PROTOCOL_TYPE_7, NAME_7);
    event = createChannelInfoEvent(c7);
    checkTestPerformSuccess(event);

    // Creating with null refId.
    ChannelInfo c8 = createTestDataChannelInfo(DESCRIPTION_8, commInfo1, null, PROTOCOL_TYPE_8, NAME_8);
    event = createChannelInfoEvent(c8);
    checkTestPerformSuccess(event);

    // Creating with duplicate refId.
    ChannelInfo c9 = createTestDataChannelInfo(DESCRIPTION_9, commInfo1, REF_ID_9, PROTOCOL_TYPE_9, NAME_9);
    event = createChannelInfoEvent(c9);
    checkTestPerformSuccess(event);

    // Creating with null protocol type.
    try
    {
      ChannelInfo c9a = createTestDataChannelInfo(DESCRIPTION_10, commInfo1, REF_ID_10, null, NAME_10);
      event = createChannelInfoEvent(c9a);
      assertTrue("Null Protocol Type", false);
    }
    catch (EventException ex)
    {
    }

    // Creating with invalid protocol type.
    ChannelInfo c9b = createTestDataChannelInfo(DESCRIPTION_11, commInfo1, REF_ID_11, "adsad", NAME_11);
    event = createChannelInfoEvent(c9b);
    checkTestPerformFail(event, true);
  }

  protected void checkTestPerformSuccess(IEvent event, ChannelInfo expected) throws Exception
  {
    Object returnData = super.checkTestPerformSuccess(event);
    checkWithDB(expected);
  }

}
