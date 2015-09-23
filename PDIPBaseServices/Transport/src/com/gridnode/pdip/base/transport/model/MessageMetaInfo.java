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
 * File: MessageMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.transport.model;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class MessageMetaInfo
{
  private String _docType;
  private String _receiver; //EG. One of the GT's OWN BE
  
  public MessageMetaInfo()
  {
    
  }
  
  public MessageMetaInfo(String docType, String receiver)
  {
    setDocType(docType);
    setReceiver(receiver);
  }

  public String getDocType()
  {
    return _docType;
  }

  public void setDocType(String docType)
  {
    this._docType = docType;
  }

  public String getReceiver()
  {
    return _receiver;
  }

  public void setReceiver(String receiver)
  {
    this._receiver = receiver;
  }
  
  public String toString()
  {
    return "MessageMetaInfo: docType="+getDocType()+" receiver="+getReceiver();
  }
}
