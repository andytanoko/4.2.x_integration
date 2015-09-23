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
 * File: SoapContextFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 3, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.transport.soap.service.handler;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class SoapContextFactory
{
  public static final String AXIS_CONTEXT_HANDLER = "axisContextHandler";
  
  public static ISoapContextHandler getSoapContextHandler(String contextHandler)
  {
    return new AxisContextHandler();
  }
}
