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
 * File: ISoapContextHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 3, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.transport.soap.service.handler;

/**
 * This interface define the method that is used to retrieve the Context
 * info from the concrete vendor soap context implementation.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public interface ISoapContextHandler
{
  /**
   * Get the authenticated User login name
   * @return
   */
  public String getAuthenticatedUsername();
}
