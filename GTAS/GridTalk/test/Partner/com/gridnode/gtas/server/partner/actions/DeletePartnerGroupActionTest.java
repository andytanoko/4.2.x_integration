/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteEntityListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 21 2003    Neo Sok Lay         Re-write test case.
 */
package com.gridnode.gtas.server.partner.actions;

import com.gridnode.gtas.events.partner.DeletePartnerGroupEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.actions.DeleteEntityListActionTest;
import com.gridnode.gtas.server.actions.IEntityTestHelper;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Hashtable;

public class DeletePartnerGroupActionTest extends DeleteEntityListActionTest
{
  // default test data set
//  private static String PARTNER_TYPE       = "DTB";
//  private static String PARTNER_TYPE_DESCR = "Distributor";
//  private static String NAME               = "LD";
//  private static String DESCRIPTION        = "Local Distributor";

  Long[] _partnerUIDs;
  
  public DeletePartnerGroupActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeletePartnerGroupActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected String getClassName()
  {
    return "DeletePartnerGroupActionTest";
  }

  protected Class getEventClass()
  {
    return DeletePartnerGroupEvent.class;
  }

  protected Class getActionClass()
  {
    return DeletePartnerGroupAction.class;
  }

  protected IEntityTestHelper getEntityTestHelper()
  {
    if (_helper == null)
      _helper = new PartnerGroupTestHelper();

    return _helper;
  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#cleanUp()
   */
  protected void cleanUp() throws Exception
  {
    PartnerGroupTestHelper helper = (PartnerGroupTestHelper)getEntityTestHelper();
    helper.deletePartners(2);
    helper.deletePartnerGroups(3);
    helper.deleteDefaultPartnerType();
  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#cleanTestData()
   */
  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#prepareTestData()
   */
  protected void prepareTestData() throws Exception
  {
    log("prepareTestData", "Enter");

    PartnerGroupTestHelper helper = (PartnerGroupTestHelper)getEntityTestHelper();
    
    helper.createPartnerGroups(2, 1);
    _partnerUIDs = helper.createPartners(2, helper._groups[0].getPartnerType(), helper._groups[0]);
    
    TEST_IDS = new String[] {"1", "2"};

    DeleteEntityListEvent[] events = new DeleteEntityListEvent[]
    {
      createEvent(Arrays.asList(helper._groupUids)),
      createEvent(Arrays.asList(new Object[]{helper._groupUids[0]})),
    }
    ;
    ExpectedResult[][] expectedResults = new ExpectedResult[][]
    {
      {
        new ExpectedResult(false, ERROR_TYPE_APPLICATION, IErrorCode.DEPENDENCIES_EXIST_ERROR),
        new ExpectedResult(true, ERROR_TYPE_NA, IErrorCode.NO_ERROR),
        new ExpectedResult(false, ERROR_TYPE_APPLICATION, IErrorCode.DELETE_NOT_ENABLED_ERROR),
      },
      {
        new ExpectedResult(true, ERROR_TYPE_NA, IErrorCode.NO_ERROR),
      }
    };
      
    putTestData(TEST_IDS[0],  events[0]);
    putTestData(TEST_IDS[1],  events[1]);
    
    Hashtable results;
    for (int i=0; i<TEST_IDS.length; i++)
    {
      results = new Hashtable();
      
      Object[] uids = events[i].getUids().toArray();
      for (int j=0; j<uids.length; j++)
      {
        results.put(uids[j], expectedResults[i][j]);
      }       
      putResultData(TEST_IDS[i], results);
    }

    createSessions(1);
    createStateMachines(1);

    log("prepareTestData", "Exit");

  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#unitTest()
   */
  protected void unitTest() throws Exception
  {
    PartnerGroupTestHelper helper = (PartnerGroupTestHelper)getEntityTestHelper();

    deleteCheckFail(_sessions[0], _stateMs[0], false, TEST_IDS[0]);

    helper.updatePartner(_partnerUIDs[0], new Number[]{Partner.PARTNER_GROUP}, new Object[]{null});
    helper.deletePartner(_partnerUIDs[1]);
      
    deleteCheckSuccess( _sessions[0], _stateMs[0], TEST_IDS[1]);
  }

  /**
   * @see com.gridnode.gtas.server.actions.GridTalkActionTest#setupManagers()
   */
  protected void setupManagers() throws Exception
  {
    getEntityTestHelper();
  }


}