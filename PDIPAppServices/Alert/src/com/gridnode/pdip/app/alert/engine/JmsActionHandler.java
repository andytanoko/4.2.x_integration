/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 3, 2006    Tam Wei Xiang       Created
 * Feb 17 2006    Neo Sok Lay         Link Jms msg to Jms destination Uid
 */
package com.gridnode.pdip.app.alert.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.JmsDestinationEntityHandler;
import com.gridnode.pdip.app.alert.jms.AlertMessagingService;
import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.app.alert.model.MessageProperty;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;

/**
 * This class delegates the sending of JMS msg to AlertMessagingServices.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsActionHandler
	extends AbstractAlertActionHandler
{
	//private MessageTemplate _msgTemplate;
	private final String ADD_SUCCESS = "JMS msg is successfully added to sending list.";
	private final String ADD_FAIL = "JMS msg is unable to be added into sending list.";
	
	public JmsActionHandler()
	{
		
	}

	
	/**
	 * Execute the JmsAction.
	 * Handle the sending of JMS Message and delegate to appropriate class.
	 * @param providerList 
	 * @param fileAttachment Attachment will not be supported.
	 */
	public String execute(IProviderList providerList, String fileAttachment)
	
	{
		try
		{
			JmsDestination jmsDest = null;
			MessageTemplate msgTemplate = super.getMessageTemplate();
			
			if(msgTemplate != null)
			{
				jmsDest = getJmsDestination(msgTemplate);
			}
		
			String msgAfterFormat = this.formatMessage(msgTemplate.getMessage(), providerList);
			Collection<MessageProperty> msgPropertiesAfterFormat = this.formatMessageProperty(msgTemplate.getMessageProperties(), providerList);
		
			AlertMessagingService alertMsgSvc = new AlertMessagingService();
			
			return alertMsgSvc.addJmsMsgToSendingList(jmsDest.getUId(), msgAfterFormat, msgPropertiesAfterFormat)?ADD_SUCCESS:ADD_FAIL;
      //return alertMsgSvc.addJmsMsgToSendingList(jmsDest.getName(), msgAfterFormat, msgPropertiesAfterFormat)?ADD_SUCCESS:ADD_FAIL;
		}
		catch(Throwable th)
		{
			AlertLogger.warnLog("JmsActionHandler", "execute","Error", th);
			return ADD_FAIL;
		}
	}
	
	/**
	 * Format the message by substitute the properties with value that derived
	 * from providerList.
	 * @param msg The jms message
	 * @param providerList
	 * @return Formatted message
	 * @throws Throwable
	 */
	private String formatMessage(String msg, IProviderList providerList)
		throws Throwable
	{
		Repository repository = new Repository();
		return repository.format(msg,providerList);
	}
	
	/**
	 * Format the messageProperty by substitute the properties with value that derived
	 * from providerList.
	 * @param msgProperties The messageProperties in MessageTemplate
	 * @param providerList
	 * @return Collection of MessageProperty(s) with formatted values
	 * @throws Throwable
	 */
	private Collection<MessageProperty> formatMessageProperty(Collection<MessageProperty> msgProperties, IProviderList providerList)
		throws Throwable
	{
		Repository repository = new Repository();
		for (MessageProperty msgProperty : msgProperties)
		{
			msgProperty.setValue(repository.format(msgProperty.getValue(), providerList));
		}
		return msgProperties;
	}
	
	/**
	 * Retrieve a particular jms destination based on the msgTemp's jmsDEstination UID
	 * @param msgTemp
	 * @return JmsDestination for the message template
	 * @throws Exception
	 */
	private JmsDestination getJmsDestination(MessageTemplate msgTemp)
		throws Exception
	{
		
		return JmsDestinationEntityHandler.getInstance().findByUID((Long)
		                                      msgTemp.getJmsDestination().getKey());
	}
	
	
}
