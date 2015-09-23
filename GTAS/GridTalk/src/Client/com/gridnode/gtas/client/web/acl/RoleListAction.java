/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RoleListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-14     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now under navgroup_server
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated methods n' stuff
 */
package com.gridnode.gtas.client.web.acl;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRoleEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class RoleListAction extends EntityListAction
{

  Object[] _columns =
  { //20030708AH
    IGTRoleEntity.ROLE,
    IGTRoleEntity.DESCRIPTION,
  };

  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_ROLE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_ROLE; //20030708AH
  }
}