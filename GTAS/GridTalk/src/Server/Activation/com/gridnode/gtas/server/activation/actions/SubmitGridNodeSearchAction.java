/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeSearchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import java.util.Map;

import com.gridnode.gtas.events.activation.SubmitGridNodeSearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.model.ISearchGridNodeCriteria;
import com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles the SubmitGridNodeSearchEvent.
 * <p>Before a GridNode can be activated as trading partner, a search to
 * the GridMaster is necessary to locate the GridNode to activate.
 * <p>The search can be based on GridNode or Whitepage information, or no criteria
 * at all.
 * <p>This action submits the search request to the GridMaster who will process
 * the search request and return the search results. The search results can
 * be retrieve via the RetrieveGridNodeSearchEvent based on the Search ID returned
 * in the EventResponse from this action.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SubmitGridNodeSearchAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7914953358922580476L;
	public static final String ACTION_NAME = "SubmitGridNodeSearchAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return SubmitGridNodeSearchEvent.class;
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

    short errorCode = IErrorCode.SEARCH_GRIDNODE_ERROR;
    if (ex instanceof ConnectionRequiredException)
      errorCode = IErrorCode.CONNECTION_REQUIRED_ERROR;

    return constructEventResponse(
             errorCode,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    SubmitGridNodeSearchEvent searchEvent = (SubmitGridNodeSearchEvent)event;

    SearchGridNodeCriteria criteria = createCriteria(searchEvent.getSearchCriteria());
    Long searchID = ServiceLookupHelper.getActivationManager().submitSearch(criteria);

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params, searchID);
  }

  protected void doSemanticValidation(IEvent event) throws java.lang.Exception
  {
    SubmitGridNodeSearchEvent searchEvent = (SubmitGridNodeSearchEvent)event;

    Map criteriaMap = searchEvent.getSearchCriteria();

    Object criteriaType = criteriaMap.get(ISearchGridNodeCriteria.CRITERIA);
    Object matchType    = criteriaMap.get(ISearchGridNodeCriteria.MATCH);
    searchEvent.checkShort("Criteria Type", criteriaType);
    switch (((Short)criteriaType).shortValue())
    {
      case ISearchGridNodeCriteria.CRITERIA_BY_GRIDNODE:
      case ISearchGridNodeCriteria.CRITERIA_BY_WHITEPAGE:
           searchEvent.checkShort("Match Type", matchType);
           validateMatchType((Short)matchType);
           break;
      case ISearchGridNodeCriteria.CRITERIA_NONE:
           break;
      default : throw new Exception("Invalid Criteria Type!");
    }

  }

  // **************************** Own Methods *****************************

  private SearchGridNodeCriteria createCriteria(Map criteriaMap)
  {
    SearchGridNodeCriteria criteria = new SearchGridNodeCriteria();

    Object[] keys = criteriaMap.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      Logger.debug(
        "[SubmitGridNodeSearchAction.createCriteria] Setting field "+keys[i] +
          " to "+criteriaMap.get(keys[i]));
      criteria.setFieldValue((Number)keys[i], criteriaMap.get(keys[i]));
    }

    return criteria;
  }

  private void validateMatchType(Short matchType)
    throws Exception
  {
    switch (matchType.shortValue())
    {
      case ISearchGridNodeCriteria.MATCH_ALL:
      case ISearchGridNodeCriteria.MATCH_ANY:
           break;
      default : throw new Exception("Invalid Match Type!");
    }
  }
}