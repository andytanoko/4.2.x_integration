/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveEmailConfigAction.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Map;

import com.gridnode.gtas.events.alert.SaveEmailConfigEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.alert.model.EmailConfig;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;


public class SaveEmailConfigAction  extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7480595794834399935L;
	private final String ACTION_NAME = "SaveEmailConfigAction";
  
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

    SaveEmailConfigEvent saveEvent = (SaveEmailConfigEvent)event;
    EmailConfig emailConfig=convertToEntity(saveEvent.getEmailConfig());
    ServiceLookupHelper.getAlertManager().updateEmailConfig(emailConfig);
    
    Object[] params = {
                      };
    return constructEventResponse(
             IErrorCode.NO_ERROR,
             params);
  }

  protected EmailConfig convertToEntity(Map emailConfigMap)
  {
    EmailConfig emailConfig = new EmailConfig();
    Object[] keys = emailConfigMap.keySet().toArray();
    for (int i=0; i<keys.length; i++)
    {
      emailConfig.setFieldValue((Number)keys[i], emailConfigMap.get(keys[i]));
    }
    return emailConfig;
  }
  
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return SaveEmailConfigEvent.class;
  }

}
