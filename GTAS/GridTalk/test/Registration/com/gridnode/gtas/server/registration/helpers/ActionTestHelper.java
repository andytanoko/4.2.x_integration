/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.gtas.server.registration.actions.ConfirmRegistrationInfoAction;
import com.gridnode.gtas.events.registration.ConfirmRegistrationInfoEvent;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.events.registration.ValidateRegistrationInfoEvent;
import com.gridnode.gtas.server.registration.actions.ValidateRegistrationInfoAction;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.coyprofile.model.*;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerObj;
import com.gridnode.pdip.app.license.facade.ejb.ILicenseManagerObj;
import com.gridnode.pdip.app.license.model.License;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;

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
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action Test cases
 * of the Registration module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */

public abstract class ActionTestHelper extends TestCase
{
  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_STRING   = "XXX";
  protected static final String DUMMY_PK       = "XXXXXX";
  protected static final String CAT_CODE       = "GNL";
  protected static final Integer GRIDNODE_ID   = new Integer(521);
  protected static final Integer DUMMY_GNODE_ID= new Integer(-9999);
  protected static final String GRIDNODE_NAME  = "AmpWay Inc";
  protected static final String PROD_KEY_F1    = "RLYMM";
  protected static final String PROD_KEY_F2    = "XD4KKK";
  protected static final String PROD_KEY_F3    = "NN7HN";
  protected static final String PROD_KEY_F4    = "5I3KDX";
  protected static final String PASSWORD       = "password";
  private static final String COMPANY_NAME = "Some company name";
  private static final DataFilterImpl[] DELETE_FILTERS = new DataFilterImpl[3];

  static
  {
    //CompanyProfile
    DELETE_FILTERS[0] = new DataFilterImpl();
    DELETE_FILTERS[0].addSingleFilter(null, CompanyProfile.COY_NAME,
      DELETE_FILTERS[0].getLikeOperator(), COMPANY_NAME, false);

    //GridNode&ConnectionStatus
    DELETE_FILTERS[1] = new DataFilterImpl();
    DELETE_FILTERS[1].addSingleFilter(null, GridNode.NAME,
      DELETE_FILTERS[1].getLikeOperator(), GRIDNODE_NAME, false);

    //License
    DELETE_FILTERS[2] = new DataFilterImpl();
    DELETE_FILTERS[2].addSingleFilter(null, License.PRODUCT_KEY,
      DELETE_FILTERS[2].getLikeOperator(), PROD_KEY_F1+PROD_KEY_F2+PROD_KEY_F3+PROD_KEY_F4, false);
  }

  protected IGridNodeManagerObj _gnMgr;
  protected ICoyProfileManagerObj _coyMgr;
  protected ISessionManagerObj _sessionMgr;
  protected ILicenseManagerObj _licenseMgr;
  protected IRegistrationServiceObj _regService;
  protected ICertificateManagerObj _certMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";
  protected static final String ENTERPRISE = "888888";
  protected static final String USER_ID   = "mockuserid";

  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected Long[]              _uIDs;
  protected CompanyProfile[]    _coyProfiles;
  //protected GnCategory[]        _categories;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _coyMgr = ServiceLookupHelper.getCoyProfileManager();
      _gnMgr  = ServiceLookupHelper.getGridNodeManager();
      _sessionMgr = getSessionMgr();
      _licenseMgr = ServiceLookupHelper.getLicenseManager();
      _regService = getRegService();
      _certMgr = getCertificateMgr();

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

  protected void createCoyProfiles(int numPartner) throws Exception
  {
    _coyProfiles = new CompanyProfile[numPartner];
    _uIDs = new Long[numPartner];
    for (int i=0; i<numPartner; i++)
    {
      _uIDs[i] = createCoyProfile(true, i, true);
      _coyProfiles[i] = findCoyProfileByUId(_uIDs[i]);
    }
  }

