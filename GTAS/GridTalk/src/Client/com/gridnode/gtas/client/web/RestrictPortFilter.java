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
 * 2003-08-14     Daniel D'Cotta      Created
 * 2003-08-19     Daniel D'Cotta      Modified to redirect to the secure port
 *                                    instead of returning a 404 error 
 */
package com.gridnode.gtas.client.web;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.logging.*;

import com.gridnode.gtas.client.utils.StaticUtils;

/**
 * Filter that will restrict access through all ports
 * except a single specified port and only allow 
 * /b2bi/rosettanet to be accessed via other ports  
 */
public class RestrictPortFilter implements Filter
{
  private static final Log _log = LogFactory.getLog(RestrictPortFilter.class);

  protected final static String PARAM_ENABLED = "enabled";     
  protected final static String PARAM_UNRESTRICTED_PORT = "unrestrictedPort";     

  protected boolean _enabled;
  protected int _unrestrictedPort;
  
  public void init(FilterConfig filterConfig)
    throws javax.servlet.ServletException
  {
    // initialise settings
    _enabled =          StaticUtils.primitiveBooleanValue(filterConfig.getInitParameter(PARAM_ENABLED));
    _unrestrictedPort = StaticUtils.primitiveIntValue(filterConfig.getInitParameter(PARAM_UNRESTRICTED_PORT));
    if(_unrestrictedPort <= 0)
    {
      _enabled = false;
    }

    if(_log.isInfoEnabled())
    {
      _log.info("RestrictPortFilter initialised");
      
      if(_log.isDebugEnabled())
      {
          _log.debug("enabled="          + _enabled);
          _log.debug("unrestrictedPort=" + _unrestrictedPort);
      }
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
    //boolean isRestrict = false; 
     
    int port = request.getServerPort();
    if(_enabled && port != _unrestrictedPort)
    {
      if(_log.isDebugEnabled())
      {
        _log.debug("port=" + port);
      }
      // request is from a restricted port 
      if(request instanceof HttpServletRequest)
      {
//        // return a 404 error (eg. resource not found)
//        HttpServletResponse httpResponse = (HttpServletResponse)response;
//        httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);

        // redirect to unrestricted port 
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        
        // assume port uses http and not https
        String url = "http://" + httpRequest.getServerName() + ":" + _unrestrictedPort + httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        if(queryString != null)
        {
          url = url + "?" + queryString;
        }
        url = httpResponse.encodeRedirectURL(url);
        if(_log.isInfoEnabled())
        {
          _log.info("Redirecting to URL: " + url);
        }
        httpResponse.sendRedirect(url);
      }
      else
      {
        // request may not be HTTP
        if(_log.isInfoEnabled())
        {
          _log.info("Unable to cast to HttpServletRequest, restricting access via Port: " + port);
        }
      }
    }
    else
    {
      // allow the request
      filterChain.doFilter(request, response);
    }
  }
}