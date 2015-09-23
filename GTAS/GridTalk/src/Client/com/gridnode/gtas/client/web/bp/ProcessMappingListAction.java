/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessMappingEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ProcessMappingListAction extends EntityListAction
{
  protected static final Object[] _columns = new Object[]{
      IGTProcessMappingEntity.PROCESS_DEF,
      IGTProcessMappingEntity.PARTNER_ID,
      IGTProcessMappingEntity.IS_INITIATING_ROLE,
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
    return IGTManager.MANAGER_PROCESS_MAPPING;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "processMapping";
  }
}