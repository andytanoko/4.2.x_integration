/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveRegistrySearchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 * Sep 23 2003    Neo Sok Lay         RegistryInfoConvertor.toSearchedBusinessEntity()
 *                                    is no longer statically accessible.
 * Oct 17 2005    Neo Sok Lay         Change IBizRegistryManager to IBizRegistryManagerObj                                   
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.gridnode.gtas.events.enterprise.RetrieveRegistrySearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.enterprise.SearchEntityFieldID;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.enterprise.helpers.RegistryInfoConvertor;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryQuery;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles RetrieveRegistrySearchEvent.
 * <p>The results of a previously submitted Registry search can be retrieved
 * based on the Search ID returned when the search was submitted.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class RetrieveRegistrySearchAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 256627299422298597L;
	public static final String ACTION_NAME = "RetrieveRegistrySearchAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return RetrieveRegistrySearchEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    Object[] params = new Object[] {
    };

    return constructEventResponse(
      IErrorCode.REGISTRY_INQUIRE_ERROR,
      params,
      ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    RetrieveRegistrySearchEvent retrieveEvent =
      (RetrieveRegistrySearchEvent) event;

    IBizRegistryManagerObj mgr = ServiceLookupHelper.getBizRegManager();
    SearchRegistryQuery query =
      mgr.retrieveSearchQuery(retrieveEvent.getSearchId());

    query.setSearchResults(
      convertSearchResults(query.getRawSearchResults(), mgr));

    return constructEventResponse(convertSearchQueryToMap(query));
  }

  // **************************** Own Methods *****************************

  /**
   * Convert the specified collection of OrganizationInfo objects to SearchedBusinessEntity
   * objects.
   * 
   * @param searchedOrgInfos The Collection of OrganizationInfo objects to convert
   * @param mgr The BusinessRegistryManager to use for any necessary retrieval.
   * @throws Exception Error during the conversion.
   */
  private Collection convertSearchResults(
    Collection searchedOrgInfos,
    IBizRegistryManagerObj mgr)
    throws Exception
  {
    RegistryInfoConvertor convertor = new RegistryInfoConvertor(
                                        PublishDelegate.getTechnicalSpecifications());
    // convert to SearchBusinessEntity(s)
    Collection searchedBes =
      convertor.toSearchedBusinessEntityList(searchedOrgInfos);

    // determine the states 
    SearchedBusinessEntity searchedBe;
    for (Iterator i = searchedBes.iterator(); i.hasNext();)
    {
      searchedBe = (SearchedBusinessEntity) i.next();
      BusinessEntity existBe =
        mgr.findBusinessEntity(
          searchedBe.getEnterpriseId(),
          searchedBe.getBusEntId());
      if (existBe == null)
      {
        searchedBe.setState(SearchedBusinessEntity.STATE_NEW_BE);
      }
      else
      {
        searchedBe.setState(
          existBe.isPartner()
            ? SearchedBusinessEntity.STATE_PARTNER_BE
            : SearchedBusinessEntity.STATE_MY_BE);
      }
    }

    return searchedBes;
  }

  /**
   * Convert a SearchRegistryQuery to Map object.
   *
   * @param query The SearchRegistryQuery to convert.
   * @return A Map object converted from the specified SearchRegistryQuery.
   *
   * @since GT 2.2
   */
  public static Map convertSearchQueryToMap(SearchRegistryQuery query)
  {
    return SearchRegistryQuery.convertToMap(
      query,
      SearchEntityFieldID.getEntityFieldID(),
      null);
  }

}