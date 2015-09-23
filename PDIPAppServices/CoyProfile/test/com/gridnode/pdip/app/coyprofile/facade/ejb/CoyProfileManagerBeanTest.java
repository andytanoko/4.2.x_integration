/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CoyProfileManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.facade.ejb;

import com.gridnode.pdip.app.coyprofile.model.*;

import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.*;

import java.io.*;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedList;
import java.sql.Timestamp;

/**
 * Test case for testing CoyProfileManagerBean<P>
 *
 * This tests the following business methods in the CoyProfileManagerBean:<P>
 * <LI>createCompanyProfile(CompanyProfile)</LI>
 * <LI>updateCompanyProfile(CompanyProfile)</LI>
 * <P>
 *
 * The following business methods are implicitly tested by this test case:<P>
 * <LI>findCompanyProfile(UID)</LI>
 * <LI>findCompanyProfiless(DataFilter)</LI>
 * <LI>deleteCompanyProfile(UID)</LI>
 * <P>
 *
 * No test cases for the following methods in the CoyProfileManagerBean:<P>
 * <LI>findCompanyProfilessKeys(DataFilter)</LI>
 * <LI>findCompanyProfilessKeys(IDataFilter filter)</LI>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CoyProfileManagerBeanTest
  extends    TestCase
{

  private static final String COMPANY_NAME = "Some company name";
  private static final DataFilterImpl DELETE_FILTER = new DataFilterImpl();
  static
  {
    DELETE_FILTER.addSingleFilter(null, CompanyProfile.COY_NAME,
      DELETE_FILTER.getLikeOperator(), COMPANY_NAME, false);
  }
  private ICoyProfileManagerObj     _coyProfileMgr;
  private CompanyProfile[] _coyProfiles;
  private Long[] _uIDs;

  public CoyProfileManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CoyProfileManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.setUp] Enter");

      _coyProfileMgr = getCoyProfileMgr();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CoyProfileManagerBeanTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[CoyProfileManagerBeanTest.tearDown] Exit");
  }


  // *********************** test cases **************************** //

  public void testFunctions() throws Throwable
  {
    prepareTestData();
    managementFunctionsTest();
    cleanupTestData();
  }

  private void prepareTestData()
  {
    _coyProfiles = new CompanyProfile[]
      {
        createDefaultCoyProfile(false, 1, false),
        createDefaultCoyProfile(true, 2, true),
        createDefaultCoyProfile(true, 3, true),
        createDefaultCoyProfile(true, 4, false),
      };
  }

  private void cleanupTestData()
  {
    CompanyProfile coyProfile = null;
    for (int i=0; i<_uIDs.length; i++)
    {
      try
      {
        coyProfile = findCoyProfileByUId(_uIDs[i]);
        coyProfile.setCanDelete(true);
        updateToDb(coyProfile);
      }
      catch (Throwable ex)
      {
      }
    }

  }

  // *************** Management **********************************
  private void managementFunctionsTest() throws Throwable
  {
    _uIDs = new Long[_coyProfiles.length];

    //Create
    for (int i=0; i<_uIDs.length; i++)
    {
      _uIDs[i] = checkCreatePass(_coyProfiles[i]);
    }

    //create fail: duplicate (only one Non-partner allowed)
    checkCreateFail(_coyProfiles[0]);

    //update
    CompanyProfile coyProfile = findCoyProfileByUId(_uIDs[1]);
    updateCoyProfile(coyProfile);
    checkUpdatePass(coyProfile);


    //mark delete
    checkDeletePass(_uIDs[2]);
    checkDeleteFail(_uIDs[3]);
  }

  private void checkDeletePass(Long uID)
  {
    try
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkDeletePass] Enter");
      _coyProfileMgr.deleteCompanyProfile(uID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CoyProfileManagerBeanTest.checkDeletePass]", ex);
      assertTrue("Error in _coyProfileMgr.deleteCompanyProfile", false);
    }

    //check that state is correct
    try
    {
      CompanyProfile coyProfile = findCoyProfileByUId(uID);
    }
    catch (AssertionFailedError ex)
    {
      return;
    }
    catch (Throwable e)
    {
    }
    assertTrue("CoyProfile not deleted!", false);
  }


  private void checkDeleteFail(Long uID)
  {
    //mark delete
    try
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkDeleteFail] Enter");
      _coyProfileMgr.deleteCompanyProfile(uID);
      assertTrue("Delete not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkDeleteFail] "+ex.getMessage());
    }
  }

  private void checkUpdateFail(CompanyProfile mockCoy)
  {
    try
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkUpdateFail] Enter");
      updateToDb(mockCoy);
      assertTrue("Update not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkUpdateFail] "+ex.getMessage());
    }
  }

  private void checkUpdatePass(CompanyProfile mockCoy) throws Throwable
  {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkUpdatePass] Enter");

    //updates
    updateToDb(mockCoy);

    CompanyProfile updated = findCoyProfileByUId((Long)mockCoy.getKey());

    //check white page
    assertEquals("UID in CoyProfile incorrect", mockCoy.getKey(), updated.getKey());
    assertEquals("CoyName is incorrect", mockCoy.getCoyName(), updated.getCoyName());
    assertEquals("AltEmail is incorrect", mockCoy.getAltEmail(), updated.getAltEmail());
    assertEquals("AltTel is incorrect", mockCoy.getAltTel(), updated.getAltTel());
    assertEquals("Email is incorrect", mockCoy.getEmail(), updated.getEmail());
    assertEquals("Tel is incorrect", mockCoy.getTel(), updated.getTel());
    assertEquals("Fax is incorrect", mockCoy.getFax(), updated.getFax());
    assertEquals("Address is incorrect", mockCoy.getAddress(), updated.getAddress());
    assertEquals("City is incorrect", mockCoy.getCity(), updated.getCity());
    assertEquals("State is incorrect", mockCoy.getState(), updated.getState());
    assertEquals("ZipCode is incorrect", mockCoy.getZipCode(), updated.getZipCode());
    assertEquals("IsPartner is incorrect", mockCoy.isPartner(), updated.isPartner());
    assertEquals("Country is incorrect", mockCoy.getCountry(), updated.getCountry());
    assertEquals("Language is incorrect", mockCoy.getLanguage(), updated.getLanguage());
    assertEquals("CanDelete is incorrect", mockCoy.canDelete(), updated.canDelete());
  }

  private void checkCreateFail(CompanyProfile mockCoy)
  {
    try
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkCreateFail] Enter");
      createToDb(mockCoy);
      assertTrue("Create not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.checkCreateFail] "+ex.getMessage());
    }
  }

  private Long checkCreatePass(CompanyProfile mockCoy) throws Throwable
  {
    Log.debug("TEST", "[CoyProfileManagerBeanTest.checkCreatePass] Enter");
    Long uID = createToDb(mockCoy);

    //check retrieved record
    CompanyProfile created = findCoyProfileByUId(uID);

    assertNotNull("CoyProfile retrieved is null", created);

    //check white page
    assertNotNull("UID in CoyProfile is null", created.getKey());
    assertEquals("CoyName is incorrect", mockCoy.getCoyName(), created.getCoyName());
    assertEquals("AltEmail is incorrect", mockCoy.getAltEmail(), created.getAltEmail());
    assertEquals("AltTel is incorrect", mockCoy.getAltTel(), created.getAltTel());
    assertEquals("Email is incorrect", mockCoy.getEmail(), created.getEmail());
    assertEquals("Tel is incorrect", mockCoy.getTel(), created.getTel());
    assertEquals("Fax is incorrect", mockCoy.getFax(), created.getFax());
    assertEquals("Address is incorrect", mockCoy.getAddress(), created.getAddress());
    assertEquals("City is incorrect", mockCoy.getCity(), created.getCity());
    assertEquals("State is incorrect", mockCoy.getState(), created.getState());
    assertEquals("ZipCode is incorrect", mockCoy.getZipCode(), created.getZipCode());
    assertEquals("IsPartner is incorrect", mockCoy.isPartner(), created.isPartner());
    assertEquals("Country is incorrect", mockCoy.getCountry(), created.getCountry());
    assertEquals("Language is incorrect", mockCoy.getLanguage(), created.getLanguage());
    assertEquals("CanDelete is incorrect", mockCoy.canDelete(), created.canDelete());
    return uID;
  }

  // ************** Finders ******************************************



  // ******************  utility methods ****************************
  private ICoyProfileManagerObj getCoyProfileMgr() throws Exception
  {
    ICoyProfileManagerHome coyProfileHome = (ICoyProfileManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ICoyProfileManagerHome.class.getName(),
                                         ICoyProfileManagerHome.class);
    return coyProfileHome.create();
  }


  private void cleanUp() throws Exception
  {
    try
    {
      Log.debug("TEST", "[CoyProfileManagerBeanTest.cleanUp] Enter");
      Collection results = _coyProfileMgr.findCompanyProfilesKeys(DELETE_FILTER);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _coyProfileMgr.deleteCompanyProfile((Long)i.next());
      }
    }
    catch (Exception ex)
    {

    }

  }

  private CompanyProfile createDefaultCoyProfile(
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

  protected static void updateCoyProfile(CompanyProfile coyProfile)
  {
    coyProfile.setCoyName(coyProfile.getCoyName() + "(Updated)");
    coyProfile.setAltEmail("UPD "+coyProfile.getAltEmail());
    coyProfile.setAltTel("UPD "+coyProfile.getAltTel());
    coyProfile.setEmail("upd" + coyProfile.getEmail());
    coyProfile.setTel("23432432");
    coyProfile.setFax("212423432");
    coyProfile.setAddress("upd " + coyProfile.getAddress());
    coyProfile.setCity("upd "+coyProfile.getCity());
    coyProfile.setState("updST");
    coyProfile.setZipCode("254323");
    coyProfile.setCountry("CHN");
    coyProfile.setLanguage("ZH");
    coyProfile.setPartner(coyProfile.isPartner());
    coyProfile.setCanDelete(coyProfile.canDelete());
  }

  private Long createToDb(CompanyProfile coyProfile) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.createToDb] Enter");

      uID = _coyProfileMgr.createCompanyProfile(coyProfile);
    }
    finally
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.createToDb] Exit");
    }
    return uID;
  }

  private void updateToDb(CompanyProfile coyProfile) throws Throwable
  {
    try
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.updateToDb] Enter");
      _coyProfileMgr.updateCompanyProfile(coyProfile);
    }
    finally
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.updateToDb] Exit");
    }

  }

  private CompanyProfile findCoyProfileByUId(Long uId)
  {
    CompanyProfile coyProfile = null;
    try
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.findCoyProfileByUId] Enter");

      coyProfile = _coyProfileMgr.findCompanyProfile(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CoyProfileManagerBeanTest.findCoyProfileByUId]", ex);
      assertTrue("Error in findCoyProfileByUId", false);
    }
    finally
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.findCoyProfileByUId] Exit");
    }
    return coyProfile;
  }

  private Collection findCoyProfilesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.findCoyProfilesByFilter] Enter");

      results = _coyProfileMgr.findCompanyProfiles(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CoyProfileManagerBeanTest.findCoyProfilesByFilter]", ex);
      assertTrue("Error in _coyProfileMgr.findCompanyProfiless", false);
    }
    finally
    {
      Log.log("TEST", "[CoyProfileManagerBeanTest.findCoyProfilesByFilter] Exit");
    }
    return results;
  }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


}