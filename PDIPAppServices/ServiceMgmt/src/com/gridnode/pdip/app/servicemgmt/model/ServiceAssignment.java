/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignment.java
 *
 ****************************************************************************
 * Date						Author              Changes
 ****************************************************************************
 * Feb 6, 2004		Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.model;

import java.util.Collection;
import java.util.Vector;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class ServiceAssignment extends AbstractEntity implements IServiceAssignment
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9201166758325471310L;
	protected String _userName;
  protected String _password;
  protected String _userType;
  protected Collection _webServiceUIds = new Vector();
  protected boolean _isPasswordModified; //transient data to check for modified password

  public ServiceAssignment()
  {
  }

  // *************** Methods from AbstractEntity ***************************

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_userName).append('/').append(_userType).append('/').append(_webServiceUIds).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ************************* Getters for attributes ***********************

  public String getPassword()
  {
    return _password;
  }

  public String getUserName()
  {
    return _userName;
  }

  public Collection getWebServiceUIds()
  {
    return _webServiceUIds;
  }

  public String getUserType()
  {
    return _userType;
  }

  //********************** Setters for attributes ***************************

  public void setPassword(String string)
  {
    _password = string;
  }

  public void setUserName(String string)
  {
    _userName = string;
  }

  public void setWebServiceUIds(Collection webServiceUIds)
  {
    _webServiceUIds = webServiceUIds;
  }

  public void setUserType(String userType)
  {
    _userType = userType;
  }

  public boolean isPasswordModified()
  {
    return _isPasswordModified;
  }

  public void setPasswordModified(boolean isPasswordModified)
  {
    _isPasswordModified = isPasswordModified;
  }

}
