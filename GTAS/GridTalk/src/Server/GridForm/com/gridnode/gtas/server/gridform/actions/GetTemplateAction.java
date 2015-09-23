/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTemplateAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.events.gridform.GetTemplateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.gridform.GFTemplateEntityFieldID;
import com.gridnode.gtas.server.gridform.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GridTalkEJBAction;

import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerHome;
import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerObj;
import com.gridnode.pdip.app.gridform.exceptions.*;
import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.*;

/**
 * This Action class handles the retrieving of a GF Template entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetTemplateAction extends GridTalkEJBAction
{
  public IEventResponse perform(IEvent event) throws EventException
  {
    IEventResponse response = null;
    try
    {
      // Init.
      IGFManagerObj mgr = getManager();
      GetTemplateEvent getTemplateEvent = (GetTemplateEvent)event;
      if (getTemplateEvent == null)
      {
        throw new EventException("Event is null");
      }

      GFTemplate entity = null;
      if (getTemplateEvent.getTemplateUID() == null)
        entity = mgr.findGFTemplate(getTemplateEvent.getTemplateName());
      else
        entity = mgr.findGFTemplate(getTemplateEvent.getTemplateUID());

      // No error.
      response = constructEventResponse(entity);
    }
    catch (NestingException ex)
    {
      // Error.
      response = constructEventResponse(ex);
    }
    catch (Throwable ex)
    {
      // Error.
      response = constructEventResponse(new SystemException(ex));
    }
    return response;
  }

  private IEventResponse constructEventResponse()
  {
    return new BasicEventResponse(IErrorCode.NO_ERROR, new Object[]{});
  }

  private IEventResponse constructEventResponse(GFTemplate entity)
  {
    return new BasicEventResponse(
               IErrorCode.NO_ERROR,
               null,
               entity.convertToMap(entity, GFTemplateEntityFieldID.getEntityFieldID(), null));
  }

  private IEventResponse constructEventResponse(NestingException ex)
  {
    BasicEventResponse response = null;

    response = new BasicEventResponse(
                   IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
                   new Object[]{},
                   (short)-1,
                   ex.getLocalizedMessage(),
                   ex.getStackTraceString());

    Logger.err("[GetTemplateAction.perform] Event Error", ex);
    return response;
  }

  private IGFManagerObj getManager() throws ServiceLookupException
  {
    return (IGFManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGFManagerHome.class.getName(),
                                                                                        IGFManagerHome.class,
                                                                                        new Object[0]);
  }
}