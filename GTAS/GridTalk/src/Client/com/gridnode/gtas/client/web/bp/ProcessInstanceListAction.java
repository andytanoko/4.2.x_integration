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
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-03-21     Andrew Hill         Support Paging
 * 2003-08-20     Andrew Hill         Remove deprecated getNavgroup and add column for userTrackingId
 * 2004-01-12     Daniel D'Cotta      Added Sorting support
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessInstanceEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IListViewOptions;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.ListViewOptionsImpl;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ProcessInstanceListAction extends EntityListAction
{
  protected static final Object[] _columns = new Object[]
  {
    IGTProcessInstanceEntity.PROCESS_INSTANCE_ID,
    IGTProcessInstanceEntity.STATE,
    IGTProcessInstanceEntity.START_TIME,
    IGTProcessInstanceEntity.END_TIME,
    IGTProcessInstanceEntity.IS_FAILED,
    IGTProcessInstanceEntity.USER_TRACKING_IDENTIFIER, //20030820AH
  };

  protected int _sortColumn = 0;
  protected boolean _sortAscending = true;
  protected boolean _newSort = true;
  
  protected String getProcessDefName(ActionContext actionContext)
  {
    return actionContext.getRequest().getParameter("processDefName");
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  {
    String param = "processDefName";
    String value = getProcessDefName(actionContext);
    if(value != null && value.length() != 0)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, param, value);
    }

    // 20040112 DDJ: Sorting
    initialiseSorting(actionContext);      
    if(_sortColumn > 0)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "sortColumn", "" + _sortColumn);
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl, "sortAscending", "" + _sortAscending);
    }

    return refreshUrl;
  }
  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_PROCESS_INSTANCE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "processInstance";
  }

  protected void processPipeline(ActionContext actionContext,
                                 RenderingContext rContext,
                                 IRenderingPipeline rPipe)
    throws GTClientException
  {
    ActionForward invokeAbortForward = actionContext.getMapping().findForward("invokeAbort");
    if (invokeAbortForward == null)
      throw new NullPointerException("invokeAbortForward is null"); //20030424AH
    rPipe.addRenderer(new ProcessInstanceListDecoratorRenderer(rContext, invokeAbortForward.getPath()));
    super.processPipeline(actionContext, rContext, rPipe);
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { 
    // 20040112 DDJ: for Sorting
    initialiseSorting(actionContext);
    Number[] sortField = null; 
    boolean[] sortAscending = null;
    if(_sortColumn > 0)
    {
      Object[] columnReferences = getColumnReferences(actionContext);
      // Currently only support single sort field
      sortField = new Number[1]; 
      sortAscending = new boolean[1];
      sortField[0] = (Number)columnReferences[_sortColumn - 1]; 
      sortAscending[0] = _sortAscending;
    }
    
    //20030321AH
    IGTManager manager = getManager(actionContext);
    String processDefName = getProcessDefName(actionContext);
    if( processDefName == null )
    {
      //return manager.getListPager();                                          // 20040112 DDJ: Sorting
      return manager.getListPager(null, null, false, sortField, sortAscending); // 20040112 DDJ: Sorting
    }
    else
    {
      //return manager.getListPager(processDefName, IGTProcessInstanceEntity.PROCESS_DEF_NAME);                                 // 20040112 DDJ: Sorting
      return manager.getListPager(processDefName, IGTProcessInstanceEntity.PROCESS_DEF_NAME, false, sortField, sortAscending);  // 20040112 DDJ: Sorting
    }
  }
  
  // 20040112 DDJ: Sorting
  protected IListViewOptions getListOptions(ActionContext actionContext)
    throws GTClientException
  {
    ListViewOptionsImpl listOptions = (ListViewOptionsImpl)super.getListOptions(actionContext);
    listOptions.setAllowsSorting(true);

    return listOptions;
  }

  // 20040112 DDJ: Sorting
  protected void initialiseSorting(ActionContext actionContext)
  {
    int sortColumn = StaticUtils.primitiveIntValue(actionContext.getRequest().getParameter("sortColumn"));
    int newSortColumn = StaticUtils.primitiveIntValue(actionContext.getRequest().getParameter("newSortColumn"));
    boolean sortAscending = StaticUtils.primitiveBooleanValue(actionContext.getRequest().getParameter("sortAscending"));
    if(newSortColumn > 0)
    {
      _sortColumn = newSortColumn;
      if(newSortColumn == sortColumn)
      {
        _sortAscending = !sortAscending; 
      }
      else
      {
        _sortAscending = true; 
      }
      _newSort = true;
    }
    else
    {
      _sortColumn = sortColumn;
      _sortAscending = sortAscending; 
      _newSort = false;  
    }
  }

}