/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitRegistrySearchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Set the MessagingStandardClassifications
 *                                    in SearchRegistryCriteria before submit.
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.SubmitRegistrySearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.enterprise.helpers.IBusinessIdentifierKeys;
import com.gridnode.gtas.server.enterprise.helpers.IMessagingStandardKeys;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.bizreg.search.model.ISearchRegistryCriteria;
import com.gridnode.pdip.app.bizreg.search.model.SearchRegistryCriteria;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.Collection;
import java.util.Map;

/**
 * This Action class handles the SubmitRegistrySearchEvent.
 * <p>Before the GridTalk can exchange documents with a trading partner, a search to
 * the public registry may be initiated to locate the partner.
 * <p>The search can be based on the description of the partner business entity and
 * the messaging standards that the partner supports.
 * <p>This action submits the search request to the public registry which will process
 * the search request and return the search results. The search results can
 * be retrieved via the RetrieveRegistrySearchEvent based on the Search ID returned
 * in the EventResponse from this action.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SubmitRegistrySearchAction
  extends    AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3724903094006293542L;
	public static final String ACTION_NAME = "SubmitRegistrySearchAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return SubmitRegistrySearchEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(
    IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                       {
                       };

    short errorCode = IErrorCode.REGISTRY_INQUIRE_ERROR;

    return constructEventResponse(
             errorCode,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SubmitRegistrySearchEvent searchEvent = (SubmitRegistrySearchEvent)event;

    SearchRegistryCriteria criteria = createCriteria(searchEvent.getSearchCriteria());
    // Set the MessagingStandardClassifications 
    criteria.setMessagingStandardClassifications(
      PublishDelegate.getAllTaxonomies(
        PublishDelegate.getTechnicalSpecifications().findNamespace(IMessagingStandardKeys.NAMESPACE_TYPE), 
        criteria.getMessagingStandards()));
    criteria.setDunsIdentifierName(IBusinessIdentifierKeys.DUNS_NUMBER);
    Long searchId = ServiceLookupHelper.getBizRegManager().submitSearchQuery(criteria);

    return constructEventResponse(searchId);
  }

  protected void doSemanticValidation(IEvent event) throws java.lang.Exception
  {
    SubmitRegistrySearchEvent searchEvent = (SubmitRegistrySearchEvent)event;

    Map criteriaMap = searchEvent.getSearchCriteria();
    //Object busEntityDesc = criteriaMap.get(ISearchRegistryCriteria.BUS_ENTITY_DESC);
    Object msgStds = criteriaMap.get(ISearchRegistryCriteria.MESSAGING_STANDARDS);
    Object queryUrl = criteriaMap.get(ISearchRegistryCriteria.QUERY_URL);
    Object match = criteriaMap.get(ISearchRegistryCriteria.MATCH);
    //searchEvent.checkString("Business Entity Description", busEntityDesc);
    searchEvent.checkString("Query Url", queryUrl);
    searchEvent.checkValidURL("Query Url", (String)queryUrl);
    searchEvent.checkCollection("Messaging Standards", (Collection)msgStds, String.class);
    if (((Collection)msgStds).isEmpty())
      throw new EventException("At least one Messaging Standard must be specified");
    searchEvent.checkShort("Match", match);
  }

  // **************************** Own Methods *****************************

  /**
   * Creates a SearchRegistryCriteria object based on the specified Map.
   * 
   * @param criteriaMap Map of SearchRegistryCriteria fields and values.
   * @return SearchRegistryCriteria object created.
   */
  private SearchRegistryCriteria createCriteria(Map criteriaMap)
  {
    SearchRegistryCriteria criteria = new SearchRegistryCriteria();

    Object[] keys = criteriaMap.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      Logger.debug(
        "[SubmitRegistrySearchAction.createCriteria] Setting field "+keys[i] +
          " to "+criteriaMap.get(keys[i]));
      criteria.setFieldValue((Number)keys[i], criteriaMap.get(keys[i]));
    }

    return criteria;
  }
}