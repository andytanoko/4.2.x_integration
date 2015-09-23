/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityForPartnerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.gtas.events.enterprise.SetBizEntityForPartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;

import com.gridnode.pdip.app.channel.model.ChannelInfo;

import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This Test case tests the SetBizEntityForPartnerAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SetBizEntityForPartnerActionTest extends ActionTestHelper
{
  SetBizEntityForPartnerEvent[] _events;

  public SetBizEntityForPartnerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SetBizEntityForPartnerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpPartnerLinks();
    cleanUpBeChannelLinks();
    cleanUpBEs(null);
    cleanUpPartners();
    cleanUpChannels();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(2, 2);
    Long[] beUIDs = getUIDs(_bizEntities);
    createPartners(2);
    Long[] pnUIDs = getUIDs(_partners);

    Long[] certUIDs = new Long[] {null, null} ;

    createChannels(2, certUIDs);
    Long[] cnUIDs = getUIDs(_channels);

    ArrayList channelUIDs = new ArrayList();
    channelUIDs.add(cnUIDs[0]);
    this.assignChannelsToBe(channelUIDs , beUIDs[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new SetBizEntityForPartnerEvent[]
              {
                //1. rejected: invalid partnerUID
                createTestEvent(DUMMY_UID, beUIDs[0], cnUIDs[0]),
                //2. rejected: invalid beUID
                createTestEvent(pnUIDs[0], DUMMY_UID, cnUIDs[0]),
                //3. rejected: invalid channelUID
                createTestEvent(pnUIDs[0], beUIDs[0], DUMMY_UID),
                //4. rejected: channel not assigned to be
                createTestEvent(pnUIDs[0], beUIDs[1], cnUIDs[0]),
                //5. accepted: new assignment
                createTestEvent(pnUIDs[1], beUIDs[0], cnUIDs[0]),
                //6. accepted: same assignment
                createTestEvent(pnUIDs[1], beUIDs[0], cnUIDs[0]),
                //7. accepted: remove assignment
                createTestEvent(pnUIDs[1], null, null),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid partnerUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    //2. invalid beUID
    getCheckFail(_events[1], _sessions[0], _sm[0], true);

    //3. invalid channelUID
    getCheckFail(_events[2], _sessions[0], _sm[0], true);

    //4. channel not assigned to be
    getCheckFail(_events[3], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************

    //5. new assignment
    getCheckSuccess(_events[4], _sessions[0], _sm[0]);

    //6. same assignment
    getCheckSuccess(_events[5], _sessions[0], _sm[0]);

    //7. remove assignment
    getCheckSuccess(_events[6], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new SetBizEntityForPartnerAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SetBizEntityForPartnerEvent setEvent = (SetBizEntityForPartnerEvent)event;

    Long currBe = getBizEntityForPartner(setEvent.getPartnerUID());
    Long currChannel = getChannelForPartner(setEvent.getPartnerUID());

    assertEquals("AssignedChannel is incorrect", setEvent.getChannelUID(), currChannel);
    assertEquals("AssignedBe is incorrect", setEvent.getBizEntityUID(), currBe);
}

  // ******************* Own methods ****************************

  private SetBizEntityForPartnerEvent createTestEvent(
    Long partnerUID, Long beUID, Long channelUID)
    throws Exception
  {
    return new SetBizEntityForPartnerEvent(partnerUID, beUID, channelUID);
  }

  private void getCheckFail(
    SetBizEntityForPartnerEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    SetBizEntityForPartnerEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}