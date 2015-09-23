/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertMessagingService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 30, 2005    Tam Wei Xiang       Created
 * Feb 17 2006    Neo Sok Lay         Link JmsMessageRecord to JmsDestination Uid
 *                                    instead of Name.
 */
package com.gridnode.pdip.app.alert.jms;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.JmsDestinationEntityHandler;
import com.gridnode.pdip.app.alert.helpers.JmsMessageRecordEntityHandler;
import com.gridnode.pdip.app.alert.model.IMessageProperty;
import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.app.alert.model.JmsMessageRecord;
import com.gridnode.pdip.app.alert.model.MessageProperty;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.JNDIFinder;

/**
 * This class handle the sending of the JMS msg to the JMS server.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class AlertMessagingService
{
  private static final String CLASS_NAME = "AlertMessagingService";
  
  private static final Integer DEFAULT_PRIORITY = -1;
  private static final Integer DEFAULT_DELIVERY_MODE = 0;
  
	public AlertMessagingService()
	{
		
	}
	
	private void sendMessage(MessageProducer producer, TextMessage msg)
		throws Exception
	{
		producer.send(msg);
	}
	
	private MessageProducer createProducer(Session session, Destination dest)
		throws Exception
	{
		return session.createProducer(dest);
	}
	
	/**
	 * An optional msg properties can be set into the jms msg.
	 * @param textMsg The jms msg
	 * @param messageProperties a collection of messageProperty that user want to add into the jms msg.
	 * @throws Exception
	 */
	private void setProperties(TextMessage textMsg, Collection<MessageProperty> messageProperties)
		throws Exception
	{
		Iterator<MessageProperty> i = messageProperties.iterator();
		while(i.hasNext())
		{
			handleProperties(textMsg, i.next());
		}
	}
	
	/**
	 * Depending the type of the msg properties, we will invoke the appropriate
	 * setProperty method in TextMessage class.
	 * @param textMsg
	 * @param msgProperty
	 * @throws Exception
	 */
	private void handleProperties(TextMessage textMsg, MessageProperty msgProperty)
		throws Exception
	{
		int type = msgProperty.getType().intValue();
		
		switch(type)
		{
			case IMessageProperty.TYPE_BOOLEAN:
				textMsg.setBooleanProperty(msgProperty.getPropertyKey(), new Boolean(msgProperty.getValue()).booleanValue());
				break;
			case IMessageProperty.TYPE_BYTE:
				textMsg.setByteProperty(msgProperty.getPropertyKey(), new Byte(msgProperty.getValue()).byteValue());
				break;
			case IMessageProperty.TYPE_DOUBLE:
				textMsg.setDoubleProperty(msgProperty.getPropertyKey(), new Double(msgProperty.getValue()).doubleValue());
				break;
			case IMessageProperty.TYPE_FLOAT:
				textMsg.setFloatProperty(msgProperty.getPropertyKey(), new Float(msgProperty.getValue()).floatValue());
				break;
			case IMessageProperty.TYPE_INT:
				textMsg.setIntProperty(msgProperty.getPropertyKey(), new Integer(msgProperty.getValue()).intValue());
				break;
			case IMessageProperty.TYPE_LONG:
				textMsg.setLongProperty(msgProperty.getPropertyKey(), new Long(msgProperty.getValue()).longValue());
				break;
			case IMessageProperty.TYPE_SHORT:
				textMsg.setShortProperty(msgProperty.getPropertyKey(), new Short(msgProperty.getValue()).shortValue());
				break;
			case IMessageProperty.TYPE_STRING:
				textMsg.setStringProperty(msgProperty.getPropertyKey(), msgProperty.getValue());
				break;
			default:
				throw new IllegalArgumentException("MessageProperty type "+ type +" is not supported.");
		}
	}

	private Session createSession(Connection conn, boolean isTransacted, 
	                           int acknowledgeMode)
		throws Exception
	{
		return conn.createSession(isTransacted, acknowledgeMode);
	}
	
	private Connection createConnection(ConnectionFactory connFactory, JmsDestination jmsDest)
		throws Exception
	{
    if (jmsDest.getConnectionUser() == null)
    {
      return connFactory.createConnection();
    }
		return connFactory.createConnection(jmsDest.getConnectionUser(), jmsDest.getConnectionPassword());
	}
	
	private ConnectionFactory getConnectionFactory(String connectionFactoryJndi, JNDIFinder finder)
		throws Exception
	{
		return (ConnectionFactory)finder.lookup(connectionFactoryJndi);
	}
	
	private Destination lookupDestination(String destName, JNDIFinder finder)
		throws Exception
	{
		return (Destination)finder.lookup(destName, Destination.class);
	}
  
  /**
   * Send the jms message/failed jms msg which are categoried by the jmsDestname;
   * For those failed msgs which retryCount exceeds its correpond JmsDestination's max retries,
   * their permanent failed will be set to true.
   * @param jmsDestName
   * @throws Exception
   */
  public void sendMessage(String jmsDestName)
  	throws Exception
  {
  	Connection conn = null;
  	JmsDestination jmsDest = null;
  	try
  	{
  		//Acquire lock from the Registry on a particular jms destination.
  		//Since the trigger of the sending of jms msg is driven by Timer module and JmsRetryMDBean,
  		//this is possible that another sending jms msg event will be kick off and
  		//try to perform sending the same jms msg records that link to the jmsdestination
  		//if the time interval for retry is too short.
  		if(!Registry.getInstance().acquireLock(jmsDestName))
  		{
  			AlertLogger.infoLog(CLASS_NAME, "sendMessage","Jms destination name "+ jmsDestName+" is currently being locked.");
  			return;
  		}

      jmsDest = retrieveJmsDestination(jmsDestName);
      //check if JmsDest is still exist
      if (jmsDest == null)
      {
        return;
      }
  		//retrieve the message that are required to send.
  		//Collection<JmsMessageRecord> jmsRecords = getAssociatedJmsMessageRecord(jmsDestName);
      Collection<JmsMessageRecord> jmsRecords = getAssociatedJmsMessageRecord(jmsDest.getUId());  		
  		if(jmsRecords == null || jmsRecords.size() == 0)
  		{
  			return;
  		}
  		
  		//jmsDest = retrieveJmsDestination(jmsDestName);
  		JNDIFinder jndiFinder = new JNDIFinder(jmsDest.getLookupProperties());
  		
  		
  		Session session = null;
  		MessageProducer producer = null;
  		try
  		{
  			ConnectionFactory connFactory = getConnectionFactory(jmsDest.getConnectionFactoryJndi(), jndiFinder);
  			conn = createConnection(connFactory, jmsDest);
  			session = createSession(conn, false, Session.AUTO_ACKNOWLEDGE);
  			Destination dest = lookupDestination(jmsDest.getJndiName(), jndiFinder);
  		
  			producer = createProducer(session, dest);
  		}
  		catch(Exception ex)
  		{
  			handleFailedMessages(jmsRecords, jmsDest);
  			throw ex;
  		}
			for(JmsMessageRecord msgRecord: jmsRecords)
			{
				try
				{
					TextMessage textMsg = session.createTextMessage();
					if(!jmsDest.getDeliveryMode().equals(DEFAULT_DELIVERY_MODE))
					{
						textMsg.setJMSDeliveryMode(deliveryModeInterpreter(jmsDest.getDeliveryMode()));
					}
					
					if(!jmsDest.getPriority().equals(DEFAULT_PRIORITY))
					{
						textMsg.setJMSPriority(jmsDest.getPriority());
					}
				
					textMsg.setText(msgRecord.getMsgData().getMessage());
          AlertLogger.debugLog(CLASS_NAME, "sendMessage","TextMessage is "+ textMsg.getText());
					setProperties(textMsg, msgRecord.getMsgData().getMsgProperties());
				
					sendMessage(producer, textMsg);
					
					//delete successfully sent jms msg record
					JmsMessageRecordEntityHandler.getInstance().deleteJmsMessageRecord((Long)msgRecord.getKey());
				}
				catch(Exception ex)
				{
					updateFailedMessage(msgRecord, jmsDest);
					throw ex;
				}
			}
			
  	}
  	catch(Exception ex)
  	{
  		throw ex;
  	}
  	finally
  	{
  		if(conn != null)
  		{
  			conn.close();
  		}
  		
  		//Release the lock that we hold on the jms dest
			Registry.getInstance().releaseLock(jmsDestName);
  	}
  }
  
  /**
   * Handle jms messsages that have failed for certain reason.
   * @param failedMessages
   * @param jmsDest
   * @throws Exception
   */
  private void handleFailedMessages(Collection<JmsMessageRecord> failedMessages, JmsDestination jmsDest)
  	throws Exception
  {
  	for(JmsMessageRecord msgRecord : failedMessages)
  	{
  		updateFailedMessage(msgRecord, jmsDest);
  	}
  }
  
  /**
   * Increment the retry count of jms msg record or set the permanent failed of the jms msg record to true depend on whether
   * the number of retries exceed maximum retries.
   * @param jmsMsgRecord the jms msg that failed to send.
   * @param jmsDest 
   */
  private void updateFailedMessage(JmsMessageRecord jmsMsgRecord , JmsDestination jmsDest)
  	throws Exception
  {
  	int numRetries = jmsMsgRecord.getMsgData().getRetryCount()+1;
  	int maxRetries = jmsDest.getMaximumRetries();
  	
  	//indicate that the jms msg has reached maximum retried.
  	if(numRetries > maxRetries && maxRetries != -1)
  	{
  		jmsMsgRecord.setPermanentFailed(Boolean.TRUE);
  	}
  	else
  	{
  		jmsMsgRecord.getMsgData().setRetryCount(numRetries);
  	}
  	JmsMessageRecordEntityHandler.getInstance().updateJmsMessageRecord(jmsMsgRecord);
  }
  
  /**
   * Retrieve the JmsDestination given its name which is the unique key for
   * table jms_destination
   * @param jmsDestName
   * @return
   */
  private JmsDestination retrieveJmsDestination(String jmsDestName)
  	throws Exception
  { 
  	return JmsDestinationEntityHandler.getInstance().findByJmsDestName(jmsDestName);
  }
  
  /**
   * Add the jms msg into DB. iCalAlarm will automatically send the jms msg given the time
   * interval user set on the jms destination.
   * @param jmsDestUid
   * @param message
   * @param msgProperties
   * @return
   * @throws Exception
   */
  //public boolean addJmsMsgToSendingList(String jmsDestname, String message, Collection<MessageProperty> msgProperties)
  public boolean addJmsMsgToSendingList(long jmsDestUid, String message, Collection<MessageProperty> msgProperties)
  	throws Exception
  {
  	try
  	{
  		//JmsMessageRecord msgRecord = createJmsMessageRecord(jmsDestname, message, msgProperties);
      JmsMessageRecord msgRecord = createJmsMessageRecord(jmsDestUid, message, msgProperties);
  		JmsMessageRecordEntityHandler.getInstance().createJmsMessageRecord(msgRecord);
  		return true;
  	}
  	catch(Exception ex)
  	{
  		AlertLogger.warnLog("AlertMessagingService", "addJmsMsgToSendingList", "Failed to add jms msg record to DB.", ex);
  		return false;
  	}
  }
  
  //private JmsMessageRecord createJmsMessageRecord(String jmsDestname, String message, Collection<MessageProperty> msgProperties)
  private JmsMessageRecord createJmsMessageRecord(long jmsDestUid, String message, Collection<MessageProperty> msgProperties)
  {
  	MessageData msgData = new MessageData();
  	msgData.setMsgProperties(msgProperties);
  	msgData.setMessage(message);
  	msgData.setRetryCount(0);
  	
  	Calendar c = Calendar.getInstance();
  	Long alertTime = c.getTimeInMillis();
  	
  	//return new JmsMessageRecord(jmsDestname, new Date(alertTime), msgData, new Boolean(false), alertTime);
    return new JmsMessageRecord(jmsDestUid, new Date(alertTime), msgData, new Boolean(false), alertTime);
  }
  
  /**
   * Retrieve a list of jms message records which tie to the jmsDestname.
   * To preserve the sequence of record creation, we will sort the record based
   * on AlertTimeInLong
   * @param jmsDestUid
   * @return
   * @throws Exception
   */
  //private Collection<JmsMessageRecord> getAssociatedJmsMessageRecord(String jmsDestname)
  private Collection<JmsMessageRecord> getAssociatedJmsMessageRecord(long jmsDestUid)
  	throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	//filter.addSingleFilter(null, JmsMessageRecord.JMS_DESTINATION_NAME, filter.getEqualOperator(),
  	//                       jmsDestname, false);
    filter.addSingleFilter(null, JmsMessageRecord.JMS_DESTINATION_UID, filter.getEqualOperator(),
                           new Long(jmsDestUid), false);
  	filter.addSingleFilter(filter.getAndConnector(), JmsMessageRecord.PERMANENT_FAILED, filter.getNotEqualOperator(),
  	                       true, false);
  	filter.addOrderField(JmsMessageRecord.ALERT_TIME_IN_LONG,true);
  	return JmsMessageRecordEntityHandler.getInstance().findJmsMessageRecordByFilter(filter);
  }
  
  private int deliveryModeInterpreter(Integer mode)
  {
  	switch(mode.intValue())
  	{
  		case 1:
  			return DeliveryMode.PERSISTENT;
  		case 2:
  			return DeliveryMode.NON_PERSISTENT;
  		default:
  			throw new IllegalArgumentException("[AlertMessagingService.deliveryModeInterpreter]DeliveryMode  "+ mode +" is not supported.");
  	}
  }
  
}
