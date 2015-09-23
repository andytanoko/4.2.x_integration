
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerFunctionEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class PartnerFunctionListAction extends EntityListAction
{
  private static final Object[] _columns = {
      IGTPartnerFunctionEntity.ID,
      IGTPartnerFunctionEntity.DESCRIPTION,
      IGTPartnerFunctionEntity.TRIGGER_ON,
                        };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PARTNER_FUNCTION;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "partnerFunction";
  }
}