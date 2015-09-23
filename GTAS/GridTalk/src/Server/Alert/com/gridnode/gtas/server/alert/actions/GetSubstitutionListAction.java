/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSubstitutionListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 03      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.events.alert.GetSubstitutionListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.alert.helpers.SubstitutionList;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;



/**
 * This Action class processes a GetSubstitutionListEvent to obtain
 * the available substitution object & field list for use in MessageTemplate.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetSubstitutionListAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6167605395910912524L;
	public static final String ACTION_NAME = "GetSubstitutionListAction";

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetSubstitutionListEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetSubstitutionListEvent getEvent = (GetSubstitutionListEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {

                      };

    return constructEventResponse(
             IErrorCode.SUBSTITUTION_LIST_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    GetSubstitutionListEvent getEvent = (GetSubstitutionListEvent) event;

    return constructEventResponse(SubstitutionList.getInstance().getSubstitutionMap());
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}