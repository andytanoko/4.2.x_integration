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
 * File: MessageInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 1, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document.model;

/**
 * This class captures the list info of a document in the GT. Such info will be make available
 * when the GT's TP query the message info via Web services.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class MessageInfo
{
  private String _msgId; //unique id to identify the message in GT, in this case it is gdoc Id
  private String _docType;
  private String _sender; //indicate the sender of the message (own business entity id)
  private boolean _isRead; //a flag indicate whether such message is downloaded before, may not be reflected correctly
  private long _creationTimestamp;
  private String _docNumber; //doc number found in the message content
  private String _docFilename; //the filename of the doc
  private boolean _isContainAttachment;
  
  public MessageInfo()
  {
    
  }

  public MessageInfo(String msgId, String docType, String sender, boolean isRead, long creationTimestamp,
                     String docNumber, String docFilename, boolean isContainAttachment)
  {
    setMsgId(msgId);
    setDocType(docType);
    setSender(sender);
    setRead(isRead);
    setCreationTimestamp(creationTimestamp);
    setDocNumber(docNumber);
    setDocFilename(docFilename);
    setContainAttachment(isContainAttachment);
  }
  
  public long getCreationTimestamp()
  {
    return _creationTimestamp;
  }

  public void setCreationTimestamp(long creationTimestamp)
  {
    this._creationTimestamp = creationTimestamp;
  }

  public String getDocFilename()
  {
    return _docFilename;
  }

  public void setDocFilename(String docFilename)
  {
    this._docFilename = docFilename;
  }

  public String getDocNumber()
  {
    return _docNumber;
  }

  public void setDocNumber(String docNumber)
  {
    this._docNumber = docNumber;
  }

  public String getDocType()
  {
    return _docType;
  }

  public void setDocType(String docType)
  {
    this._docType = docType;
  }

  public boolean isContainAttachment()
  {
    return _isContainAttachment;
  }

  public void setContainAttachment(boolean isContainAttachment)
  {
    this._isContainAttachment = isContainAttachment;
  }

  public boolean isRead()
  {
    return _isRead;
  }

  public void setRead(boolean isRead)
  {
    this._isRead = isRead;
  }

  public String getMsgId()
  {
    return _msgId;
  }

  public void setMsgId(String msgId)
  {
    this._msgId = msgId;
  }

  public String getSender()
  {
    return _sender;
  }

  public void setSender(String sender)
  {
    this._sender = sender;
  }
  
  public String toString()
  {
    return "MessageInfo: msgId="+getMsgId()+" docType="+getDocType()+" isRead="+isRead()+" creationTimestamp="+getCreationTimestamp()+
           " docNumber="+getDocNumber()+" docFilename="+getDocFilename()+" isContainAttachment="+isContainAttachment();
  }
  
}
