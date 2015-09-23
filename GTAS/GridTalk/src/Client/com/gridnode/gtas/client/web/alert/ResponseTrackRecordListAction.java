/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseTrackRecordListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-06     Daniel D'Cotta      Created

 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTResponseTrackRecordEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ResponseTrackRecordListAction extends EntityListAction
{
  private static final Object[] _columns = {
    IGTResponseTrackRecordEntity.SENT_DOC_TYPE,
    IGTResponseTrackRecordEntity.START_TRACK_DATE_XPATH,
    IGTResponseTrackRecordEntity.RESPONSE_DOC_TYPE,
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_RESPONSE_TRACK_RECORD;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "responseTrackRecord";
  }
}