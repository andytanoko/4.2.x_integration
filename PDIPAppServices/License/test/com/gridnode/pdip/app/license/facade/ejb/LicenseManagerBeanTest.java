/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.license.facade.ejb;

import java.util.GregorianCalendar;
import com.gridnode.pdip.app.license.model.*;

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
 * Test case for testing LicenseManagerBean<P>
 *
 * This tests the following business methods in the LicenseManagerBean:<P>
 * <LI>createLicense(License)</LI>
 * <LI>revokeLicense(License)</LI>
 * <LI>findValidLicense(UID)</LI>
 * <LI>findValidLicense(ProductName,ProductVersion)</LI>
 * <P>
 *
 * The following business methods are implicitly tested by this test case:<P>
 * <LI>findLicense(UID)</LI>
 * <LI>findLicenses(DataFilter)</LI>
 * <LI>deleteLicense(UID)</LI>
 * <P>
 *
 * No test cases for the following methods in the LicenseManagerBean:<P>
 * <LI>findLicensesKeys(DataFilter)</LI>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LicenseManagerBeanTest
  extends    TestCase
{
  private static final String PRODUCT_NAME = "Some product name";
  private static final DataFilterImpl DELETE_FILTER = new DataFilterImpl();
  private static final Long DUMMY_UID = new Long(-999);
  static
  {
    DELETE_FILTER.addSingleFilter(null, License.PRODUCT_NAME,
      DELETE_FILTER.getLikeOperator(), PRODUCT_NAME, false);
  }
  private ILicenseManagerObj     _licenseMgr;
  private License[] _licenses;
  private Long[] _uIDs;

  public LicenseManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(LicenseManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.setUp] Enter");

      _licenseMgr = getLicenseMgr();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[LicenseManagerBeanTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[LicenseManagerBeanTest.tearDown] Exit");
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
    Timestamp ts = new Timestamp(System.currentTimeMillis());

    Date[] start = new Date[3];
    Date[] end   = new Date[3];

    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(ts);
    cal.set(cal.get(cal.YEAR), cal.get(cal.MONTH), cal.get(cal.DATE),
      0, 0, 0);

    start[0] = cal.getTime();
    cal.roll(cal.YEAR, 1);
    end[0] = cal.getTime();

    end[1] = start[0];
    cal.set(cal.YEAR, cal.get(cal.YEAR)-2);
    start[1] = cal.getTime();

    start[2] = end[0];
    cal.set(cal.YEAR, cal.get(cal.YEAR)+4);
    end[2] = cal.getTime();

    _licenses = new License[]
      {
        //pass
        createLicense(1, start[0], end[0]),
        //expired
        createLicense(1, start[1], end[1]),
        //not commenced
        createLicense(2, start[2], end[2]),
       //invalid period
        createLicense(2, end[0], start[0]),
      };
  }

  private void cleanupTestData()
  {
  }

  // *************** Management **********************************
  private void managementFunctionsTest() throws Throwable
  {
    _uIDs = new Long[_licenses.length];

    //Create
    for (int i=0; i<_uIDs.length-1; i++)
    {
      _uIDs[i] = checkCreatePass(_licenses[i]);
    }

    //invalid validity period
    checkCreateFail(_licenses[_licenses.length-1]);

    License mock = findLicenseByUId(_uIDs[0]);

    //find valid license by uid
    License valid = findValidLicense(_uIDs[0]);
    checkLicense(mock, valid);

    //find valid license by product name & version
    valid = findValidLicense(_licenses[0].getProductName(),
              _licenses[0].getProductVersion());
    checkLicense(mock, valid);

    //revoke
    checkRevokePass(_uIDs[0]);
    checkRevokeFail(DUMMY_UID);

  }

  private void checkRevokeFail(Long uID)
  {
    try
    {
      Log.debug("TEST", "[LicenseManagerBeanTest.checkRevokeFail] Enter");
      revokeLicense(uID);
      assertTrue("Update not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[LicenseManagerBeanTest.checkRevokeFail] "+ex.getMessage());
    }
  }

  private void checkRevokePass(Long uID) throws Throwable
  {
    Log.debug("TEST", "[LicenseManagerBeanTest.checkRevokePass] Enter");


    revokeLicense(uID);

    License license = findLicenseByUId(uID);

    //check the state
    assertEquals("State is not revoked", License.STATE_REVOKED, license.getState());
  }

  private void checkCreateFail(License mockLicense)
  {
    try
    {
      Log.debug("TEST", "[LicenseManagerBeanTest.checkCreateFail] Enter");
      createToDb(mockLicense);
      assertTrue("Create not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[LicenseManagerBeanTest.checkCreateFail] "+ex.getMessage());
    }
  }

  private Long checkCreatePass(License mockLicense) throws Throwable
  {
    Log.debug("TEST", "[LicenseManagerBeanTest.checkCreatePass] Enter");
    Long uID = createToDb(mockLicense);

    //check retrieved record
    License created = findLicenseByUId(uID);

    assertNotNull("License retrieved is null", created);

    assertNotNull("UID is null", created.getKey());
    assertEquals("ProductKey is incorrect", mockLicense.getProductKey(), created.getProductKey());
    assertEquals("ProductName is incorrect", mockLicense.getProductName(), created.getProductName());
    assertEquals("ProductVersion is incorrect", mockLicense.getProductVersion(), created.getProductVersion());
    assertEquals("StartDate is incorrect", mockLicense.getStartDate(), created.getStartDate());
    assertEquals("EndDate is incorrect", mockLicense.getEndDate(), created.getEndDate());
    Date current = new Date();
    int state;
    if (mockLicense.getStartDate().after(current))
      state = License.STATE_NOT_COMMENCED;
    else if (mockLicense.getEndDate().before(current))
      state = License.STATE_EXPIRED;
    else
      state = License.STATE_VALID;

    assertEquals("State is incorrect", state, created.getState());

    return uID;
  }

  private void checkLicense(License mockLicense, License check)
  {
    //check License
    assertEquals("UID is incorrect", mockLicense.getKey(), check.getKey());
    assertEquals("ProductKey is incorrect", mockLicense.getProductKey(), check.getProductKey());
    assertEquals("ProductName is incorrect", mockLicense.getProductName(), check.getProductName());
    assertEquals("ProductVersion is incorrect", mockLicense.getProductVersion(), check.getProductVersion());
    assertEquals("StartDate is incorrect", mockLicense.getStartDate().getTime(), check.getStartDate().getTime());
    assertEquals("EndDate is incorrect", mockLicense.getEndDate(), check.getEndDate());
    assertEquals("State is incorrect", mockLicense.getState(), check.getState());
  }

  // ************** Finders ******************************************

  private License findValidLicense(Long uId) throws Exception
  {
    License license = null;
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findValidLicense] Enter");

      license = _licenseMgr.findValidLicense(uId);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findValidLicense] Exit");
    }
    return license;
  }

  private License findValidLicense(String productName, String productVersion) throws Exception
  {
    License license = null;
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findValidLicense] Enter");

      license = _licenseMgr.findValidLicense(productName, productVersion);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findValidLicense] Exit");
    }
    return license;
  }

  // ******************  utility methods ****************************
  private ILicenseManagerObj getLicenseMgr() throws Exception
  {
    ILicenseManagerHome licenseHome = (ILicenseManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ILicenseManagerHome.class.getName(),
                                         ILicenseManagerHome.class);
    return licenseHome.create();
  }


  private void cleanUp() throws Exception
  {
    try
    {
      Log.debug("TEST", "[LicenseManagerBeanTest.cleanUp] Enter");
      Collection results = _licenseMgr.findLicensesKeys(DELETE_FILTER);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _licenseMgr.deleteLicense((Long)i.next());
      }
    }
    catch (Exception ex)
    {

    }

  }

  private License createLicense(
    int variable, Date start, Date end)
  {
    License license = new License();

    license.setProductKey("abcdefzyxwvutsrqpo");
    license.setProductName(PRODUCT_NAME + variable);
    license.setProductVersion("v"+variable);
    license.setStartDate(start);
    license.setEndDate(end);

    return license;
  }

  private Long createToDb(License license) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.createToDb] Enter");

      uID = _licenseMgr.createLicense(license);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.createToDb] Exit");
    }
    return uID;
  }

  private void revokeLicense(Long uid) throws Throwable
  {
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.revokeLicense] Enter");
      _licenseMgr.revokeLicense(uid);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.revokeLicense] Exit");
    }

  }

  private License findLicenseByUId(Long uId)
  {
    License license = null;
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findLicenseByUId] Enter");

      license = _licenseMgr.findLicense(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[LicenseManagerBeanTest.findLicenseByUId]", ex);
      assertTrue("Error in findLicenseByUId", false);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findLicenseByUId] Exit");
    }
    return license;
  }

  private Collection findLicensesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findLicensesByFilter] Enter");

      results = _licenseMgr.findLicenses(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[LicenseManagerBeanTest.findLicensesByFilter]", ex);
      assertTrue("Error in _licenseMgr.findLicensess", false);
    }
    finally
    {
      Log.log("TEST", "[LicenseManagerBeanTest.findLicensesByFilter] Exit");
    }
    return results;
  }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


}