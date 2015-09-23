/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: AbstractPipMessageHelper.java
 * 
 * ***************************************************************************
 * Date Author Changes
 * ***************************************************************************
 * Feb 21, 2007 Alain Ah Ming Created
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.dao.IRnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Abstract super class for PIP message handlers
 * 
 * @author Alain Ah Ming
 * @since
 * @version
 */
public abstract class AbstractPipMessageHelper implements IPipMessageHelper
{
	private static Random _random = new Random();

	protected abstract IRnifMessageDao getRnifDao();

	public String findValue(String content, String commaDelimitedTokenList)
	{
		String[] tokenList = constructTokenList(commaDelimitedTokenList);
		String value = search(content, tokenList);
		return value;
	}

	protected String[] constructTokenList(String commaDelimitedTokenList)
	{
		StringTokenizer st = new StringTokenizer(commaDelimitedTokenList, ", ");
		String[] tokenList = new String[st.countTokens()];
		int count = 0;
		while (st.hasMoreTokens())
		{
			tokenList[count++] = st.nextToken();
		}
		return tokenList;
	}

	protected String search(String content, String[] searchTokens)
	{
		int start = 0;
		int end = -1;
		for (int i = 0; i < searchTokens.length; i++)
		{
			int idx = content.indexOf(searchTokens[i], start);
			if (idx < 0) { return null; // not found
			}
			if (i % 2 == 0) // start token
			{
				start = idx + searchTokens[i].length();
			}
			else
			// end token
			{
				end = idx;
			}
		}
		return content.substring(start, end);
	}

	public String findPipInstId(String content) throws MessageHelperException
	{
		String pipInstId = null;
		try
		{
			pipInstId = findValue(content, getRnifDao().getRnifMessageTemplate(getPipCode())
					.getPath(RnifMessageEntity.PATH_PIP_INSTANCE_ID));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
		
		return pipInstId;
	}

	public String findReceiverDuns(String content) throws MessageHelperException
	{
		try
		{
			return findValue(content, getRnifDao().getRnifMessageTemplate(getPipCode())
					.getPath(RnifMessageEntity.PATH_RECEIVER_DUNS));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
	}

	public String findSenderDuns(String content) throws MessageHelperException
	{
		try
		{
			return findValue(content, getRnifDao().getRnifMessageTemplate(getPipCode())
					.getPath(RnifMessageEntity.PATH_SENDER_DUNS));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
	}

	public String findMsgTrackingId(String content) throws MessageHelperException
	{
		try
		{
			return findValue(content, getRnifDao().getRnifMessageTemplate(getPipCode())
					.getPath(RnifMessageEntity.PATH_MESSAGE_TRACKING_ID));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
	}

	public String findInitiatorDuns(String content) throws MessageHelperException
	{
		try
		{
			return findValue(content, getRnifDao().getRnifMessageTemplate(getPipCode())
					.getPath(RnifMessageEntity.PATH_INITIATOR_DUNS));
		}
		catch (MessageDaoException e)
		{
			throw new MessageHelperException("Error in DAO", e);
		}
	}

	protected String genContentID() {
		String param0 = String.valueOf(_random.nextInt(Integer.MAX_VALUE));
		String param1 = String.valueOf(System.currentTimeMillis());
		String param2 = System.getProperty("user.name");
		String param3;
		try {
			param3 = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			param3 = "unknown.host";
		}
		return MessageFormat.format(CONTENT_ID_PATTERN, param0, param1, param2,
				param3);
	}

	protected String genTs() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyyMMdd'T'hhmmss'.'SSS'Z'");
		Date currentTime_1 = new Date();
		return formatter.format(currentTime_1);
	}


  protected String getContent(File f) throws IOException
  {
    FileInputStream is = new FileInputStream(f);
    byte[] buff = new byte[1024];
    int readLen = -1;
    StringBuffer content = new StringBuffer();
    while ((readLen=is.read(buff))>0)
    {
      content.append(new String(buff, 0, readLen));
    }    
    return content.toString();
  }

  protected void logDebug(String methodName, String message)
  {
  	Logger.debug(this.getClass().getSimpleName(), methodName, message);
  }
  
//  protected void logInfo(String methodName, String message)
//  {
//  	Logger.info(this.getClass().getSimpleName(), methodName, message);
//  }
  
//  protected void logWarn(String methodName, String message, Throwable t)
//  {
//  	Logger.warn(this.getClass().getSimpleName(), methodName, message, t);
//  }
//  protected void logError(String methodName, String message, Throwable t)
//  {
//  	Logger.error(this.getClass().getSimpleName(), methodName, message, t);
//  }
}
