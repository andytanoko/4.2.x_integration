/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: URLRewriterImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-05-30     Andrew Hill         Changed to implement IURLRewriter in renderers package
 * 2002-11-07     Andrew Hill         Use appendParameter to append parameter for oc
 */
package com.gridnode.gtas.client.web.renderers;

import javax.servlet.http.HttpServletResponse;

import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.StaticWebUtils;

/**
 * A class the wraps a call to HttpServletResponse.encodeURL(String) to provide url session
 * encoding etc... when necessary
 */
public class URLRewriterImpl implements IURLRewriter
{
  private HttpServletResponse _response;
  private String _prefix = "";
  private OperationContext _operationContext = null;

  /**
   * Constructor that caches a reference to the HttpServletResponse.
   * When using this constructor the context path is not prepended to the url.
   * @param response the HttpServletResponse
   */
  public URLRewriterImpl(HttpServletResponse response)
  {
    _response = response;
  }

  /**
   * Constructor that caches a reference to the HttpServletResponse and the
   * OperationContext.
   * When using this constructor the context path is not prepended to the url.
   * OperationContext id will be added to query string if the rewrite(String, true) method is used
   * to rewrite the URL.
   * @param response the HttpServletResponse
   * @param opCon the OperationContext object
   */
  public URLRewriterImpl(HttpServletResponse response, OperationContext opCon)
  {
    _response = response;
    _operationContext = opCon;
  }

  /**
   * Constructor that caches a reference to the HttpServletResponse and takes a context path.
   * The context path will be prepended to the url.
   * @param response the HttpServletResponse
   * @param contextPath
   */
  public URLRewriterImpl(HttpServletResponse response, String contextPath)
  {
    _response = response;
    if(contextPath != null)
    {
      _prefix = contextPath;
    }
  }

  public URLRewriterImpl(HttpServletResponse response, String contextPath, OperationContext opCon)
  {
    _response = response;
    if(contextPath != null)
    {
      _prefix = contextPath;
    }
    _operationContext = opCon;
  }

  public void setOperationContext(OperationContext operationContext)
  {
    _operationContext = operationContext;
  }

  /**
   * Modifies url to provide session url encoding when necessary.
   * Calls the encodeURL(String) method in the HttpServletResponse and returns result.
   * @param String the URL to encode
   * @return the encoded URL
   */
  public String rewriteURL(String url)
  {
    return _response.encodeURL(_prefix + url);
  }

  /**
   * Given a URL will rewrite it to include session id and such like.
   * If such rewriting is not required will return url unchanged.
   * If an OperationContext is to be cleared set the flag to true to append its
   * id to the query for the OperationContext clearing filter
   * to clear the OperationContext.
   * @param url
   * @param clearOC true if operationContext to be removed from session scope
   * @return url
   */
  public String rewriteURL(String url, boolean clearOC)
  {
    String newUrl = rewriteURL(url);
    if( clearOC && (_operationContext != null))
    {
      return StaticWebUtils.addParameterToURL(newUrl,
        OperationContext.REMOVAL_PARAMETER,
        _operationContext.getOperationContextId());
      /*if(newUrl.indexOf("?") == -1)
      {
        return (newUrl + "?" + OperationContext.REMOVAL_PARAMETER
                + "=" + _operationContext.getOperationContextId());
      }
      else
      {
        return (newUrl + "&" + OperationContext.REMOVAL_PARAMETER
                + "=" + _operationContext.getOperationContextId());
      }*/
    }
    else
    {
      return newUrl;
    }
  }
}