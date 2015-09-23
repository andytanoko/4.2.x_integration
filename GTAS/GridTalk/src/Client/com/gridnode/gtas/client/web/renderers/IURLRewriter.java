/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TemplateRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

public interface IURLRewriter
{
  /**
   * Given a URL will rewrite it to include session id and such like.
   * If such rewriting is not required will return url unchanged.
   * @param url
   * @return url
   */
  public String rewriteURL(String url);

  /**
   * Given a URL will rewrite it to include session id and such like.
   * If such rewriting is not required will return url unchanged.
   * If an OperationContext is to be cleared set the flag to true to append its
   * id to the query for the OperationContext clearing filter (not implemented yet)
   * to clear the OperationContext.
   * @param url
   * @param clearOC true if operationContext to be removed from session scope
   * @return url
   */
  public String rewriteURL(String url, boolean clearOperationContext);
}