/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfirmRegistrationInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.registration.ConfirmRegistrationInfoEvent;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * This action handles the ConfirmRegistrationInfoEvent.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ConfirmRegistrationInfoAction extends AbstractRegistrationAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3918811674942265433L;
	private final String ACTION_NAME = "ConfirmRegistrationInfoAction";

  public ConfirmRegistrationInfoAction()
  {
  }

  protected Class getExpectedEventClass()
  {
    return ConfirmRegistrationInfoEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.INVALID_REGISTRATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    ConfirmRegistrationInfoEvent confirmEvent = (ConfirmRegistrationInfoEvent)event;

    getRegistrationBean().confirmRegistration(confirmEvent.getSecurityPassword());

    RegistrationInfo regInfo = getRegistrationInfo();
    setEnterpriseID(regInfo.getGridnodeID().toString());

    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(regInfo));

  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}