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
 * File: GdocUpdateException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 8, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown when there is error in updating the gdoc
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class GdocUpdateException extends ApplicationException
{

  /**
   * 
   */
  private static final long serialVersionUID = -5933767079176516124L;

  public GdocUpdateException(String msg)
  {
    super(msg);
    
  }

  public GdocUpdateException(String msg, Throwable th)
  {
    super(msg, th);
  }
  
  public GdocUpdateException(Throwable th)
  {
    super(th);
  }
}
