/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-09-23     Andrew Hill         Renamed action to actionName to avoid conflict in JS
 */
package com.gridnode.gtas.client.web.acl;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class AccessRightAForm extends GTActionFormBase
{
  private String _description;
  private String _feature;
  private String _dataType;
  private String _action;
  private String _criteria; //?
  private String _roleUid;


  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getFeature()
  {
    return _feature;
  }

  public void setFeature(String value)
  {
    _feature = value;
  }

  public String getDataType()
  {
    return _dataType;
  }

  public void setDataType(String value)
  {
    _dataType = value;
  }

  public String getActionName()
  {
    return _action;
  }

  public void setActionName(String value)
  {
    _action = value;
  }

  public String getCriteria()
  {
    return _criteria;
  }

  public void setCriteria(String value)
  {
    _criteria = value;
  }

  public String getRole()
  {
    return _roleUid;
  }

  public void setRole(String value)
  {
    _roleUid = value;
  }

  /*public boolean isCanDelete()
  {
    return _canDelete;
  }

  public void setCanDelete(boolean flag)
  {
    _canDelete = flag;
  }*/

}