/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFieldValueCollectionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 20 Oct 2005		Sumedh C.						Created
 */
package com.gridnode.gtas.server.dbarchive.actions.searchpage;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.server.dbarchive.model.FieldValueCollection;
import com.gridnode.gtas.model.dbarchive.searchpage.FieldValueCollectionEntityFieldID;
import com.gridnode.gtas.server.dbarchive.helpers.*;
import com.gridnode.gtas.events.dbarchive.searchpage.*;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

public class GetFieldValueCollectionAction
  extends    AbstractGetEntityAction
{
  public static final String ACTION_NAME = "GetFieldValueCollectionAction";

  protected Class getExpectedEventClass()
  {
    return GetFieldValueCollectionEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
  	GetFieldValueCollectionEvent getEvent = (GetFieldValueCollectionEvent) event;
  	Long uid = getEvent.getUid();
 		return ActionHelper.getManager().getProcessDef(uid);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
  	return AbstractEntity.convertToMap(
  	                          entity,
  	                          FieldValueCollectionEntityFieldID.getEntityFieldID(),
  	                          null);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetFieldValueCollectionEvent updEvent = (GetFieldValueCollectionEvent)event;
    return new Object[]
           {
             FieldValueCollection.ENTITY_NAME,
             updEvent.getUid()
           };
  }
}