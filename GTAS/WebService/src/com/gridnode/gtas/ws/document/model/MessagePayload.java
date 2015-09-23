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
 * File: MessageInfoContent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 1, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document.model;

import javax.activation.DataHandler;

/**
 * This class captures the payload including the content of the document, and its
 * meta info.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class MessagePayload
{
  private MessageInfo _msgInfo;
  private DataHandler _dataContentHandler;
  //private DataHandler[] _attachmentContentHandler; somehow the Axis will not expose it as DataHandler[]. The client will
  //                                                 interpret as OMElement[], and the value inside is always null!
  
  public MessagePayload()
  {
    
  }

  public MessagePayload(MessageInfo msgInfo, DataHandler dataHandler)
  {
    setMsgInfo(msgInfo);
    setDataContentHandler(dataHandler);
  }
  
  public DataHandler getDataContentHandler()
  {
    return _dataContentHandler;
  }

  public void setDataContentHandler(DataHandler dataHandler)
  {
    this._dataContentHandler = dataHandler;
  }

  public MessageInfo getMsgInfo()
  {
    return _msgInfo;
  }

  public void setMsgInfo(MessageInfo msgInfo)
  {
    this._msgInfo = msgInfo;
  }
  
//  public DataHandler[] getAttachmentContentHandler()
//  {
//    return _attachmentContentHandler;
//  }
//
//  public void setAttachmentContentHandler(DataHandler[] attachmentContentHandler)
//  {
//    this._attachmentContentHandler = attachmentContentHandler;
//  }

  public String toString()
  {
    return "MessagePayload: msgInfo="+getMsgInfo()+" dataContentHandler="+getDataContentHandler();
  }
}
