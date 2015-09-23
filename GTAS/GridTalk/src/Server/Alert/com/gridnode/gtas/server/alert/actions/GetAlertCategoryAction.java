/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertCategoryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-04     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.alert.GetAlertCategoryEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.alert.model.AlertCategory;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a AlertCategory.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAlertCategoryAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6127631868247477316L;
	public static final String ACTION_NAME = "GetAlertCategoryAction";

  protected Class getExpectedEventClass()
  {
    return GetAlertCategoryEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetAlertCategoryEvent getEvent = (GetAlertCategoryEvent)event;
    return ServiceLookupHelper.getAlertManager().getAlertCategoryByUId(getEvent.getUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertAlertCategoryToMap((AlertCategory)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetAlertCategoryEvent getEvent = (GetAlertCategoryEvent)event;
    return new Object[]
           {
             AlertCategory.ENTITY_NAME,
             String.valueOf(getEvent.getUid())
           };
  }
}