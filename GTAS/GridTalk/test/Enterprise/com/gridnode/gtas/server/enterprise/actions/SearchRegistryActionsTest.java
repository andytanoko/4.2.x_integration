/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.GetMessagingStandardsEvent;
import com.gridnode.gtas.events.enterprise.RetrieveRegistrySearchEvent;
import com.gridnode.gtas.events.enterprise.SubmitRegistrySearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This test case tests the following SearchRegistry actions:<p>
 * <ul>
 *   <li>GetMessagingStandardsAction</li>
 *   <li>SubmitRegistrySearchAction</li>
 *   <li>RetrieveRegistrySearchAction</li>
 * </ul> 
 * <p>
 * This test case assumes the target public registry for testing contains the
 * 3 messaging standards, namely, RNIF1,RNIF2, and SOAP, and that there are two
 * published organizations (whose names contains 'busi' and not 'busy') 
 * each with a RNIF2 service binding.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SearchRegistryActionsTest extends ActionTestHelper
{
  SubmitRegistrySearchEvent[] _events;
  ExpectedResult[] _expectedResults;
  int _currCaseNum = -1;
  
  //static final String GOOD_URL = "http://localhost:8080/RegistryServer";
  static final String BAD_URL = "http://localhost:8080/Registry";
  static final String GOOD_URL = "http://192.168.213.186:8080/juddi/inquiry";
  static final String NAME_PATTERN_1 = "public";
  static final String NAME_PATTERN_2 = "busy";
  static final String STD_RNIF1 = "gridnode-com:rnif-1.1";
  static final String STD_RNIF2 = "gridnode-com:rnif-2";
  static final String STD_SOAP = "gridnode-com:soap-1";
  static final String[] STDS = {STD_RNIF1,STD_RNIF2,STD_SOAP};
  static final String DUNS_1 = "2909-1338-1355";
  
  private class ExpectedResult
  {
    boolean _isException;
    int _resultSize;
    
    ExpectedResult(boolean isException, int resultSize)
    {
      _isException = isException;
      _resultSize = resultSize;
    }
  }
  
  public SearchRegistryActionsTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SearchRegistryActionsTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#cleanUp()
   */
  protected void cleanUp()
  {
    purgeSessions();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#prepareTestData()
   */
  protected void prepareTestData() throws Exception
  {
    createStateMachines(1);
    createSessions(1);

    String combinedPattern = NAME_PATTERN_1 + " " + NAME_PATTERN_2;
    
    _events = new SubmitRegistrySearchEvent[]
    {
      createTestEvent(new String[]{STD_RNIF1}, NAME_PATTERN_1, null, GOOD_URL, true),
      createTestEvent(new String[]{STD_RNIF2}, NAME_PATTERN_2, null, GOOD_URL, true),
      createTestEvent(new String[]{STD_RNIF2}, NAME_PATTERN_1, null, BAD_URL, true),
      createTestEvent(new String[]{STD_RNIF2}, NAME_PATTERN_1, null, GOOD_URL, false),
      createTestEvent(new String[]{STD_RNIF1,STD_RNIF2}, combinedPattern, null, GOOD_URL, false),
      createTestEvent(new String[]{STD_RNIF1,STD_RNIF2,STD_SOAP}, null, DUNS_1, GOOD_URL, false),
      //createTestEvent(new String[]{STD_RNIF1,STD_RNIF2,STD_SOAP}, combinedPattern, null, GOOD_URL, true),
    };
    
    _expectedResults = new ExpectedResult[]
    {
      new ExpectedResult(false, 1),
      new ExpectedResult(false, 0),
      new ExpectedResult(true, 0),
      new ExpectedResult(false, 2),
      new ExpectedResult(false, 2),
      new ExpectedResult(false, 1),
     // new ExpectedResult(false, 0),  
    };
        
    // event 1: RNIF1, busi, goodUrl, matchAny (no match)
    // event 2: RNIF2, busy, goodUrl, matchAny (no match)
    // event 3: RNIF2, busi, badUrl, matchAny (exception)
    // event 4: RNIF2, busi, goodUrl, matchAny (3 match)
    // event 5: (RNIF1,RNIF2), (busi,busy), goodUrl, matchAny (3 match)
    // event 6: (RNIF1,RNIF2,SOAP), duns, goodUrl, matchAny (1 match)

    // probably a bug in jUDDI, can't do matchAll
    // event 7: (RNIF1,RNIF2,SOAP), (busi,busy), matchAll (no match)
    
  }

  private SubmitRegistrySearchEvent createTestEvent(
    String[] msgStds, String namePatterns, String duns, 
    String queryUrl, boolean matchAll)
    throws Exception
  {
    HashMap searchCriteria = new HashMap();
    searchCriteria.put(SearchRegistryCriteria.BUS_ENTITY_DESC, namePatterns);
    searchCriteria.put(SearchRegistryCriteria.MATCH, matchAll?new Short(SearchRegistryCriteria.MATCH_ALL):new Short(SearchRegistryCriteria.MATCH_ANY));
    searchCriteria.put(SearchRegistryCriteria.MESSAGING_STANDARDS, Arrays.asList(msgStds));
    searchCriteria.put(SearchRegistryCriteria.QUERY_URL, queryUrl);
    searchCriteria.put(SearchRegistryCriteria.DUNS, duns);
    
    return new SubmitRegistrySearchEvent(searchCriteria);
  }
  
  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#cleanTestData()
   */
  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#unitTest()
   */
  protected void unitTest() throws Exception
  {
    retrieveMessagingStandards(_sessions[0], _sm[0]);

    for (int i=0; i<_events.length; i++)
    {
      submitCheckSuccess(_events[i], _sessions[0], _sm[0]);
    }
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#createNewAction()
   */
  protected IEJBAction createNewAction()
  {
    return new SubmitRegistrySearchAction();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#checkActionEffect(com.gridnode.pdip.framework.rpf.event.BasicEventResponse, com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.rpf.ejb.StateMachine)
   */
  protected void checkActionEffect(
    BasicEventResponse response,
    IEvent event,
    StateMachine sm)
  {
    SubmitRegistrySearchEvent submitEvent = (SubmitRegistrySearchEvent)event;
    
    assertNotNull("No SearchId returned", response.getReturnData());
    Long searchId = (Long)response.getReturnData();
    
    // retrieveRegistrySearch, check until responded
    boolean responded = false;
    Map queryMap = null;
    while (!responded)
    {
      waitForAWhile();
      queryMap = retrieveSearchQuery(searchId, _sessions[0], sm);
      responded = queryMap.get(SearchRegistryQuery.DT_RESPONDED) != null;
    }

    System.out.println("-- Retrieved Search Query --");
    System.out.println(queryMap);
    
    // check against expected results
    ExpectedResult expected = _expectedResults[_currCaseNum];
    assertEquals("IsException is incorrect", Boolean.valueOf(expected._isException), (Boolean)queryMap.get(SearchRegistryQuery.IS_EXCEPTION));
    Collection results = (Collection)queryMap.get(SearchRegistryQuery.RESULTS);
    if (results != null)
      assertEquals("Result size is incorrect", expected._resultSize, results.size());
      
  }

  private void submitCheckSuccess(
    SubmitRegistrySearchEvent event, String session, StateMachine sm)
  {
    _currCaseNum++;
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void retrieveMessagingStandards(String sessionid, StateMachine sm)
  {
    GetMessagingStandardsEvent event = new GetMessagingStandardsEvent();
    GetMessagingStandardsAction action = new GetMessagingStandardsAction();
    
    Collection msgStds = null;
    try
    {
      BasicEventResponse response = performEvent(action, event, sessionid, sm);
      
      if (response.isEventSuccessful())
      {
        msgStds = (Collection)response.getReturnData();
      }
      else
        Log.err("TEST", "[SearchRegistryActionsTest.getMessagingStandards] EventUnsuccessful ");
    }
    catch (Exception e)
    {
      Log.err("TEST", "[SearchRegistryActionsTest.getMessagingStandards] Error: ", e);
    }
 
    assertNotNull("MessagingStandards returned is null", msgStds);
        
    for (int i=0; i<STDS.length; i++)
    {
      assertTrue("Missing MessagingStandards: "+STDS[i], msgStds.contains(STDS[i]));
    }
  }
  
  private Map retrieveSearchQuery(
    Long searchId, String sessionid, StateMachine sm) 
  {
    Map queryMap = null;
    try
    {
      RetrieveRegistrySearchEvent event = new RetrieveRegistrySearchEvent(searchId);
      RetrieveRegistrySearchAction action = new RetrieveRegistrySearchAction();

      BasicEventResponse response = performEvent(action, event, sessionid, sm);
      
      if (response.isEventSuccessful())
      {
        queryMap = (Map)response.getReturnData();
      }
      else
        Log.err("TEST", "[SearchRegistryActionsTest.retrieveSearchQuery] EventUnsuccessful ");
    }
    catch (Exception e)
    {
      Log.err("TEST", "[SearchRegistryActionsTest.retrieveSearchQuery] Error: ", e);
    }
 
    assertNotNull("SearchRegistryQuery map returned is null", queryMap);

    return queryMap;        
  }  
  
  private synchronized void waitForAWhile()
  {
    try
    {
      // wait for 10 seconds
      wait(10000);
    }
    catch (Exception e)
    {
    }
  }
}
