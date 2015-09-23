/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityListForUserAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 06 02      Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import java.util.Collection;

import com.gridnode.gtas.events.enterprise.GetBizEntityListForUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.enterprise.helpers.ActionHelper;
import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This Action class processes a GetBizEntityListForUserEvent and returns
 * the BusinessEntity(s) that a User is attached to, with the default one
 * first in the result set.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBizEntityListForUserAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6132688893462356099L;

	public static final String ACTION_NAME = "GetBizEntityListForUserAction";

  private UserAccount _userAcct;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetBizEntityListForUserEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetBizEntityListForUserEvent getEvent = (GetBizEntityListForUserEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        getEvent.getUserAccountUID(),
                      };

    return constructEventResponse(
             IErrorCode.FIND_ASSOCIATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    Collection beUIDs = ServiceLookupHelper.getEnterpriseHierarchyMgr().getBizEntitiesForUser(
                            (Long)_userAcct.getKey());

    Collection beList = ActionHelper.getBusinessEntities(beUIDs);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertBEsToMapObjects(beList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    GetBizEntityListForUserEvent getEvent = (GetBizEntityListForUserEvent) event;

    _userAcct = ActionHelper.verifyUserAccount(getEvent.getUserAccountUID());


  }

  // ******************** Own methods ***********************************

}