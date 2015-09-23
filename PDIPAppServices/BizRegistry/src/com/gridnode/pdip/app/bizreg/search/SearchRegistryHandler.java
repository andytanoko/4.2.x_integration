/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Include categories in criteria when 
 *                                    querying ConceptInfo during search.
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 * Oct 17 2005    Neo Sok Lay         Change PublicRegistryManager type to
 *                                    IPublicRegistryManagerObj.
 * Mar 03 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.bizreg.search;

import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.framework.db.keygen.KeyGen;

import java.sql.Timestamp;
import java.util.*;

/**
 * This helper class handles the search in publich registries.
 * NOTE: This set of logic can be refactored to the App BizRegistry module.
 * What's left is just the conversion of Raw search results to specific representation, 
 * which is a collection of SearchedBusinessEntity objects here.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SearchRegistryHandler
{
  private static final SearchRegistryHandler _self = new SearchRegistryHandler();
  private static final Hashtable<Long,SearchRegistryQuery> _queries = new Hashtable<Long,SearchRegistryQuery>();
  private static final SchemeInfo _dunsScheme = SchemeInfo.getDunsSchemeInfo();
  
  /**
   * Constructs a SearchRegistryHandler instance. 
   */
  private SearchRegistryHandler()
  {
  }

  /**
   * Gets an instance of SearchRegistryHandler.
   * 
   * @return An instance of SearchRegistryHandler.
   */
  public static SearchRegistryHandler getInstance()
  {
    return _self;
  }
  
  /**
   * Submits a search on a business registry using the specified search criteria.
   * 
   * @param criteria The search criteria
   * @return A SearchId, if search request is submitted successfully, for retrieving
   * the search results later on.
   * @throws Exception Error occurs when submitting the search request.
   */
  public Long submitSearchQuery(SearchRegistryCriteria criteria)
    throws Exception
  {
    Long searchId = generateSearchId();
    
    //create SearchRegistryQuery
    SearchRegistryQuery searchQuery = new SearchRegistryQuery();
    searchQuery.setSearchID(searchId);
    searchQuery.setSearchCriteria(criteria);
    searchQuery.setDTSubmitted(getCurrentTimestamp());

    //cache SearchRegistryQuery
    _queries.put(searchId, searchQuery);
    
    //create SearchRequestNotification(searchId, criteria)
    SearchRequestNotification reqNote = 
      new SearchRequestNotification(
          SearchRequestNotification.DESTINATION_PUBLIC_REGISTRY,
          searchId,
          criteria);
          
    //notify SearchRequestNotification
    Notifier.getInstance().broadcast(reqNote);
    
    return searchId;
  }
  
  /**
   * Handles when search results has returned for a particular search request.
   * 
   * @param searchId The SearchId for retrieving back the search request.
   * @param searchedOrgs Collection of OrganizationInfo objects returned from the
   * search, if successful.
   * @throws Exception Error retrieving the search request, e.g. when searchId is invalid.
   */
  public void onSearchResultsReturned(Long searchId, Collection searchedOrgs)
    throws Exception
  {
    SearchRegistryQuery searchQuery = retrieveSearchQuery(searchId);
    searchQuery.setDTResponded(getCurrentTimestamp());
    searchQuery.setRawSearchResults(searchedOrgs);
  }

  /**
   * Handles when exception has been fedback for a submitted search request.
   * 
   * @param searchId The SearchId of the search request.
   * @param exceptionStr String representation of the exception that occurred.
   * @throws Exception Error retrieving the search request, e.g. when searchId is invalid.
   */  
  public void onSearchException(Long searchId, String exceptionStr)
    throws Exception
  {
    SearchRegistryQuery searchQuery = retrieveSearchQuery(searchId);
    searchQuery.setDTResponded(getCurrentTimestamp());
    searchQuery.setIsException(true);
    searchQuery.setExceptionStr(exceptionStr);
  }

  /**
   * Perform a search on a Public registry using the specified search criteria.
   * 
   * @param searchId SearchId of the search request.
   * @param criteria The search criteria.
   * @throws Exception Error when performing the search.
   */  
  public void performSearch(Long searchId, SearchRegistryCriteria criteria)
    throws Exception
  {
    SearchResultsNotification resultsNote = null;

    IPublicRegistryManagerObj mgr = null;
     
    try
    {
      mgr = getPublicRegistryManager();
    
      // find Concepts (messagingStandards)
      Collection<ConceptInfo> concepts = mgr.queryConceptInfos(
                              criteria.getQueryUrl(), 
                              criteria.getMessagingStandards(), 
                              false,
                              criteria.getMessagingStandardClassifications(), 
                              null, 
                              null);
      
      if (concepts == null || concepts.size() == 0)
      {  
        // set exception results 
        throw new Exception("No Valid Messaging Standards found in registry");
      } 
      else
      {    
        //tokenize businessEntityDesc as namepatterns
        Collection<String> names = null;
        
        if (!isEmpty(criteria.getBusEntityDesc()))
        {
          names = tokenize(criteria.getBusEntityDesc(), " ");
        }
        
        // identifiers
        Collection<String[]> identifiers = null;
        
        Collection<String> dunsNums = null;
        if (!isEmpty(criteria.getDUNS()))
        {
          dunsNums = tokenize(criteria.getDUNS(), " ");
          identifiers = new ArrayList<String[]>();
          for (String duns : dunsNums)
          {
            identifiers.add(createIdentifierInfo(
              criteria.getDunsIdenfierName(),
              duns,
              _dunsScheme.getKey()));
          }
        }
          
        //find OrganizationInfos (namepatterns, concepts)
        Collection<OrganizationInfo> orgInfos = mgr.queryOrganizationInfos(
                                criteria.getQueryUrl(),
                                names,
                                SearchRegistryCriteria.MATCH_ALL == criteria.getMatch(),
                                null, concepts, identifiers, null);

        resultsNote = new SearchResultsNotification(
                        SearchResultsNotification.DESTINATION_PUBLIC_REGISTRY,
                        searchId,
                        orgInfos);      
      }
    }
    catch (Throwable t)
    {
      resultsNote = new SearchResultsNotification(
                      SearchResultsNotification.DESTINATION_PUBLIC_REGISTRY,
                      searchId,
                      t);
    }
    finally
    {                            
      //notify SearchResultsNotification
      Notifier.getInstance().broadcast(resultsNote);
      if (mgr != null)
        mgr.disconnectFromRegistry(createConnectionInfo(criteria.getQueryUrl()));
    }
  }

  /**
   * Create a connection information object based on the specified query url.
   * 
   * @param queryUrl The query url.
   * @return A connection information object.
   */
  private String[] createConnectionInfo(String queryUrl)
  {
    String[] connInfo = new String[IConnectionInfo.NUM_FIELDS];
    connInfo[IConnectionInfo.FIELD_QUERY_URL] = queryUrl;

    return connInfo;
  }

  /**
   * Generates a search id.
   * 
   * @return A Search id not used before.
   * @throws Exception Error generating the search id.
   */
  private Long generateSearchId() throws Exception
  {
    Long id = KeyGen.getNextId(SearchRegistryQuery.ENTITY_NAME);
    
    return id;
  }

  /**
   * Get the current timestamp.
   *
   * @return The current timestamp.
   */
  protected Timestamp getCurrentTimestamp()
  {
    return new Timestamp(System.currentTimeMillis());
  }
  
  /**
   * Retrieve a Search query submitted previously.
   * @param searchId the SearchId for the search.
   * @return The Search query object.
   * @throws Exception No such Search Id.
   */
  public SearchRegistryQuery retrieveSearchQuery(Long searchId)
    throws Exception
  {
    SearchRegistryQuery searchQuery = _queries.get(searchId);

    if (searchQuery == null)
      throw new Exception("Search Query not found for Search Id: "+searchId);

    return searchQuery;
  }

  /**
   * Tokenize a string using the specified delimiters and return a collection
   * of the tokens, excluding the delimiters.
   * 
   * @param val The String to tokenize.
   * @param delim The delimiters.
   * @return Collection of String tokens.
   */
  private Collection<String> tokenize(String val, String delim)
  {
    StringTokenizer st = new StringTokenizer(val, delim, false);
    Collection<String> tokens = new ArrayList<String>(st.countTokens());
    while (st.hasMoreTokens())
    {
      tokens.add(st.nextToken());
    }
    
    return tokens;
  }
  
  /**
   * Checks if a String value is empty, i.e. <b>null</b> or empty string.
   * 
   * @param val The String value to check.
   * @return <b>true<b> if <code>val</code> is <b>null</b> or empty string.
   * <b>false</b> otherwise.
   */
  private boolean isEmpty(String val)
  {
    return val == null || val.trim().length()==0;
  }
  
  /**
   * Obtain an instance of IPublicRegistryManager.
   *
   * @return an instance of IPublicRegistryManager.
   * @exception Exception Error in obtaining an instance of IPublicRegistryManager.
   *
   * @since GT 2.2
   */
  public static IPublicRegistryManagerObj getPublicRegistryManager()
    throws Exception
  {
    return (IPublicRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPublicRegistryManagerHome.class.getName(),
      IPublicRegistryManagerHome.class,
      new Object[0]);
  }
  
  /**
   * Create an Identifier information object.
   * 
   * @param name The Name of the identifier
   * @param value The Value of the identifier
   * @param identifierScheme The identification scheme for the identifier.
   * @return the created Identifier informatioj object.
   */
  private static String[] createIdentifierInfo(String name, String value, String identifierScheme)
  {
    String[] idInfo = new String[IIdentifierInfo.NUM_FIELDS];
    idInfo[IIdentifierInfo.FIELD_IDENTIFICATION_SCHEME_KEY] = identifierScheme;
    idInfo[IIdentifierInfo.FIELD_IDENTIFIER_NAME] = name;
    idInfo[IIdentifierInfo.FIELD_IDENTIFIER_VALUE] = value;
    
    return idInfo;
  }
  
  /**
   * Wraps an object in a Collection object.
   * 
   * @param obj The object to wrap around.
   * @return The Collection object.
   *//*
  private static Collection wrapCollection(Object obj)
  {
    Collection coll = new ArrayList();
    coll.add(obj);
    return coll;
  }*/
}
