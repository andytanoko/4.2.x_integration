
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRfcEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class RfcListAction extends EntityListAction
{
  private static final Object[] _columns = {
    IGTRfcEntity.NAME,
    IGTRfcEntity.DESCRIPTION,
    IGTRfcEntity.HOST,
    IGTRfcEntity.PORT_NUMBER,
    IGTRfcEntity.USE_COMMAND_FILE,
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
    return IGTManager.MANAGER_RFC;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "rfc";
  }
}