/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: SoapMessageRetrieveService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 1, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document;

import java.util.ArrayList;

import javax.activation.DataHandler;

import com.gridnode.gtas.ws.BaseWebService;
import com.gridnode.gtas.ws.document.exception.GdocRetrieveException;
import com.gridnode.gtas.ws.document.exception.GdocUpdateException;
import com.gridnode.gtas.ws.document.handler.DocumentHandler;
import com.gridnode.gtas.ws.document.model.MessageInfo;
import com.gridnode.gtas.ws.document.model.MessagePayload;
import com.gridnode.gtas.ws.document.model.MessageRetrieveCriteria;
import com.gridnode.gtas.ws.helpers.Logger;
import com.gridnode.pdip.base.transport.soap.exception.GtasWsException;
import com.gridnode.pdip.base.transport.soap.service.handler.ISoapContextHandler;
import com.gridnode.pdip.base.transport.soap.service.handler.SoapContextFactory;

/**
 * <p>This class is the web service pojo class for retrieving the GT document and its meta data.</p>
 * 
 * <p>Currently we only allow the external client to retrieve the document that is resided in the Outbound
 * folder</p>
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class MessageRetrieveService extends BaseWebService
{
  private static String CLASS_NAME = "MessageRetrieveService";
  
  /**
   * Mark the given array of messageInfo as downloaded.
   * @param messageInfoList The meta data of the download message
   * @throws GtasWsException thrown if failed to mark the message as downloaded
   */
  public void markMessageAsDownloaded(MessageInfo[] messageInfoList) throws GtasWsException
  {
    String mn = "markMessageAsDownloaded3";
    Logger.infoLog(CLASS_NAME, mn, "Mark message as downloaded");
    
    ISoapContextHandler contextHandler = SoapContextFactory.getSoapContextHandler(SoapContextFactory.AXIS_CONTEXT_HANDLER); 
    String authenticatedUser = contextHandler.getAuthenticatedUsername();
    Logger.infoLog(CLASS_NAME, mn, "Authenticated user:"+authenticatedUser);
    
    if(messageInfoList == null || messageInfoList.length == 0)
    {
      return;
    }
    
    DocumentHandler docHandler = new DocumentHandler();
    try
    {
      docHandler.markGdocAsRead(messageInfoList, authenticatedUser);
    }
    catch (GdocRetrieveException e)
    {
      throw new GtasWsException("Failed to retrieve gdoc", e);
    }
    catch (GdocUpdateException e)
    {
      throw new GtasWsException("Failed to mark the message as read!", e);
    }
  }
  
  /**
   * Retrieve list of message info given the retrieval criteria
   * @param retrieveCriteria the criteria for retrieving the message
   * @return list of message info or empty array if none of the message matched the given criteria.
   * @throws GtasWsException thrown if failed to retrieve the message info from the underlying services.
   */
  public MessageInfo[] retrieveMessageInfos(MessageRetrieveCriteria retrieveCriteria) throws GtasWsException
  {
    
    String mn = "retrieveMessageInfos";
    Logger.infoLog(CLASS_NAME, mn, "retrieveMessageInfos "+retrieveCriteria);
    
    ISoapContextHandler contextHandler = SoapContextFactory.getSoapContextHandler(SoapContextFactory.AXIS_CONTEXT_HANDLER); 
    String authenticatedUser = contextHandler.getAuthenticatedUsername();
    Logger.infoLog(CLASS_NAME, mn, "Authenticated user:"+authenticatedUser);
    
    DocumentHandler docHandler = new DocumentHandler();
    ArrayList<MessageInfo> msgInfos = null;
    try
    {
      msgInfos = docHandler.getMessageInfos(retrieveCriteria, authenticatedUser);
    }
    catch(GdocRetrieveException e)
    {
      throw new GtasWsException("Can not fetch the Message Info given "+retrieveCriteria);
    }
    
    return msgInfos.toArray(new MessageInfo[]{});
    
  }
  
  /**
   * Retrieve the payload of the GT message given the msgInfo.
   * @param msgInfo The meta data of the message that reside in GT doc repo.
   * @return the message payload
   * @throws GtasWsException thrown if failed to retrieve the payload from the underlying services.
   */
  public MessagePayload retrieveMessagePayload(MessageInfo msgInfo) throws GtasWsException
  {
    
    String mn = "retrieveMessagePayload";
    Logger.infoLog(CLASS_NAME, mn, "retrieveMessageInfos");
    
    ISoapContextHandler contextHandler = SoapContextFactory.getSoapContextHandler(SoapContextFactory.AXIS_CONTEXT_HANDLER); 
    String authenticatedUser = contextHandler.getAuthenticatedUsername();
    Logger.infoLog(CLASS_NAME, mn, "Authenticated user:"+authenticatedUser);
    
    if(msgInfo == null)
    {
      return null;
    }
    
    String msgId = msgInfo.getMsgId();
    if(msgId == null || msgId.trim().length() == 0)
    {
      throw new IllegalArgumentException("Message Id can not be null or empty!");
    }
    else
    {
      DocumentHandler docHandler = new DocumentHandler();
      try
      {
        MessagePayload msgPayload = docHandler.getMessagePayload(msgInfo, authenticatedUser);
        return msgPayload;
      }
      catch (GdocRetrieveException e)
      {
        throw new GtasWsException("Can not retrieve MessagePayload given "+msgInfo);
      }
    }
    
  }

  /**
   * Retrieve the GT message associated attachment
   * @param msgInfo
   * @return the array of DataHandler
   * @throws GtasWsException thrown if the underlying service failed to retrieve the attachment
   */
  public DataHandler[] retrieveAttachment(MessageInfo msgInfo) throws GtasWsException
  {
    String mn = "retrieveAttachment";
    
    ISoapContextHandler contextHandler = SoapContextFactory.getSoapContextHandler(SoapContextFactory.AXIS_CONTEXT_HANDLER); 
    String authenticatedUser = contextHandler.getAuthenticatedUsername();
    Logger.infoLog(CLASS_NAME, mn, "Authenticated user:"+authenticatedUser);
    
    Logger.infoLog(CLASS_NAME, mn, "retrieveAttachment given: "+msgInfo);
    
    DocumentHandler docHandler = new DocumentHandler();
    DataHandler[] attHandlers = null;
    try
    {
      attHandlers = docHandler.getAttachment(msgInfo, authenticatedUser);
    }
    catch (GdocRetrieveException e)
    {
      throw new GtasWsException("Failed to retrieve attachment given "+msgInfo, e);
    }
    
    return attHandlers;
  }
  
  //TODO: more variant, convenient method for retrieving the payload
}
