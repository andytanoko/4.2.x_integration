/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 * Apr 26 2002    Neo Sok Lay         Skip UserBean for finders, use DAO.
 *                                    Remove invocation of findByState method
 *                                    in UserBean.
 * Jun 13 2002    Neo Sok Lay         Return proxy object to created user account.
 * Jul 25 2002    Neo Sok Lay         Remove dependency on ejbEntityMap.
 * Jan 08 2003    Neo Sok Lay         createUserAccount() to return UserAccount
 *                                    instead of IUserLocalObj.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.user.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.pdip.app.user.entities.ejb.IUserLocalHome;
import com.gridnode.pdip.app.user.entities.ejb.IUserLocalObj;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the UserBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public final class UserEntityHandler
  extends          LocalEntityHandler
{
  private UserEntityHandler()
  {
    super(UserAccount.ENTITY_NAME);
  }

  /**
   * Get an instance of a UserEntityHandler.
   */
  public static UserEntityHandler getInstance()
  {
    UserEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(UserAccount.ENTITY_NAME, true))
    {
      handler = (UserEntityHandler)EntityHandlerFactory.getHandlerFor(
                  UserAccount.ENTITY_NAME, true);
    }
    else
    {
      handler = new UserEntityHandler();
      EntityHandlerFactory.putEntityHandler(UserAccount.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IUserLocalHome.class.getName(),
      IUserLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IUserLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IUserLocalObj.class;
  }

  // ********************** Own methods ******************************
  /**
   * Find the UserAccounts with the specified state.
   *
   * @param state The state of the User accounts.
   * @return a Collection of the UserAccounts having the specified state.
   *
   * @see UserAccountState#STATE_ENABLED
   * @see UserAccountState#STATE_DISABLED
   * @see UserAccountState#STATE_DELETED
   */
  public Collection findByAccountState(short state) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccountState.STATE,
      filter.getEqualOperator(), new Short(state), false);

    IEntityDAO asDAO = UserDAOHelper.getInstance().getAccountStateDAO();
    Collection accountStateResult = asDAO.findByFilter(filter);
    ArrayList userIdList = new ArrayList();
    UserAccountState accountState;
    Long priKey;

    for(Iterator asIter = accountStateResult.iterator(); asIter.hasNext();)
    {
      priKey = (Long) asIter.next();
      accountState = (UserAccountState) asDAO.load(priKey);
      userIdList.add(accountState.getUserID());
    }

    if(userIdList.isEmpty())
      //return empty Collection of userAccount.UID => no result
      return userIdList;
    else
    {
      filter = new DataFilterImpl();
      filter.addDomainFilter(null, UserAccount.ID, userIdList, false);
      return getDAO().getEntityByFilter(filter);
    }
  }

  /**
   * Find the UserAccount whose user id is the specified.
   *
   * @param userId The user id of the account.
   * @return The UserAccount having the specified user id, or <B>null</B> if
   * none found.
   */
  public UserAccount findByUserId(String userId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccount.ID, filter.getEqualOperator(),
      userId, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (UserAccount)result.iterator().next();
  }

  /**
   * Find the UserAccounts whose user name is the specified.
   *
   * @param userName The User name of the account.
   * @return a Collection of the UserAccounts having the specified user name.
   */
  public Collection findByUserName(String userName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccount.NAME, filter.getEqualOperator(),
      userName, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Delete a User account. This will not physically delete the account
   * from the database. This will only mark the account as deleted.
   *
   * @param userId The UserID of the User account to delete.
   */
  public void markDeleteUserAccount(Long accountUId) throws Throwable
  {
    UserAccount account = (UserAccount)getEntityByKeyForReadOnly(accountUId);
    if (account != null)
    {
      if (!account.getAccountState().canDelete())
        throw new ApplicationException("Record not allowed to be deleted!");

      account.getAccountState().setState(UserAccountState.STATE_DELETED);
      update(account);
    }
  }

  /**
   * Delete a User account. This will physically delete the account
   * from the database.
   *
   * @param userId The UserID of the User account to delete.
   */
  public void deleteUserAccount(Long accountUId) throws Throwable
  {
    remove(accountUId);
  }

  /**
   * Create a new User account.
   *
   * @param userAccount The UserAccount entity.
   * @return The created UserAccount.
   */
  public UserAccount createUserAccount(UserAccount userAccount) throws Throwable
  {
    UserAccountState accountState = userAccount.getAccountState();
    if(accountState == null)
    {
      accountState = new UserAccountState();
      userAccount.setAccountState(accountState);
    }
    accountState.setUserID(userAccount.getId());
    accountState.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));

    return (UserAccount)createEntity(userAccount);
  }

  //use UserDAOHelper as the EntityDAO.
  //everything will be taken care of by the UserDAOHelper
  protected IEntityDAO getDAO()
  {
    return UserDAOHelper.getInstance();
  }

}