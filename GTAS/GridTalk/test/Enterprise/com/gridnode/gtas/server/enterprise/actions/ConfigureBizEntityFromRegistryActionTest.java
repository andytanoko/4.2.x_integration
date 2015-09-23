/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigureBizEntityFromRegistryActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 08 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.ConfigureBizEntityFromRegistryEvent;
import com.gridnode.gtas.events.enterprise.RetrieveRegistrySearchEvent;
import com.gridnode.gtas.events.enterprise.SubmitRegistrySearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.gtas.model.channel.ISecurityInfo;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;
import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This test case tests the following ConfigureBizEntityFromRegistryAction.
 * 
 * This test case assumes the target public registry for testing contains the
 * there are published organizations (whose names contains '543') 
 * with a RNIF1 and/or RNIF2 service binding.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ConfigureBizEntityFromRegistryActionTest extends ActionTestHelper
{
  ConfigureBizEntityFromRegistryEvent[] _events;
  Collection _searchedBes;

  /**
   * turn this off if the searched security info already exists -- don't want
   * to check with the default values since there is no overwriting of the info.
   */  
  static final boolean CHECK_SECURITY_INFO = false;  
  //static final String GOOD_URL = "http://localhost:8080/RegistryServer";
  static final String GOOD_URL = "http://192.168.213.186:8080/juddi/inquiry";
  static final String NAME_PATTERN_1 = "543";
  static final String STD_RNIF1 = "gridnode-com:rnif-1.1";
  static final String STD_RNIF2 = "gridnode-com:rnif-2";
  static final String[] STDS = {STD_RNIF1,STD_RNIF2};
  
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
  
  public ConfigureBizEntityFromRegistryActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ConfigureBizEntityFromRegistryActionTest.class);
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

    SubmitRegistrySearchEvent searchEvent = createSearchEvent(
      new String[]{STD_RNIF1,STD_RNIF2}, NAME_PATTERN_1, GOOD_URL, false);
    
    Map queryMap = submitSearch(searchEvent, _sessions[0], _sm[0]);

    Long searchId = (Long)queryMap.get(SearchRegistryQuery.SEARCH_ID);
    Collection results = (Collection)queryMap.get(SearchRegistryQuery.RESULTS);
    ArrayList uuids = new ArrayList();
    for (Iterator i=results.iterator(); i.hasNext(); )
    {    
      Map searchedBeMap = (Map)i.next();
      uuids.add(searchedBeMap.get(SearchedBusinessEntity.UUID));
    }
    
    Log.debug("TEST", "[ConfigureBizEntityFromRegistryActionTest.prepareTestData] Searched UUIDs:"+uuids);
 
    _events = new ConfigureBizEntityFromRegistryEvent[]
    {
      new ConfigureBizEntityFromRegistryEvent(searchId, uuids),
    };
    
    _searchedBes = results;
  }

  private SubmitRegistrySearchEvent createSearchEvent(
    String[] msgStds, String namePatterns, String queryUrl, boolean matchAll)
    throws Exception
  {
    HashMap searchCriteria = new HashMap();
    searchCriteria.put(SearchRegistryCriteria.BUS_ENTITY_DESC, namePatterns);
    searchCriteria.put(SearchRegistryCriteria.MATCH, matchAll?new Short(SearchRegistryCriteria.MATCH_ALL):new Short(SearchRegistryCriteria.MATCH_ANY));
    searchCriteria.put(SearchRegistryCriteria.MESSAGING_STANDARDS, Arrays.asList(msgStds));
    searchCriteria.put(SearchRegistryCriteria.QUERY_URL, queryUrl);
    
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
    configureCheckSuccess(_events[0], _sessions[0], _sm[0]);
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#createNewAction()
   */
  protected IEJBAction createNewAction()
  {
    return new ConfigureBizEntityFromRegistryAction();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#checkActionEffect(com.gridnode.pdip.framework.rpf.event.BasicEventResponse, com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.rpf.ejb.StateMachine)
   */
  protected void checkActionEffect(
    BasicEventResponse response,
    IEvent event,
    StateMachine sm)
  {
    // retrieve Be  based on _searchedBes
    Map searchedBeMap;
    BusinessEntity be;
    for (Iterator i=_searchedBes.iterator(); i.hasNext();)
    {
      searchedBeMap = (Map)i.next();
      be = findBizEntity((String)searchedBeMap.get(SearchedBusinessEntity.ENTERPRISE_ID), (String)searchedBeMap.get(SearchedBusinessEntity.ID));
      assertNotNull("BusinessEntity not added", be);

      // check the details      
      checkSearchedBe(be, searchedBeMap);
      
      Collection channels = getChannelsForBizEntity((Long)be.getKey());
      checkSearchedChannels(channels, (Collection)searchedBeMap.get(SearchedBusinessEntity.CHANNELS));
    }
  }

  private void checkSearchedBe(BusinessEntity be, Map searchedBeMap)
  {
    assertEquals("BizEntity Description is incorrect", searchedBeMap.get(SearchedBusinessEntity.DESCRIPTION), be.getDescription());
    assertEquals("IsPartner is incorrect", Boolean.TRUE, Boolean.valueOf(be.isPartner()));
    assertEquals("PartnerCat is incorrect", IBusinessEntity.CATEGORY_OTHERS, be.getPartnerCategory());
    assertEquals("CanDelete is incorrect", Boolean.TRUE, Boolean.valueOf(be.canDelete()));
    
    Map wpMap = (Map)searchedBeMap.get(SearchedBusinessEntity.WHITE_PAGE);
    WhitePage wp = be.getWhitePage();
    assertEquals("Biz Description is incorrect", wpMap.get(WhitePage.BUSINESS_DESC), wp.getBusinessDesc());
    assertEquals("DUNS is incorrect", wpMap.get(WhitePage.DUNS), wp.getDUNS());
    assertEquals("GCCC is incorrect", wpMap.get(WhitePage.G_SUPPLY_CHAIN_CODE), wp.getGlobalSupplyChainCode());
    assertEquals("ContactPerson is incorrect", wpMap.get(WhitePage.CONTACT_PERSON), wp.getContactPerson());
    assertEquals("Email is incorrect",wpMap.get(WhitePage.EMAIL), wp.getEmail());
    assertEquals("Tel is incorrect", wpMap.get(WhitePage.TEL), wp.getTel());
    assertEquals("Fax is incorrect", wpMap.get(WhitePage.FAX), wp.getFax());
    assertEquals("Website is incorrect", wpMap.get(WhitePage.WEBSITE), wp.getWebsite());
    assertEquals("Address is incorrect", wpMap.get(WhitePage.ADDRESS), wp.getAddress());
    assertEquals("City is incorrect", wpMap.get(WhitePage.CITY), wp.getCity());
    assertEquals("State is incorrect", wpMap.get(WhitePage.STATE), wp.getState());
    assertEquals("ZipCode is incorrect", wpMap.get(WhitePage.ZIP_CODE), wp.getZipCode());
    assertEquals("POBox is incorrect", wpMap.get(WhitePage.PO_BOX), wp.getPOBox());
    assertEquals("Country is incorrect", wpMap.get(WhitePage.COUNTRY), wp.getCountry());
    assertEquals("Language is incorrect", wpMap.get(WhitePage.LANGUAGE), wp.getLanguage());
  }
  
  private void checkSearchedChannels(Collection channels, Collection channelMaps)
  {
    ChannelInfo[] dbChannels = (ChannelInfo[])channels.toArray(new ChannelInfo[0]);
    Map[] expChannelMaps = (Map[])channelMaps.toArray(new Map[0]);     
    
    for (int i=0; i<dbChannels.length; i++)
    {
      checkSearchedChannel(dbChannels[i], expChannelMaps[i]);
      checkSearchedCommInfo(dbChannels[i].getTptCommInfo(), (Map)expChannelMaps[i].get(ChannelInfo.TPT_COMM_INFO));
      checkSearchedPackagingInfo(dbChannels[i].getPackagingProfile(), (Map)expChannelMaps[i].get(ChannelInfo.PACKAGING_PROFILE));
      checkSearchedSecurityInfo(dbChannels[i].getSecurityProfile(), (Map)expChannelMaps[i].get(ChannelInfo.SECURITY_PROFILE));
    }
  }
  
  private void checkSearchedChannel(ChannelInfo check, Map expectedMap)
  {
    assertEquals("Description incorrect", expectedMap.get(ChannelInfo.DESCRIPTION), check.getDescription());
    assertEquals("Name incorrect", expectedMap.get(ChannelInfo.NAME), check.getName());
    assertEquals("RefId is incorrect", expectedMap.get(ChannelInfo.REF_ID), check.getReferenceId());
    assertEquals("ProtocolType is incorrect", expectedMap.get(ChannelInfo.TPT_PROTOCOL_TYPE), check.getTptProtocolType());
    assertEquals("IsPartner is incorrect", Boolean.TRUE, Boolean.valueOf(check.isPartner()));
    assertEquals("PartnerCat is incorrect", IChannelInfo.CATEGORY_OTHERS, check.getPartnerCategory());
    assertEquals("CanDelete is incorrect", Boolean.TRUE, Boolean.valueOf(check.canDelete()));
    assertEquals("IsMaster is incorrect", Boolean.FALSE, Boolean.valueOf(check.isMaster()));
  }

  private void checkSearchedCommInfo(CommInfo check, Map expectedMap)
  {
    assertEquals("Description incorrect", expectedMap.get(CommInfo.DESCRIPTION), check.getDescription());
    assertEquals("URL incorrect", expectedMap.get(CommInfo.URL), check.getURL());
    assertEquals("ProtocolType is incorrect", expectedMap.get(CommInfo.PROTOCOL_TYPE), check.getProtocolType());
    assertEquals("RefId incorrect", expectedMap.get(CommInfo.REF_ID), check.getRefId());
    assertEquals("IsPartner is incorrect", Boolean.TRUE, Boolean.valueOf(check.isPartner()));
    assertEquals("PartnerCat is incorrect", ICommInfo.CATEGORY_OTHERS, check.getPartnerCategory());
    assertEquals("CanDelete is incorrect", Boolean.TRUE, Boolean.valueOf(check.canDelete()));
  }

  private void checkSearchedPackagingInfo(PackagingInfo check, Map expectedMap)
  {
    assertEquals("Name incorrect", expectedMap.get(PackagingInfo.NAME), check.getName());
    assertEquals("Description incorrect", expectedMap.get(PackagingInfo.DESCRIPTION), check.getDescription());
    assertEquals("Envelope incorrect", expectedMap.get(PackagingInfo.ENVELOPE), check.getEnvelope());
    assertEquals("IsPartner is incorrect", Boolean.TRUE, Boolean.valueOf(check.isPartner()));
    assertEquals("PartnerCat is incorrect", IPackagingInfo.CATEGORY_OTHERS, check.getPartnerCategory());
    assertEquals("CanDelete is incorrect", Boolean.TRUE, Boolean.valueOf(check.canDelete()));
  }

  private void checkSearchedSecurityInfo(SecurityInfo check, Map expectedMap)
  {
    if (CHECK_SECURITY_INFO)
    {
      assertEquals("Name incorrect", expectedMap.get(SecurityInfo.NAME), check.getName());
      assertEquals("Description incorrect", expectedMap.get(SecurityInfo.DESCRIPTION), check.getDescription());
      assertEquals("EncryptType incorrect", expectedMap.get(SecurityInfo.ENCRYPTION_TYPE), check.getEncryptionType());
      assertEquals("SignatureEncryptType incorrect", expectedMap.get(SecurityInfo.SIGNATURE_TYPE), check.getSignatureType());
      assertEquals("IsPartner is incorrect", Boolean.TRUE, Boolean.valueOf(check.isPartner()));
      assertEquals("PartnerCat is incorrect", ISecurityInfo.CATEGORY_OTHERS, check.getPartnerCategory());
      assertEquals("CanDelete is incorrect", Boolean.TRUE, Boolean.valueOf(check.canDelete()));
    }
  }
     
  private void configureCheckSuccess(
    ConfigureBizEntityFromRegistryEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private Map submitSearch(
    SubmitRegistrySearchEvent event, String session, StateMachine sm)
  {
    SubmitRegistrySearchAction action = new SubmitRegistrySearchAction();
    
    Map queryMap = null;
    try
    {
      BasicEventResponse response = performEvent(action, event, session, sm);
      
      if (response.isEventSuccessful())
      {
        assertNotNull("No SearchId returned", response.getReturnData());
        Long searchId = (Long)response.getReturnData();
    
        // retrieveRegistrySearch, check until responded
        boolean responded = false;
        while (!responded)
        {
          waitForAWhile();
          queryMap = retrieveSearchQuery(searchId, _sessions[0], sm);
          responded = queryMap.get(SearchRegistryQuery.DT_RESPONDED) != null;
        }

        System.out.println("-- Retrieved Search Query --");
        System.out.println(queryMap);

        assertEquals("IsException is incorrect", Boolean.FALSE, (Boolean)queryMap.get(SearchRegistryQuery.IS_EXCEPTION));
      }
      else
        Log.err("TEST", "[SearchRegistryActionsTest.submitSearch] EventUnsuccessful ");
    }
    catch (Exception e)
    {
      Log.err("TEST", "[SearchRegistryActionsTest.submitSearch] Error: ", e);
    }
    
    return queryMap;
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
