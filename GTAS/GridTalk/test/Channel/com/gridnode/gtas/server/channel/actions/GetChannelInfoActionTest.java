/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.GetChannelInfoEvent;
import com.gridnode.gtas.server.channel.actions.GetChannelInfoAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This Test class handles the test for deletion of a new ChannelInfo.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
public class GetChannelInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);

  // static value for creating commInfo 1.
  protected final String COMM_HOST_1 = "HOST 1";
  protected final String COMM_IMPL_VER_1 = "TPTVE1";
  protected final int COMM_PORT_1 = 123;
  protected final String COMM_PROTOCOL_TYPE_1 = CommInfo.JMS;
  protected Hashtable COMM_PROTOCOL_DETAIL_1 = new Hashtable();
  protected final String COMM_PROTOCOL_VERSION_1 = "PROVE1";
  protected final String COMM_REF_ID_1 = "REF_ID 1";
  protected final boolean COMM_IS_DEFAULT_TPT_1 = false;
  protected final String COMM_NAME_1 = "NAME 1";
  protected final String COMM_DESCRIPTION_1 = "DESCRIPTION 1";

  // static value for creating channel info 1.
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String PROTOCOL_TYPE_1 = CommInfo.JMS;
  protected final String REF_ID_1 = "Ref Id 1";

  // static value for creating channel info 2.
  protected final Long INVALID_UID = new Long(-1111111111111111l);

  // value in the database
  CommInfo _commInfo1 = null;
  Long _commInfoUId1 = null;

  ChannelInfo _channelInfo1 = null;
  Long _channelInfoUId1 = null;

  public GetChannelInfoActionTest(String name)
  {
    super(name, "GetChannelInfoActionTest", IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(GetChannelInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(NAME_1);
    deleteTestDataCommInfo(COMM_NAME_1);
    COMM_PROTOCOL_DETAIL_1.clear();
  }

  protected void setupData() throws Exception
  {
    COMM_PROTOCOL_DETAIL_1.put("TEST", "test value");
    _commInfo1 = createTestDataCommInfo(COMM_HOST_1, COMM_IMPL_VER_1, COMM_PORT_1,
             COMM_PROTOCOL_TYPE_1, COMM_PROTOCOL_DETAIL_1, COMM_PROTOCOL_VERSION_1, COMM_REF_ID_1,
             COMM_IS_DEFAULT_TPT_1, COMM_NAME_1, COMM_DESCRIPTION_1);
    _commInfoUId1 = addTestDataCommInfo(_commInfo1);
    _commInfo1 = findTestDataCommInfo(_commInfoUId1);

    _channelInfo1 = createTestDataChannelInfo(DESCRIPTION_1, _commInfo1, REF_ID_1, PROTOCOL_TYPE_1, NAME_1);
    _channelInfoUId1 = addTestDataChannelInfo(_channelInfo1);
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);

  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetChannelInfoAction action = new GetChannelInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private GetChannelInfoEvent getChannelInfoEvent(Long uId)
    throws Exception
  {
    return new GetChannelInfoEvent(uId);
  }

  public void testPerform() throws Exception
  {
    GetChannelInfoEvent event = null;

    // Creating with null event.
    checkTestPerformFail(null, true);

    // non-existing
    event = getChannelInfoEvent(INVALID_UID);
    checkTestPerformFail(event, false);

    // existing.
    event = getChannelInfoEvent(_channelInfoUId1);
    checkTestPerformSuccess(event, _channelInfo1);

  }

  protected void checkTestPerformSuccess(IEvent event, ChannelInfo deletingChannel) throws Exception
  {
    Map returnData = (Map) super.checkTestPerformSuccess(event);
    checkReturnData(returnData, deletingChannel);
  }

}
