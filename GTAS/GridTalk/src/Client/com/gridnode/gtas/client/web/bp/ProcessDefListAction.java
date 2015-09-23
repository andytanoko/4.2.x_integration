/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-08-20     Andrew Hill         Removed deprecated getNavgroup() method, added userTrackingIdentifier
 * 2003-09-04     Guo Jianyu          Removed userTrackingIdentifier from _columns
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ProcessDefListAction extends EntityListAction
{
  protected static final Object[] _columns = new Object[]
  {
    IGTProcessDefEntity.DEF_NAME,
    IGTProcessDefEntity.PROCESS_TYPE,
    IGTProcessDefEntity.RNIF_VERSION,
    IGTProcessDefEntity.PROCESS_INDICATOR_CODE,
//    IGTProcessDefEntity.USER_TRACKING_IDENTIFIER, //20030820AH
  };

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PROCESS_DEF;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "processDef";
  }
}