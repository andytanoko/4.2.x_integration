/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTRegistryConnectInfoEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class RegistryConnectInfoListAction extends EntityListAction
{
  private static final Object[] _columns =
  { 
    IGTRegistryConnectInfoEntity.NAME,
    IGTRegistryConnectInfoEntity.PUBLISH_URL,
    IGTRegistryConnectInfoEntity.QUERY_URL,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  {
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_REGISTRY_CONNECT_INFO;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_REGISTRY_CONNECT_INFO;
  }

// 20031001 DDJ: Moved to BusinessEntityListAction  
//  protected void processPipeline( ActionContext actionContext,
//                                  RenderingContext rContext,
//                                  IRenderingPipeline rPipe)
//    throws GTClientException
//  {
//    ActionForward invokePublishForward = actionContext.getMapping().findForward("invokePublish");
//    if(invokePublishForward == null) throw new NullPointerException("invokePublishForward is null");
//    rPipe.addRenderer(new RegistryConnectInfoListDecoratorRenderer(rContext, invokePublishForward.getPath()));
//    super.processPipeline(actionContext, rContext, rPipe);
//  }
}