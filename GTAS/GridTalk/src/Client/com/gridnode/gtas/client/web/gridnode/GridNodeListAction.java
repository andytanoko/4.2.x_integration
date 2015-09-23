/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-19     Andrew Hill         Created
 * 2002-12-11     Andrew Hill         Autorefresh support
 * 2002-12-26     Andrew Hill         Use GridNodeUtils
 * 2003-03-21     Andrew Hill         Paging Support
 */
package com.gridnode.gtas.client.web.gridnode;

import java.util.Collection;

import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTGridNodeEntity;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;
 
public class GridNodeListAction extends EntityListAction
{
  protected static final Object[] _allColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.STATE,
    IGTGridNodeEntity.ACTIVATION_REASON,
  };

  protected static final Object[] _meColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.DT_CREATED,
  };

  protected static final Object[] _deletedColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
  };

  protected static final Object[] _activeColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.ACTIVATION_REASON,
    IGTGridNodeEntity.DT_CREATED,
    IGTGridNodeEntity.DT_REQ_ACTIVATE,
    IGTGridNodeEntity.DT_ACTIVATED,
  };

  protected static final Object[] _inactiveColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
    IGTGridNodeEntity.ACTIVATION_REASON,
    IGTGridNodeEntity.DT_CREATED,
    IGTGridNodeEntity.DT_ACTIVATED,
    IGTGridNodeEntity.DT_DEACTIVATED,
  };

  protected static final Object[] _gmColumns = new Object[]{
    IGTGridNodeEntity.ID,
    IGTGridNodeEntity.NAME,
  };

  protected Object[] _columns = new Object[]{
    _allColumns,
    _meColumns,
    _deletedColumns,
    _activeColumns,
    _inactiveColumns,
    _gmColumns,
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return GridNodeUtils.NAVGROUP;
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    return GridNodeUtils.appendRefreshParameters(actionContext,refreshUrl); //20021226AH
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Short state = GridNodeUtils.getState(actionContext);
    try
    {
      int stateIndex = state == null ? 0 : state.intValue() + 1;
      return (Object[])_columns[stateIndex];
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining which columns to include based on state:" + state,t);
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return GridNodeUtils.MANAGER_TYPE; //20021226AH
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "gridNode";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    Short state = GridNodeUtils.getState(actionContext);
    String deactivateUrl = null;
    if(IGTGridNodeEntity.STATE_ACTIVE.equals(state))
    {
      ActionForward deactivateForward = actionContext.getMapping().findForward("invokeDeactivate");
      if(deactivateForward != null)
      {
        deactivateUrl = GridNodeUtils.appendRefreshParameters(actionContext,
                                                              deactivateForward.getPath()); //20021226AH
      }
    }
    rPipe.addRenderer(new GridNodeListDecoratorRenderer(rContext,
                      GridNodeUtils.getState(actionContext),deactivateUrl));
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Short state = GridNodeUtils.getState(actionContext);
    if( state == null )
    {
      return manager.getAll();
    }
    else
    {
      return manager.getByKey(state,IGTGridNodeEntity.STATE);
    }
  }*/

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTManager manager = getManager(actionContext);
    Short state = GridNodeUtils.getState(actionContext);
    if( state == null )
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(state,IGTGridNodeEntity.STATE);
    }
  }

  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
    Short state = GridNodeUtils.getState(actionContext);
    if( state == null
        || (IGTGridNodeEntity.STATE_ME.equals(state))
        || (IGTGridNodeEntity.STATE_DELETED.equals(state))
        || (IGTGridNodeEntity.STATE_GM.equals(state)) )
    {
      listOptions.setAllowsSelection(false);
      listOptions.setDeleteURL(null);
    }
    else if(IGTGridNodeEntity.STATE_ACTIVE.equals(state))
    {
      listOptions.setAllowsSelection(true);
      listOptions.setDeleteURL(null);
    }
    else if(IGTGridNodeEntity.STATE_INACTIVE.equals(state))
    {
      listOptions.setAllowsSelection(true);
    }
    else
    {
      throw new java.lang.IllegalStateException("Unrecognised state:" + state);
    }
    return listOptions;
  }

  protected IDocumentRenderer getListViewRenderer(ActionContext actionContext,
                                                  RenderingContext rContext,
                                                  IListViewOptions listOptions,
                                                  Collection listItems)
    throws GTClientException
  {
    ListViewRenderer listViewRenderer = (ListViewRenderer)super.getListViewRenderer(actionContext,
                                        rContext,
                                        listOptions,
                                        listItems);
    //can override the Elr used here
    return listViewRenderer;
  }
}