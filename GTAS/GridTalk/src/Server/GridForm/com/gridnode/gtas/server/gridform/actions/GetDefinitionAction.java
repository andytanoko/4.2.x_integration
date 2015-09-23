/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDefinitionAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.events.gridform.GetDefinitionEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.gridform.GFDefinitionEntityFieldID;
import com.gridnode.gtas.server.gridform.helpers.Logger;
import com.gridnode.gtas.server.rdm.ejb.actions.GridTalkEJBAction;

import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerHome;
import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerObj;
import com.gridnode.pdip.app.gridform.model.GFDefinition;
import com.gridnode.pdip.app.gridform.exceptions.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.*;

/**
 * This Action class handles the retrieving of a GF Definition entity.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetDefinitionAction extends GridTalkEJBAction
{
  public IEventResponse perform(IEvent event) throws EventException
  {
    IEventResponse response = null;
    try
    {
      // Init.
      IGFManagerObj mgr = getManager();
      GetDefinitionEvent getDefinitionEvent = (GetDefinitionEvent)event;
      if (getDefinitionEvent == null)
      {
        throw new EventException("Event is null");
      }

      GFDefinition entity = null;
      if (getDefinitionEvent.getDefinitionUID() == null)
        entity = mgr.findGFDefinition(getDefinitionEvent.getDefinitionName());
      else
        entity = mgr.findGFDefinition(getDefinitionEvent.getDefinitionUID());

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

  private IEventResponse constructEventResponse(GFDefinition entity)
  {
    return new BasicEventResponse(
               IErrorCode.NO_ERROR,
               null,
               entity.convertToMap(entity, GFDefinitionEntityFieldID.getEntityFieldID(), null));
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

    Logger.err("[GetDefinitionAction.perform] Event Error", ex);
    return response;
  }

  private IGFManagerObj getManager() throws ServiceLookupException
  {
    return (IGFManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGFManagerHome.class.getName(),
                                                                                        IGFManagerHome.class,
                                                                                        new Object[0]);
  }
}