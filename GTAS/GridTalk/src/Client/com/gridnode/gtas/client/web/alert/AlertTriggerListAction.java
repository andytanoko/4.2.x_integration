/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertTriggerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created

 */
package com.gridnode.gtas.client.web.alert;

import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAlertTriggerEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTListPager;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;

public class AlertTriggerListAction extends EntityListAction
{
  static final String LEVEL_PARAM = "level";
  
  private static final Object[] _allLevelColumns =
  {
    IGTAlertTriggerEntity.LEVEL,
    IGTAlertTriggerEntity.ALERT_TYPE,
    IGTAlertTriggerEntity.ALERT_UID,
  };
  
  private static final Object[][] _columns =
  {
    { //Level 0
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
    { //Level 1
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
    { //Level 2
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
    { //Level 3
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_TYPE,
      IGTAlertTriggerEntity.PARTNER_GROUP,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
    { //Level 4
      IGTAlertTriggerEntity.DOC_TYPE,
      IGTAlertTriggerEntity.PARTNER_ID,
      IGTAlertTriggerEntity.ALERT_TYPE,
      IGTAlertTriggerEntity.ALERT_UID,
    },
  };

  public static Integer getLevel(HttpServletRequest request)
  {
    String levelStr = request.getParameter(LEVEL_PARAM);
    Integer level = StaticUtils.integerValue(levelStr);
    return level;
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Integer level = getLevel(actionContext.getRequest());
    if(level != null)
    {
      return _columns[level.intValue()];
    }
    else
    {
      return _allLevelColumns;
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_ALERT_TRIGGER;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_ALERT_TRIGGER;
  }
  
  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
    throws GTClientException
  { 
    Integer level = getLevel(actionContext.getRequest());
    if(level != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,LEVEL_PARAM,level.toString());
    }
    return refreshUrl;
  }
  
  protected IGTListPager getListPager(ActionContext actionContext)
    throws GTClientException
  {
    IGTManager manager = getManager(actionContext);
    Integer level = getLevel(actionContext.getRequest());
    if( level == null )
    {
      return manager.getListPager();
    }
    else
    {
      return manager.getListPager(level,IGTAlertTriggerEntity.LEVEL);
    }
  }
  
  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
    rPipe.addRenderer(new AlertTriggerListDecoratorRenderer(rContext) );
  }
}