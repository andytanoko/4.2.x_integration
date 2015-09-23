package com.gridnode.gtas.server.locale.helpers;

import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.base.locale.model.*;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerObj;
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
 * of the Locale module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */

public abstract class ActionTestHelper extends TestCase
{
  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_STRING   = "xxx";

  protected ILocaleManagerObj _localeMgr;
  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";
  protected static final String ENTERPRISE = "888888";
  protected static final String USER_ID   = "mockuserid";

//Japan,392,JP,JPN
  protected static final String CTY_ALP3 = "JPN";
  protected static final String LANG_ALP2 = "KA";
  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected CountryCode[]       _countries;
  protected LanguageCode[]      _languages;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _localeMgr = LocaleUtil.getLocaleManager();
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


  protected CountryCode findCountryCodeByAlpha3Code(String alpha3Code)
  {
    CountryCode country = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findCountryCodeByAlpha3Code] Enter");

      country = _localeMgr.findCountryCodeByAlpha3Code(alpha3Code);
      assertNotNull("CountryCode not exist", country);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findCountryCodeByAlpha3Code]", ex);
      assertTrue("Error in findCountryCodeByAlpha3Code", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findCountryCodeByAlpha3Code] Exit");
    }
    return country;
  }

  protected LanguageCode findLanguageCodeByAlpha2Code(String alpha2Code)
  {
    LanguageCode language = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findLanguageCodeByAlpha2Code] Enter");

      language = _localeMgr.findLanguageCodeByAlpha2Code(alpha2Code);
      assertNotNull("CountryCode not exist", language);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findLanguageCodeByAlpha2Code]", ex);
      assertTrue("Error in findLanguageCodeByAlpha2Code", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findLanguageCodeByAlpha2Code] Exit");
    }
    return language;
  }

//  protected CountryCode findBizEntity(String enterpriseId, String beId)
//  {
//    CountryCode country = null;
//    try
//    {
//      Log.log("TEST", "[ActionTestHelper.findBizEntity] Enter: enterpriseID="+enterpriseId);
//
//      country = _localeMgr.findCountryCode(enterpriseId, beId);
//    }
//    catch (Exception ex)
//    {
//      Log.err("TEST", "[ActionTestHelper.findBizEntity]", ex);
//      assertTrue("Error in _localeMgr.findCountryCode", false);
//    }
//    finally
//    {
//      Log.log("TEST", "[ActionTestHelper.findBizEntity] Exit");
//    }
//    return country;
//  }

  protected Collection findCountryCodesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findCountryCodesByFilter] Enter");

      results = _localeMgr.findCountryCodesByFilter(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findCountryCodesByFilter]", ex);
      assertTrue("Error in _localeMgr.findCountryCodesByFilter", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findCountryCodesByFilter] Exit");
    }
    return results;
  }

  protected Collection findLanguageCodesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findLanguageCodesByFilter] Enter");

      results = _localeMgr.findLanguageCodesByFilter(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findLanguageCodesByFilter]", ex);
      assertTrue("Error in _localeMgr.findLanguageCodesByFilter", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findLanguageCodesByFilter] Exit");
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

  protected void checkCountryCode(CountryCode origin, Map countryMap)
  {
    assertEquals("Alpha2Code is incorrect", origin.getAlpha2Code(), countryMap.get(CountryCode.ALPHA_2_CODE));
    assertEquals("Alpha3Code incorrect", origin.getAlpha3Code(), countryMap.get(CountryCode.ALPHA_3_CODE));
    assertEquals("Name is incorrect", origin.getName(), countryMap.get(CountryCode.NAME));
    assertEquals("NumericalCode is incorrect", origin.getNumericalCode(), countryMap.get(CountryCode.NUMERICAL_CODE));
  }

  protected void checkLanguageCode(LanguageCode origin, Map langMap)
  {
    assertEquals("Alpha2Code is incorrect", origin.getAlpha2Code(), langMap.get(LanguageCode.ALPHA_2_CODE));
    assertEquals("TAlpha3Code incorrect", origin.getTAlpha3Code(), langMap.get(LanguageCode.T_ALPHA_3_CODE));
    assertEquals("Name is incorrect", origin.getName(), langMap.get(LanguageCode.NAME));
    assertEquals("BAlpha3Code is incorrect", origin.getBAlpha3Code(), langMap.get(LanguageCode.B_ALPHA_3_CODE));
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}