/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-03-21     Andrew Hill         Support for Paging
 * 2003-04-02     Andrew Hill         Implement appendRefreshParameters()
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTUserProcedureEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class UserProcedureListAction extends EntityListAction
{
  private static final String TYPE_PARAM = "type"; //20030402AH

  protected static final Object[] _columns = new Object[]{
      IGTUserProcedureEntity.NAME,
      IGTUserProcedureEntity.DESCRIPTION,
      IGTUserProcedureEntity.PROC_TYPE,
      IGTUserProcedureEntity.PROC_DEF_FILE,
    };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected Integer getType(ActionContext actionContext)
  {
    String typeStr = actionContext.getRequest().getParameter("type");
    return StaticUtils.integerValue(typeStr);
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_USER_PROCEDURE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "userProcedure";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new UserProcedureListDecoratorRenderer(rContext,getType(actionContext)));
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20030402AH
    Integer type = getType(actionContext);
    if(type != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,TYPE_PARAM, "" + type);
    }
    return refreshUrl;
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTManager manager = getManager(actionContext);
    Integer type = getType(actionContext);
    if(type == null)
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(type, IGTUserProcedureEntity.PROC_TYPE);
    }
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Integer type = getType(actionContext);
    if(type == null)
    {
      return manager.getAll();
    }
    else
    {
      return manager.getByKey(type, IGTUserProcedureEntity.PROC_TYPE);
    }
  }*/
}