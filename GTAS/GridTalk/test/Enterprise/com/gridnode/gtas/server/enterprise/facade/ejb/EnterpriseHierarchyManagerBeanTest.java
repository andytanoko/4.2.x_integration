/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseHierarchyManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;

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
 * This Test case tests the EnterpriseHierarchyBean class.<p>
 *
 * The following methods are being tested:<p>
 * <li>getPartnersForBizEntity(beUID)</li>
 * <li>getPartnersForBizEntity(enterpriseID,beID)</li>
 * <p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class EnterpriseHierarchyManagerBeanTest extends ActionTestHelper
{
  private Long[] _beUIDs, _pnUIDs;

  public EnterpriseHierarchyManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(EnterpriseHierarchyManagerBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpPartnerLinks();
    //cleanUpBeChannelLinks();
    cleanUpBEs(PARTNER_ENT);
    cleanUpPartners();
    //cleanUpChannels();
  }

  protected void cleanTestData() throws Exception
  {
  }

  protected void prepareTestData() throws Exception
  {
    createBes(2, 2, PARTNER_ENT);
    _beUIDs = getUIDs(_bizEntities);
    createPartners(3);
    _pnUIDs = getUIDs(_partners);

    this.assignBeToPartner(_pnUIDs[0], _beUIDs[0]);
    this.assignBeToPartner(_pnUIDs[1], _beUIDs[0]);
  }

  protected void unitTest() throws Exception
  {
    getPartnersForBizEntityTest();
  }

  private void getPartnersForBizEntityTest()
  {
    //1. invalid beUId
    getPartnersForBizEntityCheck(DUMMY_UID, new Long[0]);

    //2. Invalid beID/enterpriseId
    getPartnersForBizEntityCheck(DUMMY_STRING, DUMMY_STRING, new Long[0]);

    //3. No partner association (by beID, enterpriseID)
    getPartnersForBizEntityCheck(PARTNER_ENT, ID+1, new Long[0]);

    //4. 2 partner associations (by beID, enterpriseID)
    getPartnersForBizEntityCheck(PARTNER_ENT, ID+0, new Long[] {_pnUIDs[0], _pnUIDs[1]});
  }

  protected IEJBAction createNewAction()
  {
    return null;
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }

  // ******************* Own methods ****************************

  private void getPartnersForBizEntityCheck(Long beUID, Long[] expectedPnUIDs)
  {
    Collection pnUIDs = getPartnerUIDsForBizEntity(beUID);
    assertEquals("Incorrect Partner count", expectedPnUIDs.length, pnUIDs.size());
    for (int i=0; i<expectedPnUIDs.length; i++)
    {
      assertTrue("Partner not returned as expected", pnUIDs.contains(expectedPnUIDs[i]));
    }
  }

  private void getPartnersForBizEntityCheck(String enterpriseID,
    String beID, Long[] expectedPnUIDs)
  {
    Collection pnUIDs = getPartnerUIDsForBizEntity(enterpriseID, beID);
    assertEquals("Incorrect Partner count", expectedPnUIDs.length, pnUIDs.size());
    for (int i=0; i<expectedPnUIDs.length; i++)
    {
      assertTrue("Partner not returned as expected", pnUIDs.contains(expectedPnUIDs[i]));
    }
  }
}