/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PopulateFormAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created
 */
package com.gridnode.gtas.server.gridform.actions;

import com.gridnode.gtas.events.gridform.PopulateFormEvent;
import com.gridnode.gtas.exceptions.*;
import com.gridnode.gtas.server.gridform.helpers.Logger;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.ejb.actions.GridTalkEJBAction;

import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerHome;
import com.gridnode.pdip.app.gridform.facade.ejb.IGFManagerObj;
import com.gridnode.pdip.app.gridform.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * Action that handles the populating of a form object.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class PopulateFormAction extends GridTalkEJBAction
{
  public IEventResponse perform(IEvent event) throws EventException
  {
    IEventResponse response = null;
    try
    {
      // Init.
      IGFManagerObj mgr = getManager();
      PopulateFormEvent populateFormEvent = (PopulateFormEvent)event;
      GFForm form = null;
      if (populateFormEvent == null)
      {
        throw new EventException("Event is null");
      }

      // Get the form from the state machine.
      form = (GFForm)sm.getAttribute(IAttributeKeys.FORM_OBJECT);

      // Populate the form with data.
      form = mgr.populateFormData(form);

      // Keep the form in state machine.
      sm.setAttribute(IAttributeKeys.FORM_OBJECT, form);

      // No error.
      response = constructEventResponse();
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

  private IEventResponse constructEventResponse(NestingException ex)
  {
    BasicEventResponse response = null;

    response = new BasicEventResponse(
                   IErrorCode.POPULATE_FORM_ERROR,
                   new Object[]{},
                   (short)-1,
                   ex.getLocalizedMessage(),
                   ex.getStackTraceString());

    Logger.err("[PopulateFormAction.perform] Event Error", ex);
    return response;
  }

  private IGFManagerObj getManager() throws ServiceLookupException
  {
    return (IGFManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGFManagerHome.class.getName(),
                                                                                        IGFManagerHome.class,
                                                                                        new Object[0]);
  }
}