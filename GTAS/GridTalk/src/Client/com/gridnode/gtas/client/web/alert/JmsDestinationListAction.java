/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16 Jan 06			SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTJmsDestinationEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerEntity;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityListAction;


/**
 * This class implementation is based on ParterListAction.java
 *
 */
public class JmsDestinationListAction extends EntityListAction
{
  Object[] COLUMNS =
  { 
    IGTJmsDestinationEntity.NAME,
    IGTJmsDestinationEntity.TYPE,
    IGTJmsDestinationEntity.JNDI_NAME
  };
  
  protected Object[] getColumnReferences(ActionContext actionContext)
  { 
    return COLUMNS;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_JMS_DESTINATION;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_JMS_DESTINATION;
  }
}