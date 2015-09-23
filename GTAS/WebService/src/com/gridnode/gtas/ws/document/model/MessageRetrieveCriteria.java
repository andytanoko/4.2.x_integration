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
 * File: MessageRetrieveCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 1, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document.model;

import java.util.Date;

/**
 * This class captures the list of criteria for the external client to retrieve
 * document from GT via web services. 
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class MessageRetrieveCriteria
{
  private String _docType;
  private long _fromCreationDate = 0; //The document creation date
  private long _toCreationDate = 0; //The document creation date
  private boolean _isRead = false;
  private String _sender; //the sender of the message
  private int _totalFetch; //indicate the total number of message to be fetched from GT
  private String _msgId;
  private String _docNumber;
  
  public MessageRetrieveCriteria()
  {
    
  }

  public MessageRetrieveCriteria(String docType, long fromCreationDate, long toCreationDate,
                                 boolean isRead, String sender, int totalFetch, String msgId,
                                 String docNumber)
  {
    setDocType(docType);
    setFromCreationDate(fromCreationDate);
    setToCreationDate(toCreationDate);
    setRead(isRead);
    setSender(sender);
    setTotalFetch(totalFetch);
    setMsgId(msgId);
    setDocNumber(docNumber);
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

  public long getFromCreationDate()
  {
    return _fromCreationDate;
  }

  public void setFromCreationDate(long fromCreationDate)
  {
    this._fromCreationDate = fromCreationDate;
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

  public long getToCreationDate()
  {
    return _toCreationDate;
  }

  public void setToCreationDate(long toCreationDate)
  {
    this._toCreationDate = toCreationDate;
  }

  public int getTotalFetch()
  {
    return _totalFetch;
  }

  public void setTotalFetch(int totalFetch)
  {
    this._totalFetch = totalFetch;
  }
  
  public String toString()
  {
    return "MessageRetrieveCriteria: fromCreationDate="+getFromCreationDate()+" toCreationDate="+getToCreationDate()+" isRead="+isRead()+
           " sender="+getSender()+" totalFetch="+getTotalFetch()+" msgId="+getMsgId()+" docNumber="+getDocNumber();
  }
  
}
