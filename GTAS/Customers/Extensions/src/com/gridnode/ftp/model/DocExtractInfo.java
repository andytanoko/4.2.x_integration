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
 * File: DocInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 20, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.ftp.model;

/**
 * This class captures the doc info we extracted out from the xml file via xpath.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class DocExtractInfo
{
  private String partner;
  private String docType;
  
  public DocExtractInfo()
  {
    
  }

  public String getDocType()
  {
    return docType;
  }

  public void setDocType(String docType)
  {
    this.docType = docType;
  }

  public String getPartner()
  {
    return partner;
  }

  public void setPartner(String partner)
  {
    this.partner = partner;
  }
  
  
}
