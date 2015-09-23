/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateChannelInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.UpdateChannelInfoEvent;
import com.gridnode.gtas.server.channel.actions.UpdateChannelInfoAction;
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
 * This Test class handles the test for update of a ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateChannelInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  // static value for creating commInfo 1.
  protected final String COMM_HOST_1 = "HOST 1";
  protected final String COMM_IMPL_VER_1 = "TPTVE1";
  protected final int COMM_PORT_1 = 123;
  protected final String COMM_PROTOCOL_TYPE_1 = UpdateChannelInfoEvent.JMS;
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
  protected final String COMM_PROTOCOL_TYPE_2 = UpdateChannelInfoEvent.JMS;
  protected Hashtable COMM_PROTOCOL_DETAIL_2 = new Hashtable();
  protected final String COMM_PROTOCOL_VERSION_2 = "PROVE2";
  protected final String COMM_REF_ID_2 = "REF_ID 2";
  protected final boolean COMM_IS_DEFAULT_TPT_2 = false;
  protected final String COMM_NAME_2 = "NAME 2";
  protected final String COMM_DESCRIPTION_2 = "DESCRIPTION 2";

  // static value for creating commInfo 3.
  protected final String COMM_HOST_3 = "HOST 3";
  protected final String COMM_IMPL_VER_3 = "TPTVE3";
  protected final int COMM_PORT_3 = 233;
  protected final String COMM_PROTOCOL_TYPE_3 = UpdateChannelInfoEvent.JMS;
  protected Hashtable COMM_PROTOCOL_DETAIL_3 = new Hashtable();
  protected final String COMM_PROTOCOL_VERSION_3 = "PROVE3";
  protected final String COMM_REF_ID_3 = "REF_ID 3";
  protected final boolean COMM_IS_DEFAULT_TPT_3 = false;
  protected final String COMM_NAME_3 = "NAME 3";
  protected final String COMM_DESCRIPTION_3 = "DESCRIPTION 3";

  // static value for creating channel info 1.
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String PROTOCOL_TYPE_1 = UpdateChannelInfoEvent.JMS;
  protected final String REF_ID_1 = "Ref Id 1";

  // static value for creating channel info 2.
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String PROTOCOL_TYPE_2 = UpdateChannelInfoEvent.JMS;
  protected final String REF_ID_2 = "Ref Id 2";

  // static value for creating channel info 3.
  protected final String NAME_3 = "Name 3";
  protected final String DESCRIPTION_3 = "Desc 3";
  protected final String PROTOCOL_TYPE_3 = UpdateChannelInfoEvent.JMS;
  protected final String REF_ID_3 = "Ref Id 3";
  //Invalid value
  protected final String INVALID_PROTOCOL_TYPE = "abc";

  // value in the database
  CommInfo _commInfo1 = null;
  CommInfo _commInfo2 = null;
  CommInfo _invalidCommInfo = null;
  Long _commInfoUId1 = null;
  Long _commInfoUId2 = null;

  ChannelInfo _channelInfo1 = null;
  ChannelInfo _channelInfo2 = null;
  Long _channelInfoUId1 = null;
  Long _channelInfoUId2 = null;

  public UpdateChannelInfoActionTest(String name)
  {
    super(name, "UpdateChannelInfoActionTest", IErrorCode.UPDATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(UpdateChannelInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(NAME_1);
    deleteTestDataChannelInfo(NAME_2);
    deleteTestDataChannelInfo(NAME_3);

    deleteTestDataCommInfo(COMM_NAME_1);
    deleteTestDataCommInfo(COMM_NAME_2);
    deleteTestDataCommInfo(COMM_NAME_3);

    COMM_PROTOCOL_DETAIL_1.clear();
    COMM_PROTOCOL_DETAIL_2.clear();
    COMM_PROTOCOL_DETAIL_3.clear();
  }

  protected void setupData() throws Exception
  {
    COMM_PROTOCOL_DETAIL_1.put("TEST", "test value");
    COMM_PROTOCOL_DETAIL_2.put("TEST 2", "test value 2");
    COMM_PROTOCOL_DETAIL_3.put("TEST 3", "test value 3");

    _commInfo1 = createTestDataCommInfo(COMM_HOST_1, COMM_IMPL_VER_1, COMM_PORT_1,
             COMM_PROTOCOL_TYPE_1, COMM_PROTOCOL_DETAIL_1, COMM_PROTOCOL_VERSION_1, COMM_REF_ID_1,
             COMM_IS_DEFAULT_TPT_1, COMM_NAME_1, COMM_DESCRIPTION_1);
    _commInfoUId1 = addTestDataCommInfo(_commInfo1);
    _commInfo1 = findTestDataCommInfo(_commInfoUId1);

    _commInfo2 = createTestDataCommInfo(COMM_HOST_2, COMM_IMPL_VER_2, COMM_PORT_2,
             COMM_PROTOCOL_TYPE_2, COMM_PROTOCOL_DETAIL_2, COMM_PROTOCOL_VERSION_2, COMM_REF_ID_2,
             COMM_IS_DEFAULT_TPT_2, COMM_NAME_2, COMM_DESCRIPTION_2);
    _commInfoUId2 = addTestDataCommInfo(_commInfo2);
    _commInfo2 = findTestDataCommInfo(_commInfoUId2);

    _invalidCommInfo = createTestDataCommInfo(COMM_HOST_2, COMM_IMPL_VER_2, COMM_PORT_2,
             COMM_PROTOCOL_TYPE_2, COMM_PROTOCOL_DETAIL_2, COMM_PROTOCOL_VERSION_2, COMM_REF_ID_2,
             COMM_IS_DEFAULT_TPT_2, COMM_NAME_2, COMM_DESCRIPTION_2);
    _invalidCommInfo.setUId(99999999999l);

    _channelInfo1 = createTestDataChannelInfo(DESCRIPTION_1, _commInfo1, REF_ID_1, PROTOCOL_TYPE_1, NAME_1);
    _channelInfoUId1 = addTestDataChannelInfo(_channelInfo1);
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);

    _channelInfo2 = createTestDataChannelInfo(DESCRIPTION_2, _commInfo2, REF_ID_2, PROTOCOL_TYPE_2, NAME_2);
    _channelInfoUId2 = addTestDataChannelInfo(_channelInfo2);
    _channelInfo2 = findTestDataChannelInfo(_channelInfoUId2);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    UpdateChannelInfoAction action = new UpdateChannelInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private UpdateChannelInfoEvent updateChannelInfoEvent(ChannelInfo info)
    throws Exception
  {
    return new UpdateChannelInfoEvent(new Long(info.getUId()), info.getName(), info.getDescription(), 
            info.getTptProtocolType(), new Long(info.getTptCommInfo().getUId()),
            new Long(info.getPackagingProfile().getUId()), new Long(info.getSecurityProfile().getUId()),
            new HashMap());
  }

  public void testPerform() throws Exception
  {
    UpdateChannelInfoEvent event = null;

    // Creating with null event.
    checkTestPerformFail(null, true);

    // non-existing
    ChannelInfo c1 = createTestDataChannelInfo(DESCRIPTION_3, _commInfo1, REF_ID_3, PROTOCOL_TYPE_3, NAME_3);
    event = updateChannelInfoEvent(c1);
    checkTestPerformFail(event, true);

    // invalid commInfoUId.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setTptCommInfo(_invalidCommInfo);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformFail(event, true);

    // duplicate commInfoUId.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setTptCommInfo(_commInfo2);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // null name.
    try
    {
      _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
      _channelInfo1.setName(null);
      event = updateChannelInfoEvent(_channelInfo1);
      assertTrue("Null Name", false);
    }
    catch (EventException e)
    {
    }

    // duplicate name.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setName(NAME_2);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformFail(event, false);

    // new name.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setName(NAME_3);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // null description.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setDescription(null);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // duplicate description.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setDescription(DESCRIPTION_2);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // null refId.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setReferenceId(null);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // duplicate refId.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setReferenceId(REF_ID_2);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformSuccess(event, _channelInfo1);

    // null protocol type.
    try
    {
      _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
      _channelInfo1.setTptProtocolType(null);
      event = updateChannelInfoEvent(_channelInfo1);
      assertTrue("Null Protocol Type", false);
    }
    catch (EventException ex)
    {
    }

    // invalid protocol type.
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
    _channelInfo1.setTptProtocolType(INVALID_PROTOCOL_TYPE);
    event = updateChannelInfoEvent(_channelInfo1);
    checkTestPerformFail(event, true);
  }

  protected void checkTestPerformSuccess(IEvent event, ChannelInfo expected) throws Exception
  {
    Object returnData = super.checkTestPerformSuccess(event);
    checkWithDB(expected);
  }

}
