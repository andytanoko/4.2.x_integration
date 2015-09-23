package com.gridnode.gtas.server.bizreg.helpers;

import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.bizreg.model.*;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action Test cases
 * of the BusinessRegistry module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */

public abstract class ActionTestHelper extends TestCase
{
  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String ID             = "BE";
  protected static final String DESC           = "BE Description ";
  protected static final String UPD_DESC       = "Upd BE Description";

  protected IBizRegistryManagerObj _bizRegMgr;
  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";
  protected static final String ENTERPRISE = "888888";
  protected static final String USER_ID   = "mockuserid";

  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected Long[]              _uIDs;
  protected BusinessEntity[]       _bizEntities;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _bizRegMgr = ActionHelper.getBizRegManager();
      _sessionMgr = getSessionMgr();

      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[ActionTestHelper.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[ActionTestHelper.tearDown] Exit");
  }

  protected void createSessions(int numSessions) throws Exception
  {
    _sessions = new String[numSessions];
    for (int i=0; i<numSessions; i++)
      _sessions[i] = openSession();
  }

  protected void createStateMachines(int numSM)
  {
    _sm = new StateMachine[numSM];
    for (int i=0; i<numSM; i++)
      _sm[i] = new StateMachine(null, null);
  }

  protected void createBes(int numBes, int numPartner) throws Exception
  {
    _bizEntities = new BusinessEntity[numBes];
    _uIDs = new Long[numBes];
    for (int i=0; i<numBes; i++)
    {
      if (i<numPartner)
      {
        createBe(null, ID+i, DESC+i, true);
        _bizEntities[i] = findBizEntity(null, ID+i);
      }
      else
      {
        createBe(ENTERPRISE, ID+i, DESC+i, false);
        _bizEntities[i] = findBizEntity(ENTERPRISE, ID+i);
      }
      _uIDs[i] = new Long(_bizEntities[i].getUId());
    }
  }

  public void testPerform() throws Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
      prepareTestData();

      unitTest();

