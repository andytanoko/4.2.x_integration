/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGnCategoryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.events.gridnode.GetGnCategoryEvent;
import com.gridnode.gtas.server.gridnode.model.GnCategory;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of one GnCategory.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetGnCategoryAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8787588846763292384L;
	public static final String ACTION_NAME = "GetGnCategoryAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertGnCategoryToMap((GnCategory)entity);
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetGnCategoryEvent getEvent = (GetGnCategoryEvent)event;

    return ActionHelper.findGnCategoryByCode(getEvent.getCategoryCode());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetGnCategoryEvent getEvent = (GetGnCategoryEvent)event;
    return new Object[]
           {
             String.valueOf(getEvent.getCategoryCode()),
           };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetGnCategoryEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

}