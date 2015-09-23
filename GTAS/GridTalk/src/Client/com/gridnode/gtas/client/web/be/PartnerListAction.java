/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-17     Andrew Hill         Created
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated methods
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class PartnerListAction extends EntityListAction
{
  Object[] _columns =
  { //20030708AH
    IGTPartnerEntity.PARTNER_ID,
    IGTPartnerEntity.PARTNER_TYPE,
    IGTPartnerEntity.PARTNER_GROUP,
    IGTPartnerEntity.DESCRIPTION,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PARTNER;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_PARTNER; //20030708AH
  }
}