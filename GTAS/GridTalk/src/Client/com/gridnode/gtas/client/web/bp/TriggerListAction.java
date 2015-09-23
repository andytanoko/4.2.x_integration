/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TriggerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Daniel D'Cotta      Created
 * 2002-10-15     Andrew Hill         Filtered TriggerLevel views
 * 2002-12-11     Andrew Hill         appendRefreshParameters()
 * 2003-03-21     Andrew Hill         Support for paging
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTTriggerEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class TriggerListAction extends EntityListAction
{

  //@todo - 20030321AH - Ive noticed some of the columns reference non-cached foreign entities
  //It would be a good idea to track down which ones and either remove those columns or have the
  //b-tier make them cached refs. Currently this listview is very slow as each row needs to fire
  //several events to deref the fields display values!
  protected static final Object[] _allColumns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.TRIGGER_LEVEL,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
      IGTTriggerEntity.DOC_TYPE,
      IGTTriggerEntity.PARTNER_TYPE,
      IGTTriggerEntity.PARTNER_GROUP,
      IGTTriggerEntity.PARTNER_ID,
    };
  protected static final Object[] _level0Columns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
    };
  protected static final Object[] _level1Columns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
      IGTTriggerEntity.DOC_TYPE,
    };
  protected static final Object[] _level2Columns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
      IGTTriggerEntity.DOC_TYPE,
      IGTTriggerEntity.PARTNER_TYPE,
    };
  protected static final Object[] _level3Columns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
      IGTTriggerEntity.DOC_TYPE,
      IGTTriggerEntity.PARTNER_TYPE,
      IGTTriggerEntity.PARTNER_GROUP,
    };
  protected static final Object[] _level4Columns = new Object[]{
      IGTTriggerEntity.TRIGGER_TYPE,
      IGTTriggerEntity.PARTNER_FUNCTION_ID,
      IGTTriggerEntity.DOC_TYPE,
      IGTTriggerEntity.PARTNER_TYPE,
      IGTTriggerEntity.PARTNER_GROUP,
      IGTTriggerEntity.PARTNER_ID,
    };
  protected static final Object[] _columns = new Object[]{
    _allColumns,
    _level0Columns,
    _level1Columns,
    _level2Columns,
    _level3Columns,
    _level4Columns,
  };

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_bp";
  }

  protected Integer getTriggerLevel(ActionContext actionContext)
  {
    String triggerLevelStr = actionContext.getRequest().getParameter("triggerLevel");
    return StaticUtils.integerValue(triggerLevelStr);
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { //20021211AH - Support refresh
    String param = "triggerLevel";
    Integer value = getTriggerLevel(actionContext);
    if(value != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,param,value.toString());
    }
    return refreshUrl;
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Integer triggerLevelInteger = getTriggerLevel(actionContext);
    try
    {
      int triggerLevel = triggerLevelInteger == null ? 0 : triggerLevelInteger.intValue() + 1;
      return (Object[])_columns[triggerLevel];
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining which columns to include based on triggerLevel:" + triggerLevelInteger,t);
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_TRIGGER;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "trigger";
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new TriggerListDecoratorRenderer(rContext,getTriggerLevel(actionContext)));
  }

  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Integer triggerLevel = getTriggerLevel(actionContext);
    if( triggerLevel == null )
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(triggerLevel,IGTTriggerEntity.TRIGGER_LEVEL);
    }
  }

  /*20030321AH - protected Collection getListItems(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Integer triggerLevel = getTriggerLevel(actionContext);
    if( triggerLevel == null )
    {
      return manager.getAll();
    }
    else
    {
      return manager.getByKey(triggerLevel,IGTTriggerEntity.TRIGGER_LEVEL);
    }
  }*/

}