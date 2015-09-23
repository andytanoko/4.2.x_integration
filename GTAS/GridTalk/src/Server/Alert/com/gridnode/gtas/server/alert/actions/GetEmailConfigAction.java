/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEmailConfigAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.GetEmailConfigEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.alert.AlertEntityFieldID;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.alert.model.EmailConfig;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class GetEmailConfigAction extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7636605534927441927L;
	private final String ACTION_NAME = "GetEmailConfigAction";
  
  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.GENERAL_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {

    GetEmailConfigEvent getEvent = (GetEmailConfigEvent)event;
    EmailConfig emailConfig=ServiceLookupHelper.getAlertManager().getEmailConfig();
    
    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params,
             convertToMap(emailConfig));
  }

  private Map convertToMap(EmailConfig emailConfig)
  {
    return EmailConfig.convertToMap(emailConfig,AlertEntityFieldID.getEntityFieldID(),null);
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetEmailConfigEvent.class;
  }
}
