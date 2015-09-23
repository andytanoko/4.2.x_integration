/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationRecordListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-15     Andrew Hill         Created
 * 2002-11-19     Andrew Hill         Filtering
 * 2002-12-11     Andrew Hill         Refresh support
 * 2002-12-02     Andrew Hill         Bodgy tweaking of avail filterings
 * 2002-12-26     Andrew Hill         Pass url to decorator constructor & farm out some common code
 * 2003-03-21     ANdrew Hill         Support for Paging
 */
package com.gridnode.gtas.client.web.activation;

import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTActivationRecordEntity;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class ActivationRecordListAction extends EntityListAction
{
  private static final String OTHER_URL_MAPPING = "submit"; //20021226AH

  private static final Object[] _allColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    IGTActivationRecordEntity.CURRENT_TYPE,
    "activationDetails.activateReason",
  };

  protected static final Object[] _activationColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    "activationDetails.activateReason",
    IGTActivationRecordEntity.DT_REQUESTED,
  };

  protected static final Object[] _approvedColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    "activationDetails.activateReason",
    IGTActivationRecordEntity.DT_REQUESTED,
    IGTActivationRecordEntity.DT_APPROVED,
  };

  protected static final Object[] _deniedColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    "activationDetails.activateReason",
    IGTActivationRecordEntity.DT_REQUESTED,
    IGTActivationRecordEntity.DT_DENIED,
  };

  protected static final Object[] _abortedColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    "activationDetails.activateReason",
    IGTActivationRecordEntity.DT_REQUESTED,
    IGTActivationRecordEntity.DT_ABORTED,
  };

  protected static final Object[] _deactivatedColumns = {
    IGTActivationRecordEntity.GRIDNODE_ID,
    IGTActivationRecordEntity.GRIDNODE_NAME,
    IGTActivationRecordEntity.DT_REQUESTED,
    IGTActivationRecordEntity.DT_DEACTIVATED,
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return ActivationUtils.NAVGROUP; //20021226AH
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      String filterType = ActivationUtils.getFilterType(actionContext);
      if("".equals(filterType) || (filterType == null) )
      {
        String summaryType = ActivationUtils.getSummaryType(actionContext);
        if("activation".equals(summaryType))
        {
          return _activationColumns;
        }
        else if("deactivation".equals(summaryType))
        {
          return _deactivatedColumns;
        }
        else
        {
          return _allColumns;
        }
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_INCOMING_ACTIVATION.equals(filterType))
      {
        return _activationColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_ACTIVATION.equals(filterType))
      {
        return _activationColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_INCOMING_DEACTIVATION.equals(filterType))
      {
        return _deactivatedColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_OUTGOING_DEACTIVATION.equals(filterType))
      {
        return _deactivatedColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_APPROVED.equals(filterType))
      {
        return _approvedColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_DENIED.equals(filterType))
      {
        return _deniedColumns;
      }
      else if(IGTActivationRecordEntity.FILTER_TYPE_ABORTED.equals(filterType))
      {
        return _abortedColumns;
      }
      else
      {
        throw new java.lang.IllegalStateException("Unrecognised filterType:" + filterType);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting columns for activationRecord listview",t);
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return ActivationUtils.MANAGER_TYPE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "activationRecord";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    String filterType = ActivationUtils.getFilterType(actionContext);
    String summaryType = ActivationUtils.getSummaryType(actionContext); //20021202AH
    ActionForward otherForward = actionContext.getMapping().findForward(OTHER_URL_MAPPING); //20021226AH
    String otherUrl = otherForward == null ? null : ActivationUtils.appendRefreshParameters(actionContext,otherForward.getPath()); //20021226AH
    rPipe.addRenderer(new ActivationRecordListDecoratorRenderer(rContext,filterType,summaryType,otherUrl));
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTManager manager = getManager(actionContext);
    String filterType = ActivationUtils.getFilterType(actionContext);
    if( filterType == null )
    {
      String summaryType = ActivationUtils.getSummaryType(actionContext);
      if("activation".equals(summaryType))
      {
        return manager.getListPager(IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION,
                                IGTActivationRecordEntity.CURRENT_TYPE);
      }
      else if("deactivation".equals(summaryType))
      {
        return manager.getListPager(IGTActivationRecordEntity.CURRENT_TYPE_DEACTIVATION,
                                IGTActivationRecordEntity.CURRENT_TYPE);
      }
      else
      {
        return manager.getListPager();
      }
    }
    else
    {
      return manager.getListPager(filterType,IGTActivationRecordEntity.FILTER_TYPE);
    }
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    String filterType = ActivationUtils.getFilterType(actionContext);
    if( filterType == null )
    {
      String summaryType = ActivationUtils.getSummaryType(actionContext);
      if("activation".equals(summaryType))
      {
        return manager.getByKey(IGTActivationRecordEntity.CURRENT_TYPE_ACTIVATION,
                                IGTActivationRecordEntity.CURRENT_TYPE);
      }
      else if("deactivation".equals(summaryType))
      {
        return manager.getByKey(IGTActivationRecordEntity.CURRENT_TYPE_DEACTIVATION,
                                IGTActivationRecordEntity.CURRENT_TYPE);
      }
      else
      {
        return manager.getAll();
      }
    }
    else
    {
      return manager.getByKey(filterType,IGTActivationRecordEntity.FILTER_TYPE);
    }
  }*/

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    return ActivationUtils.appendRefreshParameters(actionContext, refreshUrl);
  }
}