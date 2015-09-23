/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignmentAForm.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class ServiceAssignmentAForm extends GTActionFormBase
{
  private String _userName;
  private String _password;
  private String _userType;
  private String _webServiceUIds[] =new String[]{};  

  public String getUserPassword()
  {
    return _password;
  }

  public String getUserName()
  {
    return _userName;
  }

  public String[] getWebServiceUids()
  {
    return _webServiceUIds;
  }

  public String getUserType()
  {
    return _userType;
  }

  //********************** Setters for attributes ***************************

  public void setUserPassword(String string)
  {
    _password = string;
  }

  public void setUserName(String string)
  {
    _userName = string;
  }

  public void setWebServiceUids(String[] webServiceUIds)
  {
    _webServiceUIds = webServiceUIds;
  }

  public void setUserType(String userType)
  {
    _userType = userType;
  }
 
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _webServiceUIds = StaticUtils.EMPTY_STRING_ARRAY;
  }

}