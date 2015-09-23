/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsRetryListenerMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 5, 2006    Tam Wei Xiang       Created
 * Mar 03 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.alert.listener.ejb;

import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.JmsDestinationEntityHandler;
import com.gridnode.pdip.app.alert.jms.AlertMessagingService;
import com.gridnode.pdip.app.alert.jms.Registry;
import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;

/**
 * Receive the jms msg from Timer related class in order to perform 
 * resending the failed jms msg.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsRetryListenerMDBean
	extends TimeInvokeMDBean
{
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5267330383215610187L;

	protected void invoke(AlarmInfo info)
	{
		super.invoke(info);
		try
		{
			//Populate the Registry with the most updated jms destination name list. 
			Registry registry = Registry.getInstance();
			registry.syncJmsDestination(getJmsDestname());
			
			AlertMessagingService msgService = new AlertMessagingService();
			//Task ID is the jmsDest name
			msgService.sendMessage(info.getTaskId());
		}
		catch(Throwable th)
		{
			AlertLogger.errorLog(ILogErrorCodes.JMS_RETRY_LISTENER,
                           "JmsRetryListenerMDBean", "invoke", "Error occured while sending the jms msg: "+th.getMessage(), th);
		}
	}
	
	private Collection<String> getJmsDestname()
		throws Throwable
	{
		ArrayList<String> jmsDestname = new ArrayList<String>();
		Collection<JmsDestination> jmsDestList = JmsDestinationEntityHandler.getInstance().findJmsDestinationByFilter(null);
		
		for(JmsDestination jmsDest: jmsDestList)
		{
			jmsDestname.add(jmsDest.getName());
		}
		return jmsDestname;
	}
}
