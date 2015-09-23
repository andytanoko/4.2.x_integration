/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddRoleToUserAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception handling changes
 *                                    in PDIP layer.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGridTalkAction instead
 *                                    of GridTalkEJBAction (to be phased out).
 * Jul 08 2002    Neo Sok Lay         GNDB00008872: if assignment already exists
 *                                    silently return.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.gtas.events.acl.AddRoleToUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.base.acl.model.Role;
import com.gridnode.pdip.base.acl.model.SubjectRole;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class AddRoleToUserAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 671323024379894857L;

	public static final String ACTION_NAME = "AddRoleToUserAction";

  private Role _role;
  private UserAccount _userAcct;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return AddRoleToUserEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    AddRoleToUserEvent addEvent = (AddRoleToUserEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        Role.ENTITY_NAME,
                        addEvent.getRoleUId(),
                        addEvent.getRoleName(),
                        addEvent.getUserUId(),
                        addEvent.getUserId(),
                      };

    return constructEventResponse(
             IErrorCode.CREATE_ENTITY_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    if (!isAssignmentExists())
    {
      ActionHelper.getACLManager().assignRoleToSubject(
        new Long(_role.getUId()),
        new Long(_userAcct.getUId()),
        UserAccount.ENTITY_NAME);
    }
    return constructEventResponse();
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    AddRoleToUserEvent addEvent = (AddRoleToUserEvent)event;

    _role = ActionHelper.verifyRole(
              addEvent.getRoleUId(),
              addEvent.getRoleName());

    _userAcct = ActionHelper.verifyUserAccount(
                  addEvent.getUserUId(),
                  addEvent.getUserId());
  }

  // ******************** Own methods ****************************

  private boolean isAssignmentExists()
  {
    boolean exist = false;
    try
    {
      SubjectRole assignment = ActionHelper.getACLManager().getSubjectRole(
                                  new Long(_userAcct.getUId()),
                                  UserAccount.ENTITY_NAME,
                                  new Long(_role.getUId()));
      if (assignment != null)
        exist = true;
    }
    catch (Exception ex)
    {

    }
    return exist;
  }
}