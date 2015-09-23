/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsMessageRecord.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 * Feb 17 2006    Neo Sok Lay         Change jmsDestinationName to jmsDestinationUid 
 *                                    - link by UID instead of Name in case Name can be changed.
 */
package com.gridnode.pdip.app.alert.model;

import java.util.Date;

import com.gridnode.pdip.app.alert.jms.MessageData;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 *
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsMessageRecord
	extends AbstractEntity
	implements IJmsMessageRecord
{
	/**
   * Serial Version UID
   */
  private static final long serialVersionUID = -5740993703148624571L;
  //private String _jmsDestinationName;
  private long _jmsDestinationUid;
	private Date _alertTimestamp;
	private MessageData _msgData;
	private Boolean _permanentFailed; //if true indicate this jmsMessageRecord has reached maximum retry
	private Long _alertTimeInLong;    //same as alertTimestamp(DB's datetime may not support till milli second).
	                                  //we sort the jms msg alert based on this which is more accurate.
	
	public JmsMessageRecord() {}
	
	//public JmsMessageRecord(String jmsDestination, Date alertTimestamp, MessageData msgData,
  public JmsMessageRecord(long jmsDestinationUid, Date alertTimestamp, MessageData msgData,
	                        Boolean permanentFailed, Long alertTimeInLong)
	{
		//_jmsDestinationName = jmsDestination;
    _jmsDestinationUid = jmsDestinationUid;
		_alertTimestamp = alertTimestamp;
		_msgData = msgData;
		_permanentFailed = permanentFailed;
		_alertTimeInLong = alertTimeInLong;
	}
	
	public Number getKeyId()
	{
		return UID;
	}
	
	public String getEntityDescr()
	{
		//return new StringBuffer(_jmsDestinationName).append(" ").append(_uId).toString();
    return new StringBuffer(getEntityName()).append("[uid=").append(_uId).append(", jmsDestUid=").append(_jmsDestinationUid).append("]").toString();
	}
	
	public String getEntityName()
	{
		return ENTITY_NAME;
	}
	
	/**               Getter and Setter for attributes        **/
	public Date getAlertTimestamp()
	{
		return _alertTimestamp;
	}

  /*
	public String getJmsDestinationName()
	{
		return _jmsDestinationName;
	}*/
  public long getJmsDestinationUid()
  {
    return _jmsDestinationUid;
  }

	public MessageData getMsgData()
	{
		return _msgData;
	}

	public Boolean getPermanentFailed()
	{
		return _permanentFailed;
	}
	
	public Long getAlertTimeInLong()
	{
		return _alertTimeInLong;
	}
	
	public void setAlertTimestamp(Date timestamp)
	{
		_alertTimestamp = timestamp;
	}

  /*
	public void setJmsDestinationName(String destination)
	{
		_jmsDestinationName = destination;
	}*/
  public void setJmsDestinationUid(long destinationUid)
  {
    _jmsDestinationUid = destinationUid;
  }

	public void setMsgData(MessageData data)
	{
		_msgData = data;
	}
	
	public void setPermanentFailed(Boolean failed)
	{
		_permanentFailed = failed;
	}
	
	public void setAlertTimeInLong(Long alertTimeInLong)
	{
		_alertTimeInLong = alertTimeInLong;
	}

}