  public void testPerform() throws Exception
  {
    Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
    prepareTestData();

    try
    {
      unitTest();
    }
    finally
    {
      cleanTestData();
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

  protected IRegistrationServiceObj getRegService() throws Exception
  {
    IRegistrationServiceHome regHome = (IRegistrationServiceHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         IRegistrationServiceHome.class.getName(),
                                         IRegistrationServiceHome.class);
    return regHome.create();
  }

  protected ICertificateManagerObj getCertificateMgr() throws Exception
  {
    ICertificateManagerHome certHome = (ICertificateManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ICertificateManagerHome.class.getName(),
                                         ICertificateManagerHome.class);
    return certHome.create();
  }

  protected HashMap getCreateCoyMap(CompanyProfile prof)
  {
    HashMap coyProf = new HashMap();
    coyProf.put(CompanyProfile.ADDRESS, prof.getAddress());
    coyProf.put(CompanyProfile.ALT_EMAIL, prof.getAltEmail());
    coyProf.put(CompanyProfile.ALT_TEL, prof.getAltTel());
    coyProf.put(CompanyProfile.CITY, prof.getCity());
    coyProf.put(CompanyProfile.COUNTRY, prof.getCountry());
    coyProf.put(CompanyProfile.COY_NAME, prof.getCoyName());
    coyProf.put(CompanyProfile.EMAIL, prof.getEmail());
    coyProf.put(CompanyProfile.FAX, prof.getFax());
    coyProf.put(CompanyProfile.LANGUAGE, prof.getLanguage());
    coyProf.put(CompanyProfile.STATE, prof.getState());
    coyProf.put(CompanyProfile.TEL, prof.getTel());
    coyProf.put(CompanyProfile.ZIP_CODE, prof.getZipCode());
    coyProf.put(CompanyProfile.IS_PARTNER, prof.isPartner());
    coyProf.put(CompanyProfile.CAN_DELETE, new Boolean(prof.canDelete()));
    return coyProf;
  }

  protected HashMap getUpdCoyMap(CompanyProfile prof)
  {
    HashMap coyProf = new HashMap();
    coyProf.put(CompanyProfile.ADDRESS, "upd " + prof.getAddress());
    coyProf.put(CompanyProfile.ALT_EMAIL,"UPD "+ prof.getAltEmail());
    coyProf.put(CompanyProfile.ALT_TEL, prof.getAltTel());
    coyProf.put(CompanyProfile.CITY,"upd "+prof.getCity());
    coyProf.put(CompanyProfile.COUNTRY,"CHN");
    coyProf.put(CompanyProfile.COY_NAME, prof.getCoyName() + "(Updated)");
    coyProf.put(CompanyProfile.EMAIL, "upd" + prof.getEmail());
    coyProf.put(CompanyProfile.FAX, "212423432");
    coyProf.put(CompanyProfile.LANGUAGE, "ZH");
    coyProf.put(CompanyProfile.STATE, "updST");
    coyProf.put(CompanyProfile.TEL, "23432432");
    coyProf.put(CompanyProfile.ZIP_CODE, "254323");
    coyProf.put(CompanyProfile.IS_PARTNER, prof.isPartner());
    coyProf.put(CompanyProfile.CAN_DELETE, new Boolean(prof.canDelete()));
    coyProf.put(CompanyProfile.UID, prof.getKey());

    return coyProf;
  }

  protected HashMap getDummyCoyMap(CompanyProfile prof)
  {
    HashMap map = getUpdCoyMap(prof);
    map.put(CompanyProfile.UID, DUMMY_UID);

    return map;
  }

  protected void cleanUpCoys()
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.cleanUpCoys] Enter");

      CompanyProfile prof = _coyMgr.findMyCompanyProfile();
      if (prof != null)
      {
        prof.setCanDelete(true);
        _coyMgr.updateCompanyProfile(prof);
      }

      Collection results = _coyMgr.findCompanyProfilesKeys(DELETE_FILTERS[0]);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _coyMgr.deleteCompanyProfile((Long)i.next());
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.cleanUpCoys] Error", ex);
    }
  }

  protected void cleanUpGridNodes()
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.cleanUpGridNodes] Enter");

      Collection results = _gnMgr.findGridNodesKeys(DELETE_FILTERS[1]);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _gnMgr.deleteGridNode((Long)i.next());
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.cleanUpGridNodes] Error", ex);
    }
  }

  protected void cleanUpLicenses()
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.cleanUpLicenses] Enter");

      Collection results = _licenseMgr.findLicensesKeys(DELETE_FILTERS[2]);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _licenseMgr.deleteLicense((Long)i.next());
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.cleanUpLicenses] Error", ex);
    }
  }

  protected Long createCoyProfile(boolean isPartner, int variable, boolean canDelete)
    throws Exception
  {
    CompanyProfile prof = createDefaultCoyProfile(isPartner, variable, canDelete);

    try
    {
      return createToDb(prof);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createCoyProfile] Error", t);
      assertTrue("Unable to create Coy Profile", false);
    }
    return null;
  }

  protected CompanyProfile createDefaultCoyProfile(
    boolean isPartner, int variable, boolean canDelete)
  {
    CompanyProfile coyProfile = new CompanyProfile();

    coyProfile.setCoyName(COMPANY_NAME + variable);
    coyProfile.setAltEmail("altemail@email.com");
    coyProfile.setAltTel("1321435325");
    coyProfile.setEmail("email@email.com");
    coyProfile.setTel("12234444");
    coyProfile.setFax("232434324");
    coyProfile.setAddress("some address");
    coyProfile.setCity("some city");
    coyProfile.setState("STATE");
    coyProfile.setZipCode("232444");
    coyProfile.setCountry("USA");
    coyProfile.setLanguage("EN");
    coyProfile.setPartner(new Boolean(isPartner));
    coyProfile.setCanDelete(canDelete);

    return coyProfile;
  }

  private Long createToDb(CompanyProfile coyProfile) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Enter");

      uID = _coyMgr.createCompanyProfile(coyProfile);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Exit");
    }
    return uID;
  }

  private void updateToDb(CompanyProfile coyProfile) throws Throwable
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Enter");
      _coyMgr.updateCompanyProfile(coyProfile);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Exit");
    }

  }

  private CompanyProfile findCoyProfileByUId(Long uId)
  {
    CompanyProfile coyProfile = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findCoyProfileByUId] Enter");

      coyProfile = _coyMgr.findCompanyProfile(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findCoyProfileByUId]", ex);
      assertTrue("Error in findCoyProfileByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findCoyProfileByUId] Exit");
    }
    return coyProfile;
  }

  private Collection findCoyProfilesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findCoyProfilesByFilter] Enter");

      results = _coyMgr.findCompanyProfiles(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findCoyProfilesByFilter]", ex);
      assertTrue("Error in _coyProfileMgr.findCompanyProfiless", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findCoyProfilesByFilter] Exit");
    }
    return results;
  }

  protected CompanyProfile getMyCoyProfile()
  {
    CompanyProfile coyProfile = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.getMyCoyProfile] Enter");

      coyProfile = _gnMgr.getMyCompanyProfile();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getMyCoyProfile]", ex);
      assertTrue("Error in getMyCoyProfile", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.getMyCoyProfile] Exit");
    }
    return coyProfile;
  }

  protected GridNode findGridNodeByNodeID(String nodeID)
  {
    GridNode gridnode = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findGridNodeByNodeID] Enter");

      gridnode = _gnMgr.findGridNodeByID(nodeID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findGridNodeByNodeID]", ex);
      assertTrue("Error in findGridNodeByNodeID", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findGridNodeByNodeID] Exit");
    }
    return gridnode;
  }

  protected ConnectionStatus findConnectionStatusByNodeID(String nodeID)
  {
    ConnectionStatus status = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findConnectionStatusByNodeID] Enter");

      status = _gnMgr.findConnectionStatusByNodeID(nodeID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findConnectionStatusByNodeID]", ex);
      assertTrue("Error in findConnectionStatusByNodeID", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findConnectionStatusByNodeID] Exit");
    }
    return status;
  }

  protected License findLicenseByProductKey(String prodKey)
  {
    License license = null;

    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, License.PRODUCT_KEY, filter.getEqualOperator(),
        prodKey, false);

      Object[] results = _licenseMgr.findLicenses(filter).toArray();
      license = (License)results[0];
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.findLicenseByProductKey]", t);
      assertTrue("Error in findLicenseByProductKey", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findLicenseByProductKey] Exit");
    }

    return license;
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

  protected void checkCoy(CompanyProfile origin, Map coyMap)
  {
    assertEquals("CoyName is incorrect", origin.getCoyName(), coyMap.get(CompanyProfile.COY_NAME));
    assertEquals("AltEmail is incorrect", origin.getAltEmail(), coyMap.get(CompanyProfile.ALT_EMAIL));
    assertEquals("AltTel is incorrect", origin.getAltTel(), coyMap.get(CompanyProfile.ALT_TEL));
    assertEquals("Email is incorrect", origin.getEmail(), coyMap.get(CompanyProfile.EMAIL));
    assertEquals("Tel is incorrect", origin.getTel(), coyMap.get(CompanyProfile.TEL));
    assertEquals("Fax is incorrect", origin.getFax(), coyMap.get(CompanyProfile.FAX));
    assertEquals("Address is incorrect", origin.getAddress(), coyMap.get(CompanyProfile.ADDRESS));
    assertEquals("City is incorrect", origin.getCity(), coyMap.get(CompanyProfile.CITY));
    assertEquals("State is incorrect", origin.getState(), coyMap.get(CompanyProfile.STATE));
    assertEquals("ZipCode is incorrect", origin.getZipCode(), coyMap.get(CompanyProfile.ZIP_CODE));
    assertEquals("IsPartner is incorrect", origin.isPartner(), coyMap.get(CompanyProfile.IS_PARTNER));
    assertEquals("Country is incorrect", origin.getCountry(), coyMap.get(CompanyProfile.COUNTRY));
    assertEquals("Language is incorrect", origin.getLanguage(), coyMap.get(CompanyProfile.LANGUAGE));
    assertEquals("CanDelete is incorrect", new Boolean(origin.canDelete()), coyMap.get(CompanyProfile.CAN_DELETE));
  }

  protected void checkCoy(Map origin, Map coyMap)
  {
    assertEquals("CoyName is incorrect", origin.get(CompanyProfile.COY_NAME), coyMap.get(CompanyProfile.COY_NAME));
    assertEquals("AltEmail is incorrect", origin.get(CompanyProfile.ALT_EMAIL), coyMap.get(CompanyProfile.ALT_EMAIL));
    assertEquals("AltTel is incorrect", origin.get(CompanyProfile.ALT_TEL), coyMap.get(CompanyProfile.ALT_TEL));
    assertEquals("Email is incorrect", origin.get(CompanyProfile.EMAIL), coyMap.get(CompanyProfile.EMAIL));
    assertEquals("Tel is incorrect", origin.get(CompanyProfile.TEL), coyMap.get(CompanyProfile.TEL));
    assertEquals("Fax is incorrect", origin.get(CompanyProfile.FAX), coyMap.get(CompanyProfile.FAX));
    assertEquals("Address is incorrect", origin.get(CompanyProfile.ADDRESS), coyMap.get(CompanyProfile.ADDRESS));
    assertEquals("City is incorrect", origin.get(CompanyProfile.CITY), coyMap.get(CompanyProfile.CITY));
    assertEquals("State is incorrect", origin.get(CompanyProfile.STATE), coyMap.get(CompanyProfile.STATE));
    assertEquals("ZipCode is incorrect", origin.get(CompanyProfile.ZIP_CODE), coyMap.get(CompanyProfile.ZIP_CODE));
    assertEquals("IsPartner is incorrect", origin.get(CompanyProfile.IS_PARTNER), coyMap.get(CompanyProfile.IS_PARTNER));
    assertEquals("Country is incorrect", origin.get(CompanyProfile.COUNTRY), coyMap.get(CompanyProfile.COUNTRY));
    assertEquals("Language is incorrect", origin.get(CompanyProfile.LANGUAGE), coyMap.get(CompanyProfile.LANGUAGE));
    assertEquals("CanDelete is incorrect", origin.get(CompanyProfile.CAN_DELETE), coyMap.get(CompanyProfile.CAN_DELETE));
  }

  protected void checkDate(Date expDate, Date date, String prefix)
  {
    if (expDate == null)
    {
      assertNull(prefix+"is not null", date);
      return;
    }
    GregorianCalendar cal1 = new GregorianCalendar();
    GregorianCalendar cal2 = new GregorianCalendar();

    cal1.setTime(expDate);
    cal2.setTime(date);

    assertEquals(prefix+" Year is incorrect", cal1.get(cal1.YEAR), cal2.get(cal2.YEAR));
    assertEquals(prefix+" Month is incorrect", cal1.get(cal1.MONTH), cal2.get(cal2.MONTH));
    assertEquals(prefix+" Day is incorrect", cal1.get(cal1.DATE), cal2.get(cal2.DATE));
  }

  protected void initRegistration(
    Integer gridnodeID, String gridnodeName, String prodKeyF1,
    String prodKeyF2, String prodKeyF3, String prodKeyF4,
    Map profMap, String session, StateMachine sm) throws Exception
  {
    ValidateRegistrationInfoEvent event = new ValidateRegistrationInfoEvent(
      gridnodeID, gridnodeName, /*prodKeyF1, prodKeyF2,
      prodKeyF3, prodKeyF4, */ null, profMap);

    ValidateRegistrationInfoAction action = new ValidateRegistrationInfoAction();
    performEvent(action, event, session, sm);
  }

  protected void confirmRegistration(
    String password, String session, StateMachine sm) throws Exception
  {
    ConfirmRegistrationInfoEvent event = new ConfirmRegistrationInfoEvent(password);

    ConfirmRegistrationInfoAction action = new ConfirmRegistrationInfoAction();
    performEvent(action, event, session, sm);
  }

  protected void undoRegistration() throws Exception
  {
    _regService.undoRegistration();
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}