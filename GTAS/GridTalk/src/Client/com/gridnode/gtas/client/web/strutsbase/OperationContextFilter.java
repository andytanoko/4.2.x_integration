/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OperationContextFilter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.logging.*;

/**
 * Filter that will remove OperationContexts identified by the contents of the parameter
 * identified by OperationContext.REMOVAL_PARAMETER (currently "removeOC").
 */
public class OperationContextFilter implements Filter
{
  private static final Log _log = LogFactory.getLog(OperationContextFilter.class); // 20031209 DDJ

  public void init(FilterConfig filterConfig)
    throws javax.servlet.ServletException
  {
    if(_log.isInfoEnabled())
    {
      _log.info("OperationContextFilter initialised");
    }
  }

  public void destroy()
  {
    
  }

  public void doFilter( ServletRequest request,
                        ServletResponse response,
                        FilterChain filterChain)
    throws java.io.IOException, javax.servlet.ServletException
  {
    if(request instanceof HttpServletRequest)
    {
      HttpServletRequest httpRequest = (HttpServletRequest)request;
      String opConId = request.getParameter(OperationContext.REMOVAL_PARAMETER);
      if(opConId != null)
      {
        try
        {
          if(_log.isInfoEnabled())
          {
            _log.info("Removing OperationContext[" + opConId + "] from HttpSession");
          }
          OperationContext.removeOperationContext(httpRequest, opConId);
        }
        catch(OperationException oe)
        {
          
        }
        catch(Throwable t)
        {
          if(_log.isErrorEnabled())
          {
            _log.error("Error removing OperationContext[" + opConId + "]", t);
          }
        }
      }
    }
    filterChain.doFilter(request, response);
  }
}