/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserAccount.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2002    NSL/OHL             Created
 * Jun 14 2002    Neo Sok Lay         Add method to check password.
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 */
package com.gridnode.pdip.app.user.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.util.PasswordMask;

/**
 * This is an object model for UserAccount entity. A UserAccount contains the user-related
 * information.<P>
 *
 * The Model:<BR><PRE>
 *   UId        - UID for a UserAccount entity instance.
 *   Id         - UserId for login.
 *   Name       - Name of the User.
 *   Password   - User password for login.
 *   Phone      - Telephone number of the User.
 *   Email      - Email of User.
 *   Property   - User property.
 *   AccountState - UserAccountState record of the UserAccount.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 * @author Ooi Hui Linn
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class UserAccount
  extends    AbstractEntity
  implements IUserAccount
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6664635977688522547L;
	protected String  _id;
  protected String  _name;
  protected String  _password;
  protected String  _phone;
  protected String  _email;
  protected String  _property;
  protected UserAccountState _accountState;

  private static int _passwordMaskLength = -1;

  public UserAccount()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return new StringBuffer(getId()).append('/').append(getUserName()).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public int getPasswordMaskLength()
  {
    if (_passwordMaskLength == -1)
    {
      FieldMetaInfo meta = this.getFieldMetaInfo(PASSWORD);
      _passwordMaskLength = meta.getLength();
    }
    return _passwordMaskLength;
  }

  // ******************** Getters for attributes ***************************

  public String getUserName()
  {
    return _name;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getPhone()
  {
    return _phone;
  }

  public String getEmail()
  {
   return _email;
  }

  public String getProperty()
  {
    return _property;
  }

  public String getId()
  {
    return _id;
  }

  public UserAccountState getAccountState()
  {
    return _accountState;
  }

  // ******************** Setters for attributes ***************************

  public void setUserName(String name)
  {
    _name = name;
  }

  public void setPassword(PasswordMask password)
  {
    password.applyMaskLength(getPasswordMaskLength());
    _password = password.toString();
  }

  public void setPhone(String phone)
  {
    _phone = phone;
  }

  public void setId(String id)
  {
    _id = id;
  }

  public void setEmail (String email)
  {
    _email = email;
  }

  public void setProperty (String property)
  {
    _property = property;
  }

  public void setAccountState(UserAccountState accountState)
  {
    _accountState = accountState;
  }

  public boolean isPasswordMatch(PasswordMask password)
  {
    password.applyMaskLength(getPasswordMaskLength());
    return (password.toString().equals(_password));
  }

  public boolean isPasswordMatch(String maskedPassword)
  {
    PasswordMask mask = PasswordMask.newMaskedPassword(String.valueOf(maskedPassword));
    return isPasswordMatch(mask);
  }
}