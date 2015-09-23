/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AccessRightListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-14     Andrew Hill         Created
 * 2003-07-08     Andrew Hill         Remove / refactor deprecated methods
 */
package com.gridnode.gtas.client.web.acl;

import com.gridnode.gtas.client.ctrl.IGTAccessRightEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class AccessRightListAction extends EntityListAction
{
  private static final Object[] _columns =
  { //20030708AH
    IGTAccessRightEntity.DESCRIPTION,
    IGTAccessRightEntity.ROLE,
    IGTAccessRightEntity.FEATURE,
    IGTAccessRightEntity.ACTION,
    IGTAccessRightEntity.DATA_TYPE
  };
    
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_ACCESS_RIGHT;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_ACCESS_RIGHT; //20030708AH
  }
}