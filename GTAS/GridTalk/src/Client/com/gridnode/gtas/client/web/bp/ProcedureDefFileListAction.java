/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-03-21     Andrew Hill         Support for paging
 * 2003-04-02     Andrew Hill         Implement appendRefreshParameters()
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ProcedureDefFileListAction extends EntityListAction
{
  private static final String TYPE_PARAM = "type"; //20030402AH

  protected static final Object[] _columns = new Object[]{
      IGTProcedureDefFileEntity.NAME,
      IGTProcedureDefFileEntity.DESCRIPTION,
      IGTProcedureDefFileEntity.TYPE,
      IGTProcedureDefFileEntity.FILE_NAME,
    };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected Integer getType(ActionContext actionContext)
  {
    String typeStr = actionContext.getRequest().getParameter(TYPE_PARAM); //20030402AH
    return StaticUtils.integerValue(typeStr);
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PROCEDURE_DEF_FILE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "procedureDefFile";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new ProcedureDefFileListDecoratorRenderer(rContext,getType(actionContext)));
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
      return manager.getListPager(type, IGTProcedureDefFileEntity.TYPE);
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
      return manager.getByKey(type, IGTProcedureDefFileEntity.TYPE);
    }
  }*/
}