/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentResendMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Guo Jianyu          Created
 * Apr 17 2006    Neo Sok Lay         Refactor
 *                                    Change SenderKey to store Outbound gdoc uid.
 *                                    Use TaskId to store number of retries left
 *                                    instead of using repeating alarm.
 * Sep 05 2006    Neo Sok Lay         GNDB00027784: Check for null ReceiverKey before
 *                                    attempt to convert to Long for channel UID.
 * Oct 02 2007    Tam Wei Xiang       GNDB00028458: Add in document resend exhausted
 *                                    alert. 
 */
package com.gridnode.gtas.server.document.listener.ejb;

import java.util.Date;
import java.util.Hashtable;

import com.gridnode.gtas.server.document.helpers.AlertDelegate;
import com.gridnode.gtas.server.document.helpers.DocumentResendTimerHelper;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.helpers.SendDocumentHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;

/**
 * Listener for alarm for resend documents if the previous attempt to send failed.
 *
 * @author Guo Jianyu
 *
 * @version 4.0
 * @since 2.3
 */
public class DocumentResendMDBean extends TimeInvokeMDBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -840022483570573912L;
	private static Hashtable docSendTimeTable = new Hashtable();

	//NSL20060417 Refactor
	protected void invoke(AlarmInfo info)
	{
    Long alarmUID = new Long(info.getAlarmUid());
    String senderKey = info.getSenderKey();
    String channelUID = info.getReceiverKey();
    String retriesLeft = info.getTaskId();
    
    boolean oldFormat = false;
    GridDocument obGdoc = null;
    if (retriesLeft == null || retriesLeft.length()==0)
    {
    	oldFormat = true;
    	//old format (senderKey=importedgdocuid, use old method to retrieve OB gdoc
    	obGdoc = DocumentResendTimerHelper.findOutboundGdoc(Long.valueOf(senderKey));
    }
    else
    {
    	//new format (senderKey=outboundgdocuid)
    	obGdoc = DocumentResendTimerHelper.findGridDoc(Long.valueOf(senderKey));
    }
    if (obGdoc == null)
    {
    	Logger.log("[DocumentResendMDBean.invoke] No Outbound Gdoc found to resend for this alarm! Cancelling...");
    	DocumentResendTimerHelper.cancelTimer(alarmUID);
    	return;
    }
    
    //1. check if trans completed (i.e. MDN received)
    if (obGdoc.getDateTimeTransComplete() != null)
    {
    	//cancel alarm
    	DocumentResendTimerHelper.cancelTimer(alarmUID);
    	return;
    }
    
    //2. check if MDN required
    if (obGdoc.getDateTimeSendEnd() != null && (obGdoc.getDocTransStatus() == null || obGdoc.getDocTransStatus().length()==0))
    {
    	//message have been received at the recipient end and MDN not required 
    	//cancel alarm
    	DocumentResendTimerHelper.cancelTimer(alarmUID);
    	return;    	
    }
    
    iCalAlarm timer = DocumentResendTimerHelper.getTimer(alarmUID);
    if (timer == null)
    {
    	//something went wrong, do nothing and return for the time being
    	Logger.log("[DocumentResendMDBean.invoke] Could not retrieve alarm!");
    	return;
    }
    
    //3. check if exhausted all retries
    if (oldFormat) 
    {
    	retriesLeft = String.valueOf(timer.getRepeat());
    }
    
    int retriesBal = Integer.valueOf(retriesLeft);
    if (retriesBal <= 0)
    {
    	//no more try, cancel alarm
    	Logger.log("[DocumentResendMDBean.invoke] All retries exhausted! Cancelling the timer...");
    	DocumentResendTimerHelper.cancelTimer(alarmUID);
    	//need to update doctransstatus to failed?
    	if ("ongoing".equalsIgnoreCase(obGdoc.getDocTransStatus()))
    	{
    		obGdoc.setDocTransStatus("Failed - No MDN received.");
    		DocumentResendTimerHelper.updateGridDoc(obGdoc);
    	}
      
      //TWX 02102007 raise document resend exhausted alert
      AlertDelegate.raiseDocumentResendExhaustedAlert(obGdoc);
      return;
    }
    

    //4. try resend 
    boolean hasSent = false;
    Date dateTimeSendEnd = obGdoc.getDateTimeSendEnd();
    long timeElapsed = 0;
    
    if (dateTimeSendEnd == null)   // doc hasn't been sent once
    {
    	hasSent = false;
    }
    else if (dateTimeSendEnd.equals((Date)docSendTimeTable.get(obGdoc.getKey())))
    {
    	hasSent = false;
    }
    else
    {
    	hasSent = true;
    	docSendTimeTable.put(obGdoc.getKey(), dateTimeSendEnd);
    	timeElapsed = info.getDueDate().getTime() - dateTimeSendEnd.getTime();
    }

    long interval = timer.getDelayPeriod().longValue() * 1000;

    if ((hasSent == true) && (interval > timeElapsed))
    { //not time yet, update alarm to re-invoke later
      timer.setStartDt(new Date(dateTimeSendEnd.getTime() + interval));
      if (oldFormat)
      {
      	timer.setTaskId(retriesLeft);
      }
    }
    else
    { //do resend
      SendDocumentHelper helper = new SendDocumentHelper();
      try
      {
      	if (obGdoc.getAuditFileName() != null && obGdoc.getAuditFileName().length()>0)
      	{
      		helper.resend(obGdoc, channelUID);
      	}
      	else
      	{
      		//obGdoc.setRecipientChannelUid(Long.valueOf(channelUID));
          obGdoc.setRecipientChannelUid(getChannelUID(channelUID)); //NSL20060905
      		helper.prepareSendingInfo(obGdoc);
      		helper.doSend(obGdoc, true);
      	}
      }
      catch(Throwable t)
      {
        Logger.warn("Error resending document : " + obGdoc, t);
      }
      
      //and update alarm for next retry
      timer.setTaskId(String.valueOf(retriesBal - 1));
      timer.setStartDt(new Date(new Date().getTime() + interval));
    }
    
    if (oldFormat)
    {
    	timer.setSenderKey(String.valueOf(obGdoc.getUId()));
    	timer.setRepeat(new Integer(-1));
    }
    timer.setCount(new Integer(0));
    DocumentResendTimerHelper.updateTimer(timer);
    
	}
	
  private Long getChannelUID(String receiverKey)
  {
    if (receiverKey == null)
    {
      return null;
    }
    try
    {
      return Long.valueOf(receiverKey);
    }
    catch (NumberFormatException ex)
    {
      Logger.warn("[DocumentResendMDBean.getChannelUID] Invalid receiverKey format: "+receiverKey);
      return null;
    }
    
  }
	/*
  protected void invoke(AlarmInfo info)
  {
    Long alarmUID = new Long(info.getAlarmUid());
    String gDocUID = info.getSenderKey();
    String channelUID = info.getReceiverKey();

    IiCalTimeMgrObj timeMgr = null;

    try
    {
      timeMgr = (IiCalTimeMgrObj)ServiceLocator.instance(
        ServiceLocator.CLIENT_CONTEXT).getObj(
        IiCalTimeMgrHome.class.getName(),
        IiCalTimeMgrHome.class,
        new Object[0]);
    }
    catch(Exception e)
    {
      Logger.err("Error looking up iCalTimeMgrBean", e);
      return;
    }

    iCalAlarm alarm = null;
    try
    {
      alarm = timeMgr.getAlarm(alarmUID);
    }
    catch(Throwable t)
    {
      Logger.err("Error finding the alarm based on UID: " + alarmUID.toString(), t);
      return;
    }

    // check dateTimeTransEnd of the gridDoc and update the alarm accordingly
    IDocumentManagerObj documentMgr = null;
    try
    {
      documentMgr = (IDocumentManagerObj)ServiceLocator.instance(
        ServiceLocator.CLIENT_CONTEXT).getObj(
        IDocumentManagerHome.class.getName(),
        IDocumentManagerHome.class,
        new Object[0]);
    }
    catch(Exception e)
    {
      Logger.err("Error looking up IDocumentManagerBean", e);
      return;
    }

    GridDocument gDoc = null;
    try
    {
      GridDocument refGDoc = documentMgr.findGridDocument(new Long(gDocUID));
      if (refGDoc == null)
      {
        Logger.err("[DocumentResendMDBean.invoke] cannot find refGDoc with uid=" + gDocUID);
        return;
      }
      IDataFilter filter = new DataFilterImpl();
      Long refGDocID = refGDoc.getGdocId();
      filter.addSingleFilter(
        null,
        GridDocument.REF_G_DOC_ID,
        filter.getEqualOperator(),
        refGDocID,
        false
      );
      Collection results = documentMgr.findGridDocuments(filter);
      if ((results == null) || (results.isEmpty()))
      {
        Logger.err("[DocumentResendMDBean.invoke] cannot find outbound gDoc with refGDocID="
          + refGDocID.longValue());
        return;
      }
      Iterator itr = results.iterator();
      gDoc = (GridDocument)itr.next();
      Logger.debug("[DocumentResendMDBean.invoke] Found gDoc, uid = " + gDoc.getKey());
    }
    catch(Exception e)
    {
      Logger.err("Error finding the GridDocument on UID: " + gDocUID, e);
      return;
    }

    if (gDoc.getDateTimeTransComplete() != null)
    { // the document has been successfully sent (and acknowledged if an ack was requested)
      // The alarm can hence be removed
      try
      {
        timeMgr.cancelAlarm(alarmUID);
      }
      catch(Exception e)
      {
        Logger.err("Error canceling the alarm with UID:" + alarmUID.toString());
      }
      return;
    }

    int repeat = 0;
    if (alarm.getRepeat() != null)
      repeat = alarm.getRepeat().intValue();
    if (repeat == 0)
    //the last invocation of the alarm
    {
      try
      {
        //remove the alarm
        timeMgr.cancelAlarm(alarmUID);
      }
      catch(Exception e)
      {
        Logger.err("Error canceling alarm with uid: " + alarmUID, e);
      }
      //set gDoc transaction to "failed"
      gDoc.setDocTransStatus("Failed");
      return;
    }

    boolean hasSent = false;
    Date dateTimeSendEnd = gDoc.getDateTimeSendEnd();
    long timeElapsed = 0;

    if (dateTimeSendEnd == null)   // doc hasn't been sent once
      hasSent = false;
    else if (dateTimeSendEnd.equals((Date)docSendTimeTable.get(gDocUID)))
      //doc hasn't been sent since the previous attempt
      hasSent = false;
    else // doc has been sent for the first time or since the previous attempt
    {
      hasSent = true;
      docSendTimeTable.put(gDocUID, dateTimeSendEnd);
      timeElapsed = info.getDueDate().getTime() - dateTimeSendEnd.getTime();
    }

    iCalAlarm newAlarm = (iCalAlarm)alarm.clone();
    long interval = alarm.getDelayPeriod().longValue() * 1000;

    if ((hasSent == true) && (interval > timeElapsed))
    { //update the alarm
      newAlarm.setCount(new Integer(0));
      newAlarm.setStartDt(new Date(dateTimeSendEnd.getTime() + interval));
    }
    else
    { //do resend
      SendDocumentHelper helper = new SendDocumentHelper();
      try
      {
        helper.resend(gDoc, channelUID);
      }
      catch(Throwable t)
      {
        Logger.err("Error resending document : " + gDoc, t);
      }
      //and update alarm
      newAlarm.setCount(new Integer(0));
      newAlarm.setRepeat(new Integer(alarm.getRepeat().intValue() - 1));
      newAlarm.setStartDt(new Date(new Date().getTime() + interval));
    }

    try
    {
      timeMgr.cancelAlarm(alarmUID);
    }
    catch(Exception e)
    {
      Logger.err("Error canceling alarm with UID: " + alarmUID, e);
    }
    try
    {
      timeMgr.addAlarm(newAlarm);
    }
    catch(Exception e)
    {
      Logger.err("Error adding alarm:", e);
    }
  }
  */
}