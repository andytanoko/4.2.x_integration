/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerFunctionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.partnerfunction.GetPartnerFunctionEvent;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.gtas.server.partnerfunction.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a PartnerFunction.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetPartnerFunctionAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3808934565749101970L;
	public static final String ACTION_NAME = "GetPartnerFunctionAction";

  protected Class getExpectedEventClass()
  {
    return GetPartnerFunctionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetPartnerFunctionEvent getEvent = (GetPartnerFunctionEvent)event;
    return ActionHelper.getManager().findPartnerFunction(getEvent.getPartnerFunctionUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertPartnerFunctionToMap((PartnerFunction)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetPartnerFunctionEvent updEvent = (GetPartnerFunctionEvent)event;
    return new Object[]
           {
             PartnerFunction.ENTITY_NAME,
             updEvent.getPartnerFunctionUid()
           };
  }
}