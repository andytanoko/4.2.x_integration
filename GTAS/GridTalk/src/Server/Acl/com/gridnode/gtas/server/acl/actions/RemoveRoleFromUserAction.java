/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RemoveRoleFromUserAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractDeleteEntityAction
 *                                    instead of GridTalkEJBAction (to be
 *                                    phased out).
 * Jul 08 2002    Neo Sok Lay         GNDB00008872: If the role to remove is
 *                                    default User role, silently return.
 *                                    This is done because the client actually
 *                                    call a RemoveRoleFromUserAction on creation
 *                                    of user. If User role is not selected during
 *                                    creation, the User role created by default
 *                                    would be removed.
 * Jul 14 2003    Neo Sok Lay         Do not extend from AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.RemoveRoleFromUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ACLLogger;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class RemoveRoleFromUserAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6293263617908329259L;
	public static final String ACTION_NAME = "RemoveRoleFromUserAction";
  public static final String DEFAULT_USER_ROLE = "User";

  private Role _role;
  private UserAccount _userAcct;

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return RemoveRoleFromUserEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#constructErrorResponse(IEvent, TypedException)
   * @since GT 2.2 I1
   */
  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    return constructEventResponse(
      IErrorCode.DELETE_ENTITY_ERROR,
      getErrorMessageParams(event),
      ex);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction#doProcess(IEvent)
   * @since GT 2.2 I1
   */
  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    deleteEntity(event);

    return constructEventResponse(
      IErrorCode.NO_ERROR,
      getSuccessMessageParams(event));
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    RemoveRoleFromUserEvent delEvent = (RemoveRoleFromUserEvent) event;

    _role =
      ActionHelper.verifyRole(delEvent.getRoleUId(), delEvent.getRoleName());

    _userAcct =
      ActionHelper.verifyUserAccount(
        delEvent.getUserUId(),
        delEvent.getUserId());
  }

  // ******************* Own methods ***************************************

  protected Object[] getErrorMessageParams(IEvent event)
  {
    RemoveRoleFromUserEvent delEvent = (RemoveRoleFromUserEvent) event;
    /**@todo TBD */
    return new Object[] {
      Role.ENTITY_NAME,
      String.valueOf(delEvent.getRoleUId()),
      String.valueOf(delEvent.getRoleName()),
      String.valueOf(delEvent.getUserUId()),
      String.valueOf(delEvent.getUserId()),
      };
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    RemoveRoleFromUserEvent delEvent = (RemoveRoleFromUserEvent) event;
    /**@todo TBD */
    return new Object[] {
      Role.ENTITY_NAME,
      String.valueOf(delEvent.getRoleUId()),
      String.valueOf(delEvent.getRoleName()),
      String.valueOf(delEvent.getUserUId()),
      String.valueOf(delEvent.getUserId()),
      };
  }

  protected void deleteEntity(IEvent event) throws Exception
  {
    if (!isDefaultRole())
    {
      ActionHelper.getACLManager().unassignRoleFromSubject(
        new Long(_role.getUId()),
        new Long(_userAcct.getUId()),
        UserAccount.ENTITY_NAME);
    }
    else
      ACLLogger.infoLog(
        "RemoveRoleFromUserAction",
        "deleteEntity",
        "Default User Role will not be deleted");
  }

  private boolean isDefaultRole()
  {
    return DEFAULT_USER_ROLE.equals(_role.getRole());
  }
}