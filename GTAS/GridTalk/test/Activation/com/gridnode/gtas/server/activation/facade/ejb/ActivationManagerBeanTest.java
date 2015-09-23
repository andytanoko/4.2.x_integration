/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.facade.ejb;

import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.receivers.ActivationAcknowledgementReceiver;
import com.gridnode.gtas.server.activation.receivers.ActivationMessageReceiver;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.gtas.server.activation.model.SearchedGridNode;
import com.gridnode.gtas.server.activation.model.SearchGridNodeQuery;
import com.gridnode.gtas.server.activation.receivers.SearchResultReceiver;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria;
import com.gridnode.gtas.server.activation.helpers.ActionTestHelper;

import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This Test case tests the ActivationManagerBean class.<p>
 *
 * The following methods are being tested:<p>
 * <li>submitSearch</li>
 * <li>notifySearchResults</li>
 * <li>submitActivationRequest</li>
 * <li>abortActivationRequest</li>
 * <li>denyActivationRequest</li>
 * <li>approveActivationRequest</li>
 * <li>submitDeactivationRequest</li>
 * <li>receiveRequest - test via ActivationMessageReceiver</li>
 * <li>receiveAcknowledgement - test via ActivationAcknowledgementReceiver</li>
 * <p>
 * The following methods are implicitly tested:<p>
 * <li>retrieveSearch</li>
 * <li>findActivationRecord</li>
 * <li>findActivationRecordsByFilter</li>
 * <li>deleteActivationRecord</li>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ActivationManagerBeanTest extends ActionTestHelper
{
  private static final String[] CERT_FILE     = {
      DATA_PATH+"TestCert1.cer", DATA_PATH+"TestCert2.cer",
      //DATA_PATH+"TestCert3.cer"
      };

  private final String SEARCH_ACK_EVENT_ID = "801";
  private final String DEACT_EVENT_ID      = "408";
  private final String DEACT_ACK_EVENT_ID  = "409";
  private final String ABORT_EVENT_ID      = "406";
  private final String ABORT_ACK_EVENT_ID  = "407";
  private final String REPLY_EVENT_ID      = "404";
  private final String REPLY_ACK_EVENT_ID  = "405";
  private final String REQUEST_EVENT_ID    = "400";
  private final String REQUEST_ACK_EVENT_ID= "401";

  private final String SEARCH_RESULTS_FILE = DATA_PATH + "testSearch.results";
  private final String EMPTY_SEARCH_RESULTS_FILE = DATA_PATH + "testSearchEmpty.results";
  private final String ACTIVATION_FILE     = DATA_PATH + "testActivation.act";
  private final String ABORT_FILE          = DATA_PATH + "testAbort.act";
  private final String DEACTIVATION_FILE   = DATA_PATH + "testDeactivation.act";
  private final String APPROVE_FILE        = DATA_PATH + "testApprove.act";
  private final String DENY_FILE           = DATA_PATH + "testDeny.act";
  private final Collection SEARCH_RESULTS = new ArrayList();
  private final DataFilterImpl ACT_RECORD_FILTER = new DataFilterImpl();
  private final ActivationMessageReceiver ACT_RECEIVER = new ActivationMessageReceiver();
  private final ActivationAcknowledgementReceiver ACK_RECEIVER = new ActivationAcknowledgementReceiver();
  private final SearchResultReceiver SEARCH_RECEIVER = new SearchResultReceiver();

  private Long[] _beUIDs, _certUIDs, _cnUIDs;
  private Collection _exBeUIDs = new ArrayList();

  public ActivationManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ActivationManagerBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {

    cleanUpSharedBes();
    cleanUpBeChannelLinks();
    //cleanUpBEs(ENTERPRISE);
    cleanUpBEs(PARTNER_ENT);
    cleanUpChannels();
    cleanUpGridNodes();
    cleanUpGridTalkCertificates(new String[] {/*ENTERPRISE,*/ PARTNER_ENT});
    cleanUpActivationRecords(new Integer(PARTNER_ENT));
  }

  protected void cleanTestData() throws Exception
  {
  }

  protected void prepareTestData() throws Exception
  {
    /*
    createMyGridNode();

    SearchedGridNode gn = new SearchedGridNode();
    gn.setGridNodeID(new Integer(507));
    gn.setGridNodeName("ACME Inc");
    gn.setState(new Short(gn.STATE_INACTIVE));
    SEARCH_RESULTS.add(gn);

    gn = new SearchedGridNode();
    gn.setGridNodeID(new Integer(509));
    gn.setGridNodeName("Smith Inc.");
    gn.setState(new Short(gn.STATE_INACTIVE));
    SEARCH_RESULTS.add(gn);

    gn = new SearchedGridNode();
    gn.setGridNodeID(new Integer(521));
    gn.setGridNodeName("AmpWay Inc.");
    gn.setState(new Short(gn.STATE_ME));
    SEARCH_RESULTS.add(gn);

    createBes(2, 0);
    _beUIDs = getUIDs(_bizEntities);
    for (int i=0; i<_beUIDs.length; i++)
      _exBeUIDs.add(_beUIDs[i]);
    Long uid1 = createCertificate(Integer.parseInt(ENTERPRISE),
                  CERT_NAME, CERT_FILE[0]); // my cert
    _certUIDs = new Long[] {uid1, uid1, uid1} ;

    createChannels(3, _certUIDs);
    _cnUIDs = getUIDs(_channels);

    //be[0]->channel[0]
    assignChannelsToBe(new Long[] { _cnUIDs[0] }, _beUIDs[0]);
    //be[1]->channel[1],channel[2]
    assignChannelsToBe(new Long[] { _cnUIDs[1], _cnUIDs[2] }, _beUIDs[1]);
    */

    _exBeUIDs.add(new Long(2));
    _exBeUIDs.add(new Long(3));

    ACT_RECORD_FILTER.addSingleFilter(null, ActivationRecord.GRIDNODE_ID,
      ACT_RECORD_FILTER.getEqualOperator(), PARTNER_ENT, false);
    ACT_RECORD_FILTER.addSingleFilter(ACT_RECORD_FILTER.getAndConnector(),
      ActivationRecord.IS_LATEST, ACT_RECORD_FILTER.getEqualOperator(),
      Boolean.TRUE, false);

  }

  protected void unitTest() throws Exception
  {
    //simulateConnected();
    try
    {
      //searchGridNodeTest();
      proactiveActivationTest();
      reactiveActivationTest();
    }
    finally
    {
      //simulateDisconnected();
    }
  }

  private void searchGridNodeTest() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.searchGridNodeTest] Enter ");

    boolean exist = new File(SEARCH_RESULTS_FILE).exists();
    if (!exist)
    throw new Exception("Search results file does not exist");

    // 1. no criteria
    SearchGridNodeCriteria criteria1 = new SearchGridNodeCriteria();
    criteria1.setCriteriaType(criteria1.CRITERIA_NONE);
    Long searchID1 = _actMgr.submitSearch(criteria1);

    // 2. by gridnode
    SearchGridNodeCriteria criteria2 = new SearchGridNodeCriteria();
    criteria2.setCriteriaType(criteria2.CRITERIA_BY_GRIDNODE);
    criteria2.setMatchType(criteria2.MATCH_ALL);
    criteria2.setGridNodeID(new Integer(522));
    criteria2.setGridNodeName("CSVI");
    criteria2.setCategory("GNL");
    Long searchID2 = _actMgr.submitSearch(criteria2);

    // 3. by whitepage
    SearchGridNodeCriteria criteria3 = new SearchGridNodeCriteria();
    criteria3.setCriteriaType(criteria3.CRITERIA_BY_WHITEPAGE);
    criteria3.setMatchType(criteria3.MATCH_ANY);
    criteria3.setBusinessDesc("fish");
    criteria3.setContactPerson("Andy");
    criteria3.setCountry("SGP");
    criteria3.setWebsite("fish");
    Long searchID3 = _actMgr.submitSearch(criteria3);

    // return results for 1
    SEARCH_RECEIVER.handlerMessage(
      new String[] {SEARCH_ACK_EVENT_ID, searchID1.toString(), null, null, null},
      new String[]{}, new File[] {new File(SEARCH_RESULTS_FILE)}, null);
    // return results for 2
    SEARCH_RECEIVER.handlerMessage(
      new String[] {SEARCH_ACK_EVENT_ID, searchID2.toString(), null, null, null},
      new String[]{}, new File[] {new File(EMPTY_SEARCH_RESULTS_FILE)}, null);

    // retrieve results for 1
    SearchGridNodeQuery query = _actMgr.retrieveSearch(searchID1);
    checkSearchQuery(query, searchID1, criteria1, SEARCH_RESULTS);

    // retrieve results for 2
    query = _actMgr.retrieveSearch(searchID2);
    checkSearchQuery(query, searchID2, criteria2, new ArrayList());

    // retrieve results for 3
    query = _actMgr.retrieveSearch(searchID3);
    checkSearchQuery(query, searchID3, criteria3, null);
  }

  private void proactiveActivationTest() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.proactiveActivationTest] Enter ");

    submitRequestAndAbort();
    submitRequestAndDeny();
    submitRequestAndApprove();
    submitDeact();
  }

  private synchronized void submitRequestAndAbort() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.submitRequestAndAbort] Enter ");
    // submit request
    _actMgr.submitActivationRequest(
      new Integer(PARTNER_ENT), PARTNER_GN_NAME, ACTIVATE_REASON,
      _exBeUIDs);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);

    checkOutgoingActivation(actRecord);

    // receive ack for request
    ACK_RECEIVER.handlerMessage(
      new String[] {REQUEST_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[0], null);

    // abort the request
    _actMgr.abortActivationRequest((Long)actRecord.getKey());

    // receive ack for abort
    ACK_RECEIVER.handlerMessage(
      new String[] {REQUEST_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[0], null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkAbort(actRecord);
  }

  private synchronized void submitRequestAndDeny() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.submitRequestAndDeny] Enter ");
    // submit request
    _actMgr.submitActivationRequest(
      new Integer(PARTNER_ENT), PARTNER_GN_NAME, ACTIVATE_REASON,
      _exBeUIDs);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);
