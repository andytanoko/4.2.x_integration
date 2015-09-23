/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FeatureAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-14     Andrew Hill         Created
 * 2003-07-08     Andrew Hill         Remove / Refactor deprecated methods
 */
package com.gridnode.gtas.client.web.acl;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFeatureEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


public class FeatureListAction extends EntityListAction
{
  private static final Object[] _columns =
  { //20030708AH
    IGTFeatureEntity.FEATURE,
    IGTFeatureEntity.DESCRIPTION,
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030708AH
    return _columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_FEATURE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_FEATURE; //20030708AH
  }
}