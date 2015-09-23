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
 * File: AxisMessageContextHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 3, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.transport.soap.service.handler;

import java.util.Vector;

import org.apache.axis2.context.MessageContext;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;

import com.gridnode.pdip.base.transport.helpers.TptLogger;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class AxisContextHandler implements ISoapContextHandler
{
  private static final String CLASS_NAME = "AxisMessageContextHandler";
  
  public String getAuthenticatedUsername()
  {
   
    String mn = "getAuthenticatedUser";
    Vector results = null;
    String username = null;
   
    TptLogger.debugLog(CLASS_NAME, mn, "getAuthenticatedUser");
    
    MessageContext inMessageContext  = MessageContext.getCurrentMessageContext();
    if(inMessageContext == null)
    {
      throw new IllegalArgumentException("Please initiate MessageContext!");
    }
    
    
    if ((results = (Vector) inMessageContext
        .getProperty(WSHandlerConstants.RECV_RESULTS)) == null) 
    {
      TptLogger.infoLog(CLASS_NAME, mn, "Can not get Security Result from MessageContext!");
      return null;
    } 
    else 
    {
      
      for (int i = 0; i < results.size(); i++) 
      {
        //Get hold of the WSHandlerResult instance
        WSHandlerResult rResult = (WSHandlerResult) results.get(i);
        Vector wsSecEngineResults = rResult.getResults();

        TptLogger.debugLog(CLASS_NAME, mn, "WsSecurityEngineResults");
        if(wsSecEngineResults != null && wsSecEngineResults.size() > 0)
        {
          WSSecurityEngineResult wser = (WSSecurityEngineResult)wsSecEngineResults.get(0);
          //Extract the principal
          WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal)
          wser.get(WSSecurityEngineResult.TAG_PRINCIPAL);

          //Get user/pass
          username = principal.getName();
          TptLogger.infoLog(CLASS_NAME, mn, "Authenticated web service user is: "+username);
        }
      }
      return username;
    }
  }

}
