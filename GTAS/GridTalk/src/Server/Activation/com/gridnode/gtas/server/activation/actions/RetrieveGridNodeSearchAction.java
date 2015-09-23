/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveGridNodeSearchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.actions;

import com.gridnode.gtas.events.activation.RetrieveGridNodeSearchEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.activation.helpers.ActionHelper;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.model.SearchGridNodeQuery;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class handles RetrieveGridNodeSearchEvent.
 * <p>The results of a previously submitted GridNode search can be retrieved
 * based on the Search ID returned when the search was submitted.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class RetrieveGridNodeSearchAction
  extends    AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1787515101367337235L;
	public static final String ACTION_NAME = "RetrieveGridNodeSearchAction";

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return RetrieveGridNodeSearchEvent.class;
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

    return constructEventResponse(
             IErrorCode.SEARCH_GRIDNODE_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    RetrieveGridNodeSearchEvent retrieveEvent = (RetrieveGridNodeSearchEvent)event;

    SearchGridNodeQuery query = ServiceLookupHelper.getActivationManager().retrieveSearch(
                                  retrieveEvent.getSearchID());

    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(IErrorCode.NO_ERROR, params,
             ActionHelper.convertSearchQueryToMap(query));
  }

  // **************************** Own Methods *****************************

}