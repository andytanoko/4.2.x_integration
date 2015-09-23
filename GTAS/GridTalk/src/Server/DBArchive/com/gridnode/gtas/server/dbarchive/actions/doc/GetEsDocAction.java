/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsDocAction.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 9 Nov 2006      Regina Zeng            Created
 */
package com.gridnode.gtas.server.dbarchive.actions.doc;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.dbarchive.doc.GetEsDocEvent;
import com.gridnode.gtas.model.dbarchive.doc.EsDocEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.*;

import com.gridnode.gtas.server.dbarchive.model.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.gtas.server.dbarchive.helpers.*;

/**
 * This Action class handles the retrieving of one Document instance.  It is used for estore.
 * The implementation is based on  GetEsPiAction.java
 */
public class GetEsDocAction extends AbstractGridTalkAction
{
  public static final String ACTION_NAME= "GetEsDocAction";

  // ******************* AbstractGetEntityAction methods *******************

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    return constructEventResponse(
      IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
      getErrorMessageParams(event),
      ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    Map retrieved = null;
    GetEsDocEvent getEvent = (GetEsDocEvent) event;
    DocumentMetaInfo doc = ActionHelper.getManager().findEsDoc(getEvent.getUid());
    retrieved = AbstractEntity.convertToMap(doc, EsDocEntityFieldID.getEntityFieldID(), null);

    if (retrieved == null)
    {
      return constructEventResponse(java.util.Collections.EMPTY_MAP);
    }

    return constructEventResponse(retrieved);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetEsDocEvent getEvent= (GetEsDocEvent) event;
    return new Object[] { String.valueOf(getEvent.getUid()) };
  }

  // ****************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return GetEsDocEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}