      cleanTestData();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Exit ");
    }

  }

  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected HashMap getWhitePageData()
  {
    HashMap whitePage = new HashMap();
    whitePage.put(WhitePage.BUSINESS_DESC, "Business Description");
    whitePage.put(WhitePage.DUNS,"DUNS");
    whitePage.put(WhitePage.G_SUPPLY_CHAIN_CODE, "GSCC");
    whitePage.put(WhitePage.CONTACT_PERSON,"Contact Person");
    whitePage.put(WhitePage.EMAIL,"email@email.com");
    whitePage.put(WhitePage.TEL, "12234444");
    whitePage.put(WhitePage.FAX, "232434324");
    whitePage.put(WhitePage.WEBSITE, "http://abc@email.com");
    whitePage.put(WhitePage.ADDRESS, "some address");
    whitePage.put(WhitePage.CITY, "some city");
    whitePage.put(WhitePage.STATE, "STATE");
    whitePage.put(WhitePage.ZIP_CODE, "232444");
    whitePage.put(WhitePage.PO_BOX, "124434234");
    whitePage.put(WhitePage.COUNTRY, "ABC");
    whitePage.put(WhitePage.LANGUAGE, "XYZ");

    return whitePage;
  }

  protected HashMap getUpdWhitePageData(Long beUID)
  {
    HashMap whitePage = new HashMap();
    whitePage.put(WhitePage.BUSINESS_DESC, "Upd Business Description");
    whitePage.put(WhitePage.DUNS,"UPDDUNS");
    whitePage.put(WhitePage.G_SUPPLY_CHAIN_CODE, "UPDGSCC");
    whitePage.put(WhitePage.CONTACT_PERSON,"UPDContact Person");
    whitePage.put(WhitePage.EMAIL,"updemail@email.com");
    whitePage.put(WhitePage.TEL, "12234444");
    whitePage.put(WhitePage.FAX, "232434324");
    whitePage.put(WhitePage.WEBSITE, "http://abc@email.upd.com");
    whitePage.put(WhitePage.ADDRESS, "upd some address");
    whitePage.put(WhitePage.CITY, "upd some city");
    whitePage.put(WhitePage.STATE, "USTATE");
    whitePage.put(WhitePage.ZIP_CODE, "232444");
    whitePage.put(WhitePage.PO_BOX, "124434234");
    whitePage.put(WhitePage.COUNTRY, "UPD");
    whitePage.put(WhitePage.LANGUAGE, "UPD");
    whitePage.put(WhitePage.BE_UID, beUID);

    return whitePage;
  }

  protected void cleanUpBEs(String enterprise)
  {
    try
    {
      _bizRegMgr.markDeleteBusinessEntities(enterprise);
      _bizRegMgr.purgeDeletedBusinessEntities(enterprise);
    }
    catch (Exception ex)
    {
    }
  }

  protected Long createBe(String enterpriseId, String beId, String description, boolean isPartner)
  {
    return createBe(enterpriseId, beId, description, isPartner, true);
  }

  protected Long createBe(String enterpriseId, String beId, String description, boolean isPartner,
    boolean canDelete)
  {
    WhitePage wp = new WhitePage();
    ActionHelper.copyEntityFields(getWhitePageData(), wp);
    return createBe(enterpriseId, beId, description, isPartner, wp, canDelete);
  }

  protected Long createBe(
    String enterpriseId, String beId, String description, boolean isPartner,
    WhitePage whitePage, boolean canDelete)
  {
    Long uId = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.createBe] Enter");

      BusinessEntity be = new BusinessEntity();
      be.setBusEntId(beId);
      be.setDescription(description);
      be.setEnterpriseId(enterpriseId);
      be.setPartner(isPartner);
      be.setWhitePage(whitePage);
      be.setCanDelete(canDelete);

      uId = createToDb(be);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createBe]", t);
      assertTrue("Error in createBe", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createBe] Exit");
    }
    return uId;
  }


  protected Long createToDb(BusinessEntity bizEntity) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Enter");

      uID = _bizRegMgr.createBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Exit");
    }
    return uID;
  }

  protected void updateToDb(BusinessEntity bizEntity) throws Throwable
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Enter");
      _bizRegMgr.updateBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Exit");
    }
  }

  protected BusinessEntity findBizEntityByUId(Long uId)
  {
    BusinessEntity bizEntity = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntityByUId] Enter");

      bizEntity = _bizRegMgr.findBusinessEntity(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntityByUId]", ex);
      assertTrue("Error in findBizEntityByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntityByUId] Exit");
    }
    return bizEntity;
  }

  protected BusinessEntity findBizEntity(String enterpriseId, String beId)
  {
    BusinessEntity bizEntity = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntity] Enter: enterpriseID="+enterpriseId);

      bizEntity = _bizRegMgr.findBusinessEntity(enterpriseId, beId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntity]", ex);
      assertTrue("Error in _bizRegMgr.findBusinessEntity", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntity] Exit");
    }
    return bizEntity;
  }

  protected Collection findBizEntitiesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntitiesByFilter] Enter");

      results = _bizRegMgr.findBusinessEntities(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntitiesByFilter]", ex);
      assertTrue("Error in _bizRegMgr.findBusinessEntities", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntitiesByFilter] Exit");
    }
    return results;
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    _sessionMgr.authSession(session, USER_ID);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  protected void purgeSessions()
  {
    try
    {
      _sessionMgr.deleteSessions(new Date());
    }
    catch (Exception ex)
    {

    }

  }

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
  }

  protected BasicEventResponse performEvent(
    IEJBAction action, IEvent event, String session, StateMachine sm)
    throws Exception
  {
    sm.setAttribute(IAttributeKeys.SESSION_ID, session);
    sm.setAttribute(IAttributeKeys.USER_ID, USER_ID);
    sm.setAttribute(IAttributeKeys.ENTERPRISE_ID, ENTERPRISE);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    //check response
    assertNotNull("response is null", response);
    assertTrue("event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertEquals("Error type is incorrect",
      ApplicationException.APPLICATION, response.getErrorType());
  }

  protected void assertResponsePass(BasicEventResponse response, short msgCode)
  {
    assertNotNull("response is null", response);
    assertTrue("event is not successful", response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
  }

  protected void checkFail(
    IEvent event, String session, StateMachine sm, boolean eventEx,
    short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[ActionTestHelper.checkFail]" +
        " Returning fail due to EventException: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected event exception caught: "+ex.getMessage(), false);

      Log.log("TEST", "[ActionTestHelper.checkFail] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkFail] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponseFail(response, msgCode);
  }

  protected void checkSuccess(
    IEvent event, String session, StateMachine sm, short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkSuccess] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, msgCode);

    checkActionEffect(response, event, sm);
  }

  protected void checkBe(BusinessEntity origin, Map beMap)
  {
    //check be information
    assertEquals("UID is incorrect", origin.getKey(), beMap.get(BusinessEntity.UID));
    assertEquals("Id incorrect", origin.getBusEntId(), beMap.get(BusinessEntity.ID));
    assertEquals("Description incorrect", origin.getDescription(), beMap.get(BusinessEntity.DESCRIPTION));
    assertEquals("Enterprise is incorrect", origin.getEnterpriseId(), beMap.get(BusinessEntity.ENTERPRISE_ID));
    assertEquals("IsPartner is incorrect", new Boolean(origin.isPartner()), beMap.get(BusinessEntity.IS_PARTNER));
    assertEquals("State is incorrect", new Integer(origin.getState()), beMap.get(BusinessEntity.STATE));
    assertEquals("CanDelete is incorrect", new Boolean(origin.canDelete()), beMap.get(BusinessEntity.CAN_DELETE));

  }

  protected void checkWp(WhitePage origin, Map wpMap, boolean checkUID)
  {
    //check white page
    if (checkUID)
      assertEquals("UID in whitePage incorrect", origin.getKey(), wpMap.get(WhitePage.UID));
    assertEquals("BEUID is incorrect", origin.getBeUId(), wpMap.get(WhitePage.BE_UID));
    assertEquals("Biz Description is incorrect", origin.getBusinessDesc(), wpMap.get(WhitePage.BUSINESS_DESC));
    assertEquals("DUNS is incorrect", origin.getDUNS(), wpMap.get(WhitePage.DUNS));
    assertEquals("GCCC is incorrect", origin.getGlobalSupplyChainCode(), wpMap.get(WhitePage.G_SUPPLY_CHAIN_CODE));
    assertEquals("ContactPerson is incorrect", origin.getContactPerson(), wpMap.get(WhitePage.CONTACT_PERSON));
    assertEquals("Email is incorrect", origin.getEmail(),wpMap.get(WhitePage.EMAIL));
    assertEquals("Tel is incorrect", origin.getTel(), wpMap.get(WhitePage.TEL));
    assertEquals("Fax is incorrect", origin.getFax(), wpMap.get(WhitePage.FAX));
    assertEquals("Website is incorrect", origin.getWebsite(), wpMap.get(WhitePage.WEBSITE));
    assertEquals("Address is incorrect", origin.getAddress(), wpMap.get(WhitePage.ADDRESS));
    assertEquals("City is incorrect", origin.getCity(), wpMap.get(WhitePage.CITY));
    assertEquals("State is incorrect", origin.getState(), wpMap.get(WhitePage.STATE));
    assertEquals("ZipCode is incorrect", origin.getZipCode(), wpMap.get(WhitePage.ZIP_CODE));
    assertEquals("POBox is incorrect", origin.getPOBox(), wpMap.get(WhitePage.PO_BOX));
    assertEquals("Country is incorrect", origin.getCountry(), wpMap.get(WhitePage.COUNTRY));
    assertEquals("Language is incorrect", origin.getLanguage(), wpMap.get(WhitePage.LANGUAGE));
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}