/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.acl;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class RoleAForm extends GTActionFormBase
{
  private String _description;
  private String _role;
  //private boolean _canDelete;

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getRole()
  {
    return _role;
  }

  public void setRole(String value)
  {
    _role = value;
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