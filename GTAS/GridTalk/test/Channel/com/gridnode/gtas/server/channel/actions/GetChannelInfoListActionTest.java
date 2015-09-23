/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelInfoListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2002    Goh Kan Mun         Created
 */
package com.gridnode.gtas.server.channel.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.channel.GetChannelInfoListEvent;
import com.gridnode.gtas.server.channel.actions.GetChannelInfoListAction;
import com.gridnode.gtas.server.channel.helpers.ChannelLogger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import java.util.Collection;
import java.util.Hashtable;
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
public class GetChannelInfoListActionTest
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

  protected final String PROTOCOL_TYPE = CommInfo.JMS;
  // static value for creating channel info 1.
  protected final String NAME_1 = "Name 1";
  protected final String DESCRIPTION_1 = "Desc 1";
  protected final String REF_ID_1 = "Ref Id 1";

  // static value for creating channel info 2.
  protected final String NAME_2 = "Name 2";
  protected final String DESCRIPTION_2 = "Desc 2";
  protected final String REF_ID_2 = "Ref Id 2";

  protected final String NAME_3 = "Name 3";
  protected final String NAME_4 = "Name 4";
  protected final String NAME_5 = "Name 5";
  protected final String NAME_6 = "Name 6";
  protected final String NON_EXISTING_NAME = "Non Existing Name";

  public GetChannelInfoListActionTest(String name)
  {
    super(name, "GetChannelInfoListActionTest", IErrorCode.CREATE_ENTITY_ERROR);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new TestSuite(GetChannelInfoListActionTest.class));
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(NAME_1);
    deleteTestDataChannelInfo(NAME_2);
    deleteTestDataChannelInfo(NAME_3);
    deleteTestDataChannelInfo(NAME_4);
    deleteTestDataChannelInfo(NAME_5);
    deleteTestDataChannelInfo(NAME_6);
    deleteTestDataCommInfo(COMM_NAME_1);
    COMM_PROTOCOL_DETAIL_1 = new Hashtable();
  }

  protected void setupData() throws Exception
  {
    COMM_PROTOCOL_DETAIL_1.put("TEST", "test value");
    CommInfo commInfo1 = createTestDataCommInfo(COMM_HOST_1, COMM_IMPL_VER_1, COMM_PORT_1,
             COMM_PROTOCOL_TYPE_1, COMM_PROTOCOL_DETAIL_1, COMM_PROTOCOL_VERSION_1, COMM_REF_ID_1,
             COMM_IS_DEFAULT_TPT_1, COMM_NAME_1, COMM_DESCRIPTION_1);
    Long commInfoUId1 = addTestDataCommInfo(commInfo1);
    commInfo1 = findTestDataCommInfo(commInfoUId1);

    ChannelInfo c1 = createTestDataChannelInfo(DESCRIPTION_1, commInfo1, REF_ID_1, PROTOCOL_TYPE, NAME_1);
    addTestDataChannelInfo(c1);

    ChannelInfo c2 = createTestDataChannelInfo(DESCRIPTION_2, commInfo1, REF_ID_2, PROTOCOL_TYPE, NAME_2);
    addTestDataChannelInfo(c2);

    ChannelInfo c3 = createTestDataChannelInfo(DESCRIPTION_1, commInfo1, REF_ID_1, PROTOCOL_TYPE, NAME_3);
    addTestDataChannelInfo(c3);

    ChannelInfo c4 = createTestDataChannelInfo(DESCRIPTION_2, commInfo1, REF_ID_2, PROTOCOL_TYPE, NAME_4);
    addTestDataChannelInfo(c4);

    ChannelInfo c5 = createTestDataChannelInfo(DESCRIPTION_1, commInfo1, REF_ID_1, PROTOCOL_TYPE, NAME_5);
    addTestDataChannelInfo(c5);

    ChannelInfo c6 = createTestDataChannelInfo(DESCRIPTION_2, commInfo1, REF_ID_2, PROTOCOL_TYPE, NAME_6);
    addTestDataChannelInfo(c6);
  }

  protected BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetChannelInfoListAction action = new GetChannelInfoListAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _openSession);
    _sm.setAttribute(IAttributeKeys.USER_ID, _user);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private GetChannelInfoListEvent getChannelInfoListEvent()
    throws Exception
  {
    return new GetChannelInfoListEvent();
  }

  private GetChannelInfoListEvent getChannelInfoListEvent(IDataFilter filter)
    throws Exception
  {
    return new GetChannelInfoListEvent(filter);
  }

  private GetChannelInfoListEvent getChannelInfoListEvent(IDataFilter filter, int maxRows)
    throws Exception
  {
    return new GetChannelInfoListEvent(filter, maxRows);
  }

  private GetChannelInfoListEvent getChannelInfoListEvent(IDataFilter filter,
          int maxRows, int startRow) throws Exception
  {
    return new GetChannelInfoListEvent(filter, maxRows, startRow);
  }

  private GetChannelInfoListEvent getChannelInfoListEvent(String listID,
          int maxRows, int startRow) throws Exception
  {
    return new GetChannelInfoListEvent(listID, maxRows, startRow);
  }

  public void testPerform() throws Exception
  {
    GetChannelInfoListEvent event = null;
    Collection expected = null;
    IDataFilter filter = null;

    // Null Event
    checkTestPerformFail(null, true);

    // Retrieve all
    event = getChannelInfoListEvent();
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected);

    //Retrieve 1 (unique name)
    filter = createDataFilter(NAME_1, null, null);
    event = getChannelInfoListEvent(filter);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected);

    //Retrieve All with refId = REF_ID_2
    filter = createDataFilter(null, REF_ID_2, null);
    event = getChannelInfoListEvent(filter);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected);

    // Retrieve maximum of 3 rows.
    filter = createDataFilter(null, null, PROTOCOL_TYPE);
    event = getChannelInfoListEvent(filter, 3);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected, 3);

    // Retrieve maximum of 10 rows (only has 6 rows).
    filter = createDataFilter(null, null, PROTOCOL_TYPE);
    event = getChannelInfoListEvent(filter, 10);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected, 10);

    // Retrieve 2nd and 3rd rows
    filter = createDataFilter(null, null, PROTOCOL_TYPE);
    event = getChannelInfoListEvent(filter, 2, 1);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected, 2, 1);

    // Using the List Id, retrieve 3nd and 4th rows
    event = getChannelInfoListEvent(_listId, 4, 2);
    expected = findTestDataChannelInfo(filter);
    checkTestPerformSuccess(event, expected, 4, 2);

    // Retrieved Non-existing
    filter = createDataFilter(NON_EXISTING_NAME, null, null);
    event = getChannelInfoListEvent(filter);
    expected = new Vector();
    checkTestPerformSuccess(event, expected);
  }

  public DataFilterImpl createDataFilter(String name, String refId, String protocolType)
  {
    DataFilterImpl filter = null;
    filter = addFilter(ChannelInfo.NAME, name, filter);
    filter = addFilter(ChannelInfo.REF_ID, refId, filter);
    filter = addFilter(ChannelInfo.TPT_PROTOCOL_TYPE, protocolType, filter);
    return filter;
  }

}
