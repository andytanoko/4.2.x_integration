/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserListForRoleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 05 2002    Goh Kan Mun         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGridTalkAction instead
 *                                    of GridTalkEJBAction (to be phased out).
 */

package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.GetUserListForRoleEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;

import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class GetUserListForRoleAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2137668668227447079L;

	public static final String ACTION_NAME = "GetUserListForRoleAction";

  private Role _role;

  // ************* AbstractGridTalkAction methods *************************

  protected Class getExpectedEventClass()
  {
    return GetUserListForRoleEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    GetUserListForRoleEvent getEvent = (GetUserListForRoleEvent)event;
    /**@todo TBD */
    Object[] params = new Object[]
                      {
                        Role.ENTITY_NAME,
                        getEvent.getRoleUId(),
                        getEvent.getRoleName(),
                      };

    return constructEventResponse(
             IErrorCode.FIND_ENTITY_LIST_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    Collection userList = getUserList(new Long(_role.getUId()));

    return constructEventResponse(new EntityListResponseData(
             ActionHelper.convertUsersToMapObjects(userList)));
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    GetUserListForRoleEvent getEvent = (GetUserListForRoleEvent) event;

    _role = ActionHelper.verifyRole(
              getEvent.getRoleUId(),
              getEvent.getRoleName());
  }

  // **************** Own methods ***************************

  /**
   * Retrieve the list of Users that are assigned the specified role.
   *
   * @param roleUId The UID of the role.
   * @return A Collection of UserAccounts that are assigned the specified role.
   * @exception Throwable Error in retrieving the list of Users.
   *
   * @since 2.0
   */
  private Collection getUserList(Long roleUId) throws Throwable
  {
    Collection userUIdList = ActionHelper.getACLManager().getSubjectUIdsForRole(
                               roleUId,
                               UserAccount.ENTITY_NAME);

    Vector v = new Vector();
    if (userUIdList != null)
    {
      Long userUId = null;
      for (Iterator i=userUIdList.iterator(); i.hasNext(); )
      {
        try
        {
          userUId = (Long) i.next();
          v.add(ActionHelper.verifyUserAccount(userUId));
        }
        catch (Exception ex)
        {
          //skip if user account not found
        }
      }
    }

    return v;
  }

}