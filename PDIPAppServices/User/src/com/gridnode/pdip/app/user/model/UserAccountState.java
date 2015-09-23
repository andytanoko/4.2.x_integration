
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserAccountState.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2002    NSL/OHL             Created
 */
package com.gridnode.pdip.app.user.model;

import com.gridnode.pdip.framework.db.entity.*;

import java.util.Date;

/**
 * This is an object model for UserAccountState entity. A UserAccountState
 * contains the state information for an User account.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a User entity instance.
 *   UserId         - UserId for login.
 *   NumLoginTries  - Number of unsuccessful login attempts.
 *   IsFreeze       - Whether the account is freezed that the user temporary cannot
 *                    login, until a timeout for the freeze time.
 *   FreezeTime     - Time that the account is being freezed.
 *   LastLoginTime  - Time of last login of the user.
 *   LastLogoutTime - Time of last logout of the user.
 *   State          - State of the user account: Enabled,Disabled,Deleted.
 *   CreateTime     - Time of creation of the account
 *   CreateBy       - User ID of the user who created this account.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public class UserAccountState
  extends    AbstractEntity
  implements IUserAccountState
{ 
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4501358593338325808L;
	protected String  _userID;
	protected short   _numLoginTries  = 0;
	protected boolean _isFreeze       = false;
	protected Date    _freezeTime;
  protected Date    _lastLoginTime;
  protected Date    _lastLogoutTime;
  protected short   _state          = STATE_ENABLED;
  protected boolean _canDelete      = true;
  protected Date    _createTime;
  protected String  _createBy;

  public UserAccountState()
  {
  }

  public String getEntityDescr()
  {
    return getUserID() + "-" + getState();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ************************* Getters **********************************
 	public String  getUserID()
  {
    return _userID;
  }

	public short   getNumLoginTries()
  {
    return _numLoginTries;
  }

	public boolean isFreeze()
  {
    return _isFreeze;
  }

	public Date getFreezeTime()
  {
    return _freezeTime;
  }

  public Date getLastLoginTime()
  {
    return _lastLoginTime;
  }

  public Date getLastLogoutTime()
  {
    return _lastLogoutTime;
  }

  public short getState()
  {
    return _state;
  }

	public boolean canDelete()
  {
    System.out.println(" Returning from UserAccountState " + _canDelete);
    return _canDelete;
  }

  public Date getCreateTime()
  {
    return _createTime;
  }

  public String getCreateBy()
  {
    return _createBy;
  }

  // ************************* Setters **********************************
 	public void setUserID(String userID)
  {
    _userID = userID;
  }

	public void setNumLoginTries(short numLoginTries)
  {
    _numLoginTries = numLoginTries;
  }

	public void setIsFreeze(boolean isFreeze)
  {
    _isFreeze = isFreeze;
  }

	public void setFreezeTime(Date freezeTime)
  {
    _freezeTime = freezeTime;
  }

  public void setLastLoginTime(Date lastLoginTime)
  {
    _lastLoginTime = lastLoginTime;
  }

  public void setLastLogoutTime(Date lastLogoutTime)
  {
    _lastLogoutTime = lastLogoutTime;
  }

  public void setState(short state)
  {
    _state = state;
  }

  public void canDelete(boolean canDelete)
  {
    _canDelete = canDelete;
     System.out.println(" Returning from UserAccountState canDelete (boolean) " + _canDelete);
  }

  public void setCreateTime(Date createTime)
  {
    _createTime = createTime;
  }

  public void setCreateBy(String createBy)
  {
    _createBy = createBy;
  }
}