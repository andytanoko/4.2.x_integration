/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.CreateBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;

import com.gridnode.pdip.app.bizreg.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

import java.util.Map;
import java.util.Vector;

/**
 * This Test case tests the CreateBusinessEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class CreateBusinessEntityActionTest extends ActionTestHelper
{
  static final String BE_ID = "be";
  static final String BE_DESC = "be description ";

  CreateBusinessEntityEvent[] _events;

  public CreateBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateBusinessEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


  // *************** ActionTestHelper methods ****************************

  protected void cleanUp()
  {
    cleanUpBEs(null);
    cleanUpBEs(ENTERPRISE);
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createSessions(1);
    createStateMachines(1);

    _events = new CreateBusinessEntityEvent[]
              {
                //accepted
                createTestEvent(BE_ID+0, BE_DESC+0, false),
                createTestEvent(BE_ID+1, BE_DESC+1, true),
                //rejected: duplicate
                createTestEvent(BE_ID+0, BE_DESC+2, true),
                createTestEvent(BE_ID+1, BE_DESC+3, false),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** ACCEPTED *****************************
    //own
    createCheckSuccess(_events[0], _sessions[0], _sm[0]);
    //partner (no enterprise id)
    createCheckSuccess(_events[1], _sessions[0], _sm[0]);

    // ************** REJECTED ***************************
    //duplicate BE ID:partner --> own exists
    createCheckFail(_events[0], _sessions[0], _sm[0], false);
    //duplicate BE ID:own --> partner exists
    createCheckFail(_events[1], _sessions[0], _sm[0], false);
  }

  protected IEJBAction createNewAction()
  {
    return new CreateBusinessEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CreateBusinessEntityEvent createEvent = (CreateBusinessEntityEvent)event;

    System.out.println("isPartnerBE="+createEvent.isPartnerBE());
    String enterpriseID = null;
    if (!createEvent.isPartnerBE())
      enterpriseID = ENTERPRISE;

    BusinessEntity created = findBizEntity(enterpriseID, createEvent.getBeID());
    assertNotNull("BusinessEntity retrieved is null", created);

    WhitePage createdWp = created.getWhitePage();

    //check be information
    assertNotNull("UID is null", created.getKey());
    assertEquals("Id incorrect", createEvent.getBeID(), created.getBusEntId());
    assertEquals("Description incorrect", createEvent.getDescription(), created.getDescription());
    assertEquals("Enterprise is incorrect", enterpriseID, created.getEnterpriseId());
    assertEquals("IsPartner is incorrect", createEvent.isPartnerBE(), created.isPartner());
    assertEquals("State is incorrect", BusinessEntity.STATE_NORMAL, created.getState());

    //check white page
    Map wpData = createEvent.getWhitePage();
    assertNotNull("UID in whitePage is null", createdWp.getKey());
    assertEquals("BEUID is incorrect", created.getKey(), createdWp.getBeUId());
    assertEquals("Biz Description is incorrect", wpData.get(WhitePage.BUSINESS_DESC), createdWp.getBusinessDesc());
    assertEquals("DUNS is incorrect", wpData.get(WhitePage.DUNS), createdWp.getDUNS());
    assertEquals("GCCC is incorrect", wpData.get(WhitePage.G_SUPPLY_CHAIN_CODE), createdWp.getGlobalSupplyChainCode());
    assertEquals("ContactPerson is incorrect", wpData.get(WhitePage.CONTACT_PERSON), createdWp.getContactPerson());
    assertEquals("Email is incorrect",wpData.get(WhitePage.EMAIL), createdWp.getEmail());
    assertEquals("Tel is incorrect", wpData.get(WhitePage.TEL), createdWp.getTel());
    assertEquals("Fax is incorrect", wpData.get(WhitePage.FAX), createdWp.getFax());
    assertEquals("Website is incorrect", wpData.get(WhitePage.WEBSITE), createdWp.getWebsite());
    assertEquals("Address is incorrect", wpData.get(WhitePage.ADDRESS), createdWp.getAddress());
    assertEquals("City is incorrect", wpData.get(WhitePage.CITY), createdWp.getCity());
    assertEquals("State is incorrect", wpData.get(WhitePage.STATE), createdWp.getState());
    assertEquals("ZipCode is incorrect", wpData.get(WhitePage.ZIP_CODE), createdWp.getZipCode());
    assertEquals("POBox is incorrect", wpData.get(WhitePage.PO_BOX), createdWp.getPOBox());
    assertEquals("Country is incorrect", wpData.get(WhitePage.COUNTRY), createdWp.getCountry());
    assertEquals("Language is incorrect", wpData.get(WhitePage.LANGUAGE), createdWp.getLanguage());

  }

  // ****************** Utility methods ****************************

  private CreateBusinessEntityEvent createTestEvent(
    String beid, String description, boolean isPartner) throws Exception
  {
    return new CreateBusinessEntityEvent(beid, description, isPartner,
                 getWhitePageData(), new Vector());
  }

  private void createCheckFail(
    CreateBusinessEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CREATE_ENTITY_ERROR);
  }

  private void createCheckSuccess(
    CreateBusinessEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}