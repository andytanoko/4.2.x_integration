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
 * 2002-06-13     Andrew Hill         Added second dummy rewrite method (OperationContext clearing)
 */
package com.gridnode.gtas.client.web.renderers;


/**
 * A class that implements IURLRewriter but performs no actual modification of urls passed to it.
 */
public class PassThruRewriter implements IURLRewriter
{ 

  public PassThruRewriter()
  {
  }

  /**
   * Does nothing to the passed url.
   * @param String the URL to encode
   * @return the encoded URL
   */
  public String rewriteURL(String url)
  {
    return url;
  }

  /**
   * Does nothing to the passed url.
   * @param url the URL to encode
   * @param ignored a parameter that is ignored (specifying to clear OperationContext)
   * @return the encoded URL
   */
  public String rewriteURL(String url, boolean ignored)
  {
    return rewriteURL(url);
  }
}