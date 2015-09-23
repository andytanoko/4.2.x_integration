/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizRegistryManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import com.gridnode.pdip.app.bizreg.model.*;

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
 * Test case for testing BizRegistryManagerBean<P>
 *
 * This tests the following business methods in the BizRegistryManagerBean:<P>
 * <LI>createBusinessEntity(BusinessEntity)</LI>
 * <LI>updateBusinessEntity(BusinessEntity)</LI>
 * <LI>markDeleteBusinessEntity(UID)</LI>
 * <LI>markDeleteBusinessEntities(EnterpriseID)</LI>
 * <P>
 *
 * The following business methods are implicitly tested by this test case:<P>
 * <LI>findBusinessEntity(UID)</LI>
 * <LI>findBusinessEntities(DataFilter)</LI>
 * <LI>purgeDeletedEntities(EnterpriseId)</LI>
 * <P>
 *
 * No test cases for the following methods in the BizRegistryManagerBean:<P>
 * <LI>findBusinessEntitiesKeys(DataFilter)</LI>
 * <LI>findBusinessEntities(int state)</LI>
 * <LI>findBusinessEntitiesByWhitePage(IDataFilter filter)</LI>
 * <LI>findBusinessEntitiesKeys(IDataFilter filter)</LI>
 * <LI>findBusinessEntity(String enterpriseId, String beId)</LI>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class BizRegistryManagerBeanTest
  extends    TestCase
{
  private IBizRegistryManagerObj     _bizRegMgr;
  private BusinessEntity[] _bizEntities;
  private Long[] _uIDs;

  public BizRegistryManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(BizRegistryManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.setUp] Enter");

      _bizRegMgr = getBizRegistryMgr();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[BizRegistryManagerBeanTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[BizRegistryManagerBeanTest.tearDown] Exit");
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
    _bizEntities = new BusinessEntity[]
      {
        createDefaultBizEntity("ENT1", false, 1, true),
        createDefaultBizEntity("ENT1", false, 2, false),
        createDefaultBizEntity("ENT1", false, 3, true),
        createDefaultBizEntity("ENT2", false, 3, false),
      };
  }

  private void cleanupTestData()
  {
    BusinessEntity bizEntity = null;
    for (int i=0; i<_uIDs.length; i++)
    {
      try
      {
        bizEntity = findBizEntityByUId(_uIDs[i]);
        bizEntity.setCanDelete(true);
        updateToDb(bizEntity);
      }
      catch (Throwable ex)
      {
      }
    }

  }

  // *************** Management **********************************
  private void managementFunctionsTest() throws Throwable
  {
    _uIDs = new Long[_bizEntities.length];

    //Create
    for (int i=0; i<_uIDs.length; i++)
    {
      _uIDs[i] = checkCreatePass(_bizEntities[i]);
    }

    //create fail: duplicate
    checkCreateFail(_bizEntities[0]);

    //update
    BusinessEntity bizEntity = findBizEntityByUId(_uIDs[1]);
    updateBizEntity(bizEntity);
    checkUpdatePass(bizEntity);

    //update fail: duplicate
    bizEntity = findBizEntityByUId(_uIDs[2]);
    bizEntity.setEnterpriseId(_bizEntities[3].getEnterpriseId());
    checkUpdateFail(bizEntity);

    //mark delete
    checkMarkDeletePass(_uIDs[0]);
    checkMarkDeletePass("ENT1");
    checkMarkDeleteFail(_uIDs[3]);


  }

  private void checkMarkDeletePass(Long uID)
  {
    //mark delete
    try
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkMarkDeletePass] Enter");
      _bizRegMgr.markDeleteBusinessEntity(uID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[BizRegistryManagerBeanTest.checkMarkDeletePass]", ex);
      assertTrue("Error in _bizRegMgr.markDeleteBusinessEntity", false);
    }

    //check that state is correct
    BusinessEntity bizEntity = findBizEntityByUId(uID);
    assertEquals("State is not 'deleted'", BusinessEntity.STATE_DELETED,
      bizEntity.getState());
  }

  private void checkMarkDeletePass(String enterpriseId)
  {
    //mark delete
    try
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkMarkDeletePass] Enter");
      _bizRegMgr.markDeleteBusinessEntities(enterpriseId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[BizRegistryManagerBeanTest.checkMarkDeletePass]", ex);
      assertTrue("Error in _bizRegMgr.markDeleteBusinessEntities", false);
    }

    //check that state is correct
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ENTERPRISE_ID, filter.getEqualOperator(),
      enterpriseId, false);

    Collection deleted = findBizEntitiesByFilter(filter);
    for (Iterator i=deleted.iterator(); i.hasNext(); )
    {
      BusinessEntity bizEntity = (BusinessEntity)i.next();
      if (bizEntity.canDelete())
        assertEquals("State is not 'deleted'", BusinessEntity.STATE_DELETED,
          bizEntity.getState());
      else
        assertTrue("State is 'deleted' for canDelete=false",
          BusinessEntity.STATE_DELETED != bizEntity.getState());

    }
  }

  private void checkMarkDeleteFail(Long uID)
  {
    //mark delete
    try
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkMarkDeleteFail] Enter");
      _bizRegMgr.markDeleteBusinessEntity(uID);
      assertTrue("MarkDelete not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkMarkDeleteFail] "+ex.getMessage());
    }
  }

  private void checkUpdateFail(BusinessEntity mockBE)
  {
    try
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkUpdateFail] Enter");
      updateToDb(mockBE);
      assertTrue("Update not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkUpdateFail] "+ex.getMessage());
    }
  }

  private void checkUpdatePass(BusinessEntity mockBE) throws Throwable
  {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkUpdatePass] Enter");
    WhitePage mockWp = mockBE.getWhitePage();

    //updates
    updateToDb(mockBE);

    BusinessEntity updated = findBizEntityByUId((Long)mockBE.getKey());
    WhitePage updWp = updated.getWhitePage();

    //check be information
    assertEquals("UID is incorrect", mockBE.getKey(), updated.getKey());
    assertEquals("Id incorrect", mockBE.getBusEntId(), updated.getBusEntId());
    assertEquals("Description incorrect", mockBE.getDescription(), updated.getDescription());
    assertEquals("Enterprise is incorrect", mockBE.getEnterpriseId(), updated.getEnterpriseId());
    assertEquals("IsPartner is incorrect", mockBE.isPartner(), updated.isPartner());
    assertEquals("IsPublishable is incorrect", mockBE.isPublishable(), updated.isPublishable());
    assertEquals("IsSyncToServer is incorrect", mockBE.isSyncToServer(), updated.isSyncToServer());
    assertEquals("State is incorrect", mockBE.getState(), updated.getState());
    assertEquals("CanDelete is incorrect", mockBE.canDelete(), updated.canDelete());
    assertTrue("Version is not incremented", mockBE.getVersion() < updated.getVersion());

    //check white page
    assertEquals("UID in whitePage incorrect", mockWp.getKey(), updWp.getKey());
    assertEquals("BEUID is incorrect", mockWp.getBeUId(), updWp.getBeUId());
    assertEquals("Biz Description is incorrect", mockWp.getBusinessDesc(), updWp.getBusinessDesc());
    assertEquals("DUNS is incorrect", mockWp.getDUNS(), updWp.getDUNS());
    assertEquals("GCCC is incorrect", mockWp.getGlobalSupplyChainCode(), updWp.getGlobalSupplyChainCode());
    assertEquals("ContactPerson is incorrect", mockWp.getContactPerson(), updWp.getContactPerson());
    assertEquals("Email is incorrect", mockWp.getEmail(), updWp.getEmail());
    assertEquals("Tel is incorrect", mockWp.getTel(), updWp.getTel());
    assertEquals("Fax is incorrect", mockWp.getFax(), updWp.getFax());
    assertEquals("Website is incorrect", mockWp.getWebsite(), updWp.getWebsite());
    assertEquals("Address is incorrect", mockWp.getAddress(), updWp.getAddress());
    assertEquals("City is incorrect", mockWp.getCity(), updWp.getCity());
    assertEquals("State is incorrect", mockWp.getState(), updWp.getState());
    assertEquals("ZipCode is incorrect", mockWp.getZipCode(), updWp.getZipCode());
    assertEquals("POBox is incorrect", mockWp.getPOBox(), updWp.getPOBox());
    assertEquals("Country is incorrect", mockWp.getCountry(), updWp.getCountry());
    assertEquals("Language is incorrect", mockWp.getLanguage(), updWp.getLanguage());
  }

  private void checkCreateFail(BusinessEntity mockBE)
  {
    try
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkCreateFail] Enter");
      createToDb(mockBE);
      assertTrue("Create not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[BizRegistryManagerBeanTest.checkCreateFail] "+ex.getMessage());
    }
  }

  private Long checkCreatePass(BusinessEntity mockBE) throws Throwable
  {
    Log.debug("TEST", "[BizRegistryManagerBeanTest.checkCreatePass] Enter");
    Long uID = createToDb(mockBE);

    WhitePage mockWP = mockBE.getWhitePage();

    //check retrieved record
    BusinessEntity created = findBizEntityByUId(uID);

    assertNotNull("BizEntity retrieved is null", created);

    WhitePage createdWp = created.getWhitePage();

    //check be information
    assertEquals("UID is inccorect", uID, created.getKey());
    assertEquals("Id incorrect", mockBE.getBusEntId(), created.getBusEntId());
    assertEquals("Description incorrect", mockBE.getDescription(), created.getDescription());
    assertEquals("Enterprise is incorrect", mockBE.getEnterpriseId(), created.getEnterpriseId());
    assertEquals("IsPartner is incorrect", mockBE.isPartner(), created.isPartner());
    assertEquals("IsPublishable is incorrect", mockBE.isPublishable(), created.isPublishable());
    assertEquals("IsSyncToServer is incorrect", mockBE.isSyncToServer(), created.isSyncToServer());
    assertEquals("State is incorrect", mockBE.getState(), created.getState());
    assertEquals("CanDelete is incorrect", mockBE.canDelete(), created.canDelete());

    //check white page
    assertNotNull("UID in whitePage is null", createdWp.getKey());
    assertEquals("BEUID is incorrect", uID, createdWp.getBeUId());
    assertEquals("Biz Description is incorrect", mockWP.getBusinessDesc(), createdWp.getBusinessDesc());
    assertEquals("DUNS is incorrect", mockWP.getDUNS(), createdWp.getDUNS());
    assertEquals("GCCC is incorrect", mockWP.getGlobalSupplyChainCode(), createdWp.getGlobalSupplyChainCode());
    assertEquals("ContactPerson is incorrect", mockWP.getContactPerson(), createdWp.getContactPerson());
    assertEquals("Email is incorrect", mockWP.getEmail(), createdWp.getEmail());
    assertEquals("Tel is incorrect", mockWP.getTel(), createdWp.getTel());
    assertEquals("Fax is incorrect", mockWP.getFax(), createdWp.getFax());
    assertEquals("Website is incorrect", mockWP.getWebsite(), createdWp.getWebsite());
    assertEquals("Address is incorrect", mockWP.getAddress(), createdWp.getAddress());
    assertEquals("City is incorrect", mockWP.getCity(), createdWp.getCity());
    assertEquals("State is incorrect", mockWP.getState(), createdWp.getState());
    assertEquals("ZipCode is incorrect", mockWP.getZipCode(), createdWp.getZipCode());
    assertEquals("POBox is incorrect", mockWP.getPOBox(), createdWp.getPOBox());
    assertEquals("Country is incorrect", mockWP.getCountry(), createdWp.getCountry());
    assertEquals("Language is incorrect", mockWP.getLanguage(), createdWp.getLanguage());

    return uID;
  }

  // ************** Finders ******************************************



  // ******************  utility methods ****************************
  private IBizRegistryManagerObj getBizRegistryMgr() throws Exception
  {
    IBizRegistryManagerHome bizRegHome = (IBizRegistryManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         IBizRegistryManagerHome.class.getName(),
                                         IBizRegistryManagerHome.class);
    return bizRegHome.create();
  }


  private void cleanUp() throws Exception
  {
    _bizRegMgr.markDeleteBusinessEntities("ENT1");
    _bizRegMgr.markDeleteBusinessEntities("ENT2");
    _bizRegMgr.purgeDeletedBusinessEntities("ENT1");
    _bizRegMgr.purgeDeletedBusinessEntities("ENT2");
  }

  private BusinessEntity createDefaultBizEntity(
    String enterpriseId, boolean isPartner, int variable, boolean canDelete)
  {
    BusinessEntity bizEntity = new BusinessEntity();
    bizEntity.setEnterpriseId(enterpriseId);
    bizEntity.setBusEntId("ID"+variable);
    bizEntity.setDescription("BE Description "+variable);
    bizEntity.setPartner(isPartner);
    bizEntity.setCanDelete(canDelete);

    WhitePage whitePage = new WhitePage();
    whitePage.setBusinessDesc("Business Description");
    whitePage.setDUNS("DUNS");
    whitePage.setGlobalSupplyChainCode("GSCC");
    whitePage.setContactPerson("Contact Person");
    whitePage.setEmail("email@email.com");
    whitePage.setTel("12234444");
    whitePage.setFax("232434324");
    whitePage.setWebsite("http://abc@email.com");
    whitePage.setAddress("some address");
    whitePage.setCity("some city");
    whitePage.setState("STATE");
    whitePage.setZipCode("232444");
    whitePage.setPOBox("124434234");
    whitePage.setCountry("ABC");
    whitePage.setLanguage("XYZ");
    bizEntity.setWhitePage(whitePage);

    return bizEntity;
  }

  protected static void updateBizEntity(BusinessEntity bizEntity)
  {
    bizEntity.setDescription("UPD " +bizEntity.getDescription());

    WhitePage whitePage = bizEntity.getWhitePage();
    whitePage.setBusinessDesc("UPD "+whitePage.getBusinessDesc());
    whitePage.setDUNS("UPD " + whitePage.getDUNS());
    whitePage.setGlobalSupplyChainCode("UPD "+whitePage.getGlobalSupplyChainCode());
    whitePage.setContactPerson("UPD "+whitePage.getContactPerson());
    whitePage.setEmail("upd" + whitePage.getEmail());
    whitePage.setTel("23432432");
    whitePage.setFax("212423432");
    whitePage.setWebsite(whitePage.getWebsite() + ".upd");
    whitePage.setAddress("upd " + whitePage.getAddress());
    whitePage.setCity("upd "+whitePage.getCity());
    whitePage.setState("updST");
    whitePage.setZipCode("254323");
    whitePage.setPOBox("15325432");
    whitePage.setCountry("UPD");
    whitePage.setLanguage("UPD");
  }

  private Long createToDb(BusinessEntity bizEntity) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.createToDb] Enter");

      uID = _bizRegMgr.createBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.createToDb] Exit");
    }
    return uID;
  }

  private void updateToDb(BusinessEntity bizEntity) throws Throwable
  {
    try
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.updateToDb] Enter");
      _bizRegMgr.updateBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.updateToDb] Exit");
    }

  }

  private BusinessEntity findBizEntityByUId(Long uId)
  {
    BusinessEntity bizEntity = null;
    try
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.findBizEntityByUId] Enter");

      bizEntity = _bizRegMgr.findBusinessEntity(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[BizRegistryManagerBeanTest.findBizEntityByUId]", ex);
      assertTrue("Error in findBizEntityByUId", false);
    }
    finally
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.findBizEntityByUId] Exit");
    }
    return bizEntity;
  }

  private Collection findBizEntitiesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.findBizEntitiesByFilter] Enter");

      results = _bizRegMgr.findBusinessEntities(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[BizRegistryManagerBeanTest.findBizEntitiesByFilter]", ex);
      assertTrue("Error in _bizRegMgr.findBusinessEntities", false);
    }
    finally
    {
      Log.log("TEST", "[BizRegistryManagerBeanTest.findBizEntitiesByFilter] Exit");
    }
    return results;
  }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


}