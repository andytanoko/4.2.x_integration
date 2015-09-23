/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRfcAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.backend.GetRfcEvent;
import com.gridnode.gtas.server.backend.model.Rfc;
import com.gridnode.gtas.server.backend.helpers.ActionHelper;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a Rfc.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetRfcAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1748460296176523262L;
	public static final String ACTION_NAME = "GetRfcAction";

  protected Class getExpectedEventClass()
  {
    return GetRfcEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetRfcEvent getEvent = (GetRfcEvent)event;
    return ActionHelper.getManager().findRfc(getEvent.getRfcUid());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertRfcToMap((Rfc)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetRfcEvent getEvent = (GetRfcEvent)event;
    return new Object[]
           {
             Rfc.ENTITY_NAME,
             String.valueOf(getEvent.getRfcUid())
           };
  }
}