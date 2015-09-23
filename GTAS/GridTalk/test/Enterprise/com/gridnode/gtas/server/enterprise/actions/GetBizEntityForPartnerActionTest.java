/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityForPartnerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.gtas.events.enterprise.GetBizEntityForPartnerEvent;
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
 * This Test case tests the GetBizEntityForPartnerAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBizEntityForPartnerActionTest extends ActionTestHelper
{
  GetBizEntityForPartnerEvent[] _events;

  public GetBizEntityForPartnerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetBizEntityForPartnerActionTest.class);
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
    createBes(1, 1);
    Long[] beUIDs = getUIDs(_bizEntities);
    createPartners(2);
    Long[] pnUIDs = getUIDs(_partners);

    Long[] certUIDs = new Long[] {null} ;
    createChannels(1, certUIDs);
    Long[] cnUIDs = getUIDs(_channels);

    ArrayList channelUIDs = new ArrayList();
    channelUIDs.add(cnUIDs[0]);
    this.assignChannelsToBe(channelUIDs , beUIDs[0]);
    this.assignBeToPartner(pnUIDs[1], beUIDs[0]);
    this.assignChannelToPartner(pnUIDs[1], cnUIDs[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetBizEntityForPartnerEvent[]
              {
                //1. rejected: invalid partnerUID
                createTestEvent(DUMMY_UID),
                //2. accepted: no assignment
                createTestEvent(pnUIDs[0]),
                //3. accepted: have assignment
                createTestEvent(pnUIDs[1]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid partnerUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************

    //2. no assignment
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);

    //3. have assignment
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetBizEntityForPartnerAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetBizEntityForPartnerEvent getEvent = (GetBizEntityForPartnerEvent)event;

    Long currBe = getBizEntityForPartner(getEvent.getPartnerUID());
    Long currChannel = getChannelForPartner(getEvent.getPartnerUID());

    Object returnData = response.getReturnData();
    assertNotNull("Return data is null", returnData);
    assertTrue("Return data is not collection", returnData instanceof Collection);

    checkReturnCollection(getEvent, (Collection)returnData);
}

  // ******************* Own methods ****************************

  private void checkReturnCollection(GetBizEntityForPartnerEvent event,
    Collection returnData)
  {
    assertEquals("return collection size incorrect", 2, returnData.size());
    Object[] elems = returnData.toArray();
    assertTrue("Map not in Be position", elems[0] instanceof Map);
    assertTrue("Map not in Channel position", elems[1] instanceof Map);

    Long expectedBeUID = getBizEntityForPartner(event.getPartnerUID());
    Long expectedCnUID = getChannelForPartner(event.getPartnerUID());

    if (expectedBeUID == null)
    {
      Map beMap = (Map)elems[0];
      assertNull("BeUID should be null", beMap.get(BusinessEntity.UID));
    }
    else
    {
      BusinessEntity expectedBe = findBizEntityByUId(expectedBeUID);
      checkBe(expectedBe, (Map)elems[0]);
    }

    if (expectedCnUID == null)
    {
      Map beMap = (Map)elems[1];
      assertNull("ChannelUID should be null", beMap.get(ChannelInfo.UID));
    }
    else
    {
      ChannelInfo expectedCn = getChannelByUID(expectedCnUID);
      checkChannel(expectedCn, (Map)elems[1]);
    }
  }

  private GetBizEntityForPartnerEvent createTestEvent(Long partnerUID)
    throws Exception
  {
    return new GetBizEntityForPartnerEvent(partnerUID);
  }

  private void getCheckFail(
    GetBizEntityForPartnerEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    GetBizEntityForPartnerEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}