/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteCommInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.DeleteCommInfoEvent;
import com.gridnode.gtas.server.channel.actions.DeleteCommInfoAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
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
public class DeleteCommInfoActionTest
  extends AbstractChannelActionTest
{
  protected StateMachine _sm = new StateMachine(null, null);
  // Value used to create test data.
  protected final String HOST_1 = "Host 1";
  protected final int PORT_1 = 11;
  protected final String REF_ID_1 = "RefId 1";
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String DESTINATION_1 = "Dest 1";
  protected final String PASSWORD_1 = "PASS 1";
  protected final String USER_1 = "USER 1";
  protected final String IMPL_VERSION_1 = "IMP1";
  protected final String PROTOCOL_TYPE_1 = "TYPE1";
  protected final Hashtable DETAIL_1 = new Hashtable();
  protected final String PROTOCOL_VERSION_1 = "VER1";
  protected final boolean IS_DEFAULT_TPT_1 = true;

  // Value used to create test data.
  protected final String HOST_2 = "Host 2";
  protected final int PORT_2 = 22;
  protected final String REF_ID_2 = "RefId 2";
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String DESTINATION_2 = "Dest 2";
  protected final String PASSWORD_2 = "PASS 2";
  protected final String USER_2 = "USER 2";
  protected final String IMPL_VERSION_2 = "IMP2";
  protected final String PROTOCOL_TYPE_2 = "TYPE2";
  protected final Hashtable DETAIL_2 = new Hashtable();
  protected final String PROTOCOL_VERSION_2 = "VER2";
  protected final boolean IS_DEFAULT_TPT_2 = false;

  // Value used to create non- exisitng data.
  protected final Long INVALID_UID = new Long(111111111111111l);

  // database record.
  protected CommInfo _jms1 = null;
  protected CommInfo _jms2 = null;
  protected Long _jmsUId1 = null;
  protected Long _jmsUId2 = null;
  ChannelInfo _channelInfo1 = null;
  Long _channelInfoUId1 = null;

  // static value for creating channel info 1.
  protected final String CHANNEL_NAME_1 = "Name 1";
  protected final String CHANNEL_DESCRIPTION_1 = "Desc 1";
  protected final String CHANNEL_PROTOCOL_TYPE_1 = "Type 1";
  protected final String CHANNEL_REF_ID_1 = "Ref Id 1";


  public DeleteCommInfoActionTest(String name)
  {
    super(name, "DeleteCommInfoActionTest", IErrorCode.DELETE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(DeleteCommInfoActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(CHANNEL_NAME_1);
    deleteTestDataCommInfo(NAME_1);
    deleteTestDataCommInfo(NAME_2);
  }

  protected void setupData() throws Exception
  {
    DETAIL_1.put("Test 1", "Test value 1");
    DETAIL_2.put("Test 2", "Test value 2");

    // create new data.
    _jms1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1, PROTOCOL_TYPE_1,
                DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1, NAME_1, DESCRIPTION_1);
    _jmsUId1 = addTestDataCommInfo(_jms1);
    _jms1 = findTestDataCommInfo(_jmsUId1);
    _jms2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2, PROTOCOL_TYPE_2,
                DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2, NAME_2, DESCRIPTION_2);
    _jmsUId2 = addTestDataCommInfo(_jms2);
    _jms2 = findTestDataCommInfo(_jmsUId2);

    _channelInfo1 = createTestDataChannelInfo(CHANNEL_DESCRIPTION_1, _jms1,
                  CHANNEL_REF_ID_1, CHANNEL_PROTOCOL_TYPE_1, CHANNEL_NAME_1);
    _channelInfoUId1 = addTestDataChannelInfo(_channelInfo1);
    _channelInfo1 = findTestDataChannelInfo(_channelInfoUId1);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    DeleteCommInfoAction action = new DeleteCommInfoAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private DeleteCommInfoEvent deleteCommInfoEvent(Long uId)
    throws Exception
  {
    return new DeleteCommInfoEvent(uId);
  }

  public void testPerform() throws Exception
  {
    DeleteCommInfoEvent event = null;

    // Null Event
    checkTestPerformFail(null, true);

    {
      // non-existing.
      event = deleteCommInfoEvent(INVALID_UID);
      checkTestPerformFail(event, true);
    }

    {
      // dependency channel.
      event = deleteCommInfoEvent(_jmsUId1);
      checkTestPerformFail(event, false);
    }

    {
      // delete non-dependency channel.
      event = deleteCommInfoEvent(_jmsUId2);
      checkTestPerformSuccess(event, _jmsUId2);
    }

  }

  protected void checkTestPerformSuccess(IEvent event, Long deletingUId) throws Exception
  {
    Object returnData = super.checkTestPerformSuccess(event);
    try
    {
      findTestDataCommInfo(deletingUId);
      assertTrue("Able to find data!", false);
    }
    catch (Exception ex)
    {
    }
  }

}
