/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now under "navgroup_partner"
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated methods
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerGroupEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class PartnerGroupListAction extends EntityListAction
{
  private static final Object[] _columns =
  { //20030708AH
    IGTPartnerGroupEntity.NAME,
    IGTPartnerGroupEntity.DESCRIPTION,
    IGTPartnerGroupEntity.PARTNER_TYPE,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PARTNER_GROUP;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_PARTNER_GROUP; //20030708AH
  }
}