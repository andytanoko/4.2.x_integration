/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListForUserAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Add semantic validation of event data.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGridTalkAction instead
 *                                    of GridTalkEJBAction (to be phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Collection;

import com.gridnode.gtas.events.acl.GetRoleListForUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class GetRoleListForUserAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3231838922215261485L;

	public static final String ACTION_NAME = "GetRoleListForUserAction";

  private UserAccount _userAcct;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetRoleListForUserEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetRoleListForUserEvent getEvent = (GetRoleListForUserEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        getEvent.getUserUId(),
                        getEvent.getUserId(),
                      };

    return constructEventResponse(
             IErrorCode.FIND_ENTITY_LIST_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    Collection roleList = ActionHelper.getACLManager().getRolesForSubject(
                            new Long(_userAcct.getUId()),
                            UserAccount.ENTITY_NAME);

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertRolesToMapObjects(roleList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    GetRoleListForUserEvent getEvent = (GetRoleListForUserEvent) event;

    _userAcct = ActionHelper.verifyUserAccount(
                  getEvent.getUserUId(),
                  getEvent.getUserId());
  }

  // ******************** Own methods ***********************************

}