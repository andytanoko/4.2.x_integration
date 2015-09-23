/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserDAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26 2002    Neo Sok Lay         Created
 * Jun 09 2007    Tam Wei Xiang       Support Select MIN, MAX
 */
package com.gridnode.pdip.app.user.helpers;

import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.framework.db.dao.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a EntityDAO implementation for UserBean. It takes care of
 * the UserAccount entity as well as its dependent entity: UserAccountState.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserDAOHelper implements IEntityDAO
{
  private static UserDAOHelper _self;

  private UserDAOHelper()
  {
  }

  /**
   * Get the singleton instance of this DAO.
   */
  public static UserDAOHelper getInstance()
  {
    if (_self == null)
      _self = new UserDAOHelper();

    return _self;
  }

  // ******************* Start Implement methods in IEntityDAO ****************

  /**@todo check return key, throw specific exception */
  public Long create(IEntity entity)
    throws Exception
  {
    UserAccount userAccount = (UserAccount)entity;

    checkDuplicate(userAccount.getId());

    UserAccountState accountState = userAccount.getAccountState();

    //Long stateKey = 
    getAccountStateDAO().create(accountState);
    Long userKey = getUserAccountDAO().create(userAccount);
    //userAccount.setFieldValue(UserAccount.ACCOUNT_STATE, accountState);
    return userKey;
  }

  private void checkDuplicate(String userId)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccount.ID, filter.getEqualOperator(),
      userId, false);

    if (getUserAccountDAO().getEntityCount(filter) > 0)
      throw new ApplicationException("User ID already used: "+userId);
  }

  public IEntity load(Long userUID)
    throws Exception
  {
    UserAccount acct = (UserAccount)getUserAccountDAO().load(userUID);
    loadAccountState(acct);

    return acct;
  }

  public void store(IEntity userAccount)
    throws Exception
  {
    UserAccountState accountState = ((UserAccount)userAccount).getAccountState();
    getUserAccountDAO().store(userAccount);
    getAccountStateDAO().store(accountState);
  }

  public void remove(Long userUID)
    throws Exception
  {
    UserAccount acct = (UserAccount)load(userUID);

    UserAccountState accountState = acct.getAccountState();

    getUserAccountDAO().remove(userUID);
    getAccountStateDAO().remove((Long)accountState.getKey());
  }

  public Long findByPrimaryKey(Long primaryKey) throws Exception
  {
    return getUserAccountDAO().findByPrimaryKey(primaryKey);
  }

  public Collection findByFilter(IDataFilter filter)
    throws Exception
  {
    return getUserAccountDAO().findByFilter(filter);
  }

  public Collection getEntityByFilter(IDataFilter filter)
    throws Exception
  {
    Collection accts = getUserAccountDAO().getEntityByFilter(filter);
    for (Iterator i=accts.iterator(); i.hasNext(); )
    {
      UserAccount acct = (UserAccount)i.next();
      loadAccountState(acct);
    }
    return accts;
  }

  public int getEntityCount(IDataFilter filter)
    throws Exception
  {
    return getUserAccountDAO().getEntityCount(filter);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#getFieldValuesByFilter(java.lang.Number, com.gridnode.pdip.framework.db.filter.IDataFilter)
	 */
	public Collection getFieldValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
	{
		return getUserAccountDAO().getFieldValuesByFilter(fieldId, filter);
	}
  
  // ******************* Ends Implement methods in IEntityDAO ****************


	/**
   * Creates the account state record for a new user account.
   *
   * @param userAccount The new User account entity.
   *//*
  private void createAccountState(UserAccount userAccount)
    throws Exception
  {
    UserAccountState accountState = userAccount.getAccountState();

    getUserAccountDAO().create(accountState);
  }*/

  /**
   * Loads the account state record for a user account.
   *
   * @param userAccount The User account entity.
   */
  private void loadAccountState(UserAccount userAccount)
    throws Exception
  {
    Long accountStateUId = findAccountStateByUserId(userAccount.getId());
    UserAccountState accountState = (UserAccountState)getAccountStateDAO().load(
                                      accountStateUId);
    userAccount.setAccountState(accountState);
  }

  /**
   * Finds the account state record of a userAccount.
   *
   * @param userId The user id of the user account.
   * @return The primary key to the UserAccountState record.
   *
   */
  private Long findAccountStateByUserId(String userId)
    throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccountState.USER_ID,
      filter.getEqualOperator(), userId, false);
    Collection result = getAccountStateDAO().findByFilter(filter);
    if(result == null || result.isEmpty())
      return null;
    return (Long) result.iterator().next();
  }

  /**
   * Get the data access object for the UserAccount entity.
   *
   * @return the IEntityDAO for UserAccount entity.
   */
  public IEntityDAO getUserAccountDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(UserAccount.ENTITY_NAME);
  }

  /**
   * Get the data access object for the UserAccountState entity.
   *
   * @return the IEntityDAO for UserAccountState entity.
   */
  public IEntityDAO getAccountStateDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(UserAccountState.ENTITY_NAME);
  }
  
	/* (non-Javadoc)
	 * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#create(com.gridnode.pdip.framework.db.entity.IEntity, boolean)
	 */
	public Long create(IEntity entity, boolean useUID) throws Exception {
		throw new Exception("[UserDAOHelper.create(IEntity entity, boolean useUID)] Not Supported");
	}  
  
	public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getUserAccountDAO().getMinValuesByFilter(fieldId, filter);
  }
  
  public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    return getUserAccountDAO().getMaxValuesByFilter(fieldId, filter);
  }
}