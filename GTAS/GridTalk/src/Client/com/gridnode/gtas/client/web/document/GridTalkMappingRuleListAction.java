/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-17     Andrew Hill         Created
 * 2002-09-10     Andrew Hill         Modified to leverage new listview functionality
 * 2002-12-10     Andrew Hill         Fix to add decorator back & to use static columns arrays
 * 2002-12-11     Andrew Hill         Autorefresh stuff
 * 2003-03-21     Andrew Hill         Paging Support
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTGridTalkMappingRuleEntity;
import com.gridnode.gtas.client.ctrl.IGTGridTalkMappingRuleManager;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class GridTalkMappingRuleListAction extends EntityListAction
{
  private static final Object[] _allColumns = new Object[]
  {
    IGTGridTalkMappingRuleEntity.NAME,
    IGTGridTalkMappingRuleEntity.DESCRIPTION,
    IGTGridTalkMappingRuleEntity.IS_HEADER_TRANSFORMATION,
  };

  private static final Object[] _headerColumns = new Object[]
  {
    IGTGridTalkMappingRuleEntity.NAME,
    IGTGridTalkMappingRuleEntity.DESCRIPTION,
    "mappingRule.mappingFile",
    IGTGridTalkMappingRuleEntity.SOURCE_DOC_TYPE,
    IGTGridTalkMappingRuleEntity.TARGET_DOC_TYPE,
  };

  private static final Object[] _contentColumns = new Object[]
  {
    IGTGridTalkMappingRuleEntity.NAME,
    IGTGridTalkMappingRuleEntity.DESCRIPTION,
    "mappingRule.type",
    "mappingRule.mappingFile",
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Boolean headerTransformation = isHeaderTransformation(actionContext);

    if(headerTransformation == null)
    {
      return _allColumns;
    }
    else
    {
      if(headerTransformation.booleanValue())
      { // Columns for listview showing only header mrs
        return _headerColumns;
      }
      else
      { // Columns for listview showing only content mrs
        return _contentColumns;
      }
    }
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  { //20030321AH
    IGTGridTalkMappingRuleManager manager = (IGTGridTalkMappingRuleManager)getManager(actionContext);
    Boolean isHt = isHeaderTransformation(actionContext);
    if(isHt == null)
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(isHt, IGTGridTalkMappingRuleEntity.IS_HEADER_TRANSFORMATION);
    }
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTGridTalkMappingRuleManager manager = (IGTGridTalkMappingRuleManager)getManager(actionContext);
    Boolean isHt = isHeaderTransformation(actionContext);
    if(isHt == null)
    {
      return manager.getAll();
    }
    else
    {
      Collection rules = manager.getAllOfHeaderTransformation(isHt);
      return rules;
    }
  }*/

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    String param = "headerTransformation";
    Boolean value = isHeaderTransformation(actionContext);
    if(value != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,param,value.toString());
    }
    return refreshUrl;
  }

  private Boolean isHeaderTransformation(ActionContext actionContext)
  {
    String ht = actionContext.getRequest().getParameter("headerTransformation");
    if(ht == null)
    {
      ht = (String)actionContext.getRequest().getAttribute("gridTalkMappingRule.headerTransformation");
    }
    if(ht != null)
    {
      return StaticUtils.booleanValue(ht);
    }
    return null;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_GRIDTALK_MAPPING_RULE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "gridTalkMappingRule";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new GridTalkMappingRuleListDecoratorRenderer(rContext,
                      isHeaderTransformation(actionContext)));
  }
}