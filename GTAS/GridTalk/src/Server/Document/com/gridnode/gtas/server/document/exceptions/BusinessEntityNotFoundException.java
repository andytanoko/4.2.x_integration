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
 * File: BusinessEntityNotFoundException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.document.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown if no BusinessEntity can be found given the search criteria.
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class BusinessEntityNotFoundException extends ApplicationException
{

  
  /**
   * 
   */
  private static final long serialVersionUID = -2951637453003764958L;

  public BusinessEntityNotFoundException(String msg)
  {
    super(msg);
  }
  
  public BusinessEntityNotFoundException(String msg, Throwable th)
  {
    super(msg, th);
  }
  
  public BusinessEntityNotFoundException(Throwable th)
  {
    super(th);
  }
}
