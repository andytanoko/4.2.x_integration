/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteUserAccountAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 2o 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         State & event validation.
 * Jun 18 2002    Neo Sok Lay         Extend from AbstractDeleteEntityAction.
 * Jul 15 2003    Neo Sok Lay         Cater for changes in AbstractDeleteEntityAction.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.events.user.DeleteUserAccountEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the deletion of a User account. This will only
 * mark the account as deleted. Subsequent retrieval of list of accounts will
 * exclude this "deleted" account.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteUserAccountAction
  extends    AbstractDeleteEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6807643993668230052L;
	public static final String ACTION_NAME = "DeleteUserAccountAction";

  // **************** AbstractDeleteEntityAction methods *******************

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#checkCanDelete(IEntity)
   */
  protected boolean checkCanDelete(IEntity entity)
  {
    // check that user is not deleting his own account!
    String userId = (String)entity.getFieldValue(UserAccount.ID);
    boolean canDelete = !getUserID().equals(userId);

    // otherwise, check that the account is system default    
    if (canDelete)
    {
      UserAccountState acctState = (UserAccountState)entity.getFieldValue(UserAccount.ACCOUNT_STATE);
      canDelete = acctState.canDelete();
    }
    return canDelete;
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#deleteEntity(IEntity, Number)
   */
  protected void deleteEntity(IEntity entity, Number keyId) throws Exception
  {
    ActionHelper.getUserManager().deleteUserAccount(
      (Long)entity.getFieldValue(keyId), true);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityKeys(IDataFilter)
   */
  protected Collection getEntityKeys(IDataFilter filter) throws Exception
  {
    return ActionHelper.getUserManager().findUserAccountsKeys(filter);
  }

  /**
   * @see com.gridnode.gtas.server.rdm.ejb.actions.AbstractDeleteEntityAction#getEntityList(IDataFilter)
   */
  protected Collection getEntityList(IDataFilter filter) throws Exception
  {
    return ActionHelper.getUserManager().findUserAccounts(filter);
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return DeleteUserAccountEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* Own methods ***************************************


}