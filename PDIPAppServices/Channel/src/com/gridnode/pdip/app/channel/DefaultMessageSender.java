/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultMessageSender.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 * Apr 12 2006    Neo Sok Lay             send(): Need to set the MIC and AUDIT_FILENAME
 *                                        into returnMsg or else the values will
 *                                        not be updated to GridDocument
 * Aug 30 2006    Neo Sok Lay             Refactor send().                                        
 */
package com.gridnode.pdip.app.channel;

import com.gridnode.pdip.app.channel.exceptions.FlowControlException;
import com.gridnode.pdip.app.channel.exceptions.PackagingException;
import com.gridnode.pdip.app.channel.exceptions.SecurityException;
import com.gridnode.pdip.app.channel.exceptions.TransportException;
import com.gridnode.pdip.app.channel.helpers.*;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.base.transport.helpers.Feedback;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.*;

import java.util.*;
/**
 * This class is essentialy responsible to pre-process all outgoing messages
 *
 */
public class DefaultMessageSender extends AbstractMessageSender
{
  private static final String CLASS_NAME = "DefaultMessageSender";

  public DefaultMessageSender()
  {
  }

  public Message pack(PackagingInfo packagingInfo, Message message)
    throws PackagingException
  {
    return PackagingServiceDelegate.pack(packagingInfo, message);
  }

  public Message[] split(FlowControlInfo flowControlInfo, Message message)
    throws FlowControlException
  {
    if (IFlowControlInfo.STATUS_FLOWCONTROL_UNSET
      == flowControlInfo.getFlowControlStatus())
    {
      flowControlInfo = getFlowControlInfo(flowControlInfo, message);
    }
    return FlowControlServiceHandler.split(flowControlInfo, message);
  }

  public Message encrypt(SecurityInfo securityInfo, Message message)
    throws SecurityException
  {

    if (ISecurityInfo
      .ENCRYPTION_TYPE_NONE
      .equals(securityInfo.getEncryptionType())
      && ISecurityInfo.SIGNATURE_TYPE_NONE.equals(
        securityInfo.getSignatureType()))
    {
      return message; //Return Original Message if no decrypt and Verify
    }
    /* @todo to set security level 1 in security info */
    securityInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_1);
    return SecurityServiceDelegate.encrypt(securityInfo, message);
  }

  public Message send(CommInfo commInfo, Message message)
    throws TransportException
  {
    Map headers = message.getMessageHeaders();
    Map commonHeaders = message.getCommonHeaders();
    Message returnMsg = null;

    try
    {
      if (headers != null && headers.containsKey(IAS2Headers.AS2_FROM))
      {
        message.setCommonHeaders(new Hashtable());
        
        //NSL20060830 Refactor logic, any new headers to include just add in here.
        String[][] temp = {
            {IAS2Headers.MIC, (String)headers.get(IAS2Headers.MIC)},
            {IAS2Headers.AUDIT_FILE_NAME, (String)headers.get(IAS2Headers.AUDIT_FILE_NAME)},
        };
        
        for (int i=0; i<temp.length; i++)
        {
          headers.remove(temp[i][0]);
        }
        /*
        String digest = (String)headers.get(IAS2Headers.MIC);
        headers.remove(IAS2Headers.MIC);
        String auditFileName = (String)headers.get(IAS2Headers.AUDIT_FILE_NAME);
        headers.remove(IAS2Headers.AUDIT_FILE_NAME);
        */
        try
        {
          returnMsg = TransportServiceDelegate.send(commInfo, message);
        }
        finally
        {
          //NSL20060830 Refactor logic commented off below
          for (int i=0; i<temp.length; i++)
          {
            if (temp[i][1] != null)
            {
              headers.put(temp[i][0], temp[i][1]);
              if (returnMsg != null)
              {
                returnMsg.getMessageHeaders().put(temp[i][0], temp[i][1]);
              }
            }
          }
          /*
          headers.put(IAS2Headers.MIC, digest);
          headers.put(IAS2Headers.AUDIT_FILE_NAME, auditFileName);
          
          //NSL20060412 Need to set back to the returned message
        	if (returnMsg != null)
        	{
        		returnMsg.getMessageHeaders().put(IAS2Headers.MIC, digest);
        		returnMsg.getMessageHeaders().put(IAS2Headers.AUDIT_FILE_NAME, auditFileName);
        	}*/
        }
      }
      else
        returnMsg = TransportServiceDelegate.send(commInfo, message);
      returnMsg.setCommonHeaders(commonHeaders);
    }
    catch(Throwable t)
    {
      message.setCommonHeaders(commonHeaders);
      throw new TransportException(t);
    }
    return returnMsg;
  }

  public void sendFeedback(boolean status, String description, Message message)
  {
    try
    {
      Feedback feedback = new Feedback(status, description, message);
      MessageContext context = new MessageContext();
      context.setAttribute(IMessageContext.FEEDBACK_MESSAGE, feedback);
      MessageDispatcher.dispatchMessageToFeedbackListener(context);
    }
    catch (Exception ex)
    {
    	/**
    	 * @todo need review: warn or error?
    	 * temporary set to warn, reason: this is a return msg with some exception throwed.
    	 */
      ChannelLogger.warnLog(
        CLASS_NAME,
        "sendFeedback",
        "Cannot Create FeedBack Message ",
        ex);
    }
  }

  private void debugLog(String methodName, String message)
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "[" + methodName + "]",
      "[" + message + "]");
  }

  private FlowControlInfo getFlowControlInfo(
    FlowControlInfo info,
    Message message)
  {
    String eventId =
      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
    if (eventId != null)
    {
      if (FlowControlEventRegistry.getInstance().isSplitDisable(eventId))
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getFlowControlInfo()",
          "[disableSplit]");
        info.setIsSplit(false);
      }
      if (FlowControlEventRegistry.getInstance().isZipDisable(eventId))
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getFlowControlInfo()",
          "[disableZip]");
        info.setIsZip(false);
      }
    }
    return info;
  }
}