System.out.println("************* Act Record Key1 ="+actRecord.getKey());
System.out.println("************* Act Record ="+actRecord.toString());
    checkOutgoingActivation(actRecord);
System.out.println("************* Act Record Key2 ="+actRecord.getKey());
System.out.println("************* ACK RECEIVER = "+ACK_RECEIVER);
    // receive ack for request
    ACK_RECEIVER.handlerMessage(
      new String[] {REQUEST_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[0], null);

    // receive Deny
    ACT_RECEIVER.handlerMessage(
      new String[] {REPLY_EVENT_ID, "1", null, null, null},
      new String[] {Boolean.FALSE.toString()},
      new File[]{
        new File(DENY_FILE).getAbsoluteFile(),
      },
      null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkDeny(actRecord);
  }

  private synchronized void submitRequestAndApprove() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.submitRequestAndApprove] Enter ");
    // submit request
    _actMgr.submitActivationRequest(
      new Integer(PARTNER_ENT), PARTNER_GN_NAME, ACTIVATE_REASON,
      _exBeUIDs);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);

    checkOutgoingActivation(actRecord);

    // receive ack for request
    ACK_RECEIVER.handlerMessage(
      new String[] {REQUEST_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[0], null);

    // receive Approve
    ACT_RECEIVER.handlerMessage(
      new String[] {REPLY_EVENT_ID, "2", null, null, null},
      new String[] {Boolean.TRUE.toString()},
      new File[]{
        new File(APPROVE_FILE).getAbsoluteFile(),
        new File(CERT_FILE[1]).getAbsoluteFile()},
      null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkApprove(actRecord);
  }

  private synchronized void submitDeact() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.submitDeact] Enter ");
    // submit request
    _actMgr.submitDeactivationRequest(
      new Integer(PARTNER_ENT));

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record",actRecord);

    // receive ack for request
    ACK_RECEIVER.handlerMessage(
      new String[] {DEACT_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[0], null);

    checkOutgoingDeactivation(actRecord);
  }

  private void reactiveActivationTest() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.reactiveActivationTest] Enter ");
    receiveRequestAndAbort();
    receiveRequestAndDeny();
    receiveRequestAndApprove();
    receiveDeact();
  }

  private synchronized void receiveRequestAndAbort() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.receiveRequestAndAbort] Enter ");
    // receive request
    ACT_RECEIVER.handlerMessage(
      new String[] {REQUEST_EVENT_ID, "3", null, null, null},
      new String[] {}, new File[]{new File(ACTIVATION_FILE).getAbsoluteFile()},
      null);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);

    checkIncomingActivation(actRecord);

    // receive abort
    ACT_RECEIVER.handlerMessage(
      new String[] {ABORT_EVENT_ID, "4", null, null, null},
      new String[] {}, new File[]{new File(ABORT_FILE).getAbsoluteFile()},
      null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkAbort(actRecord);
  }

  private synchronized void receiveRequestAndDeny() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.receiveRequestAndDeny] Enter ");
    // receive request
    ACT_RECEIVER.handlerMessage(
      new String[] {REQUEST_EVENT_ID, "5", null, null, null},
      new String[] {}, new File[]{new File(ACTIVATION_FILE).getAbsoluteFile()},
      null);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);

    checkIncomingActivation(actRecord);

    // deny the request
    _actMgr.denyActivationRequest(new Long(actRecord.getUId()));

    ACK_RECEIVER.handlerMessage(
      new String[] {REPLY_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()}, new File[]{}, null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkDeny(actRecord);
  }

  private synchronized void receiveRequestAndApprove() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.receiveRequestAndApprove] Enter ");
    // receive request
    ACT_RECEIVER.handlerMessage(
      new String[] {REQUEST_EVENT_ID, "7", null, null, null},
      new String[] {}, new File[]{new File(ACTIVATION_FILE).getAbsoluteFile()},
      null);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record created",actRecord);

    checkIncomingActivation(actRecord);

    // approve the request
    _actMgr.approveActivationRequest(new Long(actRecord.getUId()), _exBeUIDs);

    ACK_RECEIVER.handlerMessage(
      new String[] {REPLY_ACK_EVENT_ID, String.valueOf(actRecord.getUId()), null, null, null},
      new String[] {Boolean.TRUE.toString()},
      new File[]{
        new File(CERT_FILE[1]).getAbsoluteFile(),
        },
      null);

    //wait(1000);

    actRecord = getLatestActRecord();
    checkApprove(actRecord);
  }

  private synchronized void receiveDeact() throws Exception
  {
    Logger.debug("[ActivationManagerBeanTest.receiveDeact] Enter ");
    // receive deact
    ACT_RECEIVER.handlerMessage(
      new String[] {DEACT_EVENT_ID, "9", null, null, null},
      new String[] {}, new File[]{new File(DEACTIVATION_FILE).getAbsoluteFile()},
      null);

    //wait(1000);

    ActivationRecord actRecord = getLatestActRecord();
    assertNotNull("No activation record",actRecord);

    checkIncomingDeactivation(actRecord);
  }

  // not used
  protected IEJBAction createNewAction()
  {
    return null;
  }

  // not used
  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }

  // ******************* Own methods ****************************

  private void checkOutgoingActivation(ActivationRecord record)
  {
    assertEquals("ActDirection is incorrect", ActivationRecord.DIRECTION_OUTGOING, record.getActivateDirection().shortValue());
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_ACTIVATION, record.getCurrentType().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("ActivationDetails is null", record.getActivationDetails());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
    assertEquals("Request GridNodeID is incorrect", ENTERPRISE, record.getActivationDetails().getRequestDetails().getGridnode().getID());
    assertEquals("ActivateReason is incorrect", ACTIVATE_REASON, record.getActivationDetails().getActivateReason());
  }

  private void checkIncomingActivation(ActivationRecord record)
  {
    assertEquals("ActDirection is incorrect", ActivationRecord.DIRECTION_INCOMING, record.getActivateDirection().shortValue());
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_ACTIVATION, record.getCurrentType().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
    assertEquals("ActivateReason is incorrect", ACTIVATE_REASON, record.getActivationDetails().getActivateReason());
    assertEquals("Request GridNodeID is incorrect", PARTNER_ENT, record.getActivationDetails().getRequestDetails().getGridnode().getID());
  }

  private void checkAbort(ActivationRecord record)
  {
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_ABORTION, record.getCurrentType().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertNotNull("DtAborted timestamp is null", record.getDTAborted());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
  }

  private void checkDeny(ActivationRecord record)
  {
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_DENIAL, record.getCurrentType().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertNotNull("DtDenied timestamp is null", record.getDTDenied());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
  }

  private void checkApprove(ActivationRecord record)
  {
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_APPROVAL, record.getCurrentType().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertNotNull("DtApproved timestamp is null", record.getDTApproved());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
    assertNotNull("ApproveDetails is null", record.getActivationDetails().getApproveDetails());
  }

  private void checkOutgoingDeactivation(ActivationRecord record)
  {
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_DEACTIVATION, record.getCurrentType().shortValue());
    assertEquals("DeactDirection is incorrect", ActivationRecord.DIRECTION_OUTGOING, record.getDeactivateDirection().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertNotNull("DtDeactivated timestamp is null", record.getDTDeactivated());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
    assertNotNull("ApproveDetails is null", record.getActivationDetails().getApproveDetails());
  }

  private void checkIncomingDeactivation(ActivationRecord record)
  {
    assertEquals("CurrentType is incorrect", ActivationRecord.TYPE_DEACTIVATION, record.getCurrentType().shortValue());
    assertEquals("DeactDirection is incorrect", ActivationRecord.DIRECTION_INCOMING, record.getDeactivateDirection().shortValue());
    assertNotNull("DtRequested timestamp is null", record.getDTRequested());
    assertNotNull("DtDeactivated timestamp is null", record.getDTDeactivated());
    assertEquals("GridNode id is incorrect", PARTNER_ENT, record.getGridNodeID().toString());
    assertEquals("Gridnode name is incorrect", PARTNER_GN_NAME, record.getGridNodeName());
    assertNotNull("RequestDetails is null", record.getActivationDetails().getRequestDetails());
    assertNotNull("ApproveDetails is null", record.getActivationDetails().getApproveDetails());
  }

  private void checkSearchQuery(SearchGridNodeQuery query, Long searchID,
    SearchGridNodeCriteria expCriteria, Collection expResults)
  {
    assertEquals("SearchID incorrect", searchID, query.getSearchID());
    assertNotNull("DtSubmitted is null", query.getDTSubmitted());
    if (expResults != null)
    {
      assertNotNull("DtResponded is null", query.getDTResponded());
      assertNotNull("Search Results is null", query.getSearchResults());
      checkSearchResults(query.getSearchResults(), expResults);
    }
    checkSearchCriteria(query.getSearchCriteria(), expCriteria);
  }

  private void checkSearchResults(Collection results, Collection expResults)
  {
    assertEquals("Result size is incorrect", expResults.size(), results.size());
    Object[] exp = expResults.toArray();
    Object[] ret = results.toArray();
    for (int i=0; i<exp.length; i++)
    {
      SearchedGridNode expGn = (SearchedGridNode)exp[i];
      SearchedGridNode gn = (SearchedGridNode)ret[i];

      assertEquals("GridNode ID is incorrect", expGn.getGridNodeID(), gn.getGridNodeID());
      assertEquals("GridNode Name is incorrect", expGn.getGridNodeName(), gn.getGridNodeName());
      assertEquals("State is incorrect", expGn.getState(), gn.getState());
    }
  }

  private void checkSearchCriteria(SearchGridNodeCriteria criteria,
    SearchGridNodeCriteria expCriteria)
  {
    assertEquals("Search criteria is incorrect", expCriteria, criteria);
  }

  private void assignChannelsToBe(Long[] channelUIDs, Long beUID)
  {
    ArrayList list = new ArrayList(channelUIDs.length);
    for (int i=0; i<channelUIDs.length; i++)
    {
      list.add(channelUIDs[i]);
    }
    this.assignChannelsToBe(list, beUID);
  }

  private ActivationRecord getLatestActRecord()
  {
    Collection results = findActivationRecords(ACT_RECORD_FILTER);
    if (results.size() > 0)
      return (ActivationRecord)results.toArray()[0];
    return null;
  }

}