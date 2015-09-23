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
 * File: GtasWsException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.transport.soap.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class GtasWsException extends ApplicationException
{

  /**
   * 
   */
  private static final long serialVersionUID = 6516323347463099398L;

  public GtasWsException(String msg)
  {
    super(msg);
  }

  public GtasWsException(String msg, Throwable th)
  {
    super(msg, th);
  }
  
  public GtasWsException(Throwable th)
  {
    super(th);
  }
}
