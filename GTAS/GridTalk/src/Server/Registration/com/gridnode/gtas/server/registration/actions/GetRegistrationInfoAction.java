/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistrationInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.events.registration.GetRegistrationInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * Action class for getting the product RegistrationInfo. This action guarantees
 * a non-null return of RegistrationInfo map object even though product registration
 * has not been done before.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetRegistrationInfoAction extends AbstractRegistrationAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3714156741992934168L;
	private final String ACTION_NAME = "GetRegistrationInfoAction";

  public GetRegistrationInfoAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return GetRegistrationInfoEvent.class;
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
    GetRegistrationInfoEvent getEvent = (GetRegistrationInfoEvent)event;

    //initialize the registration info (with validating of product key)
    RegistrationInfo regInfo = getRegistrationBean().getRegistrationInfo();
//    if (regInfo.getCompanyProfile() == null)
//      regInfo.setCompanyProfile(new CompanyProfile());
//    else
//    {
//      if (getEnterpriseID() == null)
//        setEnterpriseID(regInfo.getGridnodeID().toString());
//    }
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