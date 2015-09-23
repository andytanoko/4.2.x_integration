/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBusinessEntityAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.bizreg.helpers.ActionHelper;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityEvent;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one BusinessEntity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBusinessEntityAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9025294996390946487L;
	public static final String ACTION_NAME = "GetBusinessEntityAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertBusinessEntityToMap((BusinessEntity)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetBusinessEntityEvent getEvent = (GetBusinessEntityEvent)event;

    return ActionHelper.getBizRegManager().findBusinessEntity(getEvent.getBeUID());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetBusinessEntityEvent getEvent = (GetBusinessEntityEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getBeUID()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetBusinessEntityEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}