/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentResendTimerHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17, 2006   i00107             Created
 */

package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;
import java.util.Date;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author i00107
 * Helps with handling the timers for DocumentResend
 */
public class DocumentResendTimerHelper
{
	private static final String DOC_RESEND_CAT = "DocumentResend";
	
	public static GridDocument findOutboundGdoc(Long sourceDocUid)
	{
		IDocumentManagerObj docMgr = null;
		GridDocument obGdoc = null;
		try
		{
			docMgr = getDocMgr();
			GridDocument sourceGdoc = docMgr.findGridDocument(sourceDocUid);
			if (sourceGdoc != null)
			{
				DataFilterImpl filter = new DataFilterImpl();
				filter.addSingleFilter(null, GridDocument.REF_G_DOC_ID, filter.getEqualOperator(), sourceGdoc.getGdocId(), false);
				filter.addSingleFilter(filter.getAndConnector(), GridDocument.SOURCE_FOLDER, filter.getEqualOperator(), sourceGdoc.getFolder(), false);
				filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(), GridDocument.FOLDER_OUTBOUND, false);
				Collection potentialGdocs = docMgr.findGridDocuments(filter);
				if (potentialGdocs != null && !potentialGdocs.isEmpty())
				{
					obGdoc = (GridDocument)potentialGdocs.iterator().next();
					if (obGdoc != null)
					{
						obGdoc = docMgr.getCompleteGridDocument(obGdoc);
					}
				}
			}
		}
		catch (Throwable t)
		{
			Logger.warn("[DocumentResendTimerHelper.findOutboundGdoc] Error trying to retrieve outbound Gdoc for sourceGdoc UID="+sourceDocUid);
		}
		return obGdoc;
	}
	
	public static GridDocument findGridDoc(Long gdocUid)
	{
		IDocumentManagerObj docMgr = null;
		GridDocument obGdoc = null;
		try
		{
			docMgr = getDocMgr();
			obGdoc = docMgr.findGridDocument(gdocUid);
			if (obGdoc != null)
			{
				obGdoc = docMgr.getCompleteGridDocument(obGdoc);
			}
		}
		catch (Throwable t)
		{
			Logger.warn("[DocumentResendTimerHelper.findGridDoc] Error trying to retrieve Gdoc for UID="+gdocUid);
		}
		return obGdoc;
	}
	
	public static void updateGridDoc(GridDocument gdoc)
	{
		IDocumentManagerObj docMgr = null;
		try
		{
			docMgr = getDocMgr();
			docMgr.updateGridDocument(gdoc);
		}
		catch (Throwable t)
		{
			Logger.warn("[DocumentResendTimerHelper.updateGridDoc] Error trying to update Gdoc");
		}
	}
	
	private static String getSenderKey(String folder, Long gdocId)
	{
		return folder + gdocId;
	}
	
  public static void addTimer(Integer numOfRetries, Integer retryInterval, String sourceFolder, Long gdocId,
                               Long channelUID)
  {
  	try
  	{
  		iCalAlarm alarm = new iCalAlarm();
  		alarm.setCategory(DOC_RESEND_CAT);
  		alarm.setSenderKey(getSenderKey(sourceFolder, gdocId));
  		alarm.setReceiverKey(channelUID == null ? null : channelUID.toString());
  		//alarm.setRepeat(new Integer(numOfRetries.intValue() - 1));
  		alarm.setRepeat(new Integer(-1)); //set as onetime alarm
  		alarm.setDelayPeriod(new Long(retryInterval.longValue()));
  		alarm.setStartDt(new Date(new Date().getTime() + retryInterval.longValue() * 1000));
  		alarm.setDisabled(Boolean.TRUE);
  		alarm.setTaskId(String.valueOf(numOfRetries));
  		
  		getTimerMgr().addAlarm(alarm);
  	}
  	catch (Throwable t)
  	{
  		Logger.error(ILogErrorCodes.GT_DOCUMENT_RESEND_TIMER,
  		             "[DocumentResendTimerHelper.addTimer] Error setting up alarm with numOfRetries=" + numOfRetries.intValue() +
  		           ", retryInterval=" + retryInterval.intValue() + ", sourceFolder=" + sourceFolder + ", gdocId="+gdocId+" : "+t.getMessage());     
  	}
  }

  public static void updateTimer(GridDocument gdoc, ChannelInfo channel)
  {
  	//for now, only support AS2 retry timers
  	if (isAS2Channel(channel))
  	{
  		updateTimer(gdoc.getSrcFolder(), gdoc.getRefGdocId(), gdoc.getUId());
  	}
  	else
  	{
  		cancelTimer(gdoc.getSrcFolder(), gdoc.getRefGdocId());
  	}
  }
  
  private static boolean isAS2Channel(ChannelInfo channel)
  {
  	PackagingInfo pkg = channel.getPackagingProfile();
  	return pkg!=null && PackagingInfo.AS2_ENVELOPE_TYPE.equals(pkg.getEnvelope());
  }
  
  public static void updateTimer(String sourceFolder, Long srcGdocId, Long obGdocUid)
  {
  	iCalAlarm alarm = null;
  	IiCalTimeMgrObj timeMgr = null;
  	
  	try
  	{
  		timeMgr = getTimerMgr();
  		alarm = getTimer(sourceFolder, srcGdocId, timeMgr);
  		if (alarm != null)
  		{
  			//alarm.setTaskId(String.valueOf(obGdocUid));
  			alarm.setSenderKey(String.valueOf(obGdocUid));
  			alarm.setDisabled(Boolean.FALSE);
  			timeMgr.updateAlarm(alarm, true);
  		}
  	}
  	catch (Throwable t)
  	{
  		Logger.error(ILogErrorCodes.GT_DOCUMENT_RESEND_TIMER,
  		             "[DocumentResendTimerHelper.updateTimer] Error updating timer for senderKey="+getSenderKey(sourceFolder, srcGdocId)+" with taskId="+obGdocUid+" : "+t.getMessage());
  	}
  }
  
  public static void updateTimer(iCalAlarm timer)
  {
  	IiCalTimeMgrObj timeMgr = null;
  	
  	try
  	{
  		timeMgr = getTimerMgr();
			timeMgr.updateAlarm(timer, true);
  	}
  	catch (Throwable t)
  	{
  		Logger.error(ILogErrorCodes.GT_DOCUMENT_RESEND_TIMER,
  		             "[DocumentResendTimerHelper.updateTimer] Error updating timer for alarm, senderKey="+timer.getSenderKey()+" with taskId="+timer.getTaskId()+" : "+t.getMessage());
  	}
  	
  }
  
  public static void cancelTimer(String sourceFolder, Long srcGdocId)
  {
  	IiCalTimeMgrObj timeMgr = null;
  	
  	try
  	{
  		timeMgr = getTimerMgr();
  		String senderKey = getSenderKey(sourceFolder, srcGdocId);
  		DataFilterImpl filter = new DataFilterImpl();
  		filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), senderKey, false);
  		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), DOC_RESEND_CAT, false);
 			timeMgr.cancelAlarmByFilter(filter);
  	}
  	catch (Throwable t)
  	{
  		Logger.error(ILogErrorCodes.GT_DOCUMENT_RESEND_TIMER,
  		             "[DocumentResendTimerHelper.cancelTimer] Error cancelling timer for senderKey="+getSenderKey(sourceFolder, srcGdocId)+" : "+t.getMessage());
  	}
  }
  
  public static void cancelTimer(Long alarmUid)
  {
  	IiCalTimeMgrObj timeMgr = null;
  	
  	try
  	{
  		timeMgr = getTimerMgr();
 			timeMgr.cancelAlarm(alarmUid);
  	}
  	catch (Throwable t)
  	{
  		Logger.error(ILogErrorCodes.GT_DOCUMENT_RESEND_TIMER,
  		             "[DocumentResendTimerHelper.cancelTimer] Error cancelling timer for alarmUID="+alarmUid+" : "+t.getMessage());
  	}
  }
  
  public static iCalAlarm getTimer(Long alarmUid)
  {
  	IiCalTimeMgrObj timeMgr = null;
  	iCalAlarm alarm = null;
  	try
  	{
  		timeMgr = getTimerMgr();
 			alarm = timeMgr.getAlarm(alarmUid);
  	}
  	catch (Throwable t)
  	{
  		Logger.warn("[DocumentResendTimerHelper.cancelTimer] Error getting timer for alarmUID="+alarmUid);
  	}
  	return alarm;
  }
  
  private static iCalAlarm getTimer(String sourceFolder, Long srcGdocId, IiCalTimeMgrObj timeMgr)
  {
  	iCalAlarm alarm = null;
  	
  	try
  	{
  		String senderKey = getSenderKey(sourceFolder, srcGdocId);
  		DataFilterImpl filter = new DataFilterImpl();
  		filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), senderKey, false);
  		filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), DOC_RESEND_CAT, false);
  		Collection alarms = timeMgr.findAlarms(filter);
  		if (alarms != null && !alarms.isEmpty())
  		{
  			alarm = (iCalAlarm)alarms.iterator().next();
  		}
  	}
  	catch (Throwable t)
  	{
  		Logger.warn("[DocumentResendTimerHelper.getTimer] Error getting timer for senderKey="+getSenderKey(sourceFolder, srcGdocId));
  	}
  	return alarm;
  }
  
  private static IiCalTimeMgrObj getTimerMgr() throws ServiceLookupException
  {
  	return (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IiCalTimeMgrHome.class.getName(),
                                                                                          IiCalTimeMgrHome.class,
                                                                                          new Object[0]);

  }
  
  private static IDocumentManagerObj getDocMgr() throws ServiceLookupException
  {
  	return (IDocumentManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IDocumentManagerHome.class.getName(),
  	                                                                                          IDocumentManagerHome.class,
  	                                                                                          new Object[0]);

  }
}
