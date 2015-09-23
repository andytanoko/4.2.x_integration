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
 * File: GdocRetrieveException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 6, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.document.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * Thrown if encounter error when retrieving the GridDocument.
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class GdocRetrieveException extends ApplicationException
{

  /**
   * 
   */
  private static final long serialVersionUID = -7686342890382826414L;

  public GdocRetrieveException(String msg)
  {
    super(msg);
    
  }

  public GdocRetrieveException(Throwable th)
  {
    super(th);
  }
  
  public GdocRetrieveException(String msg, Throwable th)
  {
    super(msg, th);
  }
}
